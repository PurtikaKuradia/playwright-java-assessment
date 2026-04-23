package com.automationexercise.tests;

import com.automationexercise.base.BaseClass;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.ProductsPage;
import com.automationexercise.utils.TestDataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TC05 - Products: Add to Cart + Search
 * Covers automationexercise.com Test Cases 12 and 9
 */
public class TC05_AddProductToCartTest extends BaseClass {

    @Test
    @DisplayName("TC12 - Add two products to cart and verify")
    public void testAddProductToCart() {
        HomePage     homePage     = new HomePage(page);
        ProductsPage productsPage = new ProductsPage(page);

        // Verify homepage
        assertTrue(homePage.isHomePageLoaded(), "Homepage should be visible");

        // Go to Products page
        homePage.clickProducts();
        assertTrue(productsPage.isProductsPageVisible(),
            "'All Products' heading should be visible");

        // Add first product, continue shopping
        productsPage.addFirstProductToCart();
        productsPage.clickContinueShopping();

        // Add second product, then view cart
        productsPage.addSecondProductToCart();
        productsPage.clickViewCartFromModal();

        // Verify cart page
        assertTrue(productsPage.isCartPageVisible(),
            "Shopping Cart page should be visible");

        // Verify at least 2 products in cart
        int count = productsPage.getCartItemCount();
        assertTrue(count >= 2,
            "At least 2 products should be in cart. Found: " + count);
        System.out.println("✅ Products in cart: " + count);
    }

    @Test
    @DisplayName("TC9 - Search for product and verify results")
    public void testSearchProduct() {
        HomePage     homePage     = new HomePage(page);
        ProductsPage productsPage = new ProductsPage(page);

        // Go to Products page
        homePage.clickProducts();
        assertTrue(productsPage.isProductsPageVisible(),
            "'All Products' heading should be visible");

        // Search for keyword
        String keyword = TestDataLoader.getProductData("searchKeyword");
        productsPage.searchProduct(keyword);

        // FIX: After search the heading changes to "Searched Products"
        // Old code waited for "All Products" which disappears — causing timeout
        assertTrue(productsPage.isSearchedProductsVisible(),
            "'Searched Products' heading should appear after search");

        // Verify at least one result
        int count = productsPage.getSearchResultCount();
        assertTrue(count > 0,
            "Search results should appear for keyword: " + keyword);
        System.out.println("✅ Search results for '" + keyword + "': " + count);
    }
}
