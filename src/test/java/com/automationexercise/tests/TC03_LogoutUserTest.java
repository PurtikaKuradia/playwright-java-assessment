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
 * TC03 - Logout User
 * Covers automationexercise.com Test Case 4
 *
 * KEY FIX: @BeforeEach ensureAccountExists() guarantees the account
 * exists before BaseClass.login() is called.
 */
public class TC03_LogoutUserTest extends BaseClass {

    @BeforeEach
    public void ensureAccountExists() {
        AccountSetupHelper.ensureAccountExists(page);
        navigateTo(TestDataLoader.getBaseUrl());
    }

    @Test
    @DisplayName("TC4 - Logout from active session")
    public void testLogoutUser() {
        HomePage        homePage  = new HomePage(page);
        LoginSignupPage loginPage = new LoginSignupPage(page);

        // Verify homepage
        assertTrue(homePage.isHomePageLoaded(), "Homepage should be visible");

        // Login using BaseClass.login()
        login();

        // Verify logged in
        assertTrue(isLoggedIn(), "Should be logged in after login()");

        // Logout using BaseClass.logout()
        logout();

        // Verify redirected to login page and not logged in
        assertTrue(loginPage.isLoginPageVisible(),
            "Should be on login page after logout");
        assertFalse(isLoggedIn(),
            "Logout link should not be visible after logout");
    }
}
