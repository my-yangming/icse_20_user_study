package com.lingjoin.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfigUtil {
	private static Properties config = null;
	static {
		InputStream in = null;
		try {
			File config_file_path = new File("JZSearch.properties");
			if (config_file_path.exists()) {
				in = new FileInputStream(config_file_path);
			} else {
				in = ReadConfigUtil.class.getClassLoader().getResourceAsStream(
						"JZSearch.properties");
			}
		} catch (FileNotFoundException e1) {
			System.out.println("config file not found JZSearch.properties!");
		}
		config = new Properties();
		try {
			if(in==null){
				System.out.println("config file not found JZSearch.properties!");
			}else {
				config.load(in);
				in.close();				
			}
		} catch (IOException e) {
			System.out.println("No JZSearch.properties defined error");
		}
	}

	// 根�?�key读�?�value
	public static String getValue(String key) {
		// Properties props = new Properties();
		try {
			String value = config.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ConfigInfoError" + e.toString());
			return null;
		}
	}

	public static void main(String args[]) {
		System.out.println(getValue("contentUrl"));
	}
}
