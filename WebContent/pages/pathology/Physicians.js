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
	  var startTime = year+'-'+month+'-01'; //上个月第一天
	  var beforeTime = year+'-'+month+'-'+myDate.getDate();//上个月最后一天
	  console.log(startTime,beforeTime)
	  $("#sdate1").val(startTime);
	  $("#edate1").val(beforeTime);
  // 病理百张床位统计数据
  function getQuality(timeRange){
    var echartDoctorWork1 = echarts.init(document.getElementById('Physicians-image'));
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
            	const cont = p.marker + ' '  + p.name  + ': ' + p.value  + '<br/>';
              tooltipString.push(cont);
            });
            return tooltipString.join('');
          },
          axisPointer:{
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
          scale: false,   // y轴数据,根据数据的最大最小之进行计算
          //增加代码
          max: 100,    //设置最大值
          min: 0, 
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
  
    
    // 调用后台数据
    $.ajax({
    	type: 'get',
        url: path+'?method=Pathology',
        data: {},
        contentType: "application/json; charset=utf-8",
    dataType: 'json',
    success: function(res){
      var data = res.value
      if(data.length < 2){
    	  alert("当前范围内没有数据!");
    	  return;
      }
//      var title = deviceStr + ' ' + levelStr + '图像统计表'
      var title = '病理百张床位统计表'
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
      option.yAxis.name = '人数'
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

	//文本框焦点失去
	$("#chart1").blur(function(){
		 // 获取页面等级、设备类型、时间颗粒度
	    let levelStr = $('#level-doctor').find('option:selected').val() // 等级
	    let deviceStr = $('#device').find('option:selected').val() // 设备
	    let timeStr = $('#time').find('option:selected').val() // 年月周
	    var name = $("#vsecurity").find('option:selected').val()
		 var BedNum = $("#chart1").val()
		 if(BedNum == 0){
			 alert("请输入正确的床位数！");
			 return false;
		 }
	    var ex = /^\d+$/;
	    if (!ex.test(BedNum)) {
	    // 不为整数
	    	 alert("床位数只能输入正整数！");
			 return false;
	    }
		 var echartDoctorWork1 = echarts.init(document.getElementById('Physicians-image'));
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
		            	const cont = p.marker + ' '  + p.name  + ': ' + p.value  + '<br/>';
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
		          scale: false,   // y轴数据,根据数据的最大最小之进行计算
		          //增加代码
		          max: 100,    //设置最大值
		          min: 0, /**                                //最小值
		          splitNumber: 11,                 //11个刻度线，也就是10等分
		          nameTextStyle: {
		              color: '#fff',
		              padding: [0, 0, 0, 0]
		          }*/
		      },
		      series: ''
		    }
	    // 调用后台数据
	    $.ajax({
	    	type: 'get',
//	    	url: 'http://127.0.0.1:8080/EquipManageWeb/pages/pathology/zl.json',
	        url: path+'?method=Pathology',
	        data: {"BedNum":BedNum},
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
//	      var title = deviceStr + ' ' + levelStr + '图像统计表'
	      var title = '病理百张床位统计表'
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
	      option.yAxis.name = '人数'
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
});


  // 首次加载审核医生图像质量
  if ($('#Physicians-image').length){
    getQuality();
//    alert("默认病床数为100");
  }

});