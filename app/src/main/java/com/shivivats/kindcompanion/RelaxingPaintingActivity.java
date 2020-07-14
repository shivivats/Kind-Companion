package com.shivivats.kindcompanion;

import android.graphics.Bitmap;
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

public class RelaxingPaintingActivity extends AppCompatActivity implements NextImageDialogFragment.NextImageListener, ClearCanvasDialogFragment.ClearCanvasListener {

    public PaintView paintView;
    Toolbar topBar, bottomBar;
    private Bitmap currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxing_painting);

        topBar = findViewById(R.id.relaxingPaintingTopBar);
        setSupportActionBar(topBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Relaxing Painting");

        bottomBar = findViewById(R.id.relaxingPaintingBottomBar);
        paintView = findViewById(R.id.relaxingPaintView);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // set the bitmap to a random image from the colouring book folder
        LoadRandomImage();
        paintView.init(metrics, true, currentImage);

        //this.invalidateOptionsMenu();
    }

    private void LoadRandomImage() {
        // IMPLEMENT THIS FUNCTION
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_relaxing_paint, menu);

        Menu bottomMenu = bottomBar.getMenu();
        getMenuInflater().inflate(R.menu.bottombar_paint, bottomMenu);

        for (int i = 0; i < bottomMenu.size(); i++) {
            bottomMenu.getItem(i).setOnMenuItemClickListener(menuItem -> onOptionsItemSelected(menuItem));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_color_black:
                paintView.SetColor(Color.BLACK);
                return true;

            case R.id.action_color_blue:
                paintView.SetColor(Color.BLUE);
                return true;

            case R.id.action_color_red:
                paintView.SetColor(Color.RED);
                return true;

            case R.id.action_color_green:
                paintView.SetColor(Color.rgb(0, 82, 33));
                return true;

            case R.id.action_color_grey:
                paintView.SetColor(Color.rgb(180, 180, 180));
                return true;

            case R.id.action_color_lightblue:
                paintView.SetColor(Color.rgb(126, 249, 255));
                return true;

            case R.id.action_color_lightgreen:
                paintView.SetColor(Color.GREEN);
                return true;

            case R.id.action_color_orange:
                paintView.SetColor(Color.rgb(255, 103, 0));
                return true;

            case R.id.action_color_purple:
                paintView.SetColor(Color.rgb(148, 87, 235));
                return true;

            case R.id.action_color_yellow:
                paintView.SetColor(Color.YELLOW);
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
                paintView.SetColor(Color.WHITE);
                return true;

            case R.id.action_clear:
                ClearCanvas();
                return true;

            case R.id.action_next_image:
                NextImage();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ClearCanvas() {
        //show a dialog here that asks the user if they're sure
        DialogFragment newFragment = new ClearCanvasDialogFragment();
        newFragment.show(getSupportFragmentManager(), "clear_canvas");
    }

    private void NextImage() {
        // also show a dialogue here if theyre sure they wanna go to the next image
        DialogFragment newFragment = new NextImageDialogFragment();
        newFragment.show(getSupportFragmentManager(), "next_image");
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

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // set the bitmap to a random image from the colouring book folder
        LoadRandomImage();
        paintView.init(metrics, true, currentImage);
    }

    @Override
    public void onNextImageDialogNegativeClick(DialogFragment dialog) {

    }
}

