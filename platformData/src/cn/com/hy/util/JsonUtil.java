/**
 * 
 */
package cn.com.hy.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 *@author WangY
 *@Date 2017 2017年3月3日 
 */
public class JsonUtil {
	
	public static  <T> List<T> parseMap2JavaBean(String jsonStr,Type javaBeanType)  
    {  
		String json = jsonStr;
		
		Gson gson = new Gson();
		
		List<T> listT = null;
		
		if (json != null && !"".equals(json)) {
			listT = gson.fromJson(json, javaBeanType);
		}
        
        return listT;
    } 
	
	 public static Map<String, Object> parseBean2Map(Object obj) {  
		  
	        if(obj == null){  
	            return null;  
	        }          
	        Map<String, Object> map = new HashMap<String, Object>();  
	        try {  
	            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
	            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
	            for (PropertyDescriptor property : propertyDescriptors) {  
	                String key = property.getName();  
	  
	                // 过滤class属性  
	                if (!key.equals("class")) {  
	                    // 得到property对应的getter方法  
	                    Method getter = property.getReadMethod();  
	                    Object value = getter.invoke(obj);  
	  
	                    map.put(key, value);  
	                }  
	  
	            }  
	        } catch (Exception e) {  
	            System.out.println("transBean2Map Error " + e);  
	        }  
	  
	        return map;  
	  
	    }  
	  
	public static void main(String[] args) {

	}

}
