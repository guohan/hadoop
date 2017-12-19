/**
 * �¼�������
 */
var Event=new function()
{
    this.element=function(_event)
    {
        return _event.target || _event.srcElement;        
    }
    
    this.pointerX=function(_event)
    {
        return _event.pageX || (_event.clientX+(document.documentElement.scrollLeft || document.body.scrollLeft));
    }
    
    this.pointerY=function(_event)
    {
        return _event.pageY || (_event.clientY+(document.documentElement.scrollTop || document.body.scrollTop));
    }
    
    this.isLeftClick=function(_event)
    {
        return (((_event.which) && (_event.which==1)) || ((_event.button) && (_event.button==1)));
    }
    
    this.isRightClick=function(_event)
    {
        return (((_event.which) && (_event.which==2)) || ((_event.button) && (_event.button==2)));
    }
    
    this.observers=null;
    
    this.alreadyObserve=function(element,name,observer,useCapture)
    {
        for (var i=0; i<Event.observers.length; i++)
        {
            if (Event.observers[i][0]==element && Event.observers[i][1]==name && Event.observers[i][2]==observer)
        	{
        	    return true;        	    
        	}
        }
        return false;
    }
    
    this.unReg=function(element,name,observer,useCapture)
    {
        for (var i=0; i<Event.observers.length; i++)
        {
        	if (Event.observers[i][0]==element && Event.observers[i][1]==name && Event.observers[i][2]==observer)
        	{
        	    Event.observers[i][0]=null;
        	    Event.observers.splice(i,1);
        	    break;
        	}
        }
    }
    
    /**
     * ��ֹ�¼�������������ֹ
     */
    this.stopEvent=function(_event)
    {
        if (_event.preventDefault)
        {
            _event.preventDefault();
            _event.stopPropagation();
        }
        else
        {
            _event.returnValue = false;
            _event.cancelBubble = true;
        }
    }
    
    
    /**
     * �����¼�����
     * ע�⣬����ͬһ��Element,IE��Firefox�Ĵ���˳�����෴�ģ�IE��ջ����ʽ����ע�����ִ�У���Firefox�ö��е���ʽ����ע����ִ��
     * ����useCapture���ǵ�element���ڰ�����ϵ���¼��Ĵ���˳��
     * ����˳����2�֣�capturing��bubbling
     * capturing�Ǵ������ڣ���bubbling�Ǵ�������
     * IEֻ֧��bubbling������useCapture��û�������
     * Firefox�У�2�ֶ�֧�֣�useCaptureΪfalse��ʱ��Ϊbubbling��trueΪcapturing���Ƽ���bubbling
     * Ĭ��useCaptureΪfalse
     */
    this.observe=function(element, name, observer, useCapture)
    {
        if (typeof(element)=="string")
        {
            element=document.getElementById(element);
        }
        useCapture = useCapture || false;
        if (this.observers==null)
        {
            this.observers=new Array();
        }        
        //����Ƿ�ע����ͬ�����¼�����
        if (Event.alreadyObserve(element, name, observer, useCapture)) return;        
        if (element.addEventListener) //firefox
        {          
            element.addEventListener(name, observer, useCapture);
        }
        else if (element.attachEvent) //IE
        {
            element.attachEvent("on"+name, observer);
        }
        this.observers.push([element, name, observer, useCapture]);        
    }
    
    this.stopObserving=function(element, name, observer, useCapture)
    {
        if (typeof(element)=="string")
        {
            element=document.getElementById(element);
        }
        useCapture = useCapture || false;
        
        if (element.removeEventListener)
        {
            element.removeEventListener(name, observer, useCapture);
        }
        else if (element.detachEvent)
        {
            element.detachEvent("on"+name, observer);
        }
        Event.unReg(element, name, observer, useCapture);
    }
    
    this.clearObservers=function()
    {
        if (Event.observers==null) return;
        for (var i=0;i<Event.observers.length; i++)
        {
            Event.stopObserving.apply(this, Event.observers[i]);
            Event.observers[i][0] = null;//�ͷŶ�element������
        }
        Event.observers = null;
    }
}

if (window.navigator.userAgent.indexOf("MSIE")>=1)
{
    Event.observe(window,'onunload',Event.clearObservers,false);
}