/**
* @developer： WangY
* @时间：2016年10月14日 下午8:52:15
* @项目名：platformData
*  高弗特 goFirst
*/
package com.hy.manager.sys.module;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import cn.com.dimensoft.esb.query.QueryFwzxService;
import cn.com.dimensoft.esb.query.QueryService;

@Service
public class GetPlatformData {

	private static final long serialVersionUID = 1L;
	private final static String userInfo = "{'account':'040708','password':'szzhzx'}";
	private static ClassPathXmlApplicationContext consumerContext = null;
	private static QueryFwzxService queryService = null;
	
	///系統初始化函数，配置文件读取，获取bean等
	public void platformDataInit() {
		consumerContext = new ClassPathXmlApplicationContext(this.getClass().getResource("/") + "consumer.xml");
		queryService = (QueryFwzxService) consumerContext.getBean("query");
	}

	//关闭系统
	public void platformDataClosed() {
		if (consumerContext != null) {
			consumerContext.close();
		}
	}

	public String query(String dataObjectCode, String conditions, String returnFields, int pageSize) {
		
		String dataObjectCodesStr = dataObjectCode;
		///
		/// 初始化
		this.platformDataInit();

		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (conditions.matches("(.*)JJBH(.*)")) {
			if (dataObjectCodesStr.equals("DWD_DPTJCJ_JJXX")) {
				dataObjectCodesStr = "DWD_DPT_JCJ_JJXX_ONEDAY";
			}
			if (dataObjectCodesStr.equals("DWD_DPTJCJ_CJXX")) {
				dataObjectCodesStr = "DWD_DPT_JCJ_CJXX_ONEDAY";
			}
			if (dataObjectCodesStr.equals("DWD_DPT_AJ_JBXX")) {
				dataObjectCodesStr = "DWD_DPT_AJ_JBXX_ONEDAY";
			}
			
			list = queryService.query(dataObjectCodesStr, conditions, returnFields, pageSize,userInfo);
			if (null == list || 0 == list.size() ) {
				dataObjectCodesStr = dataObjectCode;
				list = queryService.query(dataObjectCodesStr, conditions, returnFields, pageSize,userInfo);
			}
		}else{
			list = queryService.query(dataObjectCodesStr, conditions, returnFields, pageSize,userInfo);
		}
		
		/***
		 * 20170307 wangyong  待解决
		 * List<Map<String, String>> listOneDay = new ArrayList<Map<String,String>>();
		
		if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)") ||dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")|| dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
			listOneDay.clear();
			
			if (conditions.indexOf("JJRQSJ") > 0) {
				int JJRQSJindex =  conditions.indexOf("JJRQSJ");
				conditions = conditions.substring(0,JJRQSJindex-4);
			}
			
			if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)")) {
				
				listOneDay = queryService.query("DWD_DPT_JCJ_JJXX_ONEDAY", conditions, returnFields, pageSize,	userInfo);
			}
			if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_JCJ_CJXX_ONEDAY", conditions, returnFields, pageSize,	userInfo);
			}
			if (dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_AJ_JBXX_ONEDAY", conditions, returnFields, pageSize,	userInfo);
			}
		}
		///添加今天数据
		if (listOneDay != null && listOneDay.size() >0) {
			list.addAll(listOneDay);
		}*/
		
		String queryResult = "";
		queryResult = (new Gson()).toJson(list);
		
		/*
		 //测试使用
		System.out.println("========开始***===========");
		System.out.println("结果是：" + queryResult);
		System.out.println("========**end***===========");*/

		/// 出现接口问题报0
		if (queryResult.equals("null")) {
			queryResult = "0";
		}

		/// 初始化的結束
		this.platformDataClosed();

		return queryResult;
	}

