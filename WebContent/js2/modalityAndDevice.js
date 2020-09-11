/**
 * 
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

					var index = 0;
					$.each(data.modalityMap, function(a, b){
						if (index == 0)
						{
							var modalityobj = createSelectedModalityElement(a);	
							$("#modalityLi").append(modalityobj);
							
							$("#device").empty();
							$.each(b, function(m, n){
								$("#device").append("<option value=''>" + n + "</option>");
							});
							
							window.g_device = a;
						}
						else
						{
							var modalityobj = createModalityElement(a);	
							$("#modalityLi").append(modalityobj);
						}
						 
						index += 1;
					});
					
					$("#device").change();
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
	});
	
	function createModalityElement(modality)
	{
		var str = "";
		str += "<a class='' href='javascript:void(0)' onclick='onButtonModality(this)'>";
		str += modality;
		str += "</a>";
		
		return str
	}
	
	function createSelectedModalityElement(modality)
	{
		var str = "";
		str += "<a class='selected' href='javascript:void(0)' onclick='onButtonModality(this)'>";
		str += modality;
		str += "</a>";
		
		return str
	}
	
	function onButtonModality(obj)
	{
		var currentModality = $(obj).text();
		if (window.g_device == currentModality)
		{
			return;
		}
		window.g_device = currentModality;

		$(".selected").removeClass("selected");
		$(obj).addClass("selected");
		
		var modalityMap = window.modalityMap;
		var deviceArray = modalityMap[currentModality];
		
		$("#device").empty();
		$.each(deviceArray, function(a, b){
			$("#device").append("<option value=''>" + b + "</option>");
		});
		
		$("#device").change();
		
	}
