package jie.com.funnel;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import jie.com.funnellib.FunnelView;
import jie.com.funnellib.IFunnelData;

/**
 * Created by hj on 2019/2/22.
 * 说明：
 */
public class MainActivity extends ListActivity {

    private final String[] data = new String[]{
            "使用默认方式(Use default mode)",
            "自定义描述文字(Custom Description Text)",
            "使用自定义宽度策略(Use custom width policy)",
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                data);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, DefaultActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, CustomLabelActivity.class));
                break;
            case 2:
                startActivity(new Intent(this,CustomHalfWidthActivity.class));
                break;
        }
    }
}
