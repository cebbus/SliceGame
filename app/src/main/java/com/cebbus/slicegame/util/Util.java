package com.cebbus.slicegame.util;

import com.cebbus.slicegame.entity.PolygonMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * Created by cebbus on 19.05.2015.
 */
public class Util {

    private static Random random = new Random();

    public static boolean isInPolygon(PolygonMap map, float x, float y) {
        boolean inPolygon = false;

        int i, j;

        float polyX[] = map.getX();
        float polyY[] = map.getY();

        for (i = 0, j = polyX.length - 1; i < polyX.length; j = i++) {
            if ((polyY[i] > y) != (polyY[j] > y) &&
                    (x < (polyX[j] - polyX[i]) *
                            (y - polyY[i]) / (polyY[j]-polyY[i]) + polyX[i])) {
                inPolygon = !inPolygon;
            }
        }

        return inPolygon;
    }

    public static float getRandomNumber(){
        int start = -10;
        int end = +10;

        long range = (long)end - (long)start + 1;
        long fraction = (long)(range * random.nextDouble());

        return (fraction + start);
    }

    public static boolean onLine(float lx1, float ly1,
                                 float lx2, float ly2,
                                 float bX, float bY, int tolerance) {

        float ml = (ly1-ly2) / (lx1-lx2);
        float bl = ly1 - (ml*lx1);

        return Math.abs((ml * bX + bl) - bY) < tolerance;
    }

    public static List<float[]> findIntersectionPointList(
            PolygonMap map, float lx1, float ly1, float lx2, float ly2) {
        List<float[]> intPointList = new ArrayList<>();

        int i, j;

        float[] polyX = map.getX();
        float[] polyY = map.getY();

        for (i = 0, j = polyX.length - 1; i < polyX.length; j = i++) {
            float[] intersection = getIntersection(
                    lx1, ly1, lx2, ly2,
                    polyX[i], polyY[i],
                    polyX[j], polyY[j]);

            if(intersection != null) intPointList.add(intersection);

            if(intPointList.size() == 2) break;
        }

        return intPointList;
    }

    public static float[] getIntersection(float lx1, float ly1, float lx2, float ly2,
                                          float px1, float py1, float px2, float py2) {
        float result[] = null;

        float ml = (ly1-ly2) / (lx1-lx2);
        float mp = (py1-py2) / (px1-px2);

        float bl = ly1 - (ml*lx1);
        float bp = py1 - (mp*px1);

        float x = (bp - bl) / (ml - mp);
        float y = ml * x + bl;

        /*
        float x = ((lx2 - lx1)*(px1*py2 - px2*py1) - (px2 - px1)*(lx1*ly2 - lx2*ly1)) /
                ((lx1 - lx2)*(py1 - py2) - (ly1 - ly2)*(px1 - px2));

        float y = ((py1 - py2)*(lx1*ly2 - lx2*ly1) - (ly1 - ly2)*(px1*py2 - px2*py1)) /
                ((lx1 - lx2)*(py1 - py2) - (ly1 - ly2)*(px1 - px2));
        */


        float minX, minY, maxX, maxY;

        if(px1 < px2) {
            minX = px1;
            maxX = px2;
        } else {
            minX = px2;
            maxX = px1;
        }

        if(py1 < py2) {
            minY = py1;
            maxY = py2;
        } else {
            minY = py2;
            maxY = py1;
        }

        if(minX < x && x < maxX
                && minY < y && y < maxY) {
            result = new float[]{x, y};
        }

        return result;
    }

    public static List<float[]> getIntPointList(PolygonMap map, float fsX,
                                             float fsY, float feX, float feY) {
        if(isInPolygon(map, fsX, fsY)) {
            return null;
        }

        if(isInPolygon(map, feX, feY)) {
            return null;
        }

        List<float[]> intPointList = findIntersectionPointList(map, fsX, fsY, feX, feY);

        if(intPointList.size() < 2) {
            return null;
        } else {
            return intPointList;
        }
    }

    public static double calculateClippedArea(PolygonMap map,
        List<float[]> pointList, float bX, float bY) {

        ArrayList<Integer> indexes = new ArrayList<>();
        Map<Integer, float[]> pointMap = new HashMap<>();

        float[] polyX = map.getX();
        float[] polyY = map.getY();

        int j;
        for(int i = 0; i < polyX.length; i++) {
            j = (i == polyX.length -1) ? 0 : i + 1;

            for(float[] intPoint : pointList) {
                if(onLine(polyX[i], polyY[i], polyX[j],
                        polyY[j], intPoint[0], intPoint[1], 1)) {
                    indexes.add(i);
                    pointMap.put(i, intPoint);
                    break;
                }
            }
        }

        final ArrayList<Float> newPolyX = new ArrayList<>();
        final ArrayList<Float> newPolyY = new ArrayList<>();

        for(int i = 0; i < indexes.get(0)+1; i++) {
            newPolyX.add(polyX[i]);
            newPolyY.add(polyY[i]);
        }

        newPolyX.add(pointMap.get(indexes.get(0))[0]);
        newPolyY.add(pointMap.get(indexes.get(0))[1]);

        newPolyX.add(pointMap.get(indexes.get(1))[0]);
        newPolyY.add(pointMap.get(indexes.get(1))[1]);

            for(int i = indexes.get(1)+1; i < polyX.length; i++) {
            newPolyX.add(polyX[i]);
            newPolyY.add(polyY[i]);
        }

        float[] x = new float[newPolyX.size()];
        float[] y = new float[newPolyY.size()];

        for(int i = 0; i < newPolyX.size(); i++) {
            x[i] = newPolyX.get(i);
            y[i] = newPolyY.get(i);
        }

        PolygonMap clippedMap = new PolygonMap() {
            @Override
            protected void setPolygonPoints() {}
        };

        clippedMap.setX(x);
        clippedMap.setY(y);

        double mainMapArea = calculateMapArea(map);
        double clippedMapArea = calculateMapArea(clippedMap);

        if(isInPolygon(clippedMap, bX, bY)) {
            clippedMapArea = mainMapArea - clippedMapArea;
        }

        return (clippedMapArea * 100 )/ mainMapArea;
    }

    private static double calculateMapArea(PolygonMap map) {
        float x[] = map.getX();
        float y[] = map.getY();

        double sumX = 0;
        double sumY = 0;

        int j;
        for(int i = 0; i < x.length; i++) {
            j = (i == x.length -1) ? 0 : i + 1;

            sumX += x[i] * y[j];
            sumY += y[i] * x[j];
        }

        return Math.abs((sumX - sumY)/2);
    }
}
