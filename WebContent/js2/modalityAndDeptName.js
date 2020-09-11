/**
 * id 为 modality device
 */

$(document).ready(function(){
	$.ajax({ 
		type: "get", 
		url: "Device?method=loginUserDeptName", 
		dataType: "json", 
		success: function (data) { 
			if (data.flag == "0")
			{	
				var modalityMap = data.modalityMap;
				var deptName = modalityMap["DeptName"];
				for(var i=0;i<deptName.length;i++){
					$(".deptName").append("<option>"+deptName[i]+"</option>")
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
})