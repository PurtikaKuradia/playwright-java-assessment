package com.automationexercise.pages;

import com.automationexercise.common.CommonActions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

/**
 * ProductsPage - Page Object for /products and /view_cart
 *
 * KEY FIX: After clicking Search, the heading changes from
 * "All Products" to "Searched Products". Old code waited for
 * "All Products" which had disappeared — causing a 30s timeout.
 * Fix: separate methods for each heading state.
 */
public class ProductsPage extends CommonActions {

    private static final String ALL_PRODUCTS_HEADING      = "h2:has-text('All Products')";
    // FIX: after search the heading becomes "Searched Products"
    private static final String SEARCHED_PRODUCTS_HEADING = "h2:has-text('Searched Products')";

    private static final String PRODUCT_WRAPPER       = ".features_items .product-image-wrapper";
    private static final String ADD_TO_CART_BTN       = ".productinfo a.add-to-cart";
    private static final String CONTINUE_SHOPPING_BTN = "button.close-modal";
    private static final String VIEW_CART_MODAL_LINK  = ".modal-body a[href='/view_cart']";
    private static final String CART_PAGE_HEADING     = "li.active:has-text('Shopping Cart')";
    private static final String CART_PRODUCT_NAMES    = "td.cart_description h4 a";

    private static final String SEARCH_INPUT   = "#search_product";
    private static final String SEARCH_BUTTON  = "#submit_search";
    private static final String PRODUCT_CARDS  = ".features_items .col-sm-4";

    public ProductsPage(Page page) {
        super(page);
    }

    /** Verify 'All Products' page loaded */
    public boolean isProductsPageVisible() {
        page.waitForSelector(ALL_PRODUCTS_HEADING);
        return page.locator(ALL_PRODUCTS_HEADING).isVisible();
    }

    /** Add first product to cart by hovering to reveal button */
    public void addFirstProductToCart() {
        page.locator(PRODUCT_WRAPPER).first().hover();
        page.locator(ADD_TO_CART_BTN).first().click();
    }

    /** Add second product to cart */
    public void addSecondProductToCart() {
        page.locator(PRODUCT_WRAPPER).nth(1).hover();
        page.locator(ADD_TO_CART_BTN).nth(1).click();
    }

    /** Click 'Continue Shopping' in the modal */
    public void clickContinueShopping() {
        page.waitForSelector(CONTINUE_SHOPPING_BTN);
        page.locator(CONTINUE_SHOPPING_BTN).click();
    }

    /** Click 'View Cart' in the modal */
    public void clickViewCartFromModal() {
        page.waitForSelector(VIEW_CART_MODAL_LINK);
        page.locator(VIEW_CART_MODAL_LINK).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /** Verify cart page is visible */
    public boolean isCartPageVisible() {
        page.waitForSelector(CART_PAGE_HEADING);
        return page.locator(CART_PAGE_HEADING).isVisible();
    }

    /** Count products currently in cart */
    public int getCartItemCount() {
        page.waitForSelector(CART_PRODUCT_NAMES);
        return page.locator(CART_PRODUCT_NAMES).count();
    }

    /** Type keyword and click Search */
    public void searchProduct(String keyword) {
        page.waitForSelector(SEARCH_INPUT);
        page.locator(SEARCH_INPUT).clear();
        page.locator(SEARCH_INPUT).fill(keyword);
        page.locator(SEARCH_BUTTON).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /**
     * After searching, verify "Searched Products" heading appears.
     * FIX: Old code checked "All Products" which disappears after search.
     */
    public boolean isSearchedProductsVisible() {
        page.waitForSelector(SEARCHED_PRODUCTS_HEADING);
        return page.locator(SEARCHED_PRODUCTS_HEADING).isVisible();
    }

    /** Count product result cards after search */
    public int getSearchResultCount() {
        page.waitForSelector(PRODUCT_CARDS);
        return page.locator(PRODUCT_CARDS).count();
    }
}
