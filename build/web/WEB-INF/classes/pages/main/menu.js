var header = "<table width=\"160\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" background=\""+imagepath+"/left_bg_frame.jpg\">"+
            "<tr>"+
            "<td width=\"172\" height=\"439\" align=\"left\" valign=\"top\" background=\""+imagepath+"/left_bg_frame.jpg\"><table width=\"172\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
            "<tr>"+
            "<td height=\"20\"></td>"+
            "</tr>";
var footer =   "</table></td>"+
               "</tr>"+
               "</table>";

function getMenuURL(p,x) {
   //if ((p=="null") || (p==null) || (p=='')) return "href=\"#\" onclick=\"return false;\"";
   if ((p=="null") || (p==null) || (p=='')) return x;
   //return "<a alt=\"\" target=\"basefrm\" href=\""+webpath+p+"\">"+x+"</a>";
   //return "<a style=\"cursor:hand\" onclick=\"parent.basefrm.location='"+webpath+p+"';return false;\" class=\"side-link\">"+x+"</a>";
   return "<a href=\"#\" onclick=\"parent.basefrm.location='"+webpath+p+"';return false;\" class=\"side-link\">"+x+"</a>";

}


function renderModule(a) {
   return   "<tr>"+
            "<td height=20 background=\""+imagepath+"/menu_bg.png"+"\" class='menu_title'><b>&nbsp;&nbsp;"+a[1]+"</b></td>"+
            "</tr>";
   /*			    
   return   "<tr>"+
            "<td><a href=\"#\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('master','','"+imagepath+"/main_menuBG.jpg',1)\"><img src=\""+imagepath+"/menu_bg.png\" name=\"master\" width=\"172\" height=\"20\" border=\"0\"></a></td>"+
            "</tr>";*/

}
/*function renderModule(a) {
   return   "<tr>"+
            "<td height=20 background=\""+imagepath+"/main_menuBG.jpg"+"\"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+a[1]+"</b></td>"+
            "</tr>";
   return   "<tr>"+
            "<td><a href=\"#\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('master','','"+imagepath+"/masterA.jpg',1)\"><img src=\""+imagepath+"/master.jpg\" name=\"master\" width=\"172\" height=\"20\" border=\"0\"></a></td>"+
            "</tr>";

} */

function renderSubModule(menu) {
   return "<tr>"+
            "<td height=\"20\" background=\""+imagepath+"/left_bg_frame.jpg\"><table width=\"172\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
            "<tr>"+
            "<td width=\"30\" height=\"20\" align=\"center\"><img src=\""+imagepath+"/submenua.png\" width=\"9\" height=\"10\"></td>"+
            "<td width=\"142\" height=\"20\" align=\"left\">"+getMenuURL(menu[2],menu[1])+"</td>"+
            "</tr>"+
            "</table></td>"+
            "</tr>";      
}

function renderFunction(menu) {
   return "<tr>"+
            "<td height=\"14\" background=\""+imagepath+"/left_bg_frame.jpg\"><table width=\"172\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
            "<tr>"+
            "<td width=\"40\" height=\"14\" align=\"center\" class=\"style3\">-</td>"+
            "<td width=\"132\" height=\"14\" align=\"left\">"+getMenuURL(menu[2],menu[1])+"</td>"+
            "</tr>"+
            "</table></td>"+
            "</tr>";
   
}

function renderSubFunction(menu) {
   return "<tr>"+
            "<td height=\"14\" background=\""+imagepath+"/left_bg_frame.jpg\"><table width=\"172\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
            "<tr>"+
            "<td width=\"50\" height=\"14\" align=\"center\" class=\"style3\">-</td>"+
            "<td width=\"132\" height=\"14\" align=\"left\">"+getMenuURL(menu[2],menu[1])+"</td>"+
            "</tr>"+
            "</table></td>"+
            "</tr>";
}

function renderSubFunction2(menu) {
   return "<tr>"+
            "<td height=\"14\" background=\""+imagepath+"/left_bg_frame.jpg\"><table width=\"172\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
            "<tr>"+
            "<td width=\"60\" height=\"14\" align=\"center\" class=\"style3\">-</td>"+
            "<td width=\"132\" height=\"14\" align=\"left\">"+getMenuURL(menu[2],menu[1])+"</td>"+
            "</tr>"+
            "</table></td>"+
            "</tr>";
}

function renderSubFunction3(menu) {
   return "<tr>"+
            "<td height=\"14\" background=\""+imagepath+"/left_bg_frame.jpg\"><table width=\"172\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
            "<tr>"+
            "<td width=\"70\" height=\"14\" align=\"center\" class=\"style3\">-</td>"+
            "<td width=\"132\" height=\"14\" align=\"left\">"+getMenuURL(menu[2],menu[1])+"</td>"+
            "</tr>"+
            "</table></td>"+
            "</tr>";
}

function renderMenu() {
   var t = '';
   for (var i=0;i<menu.length;i++) {
      var menuid = menu[i][0];

      var n = menuid.indexOf('00');

      if (n<0) n=menuid.length;

      n=(n)/3;

      switch (n) {
         case 1:t+=renderModule(menu[i]);break;
         case 2:t+=renderSubModule(menu[i]);break;
         case 3:t+=renderFunction(menu[i]);break;
         case 4:t+=renderSubFunction(menu[i]);break;
         case 5:t+=renderSubFunction2(menu[i]);break;
         case 6:t+=renderSubFunction3(menu[i]);break;
      }

      /*     if (menu[i][0].lastIndexOf('00.00.00')>0) t+=renderModule(menu[i]);
      else if (menu[i][0].lastIndexOf('00.00')>0) t+=renderSubModule(menu[i]);
      else if (menu[i][0].lastIndexOf('00')>0) t+=renderFunction(menu[i]);
      else t+=renderSubFunction(menu[i]);*/
   }

   var c = header + t + footer;

   //alert(c)

   document.getElementById('content').innerHTML = c;
}