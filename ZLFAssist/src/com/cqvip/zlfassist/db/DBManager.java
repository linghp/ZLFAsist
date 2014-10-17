package com.cqvip.zlfassist.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.cqvip.zlfassist.bean.DownloaderSimpleInfo;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.zkbean.ZKTopic;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.mozillaonline.providers.DownloadManager;

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
	 * 获取阅读路径
	 * @param id
	 * @return
	 */
	public String  getReadPath(String id){
		try {
			Dao<DownloaderSimpleInfo, Integer> favorDao = getHelper().getDownloaderSimpleInfoDao();
			List<DownloaderSimpleInfo>	lists = favorDao.queryForEq("id",id);
			if(lists!=null&&!lists.isEmpty()){
				long downloadid = lists.get(0).getDownloadId();
				Cursor cursor =	getCursor(context,downloadid);
				if(cursor!=null){
					int   mIdColumnId = cursor .getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
					for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
							.moveToNext()) {
						    if (cursor.getLong(mIdColumnId) == downloadid) {
						  	  int  mLocalUriColumnId = cursor
									    .getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI);
							  String path =  cursor.getString(mLocalUriColumnId);
						    	if(!TextUtils.isEmpty(path)){
						    		return path;
						    	}
						    	}
						    }
						    }
				}
			return null;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
	}
	/**
	 * 判断是否下载
	 * @param id
	 * @return
	 */
	public boolean isDownload(String id){
		try {
		Dao<DownloaderSimpleInfo, Integer> favorDao = getHelper().getDownloaderSimpleInfoDao();
		List<DownloaderSimpleInfo>	lists = favorDao.queryForEq("id",id);
		if(lists!=null&&!lists.isEmpty()){
			long downloadid = lists.get(0).getDownloadId();
			Cursor cursor =	getCursor(context,downloadid);
			if(cursor!=null){
				int   mIdColumnId = cursor .getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					    if (cursor.getLong(mIdColumnId) == downloadid) {
					  	  int  mLocalUriColumnId = cursor
								    .getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI);
						  String path =  cursor.getString(mLocalUriColumnId);
					    	if(!TextUtils.isEmpty(path)){
					    		return true;
					    	}
					    	}
					    }
					    }
			}
		return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Cursor getCursor(Context context2, long downloadid) {
		DownloadManager mDownloadManager = new DownloadManager(context2.getContentResolver(),context2.getPackageName());
			mDownloadManager.setAccessAllDownloads(true);
			DownloadManager.Query baseQuery = new DownloadManager.Query()
				.setOnlyIncludeVisibleInDownloadsUi(true);
			return  mDownloadManager.query(baseQuery);
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
	 * 所有已经收藏期刊
	 * @param zkTopic
	 */
	public ArrayList<ZKTopic> queryFavorits() {
		try {
			Dao<ZKTopic, Integer> favorDao = getHelper().getFavorDao();
			ArrayList<ZKTopic> temp = (ArrayList<ZKTopic>) favorDao.queryBuilder().orderBy("datetime", false).query();
			return temp;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
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
	/**
	 * 删除关注对象
	 * @param itemFollows
	 * @return
	 */
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
