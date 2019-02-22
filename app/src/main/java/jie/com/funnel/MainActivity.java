package jie.com.funnel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import jie.com.funnellib.FunnelView;
import jie.com.funnellib.IFunnelData;

/**
 * Created by hj on 2019/2/22.
 * 说明：
 */
public class MainActivity extends AppCompatActivity {

    private int[] colors = new int[]{
            Color.parseColor("#FF5656"), Color.parseColor("#FF854B"),
            Color.parseColor("#FFB240"), Color.parseColor("#FFEC3D"),
            Color.parseColor("#4DD2F9"), Color.parseColor("#52B5FF"),
            Color.parseColor("#5882FF"), Color.parseColor("#5959FF"),
            Color.parseColor("#8359FF"), Color.parseColor("#AC59FF")
    };

    private int[] fourColors = new int[]{
            Color.parseColor("#FF5D5D"), Color.parseColor("#FFB240"),
            Color.parseColor("#52B5FF"), Color.parseColor("#5882FF"),
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunnelView funnelView = findViewById(R.id.funnel);
        List<IFunnelData> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            FunnelData entity = new FunnelData();
            entity.value = "A";
            entity.color = fourColors[i];
            entity.label = i+"";
            data.add(entity);
        }
        funnelView.setChartData(data);
    }
}
