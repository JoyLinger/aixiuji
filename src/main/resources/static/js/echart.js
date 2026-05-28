function drawEChart(mySelect, type) {
  console.log("type="+type);
  // 点击后将默认选中第一个option,并将其value和text修改为刚刚用户点击的option的value和text
  if (mySelect && typeof mySelect === 'HTMLSelectElement') {
    console.log("mySelect="+mySelect);
    var val = mySelect.options[mySelect.selectedIndex].value;
    var text = mySelect.options[mySelect.selectedIndex].text;
    console.log("val="+val);
    console.log("text="+text);
    mySelect.selectedIndex = 0;
    mySelect.options[mySelect.selectedIndex].value = val;
    mySelect.options[mySelect.selectedIndex].text = text;
  }
  console.log( "Handler for 时间范围.click() called." );
  var durPeriod = document.getElementById("durPeriod").value;
  var durVal = document.getElementById("durVal").value;
  console.log("durPeriod="+durPeriod);
  console.log("durVal="+durVal);
  var myChart;
  var dom = document.getElementById("myChart");
  myChart = echarts.init(dom);
  myChart.showLoading({
    text: "图表数据正在努力加载...",
  });
  console.log("myChart="+myChart);
  $.ajax({
	type: "get",
	//异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
	async : true,
	//请求地址
    url : "/filter/stats/" + type,
	//请求参数
//	data: {"durPeriod": durPeriod, "defaultDays": 30},
	data: {"durPeriod": durPeriod, "durVal": durVal},
    //返回数据形式为json
    dataType : "json",
    //请求成功时执行该函数内容，jsonObj即为服务器返回的json对象
    success : function(jsonObj) {
      if (jsonObj && typeof jsonObj === 'object') {
        console.log("jsonObj="+jsonObj);
        console.log("JSON.stringify(jsonObj)="+JSON.stringify(jsonObj));
//        array = $.parseJSON(jsonObj);
        var option = json2option(jsonObj, type);
        console.log("option="+option);
        console.log("JSON.stringify(option)="+JSON.stringify(option));
        myChart.setOption(option, true);
        myChart.hideLoading();
      }
    },
    error : function(errorMsg) {
      console.log("JSON.stringify(errorMsg)="+JSON.stringify(errorMsg));
      alert("哎呀，图表请求数据失败咯!");
    }
  });
}

