package com.cebbus.slicegame.entity;

import android.graphics.Paint;

/**
 *
 * Created by cebbus on 22.05.2015.
 */
public class MapTwo extends PolygonMap {

    public MapTwo(float centerX, float centerY) {
        super(Paint.ANTI_ALIAS_FLAG, centerX, centerY);
    }

    @Override
    protected void setPolygonPoints() {
        x[0] = centerX-250; y[0] = centerY-250;
        x[1] = centerX-450; y[1] = centerY+450;
        x[2] = centerX+500; y[2] = centerY+500;
        x[3] = centerX+200; y[3] = centerY-200;
        x[4] = centerX+100; y[4] = centerY-100;
    }
}
