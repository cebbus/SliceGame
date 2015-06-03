package com.cebbus.slicegame.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.cebbus.slicegame.util.Util;

/**
 *
 * Created by cebbus on 18.05.2015.
 */
public class Ball extends Paint {

    private float x;
    private float y;
    private float directionX;
    private float directionY;

    public Ball(float centerX, float centerY) {
        super(Paint.ANTI_ALIAS_FLAG);
        setStyle(Style.FILL);
        setColor(Color.RED);

        this.x = centerX;
        this.y = centerY;

        this.directionX = Util.getRandomNumber();
        this.directionY = Util.getRandomNumber();
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, 20f, this);
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getDirectionX() {
        return directionX;
    }

    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }

    public float getDirectionY() {
        return directionY;
    }

    public void setDirectionY(float directionY) {
        this.directionY = directionY;
    }
}
