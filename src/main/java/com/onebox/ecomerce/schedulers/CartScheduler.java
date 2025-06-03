package com.onebox.ecomerce.schedulers;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CartScheduler {

    private static final Logger log = LoggerFactory.getLogger(CartScheduler.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<Long, ScheduledFuture<?>> cartDeletionTasks = new ConcurrentHashMap<>();

    public void scheduleCartDeletion(Long cartId, Runnable deleteAction, long delay, TimeUnit unit) {

        ScheduledFuture<?> previousTask = cartDeletionTasks.get(cartId);
        if(previousTask != null && !previousTask.isDone()) {
            previousTask.cancel(false);
        }
        ScheduledFuture<?> newTask = scheduler.schedule(deleteAction, delay, unit);
        cartDeletionTasks.put(cartId, newTask);
        log.debug("Scheduled deletion for cart ID {} in {} {}", cartId, delay, unit);
    }

    public void interactWithCart(Long cartId) {
        log.debug("Cart interaction postponed deletion for Cart ID {}", cartId);
        scheduleCartDeletion(cartId, () -> deleteTaskCart(cartId), 2, TimeUnit.MINUTES);
    }

    public void deleteTaskCart (Long cartId) {
        cartDeletionTasks.remove(cartId);
        log.debug("Cart deleted from task map for Cart id {}" , cartId);
    }
}
