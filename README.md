# Trajectory-extraction

结合PDR算法和蓝牙 iBeancon设备实现安卓端轨迹提取APP！！

编译工具版本：android studio  Giraffe | 2022.3.1

JDK 11 

Gradle 7.0.1

最低支持安卓版本: Android 10 SDK API level 29

## 📄 相关论文

本项目研究成果发表于以下论文：

> Wang H, Liu H, Li W. *Crowd evacuation path planning and simulation method based on deep reinforcement learning and repulsive force field*. **Applied Intelligence**, 2025, 55(4): 297.

如在学术研究或工程实践中使用本程序，请引用以上论文。


注意：iBeancon设备参考点需要根据实际场景自行修改代码设定，具体代码逻辑位于 MainActivity类中的positionHandler线程部分，软件成品中未设置蓝牙自定义功能，如需设置蓝牙信标参考点，请自行修改代码：

![代码](https://github.com/whykang/Trajectory-extraction/blob/main1/Image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-10-10%20213254.png?raw=true)


# References and acknowledgments：

FastBle:

https://github.com/Jasonchenlijian/FastBle

Pedestrian Dead Reckoning：

https://blog.csdn.net/wxc_1998/article/details/127265887

Kalman：

https://github.com/mherb/kalman

实际效果：

![实际运行](https://github.com/whykang/Trajectory-extraction/blob/main1/Image/QQ%E5%9B%BE%E7%89%8720231010210639.gif?raw=true)












