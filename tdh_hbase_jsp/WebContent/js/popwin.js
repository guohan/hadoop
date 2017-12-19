function PopWinObject() 
{
	
	//var i = document.createElement("<iframe IsPopWin=1 name=PopWinIframe scrolling=no style='position:absolute;top:200;border:2 outset'></iframe>");
	var i=document.createElement("div");
	document.body.insertBefore(i);
	i.outerHTML = "<iframe IsPopWin=1 id=PopWinIframe width=1 height=1 frameborder=0 scrolling=no style='position:absolute;display:none;'></iframe>";
	this.Iframe = document.all.PopWinIframe;
	this.Iframe.Object = this;
	this.Parent = self;
	this.Window = document.frames("PopWinIframe");
	this.Document = document.frames("PopWinIframe").document;
	document.frames("PopWinIframe").document.open();
	document.frames("PopWinIframe").document.close();
	document.frames("PopWinIframe").PopWinObject = this;
	this.Body = document.frames("PopWinIframe").document.body;
	this.Iframe.removeAttribute("id");
	this.Body.style.border="2 outset";
	this.Body.style.marginTop=0;
	this.Body.style.marginLeft=0;
	this.Body.style.marginRight=0;
	this.Body.style.marginBottom=0;
	this.Body.style.backgroundColor="#cccccc";
	this.IsAutoSize = true;
	this.HideWhenMouseOut = true;
	this.ClearWhenHide = false;
	EventHandler.AddObject(this);
	
	this.Iframe.onmouseover = function()
	{
		this.Object.Display = "block";
	}
	this.Iframe.onmouseout = function()
	{
		this.Object.Display = "none";
		this.Object.Window.setTimeout("if(PopWinObject.Display=='none' && PopWinObject.HideWhenMouseOut) PopWinObject.Hide();",2000) 
	}

	this.Show = function (x,y,w,h,html)
	{
		if(html)
			this.Body.innerHTML = html;
		if(h)
			this.Iframe.style.posHeight = h;
		if(w)
			this.Iframe.style.posWidth = w;
		if(y)
			this.Iframe.style.posTop = y;
		if(x)
			this.Iframe.style.posLeft = x;
		this.Iframe.style.display = "block";
		if(this.IsAutoSize)
			this.Window.setTimeout("PopWinObject.AutoSize();",1);
		if(this.OnShow)
			this.Window.setTimeout("PopWinObject.OnShow();",10);
	}
	
	this.AutoSize = function()
	{
		this.Iframe.style.posWidth = this.Body.scrollWidth;
		this.Iframe.style.posHeight = this.Body.scrollHeight;
	}
	
	this.BestPosition = function()
	{
		var x,y,cx,cy;
		cx = document.body.clientWidth - this.ReferX - this.Iframe.style.posWidth;;
		cy = document.body.clientHeight - this.ReferY - this.Iframe.style.posHeight;
		
		if(cx<0)
			this.Iframe.style.posLeft += cx;
		if(cy<0)
			this.Iframe.style.posTop += cy;
	}
	
	this.PopByPoint = function(html,x,y)
	{
		this.ReferX = x;
		this.ReferY = y;
		this.Show(this.ReferX + document.body.scrollLeft,this.ReferY+document.body.scrollTop,null,null,html);
		this.Window.setTimeout("PopWinObject.BestPosition();",100);
	}
	
	this.PopByMouse = function(html)
	{
		this.ReferX = event.x;
		this.ReferY = event.y;
		this.Show(this.ReferX + document.body.scrollLeft,this.ReferY+document.body.scrollTop,null,null,html);
		this.Window.setTimeout("PopWinObject.BestPosition();",100);
	}

	this.Hide = function()
	{
		this.Iframe.style.display = "none";
		if(this.ClearWhenHide)
			this.Iframe.removeNode(true);
	}
	
	this.OnDocumentMouseDown = function()
	{
		this.Hide();
	}
	
}
EventHandler = new Object;

EventHandler.ObjectList = new Array;

EventHandler.AddObject = function(obj)
{
	this.ObjectList[this.ObjectList.length] = obj;
}

document.onmousedown = function()
{
	for(var i = 0; i < EventHandler.ObjectList.length; i++)
	{
		if(EventHandler.ObjectList[i].OnDocumentMouseDown)
		{
			try {EventHandler.ObjectList[i].OnDocumentMouseDown();}
			catch(err) {}
		}
	}
}	
