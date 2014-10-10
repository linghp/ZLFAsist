package com.cqvip.zlfassist.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.zkbean.ZKTopic;

public class BaseTools {
	
	/**获取屏幕宽度*/
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	// 分享
	public static void bookshare_bysharesdk(Context mcontext, ZKTopic mbook,Bitmap bitmap) {
				if (mbook != null&&mcontext!=null) {
			final OnekeyShare oks = new OnekeyShare();
			oks.setNotification(R.drawable.ic_launcher, mcontext.getResources().getString(R.string.app_name));
			oks.setAddress("12345678901");
			oks.setTitle(mcontext.getResources().getString(R.string.share));
			oks.setTitleUrl("http://www.cqvip.com/");
			oks.setText("深职院图书馆友情分享:《" + mbook.getTitleC()+"》");
			oks.setImagePath("");
			//String imageurl=mbook.getCover_path();
//			if(!TextUtils.isEmpty(imageurl)){
//			oks.setImageUrl(imageurl);
//			}
			oks.setUrl("http://oldweb.cqvip.com/downloadcenter/soft/MobleLib.apk");
			//oks.setImage(bitmap);
			//oks.setFilePath(TEST_IMAGE);
			//oks.setComment(this.getString(R.string.share));
			//oks.setSite(this.getString(R.string.app_name));
			//oks.setSiteUrl("http://oldweb.cqvip.com/downloadcenter/soft/MobleLib.apk");
//			oks.setVenueName("Southeast in China");
//			oks.setVenueDescription("This is a beautiful place!");
//			oks.setLatitude(35.4964560f);
//			oks.setLongitude(139.746093f);
			oks.setSilent(false);
//			if (platform != null) {
//				oks.setPlatform(platform);
//			}
			oks.show(mcontext);
		}
		
	}
	
}
