package com.cqvip.zlfassist.view;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.activity.AddFavorActivity;
import com.cqvip.zlfassist.activity.DisplayFollowActivity;
import com.cqvip.zlfassist.activity.NotifactionUpdateActivity;
import com.cqvip.zlfassist.download.DownloadList;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.DownloadManager.Request;
import com.mozillaonline.providers.downloads.DownloadService;
import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.fb.FeedbackAgent;
/** 
 * 自定义SlidingMenu 测拉菜单类
 * */
public class DrawerView implements OnClickListener{
	private final Activity activity;
	SlidingMenu localSlidingMenu;
	//private SwitchButton night_mode_btn;
	private View add_btn,down_btn,favor_btn,update_btn,feedback_btn;
	private TextView night_mode_text;
	private RelativeLayout setting_btn;
	DownloadManager mDownloadManager;
	BadgeView badge;
	public static final int RESULT_FOLLOW=1;
	private HashMap<String, Boolean> lists = null;
	FeedbackAgent fb;
	
	public DrawerView(Activity activity) {
		this.activity = activity;
		mDownloadManager = new DownloadManager(activity.getContentResolver(),
				activity.getPackageName());
		startDownloadService();
	}
	
    private void startDownloadService() {
	Intent intent = new Intent();
	intent.setClass(activity, DownloadService.class);
	activity.startService(intent);
    }
    
	public SlidingMenu initSlidingMenu() {
		localSlidingMenu = new SlidingMenu(activity);
		localSlidingMenu.setMode(SlidingMenu.LEFT);//设置左右滑菜单
		localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//设置要使菜单滑动，触碰屏幕的范围
//		localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//设置了这个会获取不到菜单里面的焦点，所以先注释掉
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);//设置阴影图片的宽度
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);//设置阴影图片
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//SlidingMenu划出时主页面显示的剩余宽度
		localSlidingMenu.setFadeDegree(0.35F);//SlidingMenu滑动时的渐变程度
		localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);//使SlidingMenu附加在Activity右边
//		localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//设置SlidingMenu菜单的宽度
//		localSlidingMenu.setMenu(R.layout.left_drawer_fragment);//设置menu的布局文件
//		localSlidingMenu.toggle();//动态判断自动关闭或开启SlidingMenu
		localSlidingMenu.setSecondaryMenu(R.layout.left_drawer_fragment);
		localSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
					public void onOpened() {
						
					}
				});
		localSlidingMenu.setOnClosedListener(new OnClosedListener() {
			
			@Override
			public void onClosed() {
				// TODO Auto-generated method stub
				
			}
		});
		initView();
		//用户反馈
		fb = new FeedbackAgent(activity);
        // check if the app developer has replied to the feedback or not.
        fb.sync();
		return localSlidingMenu;
	}

	public void showUpdateTips(int num){
		badge.setText(""+num);
		badge.show();
	}
	public void setItems(HashMap<String, Boolean> updateList){
		this.lists = updateList;
	}
	public void hideUpdateTips(){
		badge.setVisibility(View.GONE);
	}
	
	private void initView() {
		add_btn=localSlidingMenu.findViewById(R.id.add_btn);
		down_btn=localSlidingMenu.findViewById(R.id.down_btn);
		favor_btn = localSlidingMenu.findViewById(R.id.favor_btn);
		update_btn = localSlidingMenu.findViewById(R.id.mes_btn);
		feedback_btn= localSlidingMenu.findViewById(R.id.feedback_btn);
		TextView tv_refTextView =  (TextView) localSlidingMenu.findViewById(R.id.tv_ref_update);
		 badge = new BadgeView(activity, tv_refTextView);
		add_btn.setOnClickListener(this);
		down_btn.setOnClickListener(this);
		favor_btn.setOnClickListener(this);
		update_btn.setOnClickListener(this);
		feedback_btn.setOnClickListener(this);
		
		//night_mode_btn = (SwitchButton)localSlidingMenu.findViewById(R.id.night_mode_btn);
//		night_mode_text = (TextView)localSlidingMenu.findViewById(R.id.night_mode_text);
//		night_mode_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if(isChecked){
//					night_mode_text.setText(activity.getResources().getString(R.string.action_night_mode));
//				}else{
//					night_mode_text.setText(activity.getResources().getString(R.string.action_day_mode));
//				}
//			}
//		});
//		night_mode_btn.setChecked(false);
//		if(night_mode_btn.isChecked()){
//			night_mode_text.setText(activity.getResources().getString(R.string.action_night_mode));
//		}else{
//			night_mode_text.setText(activity.getResources().getString(R.string.action_day_mode));
//		}
		
//		setting_btn =(RelativeLayout)localSlidingMenu.findViewById(R.id.setting_btn);
//		setting_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_btn:
			activity.startActivityForResult(new Intent(activity,DisplayFollowActivity.class),RESULT_FOLLOW);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.favor_btn:
			activity.startActivity(new Intent(activity,AddFavorActivity.class));
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.down_btn:
			//startDownload("");
			activity.startActivity(new Intent(activity,DownloadList.class));
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.mes_btn:
			hideUpdateTips();
			Intent intent = new Intent(activity,NotifactionUpdateActivity.class);
			intent.putExtra("ids", lists);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.feedback_btn:
			 fb.startFeedbackActivity();
			break;
//		case R.id.setting_btn:
//			activity.startActivity(new Intent(activity,SettingsActivity.class));
//			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//			break;

		default:
			break;
		}
	}
	
	   private void startDownload(String url) {
			//String url = "http://www.pptok.com/wp-content/uploads/2012/06/huanbao-1.jpg";
			url = "http://www.it.com.cn/dghome/img/2009/06/23/17/090623_tv_tf2_13h.jpg";
			//String url = "http://down.mumayi.com/41052/mbaidu";
			Uri srcUri = Uri.parse(url);
			DownloadManager.Request request = new Request(srcUri);
			request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, "/");
			request.setDescription("正在下载");
			 DownloadManager mDownloadManager = new DownloadManager(activity.getContentResolver(),
					 activity.getPackageName());
			mDownloadManager.enqueue(request);
		    }
}
