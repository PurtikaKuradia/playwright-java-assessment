package com.automationexercise.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import com.automationexercise.pages.SignupPage;

public class AccountSetupHelper {

    public static void ensureAccountExists(Page page) {

        String baseUrl  = TestDataLoader.getBaseUrl();
        String email    = TestDataLoader.getRegisterData("email");
        String password = TestDataLoader.getRegisterData("password");

        System.out.println("[AccountSetupHelper] Checking account: " + email);

        // Navigate
        page.navigate(baseUrl + "/login",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        SignupPage signupPage = new SignupPage(page);

        // Step 1: Signup attempt
        signupPage.enterSignupDetails(
                TestDataLoader.getRegisterData("name"),
                email
        );

        // Step 2: Check if already exists
        if (signupPage.isEmailAlreadyExists()) {
            System.out.println("[AccountSetupHelper] Account already exists. Skipping creation.");
            return;
        }

        // Step 3: Fill form
        signupPage.fillAccountForm(
                password,
                TestDataLoader.getRegisterData("dayOfBirth"),
                TestDataLoader.getRegisterData("monthOfBirth"),
                TestDataLoader.getRegisterData("yearOfBirth"),
                TestDataLoader.getRegisterData("firstName"),
                TestDataLoader.getRegisterData("lastName"),
                TestDataLoader.getRegisterData("company"),
                TestDataLoader.getRegisterData("address1"),
                TestDataLoader.getRegisterData("address2"),
                TestDataLoader.getRegisterData("country"),
                TestDataLoader.getRegisterData("state"),
                TestDataLoader.getRegisterData("city"),
                TestDataLoader.getRegisterData("zipcode"),
                TestDataLoader.getRegisterData("mobileNumber")
        );

        // Step 4: Create account
        signupPage.createAccount();

        // Step 5: Continue & logout
        signupPage.clickContinueIfVisible();
        signupPage.logoutIfVisible();

        System.out.println("[AccountSetupHelper] Account created successfully: " + email);
    }
}
