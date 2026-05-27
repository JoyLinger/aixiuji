$(document).ready(function() {
  // 日期格式映射
  var dateFormats = {
    day: 'yyyy-MM-dd',
    month: 'yyyy-MM',
    year: 'yyyy',
    quarter: 'yyyy-Q'
  };
  
  // 设置日期选择器
  function setDatePicker() {
    var dateDim = $('#dateDim').val();
    var format = dateFormats[dateDim] || 'yyyy-MM-dd';
    $('#queryDay').unbind('click');
    $('#queryDay').click(function() {
      WdatePicker({el: this, dateFmt: format});
    });
  }
  
  // 初始化日期选择器
  setDatePicker();
  
  // 日期维度变化时更新日期格式
  $('#dateDim').change(function() {
    setDatePicker();
    // 清空日期输入框，因为格式变了
    $('#queryDay').val('');
  });
  
  var table = $('#dataTable').DataTable({
    "processing": true,
    "serverSide": true,
    "deferRender": true,
    "paging": true,
    "ordering": true,
    "order": [[0, 'desc']],
    "info": true,
    "lengthChange": true,
    "searching": false,
    "pagingType": "full_numbers",
    "ajax": {
      "url": "/filter/showBillPage",
      "type": "POST",
      "contentType": "application/json",
      "data": function (d) {
        d.day = $('#queryDay').val();
        d.search = $('#searchNote').val();
        d.project = parseInt($('#projectFilter').val());
        d.dateDim = $('#dateDim').val();
        return JSON.stringify(d);
      }
    },
    "columns": [
      {"data": "id"},
      {
        "data": null,
        "render": function(data, type, row, meta) {
          return data.day || '-';
        }
      },
      {
        "data": null,
        "render": function(data, type, row, meta) {
          return sessionData.operations[data.operation] || '';
        }
      },
      {
        "data": null,
        "render": function(data, type, row, meta) {
          return sessionData.projects[data.project] || '';
        }
      },
      {"data": "pay_amount"},
      {
        "data": null,
        "render": function(data, type, row, meta) {
          return sessionData.payMethods[data.pay_method] || '';
        }
      },
      {
        "data": null,
        "render": function(data, type, row, meta) {
          return sessionData.roles[data.role] || '';
        }
      },
      {
        "data": null,
        "render": function(data, type, row, meta) {
          var note = data.note || '';
          if (note.length > 20) {
            return '<span title="' + note + '">' + note.substring(0, 20) + '...</span>';
          }
          return note;
        }
      },
      {
        "data": null,
        "render": function(data, type, row, meta) {
          if (data.card && data.card.vip) {
            return '<span title="' + (data.card.vip.detail || '') + '">' + (data.card.vip.name || '') + '</span>';
          }
          return '-';
        }
      },
      {
        "data": null,
        "render": function(data, type, row, meta) {
          if (data.card) {
            return '<span title="' + (data.card.detail || '') + '">' + (data.card.parseType || '') + '</span>';
          }
          return '-';
        }
      },
      {
        "data": null,
        "render": function(data, type, row, meta) {
          return '<div class="nav-item dropdown">' +
                 '<a aria-expanded="false" class="nav-link dropdown-toggle no-padding" data-bs-toggle="dropdown" href="#" id="dropdown01">更多操作</a>' +
                 '<ul aria-labelledby="dropdown01" class="dropdown-menu">' +
                 '<li><a class="dropdown-item" href="/filter/toUpdateBill?billId=' + data.id + '">修改账单信息</a></li>' +
                 '</ul>' +
                 '</div>';
        }
      }
    ],
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
  
  $('#searchBtn').click(function() {
    table.ajax.reload();
  });
  
  $('#resetBtn').click(function() {
    $('#queryDay').val('');
    $('#searchNote').val('');
    $('#projectFilter').val('-1');
    $('#dateDim').val('day');
    setDatePicker();
    table.ajax.reload();
  });
  
  $('#searchNote, #queryDay').keypress(function(e) {
    if (e.which == 13) {
      $('#searchBtn').click();
    }
  });
});
