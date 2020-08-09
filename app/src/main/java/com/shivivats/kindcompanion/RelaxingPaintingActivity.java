package com.shivivats.kindcompanion;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.util.Random;

public class RelaxingPaintingActivity extends AppCompatActivity implements NextImageDialogFragment.NextImageListener, ClearCanvasDialogFragment.ClearCanvasListener {

    public PaintView paintView;
    Toolbar topBar, bottomBar;
    private Bitmap currentImage;
    DisplayMetrics currentMetrics;


    private String cbPrefix = "cb_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_relaxing_painting);

        currentImage = null;

        topBar = findViewById(R.id.relaxingPaintingTopBar);
        setSupportActionBar(topBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Relaxing Painting");
        }
        bottomBar = findViewById(R.id.relaxingPaintingBottomBar);
        paintView = findViewById(R.id.relaxingPaintView);


        currentMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(currentMetrics);

        // set the bitmap to a random image from the colouring book folder
        //LoadRandomImage();
        InitPaintView();

        //this.invalidateOptionsMenu();
    }

    private void LoadRandomImage() {
        // IMPLEMENT THIS FUNCTION

        // we need to load a random image from all the cb_ images in the drawables folder

        TypedArray images = getResources().obtainTypedArray(R.array.cb_images);
        Random rand = new Random();
        int randomInt = rand.nextInt(images.length());
        int randomImageID = images.getResourceId(randomInt, 0);

        currentImage = decodeSampledBitmapFromResource(getResources(), randomImageID, currentMetrics.widthPixels, currentMetrics.heightPixels);

        images.recycle();
    }

    private void InitPaintView() {
        paintView.init(currentMetrics, false, currentImage);
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //options.inSampleSize=8;

        //options.inSampleSize*=2;
        // Decode bitmap with inSampleSize set

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_relaxing_paint, menu);

        Menu bottomMenu = bottomBar.getMenu();
        getMenuInflater().inflate(R.menu.bottombar_paint, bottomMenu);

        for (int i = 0; i < bottomMenu.size(); i++) {
            bottomMenu.getItem(i).setOnMenuItemClickListener(this::onOptionsItemSelected);
        }

        return true;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            //final int halfHeight = height / 2;
            //final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((height / inSampleSize) >= reqHeight
                    && (width / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_color_black:
                paintView.setColor(Color.BLACK);
                return true;

            case R.id.action_color_blue:
                paintView.setColor(Color.BLUE);
                return true;

            case R.id.action_color_red:
                paintView.setColor(Color.RED);
                return true;

            case R.id.action_color_green:
                paintView.setColor(Color.rgb(0, 82, 33));
                return true;

            case R.id.action_color_grey:
                paintView.setColor(Color.rgb(180, 180, 180));
                return true;

            case R.id.action_color_lightblue:
                paintView.setColor(Color.rgb(126, 249, 255));
                return true;

            case R.id.action_color_lightgreen:
                paintView.setColor(Color.GREEN);
                return true;

            case R.id.action_color_orange:
                paintView.setColor(Color.rgb(255, 103, 0));
                return true;

            case R.id.action_color_purple:
                paintView.setColor(Color.rgb(148, 87, 235));
                return true;

            case R.id.action_color_yellow:
                paintView.setColor(Color.YELLOW);
                return true;

            case R.id.action_stroke_big:
                paintView.SetStrokeWidth(30);
                return true;

            case R.id.action_stroke_medium:
                paintView.SetStrokeWidth(20);
                return true;

            case R.id.action_stroke_small:
                paintView.SetStrokeWidth(10);
                return true;

            case R.id.action_eraser:
                //paintView.setErase(true);
                return true;

            case R.id.action_clear:
                ClearCanvas();
                return true;

            case R.id.action_next_image:
                NextImage();
                return true;
/*
            case R.id.action_undo:
                Undo();
                return true;

            case R.id.action_redo:
                Redo();
                return true;
*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Undo() {
        //paintView.undo();
    }

    private void ClearCanvas() {
        //show a dialog here that asks the user if they're sure
        DialogFragment newFragment = new ClearCanvasDialogFragment();
        newFragment.show(getSupportFragmentManager(), "clear_canvas");
    }

    private void NextImage() {
        // also show a dialogue here if they're sure they wanna go to the next image
        DialogFragment newFragment = new NextImageDialogFragment();
        newFragment.show(getSupportFragmentManager(), "next_image");
    }

    private void Redo() {
        //paintView.redo();
    }

    @Override
    public void onClearCanvasDialogPositiveClick(DialogFragment dialog) {
        paintView.clear();
    }

    @Override
    public void onClearCanvasDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onNextImageDialogPositiveClick(DialogFragment dialog) {
        // do something for next image here
        paintView.clear();

        // set the bitmap to a random image from the colouring book folder
        InitPaintView();
    }

    @Override
    public void onNextImageDialogNegativeClick(DialogFragment dialog) {

    }
}

