package com.automationexercise.base;

import com.automationexercise.common.CommonActions;
import com.automationexercise.pages.AuthPomPage;
import com.automationexercise.utils.TestDataLoader;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * BaseClass
 * Extended by all test classes.
 *
 * KEY FIXES applied here:
 * 1. NO global page.onDialog() handler registered — it conflicts with ContactUs test.
 *    Each page registers its own dialog handler only when needed.
 * 2. All navigation uses DOMCONTENTLOADED to avoid 30s timeouts from ad scripts.
 * 3. Common ad network domains are blocked via context.route() to prevent popups.
 * 4. login() waits for a[href='/logout'] to confirm successful login.
 * 5. logout() waits for login form to confirm successful logout.
 */
@ExtendWith(TestArtifactsExtension.class)
public class BaseClass extends CommonActions {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected AuthPomPage authPomPage;
    protected String currentTestName = "unknown_test";
    protected boolean failureCaptured = false;
    protected boolean testFailed = false;

    public BaseClass() {
        super(null);
    }

    // ── Setup & Teardown ─────────────────────────────────────────

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        currentTestName = sanitizeFileName(testInfo.getDisplayName());
        failureCaptured = false;
        testFailed = false;
        playwright = Playwright.create();

        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)   // headed mode — browser window visible
                .setSlowMo(200)       // 200ms between actions for visibility
        );

        context = browser.newContext(
            new Browser.NewContextOptions()
                .setViewportSize(1280, 720)
        );
        context.tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true));

        // Block common ad networks — prevents popup ads from interfering with tests
        context.route("**/*googlesyndication*", route -> route.abort());
        context.route("**/*doubleclick*",        route -> route.abort());
        context.route("**/*adservice*",          route -> route.abort());
        context.route("**/*pagead*",             route -> route.abort());
        context.route("**/*amazon-adsystem*",    route -> route.abort());
        context.route("**/*adnxs*",              route -> route.abort());

        page = context.newPage();
        page.setDefaultTimeout(30000);
        authPomPage = new AuthPomPage(page);

        // ⚠️ DO NOT register page.onDialog() globally here.
        // It conflicts with ContactUsPage which needs to accept a specific dialog.
        // Each page/test manages its own dialog handling.

        navigateTo(TestDataLoader.getBaseUrl());
    }

    @AfterEach
    public void tearDown() {
        saveTrace();
        if (testFailed) {
            generateAndOpenAllureReport();
        }
        try { if (page != null)       page.close();      } catch (Exception ignored) {}
        try { if (context != null)    context.close();   } catch (Exception ignored) {}
        try { if (browser != null)    browser.close();   } catch (Exception ignored) {}
        try { if (playwright != null) playwright.close();} catch (Exception ignored) {}
    }

    // ── Login ────────────────────────────────────────────────────

    /**
     * Login using credentials from testdata.json → loginUser section.
     */
    public void login() {
        loginWithCredentials(
            TestDataLoader.getLoginData("email"),
            TestDataLoader.getLoginData("password")
        );
    }

    /**
     * Login with custom email and password.
     * Navigates to /login, fills the form, submits,
     * then waits for the logout link as confirmation of success.
     */
    public void loginWithCredentials(String email, String password) {
        page.navigate(TestDataLoader.getBaseUrl() + "/login",
            new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        authPomPage.login(email, password);

        // Confirm login success — logout link must appear
        try {
            authPomPage.waitForLogoutVisible(15000);
        } catch (Exception e) {
            // Save a debug screenshot to project root
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(java.nio.file.Paths.get("login_failure_debug.png")));
            throw new RuntimeException(
                "Login FAILED. URL: " + page.url() + " | Email: " + email, e);
        }
    }

    // ── Logout ───────────────────────────────────────────────────

    /**
     * Logout by clicking the Logout navbar link.
     * Waits for the login form to appear as confirmation.
     */
    public void logout() {
        authPomPage.clickLogout();
        page.waitForSelector(authPomPage.getLoginEmailInputSelector(),
            new Page.WaitForSelectorOptions().setTimeout(15000));
    }

    // ── Helpers ──────────────────────────────────────────────────

    /**
     * Returns true if the logout link is visible (user is logged in).
     */
    public boolean isLoggedIn() {
        return authPomPage.isLogoutVisible();
    }

    /**
     * Invoked by TestArtifactsExtension when a test throws an exception.
     */
    protected boolean captureFailureScreenshot(Throwable throwable) {
        if (failureCaptured || page == null) {
            return false;
        }
        testFailed = true;
        try {
            Path screenshotsDir = Paths.get("target", "screenshots");
            Files.createDirectories(screenshotsDir);
            Path screenshotPath = screenshotsDir.resolve(currentTestName + ".png");
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions()
                .setPath(screenshotPath)
                .setFullPage(true));
            Allure.addAttachment("Failure Screenshot - " + currentTestName,
                "image/png", new ByteArrayInputStream(screenshotBytes), ".png");
            failureCaptured = true;
            System.out.println("Saved failure screenshot: " + screenshotPath.toAbsolutePath());
            if (throwable != null) {
                System.out.println("Failure reason: " + throwable.getMessage());
                Allure.addAttachment("Failure Reason",
                    new ByteArrayInputStream(throwable.toString().getBytes(StandardCharsets.UTF_8)));
            }
            return true;
        } catch (Exception e) {
            System.out.println("Unable to save failure screenshot: " + e.getMessage());
            return false;
        }
    }

    private void saveTrace() {
        if (context == null) {
            return;
        }
        try {
            Path tracesDir = Paths.get("target", "traces");
            Files.createDirectories(tracesDir);
            Path tracePath = tracesDir.resolve(currentTestName + ".zip");
            context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
            System.out.println("Saved trace: " + tracePath.toAbsolutePath());
            if (testFailed && Files.exists(tracePath)) {
                Allure.addAttachment("Failure Trace - " + currentTestName,
                    "application/zip", Files.newInputStream(tracePath), ".zip");
            }
        } catch (Exception e) {
            System.out.println("Unable to save trace: " + e.getMessage());
        }
    }

    private void generateAndOpenAllureReport() {
        try {
            ProcessBuilder processBuilder = buildShellProcess("mvn -q -DskipTests allure:report");
            processBuilder.directory(Paths.get("").toAbsolutePath().toFile());
            Process process = processBuilder.start();
            boolean finished = process.waitFor(120, TimeUnit.SECONDS);
            if (!finished || process.exitValue() != 0) {
                System.out.println("Unable to generate Allure report automatically.");
                return;
            }

            Path reportIndex = Paths.get("target", "site", "allure-maven-plugin", "index.html").toAbsolutePath();
            if (!Files.exists(reportIndex)) {
                System.out.println("Allure report file not found: " + reportIndex);
                return;
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(reportIndex.toUri());
                System.out.println("Opened Allure report: " + reportIndex);
            }
        } catch (Exception e) {
            System.out.println("Unable to open Allure report automatically: " + e.getMessage());
        }
    }

    private ProcessBuilder buildShellProcess(String command) {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            return new ProcessBuilder("cmd", "/c", command);
        }
        return new ProcessBuilder("bash", "-lc", command);
    }

    private String sanitizeFileName(String value) {
        if (value == null || value.isBlank()) {
            return "unknown_test";
        }
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
