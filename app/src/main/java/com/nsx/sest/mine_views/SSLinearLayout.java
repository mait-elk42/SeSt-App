package com.nsx.sest.mine_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Size;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.nsx.sest.init_page.InitActivity;

import java.util.Random;
import java.util.Vector;

public class SSLinearLayout extends LinearLayout {

    class Point {
        public int x;
        public int y;
        public int incx = 1;
        public int incy = 1;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    class Size {
        public int Width;
        public int Height;

        public Size(int Width, int Height) {
            this.Width = Width;
            this.Height = Height;
        }
    }
    Point[] points = new Point[500];
    private Paint   paint;
    private Context context;
    private Size    size;

    public SSLinearLayout(Context context) {
        super(context);
        this.context = context;
    }

    public SSLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SSLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.size = new Size(w, h);
        init();
    }

    private void init() {
        // Initialize paint for drawing the points
        paint = new Paint();
        paint.setColor(0xFFffffff);  // Set the color of points (black in this case)
        {
            int i = 0;
            while (i < points.length) {
                points[i] = new Point(new Random().nextInt(size.Width), new Random().nextInt(size.Height));
                if (points[i].x >= size.Width / 2)
                    points[i].incx = -1;
                if (points[i].x < size.Width / 2)
                    points[i].incx = 1;

                if (points[i].y >= size.Height / 2)
                    points[i].incy = -1;
                if (points[i].x < size.Height / 2)
                    points[i].incy = 1;
                i++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get screen width and height
        int width = getWidth();
        int height = getHeight();

        // Draw random points on the screen
//        for (int i = 0; i < numberOfPoints; i++) {
//            // Generate random (x, y) coordinates within the screen limits
//            float x = random.nextFloat() * width;
//            float y = random.nextFloat() * height;

            // Draw the point at the random coordinates
//            canvas.drawCircle(i1.x, height /2, 30, paint);
//            canvas.drawLine(i1.x, height /2, i2.x, height /2, paint);
//            canvas.drawPoint(i2.x, height /2, paint);
//            i1.x += 1;
//            if (i1.x > width)
//            {
//                i1.x = 0;
//            }
//        }
        {
            int i = 0;
            while (i < points.length) {
                points[i].x+= points[i].incx;
                points[i].y+= points[i].incy;
                if (points[i].x < 0)
                    points[i].x = new Random().nextInt(size.Width /2 );
                if (points[i].x > size.Width)
                    points[i].x = new Random().nextInt(size.Width /2);
                if (points[i].y < 0)
                    points[i].y = new Random().nextInt(size.Height /2);
                if (points[i].y > size.Height)
                    points[i].y = new Random().nextInt(size.Height / 2);
                i++;
            }
            i = 0;
            while (i < points.length) {
                canvas.drawCircle(points[i].x, points[i].y, 5, paint);
                i++;
            }
            i = 0;
            while (i < points.length) {
                int j = i+ 1;
                while (j < points.length)
                {
                    final int x1 = points[i].x;
                    final int y1 = points[i].y;
                    final int x2 = points[j].x;
                    final int y2 = points[j].y;
                    float distance = (float) Math.sqrt(Math.pow(Math.abs(x2-x1), 2) + Math.pow(Math.abs(y2-y1), 2));
                    if (distance < 100) {
                        Paint paint2 = new Paint();
                        paint2.setColor(0xFF00ff00);
                        canvas.drawLine(x1, y1, x2, y2, paint2);
                    }
                    j++;
                }
                i++;
            }
        }
        postInvalidateOnAnimation();
    }
}
