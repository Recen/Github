package com.example.havingimgfun;

import java.text.DecimalFormat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class HavingImgFun extends Activity {
	/** Called when the activity is first created. */
	private static final String TAG = "HavingImgFun";
	ImageView imgView;
	Button btnProcess, btnRestore;
	Bitmap srcBitmap;
	Bitmap resultBitmap;
	Bitmap roiBitmap;
	private String url;
	private String foler;
	private DecimalFormat df;
	private Mat ROI;
	private Mat src;
	
	Handler handler=new Handler(){		
		@Override
		public void handleMessage(Message msg) {
			if (msg.what==0x9527) {
				
//				Log.i(TAG, "handleMessage=====》》》》");
				imgView.setImageBitmap(resultBitmap);
			}
		}         
	};
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

		@Override
		public void onManagerConnected(int status) {
			// TODO Auto-generated method stub
			switch (status){
			case BaseLoaderCallback.SUCCESS:
				Log.i(TAG, "成功加载");
				break;
			default:
				super.onManagerConnected(status);
				Log.i(TAG, "加载失败");
				break;
			}
			
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_have_img_fun);
//		this.setTitle("使用NDK转换灰度图");
//		
//		btnRestore = (Button) this.findViewById(R.id.btnRestore);
//		btnRestore.setOnClickListener(new ClickEvent());
		btnProcess = (Button) this.findViewById(R.id.btnProcess);
		btnProcess.setOnClickListener(new ClickEvent());
		imgView = (ImageView) this.findViewById(R.id.ImageView01);
		foler = "/storage/emulated/0/output/output/";
		srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.outimage);
		
//		roiBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Config.RGB_565);
//		df = new DecimalFormat("000000");
//		new Thread(){
//			public void run(){
//				for (int i = 1; i < 7520; i++) {
//					String s = df.format(i);
//					url = foler + "outimage" + s +".png";
//					ROI = new Mat();
//					src = new Mat();
//					srcBitmap = BitmapFactory.decodeFile(url);
//					Utils.bitmapToMat(srcBitmap, src);
//					Log.i(TAG, "-----------");
//					long address = LibImgFun.ImgFun(src.nativeObj);
//					ROI = new Mat(address);
//					Utils.matToBitmap(ROI, resultBitmap);
//					handler.sendEmptyMessage(0x9527);
//				}
//			}
//			
//		}.start();
	}

//	class ClickEvent implements View.OnClickListener {
//		public void onClick(View v) {
//			if (v == btnProcess) {
//				long current = System.currentTimeMillis();
//				Log.i(TAG, "before MAt");
//				ROI = new Mat();
//				src = new Mat();
//				Log.i(TAG, "after MAt");
//				Utils.bitmapToMat(srcBitmap, src);
//				Log.i(TAG, "-----------");
//				long address = LibImgFun.ImgFun(src.nativeObj);
//				ROI = new Mat(address);
//				Utils.matToBitmap(ROI, roiBitmap);				
//				imgView.setImageBitmap(roiBitmap);
//				
//			} else if (v == btnRestore) {
//				imgView.setImageBitmap(srcBitmap);
//				HavingImgFun.this.setTitle("使用OpenCV进行图像处理");
//			}
//		}
//	}

	class ClickEvent implements View.OnClickListener {
	public void onClick(View v) {
				df = new DecimalFormat("000000");
				
				new Thread(){
					public void run(){
//							Log.i(TAG, "In  Thread ----->"+getId());
						for (int i = 250; i < 7520; i++) {
							String s = df.format(i);
							url = foler + "outimage" + s +".png";
							ROI = new Mat();
							src = new Mat();
//							Log.i(TAG, "get srcBitmap----->");
							srcBitmap = BitmapFactory.decodeFile(url);
							resultBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Config.RGB_565);
							Utils.bitmapToMat(srcBitmap, src);
//							Log.i(TAG, "-----------");
							long address = LibImgFun.ImgFun(src.nativeObj);
							Log.i(TAG, "==============>>>>"+i);
							ROI = new Mat(address);
//							Log.i(TAG, "Mat(address)===>>>>");
							Utils.matToBitmap(ROI, resultBitmap);
//							Log.i(TAG, "Utils.matToBitmap(ROI, resultBitmap)===>>>>");
							handler.sendEmptyMessage(0x9527);
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						}
					}
					
				}.start();
	}
}
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, getApplicationContext(), mLoaderCallback);
		Log.i(TAG, "onResume sucess load OpenCV...");
	}
}
