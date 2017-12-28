package com.wxx.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 仿微信联系人滑动
 * <p>
 * 作者： 万祥新 {2017/10/14 10:54}
 */

public class SideBarView extends View {
  /**
   * 索引列表的背景色
   */
  private int backgroundColor = Color.parseColor("#CDCDCD");

  /**
   * 手指选中时的文本颜色
   */
  private int selectedTextColor = Color.parseColor("#AAAAAA");

  /**
   * 未选中时的文本颜色
   */
  private int unselectedTextColor = Color.parseColor("#222222");

  /**
   * 右边索引的数据源
   */
  private String[] dataList = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
      "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
      "W", "X", "Y", "Z", "#"};

  /**
   * 手指滑动的监听
   */
  private OnScrollListener scrollListener;

  /**
   * 当前手指选中的索引
   */
  private int currentSelectedIndex;


  private int itemHeight;


  public SideBarView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public SideBarView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SideBarView(Context context) {
    super(context);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Paint paint = new Paint();
    int height = getHeight();
    int width = getWidth();

    //计算每个item应该占的高度
    itemHeight = height / (dataList.length);
    for (int i = 0; i < dataList.length; i++) {
      paint.setAntiAlias(true);
      if (i == currentSelectedIndex) {
        //设置手指选中时按下的文字的颜色
        paint.setColor(selectedTextColor);

        //选中的字母放大
        paint.setTextSize(sp2px(18f));
      } else {
        paint.setTextSize(sp2px(14f));
        paint.setColor(unselectedTextColor);
      }
      canvas.drawText(dataList[i], (width - paint.measureText(dataList[i])) / 2, (i + 1) * itemHeight - 8, paint);
      paint.reset();
    }
  }


  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_MOVE:
        moveAction(event);
        break;
      case MotionEvent.ACTION_DOWN:
        if (scrollListener != null) {
          scrollListener.handDown();
        }
        setBackground(new ColorDrawable(backgroundColor));
        moveAction(event);
        break;
      case MotionEvent.ACTION_UP:
        setBackground(new ColorDrawable(Color.TRANSPARENT));
        invalidate();
        currentSelectedIndex = -1;
        if (scrollListener != null) {
          scrollListener.handUp();
        }
        break;
    }
    return true;
  }

  private void moveAction(MotionEvent event) {
    float currentY = event.getY();
    int tmp = (int) Math.ceil(currentY / itemHeight) - 1;
    if (tmp >= 0 && tmp < dataList.length) {
      invalidate();
      currentSelectedIndex = tmp;
      if (scrollListener != null) {
        scrollListener.selectedItem(currentSelectedIndex, dataList[currentSelectedIndex]);
      }
    }
  }

  /**
   * 提供接口让使用者调用
   */
  public interface OnScrollListener {
    /**
     * 当前选中的index项
     * @param index
     * @param item
     */
    void selectedItem(int index, String item);

    /**
     * 手指抬起
     */
    void handUp();

    /**
     * 手指按下
     */
    void handDown();
  }

  public void setOnSelectionListener(OnScrollListener scrollListener) {
    this.scrollListener = scrollListener;
  }

  /**
   * sp转px
   *
   * @param spValue sp的值
   * @return 以px为单位的值
   */
  private int sp2px(float spValue) {
    final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }

  @Override
  public void setBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public void setSelectedTextColor(int selectedTextColor) {
    this.selectedTextColor = selectedTextColor;
  }

  public void setUnselectedTextColor(int unselectedTextColor) {
    this.unselectedTextColor = unselectedTextColor;
  }

  public void setDataList(String[] dataList) {
    this.dataList = dataList;
  }
}
