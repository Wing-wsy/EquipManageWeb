/**
* 全景医疗信息系统 - 病人主页
* 
* @author haycco
*/
$(function(){
	
	//弹出框点击背景隐藏弹出框
	/*$("#imgPacs").click(function(){ 
		close();
	});
	$("#imgPacs .general_layer").click(function(e){ 
		e.stopPropagation();
	});*/
	
});	

//显示和隐藏图像控件弹出框
var obj;
var userName;
var passWord;
function showImgPacs(_this){
	$("#imgPacs").show();
	
	obj = _this;
	
	//检查id
	var fexamid = $(_this).attr("data-fexamid");
	
	//判断是用哪种方式调用影像
	var pacsJs = $("#pacsJs").val();
	if(pacsJs == "myImages.js"){
		
		$.ajax({
            type: "post",
            url: "../pacs/getImagePath",
            data: {fexamid : fexamid},
            success: function (result) {
            	var fstatus = $(_this).attr("data-fstatus");
            	var isActive = PacsModule.isActive();
            	setTimeout("PacsModule.init();", 1000);
        		if(isActive){
        			
        			//TODO 等待真实数据测试
        			//if(fstatus == "2"){  //中大六院 QR
        			//	setTimeout("PacsModule.loadQR('"+ result +"');", 2000);
                	//}else if(fstatus == "3"){   //中大二院 QR
                	//	setTimeout("PacsModule.loadQR('"+ result +"');", 2000);
                	//}else{  //海印影像传递方式FTP
            			setTimeout("PacsModule.loadFTP('"+ result +"');", 2000);
                	//}
        		}
            }
        });
		
	}else{
		
		//判断是否安装盈谷图像控件客户端,判断方法：安装路径下是否含有SpiderSightClient.exe
		var path = "C:\\Program Files (x86)\\iMAGES\\Release\\SpiderSightClient.exe";
		$.ajax({
            type: "post",
            url: "../pacs/hasFile",
            data: {path : path, fexamid : fexamid},
            success: function (result) {
                if(result != "no"){
                	var tempResult = result.split("||");
                	var url = tempResult[0];
                	userName = tempResult[1];
                	passWord = tempResult[2];
                	
                	writeContent(url);
                }else{
                	var tip = "您还没有安装图像控件，下载安装好<a href='/iMAGES客户端安装包.exe' " +
                				"style='color:#156BDD;'>iMAGES客户端安装包.exe</a>之后，<br/>页面刷新即可使用。";
                	$("#chart_tip").html(tip);
                }
            }
        });
	}
}

//退出图像控件要做的操作
function close(){
	$("#imgPacs").hide();
	
	var pacsJs = $("#pacsJs").val();
	if(pacsJs == "myImages.js"){
		PacsModule.destroy();
		$("#report").hide();
		$("#TmpOcx1").show();
	}else{
		document.location="accurad:///[OEM!DEFAULT][Version!DEFAULT][Customer!DEFAULT]" +
				"[Modality!MR][App!SSCAMatrix][Plugin!SSCPViewer]" +
				"[SSServerIP!125.76.226.192][SSServerPort!40000][UserName!"+ userName +"]" +
				"[Password!"+ passWord +"][ShareDataMode!true][Exit!true]"; 
	}
}

//影像报告设置数据
function setReportDate(){
	var name = $(obj).attr("data-name");
	var sex = $(obj).attr("data-sex") == "1" ? "男" : "女";
	var age = $(obj).attr("data-age");
	var fexamdept = $(obj).attr("data-fexamdept");
	
	var fexamtime = $(obj).attr("data-fexamtime");
	var fexampart = $(obj).attr("data-fexampart");
	var fexammethod = $(obj).attr("data-fexammethod");
	
	var fimagedesc = $(obj).attr("data-fimagedesc");
	var fexamdesc = $(obj).attr("data-fexamdesc");
	
	var fexamreportor = $(obj).attr("data-fexamreportor");
	var fexamreporttime = $(obj).attr("data-fexamreporttime");
	var fverifydoctorname = $(obj).attr("data-fverifydoctorname");
	var fverifytime = $(obj).attr("data-fverifytime");

	$("#name").html(name);
	$("#sex").html(sex);
	$("#age").html(age);
	$("#dept").html(fexamdept);
	
	$("#date").html(fexamtime);
	$("#item").html(fexampart);
	$("#way").html(fexammethod);
	
	$("#see").html(fimagedesc);
	$("#conclusions").html(fexamdesc);

	$("#reportDoctor").html(fexamreportor);
	$("#reportDate").html(fexamreporttime);
	$("#auditDoctor").html(fverifydoctorname);
	$("#auditDate").html(fverifytime);
}
