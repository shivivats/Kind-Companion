package com.shivivats.kindcompanion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaintActivity extends AppCompatActivity implements DeleteImageDialogFragment.DeleteImageListener, ClearCanvasDialogFragment.ClearCanvasListener {

    private PaintView paintView;

    private Toolbar paintViewBottomBar;

    private long currentImageId;

    private Uri currentImageUri;

    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_paint);

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("IS_EDIT", false);

        //Log.d("PAINT_ACTIVITY", "isEdit: " + isEdit);

        if (isEdit) {
            currentImageId = intent.getLongExtra("IMAGE_ID", -1);
            if (currentImageId == -1) {
                setResult(-4);
                finish();
            }
            currentImageUri = Uri.parse(intent.getStringExtra("IMAGE_URI"));
            //Log.d("PAINT_ACTIVITY", "currentImageUri: " + currentImageUri);
        } else {
            currentImageId = -1;
            currentImageUri = null;
        }

        paintView = findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Bitmap bitmap;
        //Bitmap returnedBitmap = null;
        if (isEdit) {
            // here we need to set the paintview canvas to be a bitmap from the image

            //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentImageUri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            bitmap = BitmapFactory.decodeFile(currentImageUri.getPath(), options);
            //Log.d("PAINT_ACTIVITY", "bitmap: " + bitmap);


            if (bitmap != null) {
                // we can use the bitmap to pass to the paintview here
                paintView.init(metrics, true, bitmap, false);
            } else {
                setResult(-4);
                finish();
            }
        } else {
            paintView.init(metrics, false, null, false);
        }

        Toolbar paintViewTopBar = findViewById(R.id.paintViewTopBar);
        paintViewBottomBar = findViewById(R.id.paintViewBottomBar);

        setSupportActionBar(paintViewTopBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);

        if (isEdit && ab != null) {
            ab.setTitle("Edit Image");
        } else {
            ab.setTitle("New Drawing");
        }

        // set the bottom bar
        paintViewBottomBar = findViewById(R.id.paintViewBottomBar);


        // we have the menu and all setup now we need to save the image into a file
        // i feel like we need to return the image somehow

        this.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_paint, menu);

        Menu bottomMenu = paintViewBottomBar.getMenu();
        getMenuInflater().inflate(R.menu.bottombar_paint, bottomMenu);

        for (int i = 0; i < bottomMenu.size(); i++) {
            bottomMenu.getItem(i).setOnMenuItemClickListener(this::onOptionsItemSelected);
        }

        MenuItem deleteButton = menu.findItem(R.id.action_delete_paint);

        if (isEdit) {
            deleteButton.setVisible(true);
        } else {
            deleteButton.setVisible(false);
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

            case R.id.action_save_paint:
                if (isEdit) {
                    UpdateDrawing();
                } else {
                    SaveDrawing();
                }
                return true;
/*
            case R.id.action_undo:
                Undo();
                return true;

            case R.id.action_redo:
                Redo();
                return true;
*/
            case R.id.action_delete_paint:
                if (isEdit) {
                    //show a dialog here that asks the user if they're sure
                    DialogFragment newFragment = new DeleteImageDialogFragment();
                    newFragment.show(getSupportFragmentManager(), "delete_paint");
                }
                return true;

            case R.id.action_cancel_paint:
                ReturnToNoteEdit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isEdit) {
            UpdateDrawing();
        } else {
            SaveDrawing();
        }
    }

    private void Undo() {
        //paintView.undo();
    }

    private void Redo() {
        //paintView.redo();
    }

    private void UpdateDrawing() {

        // we also set the uri and such here
        // or can we just set the new bitmap????
        // idk

        // we need to set the uri
        // we need to set the isdrawing boolean

        String path = currentImageUri.getPath();
        try (FileOutputStream out = new FileOutputStream(path)) {
            paintView.getmBitmap().compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.close();
            Uri drawingUri = Uri.fromFile(new File(path));

            Intent intent = new Intent();
            intent.putExtra("IMAGE_URI", drawingUri.toString());
            intent.putExtra("IMAGE_ID", currentImageId);
            intent.putExtra("IS_DRAWING", true);

            setResult(-3, intent);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveDrawing() {
        // save the drawing here i guess idk
        String path = "";
        try {
            path = CreateDrawingFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!path.equals("")) {
            // we have the file path here
            try (FileOutputStream out = new FileOutputStream(path)) {
                paintView.getmBitmap().compress(Bitmap.CompressFormat.JPEG, 50, out);
                out.close();
                Uri drawingUri = Uri.fromFile(new File(path));

                Intent intent = new Intent();
                intent.putExtra("DRAWING_URI", drawingUri.toString());
                setResult(RESULT_OK, intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Snackbar.make(paintView, "Drawing could not be saved.", Snackbar.LENGTH_SHORT)
                    .setAnchorView(paintViewBottomBar)
                    .show();
            //Toast.makeText(getApplicationContext(), "Drawing could not be saved.", Toast.LENGTH_SHORT).show();
        }
    }

    private void DeleteCurrentImage() {

        Intent intent = new Intent();
        intent.putExtra("IMAGE_ID", currentImageId);
        setResult(-2, intent);
        finish();
    }

    private String CreateDrawingFile() throws IOException {
        // Create an image file name
        // This gives implied locale warning, however this is just for filenames and user wont see it, so that's ok
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String drawingFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File drawing = File.createTempFile(
                drawingFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return drawing.getAbsolutePath();
    }

    private void ClearCanvas() {
        //show a dialog here that asks the user if they're sure
        DialogFragment newFragment = new ClearCanvasDialogFragment();
        newFragment.show(getSupportFragmentManager(), "clear_canvas");
    }

    private void ReturnToNoteEdit() {
        setResult(-5);
        finish();
    }

    @Override
    public void onDeleteImageDialogPositiveClick(DialogFragment dialog) {
        DeleteCurrentImage();
    }

    @Override
    public void onDeleteImageDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onClearCanvasDialogPositiveClick(DialogFragment dialog) {
        paintView.clear();
    }

    @Override
    public void onClearCanvasDialogNegativeClick(DialogFragment dialog) {

    }
}
