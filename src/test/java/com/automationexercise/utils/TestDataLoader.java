package com.automationexercise.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

/**
 * TestDataLoader
 * Reads all test data from src/test/resources/testdata.json.
 * Static methods provide easy access to each data section.
 */
public class TestDataLoader {

    private static final JsonNode rootNode;

    // Generated once per JVM run — ensures a fresh unique email every time tests are executed
    private static final String UNIQUE_EMAIL =
            "testuser_" + System.currentTimeMillis() + "@mailinator.com";

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = TestDataLoader.class.getResourceAsStream("/testdata.json");
            if (is == null) throw new RuntimeException("testdata.json not found in resources.");
            rootNode = mapper.readTree(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load testdata.json: " + e.getMessage(), e);
        }
    }

    public static String getBaseUrl() {
        return rootNode.get("baseUrl").asText();
    }

    /**
     * Returns register data from testdata.json.
     * EXCEPTION: "email" field always returns a unique timestamped address
     * so every test run registers with a fresh email — no duplicate email errors.
     */
    public static String getRegisterData(String field) {
        if ("email".equals(field)) {
            return UNIQUE_EMAIL;
        }
        return rootNode.get("registerUser").get(field).asText();
    }

    public static String getLoginData(String field) {
        return rootNode.get("loginUser").get(field).asText();
    }

    public static String getInvalidLoginData(String field) {
        return rootNode.get("invalidLoginUser").get(field).asText();
    }

    public static String getContactUsData(String field) {
        return rootNode.get("contactUs").get(field).asText();
    }

    public static String getProductData(String field) {
        return rootNode.get("product").get(field).asText();
    }
}
