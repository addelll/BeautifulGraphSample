package com.deepakbaliga.beautifulgraph;

/**
 * Created by deepakbaliga on 29/12/16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Plotter extends View {


    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint greyPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint gradientPaint = new Paint();
    private float correction = 3.5f;
    private List<Integer> plots = new ArrayList<>();
    private float[] points;


    public Plotter(Context context) {
        super(context);
    }

    public Plotter(Context context, AttributeSet attrs) {
        super(context, attrs);


        greyPaint.setStyle(Paint.Style.STROKE);
        greyPaint.setColor(context.getResources().getColor(R.color.grey_lines));
        greyPaint.setAntiAlias(true);
        greyPaint.setStrokeWidth(10);



        gradientPaint.setStyle(Paint.Style.STROKE);
        gradientPaint.setAntiAlias(true);
        gradientPaint.setStrokeWidth(10);

        textPaint.setTextSize(26);
        textPaint.setAntiAlias(true);
        textPaint.setAlpha(75);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

    }

    public Plotter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, w, oldw, oldw);
        calculateDimensions();
    }

    //Use this method to draw any custom view
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT);

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getWidth();

        drawMatrix(width, height, canvas);
        plotGraph(width, height, canvas);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
   public void setRowCol(int row, int col) {
        this.numColumns = col;
        this.numRows = row;
        calculateDimensions();
    }


    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numColumns;
        invalidate();
    }


    public void setPlots(List<Integer> plots) {
        this.plots = plots;
        points = new float[plots.size() * 2];
    }


    private void drawMatrix(int width, int height, Canvas canvas) {

        for (int i = 0; i <= numColumns; i++) {
            canvas.drawLine((i * cellWidth), 0, (i * cellWidth), height, greyPaint);
          //  canvas.drawText("", (i * cellWidth), height-20, textPaint);
        }

        for (int i = 0; i <= numRows; i++)
            canvas.drawLine(0, (i * cellHeight), width, (i * cellHeight), greyPaint);

    }


    private void plotGraph(int width, int height, Canvas canvas) {

        gradientPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Shader shader = new LinearGradient(0, 0, 0, 500, getResources().getColor(R.color.green), getResources().getColor(R.color.green), Shader.TileMode.MIRROR /*or REPEAT*/);
        gradientPaint.setShader(shader);


        for (int i = 0, j = 0; i < plots.size(); i++, j += 2) {

            canvas.drawCircle(inBoundX(i) + cellWidth, inBoundY(i, getHeight()), 10, gradientPaint);
            points[j] = inBoundX(i) + cellWidth;
            points[j + 1] = inBoundY(i, getHeight());


           // canvas.drawText("1 mois", inBoundX(i) + cellWidth, inBoundY(i, getHeight()), textPaint);

        }

        gradientPaint.setStyle(Paint.Style.STROKE);

        drawLines(canvas);

    }

    private void drawLines(Canvas canvas) {

        if (points.length <= 1)
            return;

        Path path = new Path();

        //This is to move the cursor
        path.moveTo(0, getWidth());

        for (int i = 0; i < points.length; i += 2) {
            path.lineTo(points[i], points[i + 1]);

        }


        canvas.drawPath(path, gradientPaint);

    }


    private float inBoundX(int value) {
        int perPlot = getWidth() / plots.size();
        perPlot = perPlot - 10;

        return perPlot * value;
    }

    private float inBoundY(int i, int height) {

        //return (float) plots.get(i) * getWidth() / (float)  maxValue();
        return (float) getHeight() - plots.get(i) * getWidth() / (float) maxValue();
    }


    private float maxValue() {
        int max = plots.get(0);


        for (Integer value : plots)
            if (value > max) {
                max = value;
            }


        return max + 10;
    }


}
