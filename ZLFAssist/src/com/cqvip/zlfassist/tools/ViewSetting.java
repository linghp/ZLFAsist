package com.cqvip.zlfassist.tools;

import android.app.Activity;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.freshlistview.FreshListView;
import com.cqvip.zlfassist.freshlistview.SimpleFooter;
import com.cqvip.zlfassist.freshlistview.SimpleHeader;

public class ViewSetting {

	public static void settingListview(FreshListView listView,Activity activity) {
//		float density = activity.getResources().getDisplayMetrics().density;
//        listView.setFirstTopOffset((int) (50 * density));

        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(activity);
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        listView.setHeadable(header);

        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(activity);
        footer.setCircleColor(0xff33bbee);
        listView.setFootable(footer);

        // 设置列表项出现动画（可选）
        listView.setItemAnimForTopIn(R.anim.topitem_in);
        listView.setItemAnimForBottomIn(R.anim.bottomitem_in);
	}
}
