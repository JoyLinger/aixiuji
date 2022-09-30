//筛选搜索框函数
function inputFilter(data, value){
    if(!value){//判断输入的值是否为默认值''，如果是，不用过滤，直接返回原数组
        return data;
    }else{
      return data.filter(function(ele, index, self){
           return ele.name.indexOf(value) >= 0;
            //字符串方法，indexOf 查看value这个字符串，是否存在于ele.name中，如果存在返回大于0的索引(true)
            //如果不存在，返回-1，(false)
            //如果返回(true)，返回这条数据到新数组中
       })
    }
}