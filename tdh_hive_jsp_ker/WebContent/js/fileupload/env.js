/**
 * �0�8�0�0״̬��Ϣ��ʾ��
 */
var Status=new function()
{
    this.statusDiv=null;    
    this.processBar=null;
    this.closeButton=null;    
    this.mainInfo=null;
    this.subInfo=null; 
    
    this.init=function()
    {
        if (this.statusDiv!=null)
	    {
	        return;
	    }
	    var _div=document.createElement("div");
		_div.id="status";
		_div.style.cssText="overflow:visible;width:300px;left:50%;margin-left:-150px;position:absolute;height:150px;top:50%;margin-top:-100px;background-color:transparent";
		_div.style.zIndex=1000;
		document.body.insertBefore(_div,document.body.firstChild);
		this.statusDiv=_div;
		var _table=document.createElement("table");
		_table.style.cssText="border-right:1px solid gray;border-top: 1px solid white;border-left:1px solid white;border-bottom:1px solid gray;width:300px;background-color:#ebeadb";
		_div.appendChild(_table);
				
		_tr=_table.insertRow(-1);
		this.processBar=_tr;		
		_cell=_tr.insertCell(-1);
		_cell.align="middle";
		_cell=_tr.insertCell(-1);
		_cell.align="middle";
		_cell.style.cssText="padding-top:8px;padding-bottom:3px;border-bottom:1px solid #898989";
		var _img=document.createElement("img");
		_img.style.cssText="border-right:1px solid white;border-top:1px solid gray;border-left:1px solid gray;border-bottom:1px solid white";
		//_img.src=Env.envPath+"waiting.gif";
		_img.src="./images/waiting.gif";
		_cell.appendChild(_img);
		var _input=document.createElement("input");
		_input.type="button";
		_input.style.cssText="background-color:#ebeadb;border:none;padding-top:2px;margin-left:10px;cursor:pointer";
		_input.onclick=function()
		{
		    //�ر�״17��
		    this.blur();
		    Status.setStatusShow(false);
		}
		_input.value="�ر�";
		_cell.appendChild(_input);
		_cell=_tr.insertCell(-1);		
		
		this.closeButton=_input;
		
		_tr=_table.insertRow(-1);
		_cell=_tr.insertCell(-1);
		_cell.style.cssText="width:6px";
		_cell=_tr.insertCell(-1);
		_cell.style.cssText="font-size:12px;line-height:20px;width:288px;padding-top:5px";		
		_cell.align="middle";
		this.mainInfo=_cell;		
		_cell=_tr.insertCell(-1);
		_cell.style.cssText="width:6px";
				
		_tr=_table.insertRow(-1);
		_cell=_tr.insertCell(-1);		
		_cell=_tr.insertCell(-1);
		var _div2=document.createElement("div");
		_div2.id="subInfo";
		_div2.style.cssText="margin-top:3px;margin-bottom:6px;border-right:1px solid white;border-top:1px solid gray;font-size:12px;overflow:auto;border-left:1px solid gray;border-bottom:1px solid white;background-color:white;padding:5px 0px 5px 5px;line-height:18px";				
		_cell.appendChild(_div2);
		this.subInfo=_div2;
		_cell=_tr.insertCell(-1);		
		
		this.subInfo.style.display="none";		
    }
    
    this.checkInit=function()
    {
        if (this.statusDiv==null)
	    {
	        this.init();
	    }	    
    }
    
    this.setStatusShow=function(_show)
    {
        this.checkInit();         
	    this.statusDiv.style.display=(_show)?"":"none";
	    if (_show==false)
	    {
	        this.processBar.style.display="";
	        this.clearSubInfo();
	    }
    }
    
    this.setProcessShow=function(_show)
    {
        this.checkInit();
        _show=(_show==undefined)?true:_show;
	    this.processBar.style.display=(_show)?"":"none";
    }    
    
    this.setMainInfo=function(_msg,_needRefresh)
    {
        this.checkInit();
        this.mainInfo.innerHTML=_msg;
        _needRefresh=(_needRefresh==undefined)?true:false;
        this.mainInfo.innerHTML+=(_needRefresh)?"<br>��ϵͳ��ʱ��ֹͣ��Ӧ����<a href='javascript:document.location.reload(true);'>ˢ��</a>�9�617 ":"";
        this.setStatusShow(true);
    }
    
    this.addSubInfo=function(_msg)
    {
        this.checkInit();
        this.subInfo.innerHTML+=_msg+"<br>";
        this.subInfo.style.display="";
    }
    
    this.clearSubInfo=function()
    {
        this.checkInit();
        this.subInfo.innerHTML="";
        this.subInfo.style.display="none";        
    }
    
    this.showErrInfo=function(_URI,_line,_message)
    {
        this.setProcessShow(false);
        this.setMainInfo("���ڷ������󣬳����޷�������",false);
        this.addSubInfo(_URI+"<br>"+_line+"<br><font color=red>"+_message+"</fomt>");
    }
}

