package com.cebbus.slicegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cebbus.slicegame.entity.Ball;
import com.cebbus.slicegame.entity.ClippedAreaView;
import com.cebbus.slicegame.entity.FingerLine;
import com.cebbus.slicegame.entity.MapFive;
import com.cebbus.slicegame.entity.MapFour;
import com.cebbus.slicegame.entity.MapOne;
import com.cebbus.slicegame.entity.MapThree;
import com.cebbus.slicegame.entity.MapTwo;
import com.cebbus.slicegame.entity.PolygonMap;
import com.cebbus.slicegame.entity.TimeView;
import com.cebbus.slicegame.util.Util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by cebbus on 18.05.2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Activity activity;
    private MainThread thread;

    private PolygonMap map;
    private Ball ball;
    private FingerLine fLine;
    private TimeView timeView;
    private ClippedAreaView clippedAreaView;
    private List<PolygonMap> mapList;

    private long startTime;
    private int time;
    private int totalTime;
    private int totalScore;
    private double clippedArea;
    float centerX;
    float centerY;

    private boolean stopped = false;
    private int selectedMap = 0;

    private NumberFormat formatter = new DecimalFormat("#0.00");

    private AlertDialog retryDialog;
    private AlertDialog resultDialog;

    private MediaPlayer player;
    private boolean sound;

    public GamePanel(Context context, boolean sound) {
        super(context);

        this.activity = (Activity) context;
        this.sound = sound;

        getHolder().addCallback(this);
        setFocusable(true);
        setBackgroundColor(Color.LTGRAY);

        initComponents();
    }

    private void initComponents() {
        Display display = activity.getWindowManager().getDefaultDisplay();
        centerX = display.getWidth()/2;
        centerY = display.getHeight()/2;

        mapList = new ArrayList<>();
        mapList.add(new MapOne(centerX, centerY));
        mapList.add(new MapTwo(centerX, centerY));
        mapList.add(new MapThree(centerX, centerY));
        mapList.add(new MapFour(centerX, centerY));
        mapList.add(new MapFive(centerX, centerY));

        map = mapList.get(selectedMap);

        ball = new Ball(centerX, centerY);
        fLine = new FingerLine();

        timeView = new TimeView();
        clippedAreaView = new ClippedAreaView();

        thread = new MainThread(getHolder(), this);

        createRetryDialog();
        createResultDialog();
    }

    private void createRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Tekrar oynamak ister misin?").setTitle("Oyun Bitti");

        builder.setPositiveButton("Evet", (dialog, id) -> {
            refreshView();
        });

        builder.setNegativeButton("Hayir", (dialog, id) -> {
            activity.finish();
        });

        retryDialog = builder.create();
    }

    private void createResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("").setTitle("Tebrikler");

        builder.setPositiveButton("Tamam", (dialog, id) -> {
            prepareNextMap();
        });

        resultDialog = builder.create();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        while (true) {
            try {
                stopped = true;
                thread.setRunning(false);
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if(fLine.isDrawed()) return false;

        if(stopped) return false;

        if(Util.onLine(
                fLine.getsX(), fLine.getsY(),
                fLine.geteX(), fLine.geteY(),
                ball.getX(), ball.getY(), 15)) {
            stopGame();
            retry();
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fLine.setsX(event.getX());
                fLine.setsY(event.getY());
                fLine.seteX(event.getX());
                fLine.seteY(event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                fLine.seteX(event.getX());
                fLine.seteY(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                fLine.seteX(event.getX());
                fLine.seteY(event.getY());
                fLine.setDrawed(true);

                if(sound) {
                    playSound();
                }

                stopGame();

                List<float[]> intPointList = Util.getIntPointList(map,
                        fLine.getsX(), fLine.getsY(), fLine.geteX(), fLine.geteY());

                if(intPointList != null) {
                    clippedArea = Util.calculateClippedArea(map, intPointList,
                            ball.getX(), ball.getY());

                    if(clippedArea >= 60) {
                        showResult();
                    } else {
                        retry();
                    }
                } else {
                    retry();
                }

                break;
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        invalidate();
        super.onDraw(canvas);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        map.draw(canvas);
        ball.draw(canvas);
        fLine.draw(canvas);
        timeView.draw(canvas, "Gecen Sure : " + time + " sn.");
        clippedAreaView.draw(canvas, "Kesilen Alan : " + formatter.format(clippedArea) + " %");
    }

    public void update() {
        if(stopped) return;

        time = calculateTime();
        totalTime += time;

        if(time >= 5) {
            stopGame();
            retry();
        }

        updateBall();
    }

    private void updateBall() {
        float x = ball.getX();
        float y = ball.getY();

        float dirX = ball.getDirectionX();
        float dirY = ball.getDirectionY();

        if(!Util.isInPolygon(map, x, y)) {
            dirX *= -1;
            dirY = Util.getRandomNumber();

            while(!Util.isInPolygon(map, x + dirX, y + dirY)) {
                dirX = Util.getRandomNumber();
                dirY = Util.getRandomNumber();
            }

            ball.setDirectionX(dirX);
            ball.setDirectionY(dirY);
        }

        ball.setX(x+dirX);
        ball.setY(y + dirY);
    }

    private void retry() {
        activity.runOnUiThread(retryDialog::show);
    }

    private void showResult() {
        activity.runOnUiThread(() -> {
            int tTime = totalTime == 0 ? 1 : totalTime;
            totalScore += ((selectedMap+1)*clippedArea*100)/tTime;

            resultDialog.setMessage("Puan : " + totalScore);
            resultDialog.show();
        });
    }

    private void playSound() {
        activity.runOnUiThread(() -> {
            player = MediaPlayer.create(activity, R.raw.sword);
            player.start();
        });
    }

    private int calculateTime() {
        return (int)((System.currentTimeMillis() - startTime)/1000);
    }

    private void stopGame() {
        stopped = true;
    }

    private void prepareNextMap() {
        int bufferMap = this.selectedMap;
        refreshView();

        if(bufferMap < 4) {
            selectedMap = bufferMap + 1;
            map = mapList.get(selectedMap);
        } else {
            retry();
        }
    }

    private void refreshView() {
        time = 0;
        clippedArea = 0;
        selectedMap = 0;
        totalTime = 0;
        totalScore = 0;

        fLine.setsX(0);
        fLine.seteX(0);
        fLine.setsY(0);
        fLine.seteY(0);
        fLine.setDrawed(false);

        ball.setX(centerX);
        ball.setY(centerY);
        ball.setDirectionX(Util.getRandomNumber());
        ball.setDirectionY(Util.getRandomNumber());

        map = mapList.get(selectedMap);

        stopped = false;
        startTime = System.currentTimeMillis();
    }
}
