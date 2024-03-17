package com.example.androidapp.util;

import java.util.concurrent.*;

public class ThreadPool implements AutoCloseable {
    private ExecutorService executorService;
    private static ThreadPool INSTANCE;
    private ThreadPool(int threadCount) {
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    public static ThreadPool getInstance() {
        if (INSTANCE == null) throw new IllegalStateException("Initialise the ThreadPool with getInstance(int) before getting instance");
        return getInstance(0);
    }
    public static ThreadPool getInstance(int threadCount) {
        if (INSTANCE == null) INSTANCE = new ThreadPool(threadCount);
        return INSTANCE;
    }

    public <T> Future<T> run(Callable<T> task) {
        return executorService.submit(task);
    }

    @Override
    public void close() {
        executorService.shutdown();
        executorService = null;
        INSTANCE = null;
    }
}
