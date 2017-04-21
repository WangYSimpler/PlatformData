package com.hy.manager.sys.module;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.util.DateUtils;
import com.hy.util.JsonUtil;

/**
 * 登录 controller
 * 
 * @author yong
 *
 */
@Controller
@RequestMapping("/data")
public class LogController {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LogController.class);

	/**
	 * 跳转登录页
	 */
	@RequestMapping(value = "toLogin")
	public String toLogin(HttpServletRequest request) {

		return "login";
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String no, String pwd, HttpServletRequest request, HttpSession session) {
		logger.info("登录成功");

		return "manager/sys/main";
	}

	@Resource
	GetPlatformData gpd;

	/**
	 * 退出
	 */
	@RequestMapping(value = "/getDatas", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		String queryMethodResult = "";
		String methodName = request.getParameter("methodName");
		String dataObjectCode = request.getParameter("dataObjectCode");
		String conditions = request.getParameter("conditions");

		if (null == methodName || methodName.equals("") || null == dataObjectCode || dataObjectCode.equals("")) {
			/// 没有传递函数名
			queryMethodResult = "-1";
		} else {
			/// 查询条件
			// String conditions = request.getParameter("conditions");
			/// 返回结果字段
			String returnFields = request.getParameter("returnFields");
			if (conditions == null) {
				conditions = "";
			}
			if (returnFields == null) {
				returnFields = "";
			}
			/// 每页条目数量没传值默认给20条
			// int pageSize = 20;
			String pageSizeString = request.getParameter("pageSize");
			int pageSize;

			if (pageSizeString == null || pageSizeString.equals("")) {
				pageSize = 20;
			} else {
				pageSize = Integer.parseInt(pageSizeString);
			}

			// 是否格式花
			boolean formatted = Boolean.parseBoolean(request.getParameter("formatted"));
			// xml或json
			String resultStyle = request.getParameter("resultStyle");
			/// 分页,第几页默认给他第一页
			String pageNumberString = request.getParameter("pageNumber");
			int pageNumber;
			if (null == pageNumberString || pageNumberString.equals("")) {
				pageNumber = 1;
			} else {
				pageNumber = Integer.parseInt(pageNumberString);
			}

			if (methodName.equals("query")) {

				if (resultStyle == null) {
					resultStyle = "";
				}

				if (!formatted) {
					queryMethodResult = gpd.query(dataObjectCode, conditions, returnFields, pageSize);
				} else if (formatted || resultStyle.equals("xml") || resultStyle.equals("json")) {
					queryMethodResult = gpd.query(dataObjectCode, conditions, returnFields, pageSize, formatted,
							resultStyle);
				}
			} else if (methodName.equals("count")) {
				queryMethodResult = gpd.count(dataObjectCode, conditions).toString();
			} else if (methodName.equals("pageQuery")) {
				if (resultStyle == null) {
					resultStyle = "";
				}
				if (!formatted) {
					queryMethodResult = gpd.pageQuery(dataObjectCode, conditions, returnFields, pageSize, pageNumber);
				} else if (formatted || resultStyle.equals("xml") || resultStyle.equals("json")) {
					queryMethodResult = gpd.pageQuery(dataObjectCode, conditions, returnFields, pageSize, pageNumber,
							formatted, resultStyle);
				}

			} else if (methodName.equals("queryJJXX")) {
				/// 传递时间到后台
				String dateStart = request.getParameter("D1");
				///
				String JJBHString = request.getParameter("JJBH");
				/// 刑事警情类别
				String XSJQString = request.getParameter("D2");
				// 时间的格式化
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				
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

					/// 刑事警情
					if (XSJQString != null && XSJQString != "") {
						conditions += " BJLX ='" + XSJQString + "'";
						/*if (XSJQString.equals("10000")) {
							String BJLXresult = " (BJLX >='10000' and BJLX<='19999')";
							conditions += BJLXresult;
						} else {
							conditions += " BJLX ='" + XSJQString + "'";
						}*/

					}

					if ( conditions.contains("BJLX")) {
						conditions += " and ";
					}
					///20170207 王勇
					String currentDate = simpleDateFormat.format(new Date());
					
					if (dateStart == null || dateStart == "") {
						//String currentDate = simpleDateFormat.format(new Date());
						dataObjectCode = "DWD_DPT_JCJ_JJXX_ONEDAY";
						String currentDateStart = currentDate + "000000";
						String currentDateEnd = currentDate + "235959";
						
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
				 queryMethodResult = gpd.query(dataObjectCode, conditions, returnFields, 500);

			}///20170420  王勇合肥用于系统同步数据
			else if (methodName.equals("querySyncJJXX")) {
				/// 传递时间到后台
				String dateStart = request.getParameter("DStart");
				///传递结束
				String dateEnd = request.getParameter("DEnd");
				///
				String JJBHString = request.getParameter("JJBH");
				
				String oneDayConditions = "";
				
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
					String queryResult = gpd.query(dataObjectCode, conditions, returnFields, pageSize);
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
		if (StringUtils.isEmpty(callback))

		{
			return queryMethodResult;
		} else {
			return callback + "(" + queryMethodResult + ")";
		}

	}

}
