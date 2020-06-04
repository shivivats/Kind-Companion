package com.shivivats.kindcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PaintActivity extends AppCompatActivity {

    private PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        paintView = findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bottombar_paint,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_color_blue:
                paintView.SetColor(Color.BLUE);
                return true;
            case R.id.action_color_red:
                paintView.SetColor(Color.RED);
                return true;

            case R.id.action_color_green:
                paintView.SetColor(Color.GREEN);
                return true;

            case R.id.action_color_grey:
                paintView.SetColor(Color.rgb(178,190,181));
                return true;

            case R.id.action_color_lightblue:
                paintView.SetColor(Color.rgb(172,229,238));
                return true;

            case R.id.action_color_lightgreen:
                paintView.SetColor(Color.rgb(74,255,0));
                return true;

            case R.id.action_color_orange:
                paintView.SetColor(Color.rgb(255,103,0));
                return true;

            case R.id.action_color_purple:
                paintView.SetColor(Color.rgb(148,87,235));
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

            case R.id.action_save_paint:
                // save the drawing here i guess idk
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
