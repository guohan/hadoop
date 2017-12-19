
//使用按钮进行的全选
//全选
function chooseAll(field){
if(field!=null){
		if(field.length>=1){
			for (i = 0; i < field.length; i++) {
				field[i].checked = true;
			}
		}
	}
}
//全不选
function chooseNone(field){
if(field!=null){
	if(field.length>=1){
			for (i = 0; i < field.length; i++) {
				field[i].checked = false;
			}
		}
	}
}

//使用选择框进行的全选
var checkflag ;
	function check(field) {
	if(field!=null){
		checkflag = event.srcElement.checked;
		if (!checkflag) {
			if(field.length!=null)
				{
					for (i = 0; i < field.length; i++) 
						{
						field[i].checked = false;
						}
				return "false"; 
				}
			else{
				field.checked = false;
				return "false"; 
				}
			}
		else {
			if(field.length!=null)
				{
					for (i = 0; i < field.length; i++) 
						{
							field[i].checked = true; 
						}
				return "true"; 
				}
			else{
				field.checked = true;
				return "true";
				}
			}
		}
	}

