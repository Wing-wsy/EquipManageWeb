$(function(){

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
  var startDate = year+'-'+month+'-01'; //上个月第一天
  var endDate = year+'-'+month+'-'+myDate.getDate();//上个月最后一天
  console.log(startDate,endDate)


  // 获取检查技师工作量数据
  function getTechData(timeRange){
    var echartDoctorWork = echarts.init(document.getElementById('checkTechnician'));
    var option = {
      color: ['#EDAFDA', '#59C4E6', '#6F9BBA', '#6E83A3', '#cee4ae', '#deb068','#d3ccd6', '#d1b068','#d31cd6'],   // 柱状颜色
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      legend: {
        data: ''   // 所有设备数据，数组
      },
      toolbox: {
        show: true,
        orient: 'vertical',
        left: 'right',
        top: 'center',
        feature: {
          magicType: {show: true, type: ['stack']},
          restore: {show: true}
        }
      },
      calculable: true,
      xAxis: {
          type: 'category',
          name: '医生名称',
          "axisLabel":{
             interval: 0,
             rotate:45
          }, 
          axisTick: {show: false},
          data: ''  // 所有医生名称， 数组
        
    },
      yAxis: [
        {
          type: 'value',
          name: '报告工作量'
        }
      ],
      series: ''
    }

    
    
    if(!timeRange){
      startTime = endDate
      beforeTime = startDate
    }else{
      startTime = timeRange.slice(13,23)
      beforeTime = timeRange.slice(0,10)
    }

    console.log(startTime,beforeTime)   // 时间段

    $.ajax({
      type: 'get',
      url: path+'?method=technicianWorkload',
      data: {"startDate":beforeTime,"endDate":startTime},
      contentType: "application/json; charset=utf-8",
      dataType: 'json',
      success: function(res){
        var data = res.value

        var series = [];  // 多组柱状图数据
        var deviceArr = [];  // 设备数组
        
        for(var i=2;i<data.length;i++){
          //push 设备数组
          deviceArr.push(Object.keys(data[i]).toString())
          // push 多组柱状图数据
          series.push({
            name: Object.keys(data[i]).toString(),
            type: 'bar',
            barGap: 0,
            label: Object.keys(data[i]).toString(),
            data: Object.values(data[i]).toString().split(','),
            markLine: {
              symbol: 'none',
              itemStyle: {
                normal: {
                  label: {
                    show: true
                  }
                }
              },
              data: [{type : 'average', name: 'CT平均值'}]
            }
          })
        }

        // 赋值医生数组
        option.xAxis.data = Object.values(data[1]).toString().split(',')
        // 赋值设备数组
        option.legend.data = deviceArr
        // 赋值柱状图数组
        option.series = series
        //由于医生太多，设置隔一个人隐藏几个医生名字
        if(data[1]["name"].length>80){
        	option.xAxis.axisLabel.interval = 2;
        }else if(data[1]["name"].length>40){
        	option.xAxis.axisLabel.interval = 1;
        }
        echartDoctorWork.setOption(option)
      },
      error: function(res){
        console.log(res)
      }
    })
  }

  // 选择时间段1 laydate
	laydate.render({
    elem: '#chart1',
		trigger: 'click',
    value: startDate + ' - ' + endDate,
    range: true,
    done: function(value, date ,endDate){
      // console.log("切换时间" ,value)
      getTechData(value)
    }
  })

  // 首次加载检查技师工作量
  if ($('#checkTechnician').length){
    getTechData();
  }

});