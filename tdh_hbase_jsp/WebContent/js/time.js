
function   minute(name,fName)   
  {   
  this.objName   =   name;   
  this.name   =   "xiruo_"+name;   
  this.fName   =   fName   +   "m_input";   
  this.timer   =   null;   
  this.fObj   =   null;   
  this.selObj   =   null;   
    
  this.toString   =   function()   
  {   
  var   objDate   =   new   Date();   
  var   sMinute_Common   =   "class=\"m_input\"   maxlength=\"2\"   name=\""+this.fName+"\"   onpropertychange=\""+this.name+".changeTime();\"     onfocus=\""+this.name+".setFocusObj(this)\"   onblur=\""+this.name+".setTime(this)\"   onkeyup=\""+this.name+".prevent(this)\"   onkeypress=\"if   (!/[0-9]/.test(String.fromCharCode(event.keyCode)))event.keyCode=0\"   onpaste=\"return   false\"   ondragenter=\"return   false\"";   
  var   sButton_Common   =   "class=\"m_arrow\"   onfocus=\"this.blur()\"   onmousedown=\""+this.name+".controlTime()\"   onmouseup=\""+this.name+".stop();\"   onmouseout=\""+this.name+".stop();\"   disabled"   
  var   str   =   "";   
  str   +=   "<table   border=\"0\"   cellspacing=\"0\"   cellpadding=\"0\">"   
  str   +=   "<tr>"   
  str   +=   "<td>"   
  str   +=   "<div   class=\"m_frameborder\">"   
  str   +=   "<input   radix=\"24\"   value=\"00\"   "+sMinute_Common+">:"   
  str   +=   "<input   radix=\"60\"   value=\"00\"   "+sMinute_Common+">:"   
  str   +=   "<input   radix=\"60\"   value=\"00\"   "+sMinute_Common+">"   
  str   +=   "<input   type=\"hidden\"   name=\""+this.objName+"\"   id=\""+this.objName+"\">"   
  str   +=   "</div>"   
  str   +=   "</td>"   
  str   +=   "<td>"   
  str   +=   "<table   border=\"0\"   cellspacing=\"0\"   cellpadding=\"0\">"   
  str   +=   "<tr><td   style=\"padding:0px;\"><button   id=\""+this.fName+"_up\"   "+sButton_Common+">5</button></td></tr>"   
  str   +=   "<tr><td   style=\"padding:0px;\"><button   id=\""+this.fName+"_down\"   "+sButton_Common+">6</button></td></tr>"   
  str   +=   "</table>"   
  str   +=   "</td>"   
  str   +=   "</tr>"   
  str   +=   "</table>"   
  return   str;   
  }   
  this.play   =   function(space)   
  {   
  this.timer   =   setInterval(this.name+".playback()",space);   
  }   
  this.formatTime   =   function(sTime)   
  {   
  sTime   =   ("0"+sTime);   
  return   sTime.substr(sTime.length-2);   
  }   
  this.stop   =   function(space){   
  if(this.timer&&this.timer!=null)   
  clearInterval(this.timer);   
  }   
  this.playback   =   function()   
  {   
  if   (!this.fObj)   return;   
  var   cmd   =   selObj.innerText=="5"?true:false;   
  var   i   =   parseInt(this.fObj.value,10);   
  var   radix   =   parseInt(this.fObj.radix,10)-1;   
  if   (i==radix&&cmd)   
  {   
  i   =   0;   
  }   
  else   if   (i==0&&!cmd)   
  {   
  i   =   radix;   
  }   
  else   
  {   
  cmd?i++:i--;   
  }   
  this.fObj.value   =   this.formatTime(i);   
  this.fObj.select();   
  }   
  this.prevent   =   function(obj)   
  {   
  clearInterval(this.timer);   
  this.setFocusObj(obj);   
  var   value   =   parseInt(obj.value,10);   
  var   radix   =   parseInt(obj.radix,10)-1;   
  if   (obj.value>radix||obj.value<0)   
  {   
  obj.value   =   obj.value.substr(0,1);   
  }   
  }   
  this.controlTime   =   function()   
  {   
  event.cancelBubble   =   true;   
  if   (!this.fObj)   return;   
  selObj   =   event.srcElement;   
  this.play(100);   
  }   
  this.setTime   =   function(obj)   
  {   
  obj.value   =   this.formatTime(obj.value);   
  }   
  this.setFocusObj   =   function(obj)   
  {   
  document.getElementById(this.fName+"_up").disabled   =   document.getElementById(this.fName+"_down").disabled   =   false;   
  this.fObj   =   obj;   
  }   
  this.getTime   =   function()   
  {   
  var   arrTime   =   new   Array(2);   
  for   (var   i=0;i<document.getElementsByName(this.fName).length;i++)   
  {   
  arrTime[i]   =   document.getElementsByName(this.fName)[i].value;   
  }   
  return   arrTime.join(":");   
  }   
  this.changeTime   =   function()   {   
  document.getElementById(this.objName).value=this.getTime();   
  }   
  } 
