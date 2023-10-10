# Trajectory-extraction

结合PDR算法和蓝牙 iBeancon设备实现安卓端轨迹提取APP,遵循Apache License， 仅供参考，请勿商用！！


Latest version：V2.4  Time：2023.10.10   Location：山东师范大学 304 实验室


编译工具版本：android studio  Giraffe | 2022.3.1

语言：JAVA  

JDK 11 

Gradle 7.0.1

最低支持安卓版本: Android 10 SDK API level 29

注意：iBeancon设备参考点需要根据实际场景自行修改代码设定，具体代码逻辑为位于 MainActivity类中的positionHandler线程部分，软件成品中为设置自定义功能，请自行修改代码：

![代码](https://github.com/whykang/Trajectory-extraction/blob/main/Image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-10-10%20213254.png?raw=true)


# References and acknowledgments：

FastBle:

https://github.com/Jasonchenlijian/FastBle

Pedestrian Dead Reckoning：

https://blog.csdn.net/wxc_1998/article/details/127265887

Kalman：

https://github.com/mherb/kalman

实际效果（以文淙楼1楼为背景）：

![实际运行](https://raw.githubusercontent.com/whykang/Trajectory-extraction/main/Image/QQ%E5%9B%BE%E7%89%8720231010210639.gif?token=GHSAT0AAAAAACH4FDXVDGN5YXPBOCDXPE3CZJFJ2WA)












