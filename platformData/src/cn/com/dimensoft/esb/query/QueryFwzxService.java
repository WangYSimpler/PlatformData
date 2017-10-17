package cn.com.dimensoft.esb.query;

import java.util.List;
import java.util.Map;


/**
 * 
 * @ClassName: QueryService.java
 * @Description:资源目录接口
 * @author chymilk
 * @date 2015年8月19日 下午5:44:55
 */
public interface QueryFwzxService {
	
	List<Map<String, String>> query(String dataObjectCode,String conditions, String returnFields, int pageSize,String userInfo);
	/**
	 * 查询
	 * 
	 * @param dataObjectCode
	 * @param conditions
	 * @param returnFields
	 * @param pageSize
	 * @return
	 */
	String query(String dataObjectCode, String conditions, String returnFields,int pageSize, Boolean formatted, String resultStyle,String userInfo);

	/**
	 * 分页查询
	 * 
	 * @param dataObjectCode
	 * @param conditions
	 * @param returnFields
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	String pageQuery(String dataObjectCode, String conditions,String returnFields, int pageSize, int pageNumber,Boolean formatted, String resultStyle,String userInfo);
	
	/**
	 * 查看详情
	 * @param dataObjectCode
	 * @param conditions
	 * @param detailFields
	 * @return
	 */
	Map<String, String> detail(String dataObjectCode, String conditions,String detailFields,String userInfo);
	
	/**
	 * 列表的分页展示
	 * @param dataObjectCode
	 * @param conditions
	 * @param returnFields
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	List<Map<String, String>> pageQuery(String dataObjectCode,String conditions, String returnFields, int pageSize,int pageNumber,String userInfo);
	
	
	/**
	 * 查询记录的条数
	 * @param dataObjectCode
	 * @param conditions
	 * @param userInfo
	 * @return
	 */
	int count(String dataObjectCode,String conditions,String userInfo);
}