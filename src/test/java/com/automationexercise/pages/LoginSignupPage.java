package com.automationexercise.pages;

import com.automationexercise.common.CommonActions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

/**
 * LoginSignupPage - Page Object for /login
 * Contains both the Login form and the New User Signup form.
 */
public class LoginSignupPage extends CommonActions {

    // Login form
    private static final String LOGIN_HEADING        = "h2:has-text('Login to your account')";
    private static final String LOGIN_EMAIL_INPUT    = "input[data-qa='login-email']";
    private static final String LOGIN_PASSWORD_INPUT = "input[data-qa='login-password']";
    private static final String LOGIN_BUTTON         = "button[data-qa='login-button']";
    private static final String LOGIN_ERROR_MSG      = "p:has-text('Your email or password is incorrect!')";

    // Signup form
    private static final String SIGNUP_HEADING     = "h2:has-text('New User Signup!')";
    private static final String SIGNUP_NAME_INPUT  = "input[data-qa='signup-name']";
    private static final String SIGNUP_EMAIL_INPUT = "input[data-qa='signup-email']";
    private static final String SIGNUP_BUTTON      = "button[data-qa='signup-button']";
    private static final String SIGNUP_ERROR_MSG   = "p:has-text('Email Address already exist!')";

    public LoginSignupPage(Page page) {
        super(page);
    }

    // ── Login ──────────────────────────────────────────────────

    /** Returns true if 'Login to your account' heading is visible */
    public boolean isLoginPageVisible() {
        page.waitForSelector(LOGIN_HEADING);
        return page.locator(LOGIN_HEADING).isVisible();
    }

    /**
     * Fill and submit login form.
     * Does NOT assert result — caller verifies what happens next.
     */
    public void loginUser(String email, String password) {
        page.waitForSelector(LOGIN_EMAIL_INPUT);
        page.locator(LOGIN_EMAIL_INPUT).fill(email);
        page.locator(LOGIN_PASSWORD_INPUT).fill(password);
        page.locator(LOGIN_BUTTON).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /** Returns true if login error message is visible */
    public boolean isLoginErrorVisible() {
        try {
            page.waitForSelector(LOGIN_ERROR_MSG,
                new Page.WaitForSelectorOptions().setTimeout(8000));
            return page.locator(LOGIN_ERROR_MSG).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns the text of the login error message */
    public String getLoginErrorMessage() {
        return page.locator(LOGIN_ERROR_MSG).innerText();
    }

    // ── Signup ─────────────────────────────────────────────────

    /** Returns true if 'New User Signup!' heading is visible */
    public boolean isSignupSectionVisible() {
        page.waitForSelector(SIGNUP_HEADING);
        return page.locator(SIGNUP_HEADING).isVisible();
    }

    /**
     * Fill the signup form (name + email) and click Signup button.
     */
    public void initiateSignup(String name, String email) {
        page.locator(SIGNUP_NAME_INPUT).fill(name);
        page.locator(SIGNUP_EMAIL_INPUT).fill(email);
        page.locator(SIGNUP_BUTTON).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /** Returns true if 'Email Address already exist!' error is visible */
    public boolean isSignupEmailErrorVisible() {
        return page.locator(SIGNUP_ERROR_MSG).isVisible();
    }
}
