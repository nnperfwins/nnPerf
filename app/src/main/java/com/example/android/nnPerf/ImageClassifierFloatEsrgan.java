/* Copyright 2018 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.example.android.nnPerf;

import android.app.Activity;

import java.io.IOException;

/** This classifier works with the float MobileNet model. */
public class ImageClassifierFloatEsrgan extends ImageClassifier {

  /** The mobile net requires additional normalization of the used input. */
  private static final float IMAGE_MEAN = 127.5f;

  private static final float IMAGE_STD = 127.5f;

  /**
   * An array to hold inference results, to be feed into Tensorflow Lite as outputs. This isn't part
   * of the super class, because we need a primitive array here.
   */
  private float[][] labelProbArray = null;

  /**
   * Initializes an {@code ImageClassifierFloatMobileNet}.
   *
   * @param activity
   */
  ImageClassifierFloatEsrgan(Activity activity) throws IOException {
    super(activity);
    labelProbArray = new float[1][120000];
  }

  @Override
  protected String getModelPath() {
    // you can download this file from
    // see build.gradle for where to obtain this file. It should be auto
    // downloaded into assets.
    return "esrgan_tf2_1.tflite";
  }

  @Override
  protected String getLabelPath() {
    return "labels_1001.txt";
  }

  @Override
  protected int getImageSizeX() {
    return 50;
  }

  @Override
  protected int getImageSizeY() {
    return 50;
  }

  @Override
  protected int getNumBytesPerChannel() {
    return 4; // Float.SIZE / Byte.SIZE;
  }

//  byte[] inputTest = new byte[268203];
  @Override
  protected void addPixelValue(int pixelValue) {

//    imgData.put(inputTest);
  }

  @Override
  protected float getProbability(int labelIndex) {
    return labelProbArray[0][labelIndex];
  }

  @Override
  protected void setProbability(int labelIndex, Number value) {
    labelProbArray[0][labelIndex] = value.floatValue();
  }

  @Override
  protected float getNormalizedProbability(int labelIndex) {
    return labelProbArray[0][labelIndex];
  }

  @Override
  protected void runInference() {

//    tflite.run(imgData, labelProbArray);
    try {
      new TFLiteModelInfer(tflite).predict();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
