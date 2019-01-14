/*
 * Copyright 2019 Aditya Sharat
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.adityasharat.android.proteus.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
  private final Gson gson;
  private final Type type;
  private TypeAdapter<T> typeAdapter;

  GsonResponseBodyConverter(Gson gson, Type type) {
    this.gson = gson;
    this.type = type;
  }

  private TypeAdapter<T> getAdapter() {
    if (null == typeAdapter) {
      //noinspection unchecked
      typeAdapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(type));
    }
    return typeAdapter;
  }

  @Override
  public T convert(ResponseBody value) throws IOException {
    TypeAdapter<T> adapter = getAdapter();
    JsonReader jsonReader = gson.newJsonReader(value.charStream());
    jsonReader.setLenient(true);
    try {
      return adapter.read(jsonReader);
    } finally {
      value.close();
    }
  }
}