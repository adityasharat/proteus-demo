/*
 * Copyright 2019 Aditya Sharat
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.adityasharat.android.proteus.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.flipkart.android.proteus.ProteusView;

/**
 * CircleView
 *
 * @author aditya.sharat
 */

public class CircleView extends View implements ProteusView {

  Manager viewManager;
  private String HEX_COLOR = "#45ba8a";
  private Paint drawPaint;
  float radius;

  public CircleView(Context context) {
    super(context);
    init();
  }

  public CircleView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public void setColor(String HEX_COLOR) {
    this.HEX_COLOR = HEX_COLOR;
  }

  @Override
  public Manager getViewManager() {
    return viewManager;
  }

  @Override
  public void setViewManager(@NonNull Manager viewManager) {
    this.viewManager = viewManager;
  }

  private void init() {
    drawPaint = new Paint();
    drawPaint.setColor(Color.parseColor(HEX_COLOR));
    drawPaint.setAntiAlias(true);
    setOnMeasureCallback();
  }

  @Override
  protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawCircle(radius, radius, radius, drawPaint);
  }

  private void setOnMeasureCallback() {
    ViewTreeObserver vto = getViewTreeObserver();
    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        removeOnGlobalLayoutListener(this);
        radius = getMeasuredWidth() / 2;
      }
    });
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  void removeOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener listener) {
    if (Build.VERSION.SDK_INT < 16) {
      //noinspection deprecation
      getViewTreeObserver().removeGlobalOnLayoutListener(listener);
    } else {
      getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }
  }

  @NonNull
  @Override
  public View getAsView() {
    return this;
  }

}
