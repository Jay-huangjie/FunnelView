package jie.com.funnellib;

/**
 * Created by hj on 2019/2/25.
 * 说明：自定义漏斗宽度变化策略
 */
public interface HalfWidthCallback {
    /**
     * 自定义漏斗宽度变化策略
     * @param halfWidth 上一个梯形的宽度，第一个为0
     * @param count 漏斗梯形个数
     * @param i 梯形下标
     * @return 当前梯形的宽度,单位：PX
     */
    float getHalfStrategy(float halfWidth,int count,int i);
}
