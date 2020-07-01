package com.shivivats.kindcompanion;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteListItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public NoteListItemDecoration(Drawable drawable) {
        divider = drawable;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int dividerLeft = 32;
        int dividerRight = parent.getWidth() - 32;

        for (int i = 0; i < parent.getChildCount(); i++) {
            if (i != parent.getChildCount() - 1) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int divierBottom = dividerTop + divider.getIntrinsicHeight();

                divider.setBounds(dividerLeft, dividerTop, dividerRight, divierBottom);
                divider.draw(c);
            }
        }
    }

    // onDrawOver is for drawing the decorations after drawing the views
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }

        outRect.top = divider.getIntrinsicHeight();
    }
}
