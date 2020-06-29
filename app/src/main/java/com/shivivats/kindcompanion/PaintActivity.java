package com.shivivats.kindcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaintActivity extends AppCompatActivity {

    private PaintView paintView;

    private Toolbar paintViewTopBar;
    private Toolbar paintViewBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        paintView = findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        paintViewTopBar = findViewById(R.id.paintViewTopBar);
        paintViewBottomBar = findViewById(R.id.paintViewBottomBar);

        setSupportActionBar(paintViewTopBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topbar
        ab.setDisplayShowTitleEnabled(false);

        // set the bottom bar
        paintViewBottomBar = findViewById(R.id.paintViewBottomBar);


        // we have the menu and all setup now we need to save the image into a file
        // i feel like we need to return the image somehow


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_paint, menu);

        Menu bottomMenu = paintViewBottomBar.getMenu();
        getMenuInflater().inflate(R.menu.bottombar_paint, bottomMenu);

        for (int i = 0; i < bottomMenu.size(); i++) {
            bottomMenu.getItem(i).setOnMenuItemClickListener(menuItem -> onOptionsItemSelected(menuItem));
        }

        return true;
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

            case R.id.action_clear:
                paintView.clear();
                return true;

            case R.id.action_save_paint:
                // save the drawing here i guess idk
                String path="";
                try {
                    path = CreateDrawingFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(path!="") {
                    // we have the file path here
                    try (FileOutputStream out = new FileOutputStream(path)) {
                        paintView.getmBitmap().compress(Bitmap.CompressFormat.JPEG, 50, out);
                        out.close();
                        Uri drawingUri = Uri.fromFile(new File(path));

                        Intent intent =  new Intent();
                        intent.putExtra("DRAWING_URI", drawingUri.toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Drawing could not be saved.", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String CreateDrawingFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String drawingFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File drawing = File.createTempFile(
                drawingFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String currentDrawingPath = drawing.getAbsolutePath();
        return currentDrawingPath;
    }
}
