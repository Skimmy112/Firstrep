package com.example.skimmy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import android.app.Activity;
import android.widget.TextView;

public class TextViewPrintStream extends PrintStream {

	static private ByteArrayOutputStream out = new ByteArrayOutputStream();
	private TextView textView;

	public TextViewPrintStream(Activity context) {
		super(out);
		this.textView = new TextView(context);
		context.setContentView(textView);
	}

	public TextViewPrintStream(Activity context, TextView t) {
		super(out);
		this.textView = t;
	}

	public synchronized void print(String str) {
		super.print(str);
		textView.setText(out.toString());
	}
}
