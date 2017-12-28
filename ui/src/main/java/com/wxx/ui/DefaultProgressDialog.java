package com.wxx.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;


/**
 * 作者：万祥新 2017/12/11 15:21
 * 加载的dialog,已经适应了aicoin的黑夜模式了
 * <p>
 * DefaultProgressDialog dialog = new DefaultProgressDialog(this);
 * dialog.setLoadingSize(400);
 * dialog.setAlpha(0.5f);
 * dialog.setCornerRadius(50);
 * dialog.setLoadingSpeed(1000);
 * dialog.setBackgroundColor(Color.GREEN);
 * dialog.setLoadingColor(Color.RED);
 * dialog.show();
 * </p>
 */

public class DefaultProgressDialog extends Dialog {
  /**
   * loading的大小，单位是像素,默认是35dp
   */
  private int loadingSize = dp2px(35);

  /**
   * dialog的背景的圆角，默认为6dp
   */
  private int cornerRadius = dp2px(6);

  /**
   * loading view
   */
  private DefaultLoadingView loadingView;

  /**
   * dialog的根布局
   */
  private LinearLayout rootView;

  /**
   * dialog的背景
   */
  private GradientDrawable gradientDrawable;

  /**
   * dialog的背景色,默认黑色加个透明度
   */
  private int backgroundColor = Color.parseColor("#88000000");

  public DefaultProgressDialog(@NonNull Context context) {
    super(context);
    init();
  }

  private void init() {
    initRootView();
    initLoading();
    setContentView(rootView);
    setAlpha(0);
  }

  /**
   * 初始化loading
   */
  private void initLoading() {
    //创建一个loading
    loadingView = new DefaultLoadingView(getContext());

    //设置loading参数
    ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(loadingSize, loadingSize);
    loadingView.setLayoutParams(param);

    //添加loading到根布局
    rootView.addView(loadingView);
  }

  /**
   * 初始化dialog的根布局
   */
  private void initRootView() {
    //创建dialog的根布局
    rootView = new LinearLayout(getContext());

    //配置根布局的属性
    rootView.setOrientation(LinearLayout.VERTICAL);
    rootView.setGravity(Gravity.CENTER);

    //默认根布局的padding
    rootView.setPadding(dp2px(25), dp2px(25), dp2px(25), dp2px(25));
    initViewBackground();
  }

  /**
   * 初始化dialog的背景(默认自动适应aicoin的主题色)
   */
  private void initViewBackground() {
    //创建一个圆角黑色的背景
    gradientDrawable = new GradientDrawable();
    backgroundColor = Color.parseColor("#88000000");
    gradientDrawable.setColor(backgroundColor);
    gradientDrawable.setCornerRadius(cornerRadius);
    rootView.setBackground(gradientDrawable);
  }

  @Override
  public void dismiss() {
    //子线程也可以关闭
    rootView.post(new Runnable() {
      @Override
      public void run() {
        DefaultProgressDialog.super.dismiss();
      }
    });
  }

  public void setAlpha(float alpha) {
    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
    layoutParams.dimAmount = alpha;
    getWindow().setAttributes(layoutParams);
    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
  }

  public void setLoadingSize(int loadingSize) {
    this.loadingSize = loadingSize;
    ViewGroup.LayoutParams param = (ViewGroup.LayoutParams) loadingView.getLayoutParams();
    param.height = loadingSize;
    param.width = loadingSize;
    loadingView.setLayoutParams(param);
  }

  public void setCornerRadius(int cornerRadius) {
    this.cornerRadius = cornerRadius;
    gradientDrawable.setCornerRadius(cornerRadius);
  }

  public void setBackgroundColor(@ColorInt int backgroundColor) {
    this.backgroundColor = backgroundColor;
    gradientDrawable.setColor(backgroundColor);
  }

  public void setRootViewPadding(int left, int top, int right, int bottom) {
    rootView.setPadding(left, top, right, bottom);
  }

  /**
   * 设置loading旋转的速度
   *
   * @param millis 旋转一周的毫秒
   */
  public void setLoadingSpeed(int millis) {
    loadingView.setLoadingSpeed(millis);
  }

  /**
   * 设置loading的颜色
   *
   * @param loadingColor 颜色值
   */
  public void setLoadingColor(@ColorInt int loadingColor) {
    loadingView.setLoadingColor(loadingColor);
  }

  private int dp2px(int dp) {
    float density = getContext().getResources().getDisplayMetrics().density;
    return (int) (dp * density + 0.5);
  }
}