	public String query(String dataObjectCode, String conditions, String returnFields, int pageSize, Boolean formatted,
			String resultStyle) {
		String dataObjectCodesStr = dataObjectCode;
		/// 初始化
		this.platformDataInit();
		String queryResult = "";

		queryResult = queryService.query(dataObjectCode, conditions, returnFields, pageSize, formatted, resultStyle,
				userInfo);
		/* //测试使用
		System.out.println("========开始***===========");
		System.out.println("结果是：" + queryResult);
		System.out.println("========**end***===========");*/
		
		
       /* String listOneDay = "";
		
        if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)") ||dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")|| dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
        	if (conditions.indexOf("JJRQSJ") > 0) {
				int JJRQSJindex =  conditions.indexOf("JJRQSJ");
				conditions = conditions.substring(0,JJRQSJindex);
			}
        	listOneDay="";
			if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_JCJ_JJXX_ONEDAY", conditions, returnFields, pageSize, formatted, resultStyle,userInfo);
			}
			if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_JCJ_CJXX_ONEDAY", conditions, returnFields, pageSize, formatted, resultStyle,
						userInfo);
			}
			if (dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_AJ_JBXX_ONEDAY",  conditions, returnFields, pageSize, formatted, resultStyle,
						userInfo);
			}
		}*/
		

		/// 出现接口问题报0
		if (queryResult.equals("null")) {
			queryResult = "0";
		}

		this.platformDataClosed();

		return queryResult;
	}

	/// 接警信息
	public String queryJJXX(String dataObjectCode, String conditions, String returnFields, int pageSize) {

		String queryResult = "";
		String dataObjectCodesStr = dataObjectCode;
		if (!dataObjectCode.equals("DWD_DPTJCJ_JJXX")) {

			return queryResult;
		}

		this.platformDataInit();

		int queryCounts = queryService.count(dataObjectCode, conditions, userInfo);
		// System.out.println(queryCounts);

		List<Map<String, String>> list = queryService.query(dataObjectCode, conditions, returnFields, queryCounts,	userInfo);
		
		
       /*List<Map<String, String>> listOneDay = new ArrayList<Map<String,String>>();
		
       if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)") ||dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")|| dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
    	   if (conditions.indexOf("JJRQSJ") > 0) {
				int JJRQSJindex =  conditions.indexOf("JJRQSJ");
				conditions = conditions.substring(0,JJRQSJindex);
			}
    	   listOneDay.clear();
			if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_JCJ_JJXX_ONEDAY", conditions, returnFields, pageSize,	userInfo);
			}
			if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_JCJ_CJXX_ONEDAY", conditions, returnFields, pageSize,	userInfo);
			}
			if (dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_AJ_JBXX_ONEDAY", conditions, returnFields, pageSize,	userInfo);
			}
		}
		///添加今天数据
		if (listOneDay != null&&listOneDay.size() > 0) {
			list.addAll(listOneDay);
		}*/

		listMapSort(list);

		List<Map<String, String>> frontSubList = list.subList(0, 50);
		if (queryCounts <= 50) {
			frontSubList.clear();
			frontSubList = list.subList(0, queryCounts);
		}

		queryResult = (new Gson()).toJson(frontSubList);
		
		/* //测试使用
		System.out.println("========开始***===========");
		// System.out.println("查询条数：" + queryCounts);
		System.out.println("结果是：" + queryResult);
		System.out.println("========**end***===========");*/

		/// 出现接口问题报0
		if (queryResult.equals("null")) {
			queryResult = "0";
		}

		this.platformDataClosed();

		return queryResult;
	}
	/// list 排序

	public void listMapSort(List<Map<String, String>> list) {

		Collections.sort(list, new Comparator<Map<String, String>>() {

			public int compare(Map<String, String> o1, Map<String, String> o2) {

				int objSortResult = o2.get("JJRQSJ").compareTo(o1.get("JJRQSJ"));

				return objSortResult;
			}
		});

	}

