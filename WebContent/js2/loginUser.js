$(document).ready(function(){
	$.ajax({ 
		type: "get", 
		url: "Device?method=getLoginUser", 
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
					$("#potionManage").remove();
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

