L_H_APP = "MSN+Hotmail";
H_URL_BASE = "http://help.msn.com/EN_WW";
H_CONFIG = "HotmailPIMv10.ini";
bSearch = false;var L_SignInAB="Sign in"
var L_SignInABcont="Sign In to Messenger"
var L_IsOnline="Online"
var L_IsOffline="Offline"
var L_IsBusy= "Busy"
var L_IsAway="Away"
var L_IsBRB="Be Right Back"
var L_IsOnThePhone="On The Phone"
var L_IsOutToLunch="Out To Lunch"
var L_DloadMess="Download MSN Messenger"
var L_DloadMessCont=" to see who is online, exchange instant messages, and more!"
var L_SMCWEMIMDloadMessCont=" for more cool features."
var L_Connecting="Signing In..."
var L_AppearOffline="Appear Offline"
var L_Failed="Failed"
var L_Down="Unavailable"
var AddMessengerContact=LocalUserEmail=""
var whichIM=null; 
var showContNotification=false;
function MsngrCreateObj() 
{
LocalUserEmail=HMname.innerText.replace(/\s+/g,"")
var webImEnabled = (top != self);
whichIM=null;
var isWin32Present = 0;
try
{
MsngrObj=new ActiveXObject("MSNMessenger.HotmailControl");
MsngrObj.GetLocalUserStatus();		
isWin32Present = 1;
}
catch(e)
{
isWin32Present = 0;
MsngrObj = null;
}	
if (isWin32Present && true == (MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()) && MIU()))
{
whichIM="local"		
InvalidateUI()
return true
}
if (webImEnabled)
{
if (MsngrCreateWebObj())
{
InvalidateUI()
return true	
}		
}
if (isWin32Present)
{
whichIM="local"		
InvalidateUI()
return true;
}
else
{
_NM()
return false
}
}
function MsngrCreateWebObj() 
{
try
{
MsngrObj=self.parent.gMessenger;
if ("undefined"!=typeof(MsngrObj) && MsngrObj)
{
whichIM="web"
document.body.onkeydown=f5
return true;
}
}
catch(e)
{}
return false;
}
function MsngrIsStateOnline(state)
{
return ",2,6,10,14,18,34,50,66,".indexOf(","+state+",") != (-1)
}
function MsngrGetContact(eM,loc)
{
var ret;
var img;
var msg;
var CS=MsngrObj.GetUserStatus(eM);
if (CS == null) return;
switch (CS)
{
case 1:
img=1
msg=L_IsOffline
break;
case 2:
img=0
msg=L_IsOnline
break;
case 10:
img=3
msg=L_IsBusy
break;		
case 14:
img=2
msg=L_IsBRB
break;	
case 18:
case 34:
img=2
msg=L_IsAway
break;
case 50:
img=3
msg=L_IsOnThePhone
break;		
case 66:
img=2
msg=L_IsOutToLunch
break;
}
if (loc=="InBox")
if (CS==1)
ret='<A HREF="/cgi-bin/compose?mailto=1&to='+eM.replace(/\@/,"%40")+'&'+_UM+'"><IMG alt="" src="http://64.4.55.45/icon_messenger'+img+'.gif" border=0></a>';
else
ret='<A HREF="JavaScript:MIM(%22' + eM + '%22)"><IMG alt="" src="http://64.4.55.45/icon_messenger'+img+'.gif" border=0></a>';
else if (loc=="AddressBookList")
{
if (CS==1)
ret='???<A href="#" onclick="DC();return false;"><IMG alt="" src="http://64.4.55.45/icon_messenger'+img+'.gif" border=0></a>??????';
else
ret='???<A HREF="JavaScript:MIM(%22' + eM + '%22)"><IMG alt="" src="http://64.4.55.45/icon_messenger'+img+'.gif" border=0></a>????';
}
return ret;
}
function SCL()
{
if (whichIM=="local")
{
if (MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()) && MIU())
MsngrObj.ShowContactList();
else
MsngrObj.Signin(LocalUserEmail);
}		
else
if (whichIM=="web") 
{
MsngrObj.ShowContactList();
}
}
function MIM(eM)
{
if (MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()) && MIU())
{
if ( MsngrIsStateOnline(MsngrObj.GetUserStatus(eM)) && (LocalUserEmail != eM))
MsngrObj.InstantMessage(eM);
else
MsngrObj.ShowContactList();
}
else
{
MsngrObj.Signin(LocalUserEmail);
}
}
function MCont() {
CA(1);  
var DTa=document.all.ListTable;
var Tms=L_SignInAB;
if (MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()) && MIU())
{
for (i=2; i<DTa.rows.length+-1; i++)
{
var TC=DTa.rows[i].cells[1];
if (DTa.rows[i].name)
{
var E=DTa.rows[i].name.toLowerCase();
if (MsngrObj.GetUserStatus(E)!=0)
TC.innerHTML=MsngrGetContact(E,"AddressBookList");
}
}
}
else
document.all.MEP.innerHTML="<a href='JavaScript:MsngrObj.Signin(\""+LocalUserEmail+"\");'>"+Tms+"</a>";
}
function MContPROMO()
{
CA(1);  
document.all.MEP.innerHTML="<a href='http://g.msn.com/1HMDEN/144??PS=??PS="+HMPS+"'>"+L_DloadMess+"</a>"+L_DloadMessCont;
}
function MIB()
{
CA(1); 
if (MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()))
{
DTa=document.all.MsgTable;
for (i=1; i<DTa.rows.length; i++)
{
if (DTa.rows[i].cells.length>=6)
{
var DRC=DTa.rows[i].cells
for (var n=0;n<DRC.length;n++)
{
for (var c=0;c<DRC[n].children.length;c++)
{
if (DRC[n].children[c].tagName=="A")
DRC[n].children[c].href=DRC[n].children[c].href.replace(/javascript:G\(\'|\'\)/ig,"")+"&"+_UM
}
}
var E=DTa.rows[i].name;
E=E.replace(/\s+/g,"");
if (E.length > 0 && MsngrIsStateOnline(MsngrObj.GetUserStatus(E)))
DTa.rows[i].cells[MessengerCell].innerHTML=MsngrGetContact(E,"InBox");
}
}
}
else
BFX();
}
function SenderFromAllowedDomain(eM)
{
var r=false;
var sd = eM.match(/([\w|-]+\.[\w|-]+)$/);
var l = _MDL.split("|");
if (sd&&sd.length>0&&l.length>0)
{
sd = "@"+sd[0];
for (var i=0;i<l.length;i++)
{
if (l[i]==sd)
{
r=true;
break;
}	
}
} 
return r;
}
function MIR()
{	
var from = document.all.FromText.value.toLowerCase();
if (false==MsngrObj.IsUser(from))
{
if (MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()) && MIU())
{
if (0 == MsngrObj.GetUserStatus(from))
{
if (SenderFromAllowedDomain(from))
{
var TableData=document.all.HMTB.rows[1].children[0].children[0];
var SC=TableData.rows[0].insertCell(2);
var DC=TableData.rows[0].insertCell(3);
SC.style.paddingLeft="1px";
SC.innerHTML='|';
DC.className="P";
DC.onclick=WIR1;
DC.onmouseover=OMO;
DC.onmouseout=OMOU;
DC.innerHTML='<img src="http://64.4.55.45/i.p.instantreply.gif" border=0 align="absmiddle" hspace="1"> Instant Reply';
return;		
}		
}
if (MsngrIsStateOnline(MsngrObj.GetUserStatus(from)))
{
var TableData=document.all.HMTB.rows[1].children[0].children[0];
var SC=TableData.rows[0].insertCell(2);
var DC=TableData.rows[0].insertCell(3);
SC.style.paddingLeft="1px";
SC.innerHTML='|';
DC.className="P";
DC.onclick=IR;
DC.onmouseover=OMO;
DC.onmouseout=OMOU;
DC.innerHTML='<img src="http://64.4.55.45/i.p.instantreply.gif" border=0 align="absmiddle" hspace="1"> Instant Reply';
return;
}	
}	
}
}
function WIR1()
{
if (MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()) && MIU())
{
var disName = "MSNBuddyName=" + escape(document.all.FromText.value.toLowerCase());
if (Err("150995995",true,disName))
document.location="/cgi-bin/doaddresses?_HMaction=Create&addrim="+escape(document.all.FromText.value.toLowerCase());
}
else
{
MsngrObj.Signin();
}	
}
function IR()
{MIM(document.all.FromText.value.toLowerCase());}
function OMO()
{
if (window.event.srcElement.tagName == "IMG")
event.srcElement.parentElement.className="T";
else
event.srcElement.className="T";
}
function OMOU()
{
if (window.event.srcElement.tagName == "IMG")
event.srcElement.parentElement.className="T";
else
event.srcElement.className="P";
}
function MAd(eM)
{
if ("undefined" != typeof(MsngrObj) && MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()) && MsngrObj.GetUserStatus(eM) == 0)
MsngrObj.AddContact(LocalUserEmail,eM);
}
function mSMC()
{
if (MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()) && MIU())
{
if ("undefined" != typeof(document.all.MsngrGif))
document.all.MsngrGif.src="http://64.4.55.45/i.p.im_on.gif"
for (var i=0;i<document.images.length;i++)
{
if (document.images[i].id.length>0)
{
var objImg = document.images[i]
if (ValidateEmail(objImg.id) && MsngrObj.GetUserStatus(objImg.id)!=0)
objImg.parentElement.innerHTML=MsngrGetContact(objImg.id,"InBox")
}
}
}
}
function SMCpro()
{
objTD=document.all.SMCpro;
objTD.style.paddingTop="8px";
if (_wime==0||_wime.length<=0)
objTD.innerHTML="<a href='http://g.msn.com/1HMDEN/144??PS=??PS="+HMPS+"'>"+L_DloadMess+"</a>"+L_DloadMessCont;
else
objTD.innerHTML="<a href='http://g.msn.com/1HMDEN/144??PS=??PS="+HMPS+"'>"+L_DloadMess+"</a>"+L_SMCWEMIMDloadMessCont;
}
function MIU()
{
return MsngrObj.IsUser(LocalUserEmail);
}
var NLN=2,HDN=6,BSY=10,IDL=18,FLN=1,BRB=14,AWY=34,PHN=50,LUN=66,FLD=99,DWN=98,CON=11,UKN=0;
function InvalidateUI()
{	
var bHasMsg = false;
self.parent.document.title = document.title;
if(MsngrObj && "undefined"!=typeof(MsngrObj))
{
var msgrStatus = MsngrObj.GetLocalUserStatus();
if (self.whichIM == "web")
{
bHasMsg = MsngrObj.HasPendingMessages();
if(showContNotification)
WRM();
}
else
{
document.getElementById("SignOut").style.display="none";
document.getElementById("MessOptions").style.display="none";
if (false==(MIU() && MsngrIsStateOnline(MsngrObj.GetLocalUserStatus())))
msgrStatus=FLN; 
if(showContNotification)
WRM();
}
if (true==(MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()) && MIU()))
SCPD();
ShowDropDown(MsngrObj);
ShowInstantMessageMenuItem(MsngrObj);
UpdateUI( MIU() && MsngrIsStateOnline(MsngrObj.GetLocalUserStatus()), msgrStatus, bHasMsg);	
SetAdditonalMessUI();
}
}
function ShowDropDown(obj)
{
if(obj && obj.GetLocalUserStatus() >= 0 )
{
var ele = document.getElementById("newtitledropdown")
if(ele)
ele.style.display = "block";
}
}
function ShowInstantMessageMenuItem(obj)
{
if(obj && obj.GetLocalUserStatus() >= 0 )
{
var ele = document.getElementById("IM_button")
if(ele)
ele.style.display = "block";
}
}
function UpdateIMButton(state)
{
var buttonLink = document.getElementById("IMSend");
if(buttonLink)
{
if(state == 0)
{
buttonLink.disabled = true;
buttonLink.attachEvent('onmouseover',SetMouseOverDefault);
}
else
{
buttonLink.disabled = false;
}
}	
}
function SetTitleDropDownVisibility(status)
{
var dropDownlink = document.getElementById("ImgDrop");
if(dropDownlink)
dropDownlink.style.display = ((status == DWN) ||(status == CON)) ? "none":"inline"
}
function SetMouseOverDefault()
{
document.getElementById("IMSend").style.cursor ='default'
}
function SetTitleDropDownMode(state)
{
document.getElementById("SignIn").disabled = (state != 0) 
document.getElementById("SignOut").disabled = (state == 0)
}
function UpdateUI(state, statusCode, isnew)
{
if(statusCode > -1)
{
var status = L_IsOffline
var imgSrc = ""
var statusTag = document.getElementById("MessStat")
var imgTag = document.getElementById("ImgMessStat")
if(statusTag && imgTag)
{
switch (statusCode)
{
case NLN:
status = L_IsOnline
imgSrc="on"
break;
case CON:
status = L_Connecting
imgSrc="off"
break;
case HDN:
status = L_AppearOffline
imgSrc="off"
break;
case BSY:
status = L_IsBusy
imgSrc="busy"
break;
case IDL:
case AWY:
status = L_IsAway
imgSrc="away"
break;
case LUN:
status = L_IsOutToLunch
imgSrc="lun"
break;
case PHN:
status = L_IsOnThePhone
imgSrc="phn"
break;
case BRB:
status = L_IsBRB
imgSrc="brb"
break;
case FLN:
status = L_IsOffline
imgSrc="off"
break;	
case FLD:
status = L_Failed		
imgSrc="off"
break;
case DWN:
status = L_Down		
imgSrc="off"
break;
default:
status=L_IsOffline
imgSrc="off"
break;
}
imgTag.src="http://64.4.55.45/i.p.im_"+imgSrc+".gif";
statusTag.innerHTML = status;
SetTitleDropDownMode(state);
SetTitleDropDownVisibility(statusCode);
if (typeof(isnew) != "undefined" && true == isnew )
NotifyNewMessage();
return true;
}
}
else return false;	
}
function GetNewAuth()
{
if (!document.getElementById("SignIn").disabled)
MsngrObj.Signin(LocalUserEmail); 
}
function Disconnect()
{
if (!document.getElementById("SignOut").disabled)
self.parent.Disconnect();
}
function MCHWrapper(event,WebIMMenu)
{
if( MsngrObj &&  CON != MsngrObj.GetLocalUserStatus() && DWN != MsngrObj.GetLocalUserStatus())
{
CleanRows();
if(typeof(MsngrObj)!="undefined" && self.whichIM == "web")
{
SetAdditonalMessUI();
SetAdditonalConversations();
}
MCH(event,WebIMMenu);
}
}
function SetAdditonalMessUI()
{
var signOutRowCell = document.getElementById("SignOut");
if(signOutRowCell && self.whichIM=="web")
signOutRowCell.style.display = "block";
var optionsRow = document.getElementById("MessOptions");
if(optionsRow && self.whichIM=="web")
optionsRow.style.display = "block";
var IMButton =  document.getElementById("IM_button");
if(IMButton)
IMButton.style.display = "block"; 
var IMButtonContacts =  document.getElementById("IM_button_contacts");
if(IMButtonContacts)
IMButtonContacts.style.display = "block";
}
function AddConversations(conType, arr)
{
var convTable=document.getElementById("currentConversationsTable");
var r=convTable.insertRow();
if(r!=null)
{
AddRowStyle(r)
var c=r.insertCell();
if(c!=null)
{
if(conType != 0 )
c.style.backgroundColor ="#FFF7E5";
c.innerHTML=
"<span style=\"padding:2px; \"><div onclick=MsngrObj.ShowConversation("+arr[0]+")><img src=\"http://64.4.55.45/"
+ (conType == 0 ? "i.p.conversationbubble.gif" : "i.p.conversationchanges.gif")
+ "\"> " + Truncate(arr[2]) +"</div></span>";
}
}
}
function AddRowStyle(row)
{
if(row)
{
row.className= "W";
row.onmouseover="MO_D()";
row.onmouseout="MU_D()";
}
}
function AddContacts(arr)
{
var convTable=document.getElementById("currentConversationsTable");
var r=convTable.insertRow();
if(r!=null)
{
AddRowStyle(r)
var c=r.insertCell();
if(c!=null)
{
c.innerHTML=
"<div onclick=\"MsngrObj.ShowPendingContact('"+arr[0]+"')\"><img src=\"http://64.4.55.45/i.p.addcontact.gif\">"
+ " " + Truncate(arr[1])+"</div>"
}
}
}
//-----------------------------------------------------------------
// Function : AddSeperator
// Params  : None
// Purpose : Inserts a line as a visual seperator
//-----------------------------------------------------------------
function AddSeperator()
{
var convTable=document.getElementById("currentConversationsTable");
if(convTable)
{
var r=convTable.insertRow();
var c=r.insertCell();
c.className = "V";
c.innerHTML = "<img src=\"http://64.4.55.45/spacer.gif\" height=1 width=1>";
}
}
function SetAdditonalConversations()
{
if(MsngrObj)
{
var cc = MsngrObj.GetCurrentConversations();
if(cc)
{
for(var i =0; i<cc.length ; i++)
AddConversations(cc[i][1] , cc[i]);	
}
var con = MsngrObj.GetPendingContacts();
if(con)
{
for(var i =0; i<con.length ; i++)
{
if(con[i])
AddContacts(con[i]);
}
}
if((cc && cc.length > 0) || (con && con.length >0))
AddSeperator();
}
}
// Function : CleanRows
// Params  : None
// Purpose : Cleans the current conversation rows before instions.
//-----------------------------------------------------------------
function CleanRows()
{
var convTable=document.getElementById("currentConversationsTable");
if(convTable)
{
while (convTable.rows.length >0)
{
convTable.deleteRow(convTable.rows.length - 1);
}
}
}
function NotifyNewMessage()
{
var statusTag = document.getElementById("MessStat");
var imgTag = document.getElementById("ImgMessStat");
if(statusTag && imgTag)
statusTag.innerHTML="<span style=\"background:#FFF7E5; padding:2px; \"><font color=\"#000000\" style=\"\">New Message</font></span>";
}
function Truncate(str)
{
if(!str)
{
return "";
}
if(str.length > 0 && str.length > 22)
return str.substring(0, 22) + "...";	
else
return str;
}
function AddMnsgrContact(eL)
{
var Earr = eL.split("::")
for (el in Earr)
{
MAd(Earr[el])
}
}
function WRM()
{
if(self.whichIM && "web" == self.whichIM)
document.getElementById("resultMess").innerText = "You will see your new Messenger contacts the next time you log in.  NOTE: People who do not use MSN Messenger will need to add you as a Messenger contact in Hotmail.";
else
document.getElementById("resultMess").innerText = "All the contacts were successfully added.";
showContNotification=false;
}
function SCPD()
{
if(document.getElementById("messPrompt")  && !document.getElementById("messPrompt").hasChildNodes())
{ 
var c = document.getElementById("messPrompt").insertCell();
if(c){
c.colSpan=5;
c.innerHTML = "Click on the?<img src='http://64.4.55.45/i.p.im_on.gif'>?icon to send an instant message.";
}
}
}
function f5()
{
var e=window.event
if(e.keyCode==116)
{
document.location.reload()
e.returnValue=false
e.keyCode=0
return false
}
}
var MOL=new Array(); 
function MenuObj(_A,_B,_C,_D,_E,_F,_G,_H,_I)
{
this.name=_A;
this.bOn=_E;
this.bOf=_F;
this.bA=_G;
this.SBS=SBS;
this.showing=false;
this.TM=TM;
document.onclick=MCH;
this.Direction=_I;
MOL[MOL.length]=this;
this.divObj=eval('document.all.' + _B);
this.divStyleObj=eval('document.all.' + _B + '.style');
this.refTDObj=eval('document.all.' + _C);
if (_D)
this.DdTDObj=eval('document.all.' +  _D);
this.frmObj=eval('document.all.' +  _H);
this.strShow='visible';
this.strHide='hidden';
}
function ROP(ObjRef)
{
var theObj=null;
if (ObjRef)
{
if (typeof ObjRef != 'object')
theObj=eval(ObjRef);
else
theObj=ObjRef;
return theObj;
}
else
return false;
}
function TM()
{
if (!this.showing)
{
var RelObjCords=getXY(this.refTDObj);
if (this.Direction)
{
this.divStyleObj.top = this.frmObj.style.top = RelObjCords.top + -this.divObj.offsetHeight;
this.divStyleObj.left = this.frmObj.style.left = RelObjCords.left;
}
else
{
this.divStyleObj.top = this.frmObj.style.top = RelObjCords.top + 18;
this.divStyleObj.left = this.frmObj.style.left = RelObjCords.left;
}
this.frmObj.style.height=this.divObj.offsetHeight;
this.frmObj.style.width=this.divObj.offsetWidth;
var pCurrMenuObj=ROP(this);
CM(this);
this.SBS('clicked');
this.divStyleObj.visibility = this.frmObj.style.visibility = this.strShow;
this.showing=true;
}
else
{
this.divStyleObj.visibility = this.frmObj.style.visibility = this.strHide;
this.showing=false;
this.SBS();
}
}
function CM(callerObj)
{
for (aIndex=0;aIndex < MOL.length; aIndex++)
{
if ((callerObj) && (callerObj.name != MOL[aIndex].name))
{	
if (MOL[aIndex].showing)
{
MOL[aIndex].TM();
MOL[aIndex].SBS();
}
}
else
{
if (MOL[aIndex].showing)
{
MOL[aIndex].TM();
MOL[aIndex].SBS();
}
}
}
}
function MCH(e, srcObj, srcIsMenuDiv)
{
var srcElem;
if (!e)
var e=window.event;
e.cancelBubble=true;
if (srcObj)
{
var pCurrMenuObj=ROP(srcObj); 
if (!srcIsMenuDiv)
pCurrMenuObj.divObj.onclick="MCH(event,"+srcObj+",true)";
pCurrMenuObj.TM();
}
else
CM();
}
function MME(e, srcObj)
{
try
{
if (!e) 
var e=window.event;
var pCurrMenuObj=ROP(srcObj);
if (!pCurrMenuObj.showing)
{
if (e.type == 'mouseover')
pCurrMenuObj.SBS('on');
else if ((e.type == 'mouseout') || (e.type == 'blur'))
pCurrMenuObj.SBS();
}
}
catch(e){}
}
function SBS(wS)
{
if (typeof this.refTDObj != "undefined")
{
if (wS == 'on')
{
if (this.bOn)
{
if (typeof this.DdTDObj != "undefined")
this.DdTDObj.className=this.bOn;
this.refTDObj.className=this.bOn;
}
}
else if (wS == 'clicked')
{
if (this.bA)
{
if (typeof this.DdTDObj != "undefined")
this.DdTDObj.className=this.bA;
this.refTDObj.className=this.bA;
}
}
else
{
if (this.bOf)
{
if (typeof this.DdTDObj != "undefined")
this.DdTDObj.className=this.bOf;
this.refTDObj.className=this.bOf;
}
}
}
}
function getXY(Obj) 
{
for (var sumTop=0,sumLeft=0;Obj!=document.body;sumTop+=Obj.offsetTop,sumLeft+=Obj.offsetLeft, Obj=Obj.offsetParent);
return {left:sumLeft,top:sumTop}
}
function MO(e)
{
if (!e)
var e=window.event;
var S=e.srcElement;
while (S.tagName!="TD")
{S=S.parentElement;}
S.className="T";
}
function MU(e)
{
if (!e)
var e=window.event;
var S=e.srcElement;
while (S.tagName!="TD")
{S=S.parentElement;}
S.className="P";
}
function MOD(e)
{
if (!e)
var e=window.event;
var S=e.srcElement;
while (S.tagName!="TD")
{S=S.parentElement;}
S.className="S";
}
function MUD(e)
{
if (!e)
var e=window.event;
var S=e.srcElement;
while (S.tagName!="TD")
{S=S.parentElement;}
S.className="R";
}
function MO_D(e)
{
if (!e)
var e=window.event;
var S=e.srcElement;
while (S.tagName!="TD")
{S=S.parentElement;}
S.className="X";
}
function MU_D(e)
{
if (!e)
var e=window.event;
var S=e.srcElement;
while (S.tagName!="TD")
{S=S.parentElement;}
S.className="W";
}
function MOD_D(e)
{
if (!e)
var e=window.event;
var S=e.srcElement;
while (S.tagName!="TD")
{S=S.parentElement;}
S.className="Y";
}
function MUD_D(e)
{
if (!e)
var e=window.event;
var S=e.srcElement;
while (S.tagName!="TD")
{S=S.parentElement;}
S.className="Y";
}function DoHL()
{
var e=window.event.srcElement;
while (e.tagName!="TR"){e=e.parentElement;}
if (e.className!='SL') e.className='HL';
}
function DoLL()
{
var e=window.event.srcElement;
while (e.tagName!="TR"){e=e.parentElement;}
if (e.className!='SL')	e.className='';
}
function DoSL()
{
var TB=e=window.event.srcElement;
while (TB.tagName!="TABLE")
{TB=TB.parentElement;}
for (var i=0;i<TB.rows.length;i++){TB.rows[i].className='';}
while (e.tagName!="TR"){e=e.parentElement;}
e.className='SL';
}function DoEmpty(act)
{
if (folderID=="F000000005")
{
frm.action="/cgi-bin/HoTMaiL";
var R=OW("E","399","240","","","no","no","no","no","no","/cgi-bin/dasp/reportjunkPopup.asp?"+_UM+"&type="+act,"modal");
if (R==2)
{
frm.rj.value="yes";
frm.ReportLevel.value="1";
frm.DoEmpty.value="1";
frm.submit()
}
else if (R==1)
{
frm.rj.value="no";
frm.ReportLevel.value="1";
frm.DoEmpty.value="1";
frm.submit()
}
}
else
{
if (Err("150995755",true))
G("http://by17fd.bay17.hotmail.msn.com/cgi-bin/HoTMaiL?DoEmpty=1");
}
}
function Subm(act,first,dosub,e)
{
frm.action="/cgi-bin/HoTMaiL";
if (act=='delete')
{
if (!e)	var e=window.event;
e.cancelBubble=true;
}
if (act=='notbulkmail')
frm.action="/cgi-bin/notbulk";
else if (act=='blocksender')
{
frm.action="/cgi-bin/kill";
frm.ReportLevel.value="0";
}
else if (act=='report')
{
frm.action="/cgi-bin/kill";
frm.ReportLevel.value="1";
}
else if (act=='report_n_block')
{
frm.action="/cgi-bin/kill";
frm.ReportLevel.value="2";
}
num=((first) ? slct1st(frm) : numChecked(frm));
if (num>0)
{
if (folderID=="F000000005" && (act=='delete' || (act=='MoveTo' && frm.tobox.value=="F000000004")) )
{
var R=OW("E","399","240","","","no","no","no","no","no","/cgi-bin/dasp/reportjunkPopup.asp?"+_UM+"&type="+act,"modal");
if (R==2)
{
frm.rj.value="yes";
frm.ReportLevel.value="1";
}
else if (R==1)
{
frm.rj.value="no";
frm.ReportLevel.value="1";
}
else 
dosub=false;
}
frm._HMaction.value=act;
if (dosub)
frm.submit();
}
else
Err("150995745");
}
function PI(act,first,dosub,selValue)
{
if(selValue=="CreateFolder")
G("/cgi-bin/dofolders?Create.x=Create&from=inbox");
else
{
frm.tobox.value=selValue;
Subm(act,first,dosub);
}
}
function numChecked()
{
j=0;
for(i=0;i<frm.length;i++)
{
e=frm.elements[i];
if (e.type=='checkbox' && e.name != 'allbox' && e.checked)
j++;		
}
return j;
}
function slct1st()
{
j=0;
for(i=0;i<frm.length;i++)
{
e=frm.elements[i];
if (e.type=='checkbox' && e.name != 'allbox' && e.checked)
if(j==1) e.checked=false;
else j=1;
}
return j;
}
function Mail(strCmd,e)
{
EncFields();
switch (strCmd)
{
case "Send":
if (!e)	var e = window.event;
e.cancelBubble = true;
if (e.stopPropagation) e.stopPropagation();
Send();
break;
case "Cancel":
Cancel();
break;
case "Save":
Save();
break;
case "AttachFile":
case "AttachContact":
case "AttachPicture":
case "RemoveFile":
Attachment(strCmd);
break;
case "Bcc":
ShowBcc();
break;
case "High":
case "Low":
Importance(strCmd);
break;
case "AddOrigonalText":
AOT();
break;
case "SpellChk":
case "Dictionary":
case "Thesaurus":
LangTool(strCmd);
break;
case "RTE":
TogRTE();
break;
}
}
function Importance(strCmd)
{
var HighDataTD = document.all.HighTD;
var LowDataTD = document.all.LowTD;
if (strCmd=="High")
{
if (IStatus=="" || IStatus=="L")
{
HighDataTD.className='T';
LowDataTD.className='P';
IStatus=frm.importance.value="H";
}
else
{
HighDataTD.className='P';
IStatus=frm.importance.value="";
}
}
else
{
if (IStatus=="" || IStatus=="H")
{
LowDataTD.className='T';
HighDataTD.className='P';
IStatus=frm.importance.value="L";
}
else
{
LowDataTD.className='P';
IStatus=frm.importance.value="";
}
}
}
function SIG()
{
if (frm.sigflag.value && document.hiddentext.sigtext.value.length > 0)
{
if (document.hiddentext.sigtext.value.match(/<html>/)!=null)
{
var _XD = document.all.Xdiv;
_XD.designMode="on";
_XD.innerHTML=document.hiddentext.sigtext.value;
frm.body.value += '\r\n\r\n';
frm.body.value += _XD.innerText;
_XD.innerHTML="";
}
else
{
frm.body.value = '\r\n\r\n'+document.hiddentext.sigtext.value+'\r\n\r\n'+frm.body.value;
}
}
}
function DRFT()
{
if (document.hiddentext.drafttext.value.match(/<html>/)!=null)
{
var _XD = document.all.Xdiv;
_XD.designMode="on";
_XD.innerHTML=document.hiddentext.drafttext.value;
frm.body.value += '\r\n\r\n';
frm.body.value += _XD.innerText;
_XD.innerHTML="";
}
else
{
frm.body.value += document.hiddentext.drafttext.value;
}
}
function FTF() 
{
iCount = 0;
if (document.activeElement != frm.to) 
{
if (iCount >= 0 && iCount < 10) 
{
frm.to.focus();
iCount++;
}
setTimeout("FTF()",0)
}
}
function setIt(H)
{
qF=H;
document.ToInd.src = document.CcInd.src = document.BccInd.src = 'http://64.4.55.45/spacer.gif';
if (H=="to")
document.ToInd.src = 'http://64.4.55.45/i.p.toarrow.gif';
if (H=="cc")
document.CcInd.src ='http://64.4.55.45/i.p.toarrow.gif';
if (H=="bcc")
document.BccInd.src = 'http://64.4.55.45/i.p.toarrow.gif';
}
function MIT(qaName){
if (frm.elements[qF].value.length == 0 || frm.elements[qF].value.indexOf(qaName) == -1) 
{
if (frm.elements[qF].value.length != 0 && frm.elements[qF].value.charAt(frm.elements[qF].value.length - 1) != ",")
frm.elements[qF].value += ",";
frm.elements[qF].value += qaName;
}
}
var aCh="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
var dCh="0123456789";
var asCh=aCh + dCh + "!\"#$%&'()*+,-./:;<=>?@[\]^_`{}~";
var folderID="";
ie=document.all?1:0
function G(UR,t)
{
if (!e)
var e=window.event;
if (e)
e.cancelBubble=true;
if ( top != self )
self.parent.uDataSave(UR+"&"+_UM);
if (t)
window.top.location.href=UR+"&"+_UM;
else
location.href=UR+"&"+_UM;
}
function CallPaneHelp(a,b){CPH(a,b)}
function CPH(T,TD) {
if (T.indexOf(".htm")<0) {
bSearch=true;
H_KEY=T;
L_H_TEXT=TD;
} else { 
bSearch=false;
H_TOPIC=T;
}
DoHelp();
}
function isAlphaNum(S){
var AlphaNum=aCh + dCh;
for (var i=0; i < S.length; i++)
{
if (AlphaNum.indexOf(S.charAt(i)) == -1)
return false;
}
return true;
}
function isASCII(S){
for (var i=0; i < S.length; i++)
{
if (asCh.indexOf(S.charAt(i)) == -1)
return false;
}
return true;
}
function ValidateEmail(S)
{
var R=false;
if (typeof(S) != "undefined")
{
if (/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/.test(S))
R=S;
}
return R;
}
function ValidateLooseEmail(S){
var resultS=S.replace(/ /gi, "");
var atIndex  =resultS.indexOf("@");
var dotIndex =resultS.lastIndexOf(".");
if( resultS == "" || !isASCII(resultS) || dotIndex == -1)
return "";
if ( resultS.lastIndexOf("@") != atIndex || resultS.charAt(atIndex+1) == ".")
return "";
if ( atIndex <= 0 || dotIndex < atIndex ||  dotIndex >= resultS.length-1)
return "";
return resultS;
}
function ValidateDomain(S){
var resultS=S.replace(/ /gi, "");
var atIndex  =resultS.indexOf("@");
var dotIndex =resultS.lastIndexOf(".");
if( resultS=="" || !isASCII(resultS) || dotIndex == -1)
return "";
if ( atIndex > 0 || resultS.charAt(atIndex+1) == "." || dotIndex >= resultS.length-1 )
return "";
return resultS.replace(/@/i, "");
}
function isEmail(S) {
var pass=0;
if (window.RegExp) {
var tempS="a";
var tempReg=new RegExp(tempS);
if (tempReg.test(tempS)) pass=1;
}
if (!pass)
return (S.indexOf(".") > 2) && (S.indexOf("@") > 0);
var r1=new RegExp("(@.*@)|(\\.\\.)|(@\\.)|(^\\.)");
var r2=new RegExp("^[a-zA-Z0-9\\.\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\}\\~]*[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\}\\~]\\@(\\[?)[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3}|[0-9]{1,3})(\\]?)$");
return (!r1.test(S) && r2.test(S));
}
function BFX()
{
DTa=document.all.MsgTable
for (var i=1;i<DTa.rows.length;i++)
{
var DRC=DTa.rows[i].cells
for (var n=0;n<DRC.length;n++)
{
for (var c=0;c<DRC[n].children.length;c++)
{
if (DRC[n].children[c].tagName=="A")
DRC[n].children[c].href=DRC[n].children[c].href.replace(/javascript:G\(\'|\'\)/ig,"")+"&"+_UM
}
}
}
}
function CA(isO,noHL){
var trk=0;
for (var i=0;i<frm.elements.length;i++)
{
var e=frm.elements[i];
if ((e.name != 'allbox') && (e.type=='checkbox'))
{
if (isO != 1)
{
trk++;
e.checked=frm.allbox.checked;
var NotBulkButton=document.all.NotBulkB;
if (frm.allbox.checked)
{
if (!noHL)
hL(e);
if ((folderID == "F000000005") && (trk > 1))
{
NotBulkButton.className='R';
NotBulkButton.children[0].src="http://64.4.55.45/i.p.notjunk.d.gif";
IsNotBulkEnabled=false;
}
}
else
{
if (!noHL)
dL(e);
if (folderID == "F000000005")
{
NotBulkButton.className='P';
NotBulkButton.children[0].src="http://64.4.55.45/i.p.notjunk.gif";
IsNotBulkEnabled=true;
}
}
}
else
{
e.tabIndex=i;
if (folderID != "")
{
var ee=e;
while (ee.tagName!="TR")
{ee=ee.parentElement;}
ee.children[ColspanSize].children[0].tabIndex=i;
}
if (!noHL)
{
if (e.checked)
hL(e);
else
dL(e);
}
}
}
}
}
function CCA(CB,noHL){
if (!noHL)
{
if (CB.checked)
hL(CB);
else
dL(CB);
}
var TB=TO=0;
for (var i=0;i<frm.elements.length;i++)
{
var e=frm.elements[i];
if ((e.name != 'allbox') && (e.type=='checkbox'))
{
TB++;
if (e.checked)
TO++;
}
}
if (folderID == "F000000005")
{
var NotBulkButton=NotBulkButton1=document.all.NotBulkB;
NotBulkButton.className=(TO>1)?'R':'P';
NotBulkButton.children[0].src=(TO>1)?"http://64.4.55.45/i.p.notjunk.d.gif":"http://64.4.55.45/i.p.notjunk.gif";
IsNotBulkEnabled=(TO>1)?false:true;
}
frm.allbox.checked=(TO==TB)?true:false;
}
function doTabIndex(tbleColl)
{
if (tbleColl != null)
{
for (var z=0;z<tbleColl.length;z++)
{
if ((tbleColl.item(z).tagName=='A') || ((tbleColl.item(z).tagName=='INPUT') && (tbleColl.item(z).type!='hidden')) || (tbleColl.item(z).tagName=='SELECT'))
tbleColl.item(z).tabIndex=5;
}
}
}
function Err(Err,bC,EP)
{
bC=bC?1:0;
if (!EP)
EP="";
else
EP="&"+EP;
var UR="http://by17fd.bay17.hotmail.msn.com/cgi-bin/dasp/error_modalshell.asp?Err="+Err+"&IsConf="+bC+EP;
try 
{
var RV=OW("Error","399","217","","","no","no","no","no","no",UR,"modal");
if ( !RV.help && !RV.url )
return RV.state;
if (RV.help)
CPH(RV.help);
if (RV.url)
{
if ('undefined' != typeof(DoSaveMSG) )
DoSaveMSG(RV.url);
else
location.href=RV.url;
}
}
catch(e)
{
location.href=location.href+"&errmsgModal="+Err;
}
}
function DE(D)
{
var R="";
for (var i=0;i<D.length;i++)
{
if (D.charCodeAt(i)>=128)
R += "&#"+D.charCodeAt(i)+";";
else
R += D.charAt(i);
}
return R;
}
function OW(strName,iW,iH,TOP,LEFT,R,S,SC,T,TB,URL,TYPE,dArg)
{
if (TYPE=="modal" || TYPE=="modalIframe")
{
var sF=""
var _rv
sF+=T?'unadorned:'+T+';':'';
sF+=TB?'help:'+TB+';':'';
sF+=S?'status:'+S+';':'';
sF+=SC?'scroll:'+SC+';':'';
sF+=R?'resizable:'+R+';':'';
sF+=iW?'dialogWidth:'+iW+'px;':'';
sF+=iH?'dialogHeight:'+(parseInt(iH)+(S?42:0))+'px;':'';
sF+=TOP?'dialogTop:'+TOP+'px;':'';
sF+=LEFT?'dialogLeft:'+LEFT+'px;':'';
if (TYPE=="modal")
_rv=window.showModalDialog(URL+"&r="+Math.round(Math.random()*1000000),dArg?dArg:"",sF);
else
{
var da=new Object()
da.w=iW;
da.h=iH;
da.url=URL;
_rv=window.showModalDialog("/cgi-bin/dasp/ModalIframe.asp?r="+Math.round(Math.random()*1000000),da,sF);
}
if ("undefined" != typeof(_rv) )
return _rv;
}
else
{
var sF=""
sF+=iW?'width='+iW+',':'';
sF+=iH?'height='+iH+',':'';
sF+=R?'resizable='+R+',':'';
sF+=S?'status='+S+',':'';
sF+=SC?'scrollbars='+SC+',':'';
sF+=T?'titlebar='+T+',':'';
sF+=TB?'toolbar='+TB+',':'';
sF+=TB?'menubar='+TB+',':'';
sF+=TOP?'top='+TOP+',':'';
sF+=LEFT?'left='+LEFT+',':'';
return window.open(URL?URL:'about:blank',strName?strName:'',sF).focus()
}
}
function hL(E){
while (E.tagName!="TR")
{E=E.parentElement;}
E.className="H";
}
function dL(E,S){
while (E.tagName!="TR")
{E=E.parentElement;}
if (typeof(S)!="undefined")
E.className=S;
else
E.className="";
}
function dl(ur,psec)
{
if (typeof(psec)=="undefined")
psec="&";
else
psec+="&";
var dlArg = new Object();
dlArg.url=ur;
var R=OW("E","399","217","","","no","no","no","no","no","/cgi-bin/dasp/getmsg_urlframewarn.asp?psec="+psec+_UM,"modal",dlArg);
if (R==1)
{
document.urlwarnconfirm.urlwarn.value="0";
document.urlwarnconfirm.submit();
}
}
function ol(u)
{
window.open(u,"_blank");
}
function v(){}
function cCBs()
{
tHCBs=tMCBs="";
for (var i=0;i<frm.length;i++)
{
if( (frm[i].name!='CheckAll' && frm[i].name!='imCheckAll') && frm[i].type=='checkbox')
{
if (frm[i].name.substr(0,2)=="im")
tMCBs++;
else
tHCBs++;
}
}
}
function sCA(CB)
{
for (var i=0;i<frm.length;i++)
{
if( (frm[i].name!='CheckAll' && frm[i].name!='imCheckAll') && frm[i].type=='checkbox')
{
if ( (frm[i].name.substr(0,2)=="im") && CB.name.substr(0,2)=="im")
{
if (CB.checked!=frm[i].checked)
frm[i].click();
}
else if ( (frm[i].name.substr(0,2)!="im") && CB.name.substr(0,2)!="im")
{
if (CB.checked!=frm[i].checked)
frm[i].click();
}
}
}
}
function sCCA(CB,S){
if (tHCBs=="" || tMCBs=="")
cCBs();
var DoRoll=true;
if (CB.checked)
{
if (CB.name.substr(0,2)=="im")
{
cMCBs++;
var PairCB=CB.name.substr(2,CB.name.length)
if (frm[PairCB].checked==false && frm[PairCB].type=="checkbox")
{
cHCBs++;
frm[PairCB].checked=true;
}
}
else
cHCBs++
if (cMCBs==tMCBs && bAnyToAddIM)
frm.imCheckAll.checked=true;
if (cHCBs==tHCBs && bAnyToAddHM)
frm.CheckAll.checked=true;
hL(CB);
}
else
{
if (CB.name.substr(0,2)!="im")
{
cHCBs=cHCBs-1;
var PairCB="im"+CB.name;
if ('undefined' != typeof(frm[PairCB]) )
{
if (frm[PairCB].checked==true)
{
cMCBs=cMCBs-1;
frm[PairCB].checked=false;
}
}
}
else
{
cMCBs=cMCBs-1;
var PairCB=CB.name.substr(2,CB.name.length)
if (frm[PairCB].checked==true && frm[PairCB].type=="checkbox")
DoRoll=false;
}
if (cMCBs!=tMCBs && bAnyToAddIM)
frm.imCheckAll.checked=false;
if (cHCBs!=tHCBs && bAnyToAddHM)
frm.CheckAll.checked=false;
if (DoRoll)
dL(CB,S);
}
}
function SPC(e)
{
if (!e)
var e=window.event;
var sE=e.srcElement
var sEC=getXY(sE)
while (sE.tagName!="TR")
{sE=sE.parentElement}
OW(sE.id.replace(/-/g,''),"297","201",sEC.top+screenTop+2-document.body.scrollTop,sEC.left+window.screenLeft+19-document.body.scrollLeft,"no","no","no","no","no","http://by17fd.bay17.hotmail.msn.com/cgi-bin/doaddresses?_HMaction=ShowSpace&contactId="+sE.id)
}

//将当前页数清零（用在页面按钮提交的时候调用）
  function resetPageNum(){
	var page_objForm;
	if(page_objForm==null) page_objForm=document.forms[0];
	//当前页号
//	alert("test");
        var objPageNum;
        objPageNum=page_objForm.pageNum;
        if(objPageNum!=null){
		page_objForm.pageNum.value= 1;
		page_objForm.currentPageNo.value= 1;
        }
	return true;
  }
