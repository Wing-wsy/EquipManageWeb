<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>上传附件</title>
<link href="css/base.css" type="text/css" rel="stylesheet">
<link href="css/main.css" type="text/css" rel="stylesheet">
<link href="css/page.css" type="text/css" rel="stylesheet">

<script src="js/jquery-1.8.2.min.js" type="text/jscript"></script>
<script src="js2/initialization.js" type="text/jscript"></script>
<script type="text/javascript">
$(document).ready(function () {
	window.status = 0; // 0 正常状态  1编辑状态
	
	$("#uploadFileButton").click(function () {
		return $("#selectedFile").click();
	});
	
	$("#selectedFile").change(function () {
		$("#uploadForm").submit();
	});		
});

function onDeleteAttach(id)
{
	$.ajax({ 
		type: "get", 
		url: "Device?method=deleteAttach", 
		data: {"id": id},
		dataType: "json", 
		success: function (data) { 
			if (data.flag == "0")
			{
				var str = "";
				str += "#attachul li[objid='";
				str += id;
				str += "']";
				$(str).remove();
			}
			else if (data.flag == "3")
			{
				$(location).attr('href', 'login.html');
			}
			else
			{
				alert(data.msg);
			}
		}, 
		error: function (XMLHttpRequest, textStatus, errorThrown) { 
			alert(errorThrown); 
		} 
	});
}

function onDeleteButton()
{
	if (window.status == 0)
	{
		$("#deleteBtn").text("完成");
		window.status = 1;
		$(".close").show();
	}
	else if (window.status == 1)
	{
		$("#deleteBtn").text("删除");
		window.status = 0;
		$(".close").hide();
	}
}
</script>
</head>

<body>
<div class="pacs_wrapper">
	<div class="left">
	
		<div class="logo">
			<div class="pic_lg"></div>
			<p>龙岗区第三人民医院</p>
		</div>
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
						<li><a class="second_level" href="change_shifts.html"><i class="ico-8"></i> <span>医生交接班</span></a></li>
						<li><a class="second_level" href="tech_handover.html"><i class="ico-8"></i> <span>技师交接班</span></a></li>
            <li><a class="second_level" href="pages/workload/ReportDoctor.html"><i class="ico-17"></i> <span>医生工作量统计</span></a></li>
						<li><a class="second_level" href="../technician/technician.html"><i class="ico-18"></i> <span>技师工作量统计</span></a></li>
            <li><a class="second_level" href="../ImageQuality/ImageQuality.html"><i class="ico-19"></i> <span>图像质量统计</span></a></li>
            <li><a class="second_level" href="../ReportQuality/ReportQuality.html"><i class="ico-20"></i> <span>报告质量统计</span></a></li>
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
				
					<div class="t-l"><a class="back" href="${backPage}"><i class="back_btn"></i><span>返回</span></a></div>
					<!-- 
					<div class="t-r"><a class="save" href="">保存</a></div>
					 -->
				
			</div>		
			
			
			<div class="container">
				
				<div class="upload"><a id="uploadFileButton" href="javascript:void(0)">上传附件</a></div>
				

			</div>
			
			<form id="uploadForm" action="Device?method=uploadFile&attachType=${attachType}&relativeId=${relativeId}" 
				method="post" enctype="multipart/form-data">

				<input hidden type="file" id="selectedFile" multiple="multiple" name="file1"/>	

				<div class="container sys_plan" style="padding: 0px;">
					<div class="add_btn">
						<h1>附件管理</h1>
						<a id="deleteBtn" class="export fr_2" href="javascript:void(0)" onclick="onDeleteButton()">删除</a>
					</div>
					<div class="fj-manage">
						${attachListDiv}	
						<div class="cb"></div>
					</div>
				</div>
			
			</form>

 			<!-- 
 			<div class="container sys_plan" style="padding: 0px;">
 			
 				<div class="add_btn">
					<h1>附件管理</h1>
					<a class="export fr_2" href="#" style="margin-right: 10px;">编辑</a>
					<a  class="fr_2" href="#">删除</a>
					
					<a style="display: none;" class="export fr_2" href="#">完成</a>
					

				</div>
				
				<div class="fj-manage">
				
					<ul>
						<li><a href="#"><img src="images/CTJB_001003.jpg"></a></li>
						<li><a href="#"><img src="images/CTJB_001003.jpg"></a></li>
						<li><a href="#"><img src="images/CTJB_001003.jpg"></a></li>
						<li><a href="#"><img src="images/CTJB_001003.jpg"></a></li>
						<li><a href="#"><img src="images/CTJB_001003.jpg"></a></li>
						<li><a href="#"><img src="images/CTJB_001003.jpg"></a></li>
						<li><a href="#"><img src="images/CTJB_001003.jpg"></a></li>
						
						
					</ul>
					<div class="cb"></div>
				</div>
				

			</div>
  			
  			 -->
   		
   </div>
    
</div>    
<!--end of  pacs_wrapper -->
	
</body>
</html>
