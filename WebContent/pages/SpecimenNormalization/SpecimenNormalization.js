$(function(){

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
	  // 获取标准标本数据
	  function getQuality(timeRange){
	    var echartDoctorWork1 = echarts.init(document.getElementById('standard-image'));
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
	              const cont = p.marker + ' ' + p.seriesName + '(' + p.name + ')' + ': ' + p.value  + '<br/>';
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
	          scale: false,   // y轴数据,根据数据的最大最小之进行计算
	          //增加代码
	          max: 100,    //设置最大值
            min: 0, 
	      },
	      series: ''
	    }

	    if(!timeRange){
//	    	var str = $("#chart1").val()
//	    	//设置默认时间日期
//	    	if(str == ''){
//	    		var today = new Date()
//	    		  var year = today.getFullYear()  // 获取年份
//	    		  var month = today.getMonth() + 1 // 获取月份
//	    		  
//	    		  var day = today.getDate()
//	    		  day = day<10?('0'+day):day
//
//	    		  //获取1年前
//	    		  var byear = year - 1  // 获取年份
//
//	    		  var bmonth = month + 1 // 获取月份
//	    		  if(bmonth == 13){
//	    		    byear++;
//	    		    bmonth = 1
//	    		  }
//	    		  bmonth = bmonth<10?('0'+bmonth):bmonth
//	    		  var bday = '01'
//
//	    		  //当前月份
//	    		  month = month<10?('0'+month):month
//
//	    		  //开始时间 默认今天
//	    		  var beforeTime = year +'-'+ month +'-'+ day
//	    		  //前30天
//	    		  var startTime = byear +'-'+ bmonth +'-'+ bday
//	    	}
//	    	startTime = str.slice(0,10)
//	        beforeTime = str.slice(13,23)
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
	    let timeStr = $('#time1').find('option:selected').val() // 年月周
	    var name = $("#vsecurity").find('option:selected').val()
	    
	    // 调用后台数据
	    $.ajax({
	    	type: 'get',
	        url: path+'?method=SpecimenNormalization',
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
	      var title = '固定-离体时间固定率统计表'
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
	      option.yAxis.name = '百分比(%)'
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
	
	  function getLargeQuality(timeRange){
			// 获取当天日期
			  /**
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
				var startTime = byear +'-'+ bmonth +'-'+ bday*/
			  
		        var echartTechImage = echarts.init(document.getElementById('large-image'));
			  
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
			              const cont = p.marker + ' ' + p.seriesName + '(' + p.name + ')' + ': ' + p.value  + '<br/>';
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
			          scale: false,   // y轴数据,根据数据的最大最小之进行计算
			          //增加代码
			          max: 100,    //设置最大值
		            min: 0, 
			      },
			      series: ''
			    }

			    if(!timeRange){
			    	var str = $("#chart2").val()
			    	if(str == ''){
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
			    	}else{
			    		startTime = str.slice(0,10)
				        beforeTime = str.slice(13,23)
			    	}
			    	

			    }else{
			      startTime = timeRange.slice(0,10)
			      beforeTime = timeRange.slice(13,23)
			    }
			    console.log("beforeTime="+beforeTime,"startTime="+startTime)
			  
			    // 获取页面等级、设备类型、时间颗粒度
			    let levelStr = $('#level-doctor').find('option:selected').val() // 等级
			    let deviceStr = $('#device').find('option:selected').val() // 设备
			    let timeStr = $('#time2').find('option:selected').val() // 年月周
			    var name = $("#vsecurity").find('option:selected').val()
			    
			    // 调用后台数据
			    $.ajax({
			    	type: 'get',
//			    	url: 'http://127.0.0.1:8080/EquipManageWeb/pages/pathology/zl.json',
			        url: path+'?method=LargeSpecimen',
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
//			      var title = deviceStr + ' ' + levelStr + '图像统计表'
			      var title = '大标本固定率统计表'
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
			      option.yAxis.name = '百分比(%)'
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

	  function getSmallQuality(timeRange){
		// 获取当天日期
		  /**
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
			var startTime = byear +'-'+ bmonth +'-'+ bday*/
		  
	        var echartTechImage = echarts.init(document.getElementById('small-image'));
		  
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
		              const cont = p.marker + ' ' + p.seriesName + '(' + p.name + ')' + ': ' + p.value  + '<br/>';
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

		    if(!timeRange){
		    	var str = $("#chart3").val()
		    	if(str == ''){
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
		    	}else{
		    		startTime = str.slice(0,10)
			        beforeTime = str.slice(13,23)
		    	}
		    	

		    }else{
		      startTime = timeRange.slice(0,10)
		      beforeTime = timeRange.slice(13,23)
		    }
		    console.log("beforeTime="+beforeTime,"startTime="+startTime)
		  
		    // 获取页面等级、设备类型、时间颗粒度
		    let levelStr = $('#level-doctor').find('option:selected').val() // 等级
		    let deviceStr = $('#device').find('option:selected').val() // 设备
		    let timeStr = $('#time3').find('option:selected').val() // 年月周
		    var name = $("#vsecurity").find('option:selected').val()
//		    console.log(levelStr,deviceStr,timeStr,startTime,beforeTime)
		    
		    // 调用后台数据
		    $.ajax({
		    	type: 'get',
//		    	url: 'http://127.0.0.1:8080/EquipManageWeb/pages/pathology/zl.json',
		        url: path+'?method=SmallSpecimen',
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
//		      var title = deviceStr + ' ' + levelStr + '图像统计表'
		      var title = '小标本固定率统计表'
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
		      option.yAxis.name = '百分比(%)'
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
		elem: '#chart1',
		trigger: 'click',
    value: startTime + ' - ' + beforeTime,
    range: true,
    done: function(value, date ,endDate){
			getQuality(value)
		}
	})
  // 时间段1结束
  
  // 选择时间段2 laydate
	laydate.render({
		elem: '#chart2',
		trigger: 'click',
    value: startTime + ' - ' + beforeTime,
    range: true,
    done: function(value, date ,endDate){
      // console.log(2,value, date, endDate)
			getLargeQuality(value)
		}
	})
	// 时间段2结束
	
	 // 选择时间段3 laydate
	laydate.render({
		elem: '#chart3',
		trigger: 'click',
    value: startTime + ' - ' + beforeTime,
    range: true,
    done: function(value, date ,endDate){
      // console.log(2,value, date, endDate)
			getSmallQuality(value)
		}
	})
	// 时间段3结束

  // 标准固定率切换时间颗粒度
  $("#time1").change(function(){
    getQuality();
  })

  //大标本切换时间颗粒度
  $("#time2").change(function(){
	getLargeQuality();
  })
  
    //小标本切换时间颗粒度
  $("#time3").change(function(){
	getSmallQuality();
  })

  // 首次加载标准固定率
  if ($('#standard-image').length){
    getQuality();
  }

  // 首次加载大标本  取材时间-固定时间
  if ($('#large-image').length){
    getLargeQuality();
  }
  
  // 首次加载小标本  取材时间-固定时间
  if ($('#small-image').length){
    getSmallQuality();
  }
});