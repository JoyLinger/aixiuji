// 筛选span函数
function spanFilter(data, sex) {
  if (sex == "a") { //判断输入的值是否为默认值a，如果是，不用筛选，直接返回原数组
    return data;
  } else {
    return data.filter(function (ele, index, self) {//利用数组方法filter过滤
      return sex == ele.sex;
        //第一遍循环，查看 数组第一个数据里面的 sex 是否等于 传进来的sex  如果是，返回这条数据到新数组
        //{ name: "王小明", src: "./img/head.jpg", des: "成绩很好", sex: "m", age: 18 }
    })
  }
}