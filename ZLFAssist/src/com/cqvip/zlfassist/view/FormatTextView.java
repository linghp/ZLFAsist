package com.cqvip.zlfassist.view;

import java.util.regex.Pattern;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


public class FormatTextView extends TextView {

	// private static final String ttt =
	// "http://imgsrc.baidu.com/baike/pic/item/346bd85cb9d9f066faf2c02c.jpg";
	// 匹配图片[*]
	public static Pattern PIC_PATTERN = Pattern.compile("\\[([\\*]+)\\]",
			Pattern.CASE_INSENSITIVE);

	public FormatTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FormatTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FormatTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}


	public void setText(String pretext, TextView textview) {
		
			textview.setText(pretext);
			
	}

	
}
