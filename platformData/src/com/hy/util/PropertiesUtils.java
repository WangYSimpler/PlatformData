/**
 * @file   PropertiesUtils.java
 * @author WangY
 * @Date  2017年3月8日
 */
package com.hy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author WangY
 * @Date 2017年3月8日
 */
public class PropertiesUtils {

	public Properties getProperties() {
		
		// 读取Jar 同文件夹下配置文件中数据
		// String sourcePathStr = new File("config.properties").toString();
		// 系统内部使用
		String sourcePathStr = new File(this.getClass().getResource("/").getPath().toString()).toString()
				+ "/config.properties";
		//// Test 王勇 20170309
		System.out.println(sourcePathStr);
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(sourcePathStr));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties;
	}

	public String getPropertiesValue(Properties properties, String propertyNameStr) {
		Properties prop = properties;
		String propertyName = propertyNameStr;

		return prop.getProperty(propertyName);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		PropertiesUtils propertiesUtils = new PropertiesUtils();
		Properties properties = propertiesUtils.getProperties();
		System.out.println(propertiesUtils.getPropertiesValue(properties, "DWD_DPT_JCJ_CJXX_ONEDAY"));
	}

}
