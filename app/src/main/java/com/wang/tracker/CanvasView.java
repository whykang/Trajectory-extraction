package com.wang.tracker;

import static android.view.MotionEvent.INVALID_POINTER_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.wang.tracker.log.LogRecorder;

import java.util.ArrayList;
import java.util.List;

/**
 * A view used only to draw point coordinates
 * */

/**
 *
 * @Description TODO
 * @Author WangHongyue
 * @CreateTime 2023-10-12  SDNU 304LAB
 */



public class CanvasView extends View {
    private final Paint paint = new Paint();
    private Canvas canvas = null;
    // All points are recorded in pointList
    private final List<PointF> pointList = new ArrayList<>();

    private Paint mPaint;

    private Paint mStrokePaint;

    private final int width;
    private final int height;

    private float x = 0;
    private float y = 0;

    private float yy = 0;

    private float xx = 0;

    private int cR = 10; // 圆点半径
    private int arrowR = 20; // 箭头半径

    private float mCurX = 200;
    private float mCurY = 200;
    private int mOrient=0;


    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    private float DownX,DownY;

    private float moveX=0;
    private float moveY=0;

    private float moveXx=0;
    private float moveYy=0;

    private Path mArrowPath; // 箭头路径

    private Paint paint2;

    private Paint bluePaint;

    private Context con;

    private Paint greenPaint;


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        yy=(float) (height * 0.5);
        xx=(float)(width * 0.5);
        con=context;

        //箭头
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mStrokePaint = new Paint(mPaint);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);

        //初始化箭头路径

        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR, -arrowR, arrowR, arrowR), 0, -180);
        mArrowPath.lineTo(0, -3 * arrowR);
        mArrowPath.close();


        paint2 = new Paint();
        paint2.setTextSize(50);
        paint2.setColor(Color.RED);
        paint2.setStyle(Paint.Style.STROKE);

        bluePaint = new Paint();

        greenPaint = new Paint();
        greenPaint.setColor(Color.BLACK);
        greenPaint.setAntiAlias(true); //抗锯齿

    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        for (PointF point : pointList){
            drawPoint(point.x, point.y);
            mCurX=point.x;
            mCurY=point.y;
           //canvas.drawCircle(point.x, point.y, cR, mPaint);
        }



        bluePaint.setStyle(Paint.Style.STROKE);//绘制直线
        bluePaint.setColor(Color.RED);
        bluePaint.setStrokeWidth(5); //画笔粗细为9像素点
        canvas.drawLine(xx, yy, xx+width*2, yy,bluePaint);
        canvas.drawLine(xx, yy, xx, yy-getHeight()*2,bluePaint);




        SharedPreferences dataBase =con.getSharedPreferences("Sha", con.MODE_PRIVATE);
        SharedPreferences.Editor edit = dataBase.edit();

        String name = dataBase.getString("ch2", "0");

        if(name.equals("1"))
        {
            //画矩形
            canvas.drawRect(xx+60 ,yy+80 ,xx+60+360 ,yy+80+150 ,greenPaint);
            canvas.drawRect(xx-120 ,yy+80 ,xx-120-260 ,yy+80+150 ,greenPaint);

            canvas.drawRect(xx+60 ,yy-80 ,xx+60+360 ,yy-80-150 ,greenPaint);
            canvas.drawRect(xx-120 ,yy-80 ,xx-120-260 ,yy-80-150 ,greenPaint);

        }


        canvas.drawText ("东",xx+width/2-100 ,yy-50, paint2);
        canvas.drawText ("北",xx+50,yy-getHeight()/2+50, paint2);


        canvas.save(); // 保存画布
        canvas.translate(mCurX, mCurY); // 平移画布
        canvas.rotate(mOrient); // 转动画布
        canvas.drawPath(mArrowPath, mPaint);
        canvas.drawArc(new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f),
                0, 360, false, mStrokePaint);
        canvas.restore(); // 恢复画布


    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
       // mScaleDetector.onTouchEvent(ev);

        LogRecorder.getInstance().i("angle", "---ddddddddddd----11");

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {

                DownX = ev.getX();//float DownX
                DownY = ev.getY();//float DownY
                //long  currentMS = System.currentTimeMillis();//long currentMS     获取系统时间
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                 moveX = (ev.getX() - DownX)*(float)0.02;//X轴距离
                 moveY = (ev.getY() - DownY)*(float)0.02;//y轴距离
                moveXx+=moveX;
                moveYy+=moveY;
//
                for (PointF point : pointList){
                    //drawPoint(point.x, point.y);
                    point.x+=moveX;
                    point.y+=moveY;
                    // canvas.drawCircle(point.x, point.y, cR, mPaint);
                }
               xx+=moveX;
               yy+=moveY;
//
//                x+=moveX;
//                y+=moveY;
//
////                mCurY+=moveY;
////                mCurX+=moveX;
                invalidate();

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
//                for (PointF point : pointList){
//                    //drawPoint(point.x, point.y);
//                    point.x+=moveX;
//                    point.y+=moveY;
//                    // canvas.drawCircle(point.x, point.y, cR, mPaint);
//                }
//
//                invalidate();
                break;
            }

        }

        return true;
    }


    public void autoDrawArrow(int orient) {
        mOrient = orient;
        invalidate();
    }



    // add the newly point to pointList
    public void addPoint(float dx, float dy) {
        x += dx;
        y += dy;

        LogRecorder.getInstance().i("angle", "-----------"+moveY);
        pointList.add(new PointF((float) (width * 0.5 + 10 * x), (float) (height * 0.5 - 10 * y)));
        //pointList1.add(new PointF((float) (width * 0.5 + 10 * x), (float) (height * 0.5 - 10 * y)));

        pointList.get(pointList.size()-1).x+=moveXx;
        pointList.get(pointList.size()-1).y+=moveYy;
        invalidate();
    }

    // draw all point from pointList
    private void drawPoint(float x, float y) {

        canvas.drawCircle(x, y, 10f, paint);
    }


    public void clearPoints(int xnum, int ynum) {
        x = 0;
        y = 0;
        pointList.clear();
        yy=(float) (height * 0.5);
        xx=(float)(width * 0.5);
        moveYy=0;
        moveXx=0;
        moveX=0;
        moveY=0;
        addPoint(xnum, ynum);

        invalidate();
    }

}