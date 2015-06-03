package com.cebbus.slicegame.entity;

import android.graphics.Paint;

/**
 *
 * Created by cebbus on 22.05.2015.
 */
public class MapFive extends PolygonMap{

    public MapFive(float centerX, float centerY) {
        super(Paint.ANTI_ALIAS_FLAG, centerX, centerY);
    }

    @Override
    protected void setPolygonPoints() {
        x[0] = centerX-100; y[0] = centerY-100;
        x[1] = centerX-250; y[1] = centerY+250;
        x[2] = centerX+300; y[2] = centerY+300;
        x[3] = centerX+400; y[3] = centerY-400;
        x[4] = centerX+150; y[4] = centerY-200;
    }
}
