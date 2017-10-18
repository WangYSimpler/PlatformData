package cn.com.hy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;



/**
 * 公共方法
 * 
 * @author hy
 * @version 2017-08-22
 */
public class CommonUtil {
	private static Logger logger = Logger.getLogger(CommonUtil.class);

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str 需要判断的字符串
	 * @return true：为空
	 */
	public static boolean isNullOrBlank(String str) {
		 return str == null || str.length() == 0;
	}
	
	/**
	 * 判断字符串是否为空
	 * 
	 * @param str 需要判断的字符串
	 * @return true：为空
	 */
	public static boolean isNullOrBlankByTrim(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Object转换为JSON
	 * 
	 * @param obj
	 * @param response
	 * @throws Exception
	 */
	public static void objToJson(Object obj, HttpServletResponse response) throws Exception {
		response.setContentType("text/json;charset=utf-8");
		String json = new Gson().toJson(obj);
		logger.info(json);
		json = json.replaceAll("null", "\"\"");
		response.getWriter().write(json);
		response.getWriter().flush();
	}

	/**
	 * JsonArray 转List<Class<T>>
	 */
	/*@SuppressWarnings("unchecked")
	public static <T> List<T> jsonArrayToList(JSONArray array, Class<T> beanClass) {
		List<T> list = new ArrayList<T>();
		T bean;
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = array.getJSONObject(i);
			if (!object.isNullObject()) {
				bean = (T) JSONObject.toBean(object, beanClass);
				list.add(bean);
			}
		}
		return list;
	}*/

	/**
	 * 根据指定的前缀和日期创建缓存文件的目录
	 * 
	 * @param date
	 * @return
	 */
	/*public static String getCacheFileParent(String date) {
		String root = getWebRoot();
		return getCacheFileParent(root + (root.endsWith("/") ? "" : "/") + Constant.APP_DATA_PATH, date);
	}*/

	/**
	 * 根据指定的前缀和日期创建缓存文件的目录
	 * 
	 * @param prefix
	 * @param dateStr
	 * @return
	 */
	/*public static String getCacheFileParent(String prefix, String dateStr) {
		if (null == prefix) {
			prefix = File.separator;
		} else if (!prefix.endsWith(File.separator)) {
			prefix += File.separator;
		}
		Date date = null;
		if (!isNullOrBlank(dateStr)) {
			date = DateUtil.parseDate(dateStr);
		} else {
			date = new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return prefix + sdf.format(date) + File.separator;
	}*/

	/**
	 * 检查指定名称（路径和名称）的文件是否存在
	 * 
	 * @param fileStr
	 * @return
	 */
	public static boolean checkFileExist(String fileStr) {
		if (isNullOrBlank(fileStr)) {
			return false;
		}
		return new File(fileStr).exists();
	}

	/**
	 * 将二进制数组内容压缩（gzip）后保存到指定文件中
	 * 
	 * @param destFilePath
	 *            文件全路径（包含目录和名称）
	 * @param fileByteArray
	 *            文件内容的二进制数组
	 * @throws IOException
	 */
	public static void gzipFile(String destFilePath, byte[] fileByteArray) throws IOException {
		if (null == destFilePath || "".equals(destFilePath)) {
			// 未指压缩后的文件存放路径
			throw new IOException("Unspecitied compressed file storage path ! ");
		}
		// 压缩文件
		File zipFile = createFile(destFilePath);
		GZIPOutputStream out = null;
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(zipFile, false);
			out = new GZIPOutputStream(fo);
			out.write(fileByteArray);
		} finally {
			if (null != out) {
				out.close();
			}
			if (null != fo) {
				fo.close();
			}
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param destPth
	 * @return
	 */
	public static File createFile(String destPth) {
		if (null == destPth || "".equals(destPth.trim())) {
			throw new RuntimeException(" The destination file cannot be null ! ");
		}
		if (destPth.endsWith(File.separator)) {
			throw new RuntimeException(" The destination file cannot be a directory ! ");
		}
		File dest = new File(destPth);
		if (dest.exists()) {
			return dest;
		}
		// 创建目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		return dest;
	}

	/**
	 * 将Java对象转为JSON，并输出到Response中。其中Java对象中包含Date数据类型的对象
	 * 
	 * @param obj
	 *            Java对象
	 * @param response
	 *            HttpServletResponse对象
	 * @param pattern
	 *            "yyyy-MM-dd" 或 "yyyy-MM-dd HH:mm:ss"
	 */
	/*public static JSON objToJsonContainDate(Object obj, final String pattern) {
		JSON json = null;
		if (null != obj) {
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessor() {
				@Override
				public Object processArrayValue(Object Arr, JsonConfig jsonConfigArr) {
					return null;
				}

				@Override
				public Object processObjectValue(String arg, Object sourceObj, JsonConfig arg2) {
					if (null == sourceObj) {
						return "";
					}
					if (sourceObj instanceof java.util.Date) {
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						return sdf.format(sourceObj);
					}
					return sourceObj.toString();
				}
			});

			json = JSONSerializer.toJSON(obj, jsonConfig);
		}
		return json;
	}*/

	/**
	 * 将一个javabean映射到另一个javabean，null字段不映射
	 * 
	 * @param toObj
	 *            将要映射的javabean
	 * @param obj
	 *            被映射的javabean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void beanToBean(Object toObj, Object obj) {
		try {
			Class c = obj.getClass();
			Field[] fields = c.getDeclaredFields(); // 对象中的所有属性, getName()获取属性名,
													// getType()获取属性类型
			for (int i = 0; i < fields.length; i++) {
				String name = fields[i].getName();
				String methodName = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
				Method m = c.getMethod("get" + methodName);
				Object value = m.invoke(obj);
				if (value != null)
					BeanUtils.setProperty(toObj, name, value); // 给name属性添加值
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * 名称增加前缀
	 * 
	 * @param total
	 * @param name
	 * @param prefix
	 * @return
	 */
	public static String addPrefixByName(String total, String name, String prefix) {
		return total.replace(name, prefix + name);
	}

	/**
	 * 文件名称增加后缀
	 * 
	 * @param path
	 * @param suffix
	 * @return
	 */
	public static String addSuffixByName(String path, String suffix) {
		if (CommonUtil.isNullOrBlankByTrim(path)) {
			return null;
		}
		int idx = path.lastIndexOf(".");
		return path.substring(0, idx) + suffix + path.substring(idx);
	}

	/**
	 * 获取WebRoot目录
	 * 
	 * @return
	 */
	
	@SuppressWarnings("unused")
	private static String getWebRoot() {
		String root = CommonUtil.class.getClassLoader().getResource("/").getPath();
		if (null != root && root.indexOf("/WEB-INF") > 0) {
			return root.substring(0, root.indexOf("/WEB-INF"));
		}
		throw new RuntimeException("WebRoot directory not found!");
	}
}
