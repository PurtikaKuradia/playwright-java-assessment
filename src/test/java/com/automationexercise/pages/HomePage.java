package com.automationexercise.pages;

import com.automationexercise.common.CommonActions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

/**
 * HomePage - Page Object for https://automationexercise.com
 */
public class HomePage extends CommonActions {

    private static final String LOGO              = "img[src='/static/images/home/logo.png']";
    private static final String SIGNUP_LOGIN_LINK = "a[href='/login']";
    private static final String LOGOUT_LINK       = "a[href='/logout']";
    private static final String CONTACT_US_LINK   = "a[href='/contact_us']";
    private static final String PRODUCTS_LINK     = "a[href='/products']";
    private static final String CART_LINK         = "a[href='/view_cart']";

    public HomePage(Page page) {
        super(page);
    }

    /** Returns true if the site logo is visible */
    public boolean isHomePageLoaded() {
        page.waitForSelector(LOGO);
        return page.locator(LOGO).isVisible();
    }

    /** Click 'Signup / Login' navbar link */
    public void clickSignupLogin() {
        page.locator(SIGNUP_LOGIN_LINK).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /** Click 'Contact us' navbar link */
    public void clickContactUs() {
        page.locator(CONTACT_US_LINK).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /** Click 'Products' navbar link */
    public void clickProducts() {
        page.locator(PRODUCTS_LINK).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /** Click 'Cart' navbar link */
    public void clickCart() {
        page.locator(CART_LINK).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /**
     * Returns true if the user is logged in.
     * Uses logout link visibility as the reliable indicator.
     */
    public boolean isLoggedInAsVisible() {
        return page.locator(LOGOUT_LINK).isVisible();
    }
}
