# FunnelView
一个漂亮的自定义漏斗View,支持自定义描述，颜色，自动适配高度或自定义高度，宽度也能自动适配，甚至能自己定义宽度的伸缩策略来达到适配效果

## 效果图(Design sketch)
|效果|图片|
|---|---|
|默认效果|![view1.png](https://upload-images.jianshu.io/upload_images/3468978-921867ae19dd3bcf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)|
|自定义描述文字|![view2.png](https://upload-images.jianshu.io/upload_images/3468978-0517933d5ead4902.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)|
|自定义宽度伸缩策略|![view3.png](https://upload-images.jianshu.io/upload_images/3468978-50289a6c88903663.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)|

## 使用(Usering)
### Step1
在你的root build.gradle中添加：
````
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
````
在你的app build.gradle中添加：
````
	dependencies {
	        implementation 'com.github.Jay-huangjie:FunnelView:v1.4'
	}
````
### Step2
如果你只需要简单展示，只需两步即可使用：
1. 数据源继承`IFunnelData`接口
2. 调用`setChartData`方法将数据源设置进去即可
**注意：绘制的顺序是从下往上，所以如果你希望你的数据源是从上往下排列，需要调用`Collections.reverse(List<?> list);`方法将集合结果反转**

### xml属性表(Attribute table)：
|属性|字段|
|---|---|
|lineWidth|线的长度|
|lineStoke|线的粗细|
|lineColor|线的颜色，如果不设置默认跟随梯形的颜色|
|lineTextSpace|字与线的间距|
|lastLineOffset|最下方线的偏移量,最下方线的长度=偏移量* 2|
|totalHeight|每个梯形的目标高度，当高度设置为具体的精确值时此值会失效，每个梯形高度会按照固定高度等分|
|funnelLineStoke|每个梯形之间线的粗细|
|funnelLineColor|每个梯形之间线的颜色|
|labelColor|描述文字的颜色|
|labelSize|描述文字的大小|
|hasLabel|是否需要描述文字 默认打开|


### api使用(Advanced use)
##### 自定义描述文字
参考`CustomLabelActivity`，有两种实现方式：
1. 先实现`CustomLabelCallback`接口，该接口会返回一个描述文字辅助类`LabelHelper`,可以使用该类获取画笔画布自行绘制文字或者使用内部的api来绘制
2. 如果对自定义View熟悉或者需要实现api没有覆盖到的功能，可以从该类中调用get系列的方法获取需要的画布元素来接管描述文字的绘制，其中的坐标已经都计算好了，可以直接使用
3. 如果对自定义View不太熟悉的可以使用`build`方法来构建需要绘制的元素，示例如下：
````
funnelView.addCustomLabelCallback(new CustomLabelCallback() {
            @Override
            public void drawText(LabelHelper mHelper, int index) {
                FunnelData mData = data.get(index);
                mHelper.build(
                        mHelper.getBuild()
                        .setFirstHalfText(mData.label+":")
                        .setFirstHalfTextStyle(mData.color)
                        .setCenterHalfText(mData.num)
                        .setCenterHalfTextStyle(Color.parseColor("#333333"),true)
                        .setFooterHalfText("个")
                        .setFooterTextStyle(Color.parseColor("#333333"))
                );
            }
        });
````
效果见自定义描述文字效果图

api最多支持3种文字的组合，每个组合支持设置不同的颜色和粗细，最后把它们连接到一起，**注意一定要设置颜色**，不然颜色默认为透明

**注意这是在循环中调用此接口绘制**，所以会返回一个下标用于获取指定的数据源

关闭描述文字:调用`setHasLabel`方法关闭

##### 自定义宽度策略
参考`CustomHalfWidthActivity`,通过实现`HalfWidthCallback`接口即可。该接口会返回当前下面一个梯形的新增宽度，总梯形个数以及当前绘制的下标。
返回结果为每一次变化的宽度，类似于一个动画插值器，可以精确控制每个阶段不同的返回值来实现不一样的效果。

通过每次逐渐等比例增加`halfWidth`的宽度，即可实现一个由大变小的漏斗，注意`halfWidth`不能为负，并且每次返回的结果需要比`lastLineOffset`大，这样才能保证线的长度不会为负数，不会绘制絮乱。

#### 更新日志(Update log)
````
2019/2/25
第一稳定版开发完成

2019/7/1
添加隐藏描述文字功能

2019/12/2
添加自定义描述文字Api
````

#### 常见问题
编译不过可尝试将gradle版本调低，本demo为最新版

#### 结束语（End）
开源不易，如果对你有帮助的话，请给我颗小星星噢(*^_^*)
