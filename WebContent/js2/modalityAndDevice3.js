/**
 * id 为 modality device
 */

$(document).ready(function(){
	$.ajax({ 
		type: "get", 
		url: path+"?method=getModalityMap", 
		dataType: "json", 
		success: function (data) { 
			if (data.flag == "0")
			{	
				$("#device").html("");
				$("#device1").html("");
				$("#device").append("<option value=''>全部</option>");
				$("#device1").append("<option value=''>全部</option>");
				window.modalityMap = data.modalityMap;
				$.each(data.modalityMap, function(a, b){
					$("#device").append("<option value='"+a+"'>" + a + "</option>");
					$("#device1").append("<option value='"+a+"'>" + a + "</option>");
				});
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
});