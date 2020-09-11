package com.equipmgr.annet.entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonArray;

public class Cacher {
	private Cacher() {}
	
	private String logFilePath;
	private String attachFilePath;
	
	/*
	private ArrayList<Device> deviceList = null;
	private ArrayList<Repair> repairList = null;
	private ArrayList<Remind> remindList = null;
	private ArrayList<Mainten> maintenList = null;
	private ArrayList<Detection> detectionList = null;
	private ArrayList<Nitrogen> nitrogenList = null;
	private ArrayList<Dose> doseList = null;
	private ArrayList<Train> trainList = null;
	private ArrayList<Treat> treatList = null;
	private ArrayList<Exam> examList = null;
	private ArrayList<Person> personList = null;
	private ArrayList<Attach> attachList = null;
	private ArrayList<Vacation> vacationList = null;
	*/
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	
	private Map<String, ArrayList<String>> modalityMap = null;
	
	private static Cacher cacher = new Cacher();
	static {
		cacher.readConfig();
		cacher.initList();
	}
	public static Cacher getCacher()
	{
		return cacher;
	}
	
	public void printLog(String msg)
	{
		try {
			RandomAccessFile randomFile = new RandomAccessFile(this.logFilePath, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			randomFile.writeBytes(msg + "\r\n");
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readConfig()
	{
		String classPath = this.getClass().getResource("/").getPath();
        classPath = classPath.substring(0, classPath.lastIndexOf("classes"));
        classPath += "app.properties";
        
        Properties properties = new Properties();
        try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(classPath));
			properties.load(bufferedReader);

			logFilePath = properties.getProperty("logFilePath");
			attachFilePath = properties.getProperty("attachFilePath");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        catch (Exception e)
        {
			e.printStackTrace();
        }
	}

	private boolean initList()
	{
		/*
		deviceList = DBHelper.getDBHelper().selectDevices();
		repairList = DBHelper.getDBHelper().selectRepairList(null, null, null, null);
		remindList = DBHelper.getDBHelper().selectRemindList();
		maintenList = DBHelper.getDBHelper().selectMaintenList();
		detectionList = DBHelper.getDBHelper().selectDetectionList();
		nitrogenList = DBHelper.getDBHelper().selectNitrogenList();
		doseList = DBHelper.getDBHelper().selectDoseList();
		trainList = DBHelper.getDBHelper().selectTrainList();
		treatList = DBHelper.getDBHelper().selectTreatList();
		vacationList = DBHelper.getDBHelper().selectVacationList();
		examList = DBHelper.getDBHelper().selectExamList();
		personList = DBHelper.getDBHelper().selectPersonList();
		attachList = DBHelper.getDBHelper().selectAttachList();
		*/
		//登录的时候会查询设备类型和设备型号
		System.out.println("登录的时候会查询设备类型和设备型号");
		modalityMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> modalityList = DBHelper.getDBHelper().selectModalityList();
		for (int i = 0; i < modalityList.size(); i++)
		{
			String modality = modalityList.get(i);
			ArrayList<String> deviceList = DBHelper.getDBHelper().selectDeviceList(modality);
			modalityMap.put(modality, deviceList);
		}
		return true;
	}
	public String getAttachFilePath()
	{
		return this.attachFilePath;
	}

	public ArrayList<Attach> getAttachList(int type, int id, ArrayList<Attach> attachList)
	{
		ArrayList<Attach> list = new ArrayList<Attach>();
		
		for (Attach obj : attachList)
		{
			if (obj.getAttachType() == type && obj.getRelativeID() == id)
			{
				list.add(obj);
			}
		}

		return list;
	}
	public String getAttachListDiv(int type, int relativeId, ArrayList<Attach> attachList)
	{
		String attachListDiv = "<ul id=\"attachul\">";

		ArrayList<Attach> list = getAttachList(type, relativeId, attachList);
		for (Attach attach : list)
		{
			attachListDiv += "<li objid=\"";
			attachListDiv += attach.getiD();
			attachListDiv += "\">";
			attachListDiv += "<a class=\"close\" href=\"javascript:void(0)\" onclick=\"onDeleteAttach(" + attach.getiD() + ")\">x</a>";
			attachListDiv += "<a target=\"_blank\" href=\"/attach/" + attach.getAttachPath() + "\">";
			attachListDiv += "<img src=\"images/folder.png\">";
			attachListDiv += attach.getAttachName();
			attachListDiv += "</a>";
			
			
			
			attachListDiv += "</li>";
		}
		
		attachListDiv += "</ul>";
		return attachListDiv;
	}


	public Map<String, ArrayList<String>> getModalityMap()
	{
		return modalityMap;
	}

	
	public Attach getAttachById(int id, ArrayList<Attach> attachList)
	{
		Attach attach = null;
		
		for (Attach obj : attachList)
		{
			if (obj.getiD() == id)
			{
				attach = obj;
				break;
			}
		}
		return attach;
	}
	public Repair getRepairById(int id, ArrayList<Repair> repairList)
	{
		Repair repair = null;
		for (Repair obj : repairList)
		{
			if (obj.getId() == id)
			{
				repair = obj;
				break;
			}
		}
		return repair;
	}
	public Device getDeviceById(String device, ArrayList<Device> deviceList)
	{
		Device dev = null;
		for (Device obj : deviceList)
		{
			if (obj.getDevice().equals(device))
			{
				dev = obj;
				break;
			}
		}
		
		return dev;
	}
	public Exam getExamById(int id, ArrayList<Exam> examList)
	{
		Exam exam = null;
		for (Exam obj : examList)
		{
			if (obj.getId() == id)
			{
				exam = obj;
				break;
			}
		}	
		return exam;
	}
	public boolean removeDose(int id, ArrayList<Dose> doseList)
	{
		for (Dose obj : doseList)
		{
			if (obj.getId() == id)
			{
				doseList.remove(obj);
				break;
			}
		}
		return true;
	}
	public boolean removeExam(int id, ArrayList<Exam> examList)
	{
		for (Exam obj : examList)
		{
			if (obj.getId() == id)
			{
				examList.remove(obj);
				break;
			}
		}
		return true;
	}
	public boolean removeMainten(int id, ArrayList<Mainten> maintenList)
	{
		for (Mainten obj : maintenList)
		{
			if (obj.getID() == id)
			{
				maintenList.remove(obj);
				break;
			}
		}
		return true;
	}
	public boolean removeRemind(int id, ArrayList<Remind> remindList)
	{
		for (Remind obj : remindList)
		{
			if (obj.getId() == id)
			{
				remindList.remove(obj);
				break;
			}
		}
		return true;
	}
	public boolean removeTrain(int id, ArrayList<Train> trainList)
	{
		for (Train train : trainList)
		{
			if (train.getId() == id)
			{
				trainList.remove(train);
				break;
			}
		}
		return true;
	}	
	
	public boolean deviceExsit(String device, ArrayList<Device> deviceList)
	{
		boolean b = false;
		for (int i = 0; i < deviceList.size(); i++)
		{
			Device de = deviceList.get(i);
			if (device.equals(de.getDevice()))
			{
				b = true;
				break;
			}
		}
		return b;
	}
	
	
	public Statis findStatis(java.util.Date date, ArrayList<Statis> list)
	{
		Statis s = null;
		
		for (Statis d : list)
		{
			if (d.getStudyDate().equals(date))
			{
				s = d;
				break;
			}
		}
		
		return s;
	}
	
	public Device findDeviceById(String strDevice, ArrayList<Device> deviceList)
	{
		Device device = null;
		
		for (Device obj : deviceList)
		{
			if (obj.getDevice().equals(strDevice))
			{
				device = obj;
				break;
			}
		}
		return device;
	}
	public HandOver findHandOver(java.util.Date date, ArrayList<HandOver> list)
	{
		HandOver s = null;
		
		for (HandOver d : list)
		{
			if (d.getHandoverDate().equals(date))
			{
				s = d;
				break;
			}
		}
		
		return s;
	}
	public Occur findOccor(java.util.Date date, ArrayList<Occur> list)
	{
		Occur s = null;
		
		for (Occur d : list)
		{
			if (d.getOccurTime().equals(date))
			{
				s = d;
				break;
			}
		}
		
		return s;
	}
	
	public Timestamp getTimestampFromString(String strDate)
	{
		Timestamp tt = null;
		try {
			java.util.Date date = this.simpleDateFormat.parse(strDate);
			tt = new Timestamp(date.getTime());
		} catch (Exception e ) {
		}

		return tt;
	}
	
	public java.sql.Date getSqlDateFormString(String str)
	{
		java.sql.Date dt = null;
		try {
			dt = java.sql.Date.valueOf(str);
		} catch (Exception e ) {
			e.printStackTrace();
		}
		
		return dt;
	}
	
	public java.util.Date getUtilDateFromStr(String str)
	{
		java.util.Date date = null;
		try {
			date = this.simpleDateFormat2.parse(str);
		} catch (Exception e) {
		}
		return date;
	}
	
	public int getDeviceSum(String modality, String dev, ArrayList<Device> deviceList)
	{
		int num = 0;
		for (Device device : deviceList)
		{
			if (modality.equals("全部") || device.getModality().equals(modality))
			{
				if (dev.equals("全部") || device.getDevice().equals(dev))
				{
					num += 1;
				}
			}
		}
		return num;
		
	}
	public HandOver findUserName(java.util.Date date, ArrayList<HandOver> list)
	{
		HandOver s =new HandOver();
		JsonArray ja = new JsonArray();
		for (HandOver d : list)
		{
			//boolean flag = false;
			if (d.getHandoverDate().equals(date))
			{
				s.setHandoverDate(date);
				ja.add(d.getUserName());
			}
			s.setJa(ja);
		}
		
		return s;
	}
	
}
