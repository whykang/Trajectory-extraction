# 🚶‍♂️ Trajectory-extraction

基于 **Pedestrian Dead Reckoning (PDR)** 算法与 **蓝牙 iBeacon 信标** 实现的安卓端 **轨迹提取 App**！  
融合惯性传感器与蓝牙定位，适用于室内导航、疏散路径追踪、行为轨迹分析等应用场景。

---

## 🧰 项目概况

- **编译工具**：Android Studio Giraffe | 2022.3.1  
- **JDK版本**：11  
- **Gradle版本**：7.0.1  
- **最低支持安卓版本**：Android 10 (API Level 29)

---

## 📱 功能特点

- ✅ PDR步行轨迹估计
- ✅ 支持蓝牙 iBeacon 辅助定位
- ✅ Kalman滤波融合传感器与蓝牙信号
- ✅ 实时轨迹渲染
- ✅ 可拓展支持多种传感器配置

---

## 🧠 相关论文

本项目研究成果发表于以下论文：

> Wang H, Liu H, Li W.  
> *Crowd evacuation path planning and simulation method based on deep reinforcement learning and repulsive force field*.  
> **Applied Intelligence**, 2025, 55(4): 297.

📌 如果您在学术研究或工程项目中使用本程序，请引用上述论文。

---

## ⚙️ 蓝牙信标配置说明

注意：蓝牙 iBeacon 参考点需要根据实际场景手动设定。

- 修改路径：`MainActivity.java` → `positionHandler` 线程部分  
- 默认版本中未开放蓝牙参考点配置界面，需要手动修改代码

示例代码位置如下图所示：

![蓝牙配置](https://github.com/whykang/Trajectory-extraction/blob/main1/Image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-10-10%20213254.png?raw=true)

---

## 🎬 实际运行效果

<p align="center">
  <img src="https://github.com/whykang/Trajectory-extraction/blob/main1/Image/QQ%E5%9B%BE%E7%89%8720231010210639.gif?raw=true" alt="运行效果" width="480">
</p>

---

## 🔗 第三方库与参考资料

- 📦 [FastBle - 高性能蓝牙通信库](https://github.com/Jasonchenlijian/FastBle)
- 🚶 [PDR 步行轨迹算法参考](https://blog.csdn.net/wxc_1998/article/details/127265887)
- 📐 [Kalman 滤波算法实现](https://github.com/mherb/kalman)

---

## 📁 项目结构（简要）

```
Trajectory-extraction/
├── app/
│   ├── java/com/example/trajectory/
│   │   ├── MainActivity.java         // 主界面与核心逻辑
│   │   ├── KalmanFilter.java         // 滤波算法模块
│   │   └── BluetoothScanner.java     // 蓝牙信号处理
│   └── res/layout/                   // UI 布局文件
├── gradle/
├── build.gradle
└── README.md
```

---

## 🤝 致谢

感谢所有相关开源项目及提供灵感与帮助的开发者们。  
如有建议或问题，欢迎提交 issue 或 PR！

---

## 📬 联系方式

开发者：**Hongyue Wang**  
邮箱：`1649827877@qq.com`  
