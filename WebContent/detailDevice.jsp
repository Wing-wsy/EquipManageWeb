<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>新增设备信息</title>
<link href="css/base.css" type="text/css" rel="stylesheet">
<link href="css/main.css" type="text/css" rel="stylesheet">
<link href="css/page.css" type="text/css" rel="stylesheet">
<script src="js/jquery-1.8.2.min.js" type="text/jscript"></script>
<script src="js2/initialization.js" type="text/jscript"></script>
<script type="text/javascript">
	function onButtonSave(obj)
	{
		var obj = {};
		obj.modality = $("#modality").val();
		obj.device = $("#device").val();
		obj.equNo = $("#equNo").val();
		obj.name = $("#name").val();
		obj.status = $("#status").val();
		obj.madeIn = $("#madeIn").val();
		obj.makeBy = $("#makeBy").val();
		obj.bryPrice = $("#bryPrice").val();
		obj.useUnit = $("#useUnit").val();
		obj.responseble = $("#responseble").val();
		obj.installDate = $("#installDate").val();
		obj.useStartDate = $("#useStartDate").val();
		obj.reportNo = $("#reportNo").val();
		obj.factoryTel = $("#factoryTel").val();
		obj.contactPerson = $("#contactPerson").val();
		obj.contactPhone = $("#contactPhone").val();
		obj.mantanStartDate = $("#mantanStartDate").val();
		obj.mantanEndDate = $("#mantanEndDate").val();
		obj.mantanPrice = $("#mantanPrice").val();
		
		$.ajax({ 
			type: "get", 
			url: "Device?method=updateDevice", 
			contentType: "application/json; charset=utf-8",
			data: obj,
			dataType: "json", 
			success: function (data) { 
				if (data.flag == "0")
				{
					alert("保存成功!");
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
<!--[if lte IE 9]><script src="/js/selectivizr.js"></script><![endif]-->

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
						<li class="selected"><a class="second_level"><i class="ico-1"></i> <span>设备汇总</span></a></li>
						<li><a class="second_level" href="device_report.html"><i class="ico-2"></i> <span>设备报表</span></a></li>
						<li ><a class="second_level" href="Important_Reminder.html"><i class="ico-3"></i> <span>重要提醒</span></a></li>
                        <li><a class="second_level" href="add_Fault_Repair.html"><i class="ico-4"></i> <span>故障报修</span></a></li>
                        <li><a class="second_level" href="repair_record.html"><i class="ico-5"></i> <span>维修记录</span></a></li>
                        <li><a class="second_level" href="maintenance_record.html"><i class="ico-6"></i> <span>保养记录</span></a></li>
                        <li><a class="second_level"><i class="ico-7"></i> <span>检测记录</span></a></li>
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
					<div class="t-l"><a class="back" href="equipment_summary_index.html"><i class="back_btn"></i><span>返回</span></a></div>
					<div class="t-r"><a class="save" href="javascript:void(0)"  onclick="onButtonSave(this);">保存</a></div>
			</div>		
			<div class="container">
				<div class="equipment_summary">
					<div class="content">
						<form>
							<ul>
								<h1 class="f16">设备信息</h1>
								<div class="form">
									<li>
										<label class="f14">设备类型：</label>
										<input type="text" placeholder="" value="${deviceObj.modality}" alt="" id="modality">
									</li>
									<li>
										<label class="f14">型号：</label>
										<input type="text" placeholder="" value="${deviceObj.device}" alt="" id="device">
									</li>
									<li>
										<label class="f14">设备编号：</label>
										<input type="text" placeholder="" value="${deviceObj.equNo}" alt="" id="equNo">
									</li>
									<li>
										<label class="f14">设备名称：</label>
										<input type="text" placeholder="" value="${deviceObj.name}" alt="" id="name">
									</li>
									
								</div>
								<div class="form">
									<li>
										<label class="f14">产地：</label>
										<input type="text" placeholder="" value="${deviceObj.madeIn}" alt="" id="madeIn">
									</li>
									<li>
										<label class="f14">生产厂家：</label>
										<input type="text" placeholder="" value="${deviceObj.makeBy}" alt="" id="makeBy">
									</li>
									<li>
										<label class="f14">购入价格：</label>
										<input type="text" placeholder="" value="${deviceObj.bryPrice}" alt="" id="bryPrice">
									</li>
									<li>
										<label class="f14">设备状态：</label>
										<input type="text" placeholder="" value="${deviceObj.status}" alt="" id="status">
									</li>
								</div>
							</ul>
							<ul>
								<h1 class="f16">使用情况</h1>

								<div class="form">
									
									<li>
										<label class="f14">使用单位：</label>
										<input type="text" placeholder="" value="${deviceObj.useUnit}" alt="" id="useUnit">
									</li>
									<li>
										<label class="f14">责任人：</label>
										<input type="text" placeholder="" value="${deviceObj.responseble}" alt="" id="responseble">
									</li>
									<li>
										<label class="f14">装机时间：</label>
										<input type="date" placeholder="" value="${deviceObj.installDate}" alt="" id="installDate">
									</li>
										<li>
										<label class="f14">启用时间：</label>
										<input type="date" placeholder="" value="${deviceObj.useStartDate}" alt="" id="useStartDate">
									</li>
									
								</div>
							</ul>
							<ul>
								<h1 class="f16">设备保养</h1>

								<div class="form">
									
									<li>
										<label class="f14">报修编码：</label>
										<input type="text" placeholder="" value="${deviceObj.reportNo}" alt="" id="reportNo">
									</li>
									<li>
										<label class="f14">厂家热线电话：</label>
										<input type="text" placeholder="" value="${deviceObj.factoryTel}" alt="" id="factoryTel">
									</li>
									<li>
										<label class="f14">报修联系人：</label>
										<input type="text" placeholder="" value="${deviceObj.contactPerson}" alt="" id="contactPerson">
									</li>
									<li>
										<label class="f14">报修手机号：</label>
										<input type="text" placeholder="" value="${deviceObj.contactPhone}" alt="" id="contactPhone">
									</li>

								</div>
								<div class="form">
									
									<li>
										<label class="f14">质保起始时间：</label>
										<input type="date" placeholder="" value="${deviceObj.mantanStartDate}" alt="" id="mantanStartDate">
									</li>
									<li>
										<label class="f14">质保截止时间：</label>
										<input type="date" placeholder="" value="${deviceObj.mantanEndDate}" alt="" id="mantanEndDate">
									</li>
									<li>
										<label class="f14">年保修价格：</label>
										<input type="text" placeholder="" value="${deviceObj.mantanPrice}" alt="" id="mantanPrice">
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
    