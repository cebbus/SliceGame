package com.cebbus.slicegame.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 *
 * Created by cebbus on 19.05.2015.
 */
public abstract class PolygonMap extends Paint{
    private Path wallPath = new Path();

    protected float[] x = new float[5];

    protected float[] y = new float[5];

    protected float centerX, centerY;

    public PolygonMap() {}

    public PolygonMap(int flag, float centerX, float centerY) {
        super(flag);
        setStyle(Style.FILL);
        setColor(Color.DKGRAY);

        this.centerX = centerX;
        this.centerY = centerY;

        setPolygonPoints();

        wallPath.reset();
        wallPath.moveTo(x[0], y[0]);
        wallPath.lineTo(x[1], y[1]);
        wallPath.lineTo(x[2], y[2]);
        wallPath.lineTo(x[3], y[3]);
        wallPath.lineTo(x[4], y[4]);
        wallPath.moveTo(x[0], y[0]);
    }

    protected abstract void setPolygonPoints();

    public void draw(Canvas canvas) {
        canvas.drawPath(wallPath, this);
    }

    public Path getWallPath() {
        return wallPath;
    }

    public float[] getX() {
        return x;
    }

    public float[] getY() {
        return y;
    }

    public void setX(float[] x) {
        this.x = x;
    }

    public void setY(float[] y) {
        this.y = y;
    }
}
