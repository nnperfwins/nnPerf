package com.example.android.nnPerf;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.nnapi.NnApiDelegate;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TFLiteModelInfer {
    private static final String TAG = TFLiteModelInfer.class.getName();
    private final Interpreter interpreter;
    int inputCount;
    //    private final TensorBuffer inputTensorBuffer;
//    private final TensorBuffer outputTensorBuffer;
//    Object[] inputs;
//    Map<Integer, Object> output = new HashMap<>();
    private Object inputForOne;
    private TensorBuffer outputForOne;
    private Object[] inputForMulti;
    private Map<Integer, Object> outputForMulti;
    private static final int NUM_THREADS = 1;

    /**
     * @param modelName model name
     */
    public TFLiteModelInfer(String modelName, int delegateNum, Context context) throws Exception {

        try {
            ByteBuffer buffer = loadModelFile(context.getAssets(), modelName);
            Interpreter.Options opt = new Interpreter.Options();
            opt.setNumThreads(NUM_THREADS);
            // 使用Android自带的API或者GPU加速
            if (delegateNum == 0) {
                NnApiDelegate delegate = new NnApiDelegate();
                opt.addDelegate(delegate);
            } else if (delegateNum == 1) {
                GpuDelegate delegate = new GpuDelegate();
                opt.addDelegate(delegate);
            }
//            System.out.println("------------------before------------------");
            interpreter = new Interpreter(buffer, opt);
//            System.out.println("------------------after------------------");

            inputCount = interpreter.getInputTensorCount();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("load model fail!");
        }
    }

    public TFLiteModelInfer(Interpreter tflite) {
        interpreter = tflite;
        inputCount = interpreter.getInputTensorCount();

        try {
            if (inputCount == 1) {
                inputForOne = getInputForOneInput();
                int outputTensorIndex = 0;
                int[] outputShape = interpreter.getOutputTensor(outputTensorIndex).shape();
                DataType outputDataType = interpreter.getOutputTensor(outputTensorIndex).dataType();
                outputForOne = TensorBuffer.createFixedSize(outputShape, outputDataType);
            } else {
                inputForMulti = getInputForMultiInput();
                outputForMulti = new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 执行预测
    public void predict() throws Exception {
        try {
            if (inputCount == 1) {
                interpreter.run(inputForOne, outputForOne.getBuffer().rewind());
            } else {
                interpreter.runForMultipleInputsOutputs(inputForMulti, outputForMulti);
            }
        } catch (Exception e) {
            throw new Exception("predict fail! log:" + e);
        }
    }

    public Object getInputForOneInput() throws Exception {
        int inputTensorIndex = 0;
        int[] inputShape = interpreter.getInputTensor(inputTensorIndex).shape();
        DataType inputDataType = interpreter.getInputTensor(inputTensorIndex).dataType();

        int len = 1;
        for (int j : inputShape) {
            System.out.println(j);
            len *= j;
        }
        Random random = new Random();
        TensorBuffer inputTensorBuffer = TensorBuffer.createFixedSize(inputShape, inputDataType);
        if (inputDataType == DataType.FLOAT32) {
            float[] input = new float[len];
            for (int i = 0; i < len; i++) {
                input[i] = random.nextFloat();
            }
            inputTensorBuffer.loadArray(input);
        } else if (inputDataType == DataType.UINT8) {
            int[] input = new int[len];
            for (int i = 0; i < len; i++) {
                input[i] = random.nextInt(0xff);
            }
            inputTensorBuffer.loadArray(input);
        } else {
            throw new Exception("The data type is not supported!");
        }
        return inputTensorBuffer.getBuffer();
    }

    public Object[] getInputForMultiInput() throws Exception {
        Object[] inputs = new Object[inputCount];
        for (int inputTensorIndex = 0; inputTensorIndex < inputCount; inputTensorIndex++) {
            int[] inputShape = interpreter.getInputTensor(inputTensorIndex).shape();
            DataType inputDataType = interpreter.getInputTensor(inputTensorIndex).dataType();
            int len = 1;
            for (int j : inputShape) {
                System.out.println(j);
                len *= j;
            }
            Random random = new Random();
            if (inputDataType == DataType.FLOAT32) {
                float[] input = new float[len];
                for (int i = 0; i < len; i++) {
                    input[i] = random.nextFloat();
                }
                inputs[inputTensorIndex] = input;
            } else if (inputDataType == DataType.UINT8) {
                int[] input = new int[len];
                for (int i = 0; i < len; i++) {
                    input[i] = random.nextInt(0xff);
                }
                inputs[inputTensorIndex] = input;
            } else if (inputDataType == DataType.INT32) {
                int[] input = new int[len];
                for (int i = 0; i < len; i++) {
                    input[i] = random.nextInt(0xff);
                }
                inputs[inputTensorIndex] = input;
            } else {
                throw new Exception("The data type is not supported!");
            }
        }
        return inputs;
    }

    /** Load tflite model from assets. */
    public MappedByteBuffer loadModelFile(AssetManager assetManager, String modelName) throws IOException {
        try (AssetFileDescriptor fileDescriptor = assetManager.openFd(modelName);
             FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }
}
