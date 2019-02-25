package jie.com.funnel;


import jie.com.funnellib.FunnelView;

/**
 * Created by hj on 2019/2/25.
 * 说明：使用默认方式创建一个漏斗
 */
public class DefaultActivity extends BaseFunnelActivity {

    @Override
    void initEvent(FunnelView funnelView) {
        funnelView.setChartData(FunnelData.getTenCountData());
    }
}
