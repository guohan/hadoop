
//ʹ�ð�ť���е�ȫѡ
//ȫѡ
function chooseAll(field){
if(field!=null){
		if(field.length>=1){
			for (i = 0; i < field.length; i++) {
				field[i].checked = true;
			}
		}
	}
}
//ȫ��ѡ
function chooseNone(field){
if(field!=null){
	if(field.length>=1){
			for (i = 0; i < field.length; i++) {
				field[i].checked = false;
			}
		}
	}
}

//ʹ��ѡ�����е�ȫѡ
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

