package com.automationexercise.tests;

import com.automationexercise.base.BaseClass;
import com.automationexercise.pages.ContactUsPage;
import com.automationexercise.pages.HomePage;
import com.automationexercise.utils.TestDataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TC04 - Contact Us Form
 * Covers automationexercise.com Test Case 6
 *
 * Steps:
 *  1. Verify homepage
 *  2. Click Contact Us
 *  3. Verify 'Get In Touch' heading
 *  4. Fill name, email, subject, message
 *  5. Click Submit (accepts JS confirm dialog automatically)
 *  6. Verify success message
 *  7. Click Home → verify back on homepage
 */
public class TC04_ContactUsFormTest extends BaseClass {

    @Test
    @DisplayName("TC6 - Submit Contact Us form successfully")
    public void testContactUsForm() {
        HomePage        homePage      = new HomePage(page);
        ContactUsPage   contactUsPage = new ContactUsPage(page);

        // Step 1: Verify homepage
        assertTrue(homePage.isHomePageLoaded(), "Homepage should be visible");

        // Step 2: Click Contact Us
        homePage.clickContactUs();

        // Step 3: Verify 'Get In Touch' heading
        assertTrue(contactUsPage.isContactUsPageVisible(),
            "'Get In Touch' heading should be visible");

        // Step 4: Fill the form
        contactUsPage.enterName(TestDataLoader.getContactUsData("name"));
        contactUsPage.enterEmail(TestDataLoader.getContactUsData("email"));
        contactUsPage.enterSubject(TestDataLoader.getContactUsData("subject"));
        contactUsPage.enterMessage(TestDataLoader.getContactUsData("message"));

        // Step 5: Submit — dialog handler registered inside clickSubmit()
        contactUsPage.clickSubmit();

        // Step 6: Verify success message
        assertTrue(contactUsPage.isSuccessMessageVisible(),
            "Success message should appear after submission");

        // Step 7: Click Home → verify homepage
        contactUsPage.clickHomeButton();
        assertTrue(homePage.isHomePageLoaded(),
            "Should be back on homepage after clicking Home");
    }
}
