/***********************************************************************
 * Module:  com.crux.web.jstl.Field
 * Author:  Denny Mahendra
 * Created: May 20, 2005 1:38:23 PM
 * Purpose:
 ***********************************************************************/

package com.crux.web.jstl;

import com.crux.util.JSPUtil;
import com.crux.util.LOV;
import com.crux.util.LogManager;
import com.crux.util.ConvertUtil;
import com.crux.lov.LOVManager;
import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.util.DateUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.Date;

public class Field extends BaseTag implements  ParamTag.ParamAcceptingTag {
   private final static transient LogManager logger = LogManager.getInstance(Field.class);

   private String name;
   private String title;
   private String caption;
   private String type;
   private String flags;
   private String alias;
   private String precision="2";
   private String value;
   private String filegroup;
   private String presentation;
   private String changeaction;
   private String clientchangeaction;
   private String lov;
   private String suffix;
   private String descfield;
   private String descfield2;
   private boolean postback;
   private boolean thumbnail;
   private boolean overrideRO = false;
   private boolean execclientchangeaction;
   private boolean readonly;
   private boolean show = true;
   private boolean include = true;
   private boolean mandatory;
   private boolean popuplov;
   private boolean loveall;
   private int width=100;
   private String rows=null;
   private String cols=null;
   private FrameTag frame;
   private boolean enable;
   private String linkLov;
   private boolean link = false;
   private boolean refresh;
   
   public String getLinkLov() {
      return linkLov;
   }

   public void setLinkLov(String linkLov) {
      this.linkLov = linkLov;
   }

   public String getAlias() {
      return alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public boolean isThumbnail() {
      return thumbnail;
   }

   public void setThumbnail(boolean thumbnail) {
      this.thumbnail = thumbnail;
   }

   public String getFilegroup() {
      return filegroup;
   }

   public void setFilegroup(String filegroup) {
      this.filegroup = filegroup;
   }

   public String getDescfield() {
      return descfield;
   }

   public void setDescfield(String descfield) {
      this.descfield = tlMacro(descfield);
   }

   public String getDescfield2() {
      return descfield2;
   }

   public void setDescfield2(String descfield2) {
      this.descfield2 = tlMacro(descfield2);
   }

   public String getSuffix() {
      return suffix;
   }

   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }

   public boolean isLoveall() {
      return loveall;
   }

   public void setLoveall(boolean loveall) {
      this.loveall = loveall;
   }

   public boolean isOverrideRO() {
      return overrideRO;
   }

   public void setOverrideRO(boolean overrideRO) {
      this.overrideRO = overrideRO;
   }

   public boolean isPopuplov() {
      return popuplov;
   }

   public void setPopuplov(boolean popuplov) {
      this.popuplov = popuplov;
   }
   
   public boolean isRefresh() {
      return refresh;
   }

