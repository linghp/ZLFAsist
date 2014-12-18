package com.cqvip.zlfassist.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.zkbean.ZKTopic;

public class BaseTools {
	
	/**获取屏幕宽度*/
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
	 * dip转为 px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 *  px 转为 dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	// 分享
	public static void bookshare_bysharesdk(Context mcontext, ZKTopic mbook,Bitmap bitmap) {
				if (mbook != null&&mcontext!=null) {
			final OnekeyShare oks = new OnekeyShare();
			oks.setNotification(R.drawable.ic_launcher, mcontext.getResources().getString(R.string.app_name));
			//oks.setAddress("12345678901");
			oks.setTitle(mcontext.getResources().getString(R.string.share));
			oks.setTitleUrl("http://www.cqvip.com/");
			oks.setText("中文期刊助手友情分享:《" + mbook.getTitleC()+"》  "+C.ARTICLE_DETAIL_PRE+mbook.getId());
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
			// 令编辑页面显示为Dialog模式
			oks.setDialogMode();
			oks.setSilent(false);
//			if (platform != null) {
//				oks.setPlatform(platform);
//			}
			oks.show(mcontext);
		}
		
	}
	
	
	/**
	 * 返回带书名号的字符串
	 * @param 名称
	 * @return
	 */
	public static  String formPero(String mediaC) {
		if(!TextUtils.isEmpty(mediaC)){
			
			return "《"+mediaC+"》";
		}else{
			return "";
		}
	}
	/**
	 * 返回正文或者”“
	 * @param content
	 * @return
	 */
	public static  String formContent(String content){
		if(!TextUtils.isEmpty(content)){
				return content;
			}else{
				return "";
			}
		}
	/**
	 * 返回，和正文组合
	 * @param content
	 * @return
	 */
	public static  String formSecendContent(String content){
		if(!TextUtils.isEmpty(content)){
			return "，"+content;
		}else{
			return "";
		}
	}
	/**
	 * 返回标签和标签后的内容
	 * @param content
	 * @param tag
	 * @return
	 */
	public  static String formaddTips(String content,String tag){
		if(!TextUtils.isEmpty(content)){
				return tag+content;
			}else{
				return "";
			}
		}
	/**
	 * 返回标签和标签后的内容
	 * @param tag 标签
	 * @param cotent 内容
	 * @return
	 */
	public  static String addTips(String tag,String content){
		if(!TextUtils.isEmpty(content)){
			return tag+content;
		}else{
			return "";
		}
	}
	/**
	 * 返回[标签]
	 * @param content
	 * @return
	 */
	public  static String formaaddBracket(String content){
		if(!TextUtils.isEmpty(content)){
			return "["+content+"]";
		}else{
			return "";
		}
	}
}
