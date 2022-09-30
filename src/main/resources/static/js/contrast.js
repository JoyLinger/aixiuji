function getData(attr){
  $.ajax({
	type: "get",
	//异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
	async : true,
	//请求地址
    url : "/filter/showContrasts",
	//请求参数
	data: {"attr": attr},
    //返回数据形式为json
    dataType : "json",
    //请求成功时执行该函数内容，data即为服务器返回的json对象
    success : function(data) {
      if (data && typeof data === 'object') {
        console.log("data="+data);
        var jsonData = JSON.stringify(data);
        console.log("jsonData="+jsonData);
        $('#dataTable').DataTable( {
            paging: false
        } );

        $('#dataTable').DataTable( {
            destroy: true,
            searching: false
        } );
        $('#dataTable').DataTable( {
          "ajax": jsonData
        } );
      }
    },
    error : function(errorMsg) {
      console.log("JSON.stringify(errorMsg)="+JSON.stringify(errorMsg));
      alert("哎呀，表格请求数据失败咯!");
    }
  });
}