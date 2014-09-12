package com.cqvip.zlfassist.bitmap;

public class BitmapMangerUtil {

	 public static int getCachSize(){
		 int size = (int) Math.round(0.125 * Runtime.getRuntime().maxMemory());
		 return size;
	 }
}
