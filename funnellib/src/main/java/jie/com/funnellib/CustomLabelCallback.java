package jie.com.funnellib;

/**
 * Created by hj on 2019/2/22.
 * 说明：开放自定义描述绘制画笔
 */
public interface CustomLabelCallback {
    /**
     * 循环绘制线后面的文字
     * @param mHelper 描述文字辅助类
     * @param index 绘制下标
     * 注意：绘制顺序是从下往上绘制!!!
     */
    void drawText(LabelHelper mHelper,int index);
}
