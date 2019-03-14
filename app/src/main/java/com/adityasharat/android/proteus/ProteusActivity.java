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

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adityasharat.android.proteus.api.ProteusManager;
import com.adityasharat.android.proteus.utils.GlideApp;
import com.adityasharat.android.proteus.utils.ImageLoaderTarget;
import com.flipkart.android.proteus.LayoutManager;
import com.flipkart.android.proteus.ProteusContext;
import com.flipkart.android.proteus.ProteusLayoutInflater;
import com.flipkart.android.proteus.ProteusView;
import com.flipkart.android.proteus.StyleManager;
import com.flipkart.android.proteus.Styles;
import com.flipkart.android.proteus.exceptions.ProteusInflateException;
import com.flipkart.android.proteus.value.DrawableValue;
import com.flipkart.android.proteus.value.Layout;
import com.flipkart.android.proteus.value.ObjectValue;
import com.flipkart.android.proteus.value.Value;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProteusActivity extends AppCompatActivity implements ProteusManager.Listener {

  private ProteusManager proteusManager;
  private ProteusLayoutInflater layoutInflater;

  ObjectValue data;
  Layout layout;
  Styles styles;
  Map<String, Layout> layouts;

  private StyleManager styleManager = new StyleManager() {

    @Nullable
    @Override
    protected Styles getStyles() {
      return styles;
    }
  };

  private LayoutManager layoutManager = new LayoutManager() {

    @Nullable
    @Override
    protected Map<String, Layout> getLayouts() {
      return layouts;
    }
  };

  /**
   * Simple implementation of ImageLoader for loading images from url in background.
   */
  private ProteusLayoutInflater.ImageLoader loader = new ProteusLayoutInflater.ImageLoader() {
    @Override
    public void getBitmap(ProteusView view, String url, final DrawableValue.AsyncCallback callback) {
      GlideApp.with(ProteusActivity.this)
        .load(url)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.image_broken)
        .into(new ImageLoaderTarget(callback));
    }
  };

  /**
   * Implementation of Callback. This is where we get callbacks from proteus regarding
   * errors and events.
   */
  private ProteusLayoutInflater.Callback callback = new ProteusLayoutInflater.Callback() {

    @NonNull
    @Override
    public ProteusView onUnknownViewType(ProteusContext context, String type, Layout layout, ObjectValue data, int index) {
      // TODO: instead return some implementation of an unknown view
      throw new ProteusInflateException("Unknown view type '" + type + "' cannot be inflated");
    }

    @Override
    public void onEvent(String event, Value value, ProteusView view) {
      Log.i("ProteusEvent", value.toString());
    }
  };

  ProteusView view;
  private ViewGroup container;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_proteus);

    // set the toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // setBoolean refresh button click
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        alert();
      }
    });

    container = findViewById(R.id.content_main);

    DemoApplication application = (DemoApplication) getApplication();
    proteusManager = application.getProteusManager();

    ProteusContext context = proteusManager.getProteus().createContextBuilder(this)
      .setLayoutManager(layoutManager)
      .setCallback(callback)
      .setImageLoader(loader)
      .setStyleManager(styleManager)
      .build();

    layoutInflater = context.getInflater();
  }

  @Override
  protected void onStart() {
    super.onStart();
    proteusManager.addListener(this);
    proteusManager.load();
  }

  @Override
  protected void onPause() {
    super.onPause();
    proteusManager.removeListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.reload:
        reload();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onLoad() {
    data = proteusManager.getData();
    layout = proteusManager.getRootLayout();
    layouts = proteusManager.getLayouts();
    styles = proteusManager.getStyles();
    render();
  }

  @Override
  public void onError(@NonNull Exception e) {
    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
  }

  private void alert() {
    ProteusView view = layoutInflater.inflate("AlertDialogLayout", data);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(view.getAsView())
      .setPositiveButton(R.string.action_alert_ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          dialogInterface.dismiss();
        }
      })
      .show();
  }

  void render() {

    // remove the current view
    container.removeAllViews();

    // Inflate a new view using proteus
    long start = System.currentTimeMillis();
    view = layoutInflater.inflate(layout, data, container, 0);
    System.out.println("inflate time: " + (System.currentTimeMillis() - start));

    // Add the inflated view to the container
    container.addView(view.getAsView());
  }

  void reload() {
    proteusManager.update();
  }
}
