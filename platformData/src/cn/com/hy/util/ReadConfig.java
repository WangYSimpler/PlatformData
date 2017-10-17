package cn.com.hy.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取config.properties
 * 
 * @author zhouteng
 * 
 */
public class ReadConfig {
	
	private static String value;
	private static String configFilePath = "/config.properties";

	/**
	 * 根据key读取value
	 * 
	 * @param filePath
	 *            文件名
	 * @param key
	 *            文件中的key
	 * @return
	 */
	public static String getInfo(String key) {

		Properties props = new Properties();
		InputStream ins = ReadConfig.class.getResourceAsStream(configFilePath);
		try {
			props.load(ins);
			if (props.containsKey(key)) {
				value = props.getProperty(key).trim();
				value= new String(value.getBytes("ISO-8859-1"), "UTF-8");
			} else {
				return "";
			}
		} catch (IOException ex) {
			// ex.printStackTrace();
			return "";
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					return "";
				}
			}
		}
		return value;
	}
	
	public static void main(String[] args) throws Exception {
		// new ReadConfig();
		System.out.println(ReadConfig.getInfo("bookDetailTxtPath"));
	}
	
}