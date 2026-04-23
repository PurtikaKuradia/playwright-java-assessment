package com.automationexercise.pages;

import com.automationexercise.common.CommonActions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

/**
 * AuthPomPage
 * Centralized Page Object for authentication-related locators/actions
 * used by BaseClass (login + logout checks).
 */
public class AuthPomPage extends CommonActions {

    private static final String LOGIN_EMAIL_INPUT    = "input[data-qa='login-email']";
    private static final String LOGIN_PASSWORD_INPUT = "input[data-qa='login-password']";
    private static final String LOGIN_BUTTON         = "button[data-qa='login-button']";
    private static final String LOGOUT_LINK          = "a[href='/logout']";

    public AuthPomPage(Page page) {
        super(page);
    }

    public void login(String email, String password) {
        page.waitForSelector(LOGIN_EMAIL_INPUT);
        page.locator(LOGIN_EMAIL_INPUT).fill(email);
        page.locator(LOGIN_PASSWORD_INPUT).fill(password);
        page.locator(LOGIN_BUTTON).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public void clickLogout() {
        page.waitForSelector(LOGOUT_LINK);
        page.locator(LOGOUT_LINK).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public void waitForLogoutVisible(double timeoutMs) {
        page.waitForSelector(LOGOUT_LINK,
            new Page.WaitForSelectorOptions().setTimeout(timeoutMs));
    }

    public boolean isLogoutVisible() {
        return page.locator(LOGOUT_LINK).isVisible();
    }

    public String getLoginEmailInputSelector() {
        return LOGIN_EMAIL_INPUT;
    }
}
