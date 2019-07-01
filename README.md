# FunnelView
一个漂亮的自定义漏斗View,支持自定义描述，颜色，自动适配高度或自定义高度，宽度也能自动适配，甚至能自己定义宽度的伸缩策略来达到适配效果

### 效果图(Design sketch)
|效果|图片|
|---|---|
|默认效果|![view1.png](https://upload-images.jianshu.io/upload_images/3468978-921867ae19dd3bcf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)|
|自定义描述文字|![view2.png](https://upload-images.jianshu.io/upload_images/3468978-0517933d5ead4902.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)|
|自定义宽度伸缩策略|![view3.png](https://upload-images.jianshu.io/upload_images/3468978-50289a6c88903663.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)|

### 使用(Usering)
#### Step1
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
	        implementation 'com.github.Jay-huangjie:FunnelView:v1.1'
	}
````
#### Step2
如果你只需要默认样式，只需两步即可使用：
1. 数据源继承`IFunnelData`接口
2. 调用`setChartData`方法将数据源设置进去即可
**注意：绘制的顺序是从下往上，所以如果你希望你的数据源是从上往下排列，需要调用`Collections.reverse(List<?> list);`方法将集合结果反转**

#### 属性表(Attribute table)：
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


#### 高级使用(Advanced use)
##### 自定义描述文字
参考`CustomLabelActivity`.实现`CustomLabelCallback`接口，该接口会返回画笔和画布，以及文字的x,y坐标，极易方便扩展
注意这是在循环中调用此接口绘制，所以会返回一个下标用于获取指定的数据源

##### 自定义宽度策略
参考`CustomHalfWidthActivity`,通过实现`HalfWidthCallback`接口即可。该接口会返回当前下面一个梯形的新增宽度，总梯形个数以及当前绘制的下标。
返回结果为每一次变化的宽度，类似于一个动画插值器，可以精确控制每个阶段不同的返回值来实现不一样的效果。
如果不需要描述文字可以调用`setHasLabel`方法关闭

通过每次逐渐等比例增加`halfWidth`的宽度，即可实现一个由大变小的漏斗，注意`halfWidth`不能为负，并且每次返回的结果需要比`lastLineOffset`大，这样才能保证线的长度不会为负数，不会绘制絮乱。

#### 更新日志(Update log)
````
2019/2/25
第一稳定版开发完成

2019/7/1
添加隐藏描述文字功能
````

#### 结束语（End）
开源不易，如果对你有帮助的话，请给我颗小星星噢(*^_^*)
