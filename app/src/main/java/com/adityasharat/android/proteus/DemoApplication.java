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

import android.app.Application;

import com.adityasharat.android.proteus.api.ProteusManager;
import com.adityasharat.android.proteus.converter.GsonConverterFactory;
import com.flipkart.android.proteus.gson.ProteusTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;

public class DemoApplication extends Application {

  private static final String BASE_URL = "http://10.0.2.2:8080/data/";

  private Gson gson;
  private Retrofit retrofit;
  private ProteusManager proteusManager;

  @Override
  public void onCreate() {
    super.onCreate();

    // register the proteus type adapter to deserialize proteus resources
    ProteusTypeAdapterFactory adapter = new ProteusTypeAdapterFactory(this);
    gson = new GsonBuilder()
      .registerTypeAdapterFactory(adapter)
      .create();

    // add gson to retrofit to allow deserializing proteus resources when fetched via retrofit
    retrofit = new Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(getGson()))
      .build();

    // ProteusManager is a reference implementation to fetch and
    // update all proteus resources from a remote server
    proteusManager = new ProteusManager(getRetrofit());
  }

  public Gson getGson() {
    return gson;
  }

  public Retrofit getRetrofit() {
    return retrofit;
  }

  public ProteusManager getProteusManager() {
    return proteusManager;
  }
}
