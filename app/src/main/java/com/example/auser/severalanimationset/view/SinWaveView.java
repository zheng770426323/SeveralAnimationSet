package com.example.auser.severalanimationset.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.example.auser.severalanimationset.R;
import jameson.io.library.util.ScreenUtil;

/**
 * Created by Auser on 2017/7/5.
 */

public class SinWaveView extends View {
    private static final float STRETCH_FACTOR_A = 20;
    private static final int OFFSET_Y = 0;
    private final Paint mWavePaint;
    private final PaintFlagsDrawFilter mDrawFilter;
    private int mTotalWidth;
    private int mTotalHeight;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private float[] mResetThreeYPositions;
    private float[] mResetFourYPositions;
    private float[] mResetFiveYPositions;
    private float[] mResetSixYPositions;
    private float[] mYPositions;
    private float mCycleFactorW;
    private int mXOneOffset;
    private int mXTwoOffset;
    private int mXThreeOffset;
    private int mXFourOffset;
    private int mXFiveOffset;
    private int mXSixOffset;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 7;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 5;
    // 第三条水波移动速度
    private static final int TRANSLATE_X_SPEED_THREE = 6;
    // 第四条水波移动速度
    private static final int TRANSLATE_X_SPEED_FOUR = 4;
    // 第五条水波移动速度
    private static final int TRANSLATE_X_SPEED_FIVE = 8;
    // 第六条水波移动速度
    private static final int TRANSLATE_X_SPEED_SIX = 6;
    private final int mXOffsetSpeedOne;
    private final int mXOffsetSpeedTwo;
    private final int mXOffsetSpeedThree;
    private final int mXOffsetSpeedFour;
    private final int mXOffsetSpeedFive;
    private final int mXOffsetSpeedSix;

    public SinWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = ScreenUtil.dip2px(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = ScreenUtil.dip2px(context, TRANSLATE_X_SPEED_TWO);
        mXOffsetSpeedThree = ScreenUtil.dip2px(context, TRANSLATE_X_SPEED_THREE);
        mXOffsetSpeedFour = ScreenUtil.dip2px(context, TRANSLATE_X_SPEED_FOUR);
        mXOffsetSpeedFive = ScreenUtil.dip2px(context, TRANSLATE_X_SPEED_FIVE);
        mXOffsetSpeedSix = ScreenUtil.dip2px(context, TRANSLATE_X_SPEED_SIX);
        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mWavePaint.setColor(context.getResources().getColor(R.color.wave_color));
        //抗锯齿
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    /*该方法有局限性，绘制的数据超过三条就不太明显，并且较卡顿，建议绘制两条波浪线动画
      实现的过程中我们需要关注如下几个方法：
      1.onMeasure():最先回调，用于控件的测量;
      2.onSizeChanged():在onMeasure后面回调，可以拿到view的宽高等数据，在横竖屏切换时也会回调;
      3.onDraw()：真正的绘制部分，绘制的代码都写到这里面;*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        Log.e("mTotalWidth",mTotalWidth+"");
        Log.e("mTotalHeight",mTotalHeight+"");
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];
        // 用于保存波纹三的y值
        mResetThreeYPositions = new float[mTotalWidth];
        // 用于保存波纹四的y值
        mResetFourYPositions = new float[mTotalWidth];
        // 用于保存波纹五的y值
        mResetFiveYPositions = new float[mTotalWidth];
        // 用于保存波纹六的y值
        mResetSixYPositions = new float[mTotalWidth];
        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);
        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        resetPositonY();
        for (int i = 0; i < mTotalWidth; i++) {
            // 减500只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
            // 绘制第一条水波纹
            canvas.drawLine(i, mTotalHeight - mResetOneYPositions[i] - 500, i, mTotalHeight, mWavePaint);
            // 绘制第二条水波纹
            canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] - 500, i, mTotalHeight, mWavePaint);
            // 绘制第三条水波纹
            canvas.drawLine(i, mTotalHeight - mResetThreeYPositions[i] - 400, i, mTotalHeight, mWavePaint);
            // 绘制第四条水波纹
            canvas.drawLine(i, mTotalHeight - mResetFourYPositions[i] - 400, i, mTotalHeight, mWavePaint);
            // 绘制第五条水波纹
            canvas.drawLine(i, mTotalHeight - mResetFiveYPositions[i] - 280, i, mTotalHeight, mWavePaint);
            // 绘制第六条水波纹
            canvas.drawLine(i, mTotalHeight - mResetSixYPositions[i] - 280, i, mTotalHeight, mWavePaint);
        }
        // 改变六条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;
        mXThreeOffset += mXOffsetSpeedThree;
        mXFourOffset += mXOffsetSpeedFour;
        mXFiveOffset += mXOffsetSpeedFive;
        mXSixOffset += mXOffsetSpeedSix;
        Log.e("mXOneOffset",mXOneOffset+"");
        Log.e("mXTwoOffset",mXTwoOffset+"");
        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }
        if (mXThreeOffset > mTotalWidth) {
            mXThreeOffset = 0;
        }
        if (mXFourOffset > mTotalWidth) {
            mXFourOffset = 0;
        }
        if (mXFiveOffset > mTotalWidth) {
            mXFiveOffset = 0;
        }
        if (mXSixOffset > mTotalWidth) {
            mXSixOffset = 0;
        }
        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        postInvalidate();
    }

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0, yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);

        int yThreeInterval = mYPositions.length - mXThreeOffset;
        System.arraycopy(mYPositions, mXThreeOffset, mResetThreeYPositions, 0, yThreeInterval);
        System.arraycopy(mYPositions, 0, mResetThreeYPositions, yThreeInterval, mXThreeOffset);

        int yFourInterval = mYPositions.length - mXFourOffset;
        System.arraycopy(mYPositions, mXFourOffset, mResetFourYPositions, 0, yFourInterval);
        System.arraycopy(mYPositions, 0, mResetFourYPositions, yFourInterval, mXFourOffset);

        int yFiveInterval = mYPositions.length - mXFiveOffset;
        System.arraycopy(mYPositions, mXFiveOffset, mResetFiveYPositions, 0, yFiveInterval);
        System.arraycopy(mYPositions, 0, mResetFiveYPositions, yFiveInterval, mXFiveOffset);

        int ySixInterval = mYPositions.length - mXSixOffset;
        System.arraycopy(mYPositions, mXSixOffset, mResetSixYPositions, 0, ySixInterval);
        System.arraycopy(mYPositions, 0, mResetSixYPositions, ySixInterval, mXSixOffset);
    }
}
