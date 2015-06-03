package com.cebbus.slicegame.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 *
 * Created by cebbus on 21.05.2015.
 */
public class ClippedAreaView extends Paint {

    private float x;
    private float y;

    public ClippedAreaView() {
        super(Paint.ANTI_ALIAS_FLAG);
        setColor(Color.BLACK);
        setTextSize(30);

        this.x = 750;
        this.y = 50;
    }

    public void draw(Canvas canvas, String text) {
        canvas.drawText(text, x, y, this);
    }
}
