$(document).ready(function(){
	
//		var strFullPath = window.document.location.href;
//		var strPath = window.document.location.pathname;
//		var pos = strFullPath.indexOf(strPath);
//		var prePath = strFullPath.substring(0, pos);
//		var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
//		var genPath = prePath + postPath;
	

	$.ajax({ 
		type: "get", 
//		url: genPath+"/Device?method=getLoginUser", 
		url: path+"?method=getLoginUser",
		dataType: "json", 
		success: function (data) { 
			if (data.flag == "0")
			{
				$("#loginUser").text(data.loginUser);
				//新加代码
				if(data.deptName != '病理科'){
					$("#pathology").remove();
					$("#SpecimenNormalization").remove();
					$("#DyeingSlice").remove();
					$("#Diagnosis").remove();
					$("#Histopathologic").remove();
					$("#Cytologic").remove();
					$("#MolecularIndoor").remove();
					$("#ImmBetRoom").remove();
					$("#MolecularBetRoom").remove();
					$("#CytQuality").remove();
					$("#Paraffin").remove();
				}else if(data.deptName == '病理科'){
					//登录用户所属科室为病理科
					$("#handover").remove();
					$("#dochandover").remove();
					$("#techhandover").remove();
					$("#docwork").remove();
					$("#techwork").remove();
					$("#ImageQuality").remove();
					$("#ReportQuality").remove();
					$("#QualityControl").remove();
					
				}
				
			}
			else if (data.flag == "3")
			{
				$(location).attr('href', 'login.html');
			}
		}, 
		error: function (XMLHttpRequest, textStatus, errorThrown) { 
			alert(errorThrown); 
		} 
	});
	
	$(".msg_login .btn").bind("click",function(){
		window.location.href='login.html';
	});
});

