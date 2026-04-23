package com.automationexercise.tests;

import com.automationexercise.base.BaseClass;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.LoginSignupPage;
import com.automationexercise.pages.RegisterPage;
import com.automationexercise.utils.TestDataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TC01 - Register User
 * Covers automationexercise.com Test Case 1
 *
 * Steps:
 *  1. Verify homepage
 *  2. Click Signup/Login
 *  3. Verify 'New User Signup!' is visible
 *  4. Enter name and email → click Signup
 *  5. Verify 'Enter Account Information'
 *  6. Fill all account details
 *  7. Click Create Account
 *  8. Verify 'Account Created!'
 *  9. Continue → verify 'Logged in as'
 * 10. Delete account → verify 'Account Deleted!'
 */
public class TC01_RegisterUserTest extends BaseClass {

    @Test
    @DisplayName("TC1 - Register a new user successfully")
    public void testRegisterUser() {
        HomePage        homePage      = new HomePage(page);
        LoginSignupPage loginPage     = new LoginSignupPage(page);
        RegisterPage    registerPage  = new RegisterPage(page);

        // Step 1: Verify homepage
        assertTrue(homePage.isHomePageLoaded(),
            "Homepage logo should be visible");

        // Step 2: Click Signup/Login
        homePage.clickSignupLogin();

        // Step 3: Verify 'New User Signup!'
        assertTrue(loginPage.isSignupSectionVisible(),
            "'New User Signup!' heading should be visible");

        // Step 4: Enter name and email → click Signup
        loginPage.initiateSignup(
            TestDataLoader.getRegisterData("name"),
            TestDataLoader.getRegisterData("email")
        );

        // Step 5: Verify 'Enter Account Information'
        assertTrue(registerPage.isAccountInfoPageVisible(),
            "'Enter Account Information' heading should be visible");

        // Step 6: Fill all details
        registerPage.selectTitleMr();
        registerPage.enterPassword(TestDataLoader.getRegisterData("password"));
        registerPage.selectDayOfBirth(TestDataLoader.getRegisterData("dayOfBirth"));
        registerPage.selectMonthOfBirth(TestDataLoader.getRegisterData("monthOfBirth"));
        registerPage.selectYearOfBirth(TestDataLoader.getRegisterData("yearOfBirth"));
        registerPage.checkNewsletter();
        registerPage.checkOptIn();
        registerPage.enterFirstName(TestDataLoader.getRegisterData("firstName"));
        registerPage.enterLastName(TestDataLoader.getRegisterData("lastName"));
        registerPage.enterCompany(TestDataLoader.getRegisterData("company"));
        registerPage.enterAddress1(TestDataLoader.getRegisterData("address1"));
        registerPage.enterAddress2(TestDataLoader.getRegisterData("address2"));
        registerPage.selectCountry(TestDataLoader.getRegisterData("country"));
        registerPage.enterState(TestDataLoader.getRegisterData("state"));
        registerPage.enterCity(TestDataLoader.getRegisterData("city"));
        registerPage.enterZipcode(TestDataLoader.getRegisterData("zipcode"));
        registerPage.enterMobileNumber(TestDataLoader.getRegisterData("mobileNumber"));

        // Step 7: Create Account
        registerPage.clickCreateAccount();

        // Step 8: Verify 'Account Created!'
        assertTrue(registerPage.isAccountCreatedVisible(),
            "'Account Created!' message should be visible");

        // Step 9: Continue → verify logged in
        registerPage.clickContinue();
        assertTrue(isLoggedIn(), "Should be logged in after registration");

        // Step 10: Delete account → verify 'Account Deleted!'
        registerPage.deleteAccount();
        assertTrue(registerPage.isAccountDeletedVisible(),
            "'Account Deleted!' message should be visible");
    }
}
