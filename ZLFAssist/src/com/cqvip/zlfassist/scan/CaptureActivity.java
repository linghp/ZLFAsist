package com.cqvip.zlfassist.scan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.activity.AddFavorActivity;
import com.cqvip.zlfassist.activity.DisplayFollowActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public class CaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	public static  int statusBarHeight;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.scan);
		//初始化 CameraManager
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		txtResult = (TextView) findViewById(R.id.txtResult);
		//获取状态栏高度
		txtResult.post(new Runnable() {
            public void run() {
                    init_statusheight();
            }
    });
		
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	 private void init_statusheight(){
         Rect rect = new Rect();
         Window window = getWindow();
         txtResult.getWindowVisibleDisplayFrame(rect);
         // 状态栏的高度
        statusBarHeight = rect.top;
//         // 标题栏跟状态栏的总体高度
//         int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
//         // 标题栏的高度：用上面的值减去状态栏的高度及为标题栏高度
//         int titleBarHeight = contentViewTop - statusBarHeight;
//         System.out.println(statusBarHeight+"..."+contentViewTop+"..."+titleBarHeight);
 }
	
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	//得到结果跳转
	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);
		 playBeepSoundAndVibrate();
//		txtResult.setText(obj.getBarcodeFormat().toString() + ":"
//				+ obj.getText());
//		Intent intent=new Intent(CaptureActivity.this,ResultOnSearchActivity.class);
//		intent.putExtra("ISBN", obj.getText());
//		startActivity(intent);
		 String result = obj.getText();
		 byte[] signature;
         try {
             signature = Base64.decode(result, Base64.DEFAULT);
         } catch (IllegalArgumentException e) {
             signature = new byte[0];
         }
         Log.i("handleDecode", result+","+signature.length);
         String resString = null;
		try {
			resString = new String(signature,"utf-8");
			Log.i("handleDecode", resString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 if(!TextUtils.isEmpty(resString)){
			 String[]  array = resString.split("\\|");
		 switch (array[0]) {
	//文章收藏
		 case "DOC":
			 String  topicId = array[1];
			 //插库
			 //跳转AddFavorActivity
			 Intent intent = new Intent(CaptureActivity.this,AddFavorActivity.class);
			 intent.putExtra("flag",true);
			 intent.putExtra("id",topicId);
			 startActivityForResult(intent, 1);
			break;
			//对象关注
		case "OBJ":
			//插库
			String subjectType = array[1];
			String  subjectId = array[2];
			//跳转
			 Intent _intent = new Intent(CaptureActivity.this,DisplayFollowActivity.class);
			 _intent.putExtra("flag",true);
			 _intent.putExtra("type",subjectType);
			 _intent.putExtra("id",subjectId);
			 startActivityForResult(_intent,1);
			break;
			//文章下载
		default:
			//TODO
			break;
		 }
		 }
		//finish();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}