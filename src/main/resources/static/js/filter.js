var arr = [ //模拟后台给的数据
  { name: "王小明", src: "/img/head.jpg", des: "成绩很好", sex: "m", age: 18 },
  { name: "王大海", src: "/img/head.jpg", des: "长得帅", sex: "m", age: 19 },
  { name: "刘小红", src: "/img/head.jpg", des: "莫名的喜感", sex: "f", age: 17 },
  { name: "孙小白", src: "/img/head.jpg", des: "一白遮三丑", sex: "f", age: 16 },
  { name: "刘小黑", src: "/img/head.jpg", des: "成绩很好", sex: "m", age: 20 }
];
var wra = document.getElementsByClassName("wra")[0];
var wb = wra.getElementsByClassName("wraBottom")[0];
var oUl = wb.getElementsByTagName("ul")[0];
var sex = document.getElementsByClassName('sex')[0];
var sexbtn = sex.getElementsByClassName("btn");
var inp = wra.getElementsByTagName("input")[0];

// 渲染页面
function renderPage(data) {
  oUl.innerHTML = "";
  data.forEach(function (ele, index, self) {
    //遍历数组里面的东西，取其中数据构建html结构，
    oUl.innerHTML += '<li><img src=' + ele.src + '><p class="name">' + ele.name + '</p><span>' + ele.age + '岁</span><p class="des">' + ele.des + '</p></li>';
  });
}
//renderPage(arr);



//绑定性别点击事件
var lastDefault = sexbtn[2]; //默认性别选项All
for(var i = 0; i < sexbtn.length; i++){
  (function(j){
    sexbtn[j].onclick = function(){
      sexbtn[j].className = "btn default";//点击时，给点击的按钮添加 css样式(default)
      lastDefault.className = "btn";// 赋给样式后，取消上一个btn的样式
      lastDefault = sexbtn[j];//赋给样式后，此次点击的btn 就成了过去

      store.dispatch({type: 'text', value: sexbtn[j].id});
//            state.sex = sexbtn[j].id;
//            var newArr = screenSex(arr, state.sex); //筛选性别执行后返回新数组
//            renderPage( screenInput(newArr, state.text));//利用新数组再次筛选搜索框，筛选后渲染页面
      }
  })(i)
}
//此方法用于执行执行screenInput 和 screenSex函数
function combineFilter(comfig){
  return function (data){ //这里data的值，是通过调用lastFilterArr(这里传入的)
    for(var i in comfig){
      data = comfig[i](data, store.getState(i))//要拿到input触发事件的value，只能通过store.getState方法取，此时的i为text

      //第一圈循环相当于 取出screenInput函数传入数据执行(data, store.getState(i))执行后的结果data接收

      //data 接收screenInput过滤后的新数组，等待第二次循环
      //第二次循环 轮到screenSex 函数， 此时 传入的data是screenInput过滤好后的数组，这样就达到了 双层过滤；
    }
    return data;
  }
}

var lastFilterArr = combineFilter({
  text: screenInput, //这两个函数，是我们抽出来的行为，
  sex: screenSex
   //这里传入的值  combineFilter(comfig)  接收
})

//触发input事件
inp.oninput = function(){
  store.dispatch({type: 'text', value: this.value});
}

store.subscribe(function(){
  renderPage( lastFilterArr(arr) )
})