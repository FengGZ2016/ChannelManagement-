package example.gridlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GridLayout mGridLayout;
    private int index;

    //ctrl+alt+f快熟生成变量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);

    }

    public void addItem(View view) {
        int margin=5;
        //点击时，在GridLayout 添加条目
        TextView textView=new TextView(MainActivity.this);

        textView.setText(index+"");
        //设置背景
        textView.setBackgroundResource(R.drawable.shape_tv_normal);
        //文字居中
        textView.setGravity(Gravity.CENTER);
        GridLayout.LayoutParams layoutParams=new GridLayout.LayoutParams();
       //宽高
        layoutParams.width=getResources().getDisplayMetrics().widthPixels/4-2*margin;//屏幕的宽度四分之一
        layoutParams.height=GridLayout.LayoutParams.WRAP_CONTENT;
        //间距
        layoutParams.setMargins(margin,margin,margin,margin);
        //设置宽高
        textView.setLayoutParams(layoutParams);


        index++;

        mGridLayout.addView(textView,0);
        
    }
}
