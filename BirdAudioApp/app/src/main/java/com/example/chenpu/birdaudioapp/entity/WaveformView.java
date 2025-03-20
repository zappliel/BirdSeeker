package com.example.chenpu.birdaudioapp.entity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WaveformView extends View {

    private Paint paint;
    private float waveRadius = 100f;  // 当前波纹的半径
    private int maxWaveRadius;  // 最大波纹半径
    private int waveColor = Color.parseColor("#017550");  // 波纹颜色 (半透明绿色)

    public WaveformView(Context context) {
        super(context);
        init();
    }

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(waveColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        paint.setAntiAlias(true);

        maxWaveRadius = 500;  // 设置波纹的最大半径

        // 创建一个动画来控制波纹的扩展
        ValueAnimator animator = ValueAnimator.ofFloat(0, maxWaveRadius);
        animator.setDuration(2000);  // 2秒完成一个周期
        animator.setRepeatCount(ValueAnimator.INFINITE);  // 无限循环
        animator.setRepeatMode(ValueAnimator.RESTART);  // 从头开始
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                waveRadius = (float) animation.getAnimatedValue();  // 更新波纹半径
                invalidate();  // 触发视图重绘
            }
        });
        animator.start();  // 启动动画
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 计算中心位置
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // 绘制中心圆
        paint.setColor(waveColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, 130, paint);  // 中心圆半径50

        // 绘制外部波纹
        paint.setColor(waveColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(centerX, centerY, waveRadius, paint);  // 绘制波纹
    }
}
