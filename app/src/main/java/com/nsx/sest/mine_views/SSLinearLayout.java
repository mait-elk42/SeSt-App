package com.nsx.sest.mine_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.nsx.sest.init_page.InitActivity;

import java.util.Random;
import java.util.Vector;

public class SSLinearLayout extends LinearLayout {

    class V2 {
        public int x = 0;
        public int y = 0;

        public V2(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    V2[] points = new V2[100];
    private Paint   paint;
    private Context context;
    private Random random;
    private int numberOfPoints = 1000;  // Number of points to draw

    public SSLinearLayout(Context context) {
        super(context);
        this.context = context;
        init();
        Toast.makeText(context, "--"+getWidth(), Toast.LENGTH_SHORT).show();
    }

    public SSLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        Toast.makeText(context, "--"+getWidth(), Toast.LENGTH_SHORT).show();
    }

    public SSLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        Toast.makeText(context, "---"+getWidth(), Toast.LENGTH_SHORT).show();
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //problem in size :( fixed here
        Toast.makeText(context, "--"+h, Toast.LENGTH_SHORT).show();
    }

    private void init() {
        // Initialize paint for drawing the points
        paint = new Paint();
        paint.setColor(0xFFffffff);  // Set the color of points (black in this case)
        {
            int i = 0;
            while (i < points.length) {
//                points[i] = new V2(new Random().nextInt(getWidth()), new Random().nextInt(getHeight()));
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
//        {
//            int i = 0;
//            while (i < points.length) {
//                canvas.drawCircle(points[i].x, points[i].y, 5, paint);
//                i++;
//            }
//        }
        postInvalidateOnAnimation();
    }
}
