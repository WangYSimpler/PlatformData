package com.hy.manager.sys.module;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import com.hy.util.DateUtils;

public class SyncJJXXDate {

	private static Logger logger = Logger.getLogger(SyncJJXXDate.class);

	/**
	 * 判断字符串串是否传递正确
	 * 
	 * @param dateStartStr
	 * @param dateEndStr
	 * @return
	 */
	public boolean isCorrectDateStartEnd(String dateStartStr, String dateEndStr) {

		String dateStart = dateStartStr;
		String dateEnd = dateEndStr;

		boolean isCorrectDateflag = true;
		if (dateStart.compareTo(dateEnd) > 0) {
			isCorrectDateflag = false;
			logger.info("SyncJJXXDate:日期查询异常 " + dateStart + "和" + dateEnd);
		}
		return isCorrectDateflag;
	}

	/**
	 * 是否是今天
	 * @param dateFormatStr
	 * @return
	 */
	private boolean isToday(String dateFormatStr) {

		String dateFormat = dateFormatStr;
		Date d = DateUtils.getUtilDateByString(dateFormat, "yyyyMMddHHmmss");
		String dateStartFormt = DateUtils.getDateTime(d, "yyyyMMdd");

		String currentDateStr = DateUtils.getNowTime("yyyyMMdd");

		boolean isTodayFlag = false;
		if (dateStartFormt.equals(currentDateStr)) {
			isTodayFlag = true;
		}
		return isTodayFlag;
	}

	
	public Map<String,String> getDataObjectCodeConditioMap(String objectCodesStr,String dateStartStr, String dateEndStr) {

		String objectCodes = objectCodesStr;
		String dateStart = dateStartStr;
		String dateEnd = dateEndStr;
		if (!isCorrectDateStartEnd(dateStart, dateEnd)) {
			logger.info("SyncJJXXDate:日期异常" + dateStart + "和" + dateEnd);
		}
		String oneDayObjectCodeStr = this.getOneDayObjectCode(objectCodes);
		
		
		Map<String, String> dataObjectCodeConditioMap = new HashMap<String, String>();
		if (this.isToday(dateStart)) {
			String oneDayConditionStr = this.getOneDayConditionStr(dateStart, dateEnd);
			dataObjectCodeConditioMap.clear();
			dataObjectCodeConditioMap.put(oneDayObjectCodeStr, oneDayConditionStr);
		}else if (!this.isToday(dateEnd)) {
			dataObjectCodeConditioMap.clear();
			String historyDayConditionStr = this.getHistoryDayConditionStr(dateStart, dateEnd);
			dataObjectCodeConditioMap.put("DWD_DPTJCJ_JJXX", historyDayConditionStr);
		}else if(!this.isToday(dateStart) && this.isToday(dateEnd)){
				String preNowTimeStr = DateUtils.getPreNowTime("yyyyMMdd") +"235959";
				String currentDateTimeStr = DateUtils.getNowTime("yyyyMMdd") + "000000";
				
				String historyDayConditionStr = this.getHistoryDayConditionStr(dateStart, preNowTimeStr);
				String oneDayConditionStr = this.getOneDayConditionStr(currentDateTimeStr, dateEnd);
				dataObjectCodeConditioMap.clear();
				dataObjectCodeConditioMap.put(oneDayObjectCodeStr, oneDayConditionStr);
				dataObjectCodeConditioMap.put("DWD_DPTJCJ_JJXX", historyDayConditionStr);
		}

		return dataObjectCodeConditioMap;
	}

	/** DWD_DPT_JCJ_JJXX_ONEDAY
	 * 大平台接口 获取案件文正新增的数据
	 * @param objectCodeStr
	 * @return
	 */
	private String getOneDayObjectCode(String objectCodeStr){
		String objectCode =  objectCodeStr;
		String oneDayObjectCodes ="";
		if ("DWD_DPTJCJ_JJXX".equals(objectCode)) {
			oneDayObjectCodes = "DWD_DPT_JCJ_JJXX_ONEDAY";
		}else if ("DWD_DPT_AJ_JBXX".equals(objectCode)) {
			oneDayObjectCodes = "DWD_DPT_AJ_JBXX_ONEDAY";
		}else if ("DWD_DPTJCJ_CJXX".equals(objectCode)) {
			oneDayObjectCodes = "DWD_DPT_JCJ_CJXX_ONEDAY";	
		}
		return oneDayObjectCodes;
	}
	
	private String getOneDayConditionStr(String dateStartStr, String dateEndStr) {

		String dateStart = dateStartStr;
		String dateEnd = dateEndStr;
		String oneDayConditionStr = "";
		if (this.isToday(dateStart) && this.isToday(dateEnd)) {
			oneDayConditionStr = " DJSJ >= '" + dateStart + "' AND DJSJ <= '" + dateEnd + "' ";
		}

		return oneDayConditionStr;
	}

	private String getHistoryDayConditionStr(String dateStartStr, String dateEndStr) {

		String dateStart = dateStartStr;
		String dateEnd = dateEndStr;
		/// 日期的格式结束日期不是今天
		String historyDayConditionStr = "";
		if (!this.isToday(dateEnd)) {
			historyDayConditionStr = " DJSJ>= to_date('" + dateStart + "' ,'yyyymmddhh24miss') AND DJSJ<= to_date('"
					+ dateEnd + "' ,'yyyymmddhh24miss') ";
		}

		return historyDayConditionStr;
	}

	
	public static void main(String[] args) {
		
		System.out.println(new SyncJJXXDate().isCorrectDateStartEnd("20170420125859", "20170420125900"));
		System.out.println(new SyncJJXXDate().isToday("20170420125959"));
	}

}
