package com.hsbc.transaction.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TransactionThreadPool {
    private static final Logger logger = LoggerFactory.getLogger(TransactionThreadPool.class);

    private static final int DEFAULT_CORE_POOL_SIZE = 10;
    private static final int DEFAULT_QUEUE_CAPACITY = 100;
    private static final String THREAD_NAME_PREFIX = "TransactionThread-";

    private final int workerSize;
    private final int taskQueueSize;
    private final BlockingDeque<Runnable> taskQueue;
    private final List<TransactionThread> workerThreads = new ArrayList<>();

    public TransactionThreadPool() {
        this(DEFAULT_CORE_POOL_SIZE, DEFAULT_QUEUE_CAPACITY);
    }

    public TransactionThreadPool(int workerSize, int taskQueueSize) {
        this.workerSize = workerSize > 0 ? workerSize : DEFAULT_CORE_POOL_SIZE;
        this.taskQueueSize = taskQueueSize > 0 ? taskQueueSize : DEFAULT_QUEUE_CAPACITY;
        taskQueue = new LinkedBlockingDeque<>(this.taskQueueSize);
        for (int i = 0; i < this.workerSize; i++) {
            TransactionThread worker = new TransactionThread(String.valueOf(i));
            worker.start();
            workerThreads.add(worker);
        }
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (taskQueue.remainingCapacity() == 0) {
            logger.warn("Task queue is full, task will be dropped: {}", task);
            return; // Optionally handle the case where the queue is full
        }
        try {
            taskQueue.put(task);
            logger.info("Task added to queue by {}", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            logger.error("Failed to add task to queue", e);
        }
    }

    public void shutdown() {
        logger.info("Shutting down TransactionThreadPool");
        for (TransactionThread worker : workerThreads) {
            worker.stopThread();
        }
        workerThreads.clear();
        taskQueue.clear();
        logger.info("TransactionThreadPool has been shut down");
    }

    private class TransactionThread extends Thread {

        public TransactionThread(String name) {
            super(THREAD_NAME_PREFIX + name);
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Runnable task = taskQueue.take();
                    logger.info("{} is processing a task", getName());
                    task.run();
                } catch (Exception e) {
                    logger.info("{} was interrupted by exception", getName(), e);
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                }
            }
        }

        public void stopThread() {
            this.interrupt();
        }
    }
}
