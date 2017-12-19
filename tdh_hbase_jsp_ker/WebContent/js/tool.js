document.onmousedown = initDown;
document.onmouseup   = initUp;
document.onmousemove = initMove;

function initDown() {
	doDown();
	moveme_onmousedown();
}

function initUp() {
	doUp();
	moveme_onmouseup();
}

function initMove() {
	moveme_onmousemove();
}

function create_folder(){
	var i,target_str,search,root;
	i = parent.file_main.file_state.current_id.value;
	search = parent.file_main.file_state.search_state.value;
	folder_type = parent.file_main.file_state.Folder_Type.value;
	root = parent.file_main.file_state.root_id.value;
	
	if ((folder_type == "2" || folder_type == "3") && i == root){
		alert("当前目录不能建立文件夹");
		return;
	}
	
	if (search == "1"){
		alert("当前处于搜索状态，不能建立新文件夹。");
		return;
	}
	if (i == "" || i == "null"){
		alert("当前目录状态未知，不能建立目录。");
	}
	else{
		target_str = "new_folder.jsp?folder_type=" + folder_type + "&Current_Folder=" + i;
		window.open(target_str,'file_window','width=300,height=150');
	}
	
}

function do_reload(){
	var i,target_str,folder_type;
	i = parent.file_main.file_state.current_id.value;
	folder_type = parent.file_main.file_state.Folder_Type.value;
	target_str = "file_list.jsp?folder_type=" + folder_type + "&Current_Folder=" + i;
	parent.file_main.location = target_str;
}

function create_file(){
	var i,target_str,search,folder_type,root;
	i = parent.file_main.file_state.current_id.value;
	search = parent.file_main.file_state.search_state.value;
	folder_type = parent.file_main.file_state.Folder_Type.value;
	root = parent.file_main.file_state.root_id.value;

	if ((folder_type == "2" || folder_type == "3") && (i == root)){
		alert("当前目录不能建立文件");
		return;
	}

	if (search == "1"){
		alert("当前处于搜索状态，不能建立新文件。");
		return;
	}

	if (i == "" || i == "null"){
		alert("当前目录状态未知，不能建立文件。");
	}
	else{
		target_str = "create_file.jsp?folder_type=" + folder_type + "&Current_Folder=" + i;
		window.open(target_str,'file_window','width=300,height=150');
	}
}

function do_search(){
	var temp;
	temp = parent.file_main.file_state.search_state.value;
	if (temp == "0"){
		parent.file_main.file_state.search_state.value = "1";
		window.open('searchfile.htm','contents');
	}
	else{
		parent.file_main.file_state.search_state.value = "0";
		window.open('state.jsp','contents');
	}

}

function do_upfolder(){
	var current_id,root,target_str,folder_type;
	current_id = parent.file_main.file_state.current_id.value;
	root = parent.file_main.file_state.root_id.value;
	folder_type = parent.file_main.file_state.Folder_Type.value;
	if (current_id == "" || root == ""){
		alert('数据错误，请联系管理员。根ID为：' + root + '当前目录ID为：' + parent_id);
		return;
	}
	if (current_id == root){
		alert('已经回到文件夹顶。');
		return;
	}
	else{
                target_str = "file_list.jsp?folder_type=" + folder_type + "&Current_Folder=" + current_id+ "&command=up";
		window.open(target_str,'file_main');
	}
}

function del(){
	var target_str,select_id;

	select_id = parent.file_main.file_state.select_id.value;

	if (select_id == ""){
		alert('当前没有选中任何文件或文件夹。');
		return;
	}
	if (window.confirm('是否确定删除。')){
		target_str = "del.jsp?select=" + select_id;
		window.open(target_str,'file_del','width=300,height=150');
	}
	else
		return;
}

function share(){
	var target_str,select_id;
	select_id = parent.file_main.file_state.select_id.value;
	if (select_id == ""){
		alert('请选择将要共享的文件夹。');
		return;
	}
	target_str = "share.jsp?select=" + select_id;
	window.open(target_str,'file_share','width=400,height=400');
}
function do_cut(){
	var target_str,select_id;
	select_id = parent.file_main.file_state.select_id.value;
	if (select_id == ""){
		alert('当前没有选中任何文件或文件夹。');
		return;
	}
	target_str = "file_cut.jsp?select=" + select_id;
	window.open(target_str,'file_cut','width=300,height=150');

}

function do_copy(i){
	var target_str,select_id;
	select_id = parent.file_main.file_state.select_id.value;
	if (select_id == ""){
		alert('当前没有选中任何文件或文件夹。');
		return;
	}
	target_str = "file_copy.jsp?select=" + select_id;
	window.open(target_str,'file_copy','width=300,height=150');

}

function do_plaster(){
	var temp,current;
	temp = parent.file_main.file_state.clipboard.value;
	current = parent.file_main.file_state.current_id.value;
	if (temp == "0") {
		alert('当前剪贴板中没有内容。');
		return;
	}
	if (current == ""){
		alert("当前目录状态未知，不能粘贴。");
	}
	else{
		target_str = "plaster_file.jsp?current=" + current + "plaster=" + temp;
		window.open(target_str,'file_window','width=300,height=150');
	}

}