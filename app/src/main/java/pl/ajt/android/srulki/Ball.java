package pl.ajt.android.srulki;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Jacus on 2016-10-09.
 */

public class Ball {
    public float cx;
    public float cy;
    public static float r =30.0f;
    private Color color;

    private long xVelocity = 0;
    private long yVelocity = 0;

    public Ball(){}

    public void update(long p_fps){
        cx += (xVelocity / p_fps);
        cy += (yVelocity / p_fps);
    }

    public void setColor(Color p_color){
        color = p_color;
    }
}
