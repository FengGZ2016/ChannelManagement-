package example.gridlayoutdemo;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 作者：国富小哥
 * 日期：2017/9/27
 * Created by Administrator
 *
 * 1,定义出一个setItems方法，动态的将每个频道的标题数据集合传递进来之后，按照数据进行展示
 * 2，定义出一个setAlLowDrag方法，控制控件是否可以进行拖拽操作
 */

public class DragedGridLayout extends GridLayout{

    private static final int mColumnCount=4;//列数
    private boolean allowdrag;//记录当前控件是否可以进行拖拽操作

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


    /**
     * 动态的将每个频道的标题数据集合传递进来之后，按照数据进行展示
     * @param items
     */
    public void setItems(List<String> items){
        for (String item : items) {
            addItem(item);
        }

    }


    /**
     * 控制控件是否可以进行拖拽操作
     * @param allowDrag
     */
    public void setAlLowDrag(boolean allowDrag){
        this.allowdrag=allowDrag;

        //如果允许拖拽，就设置监听
        if (this.allowdrag){
            this.setOnDragListener(odl);
        }else{
            //否则，就直接传空
            this.setOnDragListener(null);
        }
    }


    /*
   * 拖拽监听
   * */
    private View.OnDragListener odl=new View.OnDragListener() {
        /**
         * 快捷注释：/**+回车
         * @param v 当前监听拖拽事件的view
         * @param event 拖拽事件
         * @return
         */
        @Override
        public boolean onDrag(View v, DragEvent event) {

            //矩形控件，可以传入一个坐标，矩形等，用来判断矩形内是否包含被传入的东西
//            Rect rect=new Rect();
//            rect.contains();

            switch (event.getAction()){
                //当拖拽事件开始时，创建出与子控件对应的矩形数组
                case DragEvent.ACTION_DRAG_STARTED:
                    initRects();
                    break;

                case DragEvent.ACTION_DRAG_LOCATION:
                    //手指移动时，实时判断触摸点是否进入了某一个子控件
                    int touchIndex=getTouchIndex(event);

                    //判断被拖拽的视图与进入的子控件对象不是同一个的时候才进行删除添加操作
                    if (touchIndex>-1&&dragedView!=null&&dragedView!=DragedGridLayout.this.getChildAt(touchIndex)){
                        //说明触摸点已经进入了某个子控件的范围内了
                        //先将被拖拽的视图删除
                        DragedGridLayout.this.removeView(dragedView);
                        //然后将被拖拽的视图添加到进入的子控件的索引处
                        DragedGridLayout.this.addView(dragedView,touchIndex);

                    }
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    //拖拽事件结束后，让被拖拽的view设置为可用，否则背景变红，并且长按事件会失效
                    if (dragedView!=null){
                        dragedView.setEnabled(true);
                    }
                    break;
            }
            return true;
        }
    };


    //创建一个矩形数组存储矩形
    private Rect[] mRect;

    /**
     * 创建出与子控件对应的矩形数组
     */
    private void initRects() {
        mRect=new Rect[this.getChildCount()];


        //快捷for循环：fori+回车键
        for (int i = 0; i <this.getChildCount() ; i++) {
            //获取GridLayout的子控件
            View childView=this.getChildAt(i);
            //为每一个子控件创建一个矩形
            Rect rect=new Rect(childView.getLeft(),childView.getTop(),childView.getRight(),childView.getBottom());

            mRect[i]=rect;

        }

    }


    /**
     *  手指移动时，实时判断触摸点是否进入了某一个子控件
     * @param event
     * @return
     */
    private int getTouchIndex(DragEvent event) {

        //遍历所有的矩形数组，如果包含了当前的触摸点，就返回当前的数组索引
        for (int i = 0; i < mRect.length; i++) {
            Rect rect=mRect[i];
            if (rect.contains((int) event.getX(), (int) event.getY())){
                return i;
            }

        }
        return -1;
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


        if (this.allowdrag){
            //长按事件
            textView.setOnLongClickListener(olcl);
        }else {
            textView.setOnLongClickListener(null);
        }

        return textView;
    }

    private View dragedView;
    /*
           * 长按监听
           * */
    private View.OnLongClickListener olcl=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            //被拖拽的视图其实就是v参数
            dragedView=v;


            //长按时，执行拖拽操作，显示出阴影
            v.startDrag(null,new View.DragShadowBuilder(v),null,0);//已过期
            //1,data 2,shadowBuilder 3,myLocalstate 4,flags
            //v.startDragAndDrop(null,new View.DragShadowBuilder(v),null,0);

            //text view被拖拽时设置不可用
            v.setEnabled(false);
            return false;
        }
    };
}
