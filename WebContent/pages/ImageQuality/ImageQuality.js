$(function(){
/**
	// 获取当天日期
	  var today = new Date()
	  var year = today.getFullYear()  // 获取年份
	  var month = today.getMonth() + 1 // 获取月份
	  
	  var day = today.getDate()
	  day = day<10?('0'+day):day

	  //获取1年前
	  var byear = year - 1  // 获取年份

	  var bmonth = month + 1 // 获取月份
	  if(bmonth == 13){
	    byear++;
	    bmonth = 1
	  }
	  bmonth = bmonth<10?('0'+bmonth):bmonth
	  var bday = '01'

	  //当前月份
	  month = month<10?('0'+month):month

	  //开始时间 默认今天
	  var beforeTime = year +'-'+ month +'-'+ day
	  //前30天
	  var startTime = byear +'-'+ bmonth +'-'+ bday
*/
	  // 获取上个月第一天跟最后一天
	  var nowdays = new Date(); 
	  var year = nowdays.getFullYear();
	  var month = nowdays.getMonth();
	  if(month==0){
	      month = 12;
	      year = year-1;
	  }
	  if(month<10){
	      month = '0'+month;
	  }
	  var myDate = new Date(year,month,0);
	  var startTime = year+'-'+month+'-01'; //上个月第一天
	  var beforeTime = year+'-'+month+'-'+myDate.getDate();//上个月最后一天
	  console.log(startTime,beforeTime)
	  $("#sdate1").val(startTime);
	  $("#edate1").val(beforeTime);
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
              const cont = p.marker + ' ' + p.seriesName + ': ' + p.value + '<br/>';
              tooltipString.push(cont);
            });
            return tooltipString.join('');
          },
          axisPointer:{
      		  //type: 'none'
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
        orient: 'vertical',
        left: 'right',
        top: 'center'
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
    let timeStr = $('#time').find('option:selected').val() // 年月周
    var name = $("#vsecurity").find('option:selected').val()
    console.log(levelStr,deviceStr,timeStr,startTime,beforeTime)
    
    // 调用后台数据
    $.ajax({
    	type: 'get',
        url: path+'?method=AuReportDoctorImageQuality',
        data: {"Grade":levelStr,"Modality":deviceStr,"startDate":startTime,"endDate":beforeTime,"DateType":timeStr,"name":name},
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
        //push 医生数组
        docArr.push(Object.keys(data[i]).toString())
        //push 多组柱状图数据
        series.push({
          name: Object.keys(data[i]).toString(),
          type: 'bar',
          stack: '总量'+i,
          data: Object.values(data[i]).toString().split(',')
          
        })
        // console.log(Object.values(data[i]).toString())
      }

      console.log(docArr,series)

      // 标题
      option.title.text = title
      // 副标题
      option.yAxis.name = levelStr + '级报告百分比(%)'
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
	  /**
	// 获取当天日期
	    var today = new Date()
	    var year = today.getFullYear()  // 获取年份
	    var month = today.getMonth() + 1 // 获取月份
	    
	    var day = today.getDate()
	    day = day<10?('0'+day):day

	    //获取1年前
	    var byear = year - 1  // 获取年份

	    var bmonth = month + 1 // 获取月份
	    if(bmonth == 13){
	      byear++;
	      bmonth = 1
	    }
	    bmonth = bmonth<10?('0'+bmonth):bmonth
	    var bday = '01'

	    //当前月份
	    month = month<10?('0'+month):month

	    //开始时间 默认今天
	    var beforeTime = year +'-'+ month +'-'+ day
		//前30天
		var startTime = byear +'-'+ bmonth +'-'+ bday
	  */
	// 获取上个月第一天跟最后一天
	  var nowdays = new Date(); 
	  var year = nowdays.getFullYear();
	  var month = nowdays.getMonth();
	  if(month==0){
	      month = 12;
	      year = year-1;
	  }
	  if(month<10){
	      month = '0'+month;
	  }
	  var myDate = new Date(year,month,0);
	  var startTime = year+'-'+month+'-01'; //上个月第一天
	  var beforeTime = year+'-'+month+'-'+myDate.getDate();//上个月最后一天
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
              const cont = p.marker + ' ' + p.seriesName + ': ' + p.value + '<br/>';
              tooltipString.push(cont);
            });
            return tooltipString.join('');
          },
          axisPointer:{
      		  //type: 'none'
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
        orient: 'vertical',
        left: 'right',
        top: 'center'
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
    let timeStr = $('#time1').find('option:selected').val()
    var name = $("#vsecurity1").find('option:selected').val()
    console.log('等级',levelStr,'设备',deviceStr,'时间字符串',timeStr,startTime,beforeTime)
    
    // 调用后台数据
    $.ajax({
    	type: 'get',
        url: path+'?method=technicianImageQuality',
        data: {"Grade":levelStr,"Modality":deviceStr,"startDate":startTime,"endDate":beforeTime,"DateType":timeStr,"name":name},
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
      var title = deviceStr + ' ' + levelStr + '图像质量统计表'
      var series = [];  // 多组柱状图数据
      var docArr = [];  // 医生名称数组

      for(var i=1;i<data.length;i++){
        //push 医生数组
        docArr.push(Object.keys(data[i]).toString())
        //push 多组柱状图数据
        series.push({
          name: Object.keys(data[i]).toString(),
          type: 'bar',
          stack: '总量'+i,
          data: Object.values(data[i]).toString().split(',')
          
        })
        // console.log(Object.values(data[i]).toString())
      }

      console.log(docArr,series)

      // 标题
      option.title.text = title
      // 副标题
      option.yAxis.name = levelStr + '级报告百分比(%)'
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