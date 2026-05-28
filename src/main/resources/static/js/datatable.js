$(document).ready(function() {
  $('#dataTable').DataTable({
  /*
  * 默认为false
  * 是否开启状态保存，当选项开启的时候会使用一个cookie保存表格展示的信息的状态，例如分页信息，展示长度，过滤和排序等
  * 这样当终端用户重新加载这个页面的时候可以使用以前的设置
  */
//  "bStateSave": true,
  // 是否开启本地分页
  "paging": true,
  // 是否开启本地排序
  "ordering": true,
  // 规定初始化排序方式:[第一列,倒序]
  "order": [0,'desc'],
  // 是否显示左下角信息
  "info": true,
  // 是否允许用户改变表格每页显示的记录数
  "lengthChange": true,
  // 是否显示处理状态(排序的时候，数据很多耗费时间长的话，也会显示这个)
//    "processing": true,
  // 是否允许 DataTables 开启本地搜索
  "searching": true,
  // 是否开启服务器模式
//    "serverSide": true,
  // 控制 DataTables 的延迟渲染，可以提高初始化的速度
//    "deferRender": true,
  // 增加或修改通过 Ajax 提交到服务端的请求数据
//    "ajax": {
//        "url": "/admin/page",
//        "type":"POST"
//    },
  // 分页按钮显示选项
  "pagingType": "full_numbers",
    // 设置列的数据源
//    "columns": [
//        {"data": "articleId"},
//        {"data": "articleTitle"},
//        {"data": function (row, type, val, meta) {
//                var path = row.articleUrl;
//                return '<img src= "'+path+'" alt="文章展示图" style="width: 100px;height: 100px;text-align: center;">'
//            }},
//        {"data": function (row, type, val, meta) {
//            if(row.articleStatus == 1){
//                return '<p style="color:orangered;">置顶</p>'
//            }
//            else{
//                return '<p>未置顶</p>'
//            }
//            }},
//        {"data":function (row, type, val, meta) {
//                var date = DateTime.format(row.articleUpdateTime,"yyyy-MM-dd HH:mm:ss");
//                return '<p>'+date+'</p>'
//            }},
//        {
//            "data": function (row, type, val, meta) {
//                return '<a href="#" type="button" class="btn btn-sm btn-default"><i class="fa fa-search"></i> 查看</a>&nbsp;&nbsp;&nbsp;' +
//                    '<a href="#" type="button" class="btn btn-sm btn-primary"><i class="fa fa-edit"></i> 编辑</a>&nbsp;&nbsp;&nbsp;' +
//                    '<a href="#" type="button" class="btn btn-sm btn-danger"><i class="fa fa-trash-o"></i> 删除</a>'
//            }
//        }
//    ],
    // 国际化
    "language": {
      "sProcessing": "处理中...",
      "sLengthMenu": "显示 _MENU_ 项结果",
      "sZeroRecords": "没有匹配结果",
      "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
      "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
      "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
      "sInfoPostFix": "",
      "sSearch": "搜索:",
      "sUrl": "",
      "sEmptyTable": "表中数据为空",
      "sLoadingRecords": "载入中...",
      "sInfoThousands": ",",
      "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "上页",
        "sNext": "下页",
        "sLast": "末页"
      },
      "oAria": {
        "sSortAscending": ": 以升序排列此列",
        "sSortDescending": ": 以降序排列此列"
      }
    }
  });
});
// For showIncomes.html
function reStatIncomes(){
  $.ajax({
    type: "GET",
    url: "/filter/stats/incomeBase",
    cache: false,  //禁用缓存
    data: {"durPeriod": $("#durPeriod").val(), "durVal": $("#durVal").val()},
    dataType: "json",
    success: function (result) {
//          console.log("result="+JSON.stringify(result));
      //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
      //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
      callback(result);
    }
  });
}