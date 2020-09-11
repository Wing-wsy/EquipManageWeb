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
	
  // 获取审核医生报告质量数据
  function getCheckDoctor(timeRange){
    var echartDoctorWork1 = echarts.init(document.getElementById('check-doctor'));
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
            //name: '',
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
    var levelStr = $('#level-doctor').find('option:selected').val()
    var deviceStr = $('#device').find('option:selected').val()
    var timeStr = $('#time').find('option:selected').val()
    var name = $("#vsecurity").find('option:selected').val()
    console.log(levelStr,deviceStr,timeStr,startTime,beforeTime)
    
    // 调用后台数据
    $.ajax({
      type: 'get',
      url: path+'?method=AuReportDoctorReporQuality',
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
        var title = deviceStr + ' ' + levelStr + '报告统计表'
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
	
	
  // 获取报告医生报告质量数据
  function getReportDoctor(timeRange){
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
    var echartTechImage = echarts.init(document.getElementById('report-doctor'));
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
  var levelStr = $('#level-doctor1').find('option:selected').val()
  var deviceStr = $('#device1').find('option:selected').val()
  var timeStr = $('#time1').find('option:selected').val()
  var name = $("#vsecurity1").find('option:selected').val()
  console.log(levelStr,deviceStr,timeStr,startTime,beforeTime)
  
  // 调用后台数据
  $.ajax({
	type: 'get',
    url: path+'?method=ReportDoctorReporQuality',
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
      var title = deviceStr + ' ' + levelStr + '报告统计表'
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

  // 选择时间段1 laydate2;
	laydate.render({
		elem: '#chart11',
		trigger: 'click',
	    value: startTime,
	    done: function(value, date ,endDate){
	    	var valuedate = value + " - " +$("#chart12").val();
	    	getCheckDoctor(valuedate);
		}
	})
	laydate.render({
		elem: '#chart12',
		trigger: 'click',
	    value: beforeTime,
	    done: function(value, date ,endDate){
	    console.log(1,value, date, endDate)
		    var valuedate = $("#chart11").val() + " - " + value;
	    	getCheckDoctor(valuedate);
		}
	})
  // 时间段1结束
  
  // 选择时间段2 laydate
	laydate.render({
		elem: '#chart21',
		trigger: 'click',
	    value: startTime,
	    done: function(value, date ,endDate){
	    	var valuedate = value + " - " +$("#chart22").val();
	    	getReportDoctor(valuedate);
		}
	})
	laydate.render({
		elem: '#chart22',
		trigger: 'click',
	    value: beforeTime,
	    done: function(value, date ,endDate){
	    	var valuedate = $("#chart21").val() + " - " + value;
	    	getReportDoctor(valuedate);
		}
	})
	// 时间段2结束

  // 首次加载审核医生图像质量
  if ($('#check-doctor').length){
    getCheckDoctor();
  }

  // 质量刷新
  $("#level-doctor").change(function(){
      getCheckDoctor($("#chart11").val()+" - "+$("#chart12").val())
  })
  // 改变设备
  $("#device").change(function(){
    getCheckDoctor($("#chart11").val()+" - "+$("#chart12").val())
  })
  // 改变时间颗粒
  $("#time").change(function(){
    getCheckDoctor($("#chart11").val()+" - "+$("#chart12").val())
  })
  //改变医生刷新
  $("#vsecurity").change(function(){
	  getCheckDoctor($("#chart11").val()+" - "+$("#chart12").val());
  })
  

  // 首次加载报告医生报告质量
  if ($('#report-doctor').length){
    getReportDoctor();
  }
  // 质量刷新
  $("#level-doctor1").change(function(){
    getReportDoctor($("#chart21").val()+" - "+$("#chart22").val());
  })
  // 改变设备
  $("#device1").change(function(){
    getReportDoctor($("#chart21").val()+" - "+$("#chart22").val());
  })
  // 改变时间颗粒
  $("#time1").change(function(){
    getReportDoctor($("#chart21").val()+" - "+$("#chart22").val());
  })
  //改变医生刷新
  $("#vsecurity1").change(function(){
    getReportDoctor($("#chart21").val()+" - "+$("#chart22").val());
  })
});