//根据数据生成chart option: 客户统计
function json2option(jsonObj, type){
  var years = jsonObj.xAxis_data;
  var durPeriod = jsonObj.durPeriod; // 获取统计周期：1=天，2=月，3=年
  
  // 处理每个series
  var allSeries;
  
  if(type === 'income') {
    // 收入走势：应用特殊逻辑
    allSeries = jsonObj.series.map(function(item) {
      if(item.name === '收入合计' || item.name === '顾客合计') {
        // 为合计series添加formatter
        return Object.assign({}, item, {
          label: {
            show: true,
            position: 'top',
            fontSize: 14,
            fontWeight: 'bold',
            formatter: function(params) {
              // 按年统计：保持原来的格式
              if(durPeriod === 3) {
                return years[params.dataIndex] + (item.name === '收入合计' ? '收入:' : '顾客:') + params.value;
              } else {
                // 按日或按月统计：收入前面加￥，只显示数字
                if(item.name === '收入合计') {
                  return '￥' + params.value;
                } else {
                  return params.value;
                }
              }
            }
          },
          itemStyle: {
            opacity: 0.0
          }
        });
      } else {
        // 判断是否是按年统计的月份数据（包含"月"），或者是按日/月的普通数据
        var isMonthData = item.name.indexOf('月') !== -1;
        
        if(isMonthData) {
          // 按年统计的月份数据
          return Object.assign({}, item, {
            barGap: 0,
            barCategoryGap: '20%',
            barWidth: '40%',
            label: {
              show: durPeriod === 3, // 只有按年统计才显示月份标签
              position: 'inside',
              formatter: function(params) {
                var monthName = item.name;
                var value = params.value;
                if (value > 0) {
                  return monthName + '：' + value;
                } else {
                  return '';
                }
              },
              fontSize: 10
            }
          });
        } else {
          // 按日或按月统计的普通数据（收入或顾客人数）
          return Object.assign({}, item, {
            barGap: 0,
            barCategoryGap: '20%',
            barWidth: '40%',
            label: {
              show: true,
              position: 'top',
              fontSize: 12,
              formatter: function(params) {
                var value = params.value;
                if (value > 0) {
                  // 收入前面加￥
                  if(item.name === '收入') {
                    return '￥' + value;
                  } else {
                    return value;
                  }
                } else {
                  return '';
                }
              }
            }
          });
        }
      }
    });
  } else {
    // 顾客走势：保持原来的逻辑不变
    allSeries = jsonObj.series;
  }
  
  // 指定图表的配置项和数据
  var option = {
    title: {
      text: jsonObj.title
    },
    tooltip: {
      trigger: type === 'income' ? 'item' : 'axis',
      formatter: function(params) {
        var year = '';
        var result = '';
        
        if(type === 'income') {
          // 收入走势
          year = years[params.dataIndex];
          
          if(durPeriod === 3) {
            // 按年统计
            if(params.seriesName === '收入合计' || params.seriesName === '顾客合计') {
              return year + '年<br/>' + params.seriesName.replace('合计', '') + ': ' + params.value;
            }
            
            var yearIncomeTotal = 0;
            var yearPopulationTotal = 0;
            
            for(var i = 0; i < jsonObj.series.length; i++) {
              var series = jsonObj.series[i];
              if(series.name.indexOf('收入') >= 0 && series.name !== '收入合计') {
                yearIncomeTotal += (series.data[params.dataIndex] || 0);
              }
              if(series.name.indexOf('顾客') >= 0 && series.name !== '顾客合计') {
                yearPopulationTotal += (series.data[params.dataIndex] || 0);
              }
            }
            
            result = year + '年<br/>';
            result += params.seriesName + ': ' + params.value + '<br/>';
            result += '---合计---<br/>';
            result += '收入: ' + yearIncomeTotal + '<br/>';
            result += '顾客人数: ' + yearPopulationTotal;
          } else {
            // 按日或按月统计
            var totalIncome = 0;
            var totalPopulation = 0;
            
            for(var i = 0; i < jsonObj.series.length; i++) {
              var series = jsonObj.series[i];
              if(series.name === '收入') {
                totalIncome += (series.data[params.dataIndex] || 0);
              }
              if(series.name === '顾客人数') {
                totalPopulation += (series.data[params.dataIndex] || 0);
              }
            }
            
            result = year + '<br/>';
            result += '收入：￥' + totalIncome + '<br/>';
            result += '人数：' + totalPopulation;
          }
        } else {
          // 顾客走势：显示所有系列的数据
          year = years[params[0].dataIndex];
          result = year + '<br/>';
          
          for(var i = 0; i < params.length; i++) {
            var param = params[i];
            result += param.seriesName + ': ' + param.value + '<br/>';
          }
        }
        
        return result;
      }
    },
    legend: {
      data: jsonObj.legend
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true
    },
    toolbox: {
      show: true, //是否显示工具栏组件
      right: "5%",
      feature: {
        dataZoom: {
          yAxisIndex: 'none',
          title: { zoom: '区域缩放', back: '区域缩放还原' }
        },
        dataView: { readOnly: false, title: '数据视图' },
        magicType: {
          //动态类型切换
          type: ["line", "bar"],
          title: { line: '切换为折线图', bar: '切换为柱状图' }
        },
        restore: { title: '还原' }, //重置
        saveAsImage: { title: '保存为图片' }
      }
    },
    xAxis: {
      type: 'category',
      name: '时间',
      nameLocation: 'end',
      axisTick: { alignWithLabel: true },
      boundaryGap: true,
      axisLabel: {
        // 格式化横轴坐标时间刻度
//        formatter: {
//          year: "{yyyy}年",
//          month: "{M}月",
//          day: "{MM}/{dd}",
//          hour: "{HH}:{mm}",
//          minute: "{HH}:{mm}",
//          second: "{HH}:{mm}:{ss}",
//          millisecond: "{hh}:{mm}:{ss} {SSS}",
//          none: "{yyyy}-{MM}-{dd} {hh}:{mm}:{ss} {SSS}"
//        }
      },
      data: jsonObj.xAxis_data
    },
    // 当 x 轴（水平坐标轴）跨度很大，可以采用区域缩放方式灵活显示数据内容。
    dataZoom: {
      id: 'dataZoomX',
      type: 'slider',
//      type: 'inside',
      xAxisIndex: [0],
      filterMode: 'filter', // 设定为 'filter' 从而 X 的窗口变化会影响 Y 的范围。
      start: 0,
      end: 100
//      year: {
//        start: 90,
//        end: 100,
//        minValueSpan: 1000 * 3600 * 24 * 30 * 7,
//        maxValueSpan: 1000 * 3600 * 24 * 30 * 7
//      },
//      month: {
//        start: 90,
//        end: 100,
//        minValueSpan: 1000 * 3600 * 24 * 7,
//        maxValueSpan: 1000 * 3600 * 24 * 7
//      },
//      week: {
//        start: 90,
//        end: 100,
//        minValueSpan: 1000 * 3600 * 24 * 7,
//        maxValueSpan: 1000 * 3600 * 24 * 7
//      },
//      day: {
//        start: 90,
//        end: 100
//        minValueSpan: 1000 * 3600 * 7,
//        maxValueSpan: 1000 * 3600 * 7
//      }
    },
    yAxis: (function() {
      // 计算每个y轴的最大值，实现自适应
      var yAxisData = jsonObj.yAxis_data;
      
      if(type === 'income') {
        // 收入走势图的逻辑
        var incomeMax = 0;
        var populationMax = 0;
        
        for(var i = 0; i < jsonObj.series.length; i++) {
          var series = jsonObj.series[i];
          if(series.name.indexOf('收入') >= 0 && series.name !== '收入合计') {
            for(var j = 0; j < series.data.length; j++) {
              if(series.data[j] > incomeMax) {
                incomeMax = series.data[j];
              }
            }
          }
          if(series.name.indexOf('顾客') >= 0 && series.name !== '顾客合计') {
            for(var j = 0; j < series.data.length; j++) {
              if(series.data[j] > populationMax) {
                populationMax = series.data[j];
              }
            }
          }
        }
        
        // 计算每个年份的合计最大值
        var yearIncomeTotalMax = 0;
        var yearPopulationTotalMax = 0;
        
        for(var i = 0; i < years.length; i++) {
          var yearIncomeTotal = 0;
          var yearPopulationTotal = 0;
          for(var j = 0; j < jsonObj.series.length; j++) {
            var series = jsonObj.series[j];
            if(series.name.indexOf('收入') >= 0 && series.name !== '收入合计') {
              yearIncomeTotal += (series.data[i] || 0);
            }
            if(series.name.indexOf('顾客') >= 0 && series.name !== '顾客合计') {
              yearPopulationTotal += (series.data[i] || 0);
            }
          }
          if(yearIncomeTotal > yearIncomeTotalMax) {
            yearIncomeTotalMax = yearIncomeTotal;
          }
          if(yearPopulationTotal > yearPopulationTotalMax) {
            yearPopulationTotalMax = yearPopulationTotal;
          }
        }
        
        // 设置y轴最大值（使用合计最大值+10%的padding）
        if(yAxisData[0]) {
          yAxisData[0].max = Math.ceil(yearIncomeTotalMax * 1.1);
        }
        if(yAxisData[1]) {
          yAxisData[1].max = Math.ceil(yearPopulationTotalMax * 1.1);
        }
      } else {
        // 顾客走势图的逻辑：计算所有series的最大值
        var maxValue = 0;
        for(var i = 0; i < jsonObj.series.length; i++) {
          var series = jsonObj.series[i];
          for(var j = 0; j < series.data.length; j++) {
            if(series.data[j] > maxValue) {
              maxValue = series.data[j];
            }
          }
        }
        // 设置y轴最大值（使用最大值+10%的padding）
        if(yAxisData[0]) {
          yAxisData[0].max = Math.ceil(maxValue * 1.1);
          yAxisData[0].minInterval = 1; // 确保y轴刻度间隔至少为1，避免小数
        }
      }
      
      return yAxisData;
    })(),
    series: allSeries
  };
  
  return option;
}

function distinct_add_array(arr, obj){
  for(var i=0; i<arr.length; i++){
    if(arr[i] == obj){
      return arr;
    }else{
      break;
    }
  }
  arr[arr.length+1] = obj;
  return arr;
}