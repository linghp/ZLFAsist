package com.cqvip.zlfassist.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.bean.ChannelItem;
import com.cqvip.zlfassist.tools.BaseTools;
import com.cqvip.zlfassist.view.ColumnHorizontalScrollView;


public class MainActivity extends FragmentActivity {
	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	LinearLayout ll_more_columns;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	private ImageView button_more_columns;
	/** 用户选择的新闻分类列表*/
	private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
	/** 当前选中的栏目*/
	private int columnSelectIndex = 0;
	/** 左阴影部分*/
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	

	/** head 头部 的中间的loading*/
	private ProgressBar top_progress;
	/** head 头部 中间的刷新按钮*/
	private ImageView top_refresh;
	/** head 头部 的左侧菜单 按钮*/
	private ImageView top_head;
	/** head 头部 的右侧菜单 按钮*/
	private ImageView top_more;
	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	/** 调整返回的RESULTCODE */
	public final static int CHANNELRESULT = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		initView();
	}

    private void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
