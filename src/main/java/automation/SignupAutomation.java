package automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SignupAutomation {

    static WebDriver driver;
    static WebDriverWait wait;

    //  TEST DATA
    static String RUN_ID = String.valueOf(System.currentTimeMillis());

    static String firstName = "Test" + RUN_ID.substring(8);
    static String lastName  = "User" + RUN_ID.substring(6);

    static final String BASE_EMAIL = "rabindrakumarmahato00@gmail.com";
    static String email = BASE_EMAIL.replace("@", "+" + RUN_ID + "@");

    static String phone = "9" + RUN_ID.substring(3, 12);
    static String agencyName  = "Agency_" + RUN_ID.substring(7);
    static String agencyEmail = "agency" + RUN_ID + "@gmail.com";
    static String website     = "https://agency" + RUN_ID.substring(7) + ".netlify.app";
    static String registration = "REG-" + RUN_ID;

    //  MAIN
    public static void main(String[] args) {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            driver.manage().window().maximize();
            driver.get("https://authorized-partner.vercel.app/");

            openSignup();
            accountSetupPage();
            otpVerification();

            agencyDetailsPage();
            professionalExperiencePage();
            verificationPage();

            System.out.println(" FULL SIGNUP COMPLETED SUCCESSFULLY");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  PAGE 0
    static void openSignup() {
        jsClick("//button[contains(text(),'Sign Up')]");
    }

    //  PAGE 1
    static void accountSetupPage() {

        type("//label[text()='First Name']/following::input[1]", firstName);
        type("//label[text()='Last Name']/following::input[1]", lastName);
        type("//label[text()='Email Address']/following::input[1]", email);
        type("//label[text()='Phone Number']/following::input[1]", phone);

        List<WebElement> pwds = wait.until(
                d -> d.findElements(By.xpath("//input[@type='password']"))
        );
        pwds.get(0).sendKeys("Test@1234");
        pwds.get(1).sendKeys("Test@1234");

        jsClick("//button[text()='Next']");
    }

    // ================= OTP =================
    static void otpVerification() {

        System.out.println(" OTP sent to: " + email);
        System.out.println(" Please enter OTP manually...");

        // Wait until OTP input disappears
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//input[contains(@placeholder,'OTP')]")
        ));

        // Confirm navigation to Agency page
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Name']")
        ));

        System.out.println(" OTP VERIFIED");
    }

    // ================= PAGE 2 =================
    static void agencyDetailsPage() {

        type("//label[text()='Name']/following::input[1]", agencyName);
        type("//label[text()='Role in Agency']/following::input[1]", "Developer");

        WebElement emailField =
                driver.findElement(By.xpath("//label[text()='Email Address']/following::input[1]"));
        emailField.clear();
        emailField.sendKeys(agencyEmail);

        type("//label[text()='Website']/following::input[1]", website);
        type("//label[text()='Address']/following::input[1]", "Nepal");

        //  COUNTRY DROPDOWN (SCOPED & SAFE)
        WebElement countryDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//label[text()='Country']/following::div[@role='button'][1]")
                )
        );
        jsClick(countryDropdown);

        jsClick("//li[text()='Australia']");

        jsClick("//button[text()='Next']");

        System.out.println(" Agency page completed");
    }

    //  PAGE 3
    static void professionalExperiencePage() {

        // Wait until page header loads
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Professional')]")
        ));

        // EXPERIENCE DROPDOWN
        WebElement experienceDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//label[contains(text(),'Years of Experience')]/following::div[@role='button'][1]")
                )
        );
        jsClick(experienceDropdown);

        // Select option
        jsClick("//li[@role='option' and contains(text(),'4 years')]");

        //NUMBER OF STUDENTS
        WebElement students = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[@type='number']")
                )
        );
        students.clear();
        students.sendKeys("50");

        // FOCUS AREA
        WebElement focusArea = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//textarea")
                )
        );
        focusArea.clear();
        focusArea.sendKeys("Undergraduate admissions to Australia");

        // SUCCESS RATE
        WebElement successRate = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[contains(@placeholder,'%')]")
                )
        );
        successRate.clear();
        successRate.sendKeys("85");

        // ================= SERVICES CHECKBOXES =================
        jsClick("//label[contains(text(),'Career Counseling')]");
        jsClick("//label[contains(text(),'Admission Applications')]");

        //  NEXT
        jsClick("//button[text()='Next']");

        System.out.println(" Professional Experience page filled successfully");
    }


    // PAGE 4
    static void verificationPage() {

        type("//label[contains(text(),'Business Registration Number')]/following::input[1]",
                registration);

        WebElement prefCountry = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//label[contains(text(),'Preferred Countries')]/following::div[@role='button'][1]")
                )
        );
        jsClick(prefCountry);

        jsClick("//li[text()='Australia']");

        jsClick("//label[text()='Universities']");
        jsClick("//label[text()='Colleges']");

        type("//label[contains(text(),'Certification')]/following::input[1]",
                "ICEF Certified Education Agent");

        jsClick("//button[text()='Submit']");

        System.out.println("Verification page submitted");
    }

    //HELPERS
    static void jsClick(String xpath) {
        WebElement el = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});arguments[0].click();", el);
    }

    static void jsClick(WebElement el) {
        wait.until(ExpectedConditions.elementToBeClickable(el));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});arguments[0].click();", el);
    }

    static void type(String xpath, String value) {
        WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        el.clear();
        el.sendKeys(value);
    }
}
