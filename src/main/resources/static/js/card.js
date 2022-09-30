function display1(id){
  var target=document.getElementById(id);
  if(target.style.display=="none"){
      target.style.display="";
  }
  var myself=document.getElementById("bonus1button");
  myself.style.display="none";
  var bonus2button=document.getElementById("bonus2button");
  if(bonus2button.style.display=="none"){
      bonus2button.style.display="";
  }
}
function display2(id){
  var target=document.getElementById(id);
  if(target.style.display=="none"){
      target.style.display="";
  }
  var myself=document.getElementById("bonus2button");
  myself.style.display="none";
}