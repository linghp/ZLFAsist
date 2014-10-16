package com.cqvip.zlfassist.db;

import java.sql.SQLException;

import android.content.Context;
import android.widget.Toast;

import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.zkbean.ZKTopic;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class DBManager {

	private DatabaseHelper databaseHelper = null;
	private Context context;
	public DBManager(Context context){
		this.context = context;
	}
	public DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
		}
		return databaseHelper;
	}
	/**
	 * 收藏文章
	 * @param zkTopic
	 */
	public boolean saveTopic(ZKTopic zkTopic) {
		try {
			Dao<ZKTopic, Integer> favorDao = getHelper().getFavorDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			//itemFollows.setDatetime(System.currentTimeMillis());
			favorDao.create(zkTopic);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 判断文章是否已经收藏
	 * @param zkTopic
	 */
	public boolean isFavoriteTopic(ZKTopic zkTopic) {
		try {
			Dao<ZKTopic, Integer> favorDao = getHelper().getFavorDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			//itemFollows.setDatetime(System.currentTimeMillis());
			ZKTopic temp = favorDao.queryForSameId(zkTopic);
			if(temp!=null){
			return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 保存关注对象
	 * @param itemFollows
	 * @return
	 */
	public boolean saveDB(ItemFollows itemFollows) {
		try {
			Dao<ItemFollows, Integer> itemFollowsDao = getHelper()
					.getItemFollowsDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			itemFollows.setDatetime(System.currentTimeMillis());
			itemFollowsDao.create(itemFollows);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteDB(ItemFollows itemFollows) {
		try {
			Dao<ItemFollows, Integer> itemFollowsDao = getHelper()
					.getItemFollowsDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			itemFollowsDao.delete(itemFollows);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
