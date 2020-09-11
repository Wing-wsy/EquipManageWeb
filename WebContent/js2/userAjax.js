/**
 * 
 */

$(document).ready(function(){
	$.ajax({ 
		type: "get", 
		url: path+"?method=getVSecurityMap", 
		dataType:"json",
		success: function (data) { 
			if (data.length > 0)
			{	
				console.log(data);
				$("#vsecurity,#vsecurity1").append("<option value=''>全部</option>");
				for(var i=0;i<data.length;i++){
					$("#vsecurity,#vsecurity1").append("<option value='"+data[i].name+"'>"+data[i].userName+"</option>");
				}
			}
		}, 
		error: function (XMLHttpRequest, textStatus, errorThrown) { 
			alert(errorThrown); 
		} 
	});
});