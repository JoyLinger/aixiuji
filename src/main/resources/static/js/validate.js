function validateLogin(){
  //点击一次提交后显示信息数据
//  $("#msg").style.display="";
//  document.getElementById("msg").style.display="";
  var account = $("#account").val();
  var password = $("#password").val();
  if(account == null || account == ""){
    alert("账号不能为空");
    return false;
  }
  if(password == null || password == ""){
    alert("密码不能为空");
    return false;
  }
  　return true;
}
function validateLogin2(){
  alert("validateLogin2");
  var error = $("#errMsg").val();
  if(error!=""){
    alert(error);
  }
}