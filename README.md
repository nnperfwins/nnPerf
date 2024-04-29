# nnPerf + TFLite Android Image Classifier App Demo

[Homepage](https://nnperfwins.github.io/) |
[Video Demo](https://nnperfwins.github.io/#key_features) |
[Visualization tool](https://nnperfwins.github.io/#online_tool) |
[Citation](https://github.com/nnperfwins/nnPerf#Citation) |

nnPerf is a real-time on-device profiler designed to collect and analyze the DNN model runtime inference latency on mobile platforms. nnPerf demystifies the hidden layers and metrics used for pursuing DNN optimizations and adaptations at the granularity of operators and kernels, ensuring every facet contributing to a DNN model's runtime efficiency is easily accessible to mobile developers via well-defined APIs.
With nnPerf, the mobile developers can easily identify the bottleneck in model run-time efficiency and optimize the model architecture to meet system-level objectives (SLO).
For more design details, please refer to our Sensys 2023 paper.

## [Key Features](https://nnperfwins.github.io/#key_features)

- **Plug-and-play design principles**
- **Real-time on-device profiling**
- **Support measuring fine-grained information at the GPU kernel level**


## To Install / Run profile

### Quick start with apk

It is recommended to run nnPerf on system versions below Android 12.

1. Download nnPerf_v1.1.apk from the release version.

2. Use adb to connect to smartphones or mobile platforms (Android basic system).

3. Install the nnPerf_v1.1.apk.

	```shell
	adb install -t '.\nnPerfAPKinstaller\nnPerf_v1.0.apk'
	```

### Build project

1. Install Android Studio 3.6.3 (Runtime version: 1.8.0_212-release-1586-b04 amd64)

2. Import Project

	File -> Open -> Current file directory 

3. Android Studio Setting

```shell
	Android Gradle Plugin Version: 3.1.3

	Gradle Version:                4.4

	NDK Version:                   21.0.6113669

	JDK Verison:                   1.8.0_211

	Complile Sdk Version:          27

	Build Tools Version:           27.0.3

```

4. Run to profile

```shell
	Update Output path: /data/data/com.example.android.nnPerf/
```

5. Note: In the case of GPU reasoning, do not directly switch models.

6. Model support list (Support for adding other .tflite models)

```shell
	mobilenetV3-Large-Float

	mobilenetV3-Small-Float

	mobilenetV2-Float

	mobilenetV1-Float

	Squeezenet-Float

	EfficientNet-b0-Float

	MNasNet-1.0

	Densenet-Float

	mobilenetV1-Quant

	MobileBert

	SSDMobileV2

	Esrgan
```

## Citation
If you find nnPerf useful in your research, please consider citing:

```bibtex
	@inproceedings{nnPerf,
		author = {Chu, Haolin and Zheng, Xiaolong and Liu, Liang and Ma, Huadong},
		title = {nnPerf: Demystifying DNN Runtime Inference Latency on Mobile Platforms},
		year = {2024},
		publisher = {Association for Computing Machinery},
		address = {New York, NY, USA},
		url = {https://doi.org/10.1145/3625687.3625797},
		doi = {10.1145/3625687.3625797},
		booktitle = {Proceedings of the 21st ACM Conference on Embedded Networked Sensor Systems},
		pages = {125–137},
	}
```
