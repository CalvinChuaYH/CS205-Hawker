package com.example.androidapp.gamelogic;

import com.example.androidapp.util.ThreadPool;

public class GameLogic {

    // 1 for waiter, 1 for chef, 1 for customer
    private static final int THREAD_COUNT = 3;
    private ThreadPool threadPool;

    public GameLogic() {
        this.threadPool = ThreadPool.getInstance(THREAD_COUNT);
    }

    public void startGame() {
    }

    public void stopGame() {
        // Gracefully shutdown the thread pool when the game is stopped
        threadPool.close();
    }
}
