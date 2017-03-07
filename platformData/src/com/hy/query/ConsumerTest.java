package com.hy.query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;

import cn.com.dimensoft.esb.query.QueryFwzxService;
import cn.com.dimensoft.esb.query.QueryService;

public class ConsumerTest {
	private static final long serialVersionUID = 1L;
	
	 private final static String userInfo = "{'account':'040708','password':'szzhzx'}";

	public static void main(String[] args) throws Exception {

		// String str = ConsumerTest.class.getClass().getResource("/") +
		// "consumer.xml";
		ClassPathXmlApplicationContext consumerContext = new ClassPathXmlApplicationContext(
				ConsumerTest.class.getClass().getResource("/") + "consumer.xml");

		// 根据资源代码

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
		///String dataObjectCode = "DWD_RG_ZZRK_SJ";
		
		///案件基本信息
	/*	String dataObjectCode = "DWD_DIC_ITEMS_ST";
		String conditions ="";
		*/
		
		String dataObjectCode = "DWD_DPTJCJ_JJXX";
		String conditions ="";
		//dataObjectCode=DWD_DIC_ITEMS_ST&conditions=DIC_NAME=%27案件类别%27
	
		/**
		 * 获取资源信息数据
		 * dataObjectCode 资源代码 conditions where 条件 例如 ： and xx='' and ddd='dd'
		 * returnFields 返回的字段 默认全部字段 startIndex 不包含 endIndex 包含
		 */

		/**
		 * 分页查询
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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		
		
		String currentDate = simpleDateFormat.format(new Date());
		
		System.out.println(currentDate);
		
		//currentDate = "BJR='汲萌萌'";
		///接警编号 立刻查询
		//conditions ="JJBH LIKE '%J3205071716111500001%'";
		
		//conditions= "JJRQSJ>= to_date('20161111000000' ,'yyyymmddhh24miss') AND JJRQSJ<= to_date('20161111135959' ,'yyyymmddhh24miss') ";
///
//		conditions= "BJLX = '210101' and JJRQSJ>= to_date('20161115000000' ,'yyyymmddhh24miss') AND JJRQSJ<= to_date('20161115235959' ,'yyyymmddhh24miss') ";
		
		conditions= "CITGC_TIME>= to_date('20160101000000' ,'yyyymmddhh24miss')";
		
		//conditions ="JJRQSJ>= to_date('20161111000000' ,'yyyymmddhh24miss') AND JJRQSJ<= to_date('20161111235959' ,'yyyymmddhh24miss')";
		//conditions =" to_char(JJRQSJ,'yyyymmdd')='20090801'";
		 //conditions ="";
//		QueryService queryCount = (QueryService) consumerContext.getBean("query");
//		int counts = queryCount.count(dataObjectCode, conditions);
//		System.out.println("this is 查询人数：" + counts);
//		QueryService queryService = (QueryService) consumerContext.getBean("query");
//		List<Map<String, String>> list = queryService.query(dataObjectCode, conditions, "", 1000);
		
		
		
		////新接口
		QueryFwzxService pageQueryService = (QueryFwzxService) consumerContext.getBean("query");
		List<Map<String, String>> pageList = pageQueryService.query(dataObjectCode, "", "", 100,userInfo);
		//List<Map<String, String>> pageList = pageQueryService.pageQuery("DWD_DPTJCJ_JJXX", "CITGC_TIME>= to_date('20160101000000' ,'yyyymmddhh24miss')", "JJBH,DJDWMC", 10, 3000000);
		System.out.println(pageList);
	
//		QueryService queryService = (QueryService) consumerContext.getBean("pageQuery");
	
//		queryService.pageQuery("DWD_DPTJCJ_JJXX", "CITGC_TIME>= to_date('20160101000000' ,'yyyymmddhh24miss')", "", 1, 20, "", ""); // (dataObjectCode, conditions, "", 1000);
		
		//Gson gson = new Gson();
		
		///转化成JSON进行返回
		String aString = (new Gson()).toJson(pageList);
		System.out.println("========**开始***===========");
		System.out.println("结果是：" + aString);
		System.out.println("========**end***===========");

		
		/*
		 * List<Map> 展示
		 * for(Map<String,String> map : list) { //System.out.println(map);
		 * 
		 * // JSONArray queryResult =
		 * 
		 * System.out.println("========**开始***===========");
		 * System.out.println("结果是：" + map);
		 * System.out.println("========**end***==========="); }
		 */

		consumerContext.close();

	}

}
