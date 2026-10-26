package examples;
    import java.util.*;
    import com.microsoft.playwright.*;
    import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
    public class Bank {
      final Page page; final String username="qa"+UUID.randomUUID().toString().replace("-",""); final String password="DemoPass42!";
      Bank(Page page) { this.page=page; page.setDefaultTimeout(20000); }
      Locator element(String id) { return page.locator("[id='"+id+"']"); }
      void home() { page.navigate(System.getenv().getOrDefault("PARABANK_URL","https://parabank.parasoft.com/parabank")+"/index.htm"); }
      void link(String text) { page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,new Page.GetByRoleOptions().setName(text).setExact(true)).click(); }
      void text(String selector,String expected) { assertThat(page.locator(selector)).hasText(expected); }
      void form(boolean mismatch) { home(); link("Register");
    element("customer.firstName").fill("Automation");element("customer.lastName").fill("Example");element("customer.address.street").fill("123 Fictional Street");element("customer.address.city").fill("Demo City");element("customer.address.state").fill("CA");element("customer.address.zipCode").fill("90210");element("customer.phoneNumber").fill("5551234567");element("customer.ssn").fill("123456789");
        element("customer.username").fill(username); element("customer.password").fill(password); element("repeatedPassword").fill(mismatch?"Different42!":password);
      }
      void submit() { page.locator("#customerForm input[type=submit]").click(); }
      void register() { form(false); submit(); text("#rightPanel h1","Welcome "+username); }
      void login() { page.locator("input[name=username]").fill(username); page.locator("input[name=password]").fill(password); page.locator("#loginPanel input[type=submit]").click(); }
      void savings() { link("Open New Account"); assertThat(page.locator("#fromAccountId option").first()).hasAttribute("value",java.util.regex.Pattern.compile("[0-9]+")); element("type").selectOption("1"); page.locator("#openAccountForm input.button").click(); assertThat(element("newAccountId")).hasText(java.util.regex.Pattern.compile("^[0-9]+$")); String id=element("newAccountId").innerText(); element("newAccountId").click(); text("#accountId",id); text("#accountType","SAVINGS"); }
    }