	/// 总条数
	public String count(String dataObjectCode, String conditions) {
		String dataObjectCodesStr = dataObjectCode;
		this.platformDataInit();
		int counts = queryService.count(dataObjectCode, conditions, userInfo);
		
		 /*int listOneDay = 0;
			
			if (dataObjectCodesStr.matches("(.*)DWD_DPT_JJXX(.*)") ||dataObjectCodesStr.matches("(.*)DWD_DPT_CJXX(.*)")|| dataObjectCodesStr.matches("(.*)DWD_DPT_JBXX(.*)")) {
				if (conditions.indexOf("JJRQSJ") > 0) {
					int JJRQSJindex =  conditions.indexOf("JJRQSJ");
					conditions = conditions.substring(0,JJRQSJindex);
				}
				listOneDay = 0;
				if (dataObjectCodesStr.matches("(.*)DWD_DPT_JJXX(.*)")) {
					listOneDay = queryService.count("DWD_DPT_JJXX_ONEDAY",conditions, userInfo);
				}
				if (dataObjectCodesStr.matches("(.*)DWD_DPT_CJXX(.*)")) {
					listOneDay = queryService.count("DWD_DPT_CJXX_ONEDAY",conditions, userInfo);
				}
				if (dataObjectCodesStr.matches("(.*)DWD_DPT_JBXX(.*)")) {
					listOneDay = queryService.count("DWD_DPT_JBXX_ONEDAY",conditions, userInfo);
				}
			}
			if(listOneDay>0)
			{
				counts += listOneDay;
			}*/
			
		String queryResult = "";

		queryResult = counts + "";
		
		/* //测试使用
		System.out.println("========开始***===========");
		System.out.println("结果是：" + queryResult);
		System.out.println("========**end***===========");*/

		/// 出现接口问题报0
		if (queryResult.equals("null")) {
			queryResult = "0";
		}

		this.platformDataClosed();

		return queryResult;
	}

