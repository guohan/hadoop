//获得所有checkbox的值
var isCheck = false;
function checkAll()
{
   var tempCatalogs = document.getElementsByName("checkbox");
   if (document.all.checkallbox.checked == true)
   {
    isCheck = true;
   }else{
    isCheck = false;
   }
    for(var i = 0 ; i < tempCatalogs.length ; i++){
      tempCatalogs[i].checked = isCheck;
    }
}

//获得选中checkbox的值
function isSelected()
{
  var isOk = false;
  document.all.contid.value = "";
  var count = 0;
  if (document.all.checkbox!=undefined)
  {
  if (document.all.checkbox.length >=1){
    for (var i=0; i<document.all.checkbox.length; i++)
    {
      if (document.all.checkbox[i].checked)
      {
        ++ count;
        if (count == 1)
        {
          document.all.contid.value += document.all.checkbox[i].value
        }
        else
        {
          document.all.contid.value += "_" + document.all.checkbox[i].value;
        }
        isOk = true;
      }
    }
  }else{
    if (document.all.checkbox.checked)
    {
      document.all.contid.value = document.all.checkbox.value;
      isOk = true;
    }
  }
  return isOk;
  }
}
