/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cqvip.zlfassist.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cqvip.zlfassist.R;
import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.downloads.ui.DownloadAdapter;
import com.mozillaonline.providers.downloads.ui.DownloadItem.DownloadSelectListener;

/**
 * View showing a list of all downloads the Download Manager knows about.
 * 没有暂停下载
 */
public class DownloadList extends ActionBarActivity implements 
	OnItemClickListener, DownloadSelectListener, OnClickListener,
	OnCancelListener,OnItemLongClickListener{
    private static final String LOG_TAG = "DownloadList";

    private ListView mListView;
    private View mEmptyView;
    private ViewGroup mSelectionMenuView;
    private Button mSelectionDeleteButton;

    private DownloadManager mDownloadManager;
    private Cursor mCursor;
    private DownloadAdapter mAdapter;
//    private MyContentObserver mContentObserver = new MyContentObserver();
//    private MyDataSetObserver mDataSetObserver = new MyDataSetObserver();

    private int mStatusColumnId;
    private int mIdColumnId;
    private int mLocalUriColumnId;
    private int mMediaTypeColumnId;
    private int mReasonColumndId;

    //private boolean mIsSortedBySize = false;
    private Set<Long> mSelectedIds = new HashSet<Long>();

    /**
     * We keep track of when a dialog is being displayed for a pending download,
     * because if that download starts running, we want to immediately hide the
     * dialog.
     */
    private Long mQueuedDownloadId = null;
    private AlertDialog mQueuedDialog;

    private boolean[] isEditStatus={false};
    
    @Override
    public void onCreate(Bundle icicle) {
	super.onCreate(icicle);
	setupViews();
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	mDownloadManager = new DownloadManager(getContentResolver(),
		getPackageName());
	mDownloadManager.setAccessAllDownloads(true);
	DownloadManager.Query baseQuery = new DownloadManager.Query()
		.setOnlyIncludeVisibleInDownloadsUi(true);
	mCursor = mDownloadManager.query(baseQuery);

	// only attach everything to the listbox if we can access the download
	// database. Otherwise,
	// just show it empty
	if (haveCursors()) {
	    startManagingCursor(mCursor);

	    mStatusColumnId = mCursor
		    .getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
	    mIdColumnId = mCursor
		    .getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
	    mLocalUriColumnId = mCursor
		    .getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI);
	    mMediaTypeColumnId = mCursor
		    .getColumnIndexOrThrow(DownloadManager.COLUMN_MEDIA_TYPE);
	    mReasonColumndId = mCursor
		    .getColumnIndexOrThrow(DownloadManager.COLUMN_REASON);

	    mAdapter = new DownloadAdapter(this, mCursor,
		    this,isEditStatus);
	    mListView.setAdapter(mAdapter);

	}

	chooseListToShow();
    }


    private void setupViews() {
	setContentView(R.layout.download_list);
	setTitle(getText(R.string.download_title));


	mListView = (ListView) findViewById(R.id.size_ordered_list);
	mListView.setOnItemClickListener(this);
	mListView.setOnItemLongClickListener(this);
	mEmptyView = findViewById(R.id.empty);

	mSelectionMenuView = (ViewGroup) findViewById(R.id.selection_menu);
	mSelectionDeleteButton = (Button) findViewById(R.id.selection_delete);
	mSelectionDeleteButton.setOnClickListener(this);

	((Button) findViewById(R.id.deselect_all)).setOnClickListener(this);
    }

    private boolean haveCursors() {
	return  mCursor != null;
    }

    @Override
    protected void onResume() {
	super.onResume();
	if (haveCursors()) {
//	    mCursor.registerContentObserver(mContentObserver);
//	    mCursor.registerDataSetObserver(mDataSetObserver);
	    refresh();
	}
    }

    @Override
    protected void onPause() {
	super.onPause();
	if (haveCursors()) {
//		mCursor.unregisterContentObserver(mContentObserver);
//		mCursor.unregisterDataSetObserver(mDataSetObserver);
	}
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	//outState.putBoolean("isSortedBySize", mIsSortedBySize);
	outState.putLongArray("selection", getSelectionAsArray());
    }

    private long[] getSelectionAsArray() {
	long[] selectedIds = new long[mSelectedIds.size()];
	Iterator<Long> iterator = mSelectedIds.iterator();
	for (int i = 0; i < selectedIds.length; i++) {
	    selectedIds[i] = iterator.next();
	}
	return selectedIds;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	super.onRestoreInstanceState(savedInstanceState);
	//mIsSortedBySize = savedInstanceState.getBoolean("isSortedBySize");
	mSelectedIds.clear();
	for (long selectedId : savedInstanceState.getLongArray("selection")) {
	    mSelectedIds.add(selectedId);
	}
	chooseListToShow();
	showOrHideSelectionMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	if (haveCursors()) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.download_ui_menu, menu);
	}
	return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	menu.findItem(R.id.download_menu_sort_by_size).setVisible(
		false);
	menu.findItem(R.id.download_menu_sort_by_date).setVisible(
			false);
	return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	int itemId = item.getItemId();
	if (itemId == R.id.download_menu_sort_by_size) {
		//mIsSortedBySize = true;
		chooseListToShow();
		return true;
	} else if (itemId == R.id.download_menu_sort_by_date) {
		//mIsSortedBySize = false;
		chooseListToShow();
		return true;
	}else if(itemId == android.R.id.home){
		finish();
	}
	return false;
    }

    /**
     * Show the correct ListView and hide the other, or hide both and show the
     * empty view.
     */
    private void chooseListToShow() {
	mListView.setVisibility(View.GONE);

	if (mCursor == null || mCursor.getCount() == 0) {
	    mEmptyView.setVisibility(View.VISIBLE);
	} else {
	    mEmptyView.setVisibility(View.GONE);
	    mListView.setVisibility(View.VISIBLE);
	    mListView.invalidateViews(); // ensure checkboxes get updated
	}
    }

    /**
     * @return an OnClickListener to delete the given downloadId from the
     *         Download Manager
     */
    private DialogInterface.OnClickListener getDeleteClickHandler(
	    final long downloadId) {
	return new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		deleteDownload(downloadId);
	    }
	};
    }

    /**
     * @return an OnClickListener to pause the given downloadId from the
     *         Download Manager
     */
    private DialogInterface.OnClickListener getPauseClickHandler(
	    final long downloadId) {
	return new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		//mDownloadManager.pauseDownload(downloadId);
	    }
	};
    }

    /**
     * @return an OnClickListener to resume the given downloadId from the
     *         Download Manager
     */
    private DialogInterface.OnClickListener getResumeClickHandler(
	    final long downloadId) {
	return new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		mDownloadManager.resumeDownload(downloadId);
	    }
	};
    }

    /**
     * @return an OnClickListener to restart the given downloadId in the
     *         Download Manager
     */
    private DialogInterface.OnClickListener getRestartClickHandler(
	    final long downloadId) {
	return new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		mDownloadManager.restartDownload(downloadId);
	    }
	};
    }

    /**
     * Send an Intent to open the download currently pointed to by the given
     * cursor.
     */
    private void openCurrentDownload(Cursor cursor) {
	Uri localUri = Uri.parse(cursor.getString(mLocalUriColumnId));
	try {
	    getContentResolver().openFileDescriptor(localUri, "r").close();
	} catch (FileNotFoundException exc) {
	    Log.d(LOG_TAG,
		    "Failed to open download " + cursor.getLong(mIdColumnId),
		    exc);
	    showFailedDialog(cursor.getLong(mIdColumnId),
		    getString(R.string.dialog_file_missing_body));
	    return;
	} catch (IOException exc) {
	    // close() failed, not a problem
	}

	Intent intent = new Intent(Intent.ACTION_VIEW);
	intent.setDataAndType(localUri, cursor.getString(mMediaTypeColumnId));
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
		| Intent.FLAG_GRANT_READ_URI_PERMISSION);
	try {
	    startActivity(intent);
	} catch (ActivityNotFoundException ex) {
	    Toast.makeText(this, R.string.download_no_application_title,
		    Toast.LENGTH_LONG).show();
	}
    }

    private void handleItemClick(Cursor cursor) {
	long id = cursor.getInt(mIdColumnId);
	switch (cursor.getInt(mStatusColumnId)) {
	case DownloadManager.STATUS_PENDING:
	case DownloadManager.STATUS_RUNNING:
	    showRunningDialog(id);
	    break;

	case DownloadManager.STATUS_PAUSED:
	    if (isPausedForWifi(cursor)) {
		mQueuedDownloadId = id;
		mQueuedDialog = new AlertDialog.Builder(this)
			.setTitle(R.string.dialog_title_queued_body)
			.setMessage(R.string.dialog_queued_body)
			.setPositiveButton(R.string.keep_queued_download, null)
			.setNegativeButton(R.string.remove_download,
				getDeleteClickHandler(id))
			.setOnCancelListener(this).show();
	    } else {
		showPausedDialog(id);
	    }
	    break;

	case DownloadManager.STATUS_SUCCESSFUL:
	    openCurrentDownload(cursor);
	    break;

	case DownloadManager.STATUS_FAILED:
	    showFailedDialog(id, getErrorMessage(cursor));
	    break;
	}
    }

    /**
     * @return the appropriate error message for the failed download pointed to
     *         by cursor
     */
    private String getErrorMessage(Cursor cursor) {
	switch (cursor.getInt(mReasonColumndId)) {
	case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
	    if (isOnExternalStorage(cursor)) {
		return getString(R.string.dialog_file_already_exists);
	    } else {
		// the download manager should always find a free filename for
		// cache downloads,
		// so this indicates a strange internal error
		return getUnknownErrorMessage();
	    }

	case DownloadManager.ERROR_INSUFFICIENT_SPACE:
	    if (isOnExternalStorage(cursor)) {
		return getString(R.string.dialog_insufficient_space_on_external);
	    } else {
		return getString(R.string.dialog_insufficient_space_on_cache);
	    }

	case DownloadManager.ERROR_DEVICE_NOT_FOUND:
	    return getString(R.string.dialog_media_not_found);

	case DownloadManager.ERROR_CANNOT_RESUME:
	    return getString(R.string.dialog_cannot_resume);

	default:
	    return getUnknownErrorMessage();
	}
    }

    private boolean isOnExternalStorage(Cursor cursor) {
	String localUriString = cursor.getString(mLocalUriColumnId);
	if (localUriString == null) {
	    return false;
	}
	Uri localUri = Uri.parse(localUriString);
	if (!localUri.getScheme().equals("file")) {
	    return false;
	}
	String path = localUri.getPath();
	String externalRoot = Environment.getExternalStorageDirectory()
		.getPath();
	return path.startsWith(externalRoot);
    }

    private String getUnknownErrorMessage() {
	return getString(R.string.dialog_failed_body);
    }

    private void showRunningDialog(long downloadId) {
	new AlertDialog.Builder(this)
		.setTitle(R.string.download_running)
		.setMessage(R.string.dialog_running_body)
		.setNegativeButton(R.string.cancel_running_download,
			getDeleteClickHandler(downloadId))
		.setPositiveButton(R.string.pause_download,
			getPauseClickHandler(downloadId)).show();
    }

    private void showPausedDialog(long downloadId) {
	new AlertDialog.Builder(this)
		.setTitle(R.string.download_queued)
		.setMessage(R.string.dialog_paused_body)
		.setNegativeButton(R.string.delete_download,
			getDeleteClickHandler(downloadId))
		.setPositiveButton(R.string.resume_download,
			getResumeClickHandler(downloadId)).show();
    }

    private void showFailedDialog(long downloadId, String dialogBody) {
	new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_title_not_available)
		.setMessage(dialogBody)
		.setNegativeButton(R.string.delete_download,
			getDeleteClickHandler(downloadId))
		.setPositiveButton(R.string.retry_download,
			getRestartClickHandler(downloadId)).show();
    }


    // handle a click from the size-sorted list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	mCursor.moveToPosition(position);
	handleItemClick(mCursor);
    }

    // handle a click on one of the download item checkboxes
    @Override
    public void onDownloadSelectionChanged(long downloadId, boolean isSelected) {
	if (isSelected) {
	    mSelectedIds.add(downloadId);
	} else {
	    mSelectedIds.remove(downloadId);
	}
	showOrHideSelectionMenu();
    }

    private void showOrHideSelectionMenu() {
	boolean shouldBeVisible = !mSelectedIds.isEmpty();
	boolean isVisible = mSelectionMenuView.getVisibility() == View.VISIBLE;
	if (shouldBeVisible) {
	    updateSelectionMenu();
	    if (!isVisible) {
		// show menu
		mSelectionMenuView.setVisibility(View.VISIBLE);
		mSelectionMenuView.startAnimation(AnimationUtils.loadAnimation(
			this, R.anim.footer_appear));
	    }
	} else if (!shouldBeVisible && isVisible) {
	    // hide menu
	    mSelectionMenuView.setVisibility(View.GONE);
	    mSelectionMenuView.startAnimation(AnimationUtils.loadAnimation(
		    this, R.anim.footer_disappear));
	}
    }

    /**
     * Set up the contents of the selection menu based on the current selection.
     */
    private void updateSelectionMenu() {
	int deleteButtonStringId = R.string.delete_download;
	if (mSelectedIds.size() == 1) {
	    Cursor cursor = mDownloadManager.query(new DownloadManager.Query()
		    .setFilterById(mSelectedIds.iterator().next()));
	    try {
		cursor.moveToFirst();
		switch (cursor.getInt(mStatusColumnId)) {
		case DownloadManager.STATUS_FAILED:
		    deleteButtonStringId = R.string.delete_download;
		    break;

		case DownloadManager.STATUS_PENDING:
		    deleteButtonStringId = R.string.remove_download;
		    break;

		case DownloadManager.STATUS_PAUSED:
		case DownloadManager.STATUS_RUNNING:
		    deleteButtonStringId = R.string.cancel_running_download;
		    break;
		}
	    } finally {
		cursor.close();
	    }
	}
	mSelectionDeleteButton.setText(deleteButtonStringId);
    }

    @Override
    public void onClick(View v) {
	int id = v.getId();
	if (id == R.id.selection_delete) {
		for (Long downloadId : mSelectedIds) {
		deleteDownload(downloadId);
	    }
		clearSelection();
	} else if (id == R.id.deselect_all) {
		clearSelection();
		isEditStatus[0]=false;
		mListView.invalidateViews();
	}
    }

    /**
     * Requery the database and update the UI.
     */
    private void refresh() {
	mCursor.requery();
	// Adapters get notification of changes and update automatically
    }

    private void clearSelection() {
	mSelectedIds.clear();
	showOrHideSelectionMenu();
    }

    /**
     * Delete a download from the Download Manager.
     */
    private void deleteDownload(long downloadId) {
	if (moveToDownload(downloadId)) {
	    int status = mCursor.getInt(mStatusColumnId);
	    boolean isComplete = status == DownloadManager.STATUS_SUCCESSFUL
		    || status == DownloadManager.STATUS_FAILED;
	    String localUri = mCursor.getString(mLocalUriColumnId);
	    if (isComplete && localUri != null) {
		String path = Uri.parse(localUri).getPath();
		if (path.startsWith(Environment.getExternalStorageDirectory()
			.getPath())) {
		    mDownloadManager.markRowDeleted(downloadId);
		    return;
		}
	    }
	}
	mDownloadManager.remove(downloadId);
    }

    @Override
    public boolean isDownloadSelected(long id) {
	return mSelectedIds.contains(id);
    }

    /**
     * Called when there's a change to the downloads database.
     */
    void handleDownloadsChanged() {
	checkSelectionForDeletedEntries();

	if (mQueuedDownloadId != null && moveToDownload(mQueuedDownloadId)) {
	    if (mCursor.getInt(mStatusColumnId) != DownloadManager.STATUS_PAUSED
		    || !isPausedForWifi(mCursor)) {
		mQueuedDialog.cancel();
	    }
	}
    }

    private boolean isPausedForWifi(Cursor cursor) {
	return cursor.getInt(mReasonColumndId) == DownloadManager.PAUSED_QUEUED_FOR_WIFI;
    }

    /**
     * Check if any of the selected downloads have been deleted from the
     * downloads database, and remove such downloads from the selection.
     */
    private void checkSelectionForDeletedEntries() {
	// gather all existing IDs...
	Set<Long> allIds = new HashSet<Long>();
	for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor
		.moveToNext()) {
	    allIds.add(mCursor.getLong(mIdColumnId));
	}

	// ...and check if any selected IDs are now missing
	for (Iterator<Long> iterator = mSelectedIds.iterator(); iterator
		.hasNext();) {
	    if (!allIds.contains(iterator.next())) {
		iterator.remove();
	    }
	}
    }

    /**
     * Move {@link #mCursor} to the download with the given ID.
     * 
     * @return true if the specified download ID was found; false otherwise
     */
    private boolean moveToDownload(long downloadId) {
	for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor
		.moveToNext()) {
	    if (mCursor.getLong(mIdColumnId) == downloadId) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Called when a dialog for a pending download is canceled.
     */
    @Override
    public void onCancel(DialogInterface dialog) {
	mQueuedDownloadId = null;
	mQueuedDialog = null;
    }


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		isEditStatus[0]=true;
		mListView.invalidateViews();
		return true;
	}
}
