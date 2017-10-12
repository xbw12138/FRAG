package com.example.frag_model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

public class SmsContent extends ContentObserver {

	public static final String SMS_URI_INBOX = "content://sms/inbox";
	private Activity activity = null;
	private String smsContent = "";
	private EditText verifyText = null;

	public SmsContent(Activity activity, Handler handler, EditText verifyText) {
		super(handler);
		this.activity = activity;
		this.verifyText = verifyText;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Cursor cursor = null;// ���
		// ��ȡ�ռ�����ָ������Ķ���
		cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[] {
				"_id", "address", "body", "read" }, "address=? and read=?",
				new String[] { "106571207117008", "0" }, "date desc");//mob������֤����
		if (cursor != null) {// �������Ϊδ��ģʽ
			cursor.moveToFirst();
			if (cursor.moveToFirst()) {
				String smsbody = cursor
						.getString(cursor.getColumnIndex("body"));
				System.out.println("smsbody=======================" + smsbody);
				String pattern = "[1-9]\\d*";
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(smsbody.toString());
				ArrayList al =new ArrayList();
				while(m.find())
				{
					al.add(m.group(0));
				}
				if(!al.isEmpty())
				verifyText.setText(al.get(0).toString());
			}
		}
	}
}