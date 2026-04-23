package com.automationexercise.base;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Captures artifacts immediately when a test fails and
 * prints final pass/fail/screenshot summary for the run.
 */
public class TestArtifactsExtension implements TestExecutionExceptionHandler, AfterTestExecutionCallback,
    BeforeEachCallback, LifecycleMethodExecutionExceptionHandler {
    private static final Namespace SUMMARY_NAMESPACE = Namespace.create(TestArtifactsExtension.class);
    private static final String SUMMARY_KEY = "execution-summary";

    @Override
    public void beforeEach(ExtensionContext context) {
        TestRunSummary summary = getOrCreateSummary(context);
        summary.total.incrementAndGet();
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        TestRunSummary summary = getOrCreateSummary(context);
        if (context.getExecutionException().isPresent()) {
            summary.failed.incrementAndGet();
        } else {
            summary.passed.incrementAndGet();
        }
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        TestRunSummary summary = getOrCreateSummary(context);
        Object testInstance = context.getRequiredTestInstance();
        if (testInstance instanceof BaseClass baseClass) {
            if (baseClass.captureFailureScreenshot(throwable)) {
                summary.failureScreenshots.incrementAndGet();
            }
        }
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        TestRunSummary summary = getOrCreateSummary(context);
        summary.failed.incrementAndGet();
        Object testInstance = context.getRequiredTestInstance();
        if (testInstance instanceof BaseClass baseClass) {
            if (baseClass.captureFailureScreenshot(throwable)) {
                summary.failureScreenshots.incrementAndGet();
            }
        }
        throw throwable;
    }

    private TestRunSummary getOrCreateSummary(ExtensionContext context) {
        Store store = context.getRoot().getStore(SUMMARY_NAMESPACE);
        return store.getOrComputeIfAbsent(SUMMARY_KEY, key -> new TestRunSummary(), TestRunSummary.class);
    }

    private static class TestRunSummary implements CloseableResource {
        private final AtomicInteger total = new AtomicInteger();
        private final AtomicInteger passed = new AtomicInteger();
        private final AtomicInteger failed = new AtomicInteger();
        private final AtomicInteger failureScreenshots = new AtomicInteger();

        @Override
        public void close() {
            System.out.println("========== TEST EXECUTION SUMMARY ==========");
            System.out.println("Total tests        : " + total.get());
            System.out.println("Passed tests       : " + passed.get());
            System.out.println("Failed tests       : " + failed.get());
            System.out.println("failureCaptured    : " + failureScreenshots.get());
            System.out.println("============================================");
        }
    }
}
