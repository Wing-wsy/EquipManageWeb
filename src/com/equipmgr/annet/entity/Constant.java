package com.equipmgr.annet.entity;

public class Constant {
	public static String RETURN_STATUS_OK = "0"; // 成功
	public static String RETURN_STATUS_EQUIP_EXIST = "1"; // 设备已经存在
	public static String RETURN_STATUS_SQL_ERROR = "2"; // 数据库操作失败
	public static String RETURN_STATUS_NO_SESSION = "3"; // 未登录
	public static String RETURN_STATUS_LOGIN_ERROR = "4"; // 登录失败
	public static String RETURN_STATUS_PARAM_ERROR = "5"; //输入参数有误 
	
	public static int ATTACH_TYPE_BODY = 1; // 体检
	public static int ATTACH_TYPE_PERSON = 2; // 人员管理
	public static int ATTACH_TYPE_DOSE = 3; // 个人剂量
	public static int ATTACH_TYPE_TREAT = 4; //疗养记录 
	public static int ATTACH_TYPE_TRAIN = 5; //防护培训 
	public static int ATTACH_TYPE_VACATION = 6; //保健假记录 
}
