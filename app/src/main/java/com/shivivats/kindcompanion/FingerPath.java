package com.shivivats.kindcompanion;

import android.graphics.Path;

public class FingerPath {

    // we only need color and width for now

    public int color;
    public int strokeWidth;
    public Path path;

    public FingerPath(int color, int strokeWidth, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }

}

