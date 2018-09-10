package org.pcsoft.framework.jremote.ext.up.impl.queued;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pcsoft.framework.jremote.commons.type.MethodKey;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;

import java.util.concurrent.atomic.AtomicReference;

class QueuedUpdatePolicyTest {

    private AtomicReference<String> value = new AtomicReference<>("");

    @Test
    void simpleUpdateTest() throws Exception {
        final UpdatePolicy updatePolicy = new QueuedUpdatePolicy();
        final MethodKey methodKey = new MethodKey(QueuedUpdatePolicyTest.class, "test");

        updatePolicy.runModelUpdateAndObserverInvocation(methodKey, null, null, () -> value.set("hello"));

        Thread.sleep(1250);

        Assertions.assertEquals("hello", value.get());
    }

    @Test
    void smallNumberOfSelfUpdatesTest() throws Exception {
        final UpdatePolicy updatePolicy = new QueuedUpdatePolicy();
        final MethodKey methodKey = new MethodKey(QueuedUpdatePolicyTest.class, "test");

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            updatePolicy.runModelUpdateAndObserverInvocation(methodKey, null, null, () -> value.set(String.valueOf(finalI)));
        }

        Thread.sleep(1250);

        Assertions.assertEquals("9", value.get());
    }

    @Test
    void bigNumberOfSelfUpdatesTest() throws Exception {
        final UpdatePolicy updatePolicy = new QueuedUpdatePolicy();
        final MethodKey methodKey = new MethodKey(QueuedUpdatePolicyTest.class, "test");

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            updatePolicy.runModelUpdateAndObserverInvocation(methodKey, null, null, () -> value.set(String.valueOf(finalI)));
        }

        Thread.sleep(1250);

        Assertions.assertEquals("999", value.get());
    }

    @Test
    void updatesTest() throws Exception {
        final UpdatePolicy updatePolicy = new QueuedUpdatePolicy();
        final MethodKey methodKey = new MethodKey(QueuedUpdatePolicyTest.class, "test");

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            updatePolicy.runModelUpdateAndObserverInvocation(methodKey, null, null, () -> value.set(String.valueOf(finalI)));
        }

        Thread.sleep(3250);

        //Updates of timer results in 3 or 4 cause timer is running currently in any iteration time-point
        Assertions.assertTrue(value.get().equals("3") || value.get().equals("4"));
    }

}