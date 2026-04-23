package com.automationexercise.pages;

import com.automationexercise.common.CommonActions;
import com.microsoft.playwright.Dialog;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * ContactUsPage - Page Object for /contact_us
 *
 * FIX 1: h2:has-text('Get In Touch') — avoids strict mode violation
 *         (old selector "h2.title.text-center" matched 3 elements).
 *
 * FIX 2: page.once() does NOT exist in Playwright Java 1.44.
 *         Use a boolean flag with page.onDialog() to accept only the
 *         first dialog and ignore subsequent ones.
 */
public class ContactUsPage extends CommonActions {

    private static final String GET_IN_TOUCH_HEADING = "h2:has-text('Get In Touch')";
    private static final String NAME_INPUT           = "input[data-qa='name']";
    private static final String EMAIL_INPUT          = "input[data-qa='email']";
    private static final String SUBJECT_INPUT        = "input[data-qa='subject']";
    private static final String MESSAGE_INPUT        = "textarea[data-qa='message']";
    private static final String SUBMIT_BUTTON        = "input[data-qa='submit-button']";
    private static final String SUCCESS_MESSAGE =
        "div.status.alert.alert-success:has-text('Success! Your details have been submitted successfully.')";
    private static final String HOME_BUTTON          = "a.btn.btn-success";

    public ContactUsPage(Page page) {
        super(page);
    }

    public boolean isContactUsPageVisible() {
        page.waitForSelector(GET_IN_TOUCH_HEADING);
        return page.locator(GET_IN_TOUCH_HEADING).isVisible();
    }

    public void enterName(String name)       { page.locator(NAME_INPUT).fill(name); }
    public void enterEmail(String email)     { page.locator(EMAIL_INPUT).fill(email); }
    public void enterSubject(String subject) { page.locator(SUBJECT_INPUT).fill(subject); }
    public void enterMessage(String message) { page.locator(MESSAGE_INPUT).fill(message); }

    /**
     * Submit the contact form and safely handle the JS confirm.
     *
     * Some runs can dispatch dialog callbacks after the dialog is already
     * resolved. We swallow that race to keep test flow stable.
     */
    public void clickSubmit() {
        AtomicBoolean dialogAccepted = new AtomicBoolean(false);
        Consumer<Dialog> dialogHandler = dialog -> {
            try {
                System.out.println("[ContactUs] Accepting dialog: " + dialog.message());
                dialog.accept();
                dialogAccepted.set(true);
            } catch (Exception ignored) {
                // Ignore stale callback race from Chromium protocol.
            }
        };

        page.onDialog(dialogHandler);
        try {
            page.locator(SUBMIT_BUTTON).click();
            page.waitForCondition(dialogAccepted::get,
                new Page.WaitForConditionOptions().setTimeout(10000));
        } finally {
            page.offDialog(dialogHandler);
        }
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public boolean isSuccessMessageVisible() {
        page.waitForSelector(SUCCESS_MESSAGE);
        return page.locator(SUCCESS_MESSAGE).isVisible();
    }

    public void clickHomeButton() {
        page.locator(HOME_BUTTON).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }
}
