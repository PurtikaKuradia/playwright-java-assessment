package com.automationexercise.tests;

import com.automationexercise.base.BaseClass;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.LoginSignupPage;
import com.automationexercise.utils.AccountSetupHelper;
import com.automationexercise.utils.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TC02 - Login User (Valid + Invalid)
 * Covers automationexercise.com Test Cases 2 and 3
 *
 * KEY FIX: @BeforeEach ensureAccountExists() creates the test account
 * if it doesn't exist yet — TC02 and TC03 need a pre-existing account.
 * TC01 deletes the account at the end, so ensureAccountExists() recreates it.
 */
public class TC02_LoginUserTest extends BaseClass {

    /**
     * Runs after BaseClass.setUp() (browser is already open).
     * Ensures the shared test account exists before login tests run.
     */
    @BeforeEach
    public void ensureAccountExists() {
        AccountSetupHelper.ensureAccountExists(page);
        // Navigate back to homepage after setup
        navigateTo(TestDataLoader.getBaseUrl());
    }

    @Test
    @DisplayName("TC2 - Login with valid credentials")
    public void testLoginWithValidCredentials() {
        HomePage        homePage  = new HomePage(page);
        LoginSignupPage loginPage = new LoginSignupPage(page);

        // Verify homepage
        assertTrue(homePage.isHomePageLoaded(), "Homepage should be visible");

        // Click Signup/Login
        homePage.clickSignupLogin();
        assertTrue(loginPage.isLoginPageVisible(), "Login page should be visible");

        // Login with valid credentials
        loginPage.loginUser(
            TestDataLoader.getLoginData("email"),
            TestDataLoader.getLoginData("password")
        );

        // Verify logged in (logout link visible)
        page.waitForSelector("a[href='/logout']");
        assertTrue(isLoggedIn(), "Should be logged in with valid credentials");

        // Logout and verify back on login page
        logout();
        assertTrue(loginPage.isLoginPageVisible(), "Should be on login page after logout");
    }

    @Test
    @DisplayName("TC3 - Login with invalid credentials shows error")
    public void testLoginWithInvalidCredentials() {
        HomePage        homePage  = new HomePage(page);
        LoginSignupPage loginPage = new LoginSignupPage(page);

        homePage.clickSignupLogin();
        assertTrue(loginPage.isLoginPageVisible(), "Login page should be visible");

        // Login with wrong credentials
        loginPage.loginUser(
            TestDataLoader.getInvalidLoginData("email"),
            TestDataLoader.getInvalidLoginData("password")
        );

        // Error message must appear — no redirect on failed login
        assertTrue(loginPage.isLoginErrorVisible(),
            "Error message should appear for invalid credentials");
        assertTrue(
            loginPage.getLoginErrorMessage().contains("Your email or password is incorrect!"),
            "Error message text should match"
        );
    }
}
