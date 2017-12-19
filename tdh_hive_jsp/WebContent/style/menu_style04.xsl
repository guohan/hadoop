<?xml version="1.0" encoding="gb2312" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">
<HTML><HEAD>
<META http-equiv="Content-Type" content="text/html; charset=GBK"/>
<script>
	var level_1_img="../images/menu/across_1.gif";
	var level_1_height=20;
	var level_1_fontsize=10;
	var level_1_fontcolor="#000066";
	var level_1_position="left";
	var level_3_star="../images/menu/star.gif";
	var level_3_arrow="../images/menu/arrow.gif";
	var level_3_rightspace="110";
</script>
<SCRIPT language="JavaScript" src="../js/crossbrowser.js" type="text/javascript"> ;</SCRIPT>
<SCRIPT language="JavaScript" src="../js/outlook2.js" type="text/javascript"> ;</SCRIPT>
<SCRIPT language="JavaScript" src="../js/menu.js" type="text/javascript"> ;</SCRIPT>
<SCRIPT language="JavaScript" src="../js/pop2.js" type="text/javascript"> ;</SCRIPT>
<script>

  <xsl:apply-templates select="menu">
  <xsl:with-param name="pr" select="''"/>
  </xsl:apply-templates>

    function resetMidFrame(){

    }
<xsl:apply-templates select="menus"/>




</script>
</HEAD>
<BODY onresize="myOnResize();"  onmousewheel="Onwheel()"></BODY></HTML>
</xsl:template>

<xsl:template match="menus">
  <xsl:if test="@displayRight='off'">
    resetMidFrame();
  </xsl:if>
  var o = new createOutlookBar('Bar',0,0,screenSize.width,screenSize.height,'#f7f7f7','blue')
  var p;
  var theitem;

  <xsl:apply-templates select="menu">
  <xsl:with-param name="pr" select="''"/>
  <xsl:with-param name="pr2" select="''"/>
  </xsl:apply-templates>
  o.draw();


</xsl:template>

<xsl:template match="menu">
  <xsl:param name="pr"/>
  <xsl:param name="pr2"/>
  <xsl:if test="@level=0">
	p = new createPanel('M<xsl:value-of select="$pr"/>_<xsl:value-of select="position()-1"/>','<xsl:value-of select="@name"/>','<xsl:value-of select="@showFlag"/>');
  </xsl:if>
  <xsl:if test="@level=1">
	p.addButton('<xsl:value-of select="@img"/>','<xsl:value-of select="@name"/>','<xsl:value-of select="@url"/>','<xsl:value-of select="@color"/>');
  </xsl:if>
  <xsl:if test="@level>1">
    <xsl:if test="count(child::*)=0">
      arrMenuItem<xsl:value-of select="substring($pr,0,string-length($pr))"/>.push(new MenuItem('<xsl:value-of select="@name"/>','','<xsl:value-of select="@url"/>', false));
    </xsl:if>
    <xsl:if test="count(child::*)>0">
      arrMenuItem<xsl:value-of select="substring($pr,0,string-length($pr))"/>.push(new MenuItem('<xsl:value-of select="@name"/>','','<xsl:value-of select="@url"/>', true));
    </xsl:if>
  </xsl:if>
  <xsl:if test="count(child::*)>0">
    <xsl:if test="@level>0">
      var arrMenuItem<xsl:value-of select="$pr"/><xsl:value-of select="position()-1"/>=new Array();
    </xsl:if>   
    <xsl:apply-templates select="menu">
      <xsl:with-param name="pr" select="concat($pr,position()-1, '_')"/>
      <xsl:with-param name="pr2" select="concat($pr,position()-1, '_b')"/>
    </xsl:apply-templates>
    <xsl:if test="@level>0">
      printSubMenu(arrMenuItem<xsl:value-of select="$pr"/><xsl:value-of select="position()-1"/>,'panel<xsl:value-of select="$pr2"/><xsl:value-of select="position()-1"/>' );
    </xsl:if>
  </xsl:if>
  <xsl:if test="@level=0">
	o.addPanel(p);
  </xsl:if>
</xsl:template>
</xsl:stylesheet>
