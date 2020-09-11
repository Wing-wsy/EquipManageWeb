<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>新增设备信息</title>
<link href="css/base.css" type="text/css" rel="stylesheet">
<link href="css/main.css" type="text/css" rel="stylesheet">
<link href="css/page.css" type="text/css" rel="stylesheet">
<link href="css/jquery.datetimepicker.css" type="text/css" rel="stylesheet">
<script src="js/jquery-1.8.2.min.js" type="text/jscript"></script>
<script src="js/jquery.datetimepicker.full.js" type="text/jscript"></script>
<script src="js2/initialization.js" type="text/jscript"></script>
<script type="text/javascript">
$(document).ready(function () {
	$.datetimepicker.setLocale('ch');
	$('#occur_time').datetimepicker();
	$('#report_time').datetimepicker();
	$('#arrive_time').datetimepicker();
	$('#restore_time').datetimepicker();
	
	$("#result").val($("#repairResult").val());
});

	function onButtonSave(obj)
	{
		var obj = {};
		obj.arrive_time = $("#arrive_time").val();
		obj.restore_time = $("#restore_time").val();
		obj.occur_time = $("#occur_time").val();
		obj.report_time = $("#report_time").val();
		obj.repair_content = $("#repair_content").val();
		obj.replace = $("#replace").val();
		obj.cuase = $("#cuase").val();
		obj.result = $("#result").val();
		obj.engineer = $("#engineer").val();
		obj.Confirm = $("#Confirm").val();
		obj.phenomena = $("#phenomena").val();
		obj.extent = $("#extent").val();
		obj.report_person = $("#report_person").val();
		obj.repairId = $("#repairId").val();
		obj.remindStart = $("#remindStart").val();
		obj.remindEnd = $("#remindEnd").val();
		obj.remindMsg = $("#remindMsg").val();
		
		
		$.ajax({ 
			type: "get", 
			url: "Device?method=updateRepair", 
			contentType: "application/json; charset=utf-8",
			data: obj,
			dataType: "json", 
			success: function (data) { 
				if (data.flag == "0")
				{
					alert("添加成功!");
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
                        <li class="selected"><a class="second_level"><i class="ico-5"></i> <span>维修记录</span></a></li>
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
                        <li ><a class="second_level" href="health_check.html"><i class="ico-12"></i> <span>体检记录</span></a></li>
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
					<div class="t-l"><a class="back" href="repair_record.html"><i class="back_btn"></i><span>返回</span></a></div>
					<div class="t-r"><a class="save" href="javascript:void(0)"  onclick="onButtonSave(this);">保存</a></div>
			</div>		
			<div class="container">
				<div class="equipment_summary">
					<div class="content">
						<form>
							<ul>
								<h1 class="f16">报修信息</h1>
								<div class="form">
									<li>
										<label class="f14">设备类型：</label>
										<input type="text" placeholder="" value="${entityObj.modality}" alt="" id="modality">
									</li>
									<li>
										<label class="f14">设备型号：</label>
										<input type="text" placeholder="" value="${entityObj.device}" alt="" id="device">
									</li>
								</div>
								
								<div class="form">
									<li>
										<label class="f14">发生时间：</label>
										<input type="text" placeholder="" value="${entityObj.occur_time_string}" alt="" id="occur_time">
										<input type="hidden" placeholder="" value="${entityObj.id}" alt="" id="repairId">
										<input type="hidden" placeholder="" value="${entityObj.result}" alt="" id="repairResult">
									</li>
									<li>
										<label class="f14">报告时间：</label>
										<input type="text" placeholder="" value="${entityObj.report_time_string}" alt="" id="report_time">
									</li>
									<li>
										<label class="f14">到达时间：</label>
										<input type="text" placeholder="" value="${entityObj.arrive_time_string}" alt="" id="arrive_time">
									</li>
									<li>
										<label class="f14">恢复时间：</label>
										<input type="text" placeholder="" value="${entityObj.restore_time_string}" alt="" id="restore_time">
									</li>
								</div>
								<div class="form">
									<li>
										<label class="f14">报告技师：</label>
										<input type="text" placeholder="" value="${entityObj.report_person}" alt="" id="report_person">
									</li>
									<li>
										<label class="f14">是否确认：</label>
										<input type="text" placeholder="" value="${entityObj.confirm}" alt="" id="Confirm">
									</li>
									<li>
										<label class="f14">严重程度：</label>
										<input type="text" placeholder="" value="${entityObj.extent}" alt="" id="extent">
									</li>
									<li>
										<label class="f14">出现现象：</label>
										<input type="text" placeholder="" value="${entityObj.phenomena}" alt="" id="phenomena">
									</li>
								</div>
							</ul>
							
							<ul>
								<h1 class="f16">维修情况</h1>
								<div class="form">
									<li>
										<label class="f14">工程师姓名：</label>
										<input type="text" placeholder="" value="${entityObj.engineer}" alt="" id="engineer">
									</li>
									<li>
										<label class="f14">更换配件：</label>
										<input type="text" placeholder="" value="${entityObj.replace}" alt="" id="replace">
									</li>
									<li>
										<label class="f14">故障原因：</label>
										<input type="text" placeholder="" value="${entityObj.cuase}" alt="" id="cuase">
									</li>
									
								</div>
								<div class="form">
									<li>
										<label class="f14">维修内容：</label>
										<input type="text" placeholder="" value="${entityObj.repair_content}" alt="" id="repair_content">
									</li>	
									<li>
										<label class="f14">维修结果：</label>
										<select name="" id="result">
											<option value="已解决">已解决</option>
											<option value="未解决">未解决</option>
											<option value="待观察">待观察</option>
										</select>
									</li>
									<li>
										<label class="f14">确认技师：</label>
										<input type="text" placeholder="" value="${entityObj.confirmPerson}" alt="" id="confirmPerson">
									</li>
								</div>
								
							</ul>
							
							<ul>
								<h1 class="f16">维修提醒</h1>
								<div class="form">
									<li>
										<label class="f14">提醒内容：</label>
										<input type="text" placeholder="" value="" alt="" id="remindMsg">
									</li>
									<li>
										<label class="f14">开始日期：</label>
										<input type="date" placeholder="" value="" alt="" id="remindStart">
									</li>
									<li>
										<label class="f14">结束日期：</label>
										<input type="date" placeholder="" value="" alt="" id="remindEnd">
									</li>
									
								</div>
							</ul>
						</form>
					</div>
				</div>
			</div>
   		
   </div>
    
</div>    
<!--end of  pacs_wrapper -->
	
</body>
</html>