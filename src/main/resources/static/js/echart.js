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
        var option = json2option(jsonObj);
        console.log("option="+option);
        console.log("JSON.stringify(option)="+JSON.stringify(option));
        myChart.setOption(option);
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
function json2option(jsonObj){
  // 指定图表的配置项和数据
  return option = {
    title: {
      text: jsonObj.title
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: jsonObj.legend
    },
//    grid: {
//      left: '3%',
//      right: '4%',
//      bottom: '3%',
//      containLabel: true
//    },
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
    yAxis: jsonObj.yAxis_data,
    series: jsonObj.series
  };
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