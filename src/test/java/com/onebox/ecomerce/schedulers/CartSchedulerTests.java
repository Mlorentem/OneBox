package com.onebox.ecomerce.schedulers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartSchedulerTests {

    private CartScheduler cartScheduler;

    @BeforeEach
    void setUp() {
        cartScheduler = new CartScheduler();
    }


    @Test
    void whenScheduleCartDeletionIsCalled_ThenTaskIsScheduled() {
        AtomicBoolean deleted = new AtomicBoolean(false);
        Long cartId = 1L;

        cartScheduler.scheduleCartDeletion(cartId, () -> deleted.set(true), 100, TimeUnit.MILLISECONDS);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(deleted.get(), "Cart should be deleted after scheduled delay");
    }

    @Test 
    void whenScheduleCartDeletionIsCalled_ThenTaskIsCancelledIfAlreadyScheduled() {
        AtomicBoolean deleted = new AtomicBoolean(false);
        Long cartId = 1L;

        cartScheduler.scheduleCartDeletion(cartId, () -> deleted.set(true), 100, TimeUnit.MILLISECONDS);
        cartScheduler.scheduleCartDeletion(cartId, () -> deleted.set(false), 50, TimeUnit.MILLISECONDS);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertFalse(deleted.get(), "Cart should not be deleted as the task was cancelled");
    }
}
