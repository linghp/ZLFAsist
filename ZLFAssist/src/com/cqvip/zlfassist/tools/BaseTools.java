package com.cqvip.zlfassist.tools;

import android.app.Activity;
import android.util.DisplayMetrics;

public class BaseTools {
	
	/**获取屏幕宽度*/
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
}
