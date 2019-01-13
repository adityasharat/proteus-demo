/*
 * Copyright 2019 Aditya Sharat
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.adityasharat.android.proteus.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.adityasharat.android.proteus.CircleViewParser;
import com.flipkart.android.proteus.Proteus;
import com.flipkart.android.proteus.ProteusBuilder;
import com.flipkart.android.proteus.Styles;
import com.flipkart.android.proteus.gson.ProteusTypeAdapterFactory;
import com.flipkart.android.proteus.support.design.DesignModule;
import com.flipkart.android.proteus.support.v4.SupportV4Module;
import com.flipkart.android.proteus.support.v7.CardViewModule;
import com.flipkart.android.proteus.support.v7.RecyclerViewModule;
import com.flipkart.android.proteus.value.Layout;
import com.flipkart.android.proteus.value.ObjectValue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import retrofit2.Retrofit;

public class ProteusManager {

  private final ProteusApi api;
  private final Proteus proteus;

  private ObjectValue data;
  private Layout rootLayout;
  private Map<String, Layout> layouts;
  private Styles styles;

  private Set<Listener> listeners = new HashSet<>();

  public ProteusManager(Retrofit retrofit) {
    this.api = retrofit.create(ProteusApi.class);
    proteus = new ProteusBuilder()
      .register(SupportV4Module.create())
      .register(RecyclerViewModule.create())
      .register(CardViewModule.create())
      .register(DesignModule.create())
      .register(new CircleViewParser())
      .build();

    ProteusTypeAdapterFactory.PROTEUS_INSTANCE_HOLDER.setProteus(proteus);
  }

  public void load() {
    new DataLoaderTask(this).execute();
  }

  public void update() {
    new DataLoaderTask(this).execute();
  }

  public Proteus getProteus() {
    return proteus;
  }

  public ObjectValue getData() {
    return data;
  }

  public Layout getRootLayout() {
    return rootLayout;
  }

  public Map<String, Layout> getLayouts() {
    return layouts;
  }

  public Styles getStyles() {
    return styles;
  }

  public void addListener(@NonNull Listener listener) {
    listeners.add(listener);
  }

  public void removeListener(@NonNull Listener listener) {
    listeners.remove(listener);
  }

  private void broadcast(@Nullable Exception e) {
    if (e == null) {
      notifySuccess();
    } else {
      notifyError(e);
    }
  }

  private void notifySuccess() {
    for (Listener listener : listeners) {
      listener.onLoad();
    }
  }

  private void notifyError(@NonNull Exception e) {
    for (Listener listener : listeners) {
      listener.onError(e);
    }
  }

  private static class DataLoaderTask extends AsyncTask<Void, Void, Exception> {

    private final ProteusManager manager;

    DataLoaderTask(ProteusManager manager) {
      this.manager = manager;
    }

    @Override
    protected Exception doInBackground(Void... params) {
      try {
        ProteusApi api = manager.api;
        manager.data = api.getUserData().execute().body();
        manager.rootLayout = api.getLayout().execute().body();
        manager.layouts = api.getLayouts().execute().body();
        manager.styles = api.getStyles().execute().body();
      } catch (Exception e) {
        Log.e(getClass().getSimpleName(), "ERROR: " + e.getMessage());
        return e;
      }
      return null;
    }

    @Override
    protected void onPostExecute(Exception e) {
      super.onPostExecute(e);
      manager.broadcast(e);
    }
  }

  public interface Listener {

    void onLoad();

    void onError(@NonNull Exception e);
  }
}
