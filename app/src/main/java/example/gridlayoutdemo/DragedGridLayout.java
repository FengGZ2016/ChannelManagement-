package example.gridlayoutdemo;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 作者：国富小哥
 * 日期：2017/9/27
 * Created by Administrator
 *
 * 1,定义出一个setItems方法，动态的将每个频道的标题数据集合传递进来之后，按照数据进行展示
 */

public class DragedGridLayout extends GridLayout{

    private static final int mColumnCount=4;//列数

    /**
     * 第一个构造方法调用第二个构造方法
     * @param context
     */
    public DragedGridLayout(Context context) {
        this(context,null);
    }


    /**
     * 第二个构造方法调用调用第三个构造方法
     * @param context
     * @param attrs
     */
    public DragedGridLayout(Context context, AttributeSet attrs) {

        this(context, attrs,0);
    }


    public DragedGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        
    }

    private void init() {
//        android:columnCount="4"
//        android:animateLayoutChanges="true"

        //设置列数
        this.setColumnCount(mColumnCount);
        //设置动画效果
        this.setLayoutTransition(new LayoutTransition());

    }

    public void setItems(List<String> items){
        for (String item : items) {
            addItem(item);
        }

    }

    private void addItem(String item) {
        TextView textView=newItemView();
        textView.setText(item);
        this.addView(textView);

    }

    private TextView newItemView() {
        int margin=5;
        //点击时，在GridLayout 添加条目
        TextView textView=new TextView(getContext());
        //设置背景
        textView.setBackgroundResource(R.drawable.selector_tv);
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

        return textView;
    }
}