/**
 * @author zxub
 * ���ڴ��ͨ�����Ƽ�17�Ŷ�����࣬��������17����ͬͨ�����������ֲ�ͬ��ͨ�Ŷ���
 */
function HttpRequestObject()
{
    this.chunnel=null;
    this.instance=null;
}

/**
 * @author zxub
 * ͨ�Ŵ����࣬���Ծ�17�������еķ���
 */
var Request=new function()
{
    this.showStatus=true;
    
    //ͨ����Ļ���
    this.httpRequestCache=new Array();
    
    /**
     * �����µ�ͨ�Ŷ���
     * @return �A17���µ�17�Ŷ���
     */
    this.createInstance=function()
    {
        var instance=null;
        if (window.XMLHttpRequest)
        {
            //mozilla
            instance=new XMLHttpRequest();
            //��Щ�汾��Mozilla�����������������ص�δ����XML mime-typeͷ����Ϣ������ʱ�����17��ˣ�Ҫȷ�����ص����ݰ���text/xml��Ϣ
            if (instance.overrideMimeType)
            {
                instance.overrideMimeType="text/xml";
            }
        }
        else if (window.ActiveXObject)
        {
            //IE
            var MSXML = ['MSXML2.XMLHTTP.5.0', 'Microsoft.XMLHTTP', 'MSXML2.XMLHTTP.4.0', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP'];
            for(var i = 0; i < MSXML.length; i++)
            {
                try
                {
                    instance = new ActiveXObject(MSXML[i]);
                    break;
                }
                catch(e)
                {
                }
            }
        }
        return instance;
    }
    
    /**
     * ��ȡ�A17��17�Ŷ���
     * ��ûָ��ͨ�����ƣ���Ĭ��ͨ����Ϊ"default"
     * �������в�������Ҫ��ͨ���࣬�򴴽�һ����ͬʱ����ͨ���໺����
     * @param _chunnel��17�����ƣ��������ڴ˲�������Ĭ�ρA17"default"
     * @return �A17��17�Ŷ���������17���໺����
     */
    this.getInstance=function(_chunnel)
    {
        var instance=null;
        var object=null;
        if (_chunnel==undefined)//ûָ��17������
        {
            _chunnel="default";
        }
        var getOne=false;
        for(var i=0; i<this.httpRequestCache; i++)
        {
            object=HttpRequestObject(this.httpRequestCache[i]);
            if (object.chunnel==_chunnel)
            {
                if (object.instance.readyState==0 || object.instance.readyState==4)
                {
                    instance=object.instance;
                }
                getOne=true;
                break;                    
            }
        }
        if (!getOne) //�����ڻ����У��򴴎�17
        {
            object=new HttpRequestObject();
            object.chunnel=_chunnel;
            object.instance=this.createInstance();
            this.httpRequestCache.push(object);
            instance=object.instance;
        }         
        return instance;
    }
    
    /**
     * �ͻ��������˷������17
     * @param _url:����Ŀ��
     * @param _data:Ҫ���͵�����
     * @param _processRequest:���ڴ����ؽ���ĺ������䶨������ڱ�ĵط�����Ҫ�ЁA17����������Ҫ�����17�Ŷ���
     * @param _chunnel:ͨ�����ƣ�Ĭ��Ϊ"default"
     * @param _asynchronous:�Ƿ��첽����Ĭ��Ϊtrue,���첽���K17
     */
    this.send=function(_url,_data,_processRequest,_chunnel,_asynchronous)
    {    
        if (_url.length==0 || _url.indexOf("?")==0)
        {
            Status.showErrInfo("ajax������","<font color=red>����Ŀ��Ϊ�գ�����ʧ�ܣ����飡</font>","5������ʾ�Զ�����17");
            window.setTimeout("Status.setStatusShow(false)",5000);
            return;
        }
        if (this.showStatus)
        {
            Status.setMainInfo("������������������ݣ�����1717...");  
        }
        if (_chunnel==undefined || _chunnel=="")
        {
            _chunnel="default";
        }
        if (_asynchronous==undefined)
        {
            _asynchronous=true;
        }
 
        var instance=this.getInstance(_chunnel);
        if (instance==null)
        {
            Status.showErrInfo("ajax������","<font color=red>�������֧��ajax���뗉17�飡</font>","5������ʾ�Զ�����17");
            window.setTimeout("Status.setStatusShow(false)",5000);
            return;
        }  
       
        if (_asynchronous==true && typeof(_processRequest)=="function")
        {
            instance.onreadystatechange=function()
            {
                if (instance.readyState == 4) // �ж϶���״1717
                {
                    if (instance.status == 200) // ��Ϣ�Ѿ��ɹ����أ���ʼ�����Ő_17
                    {
                        try
                    	{
                    	    _processRequest(instance);
                    	    Status.setStatusShow(false);
                    	    Request.showStatus=true;
                    	}
                        catch(e)
                        {
                            Status.showErrInfo("ajax�ص�����ʱ��������","<font color=red>"+e.message+"</font>","5������ʾ�Զ�����17");
                            window.setTimeout("Status.setStatusShow(false)",5000);
                        }
                    }
                    else
                    {
                        Status.showErrInfo("ajax������","<font color=red>���������ҳ�����쳣���뗉17�飡</font>","5������ʾ�Զ�����17");
                        window.setTimeout("Status.setStatusShow(false)",5000);
                    }
                }
            }
        }

        //_url��һ��ʱ�̸ı�Ĳ�������ֹ���ڱ�����������ͬ�����������������17���17
        if (_url.indexOf("?")!=-1)
        {
            _url+="&requestTime="+(new Date()).getTime();
        }
        else
        {
            _url+="?requestTime="+(new Date()).getTime();
        }
        
        if (_data.length==0)
        {
            instance.open("GET",_url,_asynchronous);          
            instance.send(null);            
        }
        else
        {
            instance.open("POST",_url,_asynchronous);
            instance.setRequestHeader("Content-Length",_data.length);
            instance.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            instance.send(_data);
        }

        if (_asynchronous==false && typeof(_processRequest)=="function")
        {       
            _processRequest(instance);
            if (Request.showStatus)
            {
                Status.setStatusShow(false);
            }
            else
            {
                Request.showStatus=true;
            }
        }
        
    }
    
    /**
     * ���A17��ʱ�������������ֻ�����첽����ֻ����GET��ʽ
     * @param _interval:���������Ժ����|17
     * @param _url:�����ַ
     * @param _processRequest:���ڴ����ؽ���ĺ������䶨������ڱ�ĵط�����Ҫ�ЁA17����������Ҫ�����17�Ŷ���
     * @param _chunnel:ͨ�����ƣ�Ĭ��Ϊ"defaultInterval"���Ǳ���
     */
    this.intervalSend=function(_interval,_url,_processRequest,_chunnel)
    {
        var action=function()
        {
            if (_chunnel==undefined)
            {
                _chunnel="defaultInterval";
            }
            var instance=Request.getInstance(_chunnel);
            if (instance==null)
            {
                Status.showErrInfo("ajax������","<font color=red>�������֧��ajax���뗉17�飡</font>","5������ʾ�Զ�����17");
                window.setTimeout("Status.setStatusShow(false)",5000);
                return;
            }
            if (typeof(_processRequest)=="function")
            {
                instance.onreadystatechange=function()
                {
                    if (instance.readyState == 4) // �ж϶���״1717
                    {
                        if (instance.status == 200) // ��Ϣ�Ѿ��ɹ����أ���ʼ�����Ő_17
                        {
                            _processRequest(instance);
                        }
                        else
                        {
                            Status.showErrInfo("ajax������","<font color=red>���������ҳ�����쳣���뗉17�飡</font>","5������ʾ�Զ�����17");
                            window.setTimeout("Status.setStatusShow(false)",5000);
                        }
                    }
                }
            }
            //_url��һ��ʱ�̸ı�Ĳ�������ֹ���ڱ�����������ͬ�����������������17���17
            if (_url.indexOf("?")!=-1)
            {
                _url+="&requestTime="+(new Date()).getTime();
            }
            else
            {
                _url+="?requestTime="+(new Date()).getTime();
            }
            instance.open("GET",_url,true);
            instance.send(null);
        }
        window.setInterval(action,_interval);        
    }
}

var Env=new function()
{   
    this.funcList=new Array();
        
    this.envPath=null;
    
    this.selfName="env.js";
    
    this.getPath=function()
    {
        this.envPath=document.location.pathname;
        this.envPath=this.envPath.substring(0,this.envPath.lastIndexOf("/")+1);        
        var _scripts=document.getElementsByTagName("script");
        var _envPath=null;
        var _scriptSrc=null;
        for (var i=0; i<_scripts.length; i++)
        {
            _scriptSrc=_scripts[i].getAttribute("src");
        	if (_scriptSrc && _scriptSrc.indexOf(this.selfName)!=-1)
        	{
        	    break;
        	}
        }
        if (_scriptSrc!=null)
        {
            if (_scriptSrc.charAt(0)=='/')
            {
                this.envPath=_scriptSrc.substr(0,_scriptSrc.length-this.selfName.length);
            }
            else
            {
                this.envPath=this.envPath+_scriptSrc.substr(0,_scriptSrc.length-this.selfName.length);
            }
        }        
    }
    this.getPath();    
    
    /**
     * �����ȡ��17Ҫ��js�ļ�
     * @param _jsName��js�ļ�·������Ϊ���·�������Ƕ�Ӧ��ǰjs(env.js)�����·����Ҳ�����þ���·��
     * @param _language:�Է��غ������д�������ԣ�Ĭ��ΪJScript���ɲ���
     */
    this.require=function(_jsName,_language)
    {
        var _absJsName=null;
        if (_jsName.charAt(0)=='/')
        {
            _absJsName=_jsName;
        }
        else
        {
            _absJsName=this.envPath+_jsName; 
        }        
        alert(this.envPath);
                alert(_jsName);
        
        if (!Env.funcList[_absJsName])
        {
            Env.funcList[_absJsName]="finished";
            var processJs=function(_instance)
            {
                //Ϊ����firefox���Д�17
                if (_language!=undefined)
                {
                    if (window.execScript)
                    {
                        window.execScript(_instance.responseText,_language);
                    }
                    else
                    {
                        window.eval(_instance.responseText,_language);
                    }                                       
                }
                else
                {
                    if (window.execScript)
                    {
                        window.execScript(_instance.responseText);
                    }
                    else
                    {                    
                        window.eval(_instance.responseText);
                    }                    
                }               
            }

            Request.showStatus=false;
            Request.send(_absJsName,"",processJs,"",false);
        }
    }
    
    /**
     * �ú�����Ч������Ӧ������script����һ��script�`17
     * ����document.write��script���е�ִ��˳�������
     */
    this.getJs=function(_jsName)
    {
        if (!Env.funcList[_jsName])
        {
            Env.funcList[_jsName]="finished";
            document.write('<scr'+'ipt type="text/javascript" src="'+_jsName+'"></'+'scr'+'ipt>');
        }
    }
}

/**
 * ajax����Զ��ҳ���Զ��ҳ����script��δִ�еĴ��K17
 */
function reloadJs(_language)
{
    var _c=document.getElementsByTagName("SCRIPT");
    for (var i=0;i<_c.length;i++)
    {
        if (_c[i].src)
        {
            var _s=document.createElement("script");
            _s.type="text/javascript";
            _s.src=_c[i].src;
            //Ϊ����firefox����_c[0].insertAdjacentElement("beforeBegin",_s)
            _c[0].parentNode.insertBefore(_s,_c[0]);            
            _c[i].parentNode.removeChild(_c[i]);
        }
        else if (_c[i].text)
        {
            if (_language!=undefined)
            {
                if (window.execScript)
                {
                    window.execScript(_c[i].text,_language);
                }
                else
                {
                    window.eval(_c[i].text,_language);
                }
            }
            else
            {
                if (window.execScript)
                {
                    window.execScript(_c[i].text);
                }
                else
                {
                    window.eval(_c[i].text);
                }
            }
        }
    }
}

window.onerror=handleError;

function handleError(_message, _URI, _line)
{
	Status.showErrInfo(_URI,_line,_message); // ��ʾ�û����ҳ������޷�������Ӧ
    return true; // ֹͣĬ�ϵ����_17
}