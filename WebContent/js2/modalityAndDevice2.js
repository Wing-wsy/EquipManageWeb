/**
 * id 为 modality device
 */

$(document).ready(function(){
	$.ajax({ 
		type: "get", 
		url: "Device?method=getModalityMap", 
		dataType: "json", 
		success: function (data) { 
			if (data.flag == "0")
			{
				window.modalityMap = data.modalityMap;
				
				$.each(data.modalityMap, function(a, b){
					$("#modality").append("<option value=''>" + a + "</option>");
					$(".modality").append("<option value='"+ a +"'>" + a + "</option>");
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
	
	$("#modality").on("change", function(){

		$("#device").empty();

		var modality = $(this).find("option:selected").text();
		if (modality == "全部")
		{
			$("#device").append("<option value=''>" + "全部" + "</option>");
		}
		else
		{
			var map = window.modalityMap;
			var deviceList = map[modality];
			$.each(deviceList, function (a, b){
				$("#device").append("<option value=''>" + b + "</option>");
			});
		}
	});
});