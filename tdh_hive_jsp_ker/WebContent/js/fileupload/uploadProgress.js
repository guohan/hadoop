//Env.require("UI/panel.js");
//Env.require("UI/basic.js");
//Env.require("base/thread.js");
//Env.require("progressBar.js");

window.progressPanel=null;

//var detailUrl="http://localhost:8080/oms/fileupload/progressDetail.do";

var detailUrl

function setDetailUrl(url)
{
    detailUrl = url;
}



var callback=function(instance)
{	
	var response=instance.responseText;
	if (response.length!=0)
	{
	    var paras=response.split("||");
	    ProgressBar.setPosition(paras[0]);
	    updateDetail(paras[1],paras[2],paras[3],paras[4],paras[5],paras[6]);
	}
}

function getDetail()
{
    Request.showStatus=false;
    Request.send(detailUrl,"",callback);
}

var t1=new Thread(getDetail,1000,-1);

var fileName=null;
var speed=null;
var readTotalSize=null;
var totalSize=null;
var remainTime=null;
var totalTime=null;

function updateDetail(_fileName,_speed,_readTotalSize,_totalSize,_remainTime,_totalTime)
{
    if (window.progressPanel!=null && window.progressPanel.mainDiv!=null)
    {        
        fileName.title=_fileName;
        if (_fileName.length>18) _fileName="..."+_fileName.slice(_fileName.length-15);
        fileName.innerHTML=_fileName;
        speed.nodeValue=_speed;
        readTotalSize.nodeValue=_readTotalSize;
        totalSize.nodeValue=_totalSize;
        remainTime.nodeValue=_remainTime;
        totalTime.nodeValue=_totalTime;
    }
    else
    {
        t1.sleep();
    }
}

function showProgress()
{
	if (window.progressPanel==null || window.progressPanel.mainDiv==null)
	{
	    window.progressPanel=new Panel("���ݴ������",214,160);
	    window.progressPanel.mainDiv.removeChild(window.progressPanel.bottomDiv);
    	var div=UI.$Div("left");
    	div.style.height=18;
    	div.innerHTML="��Ϣ���ļ��ϴ� ";
    	fileName=document.createElement("span");
    	fileName.style.cssText="cursor:pointer";
    	fileName.innerHTML="δ֪�ļ�";
    	div.appendChild(fileName);    	
    	window.progressPanel.contentDiv.appendChild(div);   
    	div=UI.$Div("left");	
    	ProgressBar.init(200);    	
    	div.style.height=18;
    	div.appendChild(ProgressBar.container);
    	window.progressPanel.contentDiv.appendChild(div); 
    	div=UI.$Div("left");
    	div.style.height=18;
    	div.innerHTML="�ϴ��ٶȣ�";
    	speed=UI.$Text("δ֪");
    	div.appendChild(speed);
    	div.appendChild(UI.$Text(" K/S"));    	
    	window.progressPanel.contentDiv.appendChild(div);
    	div=UI.$Div("left");
    	div.style.height=18;
    	div.innerHTML="���ϴ���";
    	readTotalSize=UI.$Text("δ֪");
    	div.appendChild(readTotalSize);
    	div.appendChild(UI.$Text("M ����"));
    	totalSize=UI.$Text("δ֪");
    	div.appendChild(totalSize);
    	div.appendChild(UI.$Text("M")); 
    	window.progressPanel.contentDiv.appendChild(div);
    	div=UI.$Div("left");
    	div.style.height=18;
    	div.innerHTML="����ʣ��ʱ�䣺";
    	remainTime=UI.$Text("δ֪");
    	div.appendChild(remainTime);
    	window.progressPanel.contentDiv.appendChild(div);
    	div=UI.$Div("left");
    	div.style.height=18;
    	div.innerHTML="������ʱ�䣺";
    	totalTime=UI.$Text("δ֪");
    	div.appendChild(totalTime);
    	window.progressPanel.contentDiv.appendChild(div);

    	window.progressPanel.otherCloseAction=function()
    	{
    	    t1.sleep();
    	}
    		
    	if (t1.runFlag)
    	{
    	    t1.resume();
    	}
    	else
    	{
    	    t1.start();
    	}
	}
}