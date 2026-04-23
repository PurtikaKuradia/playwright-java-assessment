# Automation Exercise Test Framework

This project is a UI automation testing framework for [Automation Exercise](https://automationexercise.com/) built with Java, Playwright, JUnit 5, and Maven.

## What This Project Does

- Automates core user flows on the Automation Exercise website.
- Uses the Page Object Model (POM) to keep tests maintainable and reusable.
- Loads test data from JSON files for cleaner and data-driven test scenarios.
- Generates rich execution reports using Allure.

## Current Automated Test Scenarios

- `TC01_RegisterUserTest` - Register a new user.
- `TC02_LoginUserTest` - Login with valid credentials.
- `TC03_LogoutUserTest` - Logout after login.
- `TC04_ContactUsFormTest` - Submit the Contact Us form.
- `TC05_AddProductToCartTest` - Add product(s) to cart.

## Tech Stack

- Java 17
- Maven
- Playwright for Java
- JUnit 5
- Allure Report
- Jackson (for JSON test data handling)

## Project Structure

- `src/test/java/com/automationexercise/tests` - Test classes
- `src/test/java/com/automationexercise/pages` - Page Object classes
- `src/test/java/com/automationexercise/utils` - Utilities and data loader
- `src/test/resources/testdata.json` - Test input data

## How to Run

### 1) Compile Project

```bash
mvn compile
```

### 2) Run Tests

```bash
mvn test
```

### 3) Generate Allure Test Report

```bash
mvn allure:report
```

### 4) Serve Allure Report Locally

```bash
mvn allure:serve
```

`allure:serve` starts a local web server and opens the report in your browser.

## Notes

- Make sure Java 17 and Maven are installed and available in your PATH.
- If running Playwright for the first time, let Maven complete dependency setup before test execution.
