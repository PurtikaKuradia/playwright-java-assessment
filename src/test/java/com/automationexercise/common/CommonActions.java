package com.automationexercise.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * CommonActions
 * Contains all reusable Playwright actions shared across every test case.
 * KEY FIX: navigateTo() uses DOMCONTENTLOADED (not "load") to prevent
 * timeouts caused by ad scripts on automationexercise.com.
 */
public class CommonActions {

    protected Page page;

    public CommonActions(Page page) {
        this.page = page;
    }

    /** Navigate to URL — uses DOMCONTENTLOADED to avoid ad-script timeouts */
    public void navigateTo(String url) {
        page.navigate(url,
            new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }

    /** Click element by CSS/XPath selector */
    public void clickElement(String selector) {
        page.locator(selector).click();
    }

    /** Click a Locator object */
    public void clickLocator(Locator locator) {
        locator.click();
    }

    /** Fill an input field */
    public void typeText(String selector, String text) {
        page.locator(selector).fill(text);
    }

    /** Clear then fill an input field */
    public void clearAndType(String selector, String text) {
        page.locator(selector).clear();
        page.locator(selector).fill(text);
    }

    /** Select dropdown option by visible label */
    public void selectDropdownByLabel(String selector, String label) {
        page.locator(selector).selectOption(new SelectOption().setLabel(label));
    }

    /** Select dropdown option by value attribute */
    public void selectDropdownByValue(String selector, String value) {
        page.locator(selector).selectOption(value);
    }

    /** Get inner text of element */
    public String getElementText(String selector) {
        return page.locator(selector).innerText();
    }

    /** Check if element is visible */
    public boolean isElementVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    /** Check if element is enabled */
    public boolean isElementEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }

    /** Wait for element to become visible */
    public void waitForElement(String selector) {
        page.locator(selector).waitFor(
            new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    /** Scroll element into view */
    public void scrollToElement(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    /** Upload file to a file input */
    public void uploadFile(String selector, String filePath) {
        page.locator(selector).setInputFiles(java.nio.file.Paths.get(filePath));
    }

    /** Take screenshot and save to path */
    public void takeScreenshot(String filePath) {
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(java.nio.file.Paths.get(filePath)));
    }

    /** Get current page title */
    public String getPageTitle() {
        return page.title();
    }

    /** Get current URL */
    public String getCurrentUrl() {
        return page.url();
    }

    /** Press keyboard key on element */
    public void pressKey(String selector, String key) {
        page.locator(selector).press(key);
    }

    /** Hover over element */
    public void hoverElement(String selector) {
        page.locator(selector).hover();
    }

    /** Get HTML attribute value */
    public String getAttribute(String selector, String attribute) {
        return page.locator(selector).getAttribute(attribute);
    }

    /** Wait for page to reach DOMCONTENTLOADED state */
    public void waitForNavigation() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /** Check a checkbox if not already checked */
    public void checkCheckbox(String selector) {
        if (!page.locator(selector).isChecked()) {
            page.locator(selector).check();
        }
    }

    /** Wait for milliseconds (use sparingly) */
    public void waitForMs(int milliseconds) {
        page.waitForTimeout(milliseconds);
    }
}
