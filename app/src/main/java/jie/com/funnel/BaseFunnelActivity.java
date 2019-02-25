package jie.com.funnel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import jie.com.funnellib.FunnelView;

/**
 * Created by hj on 2019/2/25.
 * 说明：
 */
public abstract class BaseFunnelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        FunnelView funnelVie = findViewById(R.id.funnelView);
        initEvent(funnelVie);
    }

    abstract void initEvent(FunnelView funnelView);
}
