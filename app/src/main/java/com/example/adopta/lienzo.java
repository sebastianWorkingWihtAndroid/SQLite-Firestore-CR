package com.example.adopta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class lienzo extends View {

    private Path drawPath;
    private static Paint drawPaint;
    private Paint canvasPaint;
    private int paintColor = 0xff000000;
    private Bitmap canvasBitmap;
    private Canvas canvas;


    private static Boolean borrado = false;

    public lienzo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(6);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(canvasBitmap);
    }

    public void NuevoDibujo() {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float toucheX =event.getX();
        float toucheY =event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(toucheX,toucheY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(toucheX,toucheY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(toucheX,toucheY);
                canvas.drawPath(drawPath,drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();

        return true;
    }
}
