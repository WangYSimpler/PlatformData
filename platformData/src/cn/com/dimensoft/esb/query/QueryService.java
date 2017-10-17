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
public interface QueryService {
	
	List<Map<String, String>> query(String dataObjectCode,String conditions, String returnFields, int pageSize);
	
	/**
	 * 查询
	 * 
	 * @param dataObjectCode
	 * @param conditions
	 * @param returnFields
	 * @param pageSize
	 * @return
	 */
	String query(String dataObjectCode, String conditions, String returnFields,int pageSize, Boolean formatted, String resultStyle);

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
	String pageQuery(String dataObjectCode, String conditions,String returnFields, int pageSize, int pageNumber,
			Boolean formatted, String resultStyle);
	
	/**
	 * 查看详情
	 * @param dataObjectCode
	 * @param conditions
	 * @param detailFields
	 * @return
	 */
	Map<String, String> detail(String dataObjectCode, String conditions,String detailFields);
	
	/**
	 * 列表的分页展示
	 * @param dataObjectCode
	 * @param conditions
	 * @param returnFields
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	List<Map<String, String>> pageQuery(String dataObjectCode,String conditions, String returnFields, int pageSize,int pageNumber);
	
	/**
	 
	 * @param dataObjectCode
	 * @param conditions
	 */
	
	int count(String dataObjectCode, String conditions);
}