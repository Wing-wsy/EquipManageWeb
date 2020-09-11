package com.equipmgr.annet.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.JsonObject;

import net.sf.json.JSONArray;

import com.equipmgr.annet.commen.QualityStatistics;
import com.equipmgr.annet.entity.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.sql.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Servlet implementation class DeviceServlet
 */
@WebServlet("/Device")
public class DeviceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeviceServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		JsonObject jo = new JsonObject();
		Cacher cacher = Cacher.getCacher();
		DBHelper db = DBHelper.getDBHelper();

		cacher.printLog("doGet");

		do {
			String strQuery = request.getQueryString();
			Map<String, String> queryMap = getQueryStringMap(strQuery);
			String strMethod = queryMap.get("method");
			if (strMethod.equals("login")) {
				String strUserId = request.getParameter("userid");
				String strPassword = request.getParameter("password");
				System.out.println(strUserId);
				System.out.println(strPassword);
				strUserId = strUserId.trim();
				strPassword = strPassword.trim();
				if (strUserId.equals("")) {
					jo.addProperty("flag", Constant.RETURN_STATUS_LOGIN_ERROR);
					jo.addProperty("msg", "用户名不能为空");
					break;
				}
				if (strPassword.equals("")) {
					jo.addProperty("flag", Constant.RETURN_STATUS_LOGIN_ERROR);
					jo.addProperty("msg", "密码不能为空");
					break;
				}
				User user = DBHelper.getDBHelper().selectLoginUser(strUserId, strPassword);
				if (user == null) {
					jo.addProperty("flag", Constant.RETURN_STATUS_LOGIN_ERROR);
					System.out.println("用户名或密码错误!");
					jo.addProperty("msg", "用户名或密码错误!");
					break;
				} else {
					// 将所有权限变成字符串遍历出来，用于权限的时候遍历设备是否在字符串中存在
					List<VPurview> VPurviewList = DBHelper.getDBHelper().selectVPurview(strUserId);
					String Modality = "";
					for (int i = 0; i < VPurviewList.size(); i++) {
						if ("".equals(Modality)) {
							Modality += VPurviewList.get(i).getModality();
						} else {
							Modality = Modality + "," + VPurviewList.get(i).getModality();
						}
					}
					System.out.println(Modality);
					user.setModality(Modality);

					jo.addProperty("flag", Constant.RETURN_STATUS_OK);
					jo.addProperty("msg", "登录成功");
					HttpSession session = request.getSession();
					session.setAttribute("LoginUser", user);
					session.setAttribute("LoginVPurview", VPurviewList);
					break;
				}
			} else {
				HttpSession session = request.getSession();
				User loginUser = (User) session.getAttribute("LoginUser");
				if (loginUser == null) {
					jo.addProperty("flag", Constant.RETURN_STATUS_NO_SESSION);
					jo.addProperty("msg", "未登录");
				} else {
					if (strMethod.equals("all")) {
						JsonArray ja = new JsonArray();

						ArrayList<Device> deviceArrayList = db.selectDevices();
						for (int i = 0; i < deviceArrayList.size(); i++) {
							Device dev = deviceArrayList.get(i);
							if (loginUser.getModality().indexOf(dev.getModality()) != -1
									|| loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								JsonObject o = new JsonObject();
								o.addProperty("modality", dev.getModality());
								o.addProperty("device", dev.getDevice());
								o.addProperty("name", dev.getName());
								o.addProperty("equNo", dev.getEquNo());
								o.addProperty("reportNo", dev.getReportNo());
								o.addProperty("mantanPrice", dev.getMantanPrice());
								o.addProperty("useUnit", dev.getUseUnit());
								o.addProperty("responseble", dev.getResponseble());
								o.addProperty("status", dev.getStatus());
								ja.add(o);
							}
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("deviceList", ja);
					} else if (strMethod.equals("getLoginUser")) {
						// HttpSession s = request.getSession();
						User u = (User) session.getAttribute("LoginUser");
						if (u != null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("loginUser", u.getUserName());
							jo.addProperty("deptName", u.getDeptName());
						}
					} else if (strMethod.equals("logout")) {
						session.removeAttribute("LoginUser");
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "注销成功");
					} else if (strMethod.equals("uploadFile")) {
						String strType = request.getParameter("attachType");
						String strId = request.getParameter("relativeId");

						int nid = 0, nType = 0;
						try {
							nid = Integer.parseInt(strId);
							nType = Integer.parseInt(strType);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数错误");
							break;
						}

						String attachFilePath = cacher.getAttachFilePath() + "/" + nType;
						File file = new File(attachFilePath);
						if (!file.exists() && !file.isDirectory()) {
							file.mkdir();
						}
						attachFilePath += "/" + nid;
						file = new File(attachFilePath);
						if (!file.exists() && !file.isDirectory()) {
							file.mkdir();
						}

						String message = "";
						try {
							// 1、创建一个DiskFileItemFactory工厂
							DiskFileItemFactory factory = new DiskFileItemFactory();
							// 2、创建一个文件上传解析器
							ServletFileUpload upload = new ServletFileUpload(factory);
							// 解决上传文件名的中文乱码
							upload.setHeaderEncoding("UTF-8");
							// 3、判断提交上来的数据是否是上传表单的数据
							if (!ServletFileUpload.isMultipartContent(request)) {
								// 按照传统方式获取数据
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "param error");
								break;
							}
							// 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
							List<FileItem> list = upload.parseRequest(request);
							for (FileItem item : list) {
								// 如果fileitem中封装的是普通输入项的数据
								if (item.isFormField()) {
									String name = item.getFieldName();
									// 解决普通输入项的数据的中文乱码问题
									String value = item.getString("UTF-8");
									// value = new
									// String(value.getBytes("iso8859-1"),"UTF-8");
									System.out.println(name + "=" + value);
								} else {// 如果fileitem中封装的是上传文件
										// 得到上传的文件名称，
									String filename = item.getName();
									if (filename == null || filename.trim().equals("")) {
										cacher.printLog("文件名为空");
										continue;
									}
									cacher.printLog("上传文件:" + filename);
									// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
									// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
									// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
									filename = filename.substring(filename.lastIndexOf("\\") + 1);
									// 获取item中的上传文件的输入流
									InputStream in = item.getInputStream();
									// 创建一个文件输出流
									String strFullPath = attachFilePath + "\\" + filename;
									FileOutputStream out = new FileOutputStream(strFullPath);
									// 创建一个缓冲区
									byte buffer[] = new byte[1024];
									// 判断输入流中的数据是否已经读完的标识
									int len = 0;
									// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
									while ((len = in.read(buffer)) > 0) {
										// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath
										// + "\\" + filename)当中
										out.write(buffer, 0, len);
									}
									// 关闭输入流
									in.close();
									// 关闭输出流
									out.close();
									// 删除处理文件上传时生成的临时文件
									item.delete();
									message = "文件上传成功！";

									Attach attach = new Attach();
									attach.setAttachName(filename);
									attach.setAttachPath(nType + "/" + nid + "/" + filename);
									attach.setAttachType(nType);
									attach.setRelativeID(nid);
									db.addAttach(attach);
								}
							}

							ArrayList<Attach> attachList = db.selectAttachList();

							String strDiv = cacher.getAttachListDiv(nType, nid, attachList);
							request.setAttribute("attachType", nType);
							request.setAttribute("relativeId", nid);
							request.setAttribute("attachListDiv", strDiv);
							response.setContentType("text/html;charset=utf-8");
							response.setCharacterEncoding("utf-8");

							response.sendRedirect("Device?method=attachmentList&type=" + nType + "&id=" + nid);
						} catch (Exception e) {
							message = "文件上传失败！";
							e.printStackTrace();

						}
					}
					// 设备正常停机统计
					else if (strMethod.equals("selectStatisticListTingji")) {
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						String modality = request.getParameter("modality");
						String device = request.getParameter("device");

						if (modality == null || modality.trim().equals("")) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备类型不能为空");
							break;
						}
						if (modality == null || modality.trim().equals("") || modality.trim().equals("全部")) {
							if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								modality = "全部";
							} else {
								modality = loginUser.getModality();
							}
						}

						if (device == null || device.trim().equals("")) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不能为空");
							break;
						}
						if (startDate == null || startDate.trim().equals("")) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "开始日期不能为空");
							break;
						}
						if (endDate == null || endDate.trim().equals("")) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "结束日期不能为空");
							break;
						}
						java.util.Date udStart = cacher.getUtilDateFromStr(startDate);
						java.util.Date udEnd = cacher.getUtilDateFromStr(endDate);
						if (udStart == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "开始日期不能为空");
							break;
						}
						if (udEnd == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "结束日期不能为空");
							break;
						}
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(udStart);
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						udStart = calendar.getTime();

						calendar.setTime(udEnd);
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						udEnd = calendar.getTime();

						Timestamp ttStart = new Timestamp(udStart.getTime());
						Timestamp ttEnd = new Timestamp(udEnd.getTime());

						ArrayList<TimePair> repairList = db.selectStatisticRepairList(modality, device, ttStart, ttEnd);
						Timestamp ttOccur = null, ttRestore = null;
						long breakHour = 0;
						for (TimePair obj : repairList) {
							ttOccur = obj.getStart();
							ttRestore = obj.getEnd();
							if (ttOccur == null || ttOccur.before(ttStart)) {
								ttOccur = ttStart;
							}
							if (ttRestore == null || ttRestore.after(ttEnd)) {
								ttRestore = ttEnd;
							}

							long m = ttRestore.getTime() - ttOccur.getTime(); // 相差毫秒数
							long h = m / 60 / 60 / 1000; // 小时数
							breakHour += h;
						}

						ArrayList<Device> deviceList = db.selectDevices();

						long totalM = ttEnd.getTime() - ttStart.getTime();
						long totalH = totalM / 60 / 60 / 1000;
						int deviceSum = cacher.getDeviceSum(modality, device, deviceList);
						totalH *= deviceSum;

						long workHour = totalH - breakHour;

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.addProperty("workHour", workHour);
						jo.addProperty("breakHour", breakHour);
					}
					// 故障次数统计
					else if (strMethod.equals("selectStatisticList")) {
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						String modality = request.getParameter("modality");
						String device = request.getParameter("device");
						String dateType = request.getParameter("dateType");

						if (modality == null || modality.trim().equals("") || modality.trim().equals("全部")) {
							if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								modality = "全部";
							} else {
								modality = loginUser.getModality();
							}
						}

						if (startDate == null || endDate == null || modality == null || device == null
								|| dateType == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期不能为空");
							break;
						}

						Timestamp tsStart = cacher.getTimestampFromString(startDate);
						Timestamp tsEnd = cacher.getTimestampFromString(endDate);

						ArrayList<Occur> occurList = db.selectOccurList(startDate, endDate, modality, device, dateType);
						ArrayList<Occur> occurList2 = new ArrayList<Occur>();
						ArrayList<Statis> list = db.selectStatisticList(startDate, endDate, modality, device, dateType);
						ArrayList<Statis> list2 = new ArrayList<Statis>();

						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");
						SimpleDateFormat df3 = new SimpleDateFormat("yyyy");
						java.util.Date start = null, end = null;
						try {
							if ("10".equals(dateType)) {

								start = df.parse(startDate);
								end = df.parse(endDate);

							} else if ("7".equals(dateType)) {

								start = df2.parse(startDate);
								end = df2.parse(endDate);
							} else if ("4".equals(dateType)) {
								start = df3.parse(startDate);
								end = df3.parse(endDate);
							}
						} catch (ParseException e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期不能为空");
							break;
						}

						Calendar calendar = Calendar.getInstance();
						for (java.util.Date d = start; !d.after(end);) {

							Statis s = cacher.findStatis(d, list);
							Occur occur = cacher.findOccor(d, occurList);
							if (s == null) {
								Statis n = new Statis();
								n.setIncome(0);
								n.setStudyDate(d);
								n.setTimes(0);
								list2.add(n);
							} else {
								list2.add(s);
							}
							if (occur == null) {
								Occur o = new Occur();
								o.setOccurTime(d);
								o.setReportSum(0);
								occurList2.add(o);
							} else {
								occurList2.add(occur);
							}

							calendar.setTime(d);

							if ("10".equals(dateType)) {
								calendar.add(Calendar.DATE, 1);
							} else if ("7".equals(dateType)) {
								calendar.add(Calendar.MONTH, 1);
							} else if ("4".equals(dateType)) {
								calendar.add(Calendar.YEAR, 1);
							}
							d = calendar.getTime();
						}

						JsonArray ja = new JsonArray();

						SimpleDateFormat df1 = new SimpleDateFormat("MM-dd");
						SimpleDateFormat df4 = new SimpleDateFormat("yyyy-MM");
						SimpleDateFormat df5 = new SimpleDateFormat("yyyy");
						int totalIncome = 0;
						int totalCount = 0;
						int totalOccur = 0;
						for (int i = 0; i < list2.size(); i++) {
							Statis s = list2.get(i);
							JsonObject obj = new JsonObject();
							obj.addProperty("income", s.getIncome());
							obj.addProperty("times", s.getTimes());
							if ("10".equals(dateType)) {

								obj.addProperty("studyDate", df1.format(s.getStudyDate()));
							} else if ("7".equals(dateType)) {

								obj.addProperty("studyDate", df4.format(s.getStudyDate()));
							} else if ("4".equals(dateType)) {

								obj.addProperty("studyDate", df5.format(s.getStudyDate()));
							}

							totalIncome += s.getIncome();
							totalCount += s.getTimes();

							ja.add(obj);
						}

						JsonArray jaOccur = new JsonArray();
						for (int i = 0; i < occurList2.size(); i++) {
							Occur s = occurList2.get(i);
							JsonObject obj = new JsonObject();
							if ("10".equals(dateType)) {

								obj.addProperty("occurTime", df1.format(s.getOccurTime()));
							} else if ("7".equals(dateType)) {

								obj.addProperty("occurTime", df4.format(s.getOccurTime()));
							} else if ("4".equals(dateType)) {

								obj.addProperty("occurTime", df5.format(s.getOccurTime()));
							}

							obj.addProperty("reportSum", s.getReportSum());
							// System.out.println("日期：" + s.getOccurTime() +
							// ",次数:" + s.getReportSum());
							totalOccur += s.getReportSum();
							jaOccur.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
						jo.add("occurList", jaOccur);
						jo.addProperty("totalIncome", totalIncome);
						jo.addProperty("totalOccur", totalOccur);
						jo.addProperty("totalCount", totalCount);
						jo.addProperty("totalDateCount", occurList2.size());
					} else if (strMethod.equals("selectRemindList")) {
						JsonArray ja = new JsonArray();

						ArrayList<Remind> list = db.selectRemindList(loginUser.getModality());
						for (int i = 0; i < list.size(); i++) {
							Remind remind = list.get(i);

							JsonObject obj = new JsonObject();
							obj.addProperty("id", remind.getId());
							obj.addProperty("msg", remind.getMsg());
							obj.addProperty("Device", remind.getDevice());
							obj.addProperty("StartDate",
									remind.getStartDate() == null ? "" : remind.getStartDate().toString());
							obj.addProperty("EndDate",
									remind.getEndDate() == null ? "" : remind.getEndDate().toString());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					} else if (strMethod.equals("addNitrogen")) {
						String modality = request.getParameter("Modality");
						String device = request.getParameter("Device");
						String amount = request.getParameter("amount");

						if (modality == null || modality.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备类型不能为空？");
							break;
						}
						if (device == null || device.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不能为空？");
							break;
						}
						if (amount == null || amount.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "液氮量不能为空？");
							break;
						}

						Date recordDate = new Date(System.currentTimeMillis());

						Nitrogen nitrogen = new Nitrogen();
						nitrogen.setModality(modality);
						nitrogen.setDevice(device);
						nitrogen.setAmount(amount);
						nitrogen.setRecordDate(recordDate);

						if (db.addNitrogen(nitrogen)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "添加失败");
						}
					} else if (strMethod.equals("addDetection")) {
						String device = request.getParameter("Device");
						String detectionUnit = request.getParameter("DetectionUnit");
						String detectionResult = request.getParameter("DetectionResult");
						String detectionDate = request.getParameter("DetectionDate");
						String detectionType = request.getParameter("detectionType");

						if (device == null || device.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备类型不能为空？");
							break;
						}
						if (detectionUnit == null || detectionUnit.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "单位不能为空？");
							break;
						}
						if (detectionResult == null || detectionResult.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "检测结果不能为空？");
							break;
						}
						if (detectionDate == null || detectionDate.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "检测时间不能为空？");
							break;
						}
						if (detectionType == null || detectionType.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "检测类型不能为空？");
							break;
						}

						Date detectionDT = null;
						int nDetectionType = 0;
						try {
							detectionDT = Date.valueOf(detectionDate);
						} catch (Exception e) {
							detectionDT = null;
						}
						try {
							nDetectionType = Integer.parseInt(detectionType);
						} catch (Exception e) {
							nDetectionType = 1;
						}

						Detection detection = new Detection();
						detection.setDevice(device);
						detection.setDetectionUnit(detectionUnit);
						detection.setDetectionResult(detectionResult);
						detection.setDetectionDate(detectionDT);
						detection.setDetectionType(nDetectionType);

						if (db.addDetection(detection)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "添加失败");
						}
					} else if (strMethod.equals("addTreat")) {
						String person = request.getParameter("person");
						String startDate = request.getParameter("start");
						String endDate = request.getParameter("end");
						String deptName = request.getParameter("deptName");

						if (person == null || person.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "姓名不能为空？");
							break;
						}
						if (startDate == null || startDate.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "开始日期不能为空？");
							break;
						}
						if (endDate == null || endDate.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "结束日期不能为空？");
							break;
						}

						Date dtStart = null, dtEnd = null;
						try {
							dtStart = Date.valueOf(startDate);
							dtEnd = Date.valueOf(endDate);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期格式有误");
							break;
						}

						Treat entity = new Treat();
						entity.setPersonName(person);
						entity.setStartDate(dtStart);
						entity.setEndDate(dtEnd);
						entity.setDeptName(deptName);

						if (db.addTreat(entity)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
							jo.addProperty("id", entity.getId());
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "数据库操作失败");
						}
					} else if (strMethod.equals("addVacation")) {
						String person = request.getParameter("person");
						String startDate = request.getParameter("start");
						String endDate = request.getParameter("end");
						String deptName = request.getParameter("deptName");

						if (person == null || person.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "姓名不能为空？");
							break;
						}
						if (startDate == null || startDate.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "开始日期不能为空？");
							break;
						}
						if (endDate == null || endDate.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "结束日期不能为空？");
							break;
						}

						Date dtStart = null, dtEnd = null;
						try {
							dtStart = Date.valueOf(startDate);
							dtEnd = Date.valueOf(endDate);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期格式有误");
							break;
						}

						Vacation entity = new Vacation();
						entity.setPersonName(person);
						entity.setStartDate(dtStart);
						entity.setEndDate(dtEnd);
						entity.setDeptName(deptName);

						if (db.addVacation(entity)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
							jo.addProperty("id", entity.getId());
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "数据库操作失败");
						}
					} else if (strMethod.equals("addRemind")) {
						String Modality = request.getParameter("Modality");
						String Device = request.getParameter("Device");
						String msg = request.getParameter("msg");
						String status = request.getParameter("Status");
						String type = request.getParameter("type");
						if (Modality == null || Modality.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备类型不能为空？");
							break;
						}
						if (Device == null || Device.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不能为空？");
							break;
						}
						if (msg == null || msg.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "提示内容不能为空？");
							break;
						}

						Remind remind = new Remind();
						remind.setModality(Modality);
						remind.setDevice(Device);
						remind.setMsg(msg);
						remind.setStatus(status);
						remind.setType(type);
						if (type.equals("0")) {
							String StartDate = request.getParameter("StartDate");
							String EndDate = request.getParameter("EndDate");
							if (StartDate == null || StartDate.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "开始日期不能为空？");
								break;
							}
							if (EndDate == null || EndDate.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "结束日期不能为空？");
								break;
							}
							remind.setStartDate(Date.valueOf(StartDate));
							remind.setEndDate(Date.valueOf(EndDate));
						} else if (type.equals("1")) {
							String circle = request.getParameter("circle");
							String continueTime = request.getParameter("continueTime");
							String firstRemaind = request.getParameter("firstRemaind");
							if (circle == null || circle.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "提醒周期不能为空？");
								break;
							}
							if (continueTime == null || continueTime.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "持续时间不能为空？");
								break;
							}
							if (firstRemaind == null || firstRemaind.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "首次提醒日期不能为空？");
								break;
							}
							remind.setFirstRemaind(Date.valueOf(firstRemaind));
							remind.setCircle(circle);
							remind.setContinueTime(continueTime);
						} else {
							// 管球预热提醒先不做
						}

						if (db.addRemind(remind)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "添加失败");
						}

					} else if (strMethod.equals("selectNitrogenList")) {
						String modality = "";
						if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
							modality = "全部";
						} else {
							modality = loginUser.getModality();
						}

						JsonArray ja = new JsonArray();
						ArrayList<Nitrogen> nitrogenList = db.selectNitrogenList(modality);
						for (Nitrogen de : nitrogenList) {
							JsonObject obj = new JsonObject();
							obj.addProperty("ID", de.getID());
							obj.addProperty("Modality", de.getModality());
							obj.addProperty("Device", de.getDevice());
							obj.addProperty("amount", de.getAmount());
							obj.addProperty("RecordDate",
									de.getRecordDate() == null ? "" : de.getRecordDate().toString());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					} else if (strMethod.equals("selectDetectionList")) {
						String detectionType = request.getParameter("detectionType");
						int nType = 0;

						try {
							nType = Integer.parseInt(detectionType);
						} catch (Exception e) {
						}

						String modality = "";
						if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
							modality = "全部";
						} else {
							modality = loginUser.getModality();
						}

						JsonArray ja = new JsonArray();
						ArrayList<Detection> detectionList = db.selectDetectionList(modality);
						for (Detection de : detectionList) {
							if (de.getDetectionType() != nType) {
								continue;
							}

							JsonObject obj = new JsonObject();
							obj.addProperty("ID", de.getID());
							obj.addProperty("DetectionType", de.getDetectionType());
							obj.addProperty("Device", de.getDevice());
							obj.addProperty("DetectionUnit", de.getDetectionUnit());
							obj.addProperty("DetectionResult", de.getDetectionResult());
							obj.addProperty("DetectionDate",
									de.getDetectionDate() == null ? "" : de.getDetectionDate().toString());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					} else if (strMethod.equals("selectMaintenList")) {
						String Modality = request.getParameter("Modality");
						String Device = request.getParameter("Device");
						String startTime = request.getParameter("startTime");
						String endTime = request.getParameter("endTime");
						/**
						 * Date startDate = null; Date endDate = null; try {
						 * startDate = Date.valueOf(startTime); } catch
						 * (Exception e) { startDate = null; }
						 * 
						 * try { endDate = Date.valueOf(endTime); } catch
						 * (Exception e) { endDate = null; }
						 */
						if (Modality == null || Modality.trim().equals("") || Modality.trim().equals("全部")) {
							if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								Modality = "全部";
							} else {
								Modality = loginUser.getModality();
							}
							// Modality = "全部";
						}

						JsonArray ja = new JsonArray();
						ArrayList<Mainten> list = db.selectMaintenList(Modality, Device, startTime, endTime);
						for (int i = 0; i < list.size(); i++) {
							Mainten main = list.get(i);
							/**
							 * if (Modality != null && !Modality.equals("全部")) {
							 * if (!main.getModality().equals(Modality)) {
							 * continue; } }
							 * 
							 * if (Device != null && !Device.equals("全部")) { if
							 * (!main.getDevice().equals(Device)) { continue; }
							 * }
							 * 
							 * if (startDate != null) { if
							 * (main.getMaintenTime().before(startDate)) {
							 * continue; } }
							 * 
							 * if (endDate != null) { if
							 * (main.getMaintenTime().after(endDate)) {
							 * continue; } }
							 */
							JsonObject obj = new JsonObject();
							obj.addProperty("id", main.getID());
							obj.addProperty("MaintenContent", main.getMaintenContent());
							obj.addProperty("MaintenPerson", main.getMaintenPerson());
							obj.addProperty("Modality", main.getModality());
							obj.addProperty("Device", main.getDevice());
							obj.addProperty("ConfirmPerson", main.getConfirmPerson());
							obj.addProperty("MaintenTime",
									main.getMaintenTime() == null ? "" : main.getMaintenTime().toString());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}
					// 个人剂量
					else if (strMethod.equals("selectDoseList")) {
						String name = request.getParameter("name");
						String deptName = loginUser.getDeptName();
						JsonArray ja = new JsonArray();

						ArrayList<Dose> list = db.selectDoseList(deptName);
						for (Dose dose : list) {
							if (name != null && !name.equals("") && !dose.getPersonName().equals(name)) {
								continue;
							}

							JsonObject obj = new JsonObject();
							obj.addProperty("id", dose.getId());
							obj.addProperty("personName", dose.getPersonName());
							obj.addProperty("dose", dose.getDose());
							obj.addProperty("deptName", dose.getDeptName());
							obj.addProperty("checkTime",
									dose.getCheckTime() == null ? "" : dose.getCheckTime().toString());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}
					// 技师交接班
					else if (strMethod.equals("selectShiftTechList")) {
						String person = request.getParameter("person");
						String start = request.getParameter("start");
						String end = request.getParameter("end");
						String dev = request.getParameter("device");

						start += " 00:00:00";
						end += " 23:59:59";

						String modality = "";
						if (dev == null) {
							if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								modality = "全部";
							} else {
								modality = loginUser.getModality();
							}
						}

						JsonArray ja = new JsonArray();
						ArrayList<ShiftTech> shiftDoctorList = db.selectShiftTechList(dev, person, start, end,
								modality);

						for (ShiftTech sd : shiftDoctorList) {
							JsonObject obj = new JsonObject();

							obj.addProperty("type", sd.getPeriod() == 1 ? "接班" : "交班");
							obj.addProperty("person", sd.getName());
							obj.addProperty("time", sd.getHandoverDate_string());
							obj.addProperty("device", sd.getDevice());
							obj.addProperty("spare", sd.getSpare());
							obj.addProperty("etcSafe", sd.getEtcSafe());
							obj.addProperty("handoverComment", sd.getHandoverComment());
							obj.addProperty("errorComment", sd.getErrorComment());
							obj.addProperty("fitComment", sd.getFitComment());
							if (sd.getErrorFlag() == 0) {
								obj.addProperty("errorFlag", "正常");
							} else if (sd.getErrorFlag() == 1) {
								obj.addProperty("errorFlag", "可用");
							} else {
								obj.addProperty("errorFlag", "异常");
							}

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}
					// 医生交接班
					else if (strMethod.equals("selectShiftDoctorList")) {
						String person = request.getParameter("person");
						String start = request.getParameter("start");
						String end = request.getParameter("end");

						start += " 00:00:00";
						end += " 23:59:59";

						String modality = "";
						if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
							modality = "全部";
						} else {
							modality = loginUser.getModality();
						}

						JsonArray ja = new JsonArray();
						ArrayList<ShiftDoctor> shiftDoctorList = db.selectShiftDoctorList(person, start, end, modality);

						for (ShiftDoctor sd : shiftDoctorList) {
							JsonObject obj = new JsonObject();

							obj.addProperty("type", sd.getType() == 1 ? "接班" : "交班");
							obj.addProperty("handoverUser", sd.getHandoverUser());
							obj.addProperty("handoverDatetime", sd.getHandoverDatetime_string());
							obj.addProperty("deviceDescribe", sd.getDeviceDescribe());
							obj.addProperty("deptDesctibe", sd.getDeptDesctibe());
							obj.addProperty("remarks", sd.getRemarks());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}

					else if (strMethod.equals("selectTrainList")) {
						JsonArray ja = new JsonArray();
						String deptName = loginUser.getDeptName();
						ArrayList<Train> list = db.selectTrainList(deptName);
						for (Train train : list) {
							JsonObject obj = new JsonObject();
							obj.addProperty("id", train.getId());
							obj.addProperty("person", train.getPersonName());
							obj.addProperty("content", train.getTrainContent());
							obj.addProperty("place", train.getTrainPlace());
							obj.addProperty("deptName", train.getDeptName());
							obj.addProperty("date",
									train.getTrainDate() == null ? "" : train.getTrainDate().toString());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}
					// 人员管理
					else if (strMethod.equals("selectPersonList")) {
						String deptName = loginUser.getDeptName();
						String personName = request.getParameter("person");

						JsonArray ja = new JsonArray();
						ArrayList<Person> objList = db.selectPersonList(deptName);

						for (Person entity : objList) {
							if (personName != null && personName.length() > 0 && !personName.equals(entity.getName())) {
								continue;
							}

							JsonObject obj = new JsonObject();

							obj.addProperty("id", entity.getId());
							obj.addProperty("name", entity.getName());
							obj.addProperty("sex", entity.getSex() == 1 ? "女" : "男");
							obj.addProperty("deptName", entity.getDeptName());
							obj.addProperty("education", entity.getEducation());
							obj.addProperty("title", entity.getTitle());
							obj.addProperty("duty", entity.getDuty());
							obj.addProperty("school", entity.getSchool());
							obj.addProperty("degree", entity.getDegree());
							obj.addProperty("radiationAge", entity.getRadiationAge());
							obj.addProperty("birthday",
									entity.getBirthday() == null ? "" : entity.getBirthday().toString());
							obj.addProperty("startJob",
									entity.getStartJob() == null ? "" : entity.getStartJob().toString());
							obj.addProperty("ruzhiDate",
									entity.getRuzhiDate() == null ? "" : entity.getRuzhiDate().toString());
							obj.addProperty("jobCategory", entity.getJobCategory());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}
					// 体检记录
					else if (strMethod.equals("selectExamList")) {
						String person = request.getParameter("person");
						String deptName = loginUser.getDeptName();
						JsonArray ja = new JsonArray();
						ArrayList<Exam> objList = db.selectExamList(deptName);

						for (Exam entity : objList) {
							if (person != null && person.length() > 0 && !person.equals(entity.getPersonName())) {
								continue;
							}

							JsonObject obj = new JsonObject();

							obj.addProperty("personName", entity.getPersonName());
							obj.addProperty("id", entity.getId());
							obj.addProperty("institution", entity.getInstitution());
							obj.addProperty("checkItem", entity.getCheckItem());
							obj.addProperty("checkResult", entity.getCheckResult());
							obj.addProperty("checkTime",
									entity.getCheckTime() == null ? "" : entity.getCheckTime().toString());
							obj.addProperty("deptName", entity.getDeptName());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}
					// 保健价记录
					else if (strMethod.equals("selectVacationList")) {
						String person = request.getParameter("person");
						String deptName = loginUser.getDeptName();
						JsonArray ja = new JsonArray();
						ArrayList<Vacation> objList = db.selectVacationList(deptName);

						for (Vacation entity : objList) {
							if (person != null && person.length() > 0 && !person.equals(entity.getPersonName())) {
								continue;
							}

							JsonObject obj = new JsonObject();

							obj.addProperty("id", entity.getId());
							obj.addProperty("person", entity.getPersonName());
							obj.addProperty("start", entity.getStartDate().toString());
							obj.addProperty("end", entity.getEndDate().toString());
							obj.addProperty("deptName", entity.getDeptName());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}
					// 疗养记录
					else if (strMethod.equals("selectTreatList")) {
						String person = request.getParameter("person");
						String deptName = loginUser.getDeptName();
						JsonArray ja = new JsonArray();
						ArrayList<Treat> objList = db.selectTreatList(deptName);

						for (Treat entity : objList) {
							if (person != null && person.length() > 0 && !person.equals(entity.getPersonName())) {
								continue;
							}

							JsonObject obj = new JsonObject();

							obj.addProperty("id", entity.getId());
							obj.addProperty("person", entity.getPersonName());
							obj.addProperty("deptName", entity.getDeptName());
							obj.addProperty("start", entity.getStartDate().toString());
							obj.addProperty("end", entity.getEndDate().toString());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					} else if (strMethod.equals("selectRepairList")) {
						String Modality = request.getParameter("Modality");
						String Device = request.getParameter("Device");
						String strStartDate = request.getParameter("startDate");
						String strEndDate = request.getParameter("endDate");

						if (Modality == null || Modality.trim().equals("") || Modality.trim().equals("全部")) {
							if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								Modality = "全部";
							} else {
								Modality = loginUser.getModality();
							}
							// Modality = "全部";
						}
						if (Device == null || Device.trim().equals("")) {
							Device = "全部";
						}
						java.util.Date udStart = null, udEnd = null;
						Timestamp ttStart = null, ttEnd = null;

						udStart = cacher.getUtilDateFromStr(strStartDate);
						udEnd = cacher.getUtilDateFromStr(strEndDate);

						Calendar calendar = Calendar.getInstance();
						if (udStart != null) {
							calendar.setTime(udStart);
							calendar.set(Calendar.HOUR_OF_DAY, 0);
							calendar.set(Calendar.MINUTE, 0);
							calendar.set(Calendar.SECOND, 0);
							udStart = calendar.getTime();
							ttStart = new Timestamp(udStart.getTime());
						}
						if (udEnd != null) {
							calendar.setTime(udEnd);
							calendar.add(Calendar.DAY_OF_MONTH, 1);
							calendar.set(Calendar.HOUR_OF_DAY, 0);
							calendar.set(Calendar.MINUTE, 0);
							calendar.set(Calendar.SECOND, 0);
							udEnd = calendar.getTime();
							ttEnd = new Timestamp(udEnd.getTime());
						}

						JsonArray ja = new JsonArray();

						List<Repair> list = db.selectRepairList(Modality, Device, ttStart, ttEnd);
						for (int i = 0; i < list.size(); i++) {

							Repair pai = list.get(i);

							JsonObject obj = new JsonObject();
							obj.addProperty("arrive_time", pai.getArrive_time_string());
							obj.addProperty("restore_time", pai.getRestore_time_string());
							obj.addProperty("occur_time", pai.getOccur_time_string());
							obj.addProperty("report_time", pai.getReport_time_string());

							obj.addProperty("repair_content", pai.getRepair_content());
							obj.addProperty("replace", pai.getReplace());
							obj.addProperty("cuase", pai.getCuase());
							obj.addProperty("result", pai.getResult());
							obj.addProperty("engineer", pai.getEngineer());
							obj.addProperty("Confirm", pai.getConfirm());
							obj.addProperty("ConfirmPerson", pai.getConfirmPerson());
							obj.addProperty("phenomena", pai.getPhenomena());
							obj.addProperty("extent", pai.getExtent());
							obj.addProperty("report_person", pai.getReport_person());
							obj.addProperty("Modality", pai.getModality());
							obj.addProperty("Device", pai.getDevice());
							obj.addProperty("id", pai.getId());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					} else if (strMethod.equals("deleteVacation")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						// 检查有没有附件
						ArrayList<Attach> attachList1 = db.selectAttachList();
						ArrayList<Attach> attachList = cacher.getAttachList(Constant.ATTACH_TYPE_VACATION, nid,
								attachList1);
						if (attachList.size() > 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "请先删除该记录所属的附件？");
							break;
						}

						if (db.deleteVacation(nid)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
						}
					} else if (strMethod.equals("deleteTreat")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						// 检查有没有附件
						ArrayList<Attach> attachList1 = db.selectAttachList();
						ArrayList<Attach> attachList = cacher.getAttachList(Constant.ATTACH_TYPE_TREAT, nid,
								attachList1);
						if (attachList.size() > 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "请先删除该记录所属的附件？");
							break;
						}

						if (db.deleteTreat(nid)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
						}
					} else if (strMethod.equals("deleteDose")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						// 检查有没有附件
						ArrayList<Attach> attachList1 = db.selectAttachList();
						ArrayList<Attach> attachList = cacher.getAttachList(Constant.ATTACH_TYPE_DOSE, nid,
								attachList1);
						if (attachList.size() > 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "请先删除该记录所属的附件？");
							break;
						}

						if (db.deleteDose(nid)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
						}
					} else if (strMethod.equals("deleteAttach")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						ArrayList<Attach> attachList = db.selectAttachList();
						Attach attach = cacher.getAttachById(nid, attachList);
						if (attach == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "附件不存在");
							break;
						}

						if (db.deleteAttach(nid)) {
							File file = new File(cacher.getAttachFilePath() + "/" + attach.getAttachPath());
							if (file.exists() && file.isFile()) {
								file.delete();
							}

							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
						}
					} else if (strMethod.equals("deletePerson")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						// 检查有没有附件
						ArrayList<Attach> attachList1 = db.selectAttachList();
						ArrayList<Attach> attachList = cacher.getAttachList(Constant.ATTACH_TYPE_PERSON, nid,
								attachList1);
						if (attachList.size() > 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "请先删除该记录所属的附件？");
							break;
						}

						if (db.deletePerson(nid)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
						}
					} else if (strMethod.equals("deleteTrain")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						// 检查有没有附件
						ArrayList<Attach> attachList1 = db.selectAttachList();
						ArrayList<Attach> attachList = cacher.getAttachList(Constant.ATTACH_TYPE_TRAIN, nid,
								attachList1);
						if (attachList.size() > 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "请先删除该记录所属的附件？");
							break;
						}

						if (db.deleteTrain(nid)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
						}
					} else if (strMethod.equals("deleteExam")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						// 检查有没有附件
						ArrayList<Attach> attachList1 = db.selectAttachList();
						ArrayList<Attach> attachList = cacher.getAttachList(Constant.ATTACH_TYPE_BODY, nid,
								attachList1);
						if (attachList.size() > 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "请先删除该记录所属的附件？");
							break;
						}

						if (db.deleteExam(nid)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
							break;
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
							break;
						}
					} else if (strMethod.equals("deleteRemind")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						if (db.deleteRemind(nid)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
							break;
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
							break;
						}
					} else if (strMethod.equals("deleteMainten")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						if (db.deleteMainten(nid)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
							break;
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
							break;
						}
					} else if (strMethod.equals("addPerson")) {
						String name = request.getParameter("name");
						String sex = request.getParameter("sex");
						String education = request.getParameter("education");
						String title = request.getParameter("title");
						String duty = request.getParameter("duty");
						String school = request.getParameter("school");
						String degree = request.getParameter("degree");
						String radiationAge = request.getParameter("radiationAge");
						String birthday = request.getParameter("birthday");
						String startJob = request.getParameter("startJob");
						String ruzhiDate = request.getParameter("ruzhiDate");
						String deptName = request.getParameter("deptName");

						if (name == null || name.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "姓名不能为空？");
							break;
						}
						if (sex == null || sex.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "性别不能为空？");
							break;
						}

						int nSex = 0;
						sex = sex.trim();
						if (sex.equals("男")) {
							nSex = 0;
						} else if (sex.equals("女")) {
							nSex = 1;
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "性别输入有误");
							break;
						}

						radiationAge = radiationAge.trim();
						int nRadiationAge = 0;
						try {
							nRadiationAge = Integer.parseInt(radiationAge);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "辐射工龄必须为数字");
							break;
						}

						Date dtBirthday = null, dtStartJob = null, dtRuzhiDate = null;
						try {
							dtBirthday = Date.valueOf(birthday);
							dtStartJob = Date.valueOf(startJob);
							dtRuzhiDate = Date.valueOf(ruzhiDate);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期格式有误");
							break;
						}

						Person p = new Person();
						p.setName(name);
						p.setSex(nSex);
						p.setEducation(education);
						p.setTitle(title);
						p.setDuty(duty);
						p.setSchool(school);
						p.setDegree(degree);
						p.setRadiationAge(nRadiationAge);
						p.setBirthday(dtBirthday);
						p.setStartJob(dtStartJob);
						p.setRuzhiDate(dtRuzhiDate);
						p.setDeptName(deptName);

						if (db.addPerson(p)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "ok");
							jo.addProperty("id", p.getId());
							break;
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "数据库操作失败");
							break;
						}

					} else if (strMethod.equals("addExam")) {
						String personName = request.getParameter("personName");
						String institution = request.getParameter("institution");
						String checkItem = request.getParameter("checkItem");
						String checkResult = request.getParameter("checkResult");
						String checkTime = request.getParameter("checkTime");
						String deptName = request.getParameter("deptName");

						if (personName == null || personName.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "姓名不能为空？");
							break;
						}
						if (institution == null || institution.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "体检机构不能为空？");
							break;
						}
						if (checkItem == null || checkItem.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "体检项目不能为空？");
							break;
						}
						if (checkResult == null || checkResult.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "体检结果不能为空？");
							break;
						}
						if (checkTime == null || checkTime.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "体检时间不能为空？");
							break;
						}

						Date date = null;
						try {
							date = Date.valueOf(checkTime);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期格式有误");
							break;
						}

						Exam entity = new Exam();
						entity.setPersonName(personName);
						entity.setInstitution(institution);
						entity.setCheckItem(checkItem);
						entity.setCheckResult(checkResult);
						entity.setCheckTime(date);
						entity.setDeptName(deptName);

						if (db.addExam(entity)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "ok");
							jo.addProperty("id", entity.getId());
							break;
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "数据库操作失败");
							break;
						}

					} else if (strMethod.equals("addTrain")) {
						String personName = request.getParameter("person");
						String trainPlace = request.getParameter("place");
						String trainContent = request.getParameter("content");
						String trainDate = request.getParameter("date");
						String deptName = request.getParameter("deptName");

						if (personName == null || personName.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "姓名不能为空？");
							break;
						}
						if (trainPlace == null || trainPlace.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "地点不能为空？");
							break;
						}
						if (trainContent == null || trainContent.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "培训内容不能为空？");
							break;
						}
						if (trainDate == null || trainDate.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期不能为空？");
							break;
						}

						Date date = null;
						try {
							date = Date.valueOf(trainDate);
						} catch (Exception e) {
							date = null;
						}

						Train train = new Train();
						train.setPersonName(personName);
						train.setTrainPlace(trainPlace);
						train.setTrainContent(trainContent);
						train.setTrainDate(date);
						train.setDeptName(deptName);
						if (db.addTrain(train)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
							jo.addProperty("id", train.getId());
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "添加失败");
						}
					} else if (strMethod.equals("addDose")) {
						String personName = request.getParameter("personName");
						String dose = request.getParameter("dose");
						String checkTime = request.getParameter("checkTime");
						String deptName = request.getParameter("deptName");

						if (personName == null || personName.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "姓名不能为空？");
							break;
						}
						if (dose == null || dose.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "剂量不能为空？");
							break;
						}
						if (checkTime == null || checkTime.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期不能为空？");
							break;
						}

						Dose ds = new Dose();
						ds.setPersonName(personName);
						ds.setDose(dose);
						ds.setDeptName(deptName);
						Date checkDate = null;
						try {
							checkDate = Date.valueOf(checkTime);
						} catch (Exception e) {
							checkDate = null;
						}
						ds.setCheckTime(checkDate);

						if (db.addDose(ds)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "添加失败");
						}
					} else if (strMethod.equals("addRepair")) {
						String Modality = request.getParameter("Modality");
						String Device = request.getParameter("Device");
						String occur_time = request.getParameter("occur_time");
						String phenomena = request.getParameter("phenomena");
						String extent = request.getParameter("extent");

						if (Modality == null || Modality.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备类型不能为空？");
							break;
						}
						if (Device == null || Device.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不能为空？");
							break;
						}
						if (occur_time == null || occur_time.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "出现时间不能为空？");
							break;
						}
						if (phenomena == null || phenomena.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "出现现象不能为空？");
							break;
						}
						if (extent == null || extent.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "程度不能为空？");
							break;
						}

						Timestamp tt = cacher.getTimestampFromString(occur_time);
						if (tt == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "时间格式不对");
							break;
						}

						Repair re = new Repair();
						re.setModality(Modality);
						re.setDevice(Device);
						re.setOccur_time(tt);
						re.setPhenomena(phenomena);
						re.setExtent(extent);
						re.setReport_time(new Timestamp(System.currentTimeMillis()));
						re.setReport_person(loginUser.getUserName());

						if (db.addRepair(re)) {
							if (re.getExtent().equals("停机")) {
								ArrayList<Device> deviceList = db.selectDevices();
								Device device = cacher.findDeviceById(re.getDevice(), deviceList);
								if (device != null) {
									device.setStatus(re.getExtent());
									db.updateDeviceStatus(device.getDevice(), device.getStatus());
								}
							}

							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "添加失败");
						}

					} else if (strMethod.equals("getModalityMap")) {
						JsonObject obj = new JsonObject();
						Map<String, ArrayList<String>> modalityMap = cacher.getModalityMap();
						for (Map.Entry<String, ArrayList<String>> entry : modalityMap.entrySet()) {
							if (loginUser.getModality().indexOf(entry.getKey()) != -1
									|| loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								JsonArray array = new JsonArray();
								String modality = entry.getKey();
								ArrayList<String> deviceList = entry.getValue();
								for (String device : deviceList) {
									array.add(device);
								}
								obj.add(modality, array);
							}
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("modalityMap", obj);
					} else if (strMethod.equals("getVSecurityMap")) {
						List<VSecurity> deviceList = db.getDoctorMap();
						JSONArray jsonArray = JSONArray.fromObject(deviceList);
						response.setContentType("text/html;charset=utf-8");
						response.setCharacterEncoding("utf-8");
						response.getWriter().print(jsonArray);
						return;
					} else if (strMethod.equals("addDevice")) {
						String modality = request.getParameter("modality");
						String device = request.getParameter("device");
						String equNo = request.getParameter("equNo");
						String name = request.getParameter("name");
						String status = request.getParameter("status");
						String madeIn = request.getParameter("madeIn");
						String makeBy = request.getParameter("makeBy");
						String bryPrice = request.getParameter("bryPrice");
						String useUnit = request.getParameter("useUnit");
						String responseble = request.getParameter("responseble");
						String installDate = request.getParameter("installDate");
						String useStartDate = request.getParameter("useStartDate");
						String reportNo = request.getParameter("reportNo");
						String factoryTel = request.getParameter("factoryTel");
						String contactPerson = request.getParameter("contactPerson");
						String contactPhone = request.getParameter("contactPhone");
						String mantanStartDate = request.getParameter("mantanStartDate");
						String mantanEndDate = request.getParameter("mantanEndDate");
						String mantanPrice = request.getParameter("mantanPrice");

						if (modality == null || modality.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备类型不能为空？");
							break;
						}
						if (device == null || device.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不能为空？");
							break;
						}

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
						en.setReportNo(reportNo);
						en.setFactoryTel(factoryTel);
						en.setContactPerson(contactPerson);
						en.setContactPhone(contactPhone);
						en.setMantanPrice(mantanPrice);
						en.setModality(modality);

						Date current = new Date(System.currentTimeMillis());

						try {
							Date date = Date.valueOf(mantanEndDate);
							en.setMantanEndDate(date);
						} catch (Exception e) {
							en.setMantanEndDate(current);
						}

						try {
							Date date = Date.valueOf(mantanStartDate);
							en.setMantanStartDate(date);
						} catch (Exception e) {
							en.setMantanStartDate(current);
						}

						try {
							Date date = Date.valueOf(installDate);
							en.setInstallDate(date);
						} catch (Exception e) {
							en.setInstallDate(current);
						}

						try {
							Date date = Date.valueOf(useStartDate);
							en.setUseStartDate(date);
						} catch (Exception e) {
							en.setUseStartDate(current);
						}

						ArrayList<Device> deviceList = db.selectDevices();
						if (cacher.deviceExsit(en.getDevice(), deviceList)) {
							jo.addProperty("flag", "1");
							jo.addProperty("msg", "该设备已经存在");
						} else {
							if (db.addDevice(en)) {
								jo.addProperty("flag", "0");
								jo.addProperty("msg", "操作成功");
							} else {
								jo.addProperty("flag", "2");
								jo.addProperty("msg", "数据库操作失败");
							}
						}
					} else if (strMethod.equals("attachmentList")) {
						String id = request.getParameter("id");
						String type = request.getParameter("type");

						if (id == null || type == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "viewAttachment：参数不能为空");
							break;
						}

						int nid = 0, ntype = 0;
						try {
							nid = Integer.parseInt(id);
							ntype = Integer.parseInt(type);

							ArrayList<Attach> attachList = db.selectAttachList();
							String attachListDiv = cacher.getAttachListDiv(ntype, nid, attachList);

							String backPage = "";
							if (ntype == Constant.ATTACH_TYPE_BODY) {
								backPage = "health_check.html";
							} else if (ntype == Constant.ATTACH_TYPE_PERSON) {
								backPage = "personal_management.html";
							} else if (ntype == Constant.ATTACH_TYPE_DOSE) {
								backPage = "personal_dose.html";
							} else if (ntype == Constant.ATTACH_TYPE_TREAT) {
								backPage = "recuperation_record.html";
							} else if (ntype == Constant.ATTACH_TYPE_TRAIN) {
								backPage = "protection_training.html";
							} else if (ntype == Constant.ATTACH_TYPE_VACATION) {
								backPage = "health_record.html";
							}

							request.setAttribute("backPage", backPage);
							request.setAttribute("attachType", ntype);
							request.setAttribute("relativeId", nid);
							request.setAttribute("attachListDiv", attachListDiv);
							response.setContentType("text/html;charset=utf-8");
							response.setCharacterEncoding("utf-8");
							request.getRequestDispatcher("attachment_list.jsp").forward(request, response);

							return;

						} catch (NumberFormatException e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "viewAttachment：参数必须为数字");
							break;
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "viewAttachment：未知错误");
							break;
						}
					} else if (strMethod.equals("detailDevice")) {
						String device = request.getParameter("device");
						if (device == null || device.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不能为空");
							break;
						}

						ArrayList<Device> deviceList = db.selectDevices();
						Device dev = cacher.getDeviceById(device, deviceList);
						if (dev == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不存在");
							break;
						}

						request.setAttribute("deviceObj", dev);

						response.setContentType("text/html;charset=utf-8");
						response.setCharacterEncoding("utf-8");
						request.getRequestDispatcher("detailDevice.jsp").forward(request, response);

						return;
					} else if (strMethod.equals("detailHealthCheck")) {
						String id = request.getParameter("id");
						int nid = Integer.parseInt(id);
						String deptName = loginUser.getDeptName();
						ArrayList<Exam> examList = db.selectExamList(deptName);

						Exam exam = cacher.getExamById(nid, examList);
						request.setAttribute("exam", exam);

						response.setContentType("text/html;charset=utf-8");
						response.setCharacterEncoding("utf-8");
						request.getRequestDispatcher("detail_health_check.jsp").forward(request, response);

						return;
					} else if (strMethod.equals("addMainten")) {
						String maintenPerson = request.getParameter("maintenPerson");
						String maintenDate = request.getParameter("maintenDate");
						String confirmPerson = request.getParameter("confirmPerson");
						String maintenContent = request.getParameter("maintenContent");
						String modality = request.getParameter("modality");
						String device = request.getParameter("device");

						if (maintenPerson == null || maintenPerson.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "保养技师不能为空");
							break;
						}
						if (confirmPerson == null || confirmPerson.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "确认技师不能为空");
							break;
						}
						if (maintenDate == null || maintenDate.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "保养日期不能为空");
							break;
						}
						if (maintenContent == null || maintenContent.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "保养内容不能为空");
							break;
						}
						if (modality == null || modality.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备类型不能为空");
							break;
						}
						if (device == null || device.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不能为空");
							break;
						}

						Date dtMainten = null;
						try {
							dtMainten = Date.valueOf(maintenDate);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期格式有误");
							break;
						}

						Mainten mainten = new Mainten();
						mainten.setMaintenPerson(maintenPerson);
						mainten.setMaintenContent(maintenContent);
						mainten.setModality(modality);
						mainten.setDevice(device);
						mainten.setConfirmPerson(confirmPerson);
						mainten.setMaintenTime(dtMainten);

						if (db.addMainten(mainten)) {
							jo.addProperty("id", mainten.getID());
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
							break;
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "添加保养记录失败");
							break;
						}

					}
					//添加药剂
					else if (strMethod.equals("addPotion")) {
						String OEMCode = request.getParameter("OEMCode");
						String OEMName = request.getParameter("OEMName");
						String MedicineBatch = request.getParameter("MedicineBatch");
						String MedicineCode = request.getParameter("MedicineCode");
						String MedicineName = request.getParameter("MedicineName");
						String MedicineUnit = request.getParameter("MedicineUnit");
						String MedicineDeadline = request.getParameter("MedicineDeadline");
						String InventoryNum = request.getParameter("InventoryNum");
						String ThresholdNum = request.getParameter("ThresholdNum");
						String MedicineType = request.getParameter("MedicineType");

						if (OEMCode == null || OEMCode.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "生产厂家编码不能为空");
							break;
						}
						if (OEMName == null || OEMName.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "生产厂家名字不能为空");
							break;
						}
						if (MedicineBatch == null || MedicineBatch.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "药剂批次不能为空");
							break;
						}
						if (MedicineName == null || MedicineName.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "药剂名称不能为空");
							break;
						}
						if (MedicineUnit == null || MedicineUnit.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "规格单位不能为空");
							break;
						}
						if (MedicineDeadline == null || MedicineDeadline.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "药剂截止有效期不能为空");
							break;
						}

						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						java.util.Date dtPotion = null;
						try {
							dtPotion = df.parse(MedicineDeadline);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期格式有误");
							break;
						}
					
						MedicineManage med = new MedicineManage();
						med.setOEMCode(OEMCode);
						med.setOEMName(OEMName);
						med.setMedicineBatch(MedicineBatch);
						med.setMedicineCode(MedicineCode);
						med.setMedicineName(MedicineName);
						med.setMedicineDeadline(dtPotion);
						med.setMedicineUnit(MedicineUnit);
						med.setInventoryNum(Integer.parseInt(InventoryNum));
						med.setThresholdNum(Integer.parseInt(ThresholdNum));
						med.setMedicineType(Integer.parseInt(MedicineType));
						
						if(db.selectPotion(med)){
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "该药剂已经存在，请勿重复添加!");
							break;
						}

						if (db.addPotion(med)) {
							jo.addProperty("id", med.getManageID());
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "添加成功");
							break;
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "添加普通药剂记录失败");
							break;
						}

					}
					//修改药剂
					 else if (strMethod.equals("updatePotion")) {
						    String ManageID = request.getParameter("ManageID");
							String OEMCode = request.getParameter("OEMCode");
							String OEMName = request.getParameter("OEMName");
							String MedicineBatch = request.getParameter("MedicineBatch");
							String MedicineCode = request.getParameter("MedicineCode");
							String MedicineName = request.getParameter("MedicineName");
							String MedicineUnit = request.getParameter("MedicineUnit");
							String MedicineDeadline = request.getParameter("MedicineDeadline");
							String InventoryNum = request.getParameter("InventoryNum");
							String ThresholdNum = request.getParameter("ThresholdNum");
							String MedicineType = request.getParameter("MedicineType");

							if (OEMCode == null || OEMCode.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "生产厂家编码不能为空");
								break;
							}
							if (OEMName == null || OEMName.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "生产厂家名字不能为空");
								break;
							}
							if (MedicineBatch == null || MedicineBatch.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "药剂批次不能为空");
								break;
							}
							if (MedicineName == null || MedicineName.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "药剂名称不能为空");
								break;
							}
							if (MedicineUnit == null || MedicineUnit.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "规格单位不能为空");
								break;
							}
							if (MedicineDeadline == null || MedicineDeadline.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "药剂截止有效期不能为空");
								break;
							}

							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							java.util.Date dtPotion = null;
							try {
								dtPotion = df.parse(MedicineDeadline);
							} catch (Exception e) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "日期格式有误");
								break;
							}
						
							MedicineManage med = new MedicineManage();
							med.setOEMCode(OEMCode);
							med.setOEMName(OEMName);
							med.setMedicineBatch(MedicineBatch);
							med.setMedicineCode(MedicineCode);
							med.setMedicineName(MedicineName);
							med.setMedicineDeadline(dtPotion);
							med.setMedicineUnit(MedicineUnit);
							med.setInventoryNum(Integer.parseInt(InventoryNum));
							med.setThresholdNum(Integer.parseInt(ThresholdNum));
							med.setMedicineType(Integer.parseInt(MedicineType));
							med.setManageID(Integer.parseInt(ManageID));
							
							if(db.selectPotion(med)){
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "该药剂已经存在!");
								break;
							}

							if (db.updatePotion(med)) {
								jo.addProperty("id", med.getManageID());
								jo.addProperty("flag", Constant.RETURN_STATUS_OK);
								jo.addProperty("msg", "修改成功");
								break;
							} else {
								jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
								jo.addProperty("msg", "修改普通药剂记录失败");
								break;
							}

						}
					//药剂入库
					 else if (strMethod.equals("updateInPotion")) {
						 	User u = (User) session.getAttribute("LoginUser");
						 	if(u == null){
						 		jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
								jo.addProperty("msg", "登录过期");
								break;
						 	}
						 	
						    String ManageID = request.getParameter("ManageID");
							String num = request.getParameter("num");
							String InventoryNum = request.getParameter("InventoryNum");
							int totalNum = Integer.parseInt(num) + Integer.parseInt(InventoryNum); //总库存
							
							MedicineManage med = new MedicineManage();
							med.setInventoryNum(totalNum);
							med.setThresholdNum(Integer.parseInt(num));
							med.setManageID(Integer.parseInt(ManageID));
							med.setMedicineType(0);  //入库

							if (db.updateStorage(med)) {
								//数据库保存录入记录
								db.insertPotionRecord(med,u.getUserName());
								
								jo.addProperty("id", med.getManageID());
								jo.addProperty("flag", Constant.RETURN_STATUS_OK);
								jo.addProperty("msg", "入库成功");
								break;
							} else {
								jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
								jo.addProperty("msg", "入库失败");
								break;
							}

						}
					//药剂出库
					 else if (strMethod.equals("updateOutPotion")) {
						 	User u = (User) session.getAttribute("LoginUser");
						 	if(u == null){
						 		jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
								jo.addProperty("msg", "登录过期");
								break;
						 	}
						 	
						 	String ManageID = request.getParameter("ManageID");
							String num = request.getParameter("num");
							String InventoryNum = request.getParameter("InventoryNum");
							int totalNum = Integer.parseInt(InventoryNum) - Integer.parseInt(num); //总库存
							
							MedicineManage med = new MedicineManage();
							med.setInventoryNum(totalNum);
							med.setThresholdNum(Integer.parseInt(num));
							med.setManageID(Integer.parseInt(ManageID));
							med.setMedicineType(1);  //出库

							if (db.updateStorage(med)) {
								//数据库保存录入记录
								db.insertPotionRecord(med,u.getUserName());
								
								jo.addProperty("id", med.getManageID());
								jo.addProperty("flag", Constant.RETURN_STATUS_OK);
								jo.addProperty("msg", "出库成功");
								break;
							} else {
								jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
								jo.addProperty("msg", "出库失败");
								break;
							}

						}
					//药剂管理查询记录
					else if (strMethod.equals("selectPotionById")) {
						String manageID = request.getParameter("manageID");
						if (manageID == null || manageID.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "manageID不能为空");
							break;
						}
						MedicineManage med = db.selectPotionById(manageID);

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("ManageID", med.getManageID());
						jo.addProperty("OEMCode", med.getOEMCode());
						jo.addProperty("OEMName", med.getOEMName());
						jo.addProperty("MedicineBatch", med.getMedicineBatch());
						jo.addProperty("MedicineCode", med.getMedicineCode());
						jo.addProperty("MedicineName", med.getMedicineName());
						jo.addProperty("MedicineUnit", med.getMedicineUnit());
						jo.addProperty("InventoryNum", med.getInventoryNum());
						jo.addProperty("ThresholdNum", med.getThresholdNum());
						jo.addProperty("MedicineDeadline", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(med.getMedicineDeadline()));
						jo.addProperty("MedicineType", med.getMedicineType());
						

					}
					else if (strMethod.equals("getContactPerson")) {
						String strDevice = request.getParameter("device");
						if (strDevice == null || strDevice.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "device不能为空");
							break;
						}
						ArrayList<Device> deviceList = db.selectDevices();
						Device device = cacher.getDeviceById(strDevice, deviceList);
						if (device == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "device不存在");
							break;
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("contactPerson", device.getContactPerson());
						jo.addProperty("contactPhone", device.getContactPhone());
					} else if (strMethod.equals("detailRepairRecord")) {
						String id = request.getParameter("id");
						if (id == null || id.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "id不能为空");
							break;
						}
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "id必须为数字");
							break;
						}

						ArrayList<Repair> repairList = db.selectRepairList(null, null, null, null);
						Repair repair = cacher.getRepairById(nid, repairList);
						if (repair == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "记录不存在");
							break;
						}
						request.setAttribute("entityObj", repair);

						response.setContentType("text/html;charset=utf-8");
						response.setCharacterEncoding("utf-8");
						request.getRequestDispatcher("detail_repiar_record.jsp").forward(request, response);

						return;
					} else if (strMethod.equals("updateRepair")) {
						String repair_content = request.getParameter("repair_content");
						String replace = request.getParameter("replace");
						String cuase = request.getParameter("cuase");
						String result = request.getParameter("result");
						String engineer = request.getParameter("engineer");
						String phenomena = request.getParameter("phenomena");
						String extent = request.getParameter("extent");
						String report_person = request.getParameter("report_person");

						String strId = request.getParameter("repairId");
						String str_arrive_time = request.getParameter("arrive_time");
						String str_restore_time = request.getParameter("restore_time");
						String str_occur_time = request.getParameter("occur_time");
						String str_report_time = request.getParameter("report_time");

						if (strId == null || strId.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "");
							break;
						}
						if (str_arrive_time == null || str_arrive_time.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "到达时间不能为空");
							break;
						}
						if (str_occur_time == null || str_occur_time.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "出现时间不能为空");
							break;
						}
						if (str_report_time == null || str_report_time.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "报告时间不能为空");
							break;
						}

						if (repair_content == null || repair_content.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "维修内容不能为空");
							break;
						}
						if (replace == null || replace.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "更换配件不能为空");
							break;
						}
						if (cuase == null || cuase.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "故障原因不能为空");
							break;
						}
						if (result == null || result.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "维修结果不能为空");
							break;
						}
						if (engineer == null || engineer.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "工程师姓名不能为空");
							break;
						}
						if (phenomena == null || phenomena.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "出现不能为空");
							break;
						}
						if (report_person == null || report_person.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "报告人不能为空");
							break;
						}
						if (extent == null || extent.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "严重程度不能为空");
							break;
						}

						Timestamp restore_time = null;
						if (result.equals("已解决")) {
							if (str_restore_time == null || str_restore_time.trim().length() == 0) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "恢复时间不能为空");
								break;
							} else {
								restore_time = cacher.getTimestampFromString(str_restore_time);
								if (restore_time == null) {
									jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
									jo.addProperty("msg", "恢复时间格式错误");
									break;
								}
							}
						}

						int id;
						Timestamp arrive_time = cacher.getTimestampFromString(str_arrive_time);
						Timestamp occur_time = cacher.getTimestampFromString(str_occur_time);
						Timestamp report_time = cacher.getTimestampFromString(str_report_time);

						if (arrive_time == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期格式错误");
							break;
						}
						if (occur_time == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期格式错误");
							break;
						}
						if (report_time == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期格式错误");
							break;
						}

						try {
							id = Integer.parseInt(strId);
						} catch (NumberFormatException e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "id必须为数字");
							break;
						}

						ArrayList<Repair> repairList = db.selectRepairList(null, null, null, null);

						Repair repair = cacher.getRepairById(id, repairList);
						if (repair == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "id不存在");
							break;
						}

						// 是否需要更新设备的状态
						String updateDevice = null;
						if (repair.getExtent().equals("停机")) {
							if (result.equals("已解决")) {
								updateDevice = "正常";
							} else if (result.equals("待观察")) {
								updateDevice = "观察";
							}
						}

						// 提醒
						String remindMsg = request.getParameter("remindMsg");
						String remindStart = request.getParameter("remindStart");
						String remindEnd = request.getParameter("remindEnd");
						Remind remind = null;

						if (remindMsg != null && remindMsg.trim().length() > 0) {
							Date dtStart = cacher.getSqlDateFormString(remindStart);
							Date dtEnd = cacher.getSqlDateFormString(remindEnd);
							if (dtStart == null) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "开始日期不能为空");
								break;
							}
							if (dtEnd == null) {
								jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
								jo.addProperty("msg", "结束日期不能为空");
								break;
							}

							remind = new Remind();
							remind.setModality(repair.getModality());
							remind.setDevice(repair.getDevice());
							remind.setMsg(remindMsg);
							remind.setStartDate(dtStart);
							remind.setEndDate(dtEnd);
						}

						String ConfirmPerson = loginUser.getUserName();
						String Confirm = "已确认";

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

						repair.setArrive_time(arrive_time);
						repair.setReport_time(report_time);
						repair.setRestore_time(restore_time);
						repair.setOccur_time(occur_time);

						if (db.updateRepair(repair)) {
							if (remind != null) {
								db.addRemind(remind);
							}

							if (updateDevice != null) {
								db.updateDeviceStatus(repair.getDevice(), updateDevice);
							}

							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "修改成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "数据库操作失败");
						}
					} else if (strMethod.equals("updateDevice")) {
						String modality = request.getParameter("modality");
						String device = request.getParameter("device");
						String equNo = request.getParameter("equNo");
						String name = request.getParameter("name");
						String status = request.getParameter("status");
						String madeIn = request.getParameter("madeIn");
						String makeBy = request.getParameter("makeBy");
						String bryPrice = request.getParameter("bryPrice");
						String useUnit = request.getParameter("useUnit");
						String responseble = request.getParameter("responseble");
						String reportNo = request.getParameter("reportNo");
						String factoryTel = request.getParameter("factoryTel");
						String contactPerson = request.getParameter("contactPerson");
						String contactPhone = request.getParameter("contactPhone");
						String mantanPrice = request.getParameter("mantanPrice");

						String mantanStartDate = request.getParameter("mantanStartDate");
						String mantanEndDate = request.getParameter("mantanEndDate");
						String installDate = request.getParameter("installDate");
						String useStartDate = request.getParameter("useStartDate");

						if (modality == null || modality.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备类型不能为空？");
							break;
						}
						if (device == null || device.trim().length() == 0) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不能为空？");
							break;
						}

						ArrayList<Device> deviceList = db.selectDevices();
						Device en = cacher.findDeviceById(device, deviceList);
						if (en == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备不存在");
							break;
						}

						en.setDevice(device);
						en.setEquNo(equNo);
						en.setName(name);
						en.setStatus(status);
						en.setMadeIn(madeIn);
						en.setMakeBy(makeBy);
						en.setBryPrice(bryPrice);
						en.setUseUnit(useUnit);
						en.setResponseble(responseble);
						en.setReportNo(reportNo);
						en.setFactoryTel(factoryTel);
						en.setContactPerson(contactPerson);
						en.setContactPhone(contactPhone);
						en.setMantanPrice(mantanPrice);
						en.setModality(modality);
						en.setMantanStartDate(cacher.getSqlDateFormString(mantanStartDate));
						en.setMantanEndDate(cacher.getSqlDateFormString(mantanEndDate));
						en.setInstallDate(cacher.getSqlDateFormString(installDate));
						en.setUseStartDate(cacher.getSqlDateFormString(useStartDate));

						if (db.updateDevice(en)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "保存成功");
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "保存失败");
						}

					}
					// 检查技师工作量统计
					else if (strMethod.equals("technicianWorkload")) {
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");

						String modality = "";
						if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
							modality = "全部";
						} else {
							modality = loginUser.getModality();
						}
						JsonArray returnMap = db.selectStudyEngineer(startDate, endDate, modality);

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 报告医生工作量统计
					/**
					 * select vs.UserName,count(*) as Quantity,st.Modality from
					 * Study st left join VSecurity vs on st.Reporter = vs.Name
					 * where st.StudyDate>='2018-10-15' and
					 * st.StudyDate<='2019-10-15' GROUP BY
					 * st.Modality,vs.UserName;
					 */
					else if (strMethod.equals("reportDoctorWorkload")) {
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						String modality = "";
						if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
							modality = "全部";
						} else {
							modality = loginUser.getModality();
						}

						JsonArray returnMap = db.selectReportEngineer(startDate, endDate, modality);

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 审核医生工作量统计
					/**
					 * select vs.UserName,count(*) as Quantity,st.Modality from
					 * Study st left join VSecurity vs on st.Expert = vs.Name
					 * where st.StudyDate>='2018-10-15' and
					 * st.StudyDate<='2019-10-15' GROUP BY
					 * st.Modality,vs.UserName;
					 */
					else if (strMethod.equals("auReportDoctorWorkload")) {
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						String modality = "";
						if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
							modality = "全部";
						} else {
							modality = loginUser.getModality();
						}
						JsonArray returnMap = db.selectAuReportEngineer(startDate, endDate, modality);

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 报告医生报告质量统计
					else if (strMethod.equals("ReportDoctorReporQuality")) {
						String Grade = request.getParameter("Grade");
						String Modality = request.getParameter("Modality");
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						String name = request.getParameter("name");
						JsonArray returnMap = new JsonArray();

						try {
							if (name != null && !"".equals(name)) {
								returnMap = db.ReportDoctorReporQuality(Grade, Modality, DateType, startDate, endDate,
										name);
							} else {
								returnMap = db.ReportDoctorReporQualityNotName(Grade, Modality, DateType, startDate,
										endDate);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 检查技师图像质量统计
					else if (strMethod.equals("technicianImageQuality")) {
						String Grade = request.getParameter("Grade");
						String Modality = request.getParameter("Modality");
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						String name = request.getParameter("name");
						JsonArray returnMap = new JsonArray();
						try {
							if (name != null && !"".equals(name)) {
								returnMap = db.technicianImageQuality(Grade, Modality, DateType, startDate, endDate,
										name);
							} else {
								returnMap = db.technicianImageQualityNotName(Grade, Modality, DateType, startDate,
										endDate);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 审核医生报告质量评价统计
					else if (strMethod.equals("AuReportDoctorReporQuality")) {
						String Grade = request.getParameter("Grade");
						String Modality = request.getParameter("Modality");
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						String name = request.getParameter("name");
						JsonArray returnMap = new JsonArray();
						try {
							if (name != null && !"".equals(name)) {
								returnMap = db.AuReportDoctorReporQuality(Grade, Modality, DateType, startDate, endDate,
										name);
							} else {
								returnMap = db.AuReportDoctorReporQualityNotName(Grade, Modality, DateType, startDate,
										endDate);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 审核医生图像质量评价统计
					else if (strMethod.equals("AuReportDoctorImageQuality")) {
						String Grade = request.getParameter("Grade");
						String Modality = request.getParameter("Modality");
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						String name = request.getParameter("name");
						JsonArray returnMap = new JsonArray();
						try {
							if (name != null && !"".equals(name)) {
								returnMap = db.AuReportDoctorImageQuality(Grade, Modality, DateType, startDate, endDate,
										name);
							} else {
								returnMap = db.AuReportDoctorImageQualityNotName(Grade, Modality, DateType, startDate,
										endDate);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 审核医生审核图像数量统计
					else if (strMethod.equals("ReviewDoctorImageNum")) {
						String Grade = request.getParameter("Grade");
						String Modality = request.getParameter("Modality");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";

						JsonArray returnMap = new JsonArray();
						try {

							String flag = "b.ImgGrade";
							returnMap = db.ReviewDoctorImageNum(Grade, Modality, startDate, endDate, flag);

						} catch (ParseException e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 审核医生审核报告数量统计
					else if (strMethod.equals("ReviewDoctorReportNum")) {
						String Grade = request.getParameter("Grade");
						String Modality = request.getParameter("Modality");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";

						JsonArray returnMap = new JsonArray();
						try {
							String flag = "b.RptGrade";
							returnMap = db.ReviewDoctorImageNum(Grade, Modality, startDate, endDate, flag);

						} catch (ParseException e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 设备收入统计环比/同比
					else if (strMethod.equals("DeviceRevenueStatistics")) {
						String years = request.getParameter("yearsList");
						List<String> yearsList = Arrays.asList(years.split(","));
						String Modality = request.getParameter("Modality");

						if (Modality == null || Modality.trim().equals("") || Modality.trim().equals("0")) {
							if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								Modality = "0";
							} else {
								Modality = loginUser.getModality();
							}
						}

						JsonArray returnMap = db.DeviceRevenueStatistics(yearsList, Modality);
						if (returnMap != null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "ok");
							jo.add("value", returnMap);
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "List参数有误");
						}
					}
					// 人次统计环比/同比
					else if (strMethod.equals("DeviceExposureStatistics")) {
						String years = request.getParameter("yearsList");
						List<String> yearsList = Arrays.asList(years.split(","));
						String Modality = request.getParameter("Modality");
						if (Modality == null || Modality.trim().equals("") || Modality.trim().equals("0")) {
							if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								Modality = "0";
							} else {
								Modality = loginUser.getModality();
							}
						}

						JsonArray returnMap = db.DeviceExposureStatistics(yearsList, Modality);
						if (returnMap != null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "ok");
							jo.add("value", returnMap);
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "List参数有误");
						}
					}
					// 技师交接班
					else if (strMethod.equals("selectShiftTechList2")) {
						String start = request.getParameter("startTime");
						String end = request.getParameter("endTime");

						JsonArray returnMap = new JsonArray();

						try {
							returnMap = db.technicianTakesOver(start, end);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 医生交接班
					else if (strMethod.equals("selectShiftDoctorList2")) {
						String start = request.getParameter("startTime");
						String end = request.getParameter("endTime");

						JsonArray returnMap = new JsonArray();

						try {
							returnMap = db.doctorTakesOver(start, end);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 设备使用记录表
					else if (strMethod.equals("DeviceUseRecord")) {
						String modality = request.getParameter("modality");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						String device = request.getParameter("device");

						if (startDate == null || endDate == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期不能为空");
							break;
						}
						if (device == null) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "设备型号不能为空");
							break;
						}

						if (modality == null || modality.trim().equals("") || modality.trim().equals("全部")) {
							if (loginUser.getModality().equals("All") || loginUser.getModality().equals("ALL")) {
								modality = "全部";
							} else {
								modality = loginUser.getModality();
							}
							// Modality = "全部";
						}

						ArrayList<Statis> list = db.selectExposureStatisticList(startDate, endDate, device, modality);
						ArrayList<Statis> list2 = new ArrayList<Statis>();
						ArrayList<HandOver> handOverList = db.selectHandOverList(startDate, endDate, device, modality);
						ArrayList<HandOver> handOverList2 = new ArrayList<HandOver>();
						ArrayList<HandOver> userNameList = db.selectuserNameList(startDate, endDate, device, modality);
						ArrayList<HandOver> userNameList2 = new ArrayList<HandOver>();

						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						java.util.Date start = null, end = null;
						try {

							start = df.parse(startDate);
							end = df.parse(endDate);

						} catch (ParseException e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "日期不能为空");
							break;
						}

						Calendar calendar = Calendar.getInstance();
						for (java.util.Date d = start; !d.after(end);) {
							Statis s = cacher.findStatis(d, list);
							HandOver handOver = cacher.findHandOver(d, handOverList);
							HandOver userName = cacher.findUserName(d, userNameList);
							if (s == null) {
								Statis n = new Statis();
								n.setIncome(0);
								n.setStudyDate(d);
								n.setTimes(0);
								n.setExposureNum(0);
								list2.add(n);
							} else {
								list2.add(s);
							}
							if (userName == null) {
								HandOver o = new HandOver();
								o.setHandoverDate(d);
								o.setJa(null);
								userNameList2.add(o);
							} else {
								userNameList2.add(userName);
							}
							if (handOver == null) {
								HandOver h = new HandOver();
								h.setHandoverDate(d);
								h.setErrorFlag(0);
								handOverList2.add(h);
							} else {
								handOverList2.add(handOver);
							}
							calendar.setTime(d);
							calendar.add(Calendar.DATE, 1);
							d = calendar.getTime();
						}

						JsonArray ja = new JsonArray();

						SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
						for (int i = 0; i < list2.size(); i++) {
							Statis s = list2.get(i);
							HandOver h = handOverList2.get(i);
							HandOver o = userNameList2.get(i);

							JsonObject obj = new JsonObject();
							obj.addProperty("income", s.getIncome());
							obj.addProperty("times", s.getTimes());
							obj.addProperty("studyDate", sd.format(s.getStudyDate()));
							obj.addProperty("exposureNum", s.getExposureNum());
							obj.addProperty("errorFlag", h.getErrorFlag());
							obj.add("userName", o.getJa());

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);

					}
					// 用于人员管理中添加的科室下拉选
					else if (strMethod.equals("loginUserDeptName")) {
						JsonObject obj = new JsonObject();
						if (loginUser.getDeptName().equals("All") || loginUser.getDeptName().equals("ALL")) {
							JsonArray ja = db.loginUserDeptName();
							obj.add("DeptName", ja);
						} else {
							JsonArray ja = new JsonArray();
							ja.add(loginUser.getDeptName());
							obj.add("DeptName", ja);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("modalityMap", obj);
					}
					// 1.病理百张床位统计
					else if (strMethod.equals("Pathology")) {
						String BedNum = request.getParameter("BedNum");
						if (BedNum == null || "".equals(BedNum)) {
							BedNum = "100";
						}
						System.out.println(BedNum);
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.bedsStatistical(BedNum);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 3.标本规范化固定率统计
					else if (strMethod.equals("SpecimenNormalization")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "标本规范化固定率统计";
						String flag = "3";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 4.大标本 取材时间-固定时间固定率统计
					else if (strMethod.equals("LargeSpecimen")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "大标本规范化固定率统计";
						String flag = "4";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 5.小标本 取材时间-固定时间固定率统计
					else if (strMethod.equals("SmallSpecimen")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "小标本规范化固定率统计";
						String flag = "5";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 6.HE染色切片优良率统计
					else if (strMethod.equals("HEDyeingSlice")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "HE染色切片优良率统计";
						String flag = "6";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 7.免疫组化染色切片优良率统计
					else if (strMethod.equals("ImmuneDyeingSlice")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "免疫组化染色切片优良率统计";
						String flag = "7";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 8.特殊染色切片优良率统计
					else if (strMethod.equals("SpecialDyeingSlice")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "特殊染色切片优良率统计";
						String flag = "8";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 9.术中快速病理诊断及时率统计
					else if (strMethod.equals("Diagnosis")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "术中快速病理诊断及时率统计";
						String flag = "9";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 10.小样本组织病理诊断及时率统计
					else if (strMethod.equals("Histopathologic")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "小样本组织病理诊断及时率统计";
						String flag = "10";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 11.大样本组织病理诊断及时率统计
					else if (strMethod.equals("Largepathologic")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "大样本组织病理诊断及时率统计";
						String flag = "11";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 12.脱钙和特殊处理标本组织病理诊断及时率统计
					else if (strMethod.equals("Specialpathologic")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "脱钙和特殊处理标本组织病理诊断及时率统计";
						String flag = "12";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 13.液基细胞正常诊断及时率统计
					else if (strMethod.equals("LiquidCytologic")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "液基细胞正常诊断及时率统计";
						String flag = "13";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 14.液基细胞特殊诊断及时率统计
					else if (strMethod.equals("LiquidSpecialCytologic")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "液基细胞特殊诊断及时率统计";
						String flag = "14";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 15.细胞正常诊断及时率统计
					else if (strMethod.equals("Cytologic")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "细胞正常诊断及时率统计";
						String flag = "15";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 16.细胞特殊诊断及时率统计
					else if (strMethod.equals("SpecialCytologic")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "细胞特殊诊断及时率统计";
						String flag = "16";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 17.分子室内质控合格率统计
					else if (strMethod.equals("MolecularIndoor")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "分子室内质控合格率统计";
						String flag = "17";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 18.免疫组化室间合格率统计
					else if (strMethod.equals("ImmBetRoom")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "免疫组化室间合格率统计";
						String flag = "18";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 19.分子室间质评合格率统计
					else if (strMethod.equals("MolecularBetRoom")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "分子室间质评合格率统计";
						String flag = "19";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 20.细胞学病理诊断质控符合率
					else if (strMethod.equals("CytQuality")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "细胞学病理诊断质控符合率";
						String flag = "20";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 21.快速石蜡诊断符合率
					else if (strMethod.equals("Paraffin")) {
						String DateType = request.getParameter("DateType");
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						startDate += " 00:00:00";
						endDate += " 23:59:59";
						String name = "快速石蜡诊断符合率";
						String flag = "21";
						JsonArray returnMap = new JsonArray();
						try {

							returnMap = db.UnifiedQuality(DateType, startDate, endDate, name, flag);

						} catch (Exception e) {
							e.printStackTrace();
						}
						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.addProperty("msg", "ok");
						jo.add("value", returnMap);
					}
					// 普通和危化药剂管理
					else if (strMethod.equals("PotionManage")) {
						String OEMName = request.getParameter("oEMName");
						String MedicineName = request.getParameter("medicineName");
						String type = request.getParameter("type");

						JsonArray ja = new JsonArray();
						ArrayList<MedicineManage> list = db.selectPotionManageList(OEMName, MedicineName, type);
						for (int i = 0; i < list.size(); i++) {
							MedicineManage medicine = list.get(i);

							JsonObject obj = new JsonObject();
							obj.addProperty("ManageID", medicine.getManageID());
							obj.addProperty("OEMCode", medicine.getOEMCode());
							obj.addProperty("OEMName", medicine.getOEMName());
							obj.addProperty("MedicineBatch", medicine.getMedicineBatch());
							obj.addProperty("MedicineCode", medicine.getMedicineCode());
							obj.addProperty("MedicineName", medicine.getMedicineName());
							obj.addProperty("MedicineUnit", medicine.getMedicineUnit());
							obj.addProperty("MedicineDeadline", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(medicine.getMedicineDeadline()));
							obj.addProperty("InventoryNum", medicine.getInventoryNum());
							obj.addProperty("ThresholdNum", medicine.getThresholdNum());
							obj.addProperty("MedicineType", medicine.getMedicineType() + "" == null
									|| "0".equals(medicine.getMedicineType() + "") ? "普通药剂" : "危化药剂");

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}
					// 普通药剂管理删除记录
					else if (strMethod.equals("deleteOrdinaryPotion")) {
						String id = request.getParameter("id");
						int nid = 0;
						try {
							nid = Integer.parseInt(id);
						} catch (Exception e) {
							jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
							jo.addProperty("msg", "参数必须为数字");
							break;
						}

						if (db.deleteOrdinaryPotion(nid)) {
							jo.addProperty("flag", Constant.RETURN_STATUS_OK);
							jo.addProperty("msg", "删除成功");
							break;
						} else {
							jo.addProperty("flag", Constant.RETURN_STATUS_SQL_ERROR);
							jo.addProperty("msg", "删除失败");
							break;
						}
					}
					// 普通药剂操作记录
					else if (strMethod.equals("MedicineRecord")) {
						String time = request.getParameter("time");
						String startDate = "";
						String endDate = "";
						if(time != null && !"".equals(time)){
					        startDate = time.substring(0,10) + " 00:00:00";
					        endDate = time.substring(13) + " 23:59:59";
						}
						String type = request.getParameter("type");
						String manageId = request.getParameter("manageId");
	
						JsonArray ja = new JsonArray();
						ArrayList<MedicineOperRecord> list = db.selectMedicineRecordList(startDate, endDate, type, manageId);
						for (int i = 0; i < list.size(); i++) {
							MedicineOperRecord medicine = list.get(i);

							JsonObject obj = new JsonObject();
							obj.addProperty("ManageID", medicine.getManageID());
							obj.addProperty("MedicineBatch", medicine.getMedicineBatch());
							obj.addProperty("MedicineCode", medicine.getMedicineCode());
							obj.addProperty("MedicineName", medicine.getMedicineName());
							obj.addProperty("RecordName", medicine.getRecordName());
							obj.addProperty("OperDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(medicine.getOperDate()));
							obj.addProperty("OperNum", medicine.getOperNum());
							obj.addProperty("RecordType", medicine.getRecordType() + "" == null
									|| "0".equals(medicine.getRecordType() + "") ? "入库" : "出库");

							ja.add(obj);
						}

						jo.addProperty("flag", Constant.RETURN_STATUS_OK);
						jo.add("list", ja);
					}else {
						jo.addProperty("flag", Constant.RETURN_STATUS_PARAM_ERROR);
						jo.addProperty("msg", "未定义方法");
					}
					// else if (strMethod.equals("xxx"))
				}
			}

		} while (false);

		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.getWriter().append(jo.toString());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private Map<String, String> getQueryStringMap(String queryString) {
		Map<String, String> map = new HashMap<String, String>();
		String[] strArray = queryString.split("&");
		for (int i = 0; i < strArray.length; i++) {
			String str = strArray[i];
			String[] array = str.split("=");
			if (array.length == 2) {
				map.put(array[0], array[1]);
			}
		}

		return map;
	}

}
