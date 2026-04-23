package com.automationexercise.pages;

import com.automationexercise.common.CommonActions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

/**
 * RegisterPage - Page Object for /signup (Enter Account Information form)
 *
 * KEY FIX: ACCOUNT_INFO_HEADING uses h2:has-text('Enter Account Information')
 * instead of "h2.title.text-center b" which matched 2 elements and threw
 * a strict mode violation error.
 */
public class RegisterPage extends CommonActions {

    // FIX: text-based selector avoids strict mode violation
    // (page has two h2.title.text-center b elements: "Enter Account Information" + "Address Information")
    private static final String ACCOUNT_INFO_HEADING = "h2:has-text('Enter Account Information')";

    private static final String TITLE_MR_RADIO      = "#id_gender1";
    private static final String PASSWORD_INPUT       = "input[data-qa='password']";
    private static final String DAY_DROPDOWN         = "select[data-qa='days']";
    private static final String MONTH_DROPDOWN       = "select[data-qa='months']";
    private static final String YEAR_DROPDOWN        = "select[data-qa='years']";
    private static final String NEWSLETTER_CHECKBOX  = "#newsletter";
    private static final String OPTIN_CHECKBOX       = "#optin";

    private static final String FIRST_NAME_INPUT = "input[data-qa='first_name']";
    private static final String LAST_NAME_INPUT  = "input[data-qa='last_name']";
    private static final String COMPANY_INPUT    = "input[data-qa='company']";
    private static final String ADDRESS1_INPUT   = "input[data-qa='address']";
    private static final String ADDRESS2_INPUT   = "input[data-qa='address2']";
    private static final String COUNTRY_DROPDOWN = "select[data-qa='country']";
    private static final String STATE_INPUT      = "input[data-qa='state']";
    private static final String CITY_INPUT       = "input[data-qa='city']";
    private static final String ZIPCODE_INPUT    = "input[data-qa='zipcode']";
    private static final String MOBILE_INPUT     = "input[data-qa='mobile_number']";

    private static final String CREATE_ACCOUNT_BTN  = "button[data-qa='create-account']";
    private static final String ACCOUNT_CREATED_MSG = "h2[data-qa='account-created']";
    private static final String CONTINUE_BUTTON     = "a[data-qa='continue-button']";
    private static final String DELETE_ACCOUNT_LINK = "a[href='/delete_account']";
    private static final String ACCOUNT_DELETED_MSG = "h2[data-qa='account-deleted']";

    public RegisterPage(Page page) {
        super(page);
    }

    public boolean isAccountInfoPageVisible() {
        page.waitForSelector(ACCOUNT_INFO_HEADING);
        return page.locator(ACCOUNT_INFO_HEADING).isVisible();
    }

    public void selectTitleMr()              { page.locator(TITLE_MR_RADIO).click(); }
    public void enterPassword(String p)      { page.locator(PASSWORD_INPUT).fill(p); }
    public void selectDayOfBirth(String d)   { page.locator(DAY_DROPDOWN).selectOption(d); }
    public void selectMonthOfBirth(String m) { page.locator(MONTH_DROPDOWN).selectOption(m); }
    public void selectYearOfBirth(String y)  { page.locator(YEAR_DROPDOWN).selectOption(y); }

    public void checkNewsletter() {
        if (!page.locator(NEWSLETTER_CHECKBOX).isChecked())
            page.locator(NEWSLETTER_CHECKBOX).check();
    }
    public void checkOptIn() {
        if (!page.locator(OPTIN_CHECKBOX).isChecked())
            page.locator(OPTIN_CHECKBOX).check();
    }

    public void enterFirstName(String v)  { page.locator(FIRST_NAME_INPUT).fill(v); }
    public void enterLastName(String v)   { page.locator(LAST_NAME_INPUT).fill(v); }
    public void enterCompany(String v)    { page.locator(COMPANY_INPUT).fill(v); }
    public void enterAddress1(String v)   { page.locator(ADDRESS1_INPUT).fill(v); }
    public void enterAddress2(String v)   { page.locator(ADDRESS2_INPUT).fill(v); }
    public void enterState(String v)      { page.locator(STATE_INPUT).fill(v); }
    public void enterCity(String v)       { page.locator(CITY_INPUT).fill(v); }
    public void enterZipcode(String v)    { page.locator(ZIPCODE_INPUT).fill(v); }
    public void enterMobileNumber(String v){ page.locator(MOBILE_INPUT).fill(v); }

    public void selectCountry(String country) {
        page.locator(COUNTRY_DROPDOWN).selectOption(
            new com.microsoft.playwright.options.SelectOption().setLabel(country));
    }

    public void clickCreateAccount() {
        page.locator(CREATE_ACCOUNT_BTN).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public boolean isAccountCreatedVisible() {
        page.waitForSelector(ACCOUNT_CREATED_MSG);
        return page.locator(ACCOUNT_CREATED_MSG).innerText()
            .equalsIgnoreCase("Account Created!");
    }

    public void clickContinue() {
        page.locator(CONTINUE_BUTTON).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public void deleteAccount() {
        page.locator(DELETE_ACCOUNT_LINK).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public boolean isAccountDeletedVisible() {
        page.waitForSelector(ACCOUNT_DELETED_MSG);
        return page.locator(ACCOUNT_DELETED_MSG).innerText()
            .equalsIgnoreCase("Account Deleted!");
    }
}
