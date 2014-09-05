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
	/** �Զ���HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	LinearLayout ll_more_columns;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	private ImageView button_more_columns;
	/** �û�ѡ������ŷ����б�*/
	private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
	/** ��ǰѡ�е���Ŀ*/
	private int columnSelectIndex = 0;
	/** ����Ӱ����*/
	public ImageView shade_left;
	/** ����Ӱ���� */
	public ImageView shade_right;
	/** ��Ļ��� */
	private int mScreenWidth = 0;
	/** Item��� */
	private int mItemWidth = 0;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	

	/** head ͷ�� ���м��loading*/
	private ProgressBar top_progress;
	/** head ͷ�� �м��ˢ�°�ť*/
	private ImageView top_refresh;
	/** head ͷ�� �����˵� ��ť*/
	private ImageView top_head;
	/** head ͷ�� ���Ҳ�˵� ��ť*/
	private ImageView top_more;
	/** ����CODE */
	public final static int CHANNELREQUEST = 1;
	/** �������ص�RESULTCODE */
	public final static int CHANNELRESULT = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// һ��Item���Ϊ��Ļ��1/7
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
