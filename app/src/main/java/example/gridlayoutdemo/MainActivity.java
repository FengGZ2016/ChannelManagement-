package example.gridlayoutdemo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * 长按阴影显示后，实时获取触摸点（x,y），
 * 在移动时，实时判断触摸点是否进入了某个子控件范围内
 * 如果进入了先将被拖拽的视图删除
 * 然后将被拖拽的视图添加到进入的子控件的索引处
 */
public class MainActivity extends AppCompatActivity {
    private GridLayout mGridLayout;
    private int index;
    private final String TAG="MainActivity";

    private View dragedView;
    private DragedGridLayout mDragedGridLayout;
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
            String dragEvenAction = getDragEvenAction(event);
            Log.d(TAG,dragEvenAction);

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
                    if (touchIndex>-1&&dragedView!=null&&dragedView!=mGridLayout.getChildAt(touchIndex)){
                        //说明触摸点已经进入了某个子控件的范围内了
                        //先将被拖拽的视图删除
                        mGridLayout.removeView(dragedView);
                        //然后将被拖拽的视图添加到进入的子控件的索引处
                        mGridLayout.addView(dragedView,touchIndex);

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

    //创建一个矩形数组存储矩形
    private Rect[] mRect;

    /**
     * 创建出与子控件对应的矩形数组
     */
    private void initRects() {
        mRect=new Rect[mGridLayout.getChildCount()];


        //快捷for循环：fori+回车键
        for (int i = 0; i <mGridLayout.getChildCount() ; i++) {
            //获取GridLayout的子控件
            View childView=mGridLayout.getChildAt(i);
            //为每一个子控件创建一个矩形
            Rect rect=new Rect(childView.getLeft(),childView.getTop(),childView.getRight(),childView.getBottom());

            mRect[i]=rect;

        }

    }

//     (DragEvent.ACTION_DRAG_STARTED,"STARTED");当拖拽操作时，就会执行一次(能拿到实时的xy坐标)
//      (DragEvent.ACTION_DRAG_ENDED,"ENDED");当拖拽事件结束时，手指抬起时，执行一次
//      (DragEvent.ACTION_DRAG_ENTERED,"ENTERED");当手指进入设置了拖拽监听的空间范围内的瞬间执行一次
//   (DragEvent.ACTION_DRAG_EXITED,"EXITED");当手指离开设置了拖拽监听的控件范围内的瞬间执行一次
//      (DragEvent.ACTION_DRAG_LOCATION,"LOCATION");当手指在设置了拖拽监听的控件范围内，移动时，实时会执行，执行N次(能拿到实时的xy坐标)
//      (DragEvent.ACTION_DROP,"DROP");当手指设置了拖拽监听的控件范围内松开时，执行一次(能拿到实时的xy坐标)

    /**
     * 相当于hashMap
     */
    static SparseArray<String> dragEvenType =new SparseArray<String>();
    static {
        dragEvenType.put(DragEvent.ACTION_DRAG_STARTED,"STARTED");
        dragEvenType.put(DragEvent.ACTION_DRAG_ENDED,"ENDED");
        dragEvenType.put(DragEvent.ACTION_DRAG_ENTERED,"ENTERED");
        dragEvenType.put(DragEvent.ACTION_DRAG_EXITED,"EXITED");
        dragEvenType.put(DragEvent.ACTION_DRAG_LOCATION,"LOCATION");
        dragEvenType.put(DragEvent.ACTION_DROP,"DROP");
    }

    public static String getDragEvenAction(DragEvent de){
        return dragEvenType.get(de.getAction());
    }



    //ctrl+alt+f快熟生成变量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);

        mDragedGridLayout = (DragedGridLayout) findViewById(R.id.draged_grid_layout);

        //监听拖拽事件
        mGridLayout.setOnDragListener(odl);

    }

    public void addItem(View view) {
        int margin=5;
        //点击时，在GridLayout 添加条目
        TextView textView=new TextView(MainActivity.this);

        textView.setText(index+"");
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

        index++;

        //设置长按点击事件
        textView.setOnLongClickListener(olcl);

        mGridLayout.addView(textView,0);
        
    }
}
