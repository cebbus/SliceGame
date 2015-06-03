package com.cebbus.slicegame;

import android.view.SurfaceHolder;

/**
 *
 * Created by cebbus on 18.05.2015.
 */
public class MainThread extends Thread {

    private int FPS = 30;
    private final SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        int frameCount = 0;
        long targetTime = 1000 / FPS;

        while (running) {
            startTime = System.nanoTime();

            try {
                synchronized (surfaceHolder) {
                    gamePanel.update();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                MainThread.sleep(waitTime);
            } catch (Exception ignored) {}

            frameCount++;
            if (frameCount == FPS) {
                frameCount = 0;
            }
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }

}
