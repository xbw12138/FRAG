package com.example.frag_model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.StrictMode;

import com.example.Config.Config;

public class webcontent {

	public static String getOneHtml(final String htmlurl) throws IOException {
		URL url;
		String temp;
		final StringBuffer sb = new StringBuffer();

		try {
			url = new URL(htmlurl);
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();

			StrictMode.setThreadPolicy(policy);

			final BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), "GBK"));// 读取网页全部内容
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			in.close();
		} catch (final MalformedURLException me) {
			Config.isemp = false;
			// System.out.println("你输入的URL格式有问题！请仔细输入");
			me.getMessage();
			throw me;
		} catch (final IOException e) {
			e.printStackTrace();
			throw e;
		}
		// System.out.println(sb.toString());
		return sb.toString();
	}

	public static String getUrl(final String s) {
		String ff = "false";
		String pattern = "<dd> <a href=\"(.*?)\"[\\s]+target=\"_blank\">";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(s);
		ArrayList al = new ArrayList();
		while (m.find()) {
			al.add(m.group(1));
		}
		if (!al.isEmpty()) {
			System.out.println(al.get(0).toString());
			return al.get(0).toString();
		} else
			return ff;
		// for(int i=0;i<al.size();i++)
		// {
		// System.out.println(al.get(i).toString());
		// }

	}

	public static String getContent(final String s) {
		String msg = "";
		String pattern = "G','(.*?)'\\)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(s);
		ArrayList al = new ArrayList();
		while (m.find()) {
			al.add(m.group(1));
		}
		for (int i = 0; i < al.size(); i++) {
			msg += al.get(i).toString();
			msg += "\n";
			// System.out.println(al.get(i).toString());
		}
		// System.out.println(config_a.msg);
		return msg;
	}

	public static String getmsg(String url) {
		String sss = "";
		try {
			sss = getContent(getOneHtml(getUrl(getOneHtml(url))));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return sss;

	}

}
