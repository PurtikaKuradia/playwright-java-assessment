package com.automationexercise.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;

public class SignupPage {

    private Page page;

    public SignupPage(Page page) {
        this.page = page;
    }

    // Locators
    private String signupName = "input[data-qa='signup-name']";
    private String signupEmail = "input[data-qa='signup-email']";
    private String signupBtn = "button[data-qa='signup-button']";
    private String emailExistsMsg = "p:has-text('Email Address already exist!')";

    private String genderRadio = "#id_gender1";
    private String password = "input[data-qa='password']";
    private String days = "select[data-qa='days']";
    private String months = "select[data-qa='months']";
    private String years = "select[data-qa='years']";
    private String firstName = "input[data-qa='first_name']";
    private String lastName = "input[data-qa='last_name']";
    private String company = "input[data-qa='company']";
    private String address1 = "input[data-qa='address']";
    private String address2 = "input[data-qa='address2']";
    private String country = "select[data-qa='country']";
    private String state = "input[data-qa='state']";
    private String city = "input[data-qa='city']";
    private String zipcode = "input[data-qa='zipcode']";
    private String mobile = "input[data-qa='mobile_number']";

    private String createAccountBtn = "button[data-qa='create-account']";
    private String continueBtn = "a[data-qa='continue-button']";
    private String logoutBtn = "a[href='/logout']";

    // Actions

    public void enterSignupDetails(String name, String email) {
        page.fill(signupName, name);
        page.fill(signupEmail, email);
        page.click(signupBtn);
    }

    public boolean isEmailAlreadyExists() {
        return page.locator(emailExistsMsg).isVisible();
    }

    public void fillAccountForm(String pwd, String day, String month, String year,
                                String fName, String lName, String comp,
                                String addr1, String addr2Val, String countryVal,
                                String stateVal, String cityVal, String zip, String mobileNum) {

        page.click(genderRadio);
        page.fill(password, pwd);

        page.selectOption(days, day);
        page.selectOption(months, month);
        page.selectOption(years, year);

        page.fill(firstName, fName);
        page.fill(lastName, lName);
        page.fill(company, comp);
        page.fill(address1, addr1);
        page.fill(address2, addr2Val);

        page.selectOption(country, new SelectOption().setLabel(countryVal));

        page.fill(state, stateVal);
        page.fill(city, cityVal);
        page.fill(zipcode, zip);
        page.fill(mobile, mobileNum);
    }

    public void createAccount() {
        page.click(createAccountBtn);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public void clickContinueIfVisible() {
        if (page.locator(continueBtn).isVisible()) {
            page.click(continueBtn);
        }
    }

    public void logoutIfVisible() {
        if (page.locator(logoutBtn).isVisible()) {
            page.click(logoutBtn);
        }
    }
}