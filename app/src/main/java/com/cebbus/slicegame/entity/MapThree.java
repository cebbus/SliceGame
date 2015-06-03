package com.cebbus.slicegame.entity;

import android.graphics.Paint;

/**
 *
 * Created by cebbus on 22.05.2015.
 */
public class MapThree extends PolygonMap{

    public MapThree(float centerX, float centerY) {
        super(Paint.ANTI_ALIAS_FLAG, centerX, centerY);
    }

    @Override
    protected void setPolygonPoints() {
        x[0] = centerX-450; y[0] = centerY-450;
        x[1] = centerX-150; y[1] = centerY+150;
        x[2] = centerX+350; y[2] = centerY+350;
        x[3] = centerX+200; y[3] = centerY-200;
        x[4] = centerX+175; y[4] = centerY-175;
    }

}