	public String pageQuery(String dataObjectCode, String conditions, String returnFields, int pageSize,
			int pageNumber) {
		String dataObjectCodesStr = dataObjectCode;

		this.platformDataInit();
		List<Map<String, String>> list = queryService.pageQuery(dataObjectCode, conditions, returnFields, pageSize,
				pageNumber, userInfo);
		
		//List<Map<String, String>> list = queryService.query(dataObjectCode, conditions, returnFields, queryCounts,	userInfo);
		
		
	       List<Map<String, String>> listOneDay = new ArrayList<Map<String,String>>();
	       if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)") ||dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")|| dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
	    	   if (conditions.indexOf("JJRQSJ") > 0) {
					int JJRQSJindex =  conditions.indexOf("JJRQSJ");
					conditions = conditions.substring(0,JJRQSJindex);
				}
	    	   listOneDay.clear();
				if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)")) {
					listOneDay = queryService.pageQuery("DWD_DPT_JCJ_JJXX_ONEDAY",conditions, returnFields, pageSize,
							pageNumber, userInfo);
				}
				if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")) {
					listOneDay = queryService.pageQuery("DWD_DPT_JCJ_CJXX_ONEDAY",conditions, returnFields, pageSize,
							pageNumber, userInfo);
				}
				if (dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
					listOneDay = queryService.pageQuery("DWD_DPT_AJ_JBXX_ONEDAY", conditions, returnFields, pageSize,
							pageNumber, userInfo);
				}
			}
	       
	       if (listOneDay.size()>0) {
	    	   list.addAll(listOneDay);
		}
			
		
		
		String queryResult = "";

		queryResult = (new Gson()).toJson(list);
		
		/* //测试使用
		System.out.println("========开始***===========");
		System.out.println("结果是：" + queryResult);
		System.out.println("========**end***===========");*/

		/// 出现接口问题报0
		if (queryResult.equals("null")) {
			queryResult = "0";
		}

		this.platformDataClosed();

		return queryResult;
	}

	public String pageQuery(String dataObjectCode, String conditions, String returnFields, int pageSize, int pageNumber,
			Boolean formatted, String resultStyle) {

		this.platformDataInit();
		String queryResult = "";

		queryResult = queryService.pageQuery(dataObjectCode, conditions, returnFields, pageSize, pageNumber, formatted,
				resultStyle, userInfo);

		/* //测试使用
		System.out.println("========开始***===========");
		System.out.println("结果是：" + queryResult);
		System.out.println("========**end***===========");*/

		/// 出现接口问题报0
		if (queryResult.equals("null")) {
			queryResult = "0";
		}

		this.platformDataClosed();

		return queryResult;
	}
	// 根据资源代码
	/*
	 * public void getData(String dataObjectCode, int a) {
	 * 
	 * ClassPathXmlApplicationContext consumerContext = new
	 * ClassPathXmlApplicationContext( this.getClass().getResource("/") +
	 * "consumer.xml");
	 * 
	 * QueryService queryService = (QueryService)
	 * consumerContext.getBean("query");
	 * 
	 * List<Map<String, String>> list = queryService.pageQuery(dataObjectCode,
	 * "", "", 20, 1); for (Map<String, String> map : list) {
	 * System.out.println(map); }
	 * 
	 * consumerContext.close(); }
	 */

	/*
	 * ***接口名称*** dataObjectCode名称 1 苏州流动人口信息（ 资源代码：DWD_RG_ZZRK_SJ）
	 * 
	 * 2 住宿信息 （资源代码：DWD_ZAGUEST ）
	 * 
	 * 3 社区平台重点人员 （资源代码：DWD_PCS_T_RY_ZDRY）
	 * 
	 * 4 盘查人员 （资源代码：DWD_PCK_ZAPC_RYXX）
	 * 
	 * 5 看守所人员 （暂无，稍后再补）
	 * 
	 * 6 上网信息2016（资源代码：DWD_ZCPTSWRY2016）
	 * 
	 * 7 苏州常控预警表（资源代码：DWD_QB_ZDRY_SZCKYJ）
	 * 
	 * 8 案件处理信息（资源代码：DWD_DPT_AJ_CLCS_JGZXB）
	 */

	// 暂住人口的
	/*
	 * String dataObjectCode ="DWD_RG_ZZRK_SJ";
	 * 
	 * QueryService queryService = (QueryService)
	 * consumerContext.getBean("query");
	 */

	/**
	 * 获取资源信息数据
	 */

	/**
	 * dataObjectCode 资源代码 conditions where 条件 例如 ： and xx='' and ddd='dd'
	 * returnFields 返回的字段 默认全部字段 startIndex 不包含 endIndex 包含
	 * 
	 */

	/**
	 * 分页查询
	 * 
	 * @param dataObjectCode
	 *            资源代码
	 * @param conditions
	 *            where 条件 例如 ： and xx='' and ddd='dd'
	 * @param returnFields
	 *            返回的字段 默认全部字段
	 * @param pageSize
	 *            每页大小
	 * @param pageNumber
	 *            当前页
	 * @return
	 */

	/*
	 * List<Map<String,String>> list = queryService.pageQuery(dataObjectCode,
	 * "", "", 20, 1); for(Map<String,String> map : list) {
	 * System.out.println(map); }
	 */

	public void DisplayPage(String json, HttpServletResponse response) {
		response.setCharacterEncoding("gbk");
		PrintWriter pWriter = null;
		try {
			pWriter = (PrintWriter) response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pWriter.print(json);
		pWriter.flush();
	}
	
	public List<Map<String, String>> getOneDayList(String dataObjectCode,String conditions, String returnFields, int pageSize )
	{
		String dataObjectCodesStr = dataObjectCode;
		List<Map<String, String>> listOneDay = new ArrayList<Map<String,String>>();
		
		if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)") ||dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")|| dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
			listOneDay.clear();
			if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_JJXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_JCJ_JJXX_ONEDAY", conditions, returnFields, pageSize,	userInfo);
			}
			if (dataObjectCodesStr.matches("(.*)DWD_DPTJCJ_CJXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_JCJ_CJXX_ONEDAY", conditions, returnFields, pageSize,	userInfo);
			}
			if (dataObjectCodesStr.matches("(.*)DWD_DPT_AJ_JBXX(.*)")) {
				listOneDay = queryService.query("DWD_DPT_AJ_JBXX_ONEDAY", conditions, returnFields, pageSize,	userInfo);
			}
		}
		return listOneDay;
	}
	
	
}
