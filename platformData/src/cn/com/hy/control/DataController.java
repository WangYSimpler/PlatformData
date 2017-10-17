package cn.com.hy.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cn.com.hy.service.GetPlatformDataService;
import cn.com.hy.service.SyncJJXXDate;
import cn.com.hy.util.CommonUtil;
import cn.com.hy.util.DateUtils;
import cn.com.hy.util.JsonUtil;


@Controller
@RequestMapping("/data")
public class DataController {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DataController.class);
	
	@Resource
	GetPlatformDataService gpd;

	/**
	 * WangY
	 * http请求，获取数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getDatas", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getDatas(HttpServletRequest request, HttpServletResponse response) {

		String queryMethodResult = "";
		///前端传递数据参数
		String methodName = request.getParameter("methodName");
		String dataObjectCode = request.getParameter("dataObjectCode");
		String conditions = request.getParameter("conditions");

		if (CommonUtil.isNullOrBlank(methodName)|| CommonUtil.isNullOrBlank(dataObjectCode) ) {
			/// 没有传递函数名
			logger.info("code : -1, message:请求参数中conditions、dataObjectCode 不存在");
			queryMethodResult = "{code:-1,message:请求参数中conditions、dataObjectCode 不存在}";
		} else {
		
			/// 返回结果字段
			String returnFields = request.getParameter("returnFields");
			if (conditions == null) {
				conditions = "";
			}
			if (returnFields == null) {
				returnFields = "";
			}
			/// 每页条目数量没传值默认给20条
			int pageSize = 20;
			String pageSizeStr = request.getParameter("pageSize");

			///未传分页大小
			if (!CommonUtil.isNullOrBlank(pageSizeStr) ) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
			
			/// 分页查询页面，默认给他第一页
			String pageNumberString = request.getParameter("pageNumber");
			int pageNumber = 1;
			if (!CommonUtil.isNullOrBlank( pageNumberString) ){
				pageNumber = Integer.parseInt(pageNumberString);
			}

			// 是否格式化
			boolean formatted = Boolean.parseBoolean(request.getParameter("formatted"));
			
			// xml或json
			String resultStyle = request.getParameter("resultStyle");
			
			
			if (methodName.equals("count")) {
				queryMethodResult = this.getResultByCount(dataObjectCode, conditions);
			}else if (methodName.equals("query")) {
				///query 查询方法
				queryMethodResult = this.getResultByQuery(dataObjectCode, conditions, returnFields, pageSize, formatted,resultStyle);
			}else if (methodName.equals("pageQuery")) {
				
				queryMethodResult = this.getResultByPageQuery(dataObjectCode, conditions, returnFields, pageSize, pageNumber,formatted, resultStyle);
				
			} else if (methodName.equals("queryJJXX")) {// 接警信息，从大平台后端获取数据
				/// 传递时间到后台
				String dateStart = request.getParameter("D1");
				///
				String JJBHString = request.getParameter("JJBH");
				/// 刑事警情类别
				String XSJQString = request.getParameter("D2");
				// 时间的格式化
				 queryMethodResult = this.getResultByQueryJJXX(dataObjectCode, conditions, returnFields,dateStart,JJBHString,XSJQString);

			}///20170420  王勇合肥用于系统同步数据
			else if (methodName.equals("querySyncJJXX")) {
				/// 传递时间到后台
				String dateStart = request.getParameter("DStart");
				///传递结束
				String dateEnd = request.getParameter("DEnd");
				///
				String JJBHString = request.getParameter("JJBH");
				
				//String oneDayConditions = "";
				
				Map<String, String> dataObjectCodeConditionMap = new HashMap<String, String>();
				
				// 时间的格式化
				//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				
				if (!conditions.equals("")) {
					conditions += "  and ";
				}
				if (JJBHString != null && JJBHString != "") {
					
					conditions += "JJBH='" + JJBHString + "'";
				} else {
					/// 传条件值条件值
					if (conditions.contains("JJBH")) {
						conditions += "  and ";
					}
					///20170420   王勇
					///查询接警信息
					if (dataObjectCode.equals("DWD_DPTJCJ_JJXX") || dataObjectCode.equals("DWD_DPT_AJ_JBXX") || dataObjectCode.equals("DWD_DPTJCJ_CJXX")) {
						dataObjectCodeConditionMap.clear();
						dataObjectCodeConditionMap.putAll(new SyncJJXXDate().getDataObjectCodeConditioMap(dataObjectCode,dateStart, dateEnd));
					}
					
				}
				
				String conditionTempStr = conditions;
				//保存两个结果
				List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
				listMap.clear();
				
				///获取objectCode
				for (Map.Entry<String, String> entry : dataObjectCodeConditionMap.entrySet()) {

					dataObjectCode = entry.getKey();
					conditions = conditionTempStr + entry.getValue();
					List<Map<String, String>> listMapTemp = new ArrayList<Map<String, String>>();
				
					int queryCounts = Integer.parseInt(gpd.count(dataObjectCode, conditions));
					logger.info("test条数：" +  queryCounts);
					String queryResult = gpd.query(dataObjectCode, conditions, returnFields, queryCounts);
					listMapTemp = JsonUtil.parseMap2JavaBean(queryResult, new TypeToken<List<Map<String, String>>>() {}.getType());
					if (listMapTemp.size() > 0) {
						listMap.addAll(listMapTemp);
					}
					
				}
				queryMethodResult = new Gson().toJson(listMap);

			}else {
				queryMethodResult = "-1";
			}
		}

		// queryMethodResult = gpd.getData(dataObjectCode,conditions);
		// System.out.print();

		String callback = request.getParameter("callback");
		if (StringUtils.isEmpty(callback)){
			return queryMethodResult;
		}else{
			return callback + "(" + queryMethodResult + ")";
		}

	}
	
	/**
	 *  查询数据条数
	 * @param dataObjectCode
	 * @param conditions
	 * @return
	 */
	private String getResultByCount(String dataObjectCode, String conditions){
		return gpd.count(dataObjectCode, conditions).toString();
	}
	
	/**
	 * query 方法调取数据
	 * @param dataObjectCode
	 * @param conditions
	 * @param returnFields
	 * @param pageSize
	 * @param formatted
	 * @param resultStyle
	 * @return
	 */
	private String getResultByQuery(String dataObjectCode, String conditions
			,String returnFields,int pageSize, boolean formatted, String resultStyle){
		
		
		if (resultStyle == null) {
			resultStyle = "";
		}
		//未格式化
		if (!formatted) {
			return gpd.query(dataObjectCode, conditions, returnFields, pageSize);
		}  
		return gpd.query(dataObjectCode, conditions, returnFields, pageSize, formatted,resultStyle);
	}
	
	private String getResultByPageQuery(String dataObjectCode, String conditions,String returnFields,int pageSize, int pageNumber
										,boolean formatted, String resultStyle){
		if (resultStyle == null) {
			resultStyle = "";
		}
		if (!formatted) {
			return gpd.pageQuery(dataObjectCode, conditions, returnFields, pageSize, pageNumber);
		}
			
		return gpd.pageQuery(dataObjectCode, conditions, returnFields, pageSize, pageNumber,formatted, resultStyle);
		
	}
	
	// 查询警情警情方法
	private String  getResultByQueryJJXX(String dataObjectCode, String conditions
			,String returnFields,String dateStart,String JJBHString,String XSJQString){
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		//传递数据 conditions 不为空
		if (! "".equals(conditions)) {
			conditions += "  and ";
		}
		
		if (!CommonUtil.isNullOrBlank(JJBHString)) {
			
			conditions += "JJBH='" + JJBHString + "'";
		} else {
			/// conditions 传递参数过来
			if (conditions.contains("JJBH")) {
				conditions += "  and ";
			}

			/// 刑事警情
			if (!CommonUtil.isNullOrBlank(XSJQString)) {
				conditions += " BJLX ='" + XSJQString + "'";
			}

			if ( conditions.contains("BJLX")) {
				conditions += " and ";
			}
			///20170207 王勇
			String currentDate = simpleDateFormat.format(new Date());
			//没传日期，请求今天的数据
			if (CommonUtil.isNullOrBlank(dateStart)) {
				//String currentDate = simpleDateFormat.format(new Date());
				dataObjectCode = "DWD_DPT_JCJ_JJXX_ONEDAY";
				
				/*
				 * 当前日期使用like进行查询
				 * String currentDateStart = currentDate + "000000";
				String currentDateEnd = currentDate + "235959";*/
				
				conditions += "JJRQSJ like '" + currentDate + "%'";
				
				/*conditions += "JJRQSJ>= to_date('" + currentDateStart
						+ "' ,'yyyymmddhh24miss') AND JJRQSJ<= to_date('" + currentDateEnd
						+ "' ,'yyyymmddhh24miss')";*/
			} else {

				Date d = DateUtils.getUtilDateByString(dateStart, "yyyy-MM-dd");
				String dateStartFormt = DateUtils.getDateTime(d, "yyyyMMdd");
				if (dateStartFormt.equals(currentDate)) {
					dataObjectCode = "DWD_DPT_JCJ_JJXX_ONEDAY";
					conditions += "JJRQSJ like '" + currentDate + "%'";
				}else {
					String specialDateStart = dateStartFormt + "000000";
					String specialDateEnd = dateStartFormt + "235959";
					// System.out.println(currentDate);
					dataObjectCode = "DWD_DPTJCJ_JJXX";
					conditions += "JJRQSJ>= to_date('" + specialDateStart
							+ "' ,'yyyymmddhh24miss') AND JJRQSJ<= to_date('" + specialDateEnd
							+ "' ,'yyyymmddhh24miss')";
				}
			}
			
		}
		 return this.getResultByQuery(dataObjectCode, conditions, returnFields, 500,false,null);
		
	}
	
	
	
	

}
