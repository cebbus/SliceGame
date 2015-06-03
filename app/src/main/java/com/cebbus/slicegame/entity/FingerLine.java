package com.cebbus.slicegame.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 *
 * Created by cebbus on 18.05.2015.
 */
public class FingerLine extends Paint{

    private float sX;
    private float sY;
    private float eX;
    private float eY;

    private boolean drawed;

    public FingerLine() {
        super(Paint.ANTI_ALIAS_FLAG);
        setStyle(Paint.Style.STROKE);
        setColor(Color.CYAN);
        setStrokeWidth(15f);
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(sX, sY, eX, eY, this);
    }

    public float getsX() {
        return sX;
    }

    public void setsX(float sX) {
        this.sX = sX;
    }

    public float getsY() {
        return sY;
    }

    public void setsY(float sY) {
        this.sY = sY;
    }

    public float geteX() {
        return eX;
    }

    public void seteX(float eX) {
        this.eX = eX;
    }

    public float geteY() {
        return eY;
    }

    public void seteY(float eY) {
        this.eY = eY;
    }

    public void setDrawed(boolean drawed) {
        this.drawed = drawed;
    }

    public boolean isDrawed() {
        return drawed;
    }
}
