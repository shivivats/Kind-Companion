package com.shivivats.kindcompanion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaintView extends View {

    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    public static final float TOUCH_TOLERANCE = 4;
    public static int BRUSH_SIZE = 20;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private Bitmap baseBitmap;
    private boolean isRelaxedEdit;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }

    public void init(DisplayMetrics displayMetrics, boolean isEdit, Bitmap bitmap, boolean isRelaxedPaint) {
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        isRelaxedEdit = isRelaxedPaint;

        if (isEdit) {
            mBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            baseBitmap = mBitmap;
            mCanvas = new Canvas(mBitmap);
        } else if (isRelaxedPaint) {
            mBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            baseBitmap = mBitmap;
            mCanvas = new Canvas(mBitmap);
        } else {
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            baseBitmap = mBitmap;
            mCanvas = new Canvas(mBitmap);
        }


        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;

        if (!isEdit && !isRelaxedPaint) {
            mCanvas.drawColor(DEFAULT_BG_COLOR);
        }


    }

    public void clear() {
        //backgroundColor = DEFAULT_BG_COLOR;
        mCanvas.drawColor(DEFAULT_BG_COLOR);
        paths.clear();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
            default:
                // idk if we need this rn but i'll cover it regardless
                return super.onTouchEvent(event);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        //mCanvas.drawColor(backgroundColor);
        for (FingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);

            mCanvas.drawPath(fp.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    public void SetColor(int color) {
        currentColor = color;
    }

    public void SetStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }
}
