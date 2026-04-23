package com.kevin.qa.playwrightweb;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Sauce_Demo_Web {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(1000));
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    @DisplayName("Scenario 1: Login and Logout")
    void loginLogoutTest() {
        page.navigate("https://saucedemo.com");

        // Login
        page.locator("[data-test='username']").fill("standard_user");
        page.locator("[data-test='password']").fill("secret_sauce");
        page.locator("[data-test='login-button']").click();

        // Click Burger Menu
        page.locator("#react-burger-menu-btn").click();

        // Click Logout
        page.locator("[data-test='logout-sidebar-link']").click();

        // Assert: Check if we are back on the login page by verifying the login button is visible
        assertThat(page.locator("[data-test='login-button']")).isVisible();
    }

    @Test
    @DisplayName("Scenario 2: Failed Login (Wrong Password)")
    void wrongPasswordTest() {
        page.navigate("https://saucedemo.com");

        // Try to login with wrong password
        page.locator("[data-test='username']").fill("standard_user");
        page.locator("[data-test='password']").fill("wrong_password");
        page.locator("[data-test='login-button']").click();

        // Assert: Verify that the error message container contains the expected text
        assertThat(page.locator("[data-test='error']"))
                .containsText("Username and password do not match any user in this service");
    }

    @AfterEach
    void tearDown() {
        if (context != null) context.close();
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