   public void setRefresh(boolean refresh) {
      this.refresh = refresh;
   }

   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);
      frame = (FrameTag) findInStack(FrameTag.class);

      readonly=false;
   }

   public boolean isExclude() {
      return !include;
   }

   public void setExclude(boolean exclude) {
      this.include = !exclude;
   }

   public boolean isInclude() {
      return include;
   }

   public void setInclude(boolean include) {
      this.include = include;
   }

   public boolean isShow() {
      return show;
   }

   public void setShow(boolean show) {
      this.show = show;
   }

   public boolean isHidden() {
      return !isShow();
   }

   public void setHidden(boolean hidden) {
      this.show = !hidden;
   }

   public boolean isPostback() {
      return postback;
   }

   public void setPostback(boolean postback) {
      this.postback = postback;
   }

   public String getRows() {
      return rows;
   }

   public void setRows(String rows) {
      this.rows=null;
      if (rows!=null)
         if (rows.trim().length()>0)
            this.rows = rows;
   }

   public String getCols() {
      return cols;
   }

   public void setCols(String cols) {
      this.cols=null;
      if (cols!=null)
         if (cols.length()>0)
            this.cols = cols;
   }

   public String getChangeaction() {
      return changeaction;
   }

   public void setChangeaction(String changeaction) {
      this.changeaction = changeaction;
   }

   public String getLov() {
      return lov;
   }

   public void setLov(String lov) {
      this.lov = lov;
   }

   public String getPresentation() {
      return presentation;
   }

   public void setPresentation(String presentation) {
      this.presentation = presentation;
   }

   public int getWidth() {
      return width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public boolean isReadonly() {
      return readonly;
   }
   
   public boolean isEnable() {
      return enable;
   }

   public void setReadonly(boolean readonly) {
      this.readonly = readonly;
   }

   public boolean isMandatory() {
      return mandatory;
   }

   public void setMandatory(boolean mandatory) {
      this.mandatory = mandatory;
   }

   public String getCaption() {
      return caption;
   }

   public void setCaption(String caption) {
      this.caption = caption;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = super.tlMacro(name);
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getFlags() {
      return flags;
   }

   public void setFlags(String flags) {
      this.flags = flags;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public int startTag() throws JspException {
      if (!overrideRO)
         checkRO();

      return EVAL_BODY_INCLUDE;
   }

   private void checkRO() {
      final Boolean dRO = getDefaultRO();

      if (dRO!=null) this.readonly = dRO.booleanValue();
   }

   private String printv(Object property) {
      return JSPUtil.print(property);
   }

   public int endTag() throws JspException {
      if (!include) return 0;

      if (!readonly)
         parentFrame.getForm().enableProperty(name,type);

      if (show)
         if ("standard".equalsIgnoreCase(presentation)) {
            print("<tr><td>"+caption+"</td>");
            print("<td>:</td>");
            print("<td>");
         }

      Object value;

      if (this.value!=null) value=this.value; else value = form.getProperty(name);

      boolean isFile = "file".equalsIgnoreCase(type);

      if (isFile) {
         if (show) {
            FileView f = FileManager.getInstance().getFile((String)value);

            if (thumbnail && f!=null && f.isImage()) {
               print("<div id="+name+"descdiv style=\"cursor:hand\"><img id="+name+"desc style=\"cursor:hand; border:solid 1px black\" alt=\""+f.getStDescription()+"\" src=\"thumb.ctl?EVENT=FILE&fileid="+f.getStFileID()+"&thumb=64\"></div>");

            } else {
               print("<div id="+name+"descdiv style=\"cursor:hand\"><div style=\"width:"+width+"; border:solid 1px black; cursor:hand\" id="+name+"desc>"+parentFrame.jspUtil.print(FileManager.getInstance().getFileDesc((String)value))+"</div></div>");
            }

           
            print("<script>");
            print("var p=docEl('"+name+"descdiv');p.onmousedown=function() {");
            print("if (!this.readOnly && window.event.button==2) uploadDoc(");
            print("{file_id:'"+parentFrame.jspUtil.print(value)+"',clearbutton:'Y',group:'"+parentFrame.jspUtil.print(filegroup)+"',title:'"+caption+"',note:'Select a file for "+caption+"',validate_ext:'',upload_count:1,docName:'"+caption+"'}");
            print(",function(a) {if (a!=null) {docEl('"+name+"').value=a.id;ireload('"+name+"',a)} }");
            print(");");
            print(
                    "if (window.event.button==1) {" +
                    "  window.open('file.cfl?EVENT=FILE&fileid='+docEl('"+name+"').value,'_blank','scrollbars=yes,resizable=yes')" +
                    "}"
            );
            print("};");
            print("p.readOnly="+(readonly?"true":"false"));
            print("</script>");

         }

         print("<input type=hidden id="+name+" name="+name+" value="+parentFrame.jspUtil.print(value)+">");
      } else {
         if (!show) {
         print("<input type=hidden id="+name+" name="+name+" value=\""+printv(value)+"\">");
      } else {

         final boolean popSelect = super.hasChild(PopSelectTag.class);

         if (popSelect) {
            print("<input type=hidden id="+name+" name="+name+" value=\""+printv(value)+"\">");
            printNormalField(name+"_t", null);
         }
         else
            if (lov==null) {

               if ("check".equalsIgnoreCase(type)) {
                  final boolean checked = "Y".equalsIgnoreCase((String)value);
                  print("<input type=checkbox id="+name+" name="+name+" "+(checked?"checked":"")+(readonly?" disabled ":"")+">");
               } else {
                  printNormalField(name, alias);
               }

            } else {
               if (popuplov) {

                  LOV lov;
                  lov = LOVManager.getInstance().getLOV(this.lov, getLovParameters());
                  if (lov==null) lov = (LOV)form.getProperty(this.lov);

                  Object v;
                  v = form.getProperty(name);

                  String df=descfield==null?name+"desc":descfield;

                  if (descfield!=null) {

                     parentFrame.getForm().enableProperty(descfield);
					
			     print(parentFrame.jspUtil.getInputText(
                             df+"|"+caption+'|'+type,
                             null
                             ,form.getProperty(descfield),
                             width,
                             (mandatory?JSPUtil.MANDATORY:0)+
                             (JSPUtil.READONLY)+
                             JSPUtil.NOTEXTMODE
                     ));  

                  } else {
                  	 	print(parentFrame.jspUtil.getInputText(
	                             df+"|"+caption+'|'+type,
	                             null
	                             ,lov.getComboDesc((String)v),
	                             width,
	                             (mandatory?JSPUtil.MANDATORY:0)+
	                             (descfield==null?JSPUtil.READONLY:0)+
	                             JSPUtil.NOTEXTMODE
	                     ));

                     /*
                     print(parentFrame.jspUtil.getInputText(
                             df+"|"+caption+'|'+type,
                             null
                             ,lov.getComboDesc((String)v),
                             width,
                             (mandatory?JSPUtil.MANDATORY:0)+
                             JSPUtil.NOTEXTMODE
                     ));*/

                  }

                  print("<input type=hidden id="+name+" name="+name+" value="+parentFrame.jspUtil.print(v)+">");
                  if (!readonly) {
                     HashMap lovParameters = getLovParameters();

                     String parz = JSPUtil.serialize(lovParameters,"=","&","LVP");

                     if (parz==null) parz = "";
                     
                     String popScript="lovPop('"+name+"','"+df+"','"+caption+"','"+this.lov+"','"+parz+"')";
				 
                     //if (descfield2!=null) popScript+=";docEl('"+descfield2+"').value=docEl('"+df+"').value;";
		
                     print("<button onclick=\""+popScript+"\">...</button>");
                     if(isRefresh()) print("<button name=tes id=tes onclick=\"mform.action_event.value='refresh';mform.submit();\">></button>");
                  }
               } else {
                  LOV lov;
                  lov = LOVManager.getInstance().getLOV(this.lov, getLovParameters());
                  if (lov==null) lov = (LOV)form.getProperty(this.lov);

                  if (loveall) lov.setNullText("ALL");


                  if(lov==null) throw new RuntimeException("LOV not found : "+this.lov);

                  if (value!=null)
                     lov.setLOValue(String.valueOf(value));
                  else
                     lov.setLOValue(null);

                  String n = name;

                  if (descfield!=null) {
                     print("<input type=hidden id="+descfield+" name="+descfield+">");
                     n+="?"+descfield;
                     parentFrame.getForm().enableProperty(descfield);
                  }
				  
				  
                  print(parentFrame.jspUtil.getInputSelect(
                          n+'|'+caption+'|'+type,
                          null
                          ,lov,
                          width,
                          (mandatory?JSPUtil.MANDATORY:0)+
                          (readonly?JSPUtil.READONLY:0)+
                          JSPUtil.NOTEXTMODE
                  ));
                  
                  /*
                  print(parentFrame.jspUtil.getInputSelect(
                          n+'|'+caption+'|'+type,
                          null
                          ,lov,
                          width,
                          (mandatory?JSPUtil.MANDATORY:0)+
                          JSPUtil.NOTEXTMODE
                  ));*/
               }

               if (linked()) {
                  print("<script>");
                  print(" document.getElementById('"+name+"').onLOVLinkChange=function() {");
                  print("    var div = document.createElement('div');");
                  print("    div.style.position='absolute';");
                  print("    div.style.visibility='hidden';");
                  print("    var divname=\"div_FNLink\"+(genCtr++);");
                  print("    div.id=divname;");
                  print("    document.appendChild(div);");
                  print("    var ctlname=escape(this.name);");
                  print("    var ctl = document.getElementById(ctlname);");
                  print("");
                  print("    ctl.options.length=0;");
                  print("    ctl.value='';");
                  print("    o = document.createElement('OPTION');");
                  print("    o.text='[Loading ...]';");
                  print("    ctl.options.add(o);");
                  print("");
                  print("    var linkFuncName = 'LinkFunc'+(genCtr++);");
                  //print("    alert('window.'+linkFuncName+' = function() {document.getElementById(\\''+divname+'\\').removeNode(true);};');");
                  print("    eval('window.'+linkFuncName+' = function() {document.getElementById(\\''+divname+'\\').removeNode(true);};');");
                  print("");
                  print("    div.innerHTML='<iframe src=\""+this.lov+".lov?");
                     
                    // if(linkLov==null){
                     	for (int i = 0; i < childs.size(); i++) {
                        Object o = (Object) childs.get(i);
                        if (o instanceof LOVLinkTag) {
                           final LOVLinkTag link = (LOVLinkTag) o;
                           
                           //if(link.getLink()==null){
                           	    print(link.getName()+"='+document.getElementById('"+link.getLink()+"').value+'&");
                           //}else{
                           		//print(link.getName()+"='+document.getElementById('"+link.getLink()+"').value+'&");
                           //}
                           
                        }
                           /*
                           if (o instanceof ParamTag) {
                            final ParamTag par = (ParamTag) o;

                               if(par.getName().equalsIgnoreCase("datestart"))
                           	    print(par.getName()+"='+document.getElementById('policy.dtPeriodStart').value+'&");
                           }*/
                     	}
                     //}
                     /*else{
                     	for (int i = 0; i < childs.size(); i++) {
                        Object o = (Object) childs.get(i);
                        if (o instanceof LOVLinkTag) {
                           final LOVLinkTag link = (LOVLinkTag) o;

                           print(link.getName()+"='+document.getElementById('"+linkLov+"').value+'&");
                        }
                     
                     }
					}*/

//            print("country='+document.getElementById('address.stCountryID').value+'&");
                     print("ctl='+ctlname+'&callback='+linkFuncName+'\">';");
                     print(" };");

                     for (int i = 0; i < childs.size(); i++) {
                        Object o = (Object) childs.get(i);
                        if (o instanceof LOVLinkTag) {
                           final LOVLinkTag link = (LOVLinkTag) o;

                           if (link.isClientLink())
                              print("document.getElementById('"+link.getLink()+"').VE_onchange=function() {document.getElementById('"+name+"').onLOVLinkChange();};");
                        }
                     }

                     print("</script>");
                  }
              /*    
              if (linked()&&link) {
                  print("<script>");
                  
                     for (int i = 0; i < childs.size(); i++) {
                        Object o = (Object) childs.get(i);
                        if (o instanceof LOVLinkTag) {
                           final LOVLinkTag link = (LOVLinkTag) o;

                           if (link.isClientLink())
                              print("document.getElementById('"+link.getLink()+"').VE_onchange=function() {f.action_event.value='refresh';f.submit();};");
                        }
                     }

                     print("</script>");
                  }*/
               }
         }
      }

      if(clientchangeaction!=null) {

         if (changeaction!=null) {
            clientchangeaction += ";f.action_event.value='"+changeaction+"';f.submit();";
            changeaction=null;
         }

         if ("check".equalsIgnoreCase(type)) {
            print("<script>document.getElementById('"+name+"').onclick=function() {"+clientchangeaction+";};");
            if (execclientchangeaction) print("document.getElementById('\"+name+\"').onclick();");
            print("</script>\n");
         } else {
            print("<script>document.getElementById('"+name+"').VE_onchange=function() {"+clientchangeaction+";};");
            if (execclientchangeaction) print("document.getElementById('\"+name+\"').VE_onchange();");
            print("</script>\n");
         }
      }

      if (changeaction!=null) {
         print("<script>document.getElementById('"+name+"').onchange=function() {f.action_event.value='"+changeaction+"';f.submit();};</script>\n");
      }

      if (show)
         if ("standard".equalsIgnoreCase(presentation)) {
            print("</td></tr>\n");
         }

      return 0;
   }

   public int endTagTest() throws JspException {
      if (!include) return 0;

      if (!readonly)
         parentFrame.getForm().enableProperty(name,type);

      if (show)
         if ("standard".equalsIgnoreCase(presentation)) {
            print("<tr><td>"+caption+"</td>");
            print("<td>:</td>");
            print("<td>");
         }

      Object value;

      if (this.value!=null) value=this.value; else value = form.getProperty(name);

      boolean isFile = "file".equalsIgnoreCase(type);

      if (isFile) {
         if (show) {
            FileView f = FileManager.getInstance().getFile((String)value);

            /*
            if (thumbnail && f!=null && f.isImage()) {
               print("<div id="+name+"descdiv style=\"cursor:hand\"><img id="+name+"desc style=\"cursor:hand; border:solid 1px black\" alt=\""+f.getStDescription()+"\" src=\"thumb.ctl?EVENT=FILE&fileid="+f.getStFileID()+"&thumb=64\"></div>");

            } else {
               print("<div id="+name+"descdiv style=\"cursor:hand\"><div style=\"width:"+width+"; border:solid 1px black; cursor:hand\" id="+name+"desc>"+parentFrame.jspUtil.print(FileManager.getInstance().getFileDesc((String)value))+"</div></div>");
            }*/

            print("<div id="+name+"descdiv style=\"cursor:hand\"><div style=\"width:"+width+"; border:solid 1px black; cursor:hand\" id="+name+"desc>"+parentFrame.jspUtil.print(FileManager.getInstance().getFileDesc((String)value))+"</div></div>");

            /*
            print("<button onclick=uploadFile()>Upload</button>");
            print("<script>");
            print("var p=docEl('"+name+"descdiv');");
            print("function uploadFile(){");
            print("if (!this.readOnly) uploadDoc(");
            print("{file_id:'"+parentFrame.jspUtil.print(value)+"',clearbutton:'Y',group:'"+parentFrame.jspUtil.print(filegroup)+"',title:'"+caption+"',note:'Select a file for "+caption+"',validate_ext:'',upload_count:1,docName:'"+caption+"'}");
            print(",function(a) {if (a!=null) {docEl('"+name+"').value=a.id;ireload('"+name+"',a)} }");
            print(");");

            print("};");
            print("p.readOnly="+(readonly?"true":"false"));
            print("</script>");
            */

            
            print("<script>");


                print("var p=docEl('"+name+"descdiv');");

                print("p.onmousedown=function()");
                print("{");
                    print("if (!this.readOnly && window.event.button==2)");
                        print("uploadDoc({file_id:'"+parentFrame.jspUtil.print(value)+"',clearbutton:'Y',group:'"+parentFrame.jspUtil.print(filegroup)+"',title:'"+caption+"',note:'Select a file for "+caption+"',validate_ext:'',upload_count:1,docName:'"+caption+"', field_name:'"+ name +"'},");
                    print("function(a)");

                    print("{if (a!=null) { ireload2('"+name+"',a,docEl('"+name+"').value, 'Y', '"+caption+"')} });");

                    print("if (window.event.button==1 || window.event.button==0) {");
                        print("window.open('file.cfl?EVENT=FILE&fileid='+docEl('"+name+"').value,'_blank','scrollbars=yes,resizable=yes')}");

                print("};");

                print("p.readOnly="+(readonly?"true":"false"));

             print("</script>");
             


         }

         print("<input type=hidden id="+name+" name="+name+" value="+parentFrame.jspUtil.print(value)+">");
      } else {
         if (!show) {
         print("<input type=hidden id="+name+" name="+name+" value=\""+printv(value)+"\">");
      } else {

         final boolean popSelect = super.hasChild(PopSelectTag.class);

         if (popSelect) {
            print("<input type=hidden id="+name+" name="+name+" value=\""+printv(value)+"\">");
            printNormalField(name+"_t", null);
         }
         else
            if (lov==null) {

               if ("check".equalsIgnoreCase(type)) {
                  final boolean checked = "Y".equalsIgnoreCase((String)value);
                  print("<input type=checkbox id="+name+" name="+name+" "+(checked?"checked":"")+(readonly?" disabled ":"")+">");
               } else {
                  printNormalField(name, alias);
               }

            } else {
               if (popuplov) {

                  LOV lov;
                  lov = LOVManager.getInstance().getLOV(this.lov, getLovParameters());
                  if (lov==null) lov = (LOV)form.getProperty(this.lov);

                  Object v;
                  v = form.getProperty(name);

                  String df=descfield==null?name+"desc":descfield;

                  if (descfield!=null) {

                     parentFrame.getForm().enableProperty(descfield);

			     print(parentFrame.jspUtil.getInputText(
                             df+"|"+caption+'|'+type,
                             null
                             ,form.getProperty(descfield),
                             width,
                             (mandatory?JSPUtil.MANDATORY:0)+
                             (JSPUtil.READONLY)+
                             JSPUtil.NOTEXTMODE
                     ));

                  } else {
                  	 	print(parentFrame.jspUtil.getInputText(
	                             df+"|"+caption+'|'+type,
	                             null
	                             ,lov.getComboDesc((String)v),
	                             width,
	                             (mandatory?JSPUtil.MANDATORY:0)+
	                             (descfield==null?JSPUtil.READONLY:0)+
	                             JSPUtil.NOTEXTMODE
	                     ));

                     /*
                     print(parentFrame.jspUtil.getInputText(
                             df+"|"+caption+'|'+type,
                             null
                             ,lov.getComboDesc((String)v),
                             width,
                             (mandatory?JSPUtil.MANDATORY:0)+
                             JSPUtil.NOTEXTMODE
                     ));*/

                  }

                  print("<input type=hidden id="+name+" name="+name+" value="+parentFrame.jspUtil.print(v)+">");
                  if (!readonly) {
                     HashMap lovParameters = getLovParameters();

                     String parz = JSPUtil.serialize(lovParameters,"=","&","LVP");

                     if (parz==null) parz = "";

                     String popScript="lovPop('"+name+"','"+df+"','"+caption+"','"+this.lov+"','"+parz+"')";

                     //if (descfield2!=null) popScript+=";docEl('"+descfield2+"').value=docEl('"+df+"').value;";

                     print("<button onclick=\""+popScript+"\">...</button>");
                     if(isRefresh()) print("<button name=tes id=tes onclick=\"mform.action_event.value='refresh';mform.submit();\">></button>");
                  }
               } else {
                  LOV lov;
                  lov = LOVManager.getInstance().getLOV(this.lov, getLovParameters());
                  if (lov==null) lov = (LOV)form.getProperty(this.lov);

                  if (loveall) lov.setNullText("ALL");


                  if(lov==null) throw new RuntimeException("LOV not found : "+this.lov);

                  if (value!=null)
                     lov.setLOValue(String.valueOf(value));
                  else
                     lov.setLOValue(null);

                  String n = name;

                  if (descfield!=null) {
                     print("<input type=hidden id="+descfield+" name="+descfield+">");
                     n+="?"+descfield;
                     parentFrame.getForm().enableProperty(descfield);
                  }


                  print(parentFrame.jspUtil.getInputSelect(
                          n+'|'+caption+'|'+type,
                          null
                          ,lov,
                          width,
                          (mandatory?JSPUtil.MANDATORY:0)+
                          (readonly?JSPUtil.READONLY:0)+
                          JSPUtil.NOTEXTMODE
                  ));

                  /*
                  print(parentFrame.jspUtil.getInputSelect(
                          n+'|'+caption+'|'+type,
                          null
                          ,lov,
                          width,
                          (mandatory?JSPUtil.MANDATORY:0)+
                          JSPUtil.NOTEXTMODE
                  ));*/
               }

               if (linked()) {
                  print("<script>");
                  print(" document.getElementById('"+name+"').onLOVLinkChange=function() {");
                  print("    var div = document.createElement('div');");
                  print("    div.style.position='absolute';");
                  print("    div.style.visibility='hidden';");
                  print("    var divname=\"div_FNLink\"+(genCtr++);");
                  print("    div.id=divname;");
                  print("    document.appendChild(div);");
                  print("    var ctlname=escape(this.name);");
                  print("    var ctl = document.getElementById(ctlname);");
                  print("");
                  print("    ctl.options.length=0;");
                  print("    ctl.value='';");
                  print("    o = document.createElement('OPTION');");
                  print("    o.text='[Loading ...]';");
                  print("    ctl.options.add(o);");
                  print("");
                  print("    var linkFuncName = 'LinkFunc'+(genCtr++);");
                  //print("    alert('window.'+linkFuncName+' = function() {document.getElementById(\\''+divname+'\\').removeNode(true);};');");
                  print("    eval('window.'+linkFuncName+' = function() {document.getElementById(\\''+divname+'\\').removeNode(true);};');");
                  print("");
                  print("    div.innerHTML='<iframe src=\""+this.lov+".lov?");

                    // if(linkLov==null){
                     	for (int i = 0; i < childs.size(); i++) {
                        Object o = (Object) childs.get(i);
                        if (o instanceof LOVLinkTag) {
                           final LOVLinkTag link = (LOVLinkTag) o;

                           //if(link.getLink()==null){
                           	    print(link.getName()+"='+document.getElementById('"+link.getLink()+"').value+'&");
                           //}else{
                           		//print(link.getName()+"='+document.getElementById('"+link.getLink()+"').value+'&");
                           //}

                        }
                           /*
                           if (o instanceof ParamTag) {
                            final ParamTag par = (ParamTag) o;

                               if(par.getName().equalsIgnoreCase("datestart"))
                           	    print(par.getName()+"='+document.getElementById('policy.dtPeriodStart').value+'&");
                           }*/
                     	}
                     //}
                     /*else{
                     	for (int i = 0; i < childs.size(); i++) {
                        Object o = (Object) childs.get(i);
                        if (o instanceof LOVLinkTag) {
                           final LOVLinkTag link = (LOVLinkTag) o;

                           print(link.getName()+"='+document.getElementById('"+linkLov+"').value+'&");
                        }

                     }
					}*/

//            print("country='+document.getElementById('address.stCountryID').value+'&");
                     print("ctl='+ctlname+'&callback='+linkFuncName+'\">';");
                     print(" };");

                     for (int i = 0; i < childs.size(); i++) {
                        Object o = (Object) childs.get(i);
                        if (o instanceof LOVLinkTag) {
                           final LOVLinkTag link = (LOVLinkTag) o;

                           if (link.isClientLink())
                              print("document.getElementById('"+link.getLink()+"').VE_onchange=function() {document.getElementById('"+name+"').onLOVLinkChange();};");
                        }
                     }

                     print("</script>");
                  }
              /*
              if (linked()&&link) {
                  print("<script>");

                     for (int i = 0; i < childs.size(); i++) {
                        Object o = (Object) childs.get(i);
                        if (o instanceof LOVLinkTag) {
                           final LOVLinkTag link = (LOVLinkTag) o;

                           if (link.isClientLink())
                              print("document.getElementById('"+link.getLink()+"').VE_onchange=function() {f.action_event.value='refresh';f.submit();};");
                        }
                     }

                     print("</script>");
                  }*/
               }
         }
      }

      if(clientchangeaction!=null) {

         if (changeaction!=null) {
            clientchangeaction += ";f.action_event.value='"+changeaction+"';f.submit();";
            changeaction=null;
         }

         if ("check".equalsIgnoreCase(type)) {
            print("<script>document.getElementById('"+name+"').onclick=function() {"+clientchangeaction+";};");
            if (execclientchangeaction) print("document.getElementById('\"+name+\"').onclick();");
            print("</script>\n");
         } else {
            print("<script>document.getElementById('"+name+"').VE_onchange=function() {"+clientchangeaction+";};");
            if (execclientchangeaction) print("document.getElementById('\"+name+\"').VE_onchange();");
            print("</script>\n");
         }
      }

      if (changeaction!=null) {
         print("<script>document.getElementById('"+name+"').onchange=function() {f.action_event.value='"+changeaction+"';f.submit();};</script>\n");
      }

      if (show)
         if ("standard".equalsIgnoreCase(presentation)) {
            print("</td></tr>\n");
         }

      return 0;
   }

   public String getPrecision() {
      return precision;
   }

   public void setPrecision(String precision) {
      this.precision = precision;
   }

   private void printNormalField(String fieldName, String alias) {
      final boolean textArea = rows!=null;

      Object value = form.getProperty(name);

      if (alias==null) alias=fieldName;

      if (readonly)
         if (value instanceof BigDecimal) {
            BigDecimal v = (BigDecimal) value;
            v=v.setScale(Integer.parseInt(precision), BigDecimal.ROUND_HALF_UP);
            value=v;
         }

      if (textArea) {

         if (cols==null) cols="0";

         print(parentFrame.jspUtil.getInputTextArea(
                 alias+'|'+caption+'|'+type,
                 null
                 ,value,
                 Integer.parseInt(rows),Integer.parseInt(cols),width,
                 (mandatory?JSPUtil.MANDATORY:0)+
                 (readonly?JSPUtil.READONLY:0)+
                 JSPUtil.NOTEXTMODE
         ));

      } else {

         print(parentFrame.jspUtil.getInputText(
                 alias+'|'+caption+'|'+type,
                 null
                 ,value,
                 width,
                 (mandatory?JSPUtil.MANDATORY:0)+
                 (readonly?JSPUtil.READONLY:0)+
                 JSPUtil.NOTEXTMODE
         ));

         if (suffix!=null) print(suffix);

      }
   }

   private boolean linked() {
      if (childs==null) return false;
      for (int i = 0; i < childs.size(); i++) {
         Object o = (Object) childs.get(i);

         if (o instanceof LOVLinkTag) return true;
      }

      return false;
   }

   private HashMap getLovParameters() {
      final HashMap pars = new HashMap();
      
      if (readonly || popuplov)
         pars.put("value",form.getProperty(name));
        
      if (childs==null) return pars;

      for (int i = 0; i < childs.size(); i++) {
         final Object o = childs.get(i);

         if (o instanceof LOVLinkTag) {
            final LOVLinkTag link = (LOVLinkTag) o;
            pars.put(link.getName(),form.getProperty(link.getLink()));
         }

         if (o instanceof ParamTag) {
            final ParamTag par = (ParamTag) o;
            pars.put(par.getName(),par.getValue());
         }

      }

      logger.logDebug("childs:"+childs.size()+" getLovParameters: "+pars); 

      return pars;
   }

   public String getClientchangeaction() {
      return clientchangeaction;
   }

   public void setClientchangeaction(String clientchangeaction) {
      this.clientchangeaction = tlMacro(clientchangeaction);
   }

   public boolean isExecclientchangeaction() {
      return execclientchangeaction;
   }

   public void setExecclientchangeaction(boolean execclientchangeaction) {
      this.execclientchangeaction = execclientchangeaction;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
   
   public void setEnable(boolean enable) {
      this.enable = enable;
   }
   
   public void setLink(boolean link){
   	this.link = link;
   }
   
}
