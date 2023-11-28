// 点击行选中radio
function shot(tr) {
  console.log("tr="+tr);
  //$(tr).find('input').prop('checked', true);
}

// 根据"操作"动态调整显示内容
function showAndHide(mySelect){
  console.log("mySelect="+mySelect);
  if (mySelect && mySelect == '[object HTMLSelectElement]') {
    var val = mySelect.options[mySelect.selectedIndex].value;
    console.log("val="+val);
    switch(val){
      case '1':
        recharge();
        break;
      case '2':
        consume();
        break;
      case '3':
        bonus();
        break;
      default:console.log("[ERROR] Unknown value: " + val);
    }
  }else{
    console.log("mySelect is not a 'HTMLSelectElement'");
  }
}

function recharge(){
  document.getElementById("project").style.display="none";
  document.getElementById("pay_amount").style.display="";
  document.getElementById("pay_method").style.display="";
  document.getElementById("bonus").style.display="none";
}
function consume(){
  document.getElementById("project").style.display="";
  document.getElementById("pay_amount").style.display="";
  document.getElementById("pay_method").style.display="";
  document.getElementById("bonus").style.display="none";
}
function bonus(){
  document.getElementById("project").style.display="";
  document.getElementById("pay_amount").style.display="none";
  document.getElementById("pay_method").style.display="none";
//  document.getElementById("bonus1").style.display="";
//  document.getElementById("bonus2").style.display="";
  document.getElementById("bonus").style.display="";
}
//function display(cid){
//  var objectArr = document.getElementsByClassName(cid);
//  var i;
//  for (i = 0; i < objectArr.length; i++) {
//    objectArr[i].style.display="";
//  }
//}