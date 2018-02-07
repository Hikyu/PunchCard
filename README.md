# PunchCard

ST自动打卡

## 功能

- 自定义打卡时间段

- 跳过周六日打卡，节假日未跳过

- 微信通知，使用[itchat4j](https://github.com/yaphone/itchat4j)

- 短信通知

## 打包

1. mvn package

2. 使用 target/punchcard.jar 替换 release/punchcard/punchcard.jar

## 使用

1. 配置 config/config.properties

2. 双击 release/punchcard/start.bat，启动打卡程序
