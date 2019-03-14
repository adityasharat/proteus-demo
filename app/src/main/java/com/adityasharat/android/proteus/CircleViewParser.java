/*
 * Copyright 2019 Aditya Sharat
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.adityasharat.android.proteus;

import android.view.ViewGroup;

import com.adityasharat.android.proteus.customviews.CircleView;
import com.flipkart.android.proteus.ProteusContext;
import com.flipkart.android.proteus.ProteusView;
import com.flipkart.android.proteus.ViewTypeParser;
import com.flipkart.android.proteus.processor.StringAttributeProcessor;
import com.flipkart.android.proteus.value.Layout;
import com.flipkart.android.proteus.value.ObjectValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * CircleViewParser
 *
 * @author aditya.sharat
 */

public class CircleViewParser extends ViewTypeParser<CircleView> {

  @NonNull
  @Override
  public String getType() {
    return "CircleView";
  }

  @Nullable
  @Override
  public String getParentType() {
    return "View";
  }

  @NonNull
  @Override
  public ProteusView createView(@NonNull ProteusContext context, @NonNull Layout layout, @NonNull ObjectValue data, @Nullable ViewGroup parent, int dataIndex) {
    return new CircleView(context);
  }

  @Override
  protected void addAttributeProcessors() {

    addAttributeProcessor("color", new StringAttributeProcessor<CircleView>() {
      @Override
      public void setString(CircleView view, String value) {
        view.setColor(value);
      }
    });
  }
}

