package com.hidezo.app.buyer.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dezami on 2016/10/20.
 *
 */

public class CircleView extends View {

    private Paint paint;

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public CircleView(Context context) {
        super(context);
        paint = new Paint();
    }

    public void setColor(int color){
        paint.setColor(getResources().getColor(color));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);
        canvas.drawCircle(canvas.getHeight() / 2, canvas.getHeight() / 2, (canvas.getWidth() / 2) - 2, paint);
    }
}
