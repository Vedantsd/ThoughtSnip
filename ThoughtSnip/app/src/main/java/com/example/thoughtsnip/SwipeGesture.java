package com.example.thoughtsnip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SwipeGesture extends ItemTouchHelper.SimpleCallback {

    private Paint paint = new Paint();
    private Context context;

    public SwipeGesture(Context ctx) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.context = ctx;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isActive) {

        View item = viewHolder.itemView;
        int height = item.getBottom() - item.getTop();
        float cornerRadius = 40f;

        if (dX > 0) {
            paint.setColor(Color.parseColor("#E53935"));

            RectF rect = new RectF(
                    item.getLeft(),
                    item.getTop(),
                    item.getLeft() + dX,
                    item.getBottom()
            );
            c.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
            paint.setColor(Color.WHITE);

            Drawable icon = ContextCompat.getDrawable(context, R.drawable.delete);
            int size = height / 3;
            int left = item.getLeft() + 50;
            int top = item.getTop() + (height - size) / 2;

            icon.setBounds(left, top, left + size, top + size);
            icon.draw(c);
        }

        else if (dX < 0) {
            paint.setColor(Color.parseColor("#1E88E5"));

            RectF rect = new RectF(
                    item.getRight() + dX,
                    item.getTop(),
                    item.getRight(),
                    item.getBottom()
            );
            c.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
            paint.setColor(Color.WHITE);

            Drawable icon = ContextCompat.getDrawable(context, R.drawable.edit);
            int size = height / 3;
            int right = item.getRight() - 50;
            int top = item.getTop() + (height - size) / 2;

            icon.setBounds(right - size, top, right, top + size);
            icon.draw(c);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isActive);
    }
}
