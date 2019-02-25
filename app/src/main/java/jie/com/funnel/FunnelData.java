package jie.com.funnel;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import jie.com.funnellib.IFunnelData;

/**
 * Created by hj on 2019/2/22.
 * 说明：
 */
public class FunnelData implements IFunnelData {
    public String label;
    public int color;

    public String num;

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public String getLabel() {
        return label;
    }


    //-----------------------------测试数据--------------------------------------------------------------
    private static int[] colors = new int[]{
            Color.parseColor("#FF5656"), Color.parseColor("#FF854B"),
            Color.parseColor("#FFB240"), Color.parseColor("#FFEC3D"),
            Color.parseColor("#4DD2F9"), Color.parseColor("#52B5FF"),
            Color.parseColor("#5882FF"), Color.parseColor("#5959FF"),
            Color.parseColor("#8359FF"), Color.parseColor("#AC59FF")
    };

    private static String[] labels = new String[]{
            "Android", "ios",
            "php", "c",
            "c++", "python",
            "golang", "java",
            "javascript", ".net"
    };


    private static int[] fourColors = new int[]{
            Color.parseColor("#FF5D5D"), Color.parseColor("#FFB240"),
            Color.parseColor("#52B5FF"), Color.parseColor("#5882FF"),
    };

    private static String[] fourLabel = new String[]{
            "数学", "语文", "物理", "化学"
    };

    public static List<FunnelData> getFourCountData() {
        List<FunnelData> data = new ArrayList<>();
        for (int i = 0; i < fourLabel.length; i++) {
            FunnelData funnelData = new FunnelData();
            funnelData.label = fourLabel[i];
            funnelData.color = fourColors[i];
            funnelData.num = String.valueOf(i);
            data.add(funnelData);
        }
        return data;
    }

    public static List<FunnelData> getTenCountData() {
        List<FunnelData> data = new ArrayList<>();
        for (int i = labels.length - 1; i >= 0; i--) {
            FunnelData funnelData = new FunnelData();
            funnelData.label = labels[i];
            funnelData.color = colors[i];
            funnelData.num = String.valueOf(i);
            data.add(funnelData);
        }
        return data;
    }
}
