package com.equipmgr.annet.entity;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.equipmgr.annet.commen.QualityStatistics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DBHelper {
	
	private static DBHelper dbHelper = new DBHelper();
	static {
		dbHelper.initDB();
	};
	public static DBHelper getDBHelper()
	{
		return dbHelper;
	}

	private Connection conn;
	
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public boolean initDB()
	{
		try {
			String classPath = this.getClass().getResource("/").getPath();
	        classPath = classPath.substring(0, classPath.lastIndexOf("classes"));
	        classPath += "app.properties";
	        
			Properties pro = new Properties();
			BufferedReader bufferedReader = new BufferedReader(new FileReader(classPath));
			pro.load(bufferedReader);
			
			String ip = pro.getProperty("ip", "127.0.0.1");
			String port = pro.getProperty("port", "1433");
			String dbName = pro.getProperty("dbName");
			String userId = pro.getProperty("userId");
			String pwd = pro.getProperty("pwd");
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://";
			url += ip;
			url += ":";
			url += port;
			url += ";databaseName=";
			url += dbName;
			conn = DriverManager.getConnection(url, userId, pwd);
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean addDevice(Device dev)
	{
		String sql = "";
		sql += "INSERT INTO Equipment(EquipName,FactoryNO,Modality,Device,Price,BuyTime,MadeIn,Unit ";
		sql += ",StartTime,Princepal,EquipNO,QualityStartTime,QualityEndTime,Tel,RepairPerson,handbook,ReportCode";
		sql += ",RepairPersonTel,EquipStatus,BaoxiuPrice)";
		sql += "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement ps = null;
		try
		{
			int index = 1;
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, dev.getName());
			ps.setString(index++, dev.getMakeBy());
			ps.setString(index++, dev.getModality());
			ps.setString(index++, dev.getDevice());
			ps.setString(index++, dev.getBryPrice());
			ps.setDate(index++, dev.getInstallDate());
			ps.setString(index++, dev.getMadeIn());
			ps.setString(index++, dev.getUseUnit());
			ps.setDate(index++, dev.getUseStartDate());
			ps.setString(index++, dev.getResponseble());
			ps.setString(index++, dev.getEquNo());
			ps.setDate(index++, dev.getMantanStartDate());
			ps.setDate(index++, dev.getMantanEndDate());
			ps.setString(index++, dev.getFactoryTel());
			ps.setString(index++, dev.getContactPerson());
			ps.setString(index++, "");
			ps.setString(index++, dev.getReportNo());
			ps.setString(index++, dev.getContactPhone());
			ps.setString(index++, dev.getStatus());
			ps.setString(index++, dev.getMantanPrice());
			
			int re = ps.executeUpdate();
			return re > 0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addDoctor(Doctor doctor)
	{
		String sql = "";
		sql += "insert into EM_Person (name,sex,education,title,birthday, ";
		sql += "duty,school,degree,StartJob,RadiationAge,RuzhiDate) ";
		sql += "values(?,?,?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			int index = 1;
			ps.setString(index++, doctor.getName());
			ps.setString(index++, doctor.getSex());
			ps.setString(index++, doctor.getEducation());
			ps.setString(index++, doctor.getTitle());
			ps.setDate(index++, doctor.getBirthday());
			ps.setString(index++, doctor.getDuty());
			ps.setString(index++, doctor.getSchool());
			ps.setString(index++, doctor.getDegree());
			ps.setDate(index++, doctor.getStartJob());
			ps.setString(index++, doctor.getRadiationAge());
			ps.setDate(index++, doctor.getRuzhiDate());
			
			return ps.executeUpdate() > 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try { 
				if(ps!=null) { ps.close(); } 
			} catch (Exception e) {}
		}
				
		return false;
	}
	
	public ArrayList<Doctor> selectDoctorList()
	{
		ArrayList<Doctor> list = new ArrayList<Doctor>();
		
		String sql = "";
		sql += "select top 1000 ID,name,sex,birthday,education,title,duty,school,";
		sql += "degree,StartJob,RadiationAge,RuzhiDate from EM_Person where 1=1";
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int ID = rs.getInt("ID");
				String name = rs.getString("name");
				String sex = rs.getString("sex");
				String education = rs.getString("education");
				String title = rs.getString("title");
				String duty = rs.getString("duty");
				String school = rs.getString("school");
				String degree = rs.getString("degree");
				String RadiationAge = rs.getString("RadiationAge");
				Date birthday = rs.getDate("birthday");
				Date StartJob = rs.getDate("StartJob");
				Date RuzhiDate = rs.getDate("RuzhiDate");
				
				Doctor doctor = new Doctor();
				doctor.setID(ID);
				doctor.setName(name);
				doctor.setSex(sex);
				doctor.setEducation(education);
				doctor.setTitle(title);
				doctor.setDuty(duty);
				doctor.setSchool(school);
				doctor.setDegree(degree);
				doctor.setRadiationAge(RadiationAge);
				doctor.setBirthday(birthday);
				doctor.setStartJob(StartJob);
				doctor.setRuzhiDate(RuzhiDate);
				
				list.add(doctor);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public ArrayList<TimePair> selectStatisticRepairList(String modality, String device, Timestamp start, Timestamp end)
	{
		ArrayList<TimePair> list = new ArrayList<TimePair>();
		String sql = "";
		sql += "SELECT TOP 1000 occur_time,restore_time FROM break_report WHERE extent='停机'  ";
		
		if (modality != null && !modality.equals("全部") && !modality.equals(""))
		{
			String[] split = null;
			String modalityWhere = "";
			if(modality.indexOf(",")!=-1) {
				split = modality.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+modality+"') ";
			}
			sql += modalityWhere;
		}
		/**
		if (modality != null && !modality.equals("全部"))
		{
			sql += " and modality=?";
		}
		*/
		if (device != null && !device.equals("全部"))
		{
			sql += " and device=?";
		}
		if (start != null)
		{
			sql += " and (restore_time>=? or restore_time is null)";
		}
		if (end != null)
		{
			sql += " and occur_time<? and occur_time>?";
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try 
		{
			ps = getConn().prepareStatement(sql);
			/**
			if (modality != null && !modality.equals("全部"))
			{
				ps.setString(index++, modality);
			}
			*/
			if (device != null && !device.equals("全部"))
			{
				ps.setString(index++, device);
			}
			if (start != null)
			{
				ps.setTimestamp(index++, start);
			}
			if (end != null)
			{
				ps.setTimestamp(index++, end);
				ps.setTimestamp(index++, start);
			}
			
			rs = ps.executeQuery();
			while (rs.next())
			{
				
				Timestamp restore_time = rs.getTimestamp("restore_time");
				Timestamp occur_time = rs.getTimestamp("occur_time");
				
				TimePair tp = new TimePair();
				tp.setStart(occur_time);
				tp.setEnd(restore_time);
				
				list.add(tp);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public ArrayList<Repair> selectRepairList(String modality, String device, Timestamp start, Timestamp end)
	{
		ArrayList<Repair> list = new ArrayList<Repair>();
		String sql = "";
		sql += "SELECT TOP 1000 id,arrive_time,isnull(repair_content,'') as repair_content,replace,cuase,isnull(result,'') as result,restore_time,engineer, ";
		sql += "Confirm,ConfirmPerson,occur_time,report_time,phenomena,extent,report_person,Modality,Device ";
		sql += "FROM break_report WHERE 1=1 ";
		if (modality != null && !modality.equals("全部") && !modality.equals(""))
		{
			//sql += " and modality=?";
			String[] split = null;
			String modalityWhere = "";
			if(modality.indexOf(",")!=-1) {
				split = modality.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+modality+"') ";
			}
			sql += modalityWhere;
		}
		if (device != null && !device.equals("全部"))
		{
			sql += " and device=?";
		}
		if (start != null)
		{
			sql += " and occur_time>=?";
		}
		if (end != null)
		{
			sql += " and occur_time<?";
		}
		
		sql += " order by report_time desc;";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try 
		{
			ps = getConn().prepareStatement(sql);
			/**
			if (modality != null && !modality.equals("全部"))
			{
				ps.setString(index++, modality);
			}
			*/
			if (device != null && !device.equals("全部"))
			{
				ps.setString(index++, device);
			}
			if (start != null)
			{
				ps.setTimestamp(index++, start);
			}
			if (end != null)
			{
				ps.setTimestamp(index++, end);
			}
			
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("id");
				String repair_content = rs.getString("repair_content");
				String replace = rs.getString("replace");
				String cuase = rs.getString("cuase");
				String result = rs.getString("result");
				String engineer = rs.getString("engineer");
				String Confirm = rs.getString("Confirm");
				String ConfirmPerson = rs.getString("ConfirmPerson");
				String phenomena = rs.getString("phenomena");
				String extent = rs.getString("extent");
				String report_person = rs.getString("report_person");
				String Modality = rs.getString("Modality");
				String Device = rs.getString("Device");
				Timestamp restore_time = rs.getTimestamp("restore_time");
				Timestamp occur_time = rs.getTimestamp("occur_time");
				Timestamp report_time = rs.getTimestamp("report_time");
				Timestamp arrive_time = rs.getTimestamp("arrive_time");
				
				Repair repair = new Repair();
				repair.setId(id);
				repair.setRepair_content(repair_content);
				repair.setReplace(replace);
				repair.setCuase(cuase);
				repair.setResult(result);
				repair.setEngineer(engineer);
				repair.setConfirm(Confirm);
				repair.setConfirmPerson(ConfirmPerson);
				repair.setPhenomena(phenomena);
				repair.setExtent(extent);
				repair.setReport_person(report_person);
				repair.setModality(Modality);
				repair.setDevice(Device);
				repair.setRestore_time(restore_time);
				repair.setOccur_time(occur_time);
				repair.setReport_time(report_time);
				repair.setArrive_time(arrive_time);
				
				list.add(repair);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public ArrayList<Device> selectDevices() 
	{
		ArrayList<Device> list = new ArrayList<Device>();

		String sql = "";
		sql += "SELECT TOP 1000 id,Modality,Device,Price,BuyTime,MadeIn,Unit,StartTime,Princepal,EquipNO,QualityStartTime,";
		sql += "QualityEndTime,Tel,RepairPerson,handbook,EquipName,FactoryNO,ReportCode,RepairPersonTel,EquipStatus,BaoxiuPrice ";
		sql += "FROM Equipment ";
		sql += "WHERE 1=1 ";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				String modality = rs.getString("Modality");
				String device = rs.getString("Device");
				String equNo = rs.getString("EquipNO");
				String name = rs.getString("EquipName");
				String status = rs.getString("EquipStatus");
				String madeIn = rs.getString("MadeIn");
				String makeBy = rs.getString("FactoryNO");
				String bryPrice = rs.getString("Price");
				String useUnit = rs.getString("Unit");
				String responseble = rs.getString("Princepal");
				String reportNo = rs.getString("ReportCode");
				String factoryTel = rs.getString("Tel");
				String contactPerson = rs.getString("RepairPerson");
				String contactPhone = rs.getString("RepairPersonTel");
				String mantanPrice = rs.getString("BaoxiuPrice");

				Date mantanStartDate = rs.getDate("QualityStartTime");
				Date mantanEndDate = rs.getDate("QualityEndTime");
				Date installDate = rs.getDate("BuyTime");
				Date useStartDate = rs.getDate("StartTime");
				
				Device en = new Device();
				en.setDevice(device);
				en.setEquNo(equNo);
				en.setName(name);
				en.setStatus(status);
				en.setMadeIn(madeIn);
				en.setMakeBy(makeBy);
				en.setBryPrice(bryPrice);
				en.setUseUnit(useUnit);
				en.setResponseble(responseble);
				en.setInstallDate(installDate);
				en.setUseStartDate(useStartDate);
				en.setReportNo(reportNo);
				en.setFactoryTel(factoryTel);
				en.setContactPerson(contactPerson);
				en.setContactPhone(contactPhone);
				en.setMantanEndDate(mantanEndDate);
				en.setMantanStartDate(mantanStartDate);
				en.setMantanPrice(mantanPrice);
				en.setModality(modality);
				
				list.add(en);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				if(rs!=null){
					rs.close();
				}
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	public MedicineManage selectPotionById(String manageID)
	{
		String sql = "select TOP 1000 ManageID,OEMCode,OEMName,MedicineBatch,MedicineCode,MedicineName,MedicineUnit,MedicineDeadline,InventoryNum,ThresholdNum,MedicineType from PA_MedicineManage where ManageID = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(1, manageID);
			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("ManageID");
				String code = rs.getString("OEMCode");
				String name = rs.getString("OEMName");
				String medicineBatch = rs.getString("MedicineBatch");
				String medicineCode = rs.getString("MedicineCode");
				String medName = rs.getString("MedicineName");
				String medUnit = rs.getString("MedicineUnit");
				String medDate = rs.getString("MedicineDeadline");
				int inventoryNum = rs.getInt("InventoryNum");
				int thresholdNum = rs.getInt("ThresholdNum");
				int medType = rs.getInt("MedicineType");
				
				MedicineManage medicine = new MedicineManage();
				medicine.setManageID(id);
				medicine.setOEMCode(code);
				medicine.setOEMName(name);
				medicine.setMedicineBatch(medicineBatch);
				medicine.setMedicineCode(medicineCode);
				medicine.setMedicineName(medName);
				medicine.setMedicineUnit(medUnit);
				medicine.setMedicineDeadline((java.util.Date)new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(medDate));
				medicine.setInventoryNum(inventoryNum);
				medicine.setThresholdNum(thresholdNum);
				medicine.setMedicineType(medType);
				return medicine;
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				if(rs!=null){
					rs.close();
				}
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	public User selectLoginUser(String userId, String passWord)
	{
		String sql = "SELECT UserName,sLevel,DeptName,Modality FROM VSecurity WHERE Name=? AND Password=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, passWord);
			rs = ps.executeQuery();
			if (rs.next())
			{
				String UserName = rs.getString("UserName");
				String sLevel = rs.getString("sLevel");
				String DeptName = rs.getString("DeptName");
				String Modality = rs.getString("Modality");
				User user = new User();
				user.setUserName(UserName);
				user.setsLevel(sLevel);
				user.setDeptName(DeptName);
				user.setModality(Modality);
				return user;
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				if(rs!=null){
					rs.close();
				}
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public List<VPurview> selectVPurview(String name) {
		//DelFlag:1鏄鐢紝2鏄垹闄わ紝0鏄甯�
		String sql = "select Name,Modality,DeptName from VPurview where Name=? AND DelFlag = '0'";
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VPurview> vPurviewList = new ArrayList<VPurview>();
		try{
			ps = getConn().prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			while (rs.next())
			{
				String Name = rs.getString("Name");
				String Modality = rs.getString("Modality");
				String DeptName = rs.getString("DeptName");
				VPurview vPurview = new VPurview();
				vPurview.setName(Name);
				vPurview.setModality(Modality);
				vPurview.setDeptName(DeptName);
				
				vPurviewList.add(vPurview);
			}
			return vPurviewList;
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vPurviewList;
	}
	
	public boolean addRepair(Repair repair)
	{
		String sql = "";
		sql += "INSERT INTO break_report(Modality,Device,occur_time,report_time,";
		sql += "phenomena,extent,report_person,modified) output inserted.id VALUES(?,?,?,?,";
		sql += "?,?,?,GETDATE())";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, repair.getModality());
			ps.setString(index++, repair.getDevice());
			ps.setTimestamp(index++, repair.getOccur_time());
			ps.setTimestamp(index++, repair.getReport_time());
			ps.setString(index++, repair.getPhenomena());
			ps.setString(index++, repair.getExtent());
			ps.setString(index++, repair.getReport_person());
			
			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("id");
				repair.setId(id);
				
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
		finally
		{
			try {
				if(ps!=null){
					ps.close();
				}
				if (rs != null)
				{
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	ArrayList<Plan> selectPlanList()
	{
		ArrayList<Plan> list = new ArrayList<Plan>();
		
		String sql = "";
		sql += "select top 1000 ID,name,content from EM_Plan where 1=1";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int ID = rs.getInt("ID");
				String name = rs.getString("name");
				String content = rs.getString("content");
				
				Plan plan = new Plan();
				plan.setID(ID);
				plan.setName(name);
				plan.setContent(content);
				
				list.add(plan);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean addPlan(Plan plan)
	{
		String sql = "";
		sql += "INSERT INTO EM_Plan(name,content)VALUS(?,?)";
		
		PreparedStatement ps = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, plan.getName());
			ps.setString(index++, plan.getContent());

			return ps.executeUpdate() > 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
		finally
		{
			try {
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<Remind> selectRemindList(String Modality)
	{	
		String modalityWhere = "";
		if(Modality != null && !Modality.equals("") && !Modality.equals("ALL") && !Modality.equals("All")) {
			String[] split = null;
			if(Modality.indexOf(",")!=-1) {
				split = Modality.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+Modality+"') ";
			}
		}
		
		
		ArrayList<Remind> list = new ArrayList<Remind>();
		String sql = "";
		sql += "SELECT TOP 1000 id,type,msg,status,circle,FirstRemaind,ContinueTime";
		sql += ",StartDate,EndDate,Modality,Device FROM EM_Remaind where 1=1 ";
		sql += modalityWhere;
		
		System.out.println(sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("id");
				String Device = rs.getString("Device");
				String msg = rs.getString("msg");
				Date StartDate = rs.getDate("StartDate");
				Date EndDate = rs.getDate("EndDate");
				
				Remind remind = new Remind();
				remind.setId(id);
				remind.setDevice(Device);
				remind.setMsg(msg);
				remind.setStartDate(StartDate);
				remind.setEndDate(EndDate);
				
				list.add(remind);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean addRemind(Remind remind)
	{
		String sql = "";
		sql += "insert into EM_Remaind (msg,Device,StartDate,EndDate,Modality,status,type,circle,ContinueTime,FirstRemaind) "
				+ "output inserted.id values(?,?,?,?,?,?,?,?,?,?)";
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, remind.getMsg());
			ps.setString(index++, remind.getDevice());
			ps.setDate(index++, remind.getStartDate());
			ps.setDate(index++, remind.getEndDate());
			ps.setString(index++, remind.getModality());
			ps.setString(index++, remind.getStatus());
			ps.setString(index++, remind.getType());
			ps.setString(index++, remind.getCircle());
			ps.setString(index++, remind.getContinueTime());
			ps.setDate(index++, remind.getFirstRemaind());
			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("id");
				remind.setId(id);
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
		finally
		{
			try {
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean deleteRemind(int id)
	{
		String sql = "delete from EM_Remaind where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<String> selectDeviceList(String modality)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		String sql = "select Device from Equipment where Modality=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(1, modality);
			rs = ps.executeQuery();
			while (rs.next())
			{
				String device = rs.getString("Device");
				list.add(device);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public ArrayList<String> selectModalityList()
	{
		ArrayList<String> modalityList = new ArrayList<String>();
		
		String sql = "select distinct Modality from Equipment";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int x = 1;
		x++;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				String modality = rs.getString("Modality");
				modalityList.add(modality);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		
		return modalityList;
	}
	
	public boolean addMainten(Mainten mainten)
	{
		String sql = "";
		
		sql += "INSERT INTO EM_Mainten (Modality ,Device ,MaintenContent ,MaintenPerson ,MaintenTime ,ConfirmPerson)";
		sql += " output inserted.id VALUES(?,?,?,?,?,?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, mainten.getModality());
			ps.setString(index++, mainten.getDevice());
			ps.setString(index++, mainten.getMaintenContent());
			ps.setString(index++, mainten.getMaintenPerson());
			ps.setDate(index++, mainten.getMaintenTime());
			ps.setString(index++, mainten.getConfirmPerson());

			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("id");
				mainten.setID(id);
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
				if (rs != null)
				{
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	public boolean addPotion(MedicineManage med)
	{
		
		
		String sql = "";
		sql += "INSERT INTO PA_MedicineManage (OEMCode,OEMName,MedicineBatch,MedicineCode,MedicineName,MedicineUnit,MedicineDeadline,InventoryNum,ThresholdNum,MedicineType)";
		sql += " output inserted.ManageID  VALUES(?,?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, med.getOEMCode());
			ps.setString(index++, med.getOEMName());
			ps.setString(index++, med.getMedicineBatch());
			ps.setString(index++, med.getMedicineCode());
			ps.setString(index++, med.getMedicineName());
			ps.setString(index++, med.getMedicineUnit());
			ps.setString(index++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(med.getMedicineDeadline()));
			ps.setInt(index++, med.getInventoryNum());
			ps.setInt(index++, med.getThresholdNum());
			ps.setInt(index++, med.getMedicineType());
			

			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("ManageID");
				med.setManageID(id);
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
				if (rs != null)
				{
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	public boolean selectPotion(MedicineManage med)
	{
		
		
		String sql = "";
		sql += " select * from PA_MedicineManage where OEMCode = ? and MedicineBatch = ? and MedicineName = ?";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, med.getOEMCode());
			ps.setString(index++, med.getMedicineBatch());
			ps.setString(index++, med.getMedicineName());
			
			rs = ps.executeQuery();
			if (rs.next())
			{
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
				if (rs != null)
				{
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	public boolean updatePotion(MedicineManage med)
	{
		String sql = "";
		sql += "update PA_MedicineManage set OEMCode=?,OEMName=?,MedicineBatch=?,MedicineCode=?,MedicineName=?,MedicineUnit=?,MedicineDeadline=?,InventoryNum=?,ThresholdNum=?,MedicineType=? where ManageID = ? ";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, med.getOEMCode());
			ps.setString(index++, med.getOEMName());
			ps.setString(index++, med.getMedicineBatch());
			ps.setString(index++, med.getMedicineCode());
			ps.setString(index++, med.getMedicineName());
			ps.setString(index++, med.getMedicineUnit());
			ps.setString(index++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(med.getMedicineDeadline()));
			ps.setInt(index++, med.getInventoryNum());
			ps.setInt(index++, med.getThresholdNum());
			ps.setInt(index++, med.getMedicineType());
			ps.setInt(index++, med.getManageID());
			
			int id = ps.executeUpdate();
			if (id > 0)
			{
				med.setManageID(id);
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
				if (rs != null)
				{
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	public boolean updateStorage(MedicineManage med)
	{
		String sql = "";
		sql += "update PA_MedicineManage set InventoryNum=? where ManageID = ? ";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(index++, med.getInventoryNum());
			ps.setInt(index++, med.getManageID());
			
			int id = ps.executeUpdate();
			if (id > 0)
			{
				//med.setManageID(id);
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
				if (rs != null)
				{
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	public boolean insertPotionRecord(MedicineManage med,String user)
	{
		String sql = "";
		sql += "insert into PA_MedicineOperRecord (ManageID,RecordName,OperDate,OperNum,RecordType) values(?,?,?,?,?) ";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(index++, med.getManageID());
			ps.setString(index++, user);
			ps.setString(index++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
			ps.setInt(index++, med.getThresholdNum());
			ps.setInt(index++, med.getMedicineType());
			
			int id = ps.executeUpdate();
			if (id > 0)
			{
				med.setManageID(id);
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
				if (rs != null)
				{
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	public boolean deleteMainten(int id)
	{
		String sql = "delete from EM_Mainten where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<Mainten> selectMaintenList(String modality,String device,String startDate,String endDate)
	{
		ArrayList<Mainten> list = new ArrayList<Mainten>();
		
		String sql = "select top 1000 ID,MaintenContent,MaintenPerson,MaintenTime,Modality,Device,isNull(ConfirmPerson,'') ConfirmPerson from EM_Mainten WhERE 1=1 ";
		if (modality != null && !modality.equals("全部") && !modality.equals(""))
		{
			//sql += " and modality=?";
			String[] split = null;
			String modalityWhere = "";
			if(modality.indexOf(",")!=-1) {
				split = modality.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+modality+"') ";
			}
			sql += modalityWhere;
		}
		if (device != null && !device.equals("全部"))
		{
			sql += " and Device='"+device+"'";
		}
		
		if (startDate != null && !startDate.equals(""))
		{
			sql += " and MaintenTime>='"+startDate+"'";
		}
		
		if (endDate != null && !endDate.equals(""))
		{
			sql += " and MaintenTime<='"+endDate+"'";
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int ID = rs.getInt("ID");
				String MaintenContent = rs.getString("MaintenContent");
				String MaintenPerson = rs.getString("MaintenPerson");
				String Modality = rs.getString("Modality");
				String Device = rs.getString("Device");
				String ConfirmPerson = rs.getString("ConfirmPerson");
				Date MaintenTime = rs.getDate("MaintenTime");
				
				Mainten mainten = new Mainten();
				mainten.setMaintenContent(MaintenContent);
				mainten.setMaintenPerson(MaintenPerson);
				mainten.setModality(Modality);
				mainten.setDevice(Device);
				mainten.setConfirmPerson(ConfirmPerson);
				mainten.setMaintenTime(MaintenTime);
				mainten.setID(ID);
				
				list.add(mainten);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public ArrayList<Detection> selectDetectionList(String modality)
	{
		ArrayList<Detection> list = new ArrayList<Detection>();
		
		String modalityWhere = "";
		if (modality != null && !modality.equals("全部") && !modality.equals(""))
		{
			String[] split = null;
			modalityWhere = "";
			if(modality.indexOf(",")!=-1) {
				split = modality.split(",");
				if(split!=null) {
					modalityWhere = " and modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = "where device in (select device from modality where 1=1 "+ modalityWhere + m + "))";
				}
			}else {
				modalityWhere = "where device in (select device from modality where 1=1 and modality in ('"+modality+"')) ";
			}
		}

		String sql = "select ID,DetectionType,Device,DetectionUnit,DetectionDate,DetectionResult from Detection "+modalityWhere+" order by DetectionDate desc";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int iD = rs.getInt("ID");
				int detectionType = rs.getInt("DetectionType");
				String device = rs.getString("Device");
				String detectionUnit = rs.getString("DetectionUnit");
				Date detectionDate = rs.getDate("DetectionDate");
				String detectionResult = rs.getString("DetectionResult");

				Detection detec = new Detection();

				detec.setID(iD);
				detec.setDetectionDate(detectionDate);
				detec.setDetectionResult(detectionResult);
				detec.setDetectionType(detectionType);
				detec.setDevice(device);
				detec.setDetectionUnit(detectionUnit);
				
				list.add(detec);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean addDetection(Detection detection)
	{
		String sql = "";
		sql += "insert into Detection(Device,DetectionType,DetectionUnit,DetectionDate,DetectionResult)values(?,?,?,?,?)";
		
		PreparedStatement ps = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, detection.getDevice());
			ps.setInt(index++, detection.getDetectionType());
			ps.setString(index++, detection.getDetectionUnit());
			ps.setDate(index++, detection.getDetectionDate());
			ps.setString(index++, detection.getDetectionResult());

			return ps.executeUpdate() > 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
		finally
		{
			try {
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<Nitrogen> selectNitrogenList(String Modality)
	{
		ArrayList<Nitrogen> list = new ArrayList<Nitrogen>();
		
		String modalityWhere = "";
		if (Modality != null && !Modality.equals("全部") && !Modality.equals(""))
		{
			String[] split = null;
			modalityWhere = "";
			if(Modality.indexOf(",")!=-1) {
				split = Modality.split(",");
				if(split!=null) {
					modalityWhere = " and modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = "where device in (select device from modality where 1=1 "+ modalityWhere + m + "))";
				}
			}else {
				modalityWhere = "where device in (select device from modality where 1=1 and modality in ('"+Modality+"')) ";
			}
		}
		
		String sql = "select ID,Modality,Device,amount,RecordDate from Nitrogen "+modalityWhere+" order by RecordDate desc";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int iD = rs.getInt("ID");
				String modality = rs.getString("Modality");
				String device = rs.getString("Device");
				String amount = rs.getString("amount");
				Date recordDate = rs.getDate("RecordDate");
				
				Nitrogen nitrogen = new Nitrogen();
				nitrogen.setAmount(amount);
				nitrogen.setDevice(device);
				nitrogen.setID(iD);
				nitrogen.setRecordDate(recordDate);
				nitrogen.setModality(modality);
				
				list.add(nitrogen);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean addNitrogen(Nitrogen nitrogen)
	{
		String sql = "";
		sql += "insert into Nitrogen (Modality,Device,amount,RecordDate)values(?,?,?,?)";
		
		PreparedStatement ps = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, nitrogen.getModality());
			ps.setString(index++, nitrogen.getDevice());
			ps.setString(index++, nitrogen.getAmount());
			ps.setDate(index++, nitrogen.getRecordDate());

			return ps.executeUpdate() > 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
		finally
		{
			try {
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<Dose> selectDoseList(String DeptName)
	{
		ArrayList<Dose> list = new ArrayList<Dose>();
		
		String sql = "select ID,PersonName,Dose,CheckTime,isnull(DeptName,'') as DeptName from EM_Dose";
		if(!DeptName.equals("All") && !DeptName.equals("ALL")) {
			sql += " where 1=1 and deptName = '"+DeptName+"';";
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("ID");
				String personName = rs.getString("PersonName");
				String dose = rs.getString("Dose");
				String deptName = rs.getString("DeptName");
				Date checkTime = rs.getDate("CheckTime");
				
				Dose ds = new Dose();
				ds.setId(id);
				ds.setCheckTime(checkTime);
				ds.setDose(dose);
				ds.setPersonName(personName);
				ds.setDeptName(deptName);
				
				list.add(ds);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean deleteDose(int id)
	{
		String sql = "delete from EM_Dose where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean addDose(Dose dose)
	{
		String sql = "";
		sql += "insert into EM_Dose (PersonName,Dose,CheckTime,DeptName)output inserted.id values (?,?,?,?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, dose.getPersonName());
			ps.setString(index++, dose.getDose());
			ps.setDate(index++, dose.getCheckTime());
			ps.setString(index++, dose.getDeptName());
			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("id");
				dose.setId(id);

				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
		finally
		{
			try
			{

				if(ps != null)
				{
					ps.close();
				}

				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<ShiftDoctor> selectShiftDoctorList(String person, String start, String end,String modality)
	{
		ArrayList<ShiftDoctor> list = new ArrayList<ShiftDoctor>();
		
		String sql = "";
		sql += "SELECT TOP 1000 HandoverID ,Type ,HandoverUser ,isnull(Remarks,'') as Remarks ,HandoverDatetime ,UserType ";
		sql += ",isnull(DeviceDescribe,'') as  DeviceDescribe,isnull(DeptDesctibe,'') as DeptDesctibe FROM Handover WHERE UserType=0";
		
		if (person != null && person.length() > 0)
		{
			sql += " and HandoverUser='" + person + "'";
		}
		if (start != null && start.length() > 0)
		{
			sql += " and HandoverDatetime>='" + start + "'";
		}
		if (end != null && end.length() > 0)
		{
			sql += " and HandoverDatetime<='" + end + "'";
		}
		sql += " ORDER BY HandoverDatetime DESC";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int handoverID = rs.getInt("HandoverID"); 
				int type = rs.getInt("Type");
				int userType = rs.getInt("UserType"); 
				Timestamp handoverDatetime = rs.getTimestamp("HandoverDatetime"); 
				String deviceDescribe = rs.getString("DeviceDescribe");
				String deptDesctibe = rs.getString("DeptDesctibe");
				String handoverUser = rs.getString("HandoverUser");
				String remarks = rs.getString("Remarks");
				
				ShiftDoctor sd = new ShiftDoctor();
				sd.setHandoverID(handoverID);
				sd.setType(type);
				sd.setUserType(userType);
				sd.setHandoverDatetime(handoverDatetime);
				sd.setDeviceDescribe(deviceDescribe);
				sd.setDeptDesctibe(deptDesctibe);
				sd.setHandoverUser(handoverUser);
				sd.setRemarks(remarks);

				list.add(sd);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public ArrayList<ShiftTech> selectShiftTechList(String dev, String person, String start, String end,String modality)
	{
		ArrayList<ShiftTech> list = new ArrayList<ShiftTech>();
		
		String sql = "";
		
		sql += "select top 1000 HandoverID,ForwardID,FitFlag,HandComfirFlag,isnull(FitComment,'') as FitComment";
		sql += ",HandoverDate,Period,Device,Spare,isnull(EtcSafe,'') as EtcSafe,isnull(HandoverComment,'') as HandoverComment,UserName";
		sql += ",isnull(ErrorFlag,'0') as ErrorFlag,isnull(ErrorComment,'') as ErrorComment ";
		sql += "from DeviceHandover,VSecurity where 1=1 and DeviceHandover.Name = VSecurity.Name";
		
		if (dev != null && dev.length() > 0)
		{
			sql += " and Device='" + dev + "'";
		}else {
//			if (modality != null && !modality.equals("全部") && !modality.equals(""))
			if (modality != null && !modality.equals("全部") && !modality.equals(""))
			{
				String[] split = null;
				String modalityWhere = "";
				if(modality.indexOf(",")!=-1) {
					split = modality.split(",");
					if(split!=null) {
						modalityWhere = " and Modality in (";
						String m = "";
						for(int i=0;i<split.length;i++) {
							if("".equals(m)) {
								m = m + "'"+ split[i] +"'";
							}else {
								m = m+"," + "'"+ split[i] +"'";
							}
						}
						modalityWhere = " and device in (select device from modality where 1=1 "+ modalityWhere + m + "))";
					}
				}else {
					modalityWhere = " and device in (select device from modality where 1=1 and Modality in ('"+modality+"')) ";
				}
				sql += modalityWhere;
			}

		}
		
		if (person != null && person.length() > 0)
		{
			sql += "  and Period = 2 and UserName ='" + person + "'";
		}
		if (!"".equals(start) && start != null && start.length() > 0)
		{
			sql += " and HandoverDate>='" + start + "'";
		}
		if (!"".equals(end) && end != null && end.length() > 0)
		{
			sql += " and HandoverDate<='" + end + "'";
		}
		sql += " ORDER BY HandoverDate DESC";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int handoverID = rs.getInt("HandoverID");
				int forwardID = rs.getInt("ForwardID");
				int fitFlag = rs.getInt("FitFlag");
				int handComfirFlag = rs.getInt("HandComfirFlag");
				int period = rs.getInt("Period");
				int errorFlag = rs.getInt("ErrorFlag");
				
				String fitComment = rs.getString("FitComment");
				String device = rs.getString("Device");
				String spare = rs.getString("Spare");
				String etcSafe = rs.getString("EtcSafe");
				String handoverComment = rs.getString("HandoverComment");
				String name = rs.getString("UserName");
				String errorComment = rs.getString("ErrorComment");
				
				//Date handoverDate = rs.getDate("HandoverDate");
				Timestamp handoverDate = rs.getTimestamp("HandoverDate");
				
				ShiftTech st = new ShiftTech();
				st.setHandoverID(handoverID);
				st.setForwardID(forwardID);
				st.setFitFlag(fitFlag);
				st.setHandComfirFlag(handComfirFlag);
				st.setPeriod(period);
				st.setErrorFlag(errorFlag);
				st.setFitComment(fitComment);
				st.setDevice(device);
				st.setSpare(spare);
				st.setEtcSafe(etcSafe);
				st.setHandoverComment(handoverComment);
				st.setName(name);
				st.setErrorComment(errorComment);
				st.setHandoverDate(handoverDate);
				
				list.add(st);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}


	public ArrayList<Train> selectTrainList(String DeptName)
	{
		ArrayList<Train> list = new ArrayList<Train>();
		
		String sql = "select ID,PersonName,TrainPlace,TrainContent,TrainDate,isnull(DeptName,'') as DeptName from EM_Train";
		if(!DeptName.equals("All") && !DeptName.equals("ALL")) {
			sql += "  where 1=1 and DeptName = '"+DeptName+"';";
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("ID");
				String personName = rs.getString("PersonName");
				String trainPlace = rs.getString("TrainPlace");
				String trainContent = rs.getString("TrainContent");
				Date trainDate = rs.getDate("TrainDate");
				String deptName = rs.getString("DeptName");

				Train train = new Train();
				train.setId(id);
				train.setPersonName(personName);
				train.setTrainContent(trainContent);
				train.setTrainPlace(trainPlace);
				train.setTrainDate(trainDate);
				train.setDeptName(deptName);
				
				list.add(train);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean deleteTrain(int id)
	{
		String sql = "delete from EM_Train where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);
			
			return ps.executeUpdate() > 0;
		}
		catch (Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(ps != null)
				{
					ps.close();
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	public boolean addTrain(Train train)
	{
		String sql = "";
		sql += "insert into EM_Train (PersonName,TrainPlace,TrainContent,TrainDate,DeptName)";
		sql += "output inserted.id values (?,?,?,?,?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, train.getPersonName());
			ps.setString(index++, train.getTrainPlace());
			ps.setString(index++, train.getTrainContent());
			ps.setDate(index++, train.getTrainDate());
			ps.setString(index++, train.getDeptName());

			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("id");
				train.setId(id);
				
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
		finally
		{
			try 
			{
				if(ps != null)
				{
					ps.close();
				}
				if (rs != null)
				{
					rs.close();
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	
	
	public ArrayList<Treat> selectTreatList(String DeptName)
	{
		ArrayList<Treat> list = new ArrayList<Treat>();
		
		String sql = "select ID,PersonName,StartDate,EndDate,isnull(DeptName,'') as DeptName from EM_Rest";
		if(!DeptName.equals("All") && !DeptName.equals("ALL")) {
			sql += "  where 1=1 and DeptName = '"+DeptName+"';";
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("ID");
				String personName = rs.getString("PersonName");
				Date startDate = rs.getDate("StartDate");
				Date endDate = rs.getDate("EndDate");
				String deptName = rs.getString("DeptName");
				
				Treat treat = new Treat();
				treat.setId(id);
				treat.setPersonName(personName);
				treat.setStartDate(startDate);
				treat.setEndDate(endDate);
				treat.setDeptName(deptName);
				
				list.add(treat);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean deleteTreat(int id)
	{
		String sql = "delete from EM_Rest where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean addTreat(Treat treat)
	{
		String sql = "";
		
		sql += "insert into EM_Rest (PersonName,StartDate,EndDate,DeptName";
		sql += ")output inserted.id values (?,?,?,?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, treat.getPersonName());
			ps.setDate(index++, treat.getStartDate());
			ps.setDate(index++, treat.getEndDate());
			ps.setString(index++, treat.getDeptName());

			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("id");
				treat.setId(id);
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
		finally
		{
			try {
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<Vacation> selectVacationList(String DeptName)
	{
		ArrayList<Vacation> list = new ArrayList<Vacation>();
		
		String sql = "select ID,PersonName,StartDate,EndDate,isnull(DeptName,'') as DeptName from EM_Holiday";
		if(!DeptName.equals("All") && !DeptName.equals("ALL")) {
			sql += "  where 1=1 and DeptName = '"+DeptName+"';";
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("ID");
				String personName = rs.getString("PersonName");
				Date startDate = rs.getDate("StartDate");
				Date endDate = rs.getDate("EndDate");
				String deptName = rs.getString("DeptName");
				
				Vacation obj = new Vacation();
				obj.setId(id);
				obj.setPersonName(personName);
				obj.setStartDate(startDate);
				obj.setEndDate(endDate);
				obj.setDeptName(deptName);
				
				list.add(obj);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean addVacation(Vacation vacation)
	{
		String sql = "";
		
		sql += "insert into EM_Holiday (PersonName,StartDate,EndDate,DeptName";
		sql += ")output inserted.id values (?,?,?,?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, vacation.getPersonName());
			ps.setDate(index++, vacation.getStartDate());
			ps.setDate(index++, vacation.getEndDate());
			ps.setString(index++, vacation.getDeptName());
			
			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("id");
				vacation.setId(id);
				return true;
			}

		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean deleteVacation(int id)
	{
		String sql = "delete from EM_Holiday where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<Exam> selectExamList(String DeptName)
	{
		ArrayList<Exam> list = new ArrayList<Exam>();
		
		String sql = "select ID,PersonName,isnull(DeptName,'') as DeptName,Institution,CheckTime,CheckItem,CheckResult from EM_BodyCheck";
		if(!DeptName.equals("All") && !DeptName.equals("ALL")) {
			sql += "  where 1=1 and DeptName = '"+DeptName+"';";
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("ID");
				String personName = rs.getString("PersonName");
				String deptName = rs.getString("DeptName");
				String institution = rs.getString("Institution");
				String checkItem = rs.getString("CheckItem");
				String checkResult = rs.getString("CheckResult");
				Date checkTime = rs.getDate("CheckTime");
				
				Exam exam = new Exam();
				exam.setId(id);
				exam.setPersonName(personName);
				exam.setDeptName(deptName);
				exam.setInstitution(institution);
				exam.setCheckResult(checkResult);
				exam.setCheckItem(checkItem);
				exam.setCheckTime(checkTime);
				
				list.add(exam);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean addExam(Exam exam)
	{
		String sql = "";
		
		sql += "insert into EM_BodyCheck (PersonName,Institution,CheckTime,";
		sql += "CheckItem,CheckResult,DeptName)output inserted.id values (?,?,?,?,?,?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, exam.getPersonName());
			ps.setString(index++, exam.getInstitution());
			ps.setDate(index++, exam.getCheckTime());
			ps.setString(index++, exam.getCheckItem());
			ps.setString(index++, exam.getCheckResult());
			ps.setString(index++, exam.getDeptName());
			
			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("id");
				exam.setId(id);
				return true;
			}

		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean deleteExam(int id)
	{
		String sql = "delete from EM_BodyCheck where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<Person> selectPersonList(String DeptName)
	{
		ArrayList<Person> list = new ArrayList<Person>();
		
		String sql = "";
		sql += "select top 1000 ID,name,sex,isnull(deptName,'') as deptName,birthday,education,isnull(title,'') as title,isnull(duty,'') as duty,isnull(school,'') as school,";
		sql += "isnull(degree,'') as degree,StartJob,RadiationAge,RuzhiDate,isnull(jobCategory,'') as jobCategory from EM_Person where 1=1";
		if(!DeptName.equals("All") && !DeptName.equals("ALL")) {
			sql += " and deptName = '"+DeptName+"';";
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("ID");
				String name = rs.getString("name");
				int sex = rs.getInt("sex");
				String deptName = rs.getString("deptName");
				String education = rs.getString("education");
				String title = rs.getString("title");
				String duty = rs.getString("duty");
				String school = rs.getString("school");
				String degree = rs.getString("degree");
				int radiationAge = rs.getInt("RadiationAge");
				Date birthday = rs.getDate("birthday");
				Date startJob = rs.getDate("StartJob");
				Date ruzhiDate = rs.getDate("RuzhiDate");
				String jobCategory = rs.getString("jobCategory");
				
				Person person = new Person();
				person.setId(id);
				person.setName(name);
				person.setSex(sex);
				person.setDeptName(deptName);
				person.setEducation(education);
				person.setTitle(title);
				person.setDuty(duty);
				person.setSchool(school);
				person.setDegree(degree);
				person.setRadiationAge(radiationAge);
				person.setStartJob(startJob);
				person.setRuzhiDate(ruzhiDate);
				person.setBirthday(birthday);
				person.setJobCategory(jobCategory);
				
				list.add(person);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean addPerson(Person person)
	{
		String sql = "";
		
		sql += "insert into EM_Person (name,sex,education,title,birthday,";
		sql += "duty,school,degree,StartJob,RadiationAge,RuzhiDate,DeptName)";
		sql += " output inserted.id values(?,?,?,?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, person.getName());
			ps.setInt(index++, person.getSex());
			ps.setString(index++, person.getEducation());
			ps.setString(index++, person.getTitle());
			ps.setDate(index++, person.getBirthday());
			ps.setString(index++, person.getDuty());
			ps.setString(index++, person.getSchool());
			ps.setString(index++, person.getDegree());
			ps.setDate(index++, person.getStartJob());
			ps.setInt(index++, person.getRadiationAge());
			ps.setDate(index++, person.getRuzhiDate());
			ps.setString(index++, person.getDeptName());

			rs = ps.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt("id");
				person.setId(id);
				
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean deletePerson(int id)
	{
		String sql = "delete from EM_Person where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public ArrayList<Statis> selectStatisticList(String start, String end, String modality, String device, String dateType)
	{
		ArrayList<Statis> list = new ArrayList<Statis>();
		String startDate = start + " 00:00:00";
		String endDate = end + " 23:59:59";
		
		String sql = "select convert(varchar("+dateType+"),StudyDate,120) as datetime,COUNT(*) as StudyTimes,SUM(Fee) as income  from Study";
		sql += " where StudyDate>='" + startDate + "' and StudyDate<='" + endDate + "'";
		if (modality != null && !modality.equals("") && !modality.equals("全部"))
		{
			String[] split = null;
			String modalityWhere = "";
			if(modality.indexOf(",")!=-1) {
				split = modality.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+modality+"') ";
			}
			sql += modalityWhere;
		}
		/**
		if (modality != null && !modality.equals("") && !modality.equals("全部"))
		{
			sql += " and Modality='" + modality + "'";
		}
		*/
		if (device != null && !device.equals("") && !device.equals("全部"))
		{
			sql += " and Device='" + device + "'";
		}

		sql += " group by convert(varchar("+dateType+"),StudyDate,120) order by convert(varchar("+dateType+"),StudyDate,120)";

		PreparedStatement ps = null;
		ResultSet rs = null;
		SimpleDateFormat sd =new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sd1 =new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sd2 =new SimpleDateFormat("yyyy");
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next())
			{
				String date = rs.getString("datetime");
				int studyTimes = rs.getInt("StudyTimes");
				int income = rs.getInt("income");
				
				Statis st = new Statis();
				try {
					if("10".equals(dateType)){
						
						st.setStudyDate(sd.parse(date));
					}else if("7".equals(dateType)){
						
						st.setStudyDate(sd1.parse(date));
						
					}else if("4".equals(dateType)){
						
						st.setStudyDate(sd2.parse(date));
						
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				st.setTimes(studyTimes);
				st.setIncome(income);
				
				list.add(st);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public ArrayList<Occur> selectOccurList(String start, String end, String modality, String device,String dateType)
	{
		ArrayList<Occur> list = new ArrayList<Occur>();
		String startDate = start + " 00:00:00";
		String endDate = end + " 23:59:59";
		
		String sql = "select CONVERT(varchar("+dateType+"),occur_time,120) as occurTime,";
		sql += "COUNT(*) as reportSum  from break_report where occur_time>='" + startDate + "' and occur_time<'" + endDate + "'"; 
		
		if (modality != null && !modality.equals("") && !modality.equals("全部"))
		{
			String[] split = null;
			String modalityWhere = "";
			if(modality.indexOf(",")!=-1) {
				split = modality.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+modality+"') ";
			}
			sql += modalityWhere;
		}
		/**
		if (modality != null && !modality.equals("") && !modality.equals("全部"))
		{
			sql += " and Modality='" + modality + "'";
		}
		*/
		if (device != null && !device.equals("") && !device.equals("全部"))
		{
			sql += " and Device='" + device + "'";
		}

		sql += " group by convert(varchar("+dateType+"),occur_time,120)"; 
		sql += " order by convert(varchar("+dateType+"),occur_time,120)";
		

		PreparedStatement ps = null;
		ResultSet rs = null;
		SimpleDateFormat sd =new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sd1 =new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sd2 =new SimpleDateFormat("yyyy");
		try 
		{
			ps = getConn().prepareStatement(sql);
			//ps.setTimestamp(1, start);
			//ps.setTimestamp(2, end);
			rs = ps.executeQuery();

			while (rs.next())
			{
				String occur_time = rs.getString("occurTime");
				int reportSum = rs.getInt("reportSum");
				
				Occur obj = new Occur();
				try {
					if("10".equals(dateType)){
						
						obj.setOccurTime(sd.parse(occur_time));
						
					}else if("7".equals(dateType)){
						
						obj.setOccurTime(sd1.parse(occur_time));
						
					}else if("4".equals(dateType)){
						
						obj.setOccurTime(sd2.parse(occur_time));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				obj.setReportSum(reportSum);
				
				list.add(obj);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public ArrayList<Attach> selectAttachList()
	{
		ArrayList<Attach> list = new ArrayList<Attach>();
		
		String sql = "SELECT TOP 1000 ID ,AttachName ,AttachType ,RelativeID ,AttachPath FROM EM_Attach";
		  
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("ID");
				int attachType = rs.getInt("AttachType");
				int relativeId = rs.getInt("RelativeID");
				String attachName = rs.getString("AttachName");
				String attachPath = rs.getString("AttachPath");
				
				Attach obj = new Attach();
				obj.setiD(id);
				obj.setAttachName(attachName);
				obj.setAttachPath(attachPath);
				obj.setRelativeID(relativeId);
				obj.setAttachType(attachType);
				
				list.add(obj);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean addAttach(Attach attach)
	{
		String sql = "";
		
		sql += "insert into EM_Attach (AttachName,AttachType,RelativeID,AttachPath) output inserted.id values (?,?,?,?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, attach.getAttachName());
			ps.setInt(index++, attach.getAttachType());
			ps.setInt(index++, attach.getRelativeID());
			ps.setString(index++, attach.getAttachPath());
			
			rs = ps.executeQuery();
			if (rs.next())
			{
				int newid = rs.getInt("id");
				attach.setiD(newid);
				return true;
			}

		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
				if (rs != null)
				{
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean deleteAttach(int id)
	{
		String sql = "delete from EM_Attach where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean updateRepair(Repair repair)
	{
		String sql = "update break_report set occur_time=?,report_time=?,arrive_time=?,restore_time=?";
		sql += ",phenomena=?,extent=?,report_person=?,repair_content=?,replace=?,cuase=?,result=?,";
		sql += "engineer=?,Confirm=?,ConfirmPerson=? where ID=?";
		
		PreparedStatement ps = null;
		int index = 1;
		try
		{
			int x = 0;
			x += 1;
			ps = getConn().prepareStatement(sql);
			ps.setTimestamp(index++, repair.getOccur_time());
			ps.setTimestamp(index++, repair.getReport_time());
			ps.setTimestamp(index++, repair.getArrive_time());
			ps.setTimestamp(index++, repair.getRestore_time());
			ps.setString(index++, repair.getPhenomena());
			ps.setString(index++, repair.getExtent());
			ps.setString(index++, repair.getReport_person());
			ps.setString(index++, repair.getRepair_content());
			ps.setString(index++, repair.getReplace());
			ps.setString(index++, repair.getCuase());
			ps.setString(index++, repair.getResult());
			ps.setString(index++, repair.getEngineer());
			ps.setString(index++, repair.getConfirm());
			ps.setString(index++, repair.getConfirmPerson());
			ps.setInt(index++, repair.getId());
			
			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		
		return false;
	}
	public boolean updateDeviceStatus(String device, String status)
	{
		String sql = "update Equipment set EquipStatus=? where device=?";
		PreparedStatement ps = null;
		try {
			ps = getConn().prepareStatement(sql);
			ps.setString(1, status);
			ps.setString(2, device);
			ps.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean updateDevice(Device device)
	{
		String sql = "update Equipment set EquipNO=?,EquipName=?,EquipStatus=?,MadeIn=?,FactoryNO=?,Price=?,Unit=?,Princepal=?,"
				+ "ReportCode=?,Tel=?,RepairPerson=?,RepairPersonTel=?,BaoxiuPrice=?,QualityStartTime=?,"
				+ "QualityEndTime=?,BuyTime=?,StartTime=? where Device=?";
		PreparedStatement ps = null;
		int index = 1;
		try {
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, device.getEquNo());
			ps.setString(index++, device.getName());
			ps.setString(index++, device.getStatus());
			ps.setString(index++, device.getMadeIn());
			ps.setString(index++, device.getMakeBy());
			ps.setString(index++, device.getBryPrice());
			ps.setString(index++, device.getUseUnit());
			ps.setString(index++, device.getResponseble());
			ps.setString(index++, device.getReportNo());
			ps.setString(index++, device.getFactoryTel());
			ps.setString(index++, device.getContactPerson());
			ps.setString(index++, device.getContactPhone());
			ps.setString(index++, device.getMantanPrice());
			ps.setDate(index++, device.getMantanStartDate());
			ps.setDate(index++, device.getMantanEndDate());
			ps.setDate(index++, device.getInstallDate());
			ps.setDate(index++, device.getUseStartDate());
			ps.setString(index++, device.getDevice());

			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	
	///////////////////////////////////////////////////
	
	public ArrayList<Vacation> selectXxxList()
	{
		ArrayList<Vacation> list = new ArrayList<Vacation>();
		
		String sql = "select ID,PersonName,StartDate,EndDate from EM_Holiday";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("ID");
				String personName = rs.getString("PersonName");
				Date startDate = rs.getDate("StartDate");
				Date endDate = rs.getDate("EndDate");
				
				Vacation obj = new Vacation();
				obj.setId(id);
				obj.setPersonName(personName);
				obj.setStartDate(startDate);
				obj.setEndDate(endDate);
				
				list.add(obj);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean addXXX(Vacation vacation)
	{
		String sql = "";
		
		sql += "insert into EM_Holiday (PersonName,StartDate,EndDate";
		sql += ")output inserted.id values (?,?,?)";
		
		PreparedStatement ps = null;
		int index = 1;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setString(index++, vacation.getPersonName());
			ps.setDate(index++, vacation.getStartDate());
			ps.setDate(index++, vacation.getEndDate());

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean deleteXXX(int id)
	{
		String sql = "delete from EM_Holiday where ID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
    ////妫�鏌ユ妧甯堝伐浣滈噺缁熻
    public JsonArray selectStudyEngineer(String startDate,String endDate,String modalityLists){
    	String modalityWhere = "";
        if (modalityLists != null && !modalityLists.equals("全部") && !modalityLists.equals(""))
		{
			String[] split = null;
			if(modalityLists.indexOf(",")!=-1) {
				split = modalityLists.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+modalityLists+"') ";
			}
		}
    	
        String sql = "select Recorder,count(*) as Quantity,Modality from Study where StudyDate>='"+startDate+"' and StudyDate<='"+endDate+"' and ISNULL(Recorder,'')<>'' and ReserveInt3 >= 6 "+modalityWhere+" GROUP BY Modality,Recorder;";
        PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String recorder = rs.getString("Recorder");
                String quantity = rs.getString("Quantity");
                String modality = rs.getString("Modality");

                map.put(recorder+modality,quantity);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> recorderList = new ArrayList<>();
        String sql1 = "select Recorder from Study where  StudyDate>='"+startDate+"' and StudyDate<='"+endDate+"' and ISNULL(Recorder,'')<>'' and ReserveInt3 >= 6  "+modalityWhere+" GROUP BY Recorder;";
        try
        {
            ps = getConn().prepareStatement(sql1);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String recorder = rs.getString("Recorder");
                recorderList.add(recorder);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        List<String> modalityList = new ArrayList<>();
        String sql2 = "select Modality from Modality WHERE 1=1 "+modalityWhere+" GROUP BY Modality;";
        try
        {
            ps = getConn().prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String modality = rs.getString("Modality");
                modalityList.add(modality);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        JsonObject jsonObject2 = new JsonObject();
        JsonArray jsonArrayDevic = new JsonArray();
        for(int i=0;i<modalityList.size();i++){
        	jsonArrayDevic.add(modalityList.get(i));
        }
        jsonObject2.add("devic", jsonArrayDevic);
        
        JsonObject jsonObject1 = new JsonObject();
        JsonArray jsonArrayName = new JsonArray();
        for(int i=0;i<recorderList.size();i++){
            jsonArrayName.add(recorderList.get(i));
        }
        jsonObject1.add("name", jsonArrayName);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject1);
        for(int i=0;i<modalityList.size();i++) {
            String modality = modalityList.get(i);
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            for (int j = 0; j < recorderList.size(); j++) {
                String recorder = recorderList.get(j);
                String quantity = map.get(recorder+modality);
                if (quantity != null) {
                    jsonArray1.add(quantity);
                } else {
                    jsonArray1.add("0");
                }
            }
            jsonObject.add(modality, jsonArray1);
            jsonArray.add(jsonObject);
        }
        /**
        Map<String,Object> returnMap = new HashMap<String,Object>();
        returnMap.put("濮撳悕",recorderList);
        for(int i=0;i<modalityList.size();i++){
            String modality = modalityList.get(i);
            List<String> quantityList = new ArrayList<String>();
            for(int j=0;j<recorderList.size();j++){
                String recorder = recorderList.get(j);
                String quantity = map.get(modality+recorder);
                if(quantity!=null){
                    quantityList.add(quantity);
                }else{
                    quantityList.add("0");
                }
            }
            returnMap.put(modality,quantityList);*/
        return  jsonArray;
    }
    //鎶ュ憡鍖荤敓宸ヤ綔閲�
    public JsonArray selectReportEngineer(String startDate,String endDate,String modalityLists){
    	String modalityWhere = "";
    	String modalityWhere1 = "";
        if (modalityLists != null && !modalityLists.equals("全部") && !modalityLists.equals(""))
		{
			String[] split = null;
			if(modalityLists.indexOf(",")!=-1) {
				split = modalityLists.split(",");
				if(split!=null) {
					modalityWhere = " where Modality in (";
					modalityWhere1 = " and st.Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
					modalityWhere1 = modalityWhere1 + m + ")";
				}
			}else {
				modalityWhere = " where Modality in ('"+modalityLists+"') ";
				modalityWhere1 = " and st.Modality in ('"+modalityLists+"') ";
			}
		}
    	
    	String sql = "select vs.UserName as UserName,count(*) as Quantity,st.Modality from Study st left join VSecurity vs on st.Reporter = vs.Name where st.StudyDate>='"+startDate+"' and st.StudyDate<='"+endDate+"' and ISNULL(st.Reporter,'')<>'' and ReserveInt3 >= 7 "+modalityWhere1+"  GROUP BY st.Modality,vs.UserName;";
        PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String recorder = rs.getString("UserName");
                String quantity = rs.getString("Quantity");
                String modality = rs.getString("Modality");

                map.put(recorder+modality,quantity);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> recorderList = new ArrayList<>();
        String sql1 = "select v.UserName as UserName from Study st left join VSecurity v on st.Reporter = v.Name where st.StudyDate>='"+startDate+"' and st.StudyDate<='"+endDate+"' and ISNULL(st.Reporter,'')<>'' and ReserveInt3 >= 7 "+modalityWhere1+" GROUP BY v.UserName;";
        try
        {
            ps = getConn().prepareStatement(sql1);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String recorder = rs.getString("UserName");
                recorderList.add(recorder);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        List<String> modalityList = new ArrayList<>();
        String sql2 = "select Modality from Modality "+modalityWhere+" GROUP BY Modality;";
        try
        {
            ps = getConn().prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String modality = rs.getString("Modality");
                modalityList.add(modality);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        JsonObject jsonObject2 = new JsonObject();
        JsonArray jsonArrayDevic = new JsonArray();
        for(int i=0;i<modalityList.size();i++){
        	jsonArrayDevic.add(modalityList.get(i));
        }
        jsonObject2.add("devic", jsonArrayDevic);
        
        JsonObject jsonObject1 = new JsonObject();
        JsonArray jsonArrayName = new JsonArray();
        for(int i=0;i<recorderList.size();i++){
            jsonArrayName.add(recorderList.get(i));
        }
        jsonObject1.add("name", jsonArrayName);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject1);
        for(int i=0;i<modalityList.size();i++) {
            String modality = modalityList.get(i);
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            for (int j = 0; j < recorderList.size(); j++) {
                String recorder = recorderList.get(j);
                String quantity = map.get(recorder+modality);
                if (quantity != null) {
                    jsonArray1.add(quantity);
                } else {
                    jsonArray1.add("0");
                }
            }
            jsonObject.add(modality, jsonArray1);
            jsonArray.add(jsonObject);
        }
        return  jsonArray;
    }
    //瀹℃牳鍖荤敓宸ヤ綔閲�
    public JsonArray selectAuReportEngineer(String startDate,String endDate,String modalityLists){
    	String modalityWhere = "";
    	String modalityWhere1 = "";
        if (modalityLists != null && !modalityLists.equals("全部") && !modalityLists.equals(""))
		{
			String[] split = null;
			if(modalityLists.indexOf(",")!=-1) {
				split = modalityLists.split(",");
				if(split!=null) {
					modalityWhere = " where Modality in (";
					modalityWhere1 = " and st.Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
					modalityWhere1 = modalityWhere1 + m + ")";
				}
			}else {
				modalityWhere = " where Modality in ('"+modalityLists+"') ";
				modalityWhere1 = " and st.Modality in ('"+modalityLists+"') ";
			}
		}
    	
    	String sql = "select vs.UserName as UserName,count(*) as Quantity,st.Modality from Study st left join VSecurity vs on st.Expert = vs.Name where st.StudyDate>='"+startDate+"' and st.StudyDate<='"+endDate+"' and ISNULL(st.Expert,'')<>'' and ReserveInt3 >= 8 "+modalityWhere1+" GROUP BY st.Modality,vs.UserName;";
        PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String recorder = rs.getString("UserName");
                String quantity = rs.getString("Quantity");
                String modality = rs.getString("Modality");

                map.put(recorder+modality,quantity);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> recorderList = new ArrayList<>();
        String sql1 = "select v.UserName as UserName from Study st left join VSecurity v on st.Expert = v.Name where st.StudyDate>='"+startDate+"' and st.StudyDate<='"+endDate+"'  and ISNULL(st.Expert,'')<>'' and ReserveInt3 >= 8 "+modalityWhere1+" GROUP BY v.UserName;";
        try
        {
            ps = getConn().prepareStatement(sql1);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String recorder = rs.getString("UserName");
                recorderList.add(recorder);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        List<String> modalityList = new ArrayList<>();
        String sql2 = "select Modality from Modality "+modalityWhere+" GROUP BY Modality;";
        try
        {
            ps = getConn().prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String modality = rs.getString("Modality");
                modalityList.add(modality);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        JsonObject jsonObject2 = new JsonObject();
        JsonArray jsonArrayDevic = new JsonArray();
        for(int i=0;i<modalityList.size();i++){
        	jsonArrayDevic.add(modalityList.get(i));
        }
        jsonObject2.add("devic", jsonArrayDevic);
        
        JsonObject jsonObject1 = new JsonObject();
        JsonArray jsonArrayName = new JsonArray();
        for(int i=0;i<recorderList.size();i++){
            jsonArrayName.add(recorderList.get(i));
        }
        jsonObject1.add("name", jsonArrayName);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject1);
        for(int i=0;i<modalityList.size();i++) {
            String modality = modalityList.get(i);
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            for (int j = 0; j < recorderList.size(); j++) {
                String recorder = recorderList.get(j);
                String quantity = map.get(recorder+modality);
                if (quantity != null) {
                    jsonArray1.add(quantity);
                } else {
                    jsonArray1.add("0");
                }
            }
            jsonObject.add(modality, jsonArray1);
            jsonArray.add(jsonObject);
        }
        return  jsonArray;
    }
    //鎶ュ憡鍖荤敓鎶ュ憡璐ㄩ噺缁熻
    public JsonArray ReportDoctorReporQualityNotName(String Grade,String Modality,String DateType,String startDate,String endDate) throws ParseException{    	
		String Modalitysql = "";
		if(Modality!=null && !"".equals(Modality)) {
			Modalitysql = " and s.Modality = '"+Modality+"' ";
		}
		String Gradesql = "";
		if(Grade!=null && !"".equals(Grade)) {
			Gradesql =	" and g.RptGrade = '"+Grade+"' ";
		}
		
		String sql = "select count(*) as Quantity,v.UserName as UserName " + 
    			"from  Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Reporter,'')<>'' and s.StudyID = g.studyid and s.Reporter = v.Name " + Gradesql + Modalitysql + " and s.Reporter <> 'administrator' " +
    			"group by v.UserName";
		System.out.println("鎶ュ憡鍖荤敓鎶ュ憡璐ㄩ噺缁熻="+sql);
    	PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                if(UserName!=null && !"".equals(UserName)) {
                	map.put(UserName,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    	
        String sqlz = "select count(*) as Quantity,v.UserName as UserName " + 
        		" from  Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Reporter,'')<>'' and s.StudyID = g.studyid and s.Reporter = v.Name " + Modalitysql + " and s.Reporter <> 'administrator' " +
    			"group by v.UserName";
        System.out.println("鎶ュ憡鍖荤敓鎶ュ憡璐ㄩ噺缁熻1="+sqlz);
		Map<String,String> map1 = new HashMap<String,String>();
	    try
	    {
	       ps = getConn().prepareStatement(sqlz);
	       rs = ps.executeQuery();
	       while (rs.next())
	       {
		       String UserName = rs.getString("UserName");
		       String Quantity = rs.getString("Quantity");
		       if(UserName!=null && !"".equals(UserName)) {
		           	map1.put(UserName,Quantity);
	           }
	       }
	    }
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
        
        
        JsonArray returnList = QualityStatistics.resultStatisticsNotName(map,map1,Grade);
        return returnList;
    }
    
    public JsonArray ReportDoctorReporQuality(String Grade,String Modality,String DateType,String startDate,String endDate,String name) throws ParseException{
    	ArrayList<String> result = new ArrayList<String>();
    	result = QualityStatistics.DateStatistics(DateType,startDate,endDate);
    	
		String Modalitysql = "";
		if(Modality!=null && !"".equals(Modality)) {
			Modalitysql = " and s.Modality = '"+Modality+"' ";
		}
		String Gradesql = "";
		if(Grade!=null && !"".equals(Grade)) {
			Gradesql =	"and s.StudyID = g.studyid and g.RptGrade = '"+Grade+"' ";
		}
		String Namesql = "";
		if(name!=null && !"".equals(name)) {
			Namesql = "and v.Name = '"+name+"' ";
		}
		
		String sql = "select count(*) as Quantity,  CONVERT(varchar("+DateType+"),StudyDate,120) as Date,v.UserName as UserName " + 
    			"from  Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Reporter,'')<>'' and s.Reporter = v.Name " + Gradesql + Modalitysql + Namesql + " and s.Reporter <> 'administrator' " +
    			"group by CONVERT(varchar("+DateType+"),StudyDate,120),v.UserName";
		System.out.println("鎶ュ憡鍖荤敓鎶ュ憡璐ㄩ噺缁熻="+sql);
    	PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                String Date = rs.getString("Date");
                if(UserName!=null && !"".equals(UserName)) {
                	map.put(UserName+Date,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    	
        String sqlz = "select count(*) as Quantity,  CONVERT(varchar("+DateType+"),StudyDate,120) as Date,v.UserName as UserName " + 
        		" from  Study s,VSecurity v where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Reporter,'')<>'' and s.Reporter = v.Name " + Modalitysql + Namesql + " and s.Reporter <> 'administrator' " +
    			"group by CONVERT(varchar("+DateType+"),StudyDate,120),v.UserName";
			   Map<String,String> map1 = new HashMap<String,String>();
		System.out.println("鎶ュ憡鍖荤敓鎶ュ憡璐ㄩ噺缁熻1="+sqlz);
	    try
	    {
	       ps = getConn().prepareStatement(sqlz);
	       rs = ps.executeQuery();
	       while (rs.next())
	       {
		       String UserName = rs.getString("UserName");
		       String Quantity = rs.getString("Quantity");
		       String Date = rs.getString("Date");
		       if(UserName!=null && !"".equals(UserName)) {
		           	map1.put(UserName+Date,Quantity);
	           }
	       }
	    }
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
        
        sql = "select v.UserName as UserName from Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"'" + 
				"and ISNULL(s.Reporter,'')<>'' and s.Reporter = v.Name " + Gradesql + Namesql + " and s.Reporter <> 'administrator' " +
				"group by v.UserName";
        
    	ps = null;
        rs = null;
        List<String> UserList = new ArrayList<String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                if(UserName!=null && !"".equals(UserName)) {
                	UserList.add(UserName);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        JsonArray returnList = QualityStatistics.resultStatistics(result,UserList,map,map1,Grade);
        return returnList;
    }
    
    //瀹℃牳鍖荤敓鎶ュ憡璐ㄩ噺缁熻
    public JsonArray AuReportDoctorReporQuality(String Grade,String Modality,String DateType,String startDate,String endDate,String name) throws ParseException {
    	ArrayList<String> result = new ArrayList<String>();
    	result = QualityStatistics.DateStatistics(DateType,startDate,endDate);
    	
		String Modalitysql = "";
		if(!"".equals(Modality) && Modality!=null) {
			Modalitysql = " and s.Modality = '"+Modality+"' ";
		}
		String Gradesql = "";
		if(!"".equals(Grade) && Grade!=null) {
			Gradesql =	"and s.StudyID = g.studyid and g.RptGrade = '"+Grade+"' " + Modalitysql;
		}
		String Namesql = "";
		if(name!=null && !"".equals(name)) {
			Namesql = "and v.Name = '"+name+"' ";
		}
		
    	String sql = "select count(*) as Quantity,  CONVERT(varchar("+DateType+"),StudyDate,120) as Date,v.UserName as UserName " + 
    			"from Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Expert,'')<>'' and s.Expert = v.Name " + Gradesql + Namesql + " and s.Expert <> 'administrator' "+
    			"group by CONVERT(varchar("+DateType+"),StudyDate,120),v.UserName";
    	System.out.println("瀹℃牳鍖荤敓鎶ュ憡璐ㄩ噺缁熻="+sql);
    	PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                String Date = rs.getString("Date");
                if(UserName!=null && !"".equals(UserName)) {
                	map.put(UserName+Date,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    	
        String sqlz = "select count(*) as Quantity,  CONVERT(varchar("+DateType+"),StudyDate,120) as Date,v.UserName as UserName " + 
        			" from Study s,VSecurity v where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
        			"and ISNULL(s.Expert,'')<>'' and s.Expert = v.Name " + Modalitysql + Namesql + " and s.Expert <> 'administrator' " +
        			"group by CONVERT(varchar("+DateType+"),StudyDate,120),v.UserName";
       System.out.println("瀹℃牳鍖荤敓鎶ュ憡璐ㄩ噺缁熻="+sqlz);
       Map<String,String> map1 = new HashMap<String,String>();
       try
       {
           ps = getConn().prepareStatement(sqlz);
           rs = ps.executeQuery();
           while (rs.next())
           {
           	String UserName = rs.getString("UserName");
               String Quantity = rs.getString("Quantity");
               String Date = rs.getString("Date");
               if(UserName!=null && !"".equals(UserName)) {
               	map1.put(UserName+Date,Quantity);
               }
           }
       }
       catch (SQLException e) {
           e.printStackTrace();
       }
        
        sql = "select v.UserName as UserName from Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"'" + 
				"and ISNULL(s.Expert,'')<>'' and s.Expert = v.Name " + Gradesql + Namesql + " and s.Expert <> 'administrator' "+
				"group by v.UserName";
    	ps = null;
        rs = null;
        List<String> UserList = new ArrayList<String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                if(UserName!=null && !"".equals(UserName)) {
                	UserList.add(UserName);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        JsonArray returnList = QualityStatistics.resultStatistics(result,UserList,map,map1,Grade);
        return returnList;
    }
    
    public JsonArray AuReportDoctorReporQualityNotName(String Grade,String Modality,String DateType,String startDate,String endDate) throws ParseException {	
		String Modalitysql = "";
		if(!"".equals(Modality) && Modality!=null) {
			Modalitysql = " and s.Modality = '"+Modality+"' ";
		}
		String Gradesql = "";
		if(!"".equals(Grade) && Grade!=null) {
			Gradesql =	"and g.RptGrade = '"+Grade+"' " + Modalitysql;
		}
		
		
    	String sql = "select count(*) as Quantity,v.UserName as UserName " + 
    			"from Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Expert,'')<>'' and s.StudyID = g.studyid and s.Expert = v.Name " + Gradesql + " and s.Expert <> 'administrator' "+
    			"group by v.UserName";
    	System.out.println("瀹℃牳鍖荤敓鎶ュ憡璐ㄩ噺缁熻="+sql);
    	PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                if(UserName!=null && !"".equals(UserName)) {
                	map.put(UserName,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    	
        String sqlz = "select count(*) as Quantity,v.UserName as UserName " + 
        			" from Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
        			"and ISNULL(s.Expert,'')<>'' and s.StudyID = g.studyid and s.Expert = v.Name " + Modalitysql  + " and s.Expert <> 'administrator' " +
        			"group by v.UserName";
       System.out.println("瀹℃牳鍖荤敓鎶ュ憡璐ㄩ噺缁熻="+sqlz);
       Map<String,String> map1 = new HashMap<String,String>();
       try
       {
           ps = getConn().prepareStatement(sqlz);
           rs = ps.executeQuery();
           while (rs.next())
           {
           	String UserName = rs.getString("UserName");
               String Quantity = rs.getString("Quantity");
               if(UserName!=null && !"".equals(UserName)) {
               	map1.put(UserName,Quantity);
               }
           }
       }
       catch (SQLException e) {
           e.printStackTrace();
       }
        
        JsonArray returnList = QualityStatistics.resultStatisticsNotName(map,map1,Grade);
        return returnList;
    }
    
    //妫�鏌ユ妧甯堝浘鍍忚川閲忕粺璁�
    public JsonArray technicianImageQuality(String Grade,String Modality,String DateType,String startDate,String endDate,String name) throws ParseException{
    	ArrayList<String> result = new ArrayList<String>();
    	result = QualityStatistics.DateStatistics(DateType,startDate,endDate);
    	
		String Modalitysql = "";
		if(!"".equals(Modality) && Modality!=null) {
			Modalitysql = " and s.Modality = '"+Modality+"' ";
		}
		String Gradesql = "";
		if(!"".equals(Grade) && Grade!=null) {
			Gradesql =	"and g.ImgGrade = '"+Grade+"' " + Modalitysql;
		}
		String Namesql = "";
		if(name!=null && !"".equals(name)) {
			Namesql = "and v.Name = '"+name+"' ";
		}
		
    	String sql = "select count(*) as Quantity,  CONVERT(varchar("+DateType+"),StudyDate,120) as Date,t.Tempvalue as UserName " + 
    				 "from Grade g with(nolock) ,Study s with(nolock) " + 
    				 "CROSS APPLY [dbo].[Fun_Split](Recorder, ',') t,VSecurity v  where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    				 "and ISNULL(Recorder,'')<>'' and s.StudyID = g.studyid and t.Tempvalue = v.UserName " + Gradesql + Namesql +  
    				 "group by CONVERT(varchar("+DateType+"),StudyDate,120),t.Tempvalue";
    	System.out.println("妫�鏌ユ妧甯堝浘鍍忚川閲忕粺璁�="+sql);
    	PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                String Date = rs.getString("Date");
                if(UserName!=null && !"".equals(UserName)) {
                	map.put(UserName+Date,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    	
        String sqlz = "select count(*) as Quantity,  CONVERT(varchar("+DateType+"),StudyDate,120) as Date,t.Tempvalue as UserName " + 
        		 "from Grade g with(nolock) , Study s with(nolock) " + 
				 "CROSS APPLY [dbo].[Fun_Split](Recorder, ',') t,VSecurity v   where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
				 "and ISNULL(Recorder,'')<>'' and s.StudyID = g.studyid and t.Tempvalue = v.UserName " + Modalitysql + Namesql +  
    			 "group by CONVERT(varchar("+DateType+"),StudyDate,120),t.Tempvalue";
        Map<String,String> map1 = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sqlz);
            rs = ps.executeQuery();
            while (rs.next())
            {
            	String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                String Date = rs.getString("Date");
                if(UserName!=null && !"".equals(UserName)) {
                	map1.put(UserName+Date,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        sql = "select t.Tempvalue as UserName from Grade g with(nolock) ,Study s with(nolock) " + 
        		"CROSS APPLY [dbo].[Fun_Split](Recorder, ',') t,VSecurity v where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
        		"and ISNULL(Recorder,'')<>'' and s.StudyID = g.studyid and t.Tempvalue = v.UserName " + Gradesql + Namesql +  
        		"group by t.Tempvalue";
        
    	ps = null;
        rs = null;
        List<String> UserList = new ArrayList<String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                if(UserName!=null && !"".equals(UserName)) {
                	UserList.add(UserName);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        JsonArray returnList = QualityStatistics.resultStatistics(result,UserList,map,map1,Grade);
        return returnList;
    }
    
    public JsonArray technicianImageQualityNotName(String Grade,String Modality,String DateType,String startDate,String endDate) throws ParseException{
		String Modalitysql = "";
		if(!"".equals(Modality) && Modality!=null) {
			Modalitysql = " and s.Modality = '"+Modality+"' ";
		}
		String Gradesql = "";
		if(!"".equals(Grade) && Grade!=null) {
			Gradesql =	"and g.ImgGrade = '"+Grade+"' " + Modalitysql;
		}
		
    	String sql = "select count(*) as Quantity,t.Tempvalue as UserName " + 
    				 "from Grade g with(nolock) ,Study s with(nolock) " + 
    				 "CROSS APPLY [dbo].[Fun_Split](Recorder, ',') t  where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    				 "and ISNULL(Recorder,'')<>'' " + Gradesql + 
    				 "and s.StudyID = g.studyid "+
    				 "group by t.Tempvalue";
    	System.out.println("妫�鏌ユ妧甯堝浘鍍忚川閲忕粺璁�="+sql);
    	PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                if(UserName!=null && !"".equals(UserName)) {
                	map.put(UserName,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    	
        String sqlz = "select count(*) as Quantity,t.Tempvalue as UserName " + 
        		 "from Grade g with(nolock), Study s with(nolock)  " + 
				 "CROSS APPLY [dbo].[Fun_Split](Recorder, ',') t  where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
				 "and ISNULL(Recorder,'')<>'' " + Modalitysql +
				 "and s.StudyID = g.studyid "+
    			 "group by t.Tempvalue";
        Map<String,String> map1 = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sqlz);
            rs = ps.executeQuery();
            while (rs.next())
            {
            	String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                if(UserName!=null && !"".equals(UserName)) {
                	map1.put(UserName,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        JsonArray returnList = QualityStatistics.resultStatisticsNotName(map,map1,Grade);
        return returnList;
    }
    
    //瀹℃牳鍖荤敓鍥惧儚璐ㄩ噺缁熻
    public JsonArray AuReportDoctorImageQuality(String Grade,String Modality,String DateType,String startDate,String endDate,String name) throws ParseException {
    	ArrayList<String> result = new ArrayList<String>();
    	result = QualityStatistics.DateStatistics(DateType,startDate,endDate);
    	
		String Modalitysql = "";
		if(!"".equals(Modality) && Modality!=null) {
			Modalitysql = " and s.Modality = '"+Modality+"' ";
		}
		String Gradesql = "";
		if(!"".equals(Grade) && Grade!=null) {
			Gradesql =	"and s.StudyID = g.studyid and g.ImgGrade = '"+Grade+"' " + Modalitysql;
		}
		String Namesql = "";
		if(name!=null && !"".equals(name)) {
			Namesql = "and v.Name = '"+name+"' ";
		}
		
    	String sql = "select count(*) as Quantity,  CONVERT(varchar("+DateType+"),StudyDate,120) as Date,v.UserName as UserName " + 
    			"from Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Expert,'')<>'' and s.Expert = v.Name " + Gradesql + Namesql + " and s.Expert <> 'administrator' " +
    			"group by CONVERT(varchar("+DateType+"),StudyDate,120),v.UserName";
    	
    	PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println("瀹℃牳鍖荤敓鍥惧儚璐ㄩ噺缁熻="+sql);
        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                String Date = rs.getString("Date");
                if(UserName!=null && !"".equals(UserName)) {
                	map.put(UserName+Date,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        String sqlz = "select count(*) as Quantity,  CONVERT(varchar("+DateType+"),StudyDate,120) as Date,v.UserName as UserName " + 
    			"from Study s,VSecurity v where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Expert,'')<>'' and s.Expert = v.Name " + Modalitysql + Namesql + " and s.Expert <> 'administrator' " +
    			"group by CONVERT(varchar("+DateType+"),StudyDate,120),v.UserName";
        System.out.println("鍏ㄩ儴瀹℃牳鍖荤敓鍥惧儚璐ㄩ噺缁熻="+sqlz);
        Map<String,String> map1 = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sqlz);
            rs = ps.executeQuery();
            while (rs.next())
            {
            	String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                String Date = rs.getString("Date");
                if(UserName!=null && !"".equals(UserName)) {
                	map1.put(UserName+Date,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    	
        sql = "select v.UserName as UserName from Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"'" + 
				"and ISNULL(s.Expert,'')<>'' and s.Expert = v.Name " + Gradesql + Namesql + " and s.Expert <> 'administrator' " +
				"group by v.UserName";
    	ps = null;
        rs = null;
        List<String> UserList = new ArrayList<String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                if(UserName!=null && !"".equals(UserName)) {
                	UserList.add(UserName);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        JsonArray returnList = QualityStatistics.resultStatistics(result,UserList,map,map1,Grade);
        return returnList;
    }
    
    public JsonArray AuReportDoctorImageQualityNotName(String Grade,String Modality,String DateType,String startDate,String endDate) throws ParseException {
		String Modalitysql = "";
		if(!"".equals(Modality) && Modality!=null) {
			Modalitysql = " and s.Modality = '"+Modality+"' ";
		}
		String Gradesql = "";
		if(!"".equals(Grade) && Grade!=null) {
			Gradesql =	"and g.ImgGrade = '"+Grade+"' " + Modalitysql;
		}
		
    	String sql = "select count(*) as Quantity,v.UserName as UserName " + 
    			"from Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Expert,'')<>'' and s.StudyID = g.studyid and s.Expert = v.Name " + Gradesql + " and s.Expert <> 'administrator' " +
    			"group by v.UserName";
    	
    	PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println("瀹℃牳鍖荤敓鍥惧儚璐ㄩ噺缁熻="+sql);
        Map<String,String> map = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                if(UserName!=null && !"".equals(UserName)) {
                	map.put(UserName,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        String sqlz = "select count(*) as Quantity,v.UserName as UserName " + 
    			"from Study s,VSecurity v,Grade g where s.studydate>='"+startDate+"' and s.StudyDate<='"+endDate+"' " + 
    			"and ISNULL(s.Expert,'')<>'' and s.StudyID = g.studyid and s.Expert = v.Name " + Modalitysql + " and s.Expert <> 'administrator' " +
    			"group by v.UserName";
        System.out.println("鍏ㄩ儴瀹℃牳鍖荤敓鍥惧儚璐ㄩ噺缁熻="+sqlz);
        Map<String,String> map1 = new HashMap<String,String>();
        try
        {
            ps = getConn().prepareStatement(sqlz);
            rs = ps.executeQuery();
            while (rs.next())
            {
            	String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                if(UserName!=null && !"".equals(UserName)) {
                	map1.put(UserName,Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        JsonArray returnList = QualityStatistics.resultStatisticsNotName(map,map1,Grade);
        return returnList;
    }
    
    public JsonArray ReviewDoctorImageNum(String Grade,String Modality,String startDate,String endDate,String flag) throws ParseException {
    	Map<String,String> map1 = new HashMap<String,String>();
    	
    	String Modalitysql = "";
		if(!"".equals(Modality) && Modality!=null) {
			Modalitysql = " and a.Modality = '"+Modality+"' ";
		}
		String Gradesql = "";
		if(!"".equals(Grade) && Grade!=null) {
			Gradesql =	"and "+flag+" = '"+Grade+"' " + Modalitysql;
		}
		
    	String sql = "select c.username as UserName,count(c.username) as Quantity " + 
    			"from study a,grade b,VSecurity c where a.studyid=b.studyid and a.expert=c.name " +
    			"and studydate>='"+startDate+"' and StudyDate<='"+endDate+"' " + Gradesql +
    			"group by c.username";
    	
    	
    	PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println("瀹℃牳鍖荤敓鍥惧儚璐ㄩ噺缁熻="+sql);
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                String Quantity = rs.getString("Quantity");
                if(UserName!=null && !"".equals(UserName)) {
                	map1.put(UserName, Quantity);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        sql = "select distinct c.username as UserName from study a,VSecurity c " + 
				"where a.expert=c.name " + Modalitysql +
				" and studydate>='"+startDate+"' and StudyDate<='"+endDate+"' ";
    	ps = null;
        rs = null;
        List<String> UserList = new ArrayList<String>();
        try
        {
            ps = getConn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String UserName = rs.getString("UserName");
                if(UserName!=null && !"".equals(UserName)) {
                	UserList.add(UserName);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        JsonArray returnList = QualityStatistics.resultReviewDoctor(UserList,map1);
        
        return returnList;
    }
    
    //设备收入统计环比/同比
    public JsonArray DeviceRevenueStatistics(List<String> yearsList,String modality) {
    	if(yearsList.size()>0) {
    		String modalitySql = "";
    		/**
    		if(!"0".equals(modality)) {
    			modalitySql = "and Modality='"+modality+"'";
    		}*/
    		if (modality != null && !modality.equals("0") && !modality.equals(""))
    		{
    			String[] split = null;
    			String modalityWhere = "";
    			if(modality.indexOf(",")!=-1) {
    				split = modality.split(",");
    				if(split!=null) {
    					modalityWhere = " and Modality in (";
    					String m = "";
    					for(int i=0;i<split.length;i++) {
    						if("".equals(m)) {
    							m = m + "'"+ split[i] +"'";
    						}else {
    							m = m+"," + "'"+ split[i] +"'";
    						}
    					}
    					modalityWhere = modalityWhere + m + ")";
    				}
    			}else {
    				modalityWhere = " and Modality in ('"+modality+"') ";
    			}
    			modalitySql += modalityWhere;
    		}

    		
    		
    		JsonArray jsonArray = new JsonArray();
    		JsonArray jsonArray1 = new JsonArray();
    		JsonObject jsonObject1 = new JsonObject();
            jsonArray1.add("1月");
            jsonArray1.add("2月");
            jsonArray1.add("3月");
            jsonArray1.add("4月");
            jsonArray1.add("5月");
            jsonArray1.add("6月");
            jsonArray1.add("7月");
            jsonArray1.add("8月");
            jsonArray1.add("9月");
            jsonArray1.add("10月");
            jsonArray1.add("11月");
            jsonArray1.add("12月");
            jsonObject1.add("time",jsonArray1);
            jsonArray.add(jsonObject1);
            
            Map<String,List<String>> mapList = new HashMap<String,List<String>>();
    		for(int i=0;i<yearsList.size();i++) {
    			String years = yearsList.get(i);
    			String sql = "select distinct " + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-01-01 00:00:00' and '"+years+"-02-01 00:00:00' "+modalitySql+") '1月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-02-01 00:00:00' and '"+years+"-03-01 00:00:00' "+modalitySql+") '2月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-03-01 00:00:00' and '"+years+"-04-01 00:00:00' "+modalitySql+") '3月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-04-01 00:00:00' and '"+years+"-05-01 00:00:00' "+modalitySql+") '4月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-05-01 00:00:00' and '"+years+"-06-01 00:00:00' "+modalitySql+") '5月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-06-01 00:00:00' and '"+years+"-07-01 00:00:00' "+modalitySql+") '6月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-07-01 00:00:00' and '"+years+"-08-01 00:00:00' "+modalitySql+") '7月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-08-01 00:00:00' and '"+years+"-09-01 00:00:00' "+modalitySql+") '8月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-09-01 00:00:00' and '"+years+"-10-01 00:00:00' "+modalitySql+") '9月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-10-01 00:00:00' and '"+years+"-11-01 00:00:00' "+modalitySql+") '10月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-11-01 00:00:00' and '"+years+"-12-01 00:00:00' "+modalitySql+") '11月'," + 
    					"(select SUM(fee) from  Study with (nolock) where StudyDate between '"+years+"-12-01 00:00:00' and '"+years+"-12-31 23:59:59' "+modalitySql+") '12月'" + 
    					"from study";
    	        PreparedStatement ps = null;
    	        ResultSet rs = null;
    	        
    	        JsonObject jsObject = new JsonObject();
    	        try
    	        {	
    	            ps = getConn().prepareStatement(sql);
    	            rs = ps.executeQuery();
    	            while (rs.next())
    	            {
    	            	List<String> stringList = new ArrayList<String>();
    	            	JsonArray StringY = new JsonArray();
    	                String yue = rs.getString("1月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("2月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("3月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("4月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("5月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("6月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("7月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("8月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("9月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("10月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("11月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("12月");
    	                StringY.add(yue);stringList.add(yue);
    	                jsObject.add(years,StringY);
    	                mapList.put(years,stringList);
    	            }
    	        }
    	        catch (SQLException e) {
    	            e.printStackTrace();
    	        }
    	        jsonArray.add(jsObject);
    		}
    		
    		JsonObject jsObjects = new JsonObject();
    		JsonArray jsonArrays = new JsonArray();
    		List<String> aList = mapList.get(yearsList.get(0));
	        List<String> bList = mapList.get(yearsList.get(1));
	        for(int j=0;j<aList.size();j++) {
	        	if(aList.get(j)!=null) {
	        		if(bList.get(j)!=null) {
	        			float a = Float.parseFloat(aList.get(j));
	        			float b = Float.parseFloat(bList.get(j));
	        			float d = (float)(b-a)/a;
	        			System.out.println(d);
	        			jsonArrays.add(d*100);
		        	}
	        	}
	        }
	        jsObjects.add("增长率",jsonArrays);
	        jsonArray.add(jsObjects);
    		return jsonArray;
    	}
    	return null;
    }
    
    //人次/日期环比同比
    public JsonArray DeviceExposureStatistics(List<String> yearsList,String modality) {
    	if(yearsList.size()>0) {
    		String modalitySql = "";
    		/**
    		if(!"0".equals(modality)) {
    			modalitySql = "and Modality='"+modality+"'";
    		}
    		*/
    		if (modality != null && !modality.equals("0") && !modality.equals(""))
    		{
    			String[] split = null;
    			String modalityWhere = "";
    			if(modality.indexOf(",")!=-1) {
    				split = modality.split(",");
    				if(split!=null) {
    					modalityWhere = " and Modality in (";
    					String m = "";
    					for(int i=0;i<split.length;i++) {
    						if("".equals(m)) {
    							m = m + "'"+ split[i] +"'";
    						}else {
    							m = m+"," + "'"+ split[i] +"'";
    						}
    					}
    					modalityWhere = modalityWhere + m + ")";
    				}
    			}else {
    				modalityWhere = " and Modality in ('"+modality+"') ";
    			}
    			modalitySql += modalityWhere;
    		}

    		JsonArray jsonArray = new JsonArray();
    		JsonArray jsonArray1 = new JsonArray();
    		JsonObject jsonObject1 = new JsonObject();
            jsonArray1.add("1月");
            jsonArray1.add("2月");
            jsonArray1.add("3月");
            jsonArray1.add("4月");
            jsonArray1.add("5月");
            jsonArray1.add("6月");
            jsonArray1.add("7月");
            jsonArray1.add("8月");
            jsonArray1.add("9月");
            jsonArray1.add("10月");
            jsonArray1.add("11月");
            jsonArray1.add("12月");
            jsonObject1.add("time",jsonArray1);
            jsonArray.add(jsonObject1);
            
            Map<String,List<String>> mapList = new HashMap<String,List<String>>();
    		for(int i=0;i<yearsList.size();i++) {
    			String years = yearsList.get(i);
    			String sql = "select distinct " + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-01-01 00:00:00' and '"+years+"-02-01 00:00:00' "+modalitySql+") '1月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-02-01 00:00:00' and '"+years+"-03-01 00:00:00' "+modalitySql+") '2月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-03-01 00:00:00' and '"+years+"-04-01 00:00:00' "+modalitySql+") '3月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-04-01 00:00:00' and '"+years+"-05-01 00:00:00' "+modalitySql+") '4月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-05-01 00:00:00' and '"+years+"-06-01 00:00:00' "+modalitySql+") '5月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-06-01 00:00:00' and '"+years+"-07-01 00:00:00' "+modalitySql+") '6月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-07-01 00:00:00' and '"+years+"-08-01 00:00:00' "+modalitySql+") '7月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-08-01 00:00:00' and '"+years+"-09-01 00:00:00' "+modalitySql+") '8月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-09-01 00:00:00' and '"+years+"-10-01 00:00:00' "+modalitySql+") '9月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-10-01 00:00:00' and '"+years+"-11-01 00:00:00' "+modalitySql+") '10月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-11-01 00:00:00' and '"+years+"-12-01 00:00:00' "+modalitySql+") '11月'," + 
    					"(select SUM(convert(int,case when PATINDEX('%[^0-9]%',Exposure) = 0 then Exposure else 0 end)) from  Study with (nolock) where StudyDate between '"+years+"-12-01 00:00:00' and '"+years+"-12-31 23:59:59' "+modalitySql+") '12月'" + 
    					"from study";
    	        PreparedStatement ps = null;
    	        ResultSet rs = null;
    	        
    	        JsonObject jsObject = new JsonObject();
    	        try
    	        {	
    	            ps = getConn().prepareStatement(sql);
    	            rs = ps.executeQuery();
    	            while (rs.next())
    	            {
    	            	List<String> stringList = new ArrayList<String>();
    	            	JsonArray StringY = new JsonArray();
    	                String yue = rs.getString("1月");
    	                StringY.add(yue);
    	                stringList.add(yue);
    	                yue = rs.getString("2月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("3月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("4月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("5月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("6月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("7月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("8月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("9月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("10月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("11月");
    	                StringY.add(yue);stringList.add(yue);
    	                yue = rs.getString("12月");
    	                StringY.add(yue);stringList.add(yue);
    	                jsObject.add(years,StringY);
    	                mapList.put(years,stringList);
    	            }
    	        }
    	        catch (SQLException e) {
    	            e.printStackTrace();
    	        }
    	        jsonArray.add(jsObject);
    		}
    		JsonObject jsObjects = new JsonObject();
    		JsonArray jsonArrays = new JsonArray();
    		List<String> aList = mapList.get(yearsList.get(0));
	        List<String> bList = mapList.get(yearsList.get(1));
	        for(int j=0;j<aList.size();j++) {
	        	if(aList.get(j)!=null) {
	        		if(bList.get(j)!=null) {
	        			int a = Integer.parseInt(aList.get(j));
	        			int b = Integer.parseInt(bList.get(j));
	        			float d = (float)(b-a)/a;
	        			System.out.println(d);
	        			jsonArrays.add(d*100);
		        	}
	        	}
	        }
	        jsObjects.add("增长率",jsonArrays);
	        jsonArray.add(jsObjects);
    		return jsonArray;
    	}
    	return null;
    }
  //璁惧浣跨敤璁板綍琛�
    public ArrayList<Statis> selectExposureStatisticList(String start, String end,String device,String modality)
	{
    	ArrayList<Statis> list = new ArrayList<Statis>();
		String startDate = start + " 00:00:00";
		String endDate = end + " 23:59:59";
		
		String sql = "select count(*) as StudyTimes,SUM(Fee) as income ,SUM(cast(Exposure as int)) as exposureNum ,CONVERT(varchar(10),StudyDate,120) as datetime from Study";
		sql += " where StudyDate>='" + startDate + "' and StudyDate<='" + endDate + "'";
		
		if (device != null && !device.equals("") && !device.equals("全部"))
		{
			sql += " and Device='" + device + "'";
		}
		
		if (modality != null && !modality.equals("全部") && !modality.equals(""))
		{
			//sql += " and modality=?";
			String[] split = null;
			String modalityWhere = "";
			if(modality.indexOf(",")!=-1) {
				split = modality.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+modality+"') ";
			}
			sql += modalityWhere;
		}
		sql += " group by CONVERT(varchar(10),StudyDate,120) order by CONVERT(varchar(10),StudyDate,120)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimpleDateFormat sd =new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next())
			{
				String date = rs.getString("datetime");
				int studyTimes = rs.getInt("StudyTimes");
				int income = rs.getInt("income");
				int exposureNum = rs.getInt("exposureNum");
				
				Statis st = new Statis();
				try {
					
					st.setStudyDate(sd.parse(date));
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				st.setTimes(studyTimes);
				st.setIncome(income);
				st.setExposureNum(exposureNum);
				
				list.add(st);

			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
    public ArrayList<HandOver> selectHandOverList(String start, String end,String device,String modality)
	{
    	ArrayList<HandOver> list = new ArrayList<HandOver>();
		String startDate = start + " 00:00:00";
		String endDate = end + " 23:59:59";
		
		String sql = "select MAX(ISNULL(ErrorFlag,0)) as ErrorFlag,CONVERT(varchar(10),HandoverDate,120) as HandoverDate from DeviceHandover";
		sql += " where HandoverDate>='" + startDate + "' and HandoverDate<='" + endDate + "'";
		
		if (modality != null && !modality.equals("全部") && !modality.equals(""))
		{
			//sql += " and modality=?";
			String[] split = null;
			String modalityWhere = "";
			if(modality.indexOf(",")!=-1) {
				split = modality.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+modality+"') ";
			}
			sql += modalityWhere;
		}
		
		if (device != null && !device.equals("") && !device.equals("全部"))
		{
			sql += " and Device='" + device + "'";
		}
		sql += "group by CONVERT(varchar(10),HandoverDate,120) order by CONVERT(varchar(10),HandoverDate,120)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimpleDateFormat sd =new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next())
			{
				String date = rs.getString("HandoverDate");
				int errorFlag = rs.getInt("ErrorFlag");
				
				HandOver ho = new HandOver();
				try {
					
					ho.setHandoverDate(sd.parse(date));
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				    ho.setErrorFlag(errorFlag);
				    
				list.add(ho);

			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
    public ArrayList<HandOver> selectuserNameList(String start, String end,String device,String modality)
   	{
    	
    	ArrayList<HandOver> list = new ArrayList<HandOver>();
		String startDate = start + " 00:00:00";
		String endDate = end + " 23:59:59";
		
		String sql = "select isnull(v.UserName,'') as UserName,CONVERT(varchar(10),d.HandoverDate,120) as HandoverDate from DeviceHandover d,VSecurity v";
		sql += " where HandoverDate>='" + startDate + "' and HandoverDate<='" + endDate + "'";
		sql += "and d.Name = v.Name";
		
		if (modality != null && !modality.equals("全部") && !modality.equals(""))
		{
			//sql += " and modality=?";
			String[] split = null;
			String modalityWhere = "";
			if(modality.indexOf(",")!=-1) {
				split = modality.split(",");
				if(split!=null) {
					modalityWhere = " and Modality in (";
					String m = "";
					for(int i=0;i<split.length;i++) {
						if("".equals(m)) {
							m = m + "'"+ split[i] +"'";
						}else {
							m = m+"," + "'"+ split[i] +"'";
						}
					}
					modalityWhere = modalityWhere + m + ")";
				}
			}else {
				modalityWhere = " and Modality in ('"+modality+"') ";
			}
			sql += modalityWhere;
		}
		
		if (device != null && !device.equals("") && !device.equals("全部"))
		{
			sql += " and Device='" + device + "'";
		}
		sql += " group by CONVERT(varchar(10),d.HandoverDate,120),v.UserName order by CONVERT(varchar(10),d.HandoverDate,120)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimpleDateFormat sd =new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next())
			{
				String date = rs.getString("HandoverDate");
				String userName = rs.getString("UserName");
				
				HandOver ho = new HandOver();
				try {
					
					ho.setHandoverDate(sd.parse(date));
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				    ho.setUserName(userName);
				    
				list.add(ho);

			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
   	}
    public JsonArray doctorTakesOver(String startDate, String endDate) throws ParseException
	{
    	String start = "";
    	String end = "";
    	if(startDate != null){
    		start = startDate + " 00:00:00";
    	}
    	if(endDate != null){
    		end = endDate + " 23:59:59";
    	}
    	ArrayList<String> result = new ArrayList<String>();
    	result = QualityStatistics.DateStatistics("10",start,end);
		
		Map<String,String> map = new HashMap<String,String>();
		for(int i = 0 ;i < result.size() ; i++){
			String data = result.get(i);
			//  takeoverZ:鎺ョ彮鏃╃彮   takeoverW:鎺ョ彮鏅氱彮   handoverZ:浜ょ彮鏃╃彮   handoverW:浜ょ彮鏅氱彮
			map.put("handoverZ"+data, "");
			map.put("takeoverZ"+data, "");
			map.put("handoverW"+data, "");
			map.put("takeoverW"+data, "");
		}
		
    	String sql = "";
		sql += "SELECT TOP 1000 HandoverDatetime ,Type ,HandoverUser ,Remarks ";
		sql += "FROM Handover WHERE UserType=0 ";
		
		
		if (start != null && start.length() > 0)
		{
			sql += " and HandoverDatetime>='" + start + "'";
		}
		if (end != null && end.length() > 0)
		{
			sql += " and HandoverDatetime<='" + end + "'";
		}
		sql += " ORDER BY HandoverDatetime DESC";
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				String handoverDatetime = rs.getString("HandoverDatetime");
				String type = rs.getString("Type");
				String handoverUser = rs.getString("HandoverUser");
				String remarks = rs.getString("Remarks");
				
				String content = "";
				String flag = "";
				if(!"".equals(handoverUser)){
					content += handoverUser ;
				}
				if(!"".equals(remarks)){
					content += "," + remarks ;
				}
				if("1".equals(type)){
					//鎺ョ彮
					flag += "takeover";
				}else if("0".equals(type)){
					//浜ょ彮
					flag += "handover";
				}
				
				String h = handoverDatetime.substring(11, 13);
				int hour = Integer.valueOf(h);
				if(hour <= 12){
					flag += "Z";
				}else{
					flag += "W";
				}
				flag = flag + handoverDatetime.substring(0, 10);
				String con = map.get(flag);
				con = con + content + ";";
				map.put(flag, con);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		JsonArray returnList = QualityStatistics.resultShiftDoctors(result,map);

		return returnList;
	}
    public JsonArray technicianTakesOver(String startDate, String endDate) throws ParseException
	{
    	String start = "";
    	String end = "";
    	if(startDate != null){
    		start = startDate + " 00:00:00";
    	}
    	if(endDate != null){
    		end = endDate + " 23:59:59";
    	}
	   
    	ArrayList<String> result = new ArrayList<String>();
    	result = QualityStatistics.DateStatistics("10",start,end);
		
		Map<String,String> map = new HashMap<String,String>();
		for(int i = 0 ;i < result.size() ; i++){
			String data = result.get(i);
			//  takeoverZ:鎺ョ彮鏃╃彮   takeoverW:鎺ョ彮鏅氱彮   handoverZ:浜ょ彮鏃╃彮   handoverW:浜ょ彮鏅氱彮
			map.put("handoverZ"+data, "");
			map.put("takeoverZ"+data, "");
			map.put("handoverW"+data, "");
			map.put("takeoverW"+data, "");
		}
		
    	String sql = "";
		sql += "select top 1000  HandoverDate,Device,Period,UserName,isnull(HandoverComment,'') as HandoverComment ,";
		sql += "isnull(ErrorComment,'')as ErrorComment,isnull(FitComment,'') as FitComment from DeviceHandover,VSecurity";
		sql += " where 1=1 and DeviceHandover.Name = VSecurity.Name ";
		
		if (start != null && start.length() > 0)
		{
			sql += " and HandoverDate>='" + start + "'";
		}
		if (end != null && end.length() > 0)
		{
			sql += " and HandoverDate<='" + end + "'";
		}
		sql += " ORDER BY HandoverDate DESC";
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				String period = rs.getString("Period");
				String fitComment = rs.getString("FitComment");
				String device = rs.getString("Device");
				String handoverComment = rs.getString("HandoverComment");
				String name = rs.getString("UserName");
				String errorComment = rs.getString("ErrorComment");
				String handoverDate = rs.getString("HandoverDate");
				
				String content = "";
				String flag = "";
				if("1".equals(period)){
					//鎺ョ彮
					content = device+","+name;
					if(!"".equals(fitComment)){
						content = content + "," + fitComment;
					}
					flag += "takeover";
				}else if("2".equals(period)){
					//浜ょ彮
					content = device+","+name;
					if(!"".equals(handoverComment)){
						content = content + "," + handoverComment;
					}
					if(!"".equals(errorComment)){
						content = content + "," + errorComment;
					}
					flag += "handover";
				}
				
				String h = handoverDate.substring(11, 13);
				int hour = Integer.valueOf(h);
				if(hour <= 12){
					flag += "Z";
				}else{
					flag += "W";
				}
				flag = flag + handoverDate.substring(0, 10);
				String con = map.get(flag);
				con = con + content + ";";
				map.put(flag, con);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		JsonArray returnList = QualityStatistics.resultShiftTechs(result,map);

		return returnList;
	}
    
    public JsonArray loginUserDeptName() {
    	String sql = "SELECT DISTINCT deptName as deptName FROM VSecurity where DeptName != 'ALL' and DeptName != 'All';";
    	JsonArray returnList = new JsonArray();
    	PreparedStatement ps = null;
		ResultSet rs = null;
    	try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				String deptName = rs.getString("deptName");
				returnList.add(deptName);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    	return returnList;
    }
    
    //鏌ヨ鎵�鏈夊尰鐢�
    public ArrayList<VSecurity> getDoctorMap() {
    	String sql = "SELECT DISTINCT Name as name,UserName as userName FROM VSecurity ;";
    	ArrayList<VSecurity> vSecurityList = new ArrayList<VSecurity>();
    	PreparedStatement ps = null;
		ResultSet rs = null;
    	try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				String name = rs.getString("name");
				String userName = rs.getString("userName");
				VSecurity vsecurity = new VSecurity();
				vsecurity.setName(name);
				vsecurity.setUserName(userName);
				vSecurityList.add(vsecurity);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    	return vSecurityList;
    }
    
    public JsonArray UnifiedQuality(String DateType,String startDate,String endDate,String name,String flag) throws ParseException {
    	
    	ArrayList<String> result = new ArrayList<String>();
    	result = QualityStatistics.DateStatistics(DateType,startDate,endDate);
    	
    	String sql = "{call PA_QueryQualityControlSQL(?,?,?,?)}";
    	
    	CallableStatement call = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println("瀹℃牳鍖荤敓鍥惧儚璐ㄩ噺缁熻="+sql);
        Map<String,String> map = new HashMap<String,String>();
        try
        {
        	call = conn.prepareCall(sql);
        	call.setString(1, startDate);
        	call.setString(2, endDate);
        	call.setString(3, DateType);
        	call.setString(4, flag);
        	rs = call.executeQuery();
            while (rs.next())
            {
                String date = rs.getString(1);
                String num = rs.getString(2);
                String totalNum = rs.getString(3);
                String rate = rs.getString(4);
                float rate1 = Float.parseFloat(rate)*100;
                String type = rs.getString(5);
                map.put(date,rate1+"");
            }
            System.out.println(map);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        JsonArray returnList = QualityStatistics.resultCanonical(result,map,name);
        return returnList;
    }
    
    public JsonArray bedsStatistical(String BedNum) throws ParseException {
    	JsonArray returnList = new JsonArray();
    	JsonObject obType = new JsonObject();
    	JsonArray type = new JsonArray();
    	type.add("病理医师数");
    	type.add("病理技术人员数");
    	obType.add("time", type);
    	returnList.add(obType);
    	
    	String sql = "{call PA_QueryQualityControlSQL(?,?,?,?)}";
    	
    	CallableStatement call = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println("瀹℃牳鍖荤敓鍥惧儚璐ㄩ噺缁熻="+sql);
        try
        {
        	call = conn.prepareCall(sql);
        	call.setString(1, "");
        	call.setString(2, "");
        	call.setString(3, BedNum);
        	call.setString(4, "1");
        	rs = call.executeQuery();
        	JsonArray number = new JsonArray();
        	JsonObject ob = new JsonObject();
            while (rs.next())
            {
                String date = rs.getString(1);
                String num = rs.getString(2);
                String totalNum = rs.getString(3);
                String rate = rs.getString(4);
                String subRate = rate.substring(0,rate.length()-2);
                String type1 = rs.getString(5);
                number.add(subRate);
            }
            ob.add("病理百张床位统计", number);
            returnList.add(ob);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        return returnList;
    }
    
	public ArrayList<MedicineManage> selectPotionManageList(String OEMName,String MedicineName,String type)
	{
		ArrayList<MedicineManage> list = new ArrayList<MedicineManage>();
		
		String sql = "select TOP 1000  ManageID,OEMCode,OEMName,MedicineBatch,MedicineCode,MedicineName,"
				+ " MedicineUnit,MedicineDeadline,InventoryNum,ThresholdNum,MedicineType from PA_MedicineManage where 1 = 1 and MedicineType = "+ type;
		if (OEMName != null && !OEMName.equals(""))
		{
			sql += " and OEMName like '%"+ OEMName +"%'";

		}
		if (MedicineName != null && !MedicineName.equals(""))
		{
			sql += " and MedicineName like '%"+ MedicineName +"%'";

		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int manageID = rs.getInt("ManageID");
				String code = rs.getString("OEMCode");
				String name = rs.getString("OEMName");
				String medicineBatch = rs.getString("MedicineBatch");
				String medicineCode = rs.getString("MedicineCode");
				String medName = rs.getString("MedicineName");
				String medUnit = rs.getString("MedicineUnit");
				String medDate = rs.getString("MedicineDeadline");
				int inventoryNum = rs.getInt("InventoryNum");
				int thresholdNum = rs.getInt("ThresholdNum");
				int medType = rs.getInt("MedicineType");
				
				MedicineManage medicine = new MedicineManage();
				medicine.setManageID(manageID);
				medicine.setOEMCode(code);
				medicine.setOEMName(name);
				medicine.setMedicineBatch(medicineBatch);
				medicine.setMedicineCode(medicineCode);
				medicine.setMedicineName(medName);
				medicine.setMedicineUnit(medUnit);
				medicine.setMedicineDeadline((java.util.Date)new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(medDate));
				medicine.setInventoryNum(inventoryNum);
				medicine.setThresholdNum(thresholdNum);
				medicine.setMedicineType(medType);
				
				list.add(medicine);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	public ArrayList<MedicineOperRecord> selectMedicineRecordList(String startDate,String endDate,String type,String manageId)
	{
		ArrayList<MedicineOperRecord> list = new ArrayList<MedicineOperRecord>();
		
		String sql = "select a.ManageID,b.MedicineBatch,b.MedicineCode,b.MedicineName,"
				+ "  a.RecordName,a.OperDate,a.OperNum,a.RecordType from PA_MedicineOperRecord a,PA_MedicineManage b where a.ManageID = b.ManageID ";
		if (startDate != null && !startDate.equals(""))
		{
			sql += "  and OperDate between '"+ startDate  +"' and '"+ endDate  +"'";

		}
		if (type != null && !type.equals(""))
		{
			sql += "and a.RecordType = " + type;

		}
		if (manageId != null && !manageId.equals(""))
		{
			sql += " and a.ManageID = " + manageId;

		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps = getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next())
			{
				int manageID = rs.getInt("ManageID");
				String medicineBatch = rs.getString("MedicineBatch");
				String medicineCode = rs.getString("MedicineCode");
				String medName = rs.getString("MedicineName");
				String recordName = rs.getString("RecordName");
				String operDate = rs.getString("OperDate");
				int operNum = rs.getInt("OperNum");
				int recordType = rs.getInt("RecordType");
				
				MedicineOperRecord mo = new MedicineOperRecord();
				mo.setManageID(manageID);
				mo.setMedicineBatch(medicineBatch);
				mo.setMedicineCode(medicineCode);
				mo.setMedicineName(medName);
				mo.setRecordName(recordName);
				mo.setOperDate((java.util.Date)new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(operDate));
				mo.setOperNum(operNum);
				mo.setRecordType(recordType);
				
				list.add(mo);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs!=null)
				{
					rs.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean deleteOrdinaryPotion(int id)
	{
		String sql = "delete from PA_MedicineManage where ManageID=?";
		
		PreparedStatement ps = null;
		try
		{
			ps = getConn().prepareStatement(sql);
			ps.setInt(1, id);

			return ps.executeUpdate() > 0;
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
