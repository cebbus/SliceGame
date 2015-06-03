package com.cebbus.slicegame.entity;

import android.graphics.Paint;

/**
 *
 * Created by cebbus on 22.05.2015.
 */
public class MapFour extends PolygonMap {

    public MapFour(float centerX, float centerY) {
        super(Paint.ANTI_ALIAS_FLAG, centerX, centerY);
    }

    @Override
    protected void setPolygonPoints() {
        x[0] = centerX-75; y[0] = centerY-75;
        x[1] = centerX-300; y[1] = centerY+300;
        x[2] = centerX+250; y[2] = centerY+250;
        x[3] = centerX+300; y[3] = centerY-300;
        x[4] = centerX+200; y[4] = centerY-150;
    }

}
