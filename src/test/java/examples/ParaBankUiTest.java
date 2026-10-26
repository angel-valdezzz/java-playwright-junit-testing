package examples;
    import org.junit.jupiter.api.*;
    import com.microsoft.playwright.*;
    import java.nio.file.*;
    class ParaBankUiTest {
      Playwright playwright; Browser browser; Page page; Bank bank;
      @BeforeEach void setup() { playwright=Playwright.create(); browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true)); page=browser.newPage(); bank=new Bank(page); }
      @AfterEach void teardown() throws Exception { try { if(page!=null) { Files.createDirectories(Path.of("reports")); page.screenshot(new Page.ScreenshotOptions().setPath(Path.of("reports",java.util.UUID.randomUUID()+".png"))); } } finally { if(browser!=null) browser.close(); if(playwright!=null) playwright.close(); } }

    @Test public void registration() { bank.register(); }
    @Test public void logoutLogin() { bank.register(); bank.link("Log Out"); bank.login(); bank.text("#rightPanel h1","Accounts Overview"); }
    @Test public void requiredFields() { bank.home(); bank.link("Register"); bank.submit(); bank.text("[id='customer.firstName.errors']","First name is required."); }
    @Test public void passwordMismatch() { bank.form(true); bank.submit(); bank.text("[id='repeatedPassword.errors']","Passwords did not match."); }
    @Test public void invalidLogin() { bank.home(); bank.login(); bank.text("#rightPanel .error","The username and password could not be verified."); }
    @Test public void savingsAccount() { bank.register(); bank.savings(); }
}
