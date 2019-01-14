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

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public final class GsonConverterFactory extends Converter.Factory {

  private final Gson gson;

  private GsonConverterFactory(Gson gson) {
    this.gson = gson;
  }

  public static GsonConverterFactory create(Gson gson) {
    return new GsonConverterFactory(gson);
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
    return new GsonResponseBodyConverter<>(gson, type);
  }

  @Override
  public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                        Annotation[] methodAnnotations, Retrofit retrofit) {
    Converter<?, RequestBody> result = null;
    for (Annotation annotation : methodAnnotations) {
      if (annotation instanceof URLEncoded) {
        result = new UrlEncodedGsonRequestBodyConverter<>(gson, type);
      }
    }

    if (null == result) {
      result = new GsonRequestBodyConverter<>(gson, type);
    }

    return result;
  }

  /**
   * URL Encode the JSON String object before sending it over the wire.
   */
  @Documented
  @Target(METHOD)
  @Retention(RUNTIME)
  public @interface URLEncoded {
  }
}
