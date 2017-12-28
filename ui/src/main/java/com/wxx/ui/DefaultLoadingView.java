package com.wxx.ui;

/**
 * 作者：万祥新 2017/12/13 14:36
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * 作者：万祥新 2017/12/13 15:18
 * 自己定义的一个加载动作,已经适应了aicoin的黑夜模式了
 * 通过下面方式即可使用，自动播放
 * <p>
 * <com.wxx.test.widget.DefaultLoadingView
 * android:id="@+id/view_loading"
 * android:layout_width="40dp"
 * android:layout_height="40dp"
 * />
 * </p>
 */

public class DefaultLoadingView extends View {

  /**
   * loading的颜色
   */
  private int loadingColor;

  /**
   * loading的大小，通常直接在XML文件中配置宽和高，不建议在代码中设置其大小
   */
  private int size;

  /**
   * 默认起始旋转的角度
   */
  private int animateValue = 0;

  /**
   * 旋转一周所需的时长
   */
  private int A_ROUND_CYCLE = 600;

  /**
   * Android中的属性动画
   */
  private ValueAnimator animator;

  /**
   * 画加载进度的画笔
   */
  private Paint paint;

  /**
   * 加载圈线条的个数
   */
  private static final int LINE_COUNT = 12;

  /**
   * 每两条线之间的角度
   */
  private static final int DEGREE_PER_LINE = 360 / LINE_COUNT;

  /**
   * 默认的构造函数
   *
   * @param context 上下文对象
   */
  public DefaultLoadingView(Context context) {
    super(context);
    init();
  }

  /**
   * 默认的带有XML属性的构造函数
   *
   * @param context 上下文对象
   * @param attrs   xml属性集合
   */
  public DefaultLoadingView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  /**
   * 自定义的构造函数
   *
   * @param context 上下文对象
   * @param size    加载view的大小
   */
  public DefaultLoadingView(Context context, int size) {
    super(context);
    this.size = size;
    init();
  }

  /**
   * 初始化loading，默认已经将loading的白天和黑夜模式的颜色值写进去了
   */
  private void init() {
    loadingColor = Color.parseColor("#9E9E9E");
    paint = new Paint();
    paint.setColor(loadingColor);
    paint.setAntiAlias(true);
    paint.setStrokeCap(Paint.Cap.ROUND);
  }

  private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
      animateValue = (int) animation.getAnimatedValue();
      invalidate();
    }
  };

  /**
   * 开启loading的动画
   */
  public void start() {
    if (animator == null) {
      animator = ValueAnimator.ofInt(0, LINE_COUNT - 1);
      animator.addUpdateListener(mUpdateListener);
      animator.setDuration(A_ROUND_CYCLE);
      animator.setRepeatMode(ValueAnimator.RESTART);
      animator.setRepeatCount(ValueAnimator.INFINITE);
      animator.setInterpolator(new LinearInterpolator());
      animator.start();
    } else if (!animator.isStarted()) {
      animator.start();
    }
  }

  /**
   * 停止loading的动画
   */
  public void stop() {
    if (animator != null) {
      animator.removeUpdateListener(mUpdateListener);
      animator.removeAllUpdateListeners();
      animator.cancel();
      animator = null;
    }
  }

  /**
   * 通过canvas画loading
   *
   * @param canvas        画布
   * @param rotateDegrees 旋转的角度
   */
  private void drawLoading(Canvas canvas, int rotateDegrees) {
    if (size == 0) {
      size = getWidth();
    }
    int width = size / 12, height = size / 6;
    paint.setStrokeWidth(width);

    canvas.rotate(rotateDegrees, size / 2, size / 2);
    canvas.translate(size / 2, size / 2);

    for (int i = 0; i < LINE_COUNT; i++) {
      canvas.rotate(DEGREE_PER_LINE);
      paint.setAlpha((int) (255f * (i + 1) / LINE_COUNT));
      canvas.translate(0, -size / 2 + width / 2);
      canvas.drawLine(0, 0, 0, height, paint);
      canvas.translate(0, size / 2 - width / 2);
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
    drawLoading(canvas, animateValue * DEGREE_PER_LINE);
    canvas.restoreToCount(saveCount);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    start();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    stop();
  }

  @Override
  protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
    super.onVisibilityChanged(changedView, visibility);
    if (visibility == VISIBLE) {
      start();
    } else {
      stop();
    }
  }

  /**
   * 设置loading的颜色
   *
   * @param paintColor 颜色值
   */
  public void setLoadingColor(@ColorInt int paintColor) {
    this.loadingColor = paintColor;
    if (paint != null) {
      paint.setColor(paintColor);
    }
  }

  /**
   * 设置loading的大小，不建议调用该方法设置，推荐使用xml或者带有size的构造函数初始化大小
   *
   * @param size loading的大小
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * 设置loading的速度
   *
   * @param millis 旋转一圈的毫秒数
   */
  public void setLoadingSpeed(int millis) {
    A_ROUND_CYCLE = millis;
  }
}