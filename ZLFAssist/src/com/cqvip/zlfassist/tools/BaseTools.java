package com.cqvip.zlfassist.tools;

import android.app.Activity;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class BaseTools {
	
	/**获取屏幕宽度*/
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
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
