$(function(){
	// 默认显示前一天时间
	function getCurrentDay() {
		  var date = new Date()
		  var year = ''
		  var month = ''
		  var day = ''
		 
		  year = date.getFullYear()
	      month = parseInt(date.getMonth())
	      day = parseInt(date.getDate()) - 1
		  
	      if(day == '0'){
	    	  
	    	  var day = new Date(year, month, 0)
	    	  if (month < 10) {
			        month = '0' + month
			      }
			  if (day < 10) {
			        day = '0' + day
			      }
	    	  return  year + '-' + (month) + '-' + day.getDate()
	    	  
	      }else{
	    	  var month1 = month + 1
	    	  if (month1 < 10) {
	    		  month1 = '0' + month1
			      }
			  if (day < 10) {
			        day = '0' + day
			      }
			  return year + '-' + month1 + '-' + day
	      }
		  
	    }
	    var startTime = getCurrentDay()
	    var beforeTime = getCurrentDay()
		$("#sdate1").val(getCurrentDay()) 
	    $("#edate1").val(getCurrentDay()) 
	  /*
		 * // 获取上个月第一天跟最后一天 var nowdays = new Date(); var year =
		 * nowdays.getFullYear(); var month = nowdays.getMonth(); if(month==0){
		 * month = 12; year = year-1; } if(month<10){ month = '0'+month; } var
		 * myDate = new Date(year,month,0); var startTime =
		 * year+'-'+month+'-01'; //上个月第一天 var beforeTime =
		 * year+'-'+month+'-'+myDate.getDate();//上个月最后一天
		 * console.log(startTime,beforeTime) $("#sdate1").val(startTime);
		 * $("#edate1").val(beforeTime);
		 */
  // 获取审核医生图像质量数据
  function getQuality(timeRange){
    var echartDoctorWork1 = echarts.init(document.getElementById('doctor-image'));
    var option = {
      title: {
        text: ''
      },
      tooltip: {
          trigger: 'axis',
          formatter: function(params){
            let newParams = [];
            let tooltipString = [];
            newParams = [...params];
            newParams.sort((a,b) => {return b.value - a.value});
            newParams.forEach((p) => {
            	const cont = p.marker + ' ' + p.name +  ': ' + p.value  + '<br/>';
              tooltipString.push(cont);
            });
            return tooltipString.join('');
          },
          axisPointer:{
      		  // type: 'none'
      		  type: 'shadow'
      	  }
      },
      legend: {
          data: ''
      },
      grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
      },
      toolbox: {
	        show: true,
	        orient: 'horizontal',
	        left: 'right',
	        top: 'auto',
	        feature: {
	            dataZoom: {
	                yAxisIndex: 'none'
	            },
	            dataView: {readOnly: false},
	            magicType: {type: ['line', 'bar']},
	            restore: {},
	            saveAsImage: {}
	        }
	      },
      xAxis: {
          type: 'category',
          boundaryGap: true,
          data: ''
      },
      yAxis: {
          type: 'value',
          name: '',
          scale: false   // y轴数据,根据数据的最大最小之进行计算
      },
      series: ''
    }

    if(!timeRange){
    	startTime = startTime
        beforeTime = beforeTime
    }else{
      startTime = timeRange.slice(0,10)
      beforeTime = timeRange.slice(13,23)
    }
    console.log("beforeTime="+beforeTime,"startTime="+startTime)
  
    // 获取页面等级、设备类型、时间颗粒度
    let levelStr = $('#level-doctor').find('option:selected').val() // 等级
    let deviceStr = $('#device').find('option:selected').val() // 设备
    
    // 调用后台数据
    $.ajax({
    	type: 'get',
        url: path+'?method=ReviewDoctorImageNum',
        data: {"Grade":levelStr,"Modality":deviceStr,"startDate":startTime,"endDate":beforeTime},
        contentType: "application/json; charset=utf-8",
    dataType: 'json',
    success: function(res){
      var data = res.value
      if(data.length < 2){
    	  alert("当前范围内没有数据!");
    	  return;
      }
      if(deviceStr == null || deviceStr == ''){
      	deviceStr = "全部"
      }
      var title = deviceStr + ' ' + levelStr + '图像统计表'
      var series = [];  // 多组柱状图数据
      var docArr = [];  // 医生名称数组

      for(var i=1;i<data.length;i++){
        // push 医生数组
        docArr.push(Object.keys(data[i]).toString())
        // push 多组柱状图数据
        series.push({
          name: Object.keys(data[i]).toString(),
          type: 'bar',
          stack: '总量'+i,
          data: Object.values(data[i]).toString().split(','),
          markPoint: {
              data: [
                  {type: 'max', name: '最大值'},
                  {type: 'min', name: '最小值'}
              ]
          },
        markLine : {
      　　　　　　data : [
      　　　　　　　　{type : 'average', name: '平均值'}
      　　　　　　]
      　　　　}
          
        })
        // console.log(Object.values(data[i]).toString())
      }

      console.log(docArr,series)

      // 标题
      option.title.text = title
      // 副标题
// option.yAxis.name = levelStr + '级报告百分比(%)'
      option.yAxis.name = '数量'
      // 赋值时间数组
      option.xAxis.data = Object.values(data[0]).toString().split(',')
      // console.log(Object.values(data[1]), typeof Object.values(data[1]))

      // 赋值后台返回医生名称数组
      option.legend.data = docArr

      // 赋值柱状图数组
      option.series = series

      echartDoctorWork1.setOption(option,true)
    }
    })

  }

  // 获取检查技师图像质量数据
  function getTechQuality(timeRange){
	  
	  var date = getCurrentDay();
	
	  var startTime = date; 
	  var beforeTime = date;
	  console.log(startTime,beforeTime)
	  
    var echartTechImage = echarts.init(document.getElementById('technician-image'));
    var option = {
      title: {
        text: ''
      },
      tooltip: {
          trigger: 'axis',
          formatter: function(params){
            let newParams = [];
            let tooltipString = [];
            newParams = [...params];
            newParams.sort((a,b) => {return b.value - a.value});
            newParams.forEach((p) => {
            	const cont = p.marker + ' ' + p.name +  ': ' + p.value  + '<br/>';
              tooltipString.push(cont);
            });
            return tooltipString.join('');
          },
          axisPointer:{
      		  // type: 'none'
      		  type: 'shadow'
      	  }
      },
      legend: {
          data: ''
      },
      grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
      },
      toolbox: {
	        show: true,
	        orient: 'horizontal',
	        left: 'right',
	        top: 'auto',
	        feature: {
	            dataZoom: {
	                yAxisIndex: 'none'
	            },
	            dataView: {readOnly: false},
	            magicType: {type: ['line', 'bar']},
	            restore: {},
	            saveAsImage: {}
	        }
	      },
      xAxis: {
          type: 'category',
          boundaryGap: true,
          data: ''
      },
      yAxis: {
          type: 'value',
          name: '',
          scale: false   // y轴数据,根据数据的最大最小之进行计算
      },
      series: ''
    }
    
    if(!timeRange){
    	startTime = startTime
        beforeTime = beforeTime
    }else{
      startTime = timeRange.slice(0,10)
      beforeTime = timeRange.slice(13,23)
    }
    
    // 获取页面等级、设备类型、时间颗粒度、时间段
    let levelStr = $('#level-tech').find('option:selected').val() // 等级
    let deviceStr = $('#device1').find('option:selected').val()
    
    // 调用后台数据
    $.ajax({
    	type: 'get',
        url: path+'?method=ReviewDoctorReportNum',
        data: {"Grade":levelStr,"Modality":deviceStr,"startDate":startTime,"endDate":beforeTime},
        contentType: "application/json; charset=utf-8",
    dataType: 'json',
    success: function(res){
      var data = res.value
      if(data.length < 2){
    	  alert("当前范围内没有数据!");
    	  return;
      }
      if(deviceStr == null || deviceStr == ''){
      	deviceStr = "全部"
      }
      var title = deviceStr + ' ' + levelStr + '报告统计表'
      var series = [];  // 多组柱状图数据
      var docArr = [];  // 医生名称数组

      for(var i=1;i<data.length;i++){
        // push 医生数组
        docArr.push(Object.keys(data[i]).toString())
        // push 多组柱状图数据
        series.push({
          name: Object.keys(data[i]).toString(),
          type: 'bar',
          stack: '总量'+i,
          data: Object.values(data[i]).toString().split(','),
          //增加代码
          markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
          markLine : {
        　　　　　　data : [
        　　　　　　　　{type : 'average', name: '平均值'}
        　　　　　　]
        　　　　}
          
        })
        // console.log(Object.values(data[i]).toString())
      }

      console.log(docArr,series)

      // 标题
      option.title.text = title
      // 副标题
// option.yAxis.name = levelStr + '级报告百分比(%)'
      option.yAxis.name = '数量'
      // 赋值时间数组
      option.xAxis.data = Object.values(data[0]).toString().split(',')
      // console.log(Object.values(data[1]), typeof Object.values(data[1]))

      // 赋值后台返回医生名称数组
      option.legend.data = docArr

      // 赋值柱状图数组
      option.series = series

      echartTechImage.setOption(option,true)
    }
    })
  }

  // 选择时间段1 laydate
	laydate.render({
		elem: '#chart11',
		trigger: 'click',
    value: startTime,
    done: function(value, date ,endDate){
    		var valuedate = value + " - " +$("#chart12").val();
			getQuality(valuedate);
		}
	})
	laydate.render({
		elem: '#chart12',
		trigger: 'click',
    value: beforeTime,
    done: function(value, date ,endDate){
    		var valuedate = $("#chart11").val() + " - " + value;
			getQuality(valuedate);
		}
	})
  // 时间段1结束
  
  // 选择时间段2 laydate
	laydate.render({
		elem: '#chart21',
		trigger: 'click',
    value: startTime,
    done: function(value, date ,endDate){
      // console.log(2,value, date, endDate)
    		var valuedate = value + " - " +$("#chart22").val();
			getTechQuality(valuedate)
		}
	})
	laydate.render({
		elem: '#chart22',
		trigger: 'click',
    value: beforeTime,
    done: function(value, date ,endDate){
      // console.log(2,value, date, endDate)
    		var valuedate = $("#chart21").val() + " - " + value;
			getTechQuality(valuedate)
		}
	})
	// 时间段2结束

  // 审核医生切换等级
  $("#level-doctor").change(function(){
    getQuality();
  })
  // 审核医生切换设备
  $("#device").change(function(){
    getQuality();
  })
  // 审核医生切换时间颗粒度
  $("#time").change(function(){
    getQuality();
  })
  // 检查技师切换等级
  $("#level-tech").change(function(){
	  getTechQuality($("#chart21").val() + " - " +$("#chart22").val());
  })
  $("#vsecurity").change(function(){
	  getQuality();
  })
  
  $("#vsecurity1").change(function(){
	  getTechQuality($("#chart21").val() + " - " +$("#chart22").val());
  })
  // 检查技师切换设备
  $("#device1").change(function(){
	  getTechQuality($("#chart21").val() + " - " +$("#chart22").val());
  })
  // 检查技师切换时间颗粒度
  $("#time1").change(function(){
    getTechQuality($("#chart21").val() + " - " +$("#chart22").val());
  })

  // 首次加载审核医生图像质量
  if ($('#doctor-image').length){
    getQuality();
  }

  // 首次加载检查技师图像质量
  if ($('#technician-image').length){
    getTechQuality();
  }
});