package jie.com.funnel;

import jie.com.funnellib.IFunnelData;

/**
 * Created by hj on 2019/2/22.
 * 说明：
 */
public class FunnelData implements IFunnelData {
    public String value;
    public String label;
    public int color;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
