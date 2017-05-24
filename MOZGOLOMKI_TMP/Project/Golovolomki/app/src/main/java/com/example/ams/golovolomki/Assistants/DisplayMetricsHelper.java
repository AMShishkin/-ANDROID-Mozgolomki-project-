package com.example.ams.golovolomki.Assistants;

import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

public class DisplayMetricsHelper {
    private static Display display;
    private static int logicalHeight = 0, logicalWidth = 0, logicalDpi = 0, xDpi = 0, yDpi = 0;

    public static void setDisplayMetrics (Display displayValue) {
        Point _displaySize = new Point();
        DisplayMetrics _displayMetrics = new DisplayMetrics();

        display = displayValue;
        display.getRealSize(_displaySize);
        logicalWidth = _displaySize.x;
        logicalHeight = _displaySize.y;

        displayValue.getRealMetrics(_displayMetrics);
        xDpi = (int) _displayMetrics.xdpi;
        yDpi = (int) _displayMetrics.ydpi;

        logicalDpi = _displayMetrics.densityDpi;
    }

    public static Display getDisplay() {
        return display;
    }

    public static int getLogicalHeight() {
        return logicalHeight;
    }

    public static int getLogicalWidth() {
        return logicalWidth;
    }

    public static int getLogicalDpi() {
        return logicalDpi;
    }

    public static int getXDpi() { return xDpi; }

    public static int getYDpi() { return yDpi; }
}