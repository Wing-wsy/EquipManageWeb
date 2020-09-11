<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>新增体检记录</title>
<link href="css/base.css" type="text/css" rel="stylesheet">
<link href="css/main.css" type="text/css" rel="stylesheet">
<link href="css/page.css" type="text/css" rel="stylesheet">
<script src="js/jquery-1.8.2.min.js" type="text/jscript"></script>
<script src="js2/initialization.js" type="text/jscript"></script>
<script type="text/javascript">
	$("#uploadFileButton").click(function () {
		return $("#selectedFile").click();
	});
	
	$("selectedFile").change(function () {
		
	});
</script>
<!--[if lte IE 9]><script src="/js/selectivizr.js"></script><![endif]-->
</head>

<body>
<div class="pacs_wrapper">
	<div class="left">
	
		<div class="logo"></div>
		<div class="tree">
			<ul>
                <li>
                    <a class="first_level"><i class="icon-plus-sign"></i> <span>设备管理</span></a>
                    <ul>
						<li><a class="second_level" href="equipment_summary_index.html"><i class="ico-1"></i> <span>设备汇总</span></a></li>
						<li><a class="second_level" href="device_report.html"><i class="ico-2"></i> <span>设备报表</span></a></li>
						<li ><a class="second_level" href="Important_Reminder.html"><i class="ico-3"></i> <span>重要提醒</span></a></li>
                        <li><a class="second_level" href="add_Fault_Repair.html"><i class="ico-4"></i> <span>故障报修</span></a></li>
                        <li><a class="second_level" href="repair_record.html"><i class="ico-5"></i> <span>维修记录</span></a></li>
                        <li><a class="second_level" href="maintenance_record.html"><i class="ico-6"></i> <span>保养记录</span></a></li>
                        <li><a class="second_level" href="check.html"><i class="ico-7"></i> <span>检测记录</span></a></li>
                    </ul>
                </li>
                <li>
                    <a class="first_level"><i class="icon-plus-sign"></i> <span>工作管理</span></a>
                    <ul>
						<li ><a class="second_level" href="change_shifts.html"><i class="ico-8"></i> <span>交接班</span></a></li>
            <li><a class="second_level" href="pages/workload/ReportDoctor.html"><i class="ico-17"></i> <span>医生工作量统计</span></a></li>
						<li><a class="second_level" href="pages/technician/technician.html"><i class="ico-18"></i> <span>技师工作量统计</span></a></li>
            <li><a class="second_level" href="pages/ImageQuality/ImageQuality.html"><i class="ico-19"></i> <span>图像质量统计</span></a></li>
            <li><a class="second_level" href="pages/ReportQuality/ReportQuality.html"><i class="ico-20"></i> <span>报告质量统计</span></a></li>
                    </ul>
                </li>
                <li>
                    <a class="first_level"><i class="icon-plus-sign"></i> <span>人员管理</span></a>
                    <ul>
						<li ><a class="second_level" href="personal_management.html"><i class="ico-10"></i> <span>人员管理</span></a></li>
						<!-- 
                        <li><a class="second_level"><i class="ico-11"></i> <span>分组管理</span></a></li>
                         -->
                        <li class="selected"><a class="second_level"><i class="ico-12"></i> <span>体检记录</span></a></li>
                        <li><a class="second_level" href="personal_dose.html"><i class="ico-13"></i> <span>个人剂量</span></a></li>
                        <li ><a class="second_level" href="recuperation_record.html"><i class="ico-14"></i> <span>疗养记录</span></a></li>
                        <li><a class="second_level" href="protection_training.html"><i class="ico-15"></i> <span>防护培训</span></a></li>
                        <li ><a class="second_level" href="health_record.html"><i class="ico-16"></i> <span>保健假记录</span></a></li>
                    </ul>
                </li>
            </ul>
		</div>
	</div>
   <div class="main">
			<div class="top_contorl">
					<div class="t-l"><a class="back" href="health_check.html"><i class="back_btn"></i><span>返回</span></a></div>
					<div class="t-r"><a class="save" href="javascript:void(0)" onclick="onButtonAdd()">保存</a></div>
			</div>		
			<div class="container">
				<div class="equipment_summary">
					<div class="title f14 purple">新增体检记录</div>
					<div class="content">
						<form>
							<ul class="m_0">
								
								<div class="form">
								
									<li class="mr30">
										<label class="f14">姓名：</label>
										<input type="text" alt="" value="${exam.personName}" placeholder="" id="personName">
									</li>
									<li class="mr30">
										<label class="f14">体检项目：</label>
										<input type="text" alt="" value="${exam.checkItem}" placeholder="" id="checkItem">
									</li>
									<li class="mr30">
										<label class="f14">体检机构：</label>
										<input type="text" alt="" value="${exam.institution}" placeholder="" id="institution">
									</li>
								</div>
								<div class="form">	
									
									<li class="mr30">
										<label class="f14">检测时间：</label>
										<input type="date" alt="" value="${exam.checkTime}" placeholder="" id="checkTime">
									</li>
									
									<li class="mr30">
										<label class="f14">体检结果：</label>
										<input type="text" alt="" value="${exam.checkResult}" placeholder="" id="checkResult">
									</li>
									
									
								</div>
								
								<!-- 
								<div class="form">
								
									<li class="file">
										
										<input id="location" class="form-control" disabled placeholder="点击浏览上传文件">
										<input type="button" id="i-check" value="浏览" class="btn" onclick="$('#i-file').click();">
										<input class="hide" type="file" id="i-file"  onchange="$('#location').val($('#i-file').val());" style="display: ">
									</li>

								</div>
								
								 -->
								
								
								
								
								
							</ul>
							
							
							
						</form>
					</div>
				</div>
			</div>
			
			<div class="container" id="attachmentdiv">
				<input hidden type="file" id="selectedFile"/>
				<button type="button" id="uploadFileButton">上传文件</button>
			</div>
			
			
   		
   </div>
    
</div>    
<!--end of  pacs_wrapper -->
	
</body>
</html>
