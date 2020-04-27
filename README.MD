# UntilTheEnd

---

## 提交错误/想法
如果有任何错误或者想法, 请前往 [GitHub Issue](https://github.com/UntilTheEndDev/UntilTheEnd/issues?q=is%3Aissue)
创建一个新的issue前请先确认是否有同样的issue, 不然你的issue可能不会被我们接受

## 开源地址

[GitHub](https://github.com/UntilTheEndDev/UntilTheEnd)

[Coding](https://e.coding.net/untiltheend/UntilTheEnd.git)

## 关于包名带有大写字母的说明

---
别吐槽，Karlatemp 接手的时候已经是这种带大写的了,
由于更正会导致使用UTE提供的API的插件全部爆炸，
所以决定本项目的主包名不再按照
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
来

*不是不想是不能改啊!!!!*

## 下载

你可以在 [GitHub](https://github.com/UntilTheEndDev/UntilTheEndReleases/tree/master/shadow/until-the-end)
或者 [Gitee](https://gitee.com/Karlatemp-bot/UntilTheEndReleases/tree/master/releases) 找到我们的全部构建


## 如何构建

使用 `glt clone` 克隆本仓库到你本机上, 然后打开命令行, 键入 `./gradlew :buildAll`
然后你就可以在 `dist` 中找到构建

## Placeholder

----
这是UTE的全部PAPI可用占位符

| 名称  | 描述 |
|----: | :---- |
|ute_san|理智值|
|ute_tem|温度值|
|ute_hum|湿度值|
|ute_tir|疲劳值|
|ute_season|玩家所在世界的季节名字|
|ute_role|玩家当前直接名字|
|ute_sanmax|玩家最大理智值|
|ute_level|玩家等级|
|ute_damagelevel|玩家伤害缩放系数|
|ute_day|玩家所在世界的第N天|
|ute_saniticolor|理智颜色占位|
|ute_temperaturecolor|温度颜色占位|
|ute_humiditycolor|湿度颜色占位|
|ute_tirednesscolor|疲劳颜色占位|
|ute_seasoncolor|季节颜色占位|
|ute_sanitytend|理智变化箭头占位|
|ute_temperaturetend|温度变化箭头占位|
|ute_humiditytend|湿度变化箭头占位|
|ute_tirednesstend|疲劳变化箭头占位|
|ute_sanitybar|理智条|
|ute_humiditybar|湿度条|
|ute_temperaturebar|温度条|
|ute_tirednessbar|疲劳条|

### 可用模板

---

```text
§7§l理智值：%ute_sanitycolor%%ute_san%%ute_sanitytend%
§7§l温度：%ute_temperaturecolor%%ute_tem%%ute_temperaturetend%
§7§l湿度：%ute_humiditycolor%%ute_hum%%ute_humiditytend%
§7§l季节：%ute_seasoncolor%%ute_season%
§7§l季节天数：%ute_day%
```