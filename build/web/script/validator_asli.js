// author : Denny M
var NS_event=null;
var dtCh= "/";
var minYear=1900;
var maxYear=9999;
var nf = null;
var dimColor = 'transparent';
var mandatory_color = '#FFFFA0';
var editable_color = '#E0FFE0';

document.validator_js = true;

var VC_FLOATDECIMALSEPARATOR = '.';
var VC_FLOATGROUPSEPARATOR = ',';

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }

    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   }
   return this
}

var dateerrmsg='';

function valert(x) {
    dateerrmsg=x;
}

//begin -- add by windu
/**
Wildcard object
this object contain one field property and one method called test()
test() method will check the input parameter string.
*/
function WildCard(wildCard){
    this.wildCard = wildCard;
    this.test = WCARD_test;
    return this;
}

/**
compare each character from input string with specified wildcard string
if the input string is contain wildcard character only, it will return false
otherwise return true.
*/
function WCARD_test(strWCard){
    if(strWCard != null){
        var i;
        var j = 0;
        for(i = 0; i < strWCard.length; i++){
            if(this.wildCard.indexOf(strWCard.charAt(i)) >= 0){
                j++;
            }
        }
        return (j < i);
    }
    return true;
}

var wildCard = new WildCard("*_");
//end ---

function datestrtointeger(dtStr) {
    var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)

    var d = day + (month*31) + (year*31*12);

    return d;
}

function compareDate(dtStr1, dtStr2) {
   //alert('comparedate('+dtStr1+','+dtStr2+')');

   var dt1 = datestrtointeger(dtStr1);
   var dt2 = datestrtointeger(dtStr2);

    if (dt1 > dt2) return 1; else
    if (dt1 < dt2) return -1; else
        return 0;
}

function timestrtointeger(x) {
   var t=x.split(':');
   return (t[0]*60)+parseInt(t[1]);
}

function compareTime(t1,t2) {
   var ti2 = timestrtointeger(t2);
   var ti1 = timestrtointeger(t1);
   if (ti1 > ti2) return 1; else
   if (ti1 < ti2) return -1; else
      return 0;
}

function getDateFromInput(di) {
   var re = new RegExp("^([0-9]+)[\\/]([0-9]+)[\\/]([0-9]+)$",'');
   var ds = re.exec(di);

   if (ds == null) {
      alert('invalid date : '+di);
      return null;
   }

   return new Date(new Number(ds[3]), new Number(ds[2]) - 1, new Number(ds[1]));
}

Date.prototype.addDays = function(dy) {
   this.setDate(this.getDate()+dy);
}

Date.prototype.addMonth = function(mn) {
   this.setMonth(this.getMonth()+mn);
}
//
//Date.prototype.toString = function() {
//   return getDateStr(this);
//}

function getDateStr(dt) {   // MM/DD/YYYY
   return dt.getDate()+'/'+(dt.getMonth()+1)+'/'+dt.getYear();
}

function addDateDays(dt,days) {
   dt.setTime(dt.getTime() + (days*24*60*60*1000));

   return dt;
}

function addDateMonth(dt,month) {
   dt.setMonth(dt.getMonth+month);
   return dt;
}

function getDateDiff(d1,d2) {
   return (d2.getTime() - d1.getTime())/(24*60*60*1000);
}

function addDateMonths(dt,numMonths) {
   var tmp = dt.split('/');
   var returnDate = new Date(tmp[1]+'/'+tmp[0]+'/'+tmp[2]);
   returnDate = new Date(returnDate.getTime());
   var month = returnDate.getMonth() + 1 + parseFloat(numMonths);
   var yearsToAdd = 0;
   if (month > 11) {
		yearsToAdd = Math.floor((month+1)/12);
		month -= 12*yearsToAdd;
   }
   returnDate.setMonth(month);
   returnDate.setFullYear(returnDate.getFullYear() + yearsToAdd);
   return returnDate.getDate() + '/' + returnDate.getMonth() + '/' + returnDate.getFullYear();
}

function getNumDays(dt1, dt2) {
   tmp = dt1.split('/');
   var dtStart = new Date(tmp[1] + '/' + tmp[0] + '/' + tmp[2]);
   tmp = dt2.split('/');
   var dtEnd = new Date(tmp[1] + '/' + tmp[0] + '/' + tmp[2]);
   return (dtEnd.getTime() - dtStart.getTime())/(24*60*60*1000);
}


function isDate(dtStr){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		valert("The date format should be : dd/mm/yyyy")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		valert("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		valert("Please enter a valid day")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		valert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		valert("Please enter a valid date")
		return false
	}
    return true
}

var VM_errmsg='';

function VM_alert(msg) {
    VM_errmsg=msg;
}

var VM_silent = false;

function VD_validate(Vexpression,control,errmsg) {
    if (Vexpression)  return true;
    if (!VM_silent) {
      window.alert(errmsg);
      try {
         if (control.focuscontrol) {
            control.focuscontrol.focus();
            control.focuscontrol.select();
         } else {
            control.focus();
            control.select();
         }
      } catch(e) {
      }
    }
    return false;
}

function VD_notnull(control,errmsg) {
    return VD_validate((control.value != ''),control,errmsg);
};

function VD_integer(control,errmsg) {
    return VD_validate(!isNaN(control.value),control,errmsg)
};

function VD_float(control,errmsg) {
    //window.alert('VD_float'+isNaN(control.value));
    return VD_validate(!isNaN(control.value),control,errmsg)
};

var timeerrmsg='';

function isTime(x) {
   var d = x.split(':');
   if (d.length != 2) {
      timeerrmsg = 'Enter a valid time (hh.mm)';
      return false;
   }

   d[0]=parseInt(d[0]);
   d[1]=parseInt(d[1]);

   if (isNaN(d[0]) ||
       (d[0]<0) ||
       (d[0]>23)
       ) {
       timeerrmsg = 'Enter a valid hour (0..23)';
       return false;
    }

   if (isNaN(d[1]) ||
       (d[1]<0) ||
       (d[1]>59)
       ) {
       timeerrmsg = 'Enter a valid minute (0..59)';
       return false;
    }

    return true;
}

function VD_timevalid(control,errmsg) {
    sTime = control.value;

    if (VM_silent != true) {
       if (sTime.indexOf(":")==-1) {
           control.value=sTime.substr(0,2) + ":" + sTime.substr(2,2);
           sTime=control.value;
       }
    }

    return (
        VD_notnull(control,errmsg)
        && VD_validate(isTime(control.value),control,errmsg + ' ('+timeerrmsg+')')
        );
}

var dateRE = new RegExp('^(([0-9]{8,8})|([0-9]{1,2}[\\/][0-9]{1,2}[\\/][0-9]{1,4}))$');

function VD_datevalid(control,errmsg) {
    sDate = control.value;

    var r = VD_validate((dateRE.test(sDate)),control,errmsg+' (accepted date format is [DDMMYYYY], [DD/MM/YYYY])');

    if (r) {
       if (VM_silent != true) {
          if (sDate.indexOf("/")==-1) {
              sDate=sDate.substr(0,2) + "/" + sDate.substr(2,2) + "/" + sDate.substr(4,4);
          }
       }

       var r = (
           VD_notnull(control,errmsg)
           && VD_validate(isDate(sDate),control,errmsg + ' ('+dateerrmsg+')')
           );

      if (r) control.value = sDate;
   }

   return r;
}

var datetimeRE = new RegExp('^(([0-9]{8,8}[\\ ][0-9]{4,4})|([0-9]{1,2}[\\/][0-9]{1,2}[\\/][0-9]{2,4}[\\ ][0-9]{1,2}[\\:][0-9]{1,2}))$');

function VD_datetimevalid(control,errmsg) {
    sDate = control.value;

    var r = VD_validate((datetimeRE.test(sDate)),control,errmsg+' (accepted date format is [DDMMYYYY HHMM], [DD/MM/YYYY HH:MM])');

    if (r) {
       if (VM_silent != true) {
          if (sDate.indexOf("/")==-1) {
              sDate=sDate.substr(0,2) + "/" + sDate.substr(2,2) + "/" + sDate.substr(4,4) + " " + sDate.substr(9,2) + ":" + sDate.substr(11,2) ;
          }
       }

       var r = (
           VD_notnull(control,errmsg)
           && VD_validate(isDateTime(sDate),control,errmsg + ' ('+dateerrmsg+timeerrmsg+')')
           );

      if (r) control.value = sDate;
   }

   return r;
}

function isDateTime(sdx) {
   var s=sdx.split(' ');
   if (isDate(s[0]))
      return isTime(s[1]);
   else
      return false;
}

function VD_money(V) {
   if (!V.RE.test(V.control.value)) return false;

   return V.floatRE.test(moneytofloat(V.control.value));
}

//var reEMAIL=new RegExp("^[0-9a-zA-Z\_\.\-]+[\@][0-9a-zA-Z\.\_\-]+$");
var reEMAIL=new RegExp("^[0-9a-zA-Z\_\-]+([\\.][0-9a-zA-Z\_\-]+)*[\\@][0-9a-zA-Z\_\-]+([\\.][0-9a-zA-Z\_\-]+)+$");

function VD_email(control,errmsg) {
    return VD_validate(reEMAIL.test(control.value),control,errmsg);
}

function VD_datesequence(control1, control2, errmsg) {
    if ((!VM_isrequired(control2)) && (control2.value == '')) return true;
    if ((!VM_isrequired(control1)) && (control1.value == '')) return true;
    return (
        VD_validate(
            (compareDate(control1.value,control2.value) == -1 || ((control1.fAllowEqual != false) && compareDate(control1.value,control2.value) == 0)),
            control1,
            errmsg)
    );
}

function VD_datesequenceinverse(control1, control2, errmsg) {
    if ((!VM_isrequired(control2)) && (control2.value == '')) return true;
    if ((!VM_isrequired(control1)) && (control1.value == '')) return true;
    return (
        VD_validate(
            (compareDate(control1.value,control2.value) == 1 || ((control1.fAllowEqual != false) && compareDate(control1.value,control2.value) == 0)),
            control1,
            errmsg)
    );
}

function VD_timesequence(control1, control2, errmsg) {
    if ((!VM_isrequired(control2)) && (control2.value == '')) return true;
    if ((!VM_isrequired(control1)) && (control1.value == '')) return true;
    return (
        VD_validate(
            (compareTime(control1.value,control2.value) == -1 || compareTime(control1.value,control2.value) == 0),
            control1,
            errmsg)
    );
}

function VD_dateconstrain(ctl, dstart,dend, errmsg) {
   var o = ctl;
   if(ctl.options != null){ //if combobox get the selected option
        o = ctl.options[ctl.selectedIndex];
   }

   if (o) {

      var mindate = o.getAttribute("ACTIVE_DATE");

      if (
         (dstart.datemin == null) ||
         (dstart.datemin == "") ||
         (compareDate(mindate,dstart.datemin)>0)
         ) {
         dstart.datemin = mindate;
      }

      var maxdate = o.getAttribute("INACTIVE_DATE");

      if (
         (dend.datemax == null) ||
         (dend.datemax == "") ||
         (compareDate(maxdate,dend.datemax)>0)
         ) {
         dend.datemax = maxdate;
      }


      return (
         VM_validate(dstart.validateobject) &&
         VM_validate(dend.validateobject)
         );

   }
}

function VD_mindate(d1,d2) {
   if (d1 == null) d1 = "";
   if (d2 == null) d2 = "";

   d1 = d1.trim();
   d2 = d2.trim();

   var min = d1;

   if (min == "") min = d2; else
      if (compareDate(d1,d2)>0) min = d2;

   return min;
}

function VD_maxdate(d1,d2) {
   if (d1 == null) d1 = "";
   if (d2 == null) d2 = "";

   d1 = d1.trim();
   d2 = d2.trim();

   var max = d1;

   if (max == "") max = d2; else
      if (compareDate(max,d2)<0) max = d2;

   return max;
}

function VD_dateconstrain2(ctl1,ctl2,errmsg) {

   var V = ctl1.Vdateconstrain;

   var param = V.constraincontrols;

   V.datemin = "";
   V.datemax = "";

   var i,s;

   s='controls = ['+ctl1.id+','+ctl2.id+']\n';

   for (i=0;i<param.length;i++) {

      var o = param[i];

      //if combobox get the selected option
      if(o.options != null){
        o = o.options[param[i].selectedIndex];
      }

      V.datemax = VD_maxdate(V.datemax,o.getAttribute("ACTIVE_DATE"));
      V.datemin = VD_mindate(V.datemin,o.getAttribute("INACTIVE_DATE"));

      s+=param[i].id+' constrain = ['+o.getAttribute("ACTIVE_DATE")+']-['+o.getAttribute("INACTIVE_DATE")+']\n';
   }

   ctl1.datemin = V.datemax;
   ctl2.datemax = V.datemin;

   //alert(s+'Date constrain = [ACTIVE_DATE>'+ctl1.datemin+']-[INACTIVE_DATE<'+ctl2.datemax+']');

   if ((ctl2.value == '') && (ctl2.datemax.length > 0)) {
      return VD_validate(false,ctl2,'Inactive date cannot be empty, and should be earlier than '+ctl2.datemax);
   }

   return (
      VM_validatecontrol(ctl1,V) &&
      VM_validatecontrol(ctl2,V)
      );

}

function VM_validatecontrol(ctl,exceptV) {
   var i;

   for (i=0;i<VAP.length;i++) {
      var V = VAP[i];

      if (V == exceptV) continue;

      if ((V.control == ctl) || (V.control2 == ctl)) {
         if (!VM_validate(V)) return false;
      }
   }

   return true;
}

var VAP = new Array();
var prvControl = null;

function newVAP() {
    var i = VAP.length;

    VAP[i] = new Object();

    VAP[i].id = 'VAP' + i.toString();
    VAP[i].validate = true;

    return VAP[i];
};

function VH_setmaxlen(V,maxlen) {
    if (maxlen.length == null) {
        V.minlen = 0; V.maxlen = maxlen;
    } else {
        V.minlen=maxlen[0]; V.maxlen=maxlen[1];
    }

    if (V.control.maxLength != null)
        V.control.maxLength = V.maxlen;
    else
        V.control.setAttribute('maxlength',V.maxlen);
}
//added "=" character by daniel for url in function and subfunction
//added "~" character by pram
var defaultFilterSrc='[a-zA-Z0-9 \\.\\/\\*\\-\\(\\)\\$\\@\\,\\?\\;\\:\\[\\]\\{\\}\\_\\!\\#\\^\\=\\~\\n\\r\\|\\+\\&\\%\\\']';

var defaultFilter = new RegExp(defaultFilterSrc);

var defaultSFilter = new RegExp('^'+defaultFilterSrc+'*$');

var strictFilterSrc = '[a-zA-Z0-9 \\.\\*\\_\\-]';

var strictFilter = new RegExp(strictFilterSrc);

var strictSFilter = new RegExp('^'+strictFilterSrc+'*$');

function callfunc(x) {
   if (typeof x == 'string') eval(x); else x();
}

function VH_keypressNS(event){

       ctl = event.currentTarget;

        var isupper = (ctl.getAttribute("uppercase") != null);

        if (isupper) {
            var sKey=String.fromCharCode(event.which);

            event.which = sKey.toUpperCase().charCodeAt(0);
        }

        var sKey=String.fromCharCode(event.which);

        var V = event.currentTarget.validateobject;

        if (ctl.onEnterKey != null) {
          if (sKey == '\r') {
            event.returnValue=false;
            callfunc(ctl.onEnterKey);
            return false;
          }
        }

        if (sKey == '\r') {
           //event.returnValue=false;    // use this to cancel enter button
           //return false;

           if (V.control.keyevt != null)
             return V.control.keyevt();

           return true;
        }

        if (V.control.getAttribute('keyword') != null) {
         if ((sKey == '*') || (sKey == '_')) return true;
        }

        if (sKey != '\r') {
           if (ctl.getAttribute('strict') != null) {
              if (!strictFilter.test(sKey)) {
                  event.cancelBubble=true;
                  return false;
              }
           }

           var sFilter=ctl.getAttribute("filter");

           if (sFilter == null)
              if (!defaultFilter.test(sKey)) {
                  event.cancelBubble=true;
                  return false;
              }

		     if (ctl.maxLength == null)
               if (ctl.getAttribute('maxlength') != null) {
                   if (ctl.value.length >= ctl.getAttribute('maxlength'))
                       event.cancelBubble=true;
                   }
           if (ctl.fKeypress && ctl.fKeypress()==false)
               event.cancelBubble=true;



           if(sFilter){

               var re=new RegExp(sFilter);
               // Do not filter out ENTER!
               if(sKey!="\r" && !re.test(sKey)) {
                   //alert('failed:'+sKey);
                   event.cancelBubble=true;
               }

               try{
                  event.which=sKey.charCodeAt(0);
               }
               catch(e) {
               }
           }

           if (V.control.value != '') {
               var RE = this.V.fRE;
               //alert(RE.toString());
               if (RE) {
                   //var lSr = V.control.value;
                   var Sr = valueprediction(V.control,sKey);
                   //var oR = document.selection.createRange();
                   //var oRbm = oR.getBookmark;
                   //var orx = oR.boundingLeft;
                   //var ory = oR.boundingTop;
                   //oR.text = '';
                   //oR.text = sKey;
                   //oR.collapse();
                   //document.selection.createRange().moveToPoint(orx,ory);

                   //var Sr = V.control.value;
                   if (!RE.test(Sr)) {
                       event.cancelBubble=true;
                       return false;
                       //V.control.value = lSr;
                       //oR = document.selection.createRange();
                       //oR.moveToBookmark(oRbm);
                   }
                   //document.selection.createRange().collapse(false);
                   //event.returnValue=false;

                   //eval('alert("ads")');
              }
           }
        }

        //alert(window.event.returnValue);

        if (V.control.keyevt != null)
            return V.control.keyevt();

         return true;
    };

function VH_keypress(e){
      if (e) return VH_keypressNS(e);

        var isupper = (this.getAttribute("uppercase") != null);

        if (isupper) {
            var sKey=String.fromCharCode(event.keyCode);

            event.keyCode = sKey.toUpperCase().charCodeAt(0);
        }

        var sKey=String.fromCharCode(event.keyCode);

        var V = event.srcElement.validateobject;

        var ctl = V.control;

        if (ctl.onEnterKey != null) {
          if (sKey == '\r') {
            event.returnValue=false;
            callfunc(ctl.onEnterKey);
            return false;
          }
        }

        if (sKey == '\r') {
           //event.returnValue=false;    // use this to cancel enter button
           //return false;

           if (V.control.keyevt != null)
             return V.control.keyevt();

           return true;
        }

        if (V.control.getAttribute('keyword') != null) {
         if ((sKey == '*') || (sKey == '_')) return true;
        }

        if (sKey != '\r') {
           if (this.getAttribute('strict') != null) {
              if (!strictFilter.test(sKey)) {
                  event.returnValue=false;
                  return false;
              }
           }

           var sFilter=this.getAttribute("filter");

           if (sFilter == null)
              if (!defaultFilter.test(sKey)) {
                  event.returnValue=false;
                  return false;
              }

		     if (this.maxLength == null)
               if (this.getAttribute('maxlength') != null) {
                   if (this.value.length >= this.getAttribute('maxlength'))
                       window.event.returnValue = false;
                   }
           if (this.fKeypress && this.fKeypress()==false)
               event.returnValue=false;



           if(sFilter){

               var re=new RegExp(sFilter);
               // Do not filter out ENTER!
               if(sKey!="\r" && !re.test(sKey)) {
                   //alert('failed:'+sKey);
                   event.returnValue=false;
               }

               try{
                  event.keyCode=sKey.charCodeAt(0);
               }
               catch(e) {
               }
           }

           /*if (V.control.value != '') {
               var RE = this.V.fRE;
               //alert(RE.toString());
               if (RE) {
                   //var lSr = V.control.value;
                   var Sr = valueprediction(V.control,sKey);
                   //var oR = document.selection.createRange();
                   //var oRbm = oR.getBookmark;
                   //var orx = oR.boundingLeft;
                   //var ory = oR.boundingTop;
                   //oR.text = '';
                   //oR.text = sKey;
                   //oR.collapse();
                   //document.selection.createRange().moveToPoint(orx,ory);

                   //var Sr = V.control.value;
                   if (!RE.test(Sr)) {
                       event.returnValue=false;
                       return false;
                       //V.control.value = lSr;
                       //oR = document.selection.createRange();
                       //oR.moveToBookmark(oRbm);
                   }
                   //document.selection.createRange().collapse(false);
                   //event.returnValue=false;

                   //eval('alert("ads")');
              }
           }*/
        }

        //alert(window.event.returnValue);

        if (V.control.keyevt != null)
            return V.control.keyevt();

         return true;
    };


function valueprediction(ctl, key) {
   //alert(document.selection.createRange()
   //return ctl.value + key;

   var V = ctl.validateobject;
   var cr = document.selection.createRange();
   var seltext = cr.text;

   if (V.defaultrange == null) {
      V.defaultrange = cr.duplicate();
      V.defaultrange.expand('textedit');
   }

   var dr = V.defaultrange;

   var x = 0;
   while (dr.compareEndPoints('StartToStart',cr)<0) {
      cr.move('character',-1);
      x++;
   }

   var src = ctl.value;

   src =  src.substring(0,x)+key+src.substring(x+seltext.length,src.length);

   //alert(src);

   return src;

   //alert('range = '+x+' '+seltext.length+' characters');
   //document.selection.createRange().text = key;

}

function VH_assignfilter(V) {
    V.control.V = V;

    if (V.control.onkeypress != VH_keypress) {// prevent double circular assigns
        V.control.keyevt = V.control.onkeypress;
        V.control.onkeypress=VH_keypress;
    }

}


function VH_setdaterange(p0,p1) {
   this.control.datemin=p0;
   this.control.datemax=p1;

   return this;
}

function VH_settimerange(p0,p1) {
   this.control.timemin=p0;
   this.control.timemax=p1;

   return this;
}

function VH_setintegerrange(p0,p1) {
   this.control.integermin=p0;
   this.control.integermax=p1;

   return this;
}

function VH_setconstrain(params) { // params is an array of object, which each object is combo box control, which each of its entry (option) has effective date values
   this.constraincontrols = params;

   var i;
   for (i=0;i<params.length;i++) {
       VA_trigger(params[i],params[i].getAttribute['fieldname'],this);
   }
}

function VH_setvaluemoney(val) {
   val = moneytofloat(val);
   if (isNAN(val)) {
      this.value = '';
      return;
   }

   this.value = floattomoney(val,this.validateobject.floatconst[1]);
}

function VH_setvaluemoney2(val) {
   val = unFormatted(val);
   if (!isNaN(val) && val != '') {
      if (document.numberFormat_js == null) {
         window.alert('you must include numberFormat.js to format number');
         return
      }
      if (nf == null) {
         nf = new NumberFormat();
      }
      nf.setNumber(new Number(val).toFixed(this.validateobject.floatconst[1]));
      nf.setPlaces(this.validateobject.floatconst[1]);
      nf.setSeparators(true,nf.COMMA);
      nf.setCurrency(false);
      this.value = nf.toFormatted();
   } else {
      this.value='';
   }
}

function VH_setvaluefloat(val) {
   if (!isNaN(val) && (val!='')) {
      this.value = new Number(val).toFixed(this.validateobject.floatconst[1]);
   } else {
      this.value='';
   }
}

function VH_setvalueinteger(val) {
   if (!isNaN(val) && (val!='')) {
      this.value = Math.floor(parseFloat(this.value));
   } else {
      this.value='';
   }
}

function VH_datesequenceinverse() {

   if (this.inverted == null)
      this.inverted = true;
   else
      this.inverted = !this.inverted;

   return this;
}

function VH_datesequencenoEqual() {
   this.control.fAllowEqual = false;
}

function VH_setfloatrange(amin,amax) {
   this.control.floatmin = amin;
   this.control.floatmax = amax;

   return this;
}

function VH_rejectzero() {
   this.fnozero = true;

   return this;
}

function VH_allowzero() {
   this.fnozero = false;

   return this;
}

function VH_isZeroAllowed(V) {
   var p = V.fnozero != true;
   if (p) {
      return true;
   }

      if (V.control.integermin >= 0) return true;
      if (V.control.integermax <= 0) return true;

      if (V.control.floatmin >= 0) return true;
      if (V.control.floatmax <= 0) return true;

   return false;
}

function VH_setdatatype(V,dt,params) {
    if (dt.substr(0,5) == 'float') {
        dtp = dt.substr(5);
        //if (dtp != null)
        {
            var pf = dtp.split('.');

            var dt = 'float';
            V.floatconst = pf;
            pf[0] = pf[0] - pf[1];
            //if (pf[1]>0) pf[0] = pf[0] - 1;

            //alert('^[0-9]{1,' + pf[0] + '}([.][0-9]{1,' + pf[1] + '})?$');
            V.RE = new RegExp('^[-]?[0-9]{1,' + pf[0] + '}([\\'+VC_FLOATDECIMALSEPARATOR+'][0-9]{1,' + pf[1] + '})?$');
            V.fRE = new RegExp('^[-]?[0-9]{0,' + pf[0] + '}(([\\'+VC_FLOATDECIMALSEPARATOR+'][0-9]{0,' + pf[1] + '})|([\\'+VC_FLOATDECIMALSEPARATOR+']?))?$');
            //maxlen = new Number(pf[0])+new Number(pf[1])+1;
            maxlen = 100;
            VH_setmaxlen(V,maxlen);
            //window.alert('^[0-9]{0,' + pf[0] + '}[.]?[0-9]{0,' + pf[1] + '}?$');
            //window.alert('Float : ('+pf[0]+'.'+pf[1]+')');
            V.control.setvalue = VH_setvaluefloat;
            V.control.setvalue(V.control.value);
            V.setrange = VH_setfloatrange;
            V.nozero = VH_rejectzero;
            V.allowzero = VH_allowzero;

            if (VM_isrequired(V.control)) V.nozero();
        }
    }

    if (dt.substr(0,5) == 'money') {
        dtp = dt.substr(5);
        //if (dtp != null)
        {
            var pf = dtp.split('.');

            if (pf[1]!='0') {
               var dt = 'money';
               V.floatconst = pf;
               pf[0] = pf[0] - pf[1];
               V.RE = new RegExp('^[-]?((([0-9]{1,3})?([\\'+VC_FLOATGROUPSEPARATOR+'][0-9]{3}){0,'+Math.floor(pf[0]/3)+'})|([0-9]{1,'+pf[0]+'}))(([\\'+VC_FLOATDECIMALSEPARATOR+'][0-9]{1,'+pf[1]+'})?)$');
               V.floatRE = new RegExp('^[-]?[0-9]{1,' + pf[0] + '}([\\'+VC_FLOATDECIMALSEPARATOR+'][0-9]{1,' + pf[1] + '})?$');
               V.fRE = new RegExp('^[-]?(([0-9]{1,3}(((([\\'+VC_FLOATGROUPSEPARATOR+'][0-9]{3}){0,'+Math.floor(pf[0]/3-1)+'})(([\\'+VC_FLOATGROUPSEPARATOR+'][0-9]{0,3})?))|(([\\'+VC_FLOATGROUPSEPARATOR+'][0-9]{3}){0,'+Math.floor(pf[0]/3)+'}(([\\'+VC_FLOATDECIMALSEPARATOR+'][0-9]{0,'+pf[1]+'}$)?))))|([0-9]{1,'+pf[0]+'})(([\\'+VC_FLOATDECIMALSEPARATOR+'][0-9]{0,'+pf[1]+'})?))$');

            }
            else {
               var dt = 'money';
               V.floatconst = pf;
               pf[0] = pf[0] - pf[1];
               V.RE = new RegExp('^[-]?((([0-9]{1,3})?([\\'+VC_FLOATGROUPSEPARATOR+'][0-9]{3}){0,'+Math.floor(pf[0]/3)+'})|([0-9]{1,'+pf[0]+'}))$');
               V.floatRE = new RegExp('^[-]?[0-9]{1,' + pf[0] + '}$');
               V.fRE = new RegExp('^[-]?(([0-9]{1,3}(((([\\'+VC_FLOATGROUPSEPARATOR+'][0-9]{3}){0,'+Math.floor(pf[0]/3-1)+'})(([\\'+VC_FLOATGROUPSEPARATOR+'][0-9]{0,3})?))|(([\\'+VC_FLOATGROUPSEPARATOR+'][0-9]{3}){0,'+Math.floor(pf[0]/3)+'})))|([0-9]{1,'+pf[0]+'}))$');

            }

            maxlen = 100;
            VH_setmaxlen(V,maxlen);
            V.control.setvalue = VH_setvaluemoney;
            V.control.setvalue(V.control.value);
            V.setrange = VH_setfloatrange;
            V.nozero = VH_rejectzero;
            V.allowzero = VH_allowzero;

            if (VM_isrequired(V.control)) V.nozero();
        }
    }

    V.datatype = dt;

    if (dt == "integer") {
        V.control.setAttribute('integer','');
        V.control.setAttribute('filter','[\\-0-9]');
        V.control.setvalue = VH_setvalueinteger;
        V.control.setvalue(V.control.value);
        V.setrange = VH_setintegerrange;
        V.nozero = VH_rejectzero;
        V.allowzero = VH_allowzero;
    } else
    if (dt == "datesequence") {
        V.control2 = params;
        V.noEqual = VH_datesequencenoEqual;
        return;
    } else
    if (dt == "datesequence2") {
        V.control2 = params;
        V.noEqual = VH_datesequencenoEqual;
        V.inverse = VH_datesequenceinverse;
        return;
    } else
    if (dt == "timesequence") {
        V.control2 = params;
        return;
    } else
    if (dt == "dateconstrain") {
        //V.datestart = params[0];
        //V.dateend = params[1];

        V.control2 = params;
        V.setconstrain = VH_setconstrain;
        V.control.Vdateconstrain = V;
        return;
    } else
    if (dt == "TriggerV") {
    } else
    if (dt == "keyword") {
		V.control.setAttribute('filter','[a-zA-Z0-9.\\-\\_ \\*]'); //--added by pram
        return VH_setdatatype(V,"string",params);
    } else
    if (dt == "quantity") {
      return VH_setdatatype(V,"money"+params+".0");
    } else
    if (dt == "name") {
		V.control.setAttribute('filter','[a-zA-Z0-9.\\-\\_ ]'); //--added by pram
        return VH_setdatatype(V,"string",params);
    } else
    if (dt == "policyno") {
		 V.control.setAttribute('filter','[0-9\\-\\/\\.]');
	    return VH_setdatatype(V,"string",25);
    } else
    if (dt == "policynokeyword") {
		 V.control.setAttribute('filter','[a-zA-Z0-9\\-\\/\\.\\_\\*]');
	    return VH_setdatatype(V,"string",25);
    } else
    if (dt == "certno") {
		V.control.setAttribute('filter','[a-zA-Z0-9\\-\\/\\.]');
	    return VH_setdatatype(V,"string",params);
    } else
    if (dt == "certnokeyword") {
		V.control.setAttribute('filter','[a-zA-Z0-9\\-\\/\\.\\_\\*]');
	    return VH_setdatatype(V,"string",params);
    } else
    if (dt == "collateralno") {
		V.control.setAttribute('filter','[0-9]');
	    return VH_setdatatype(V,"string",30);
    } else
    if (dt == "appconno") {
		V.control.setAttribute('filter','[0-9]');
	    return VH_setdatatype(V,"string",15);
    } else
    if (dt == "voucherno") {
		V.control.setAttribute('filter','[0-9]');
	    return VH_setdatatype(V,"string",20);
    } else
    if (dt == "bankaccountno") {
		V.control.setAttribute('filter','[0-9a-zA-Z\\.\\-]');
	    return VH_setdatatype(V,"string",params);
    } else
    if (dt == "vouchernokeyword") {
		V.control.setAttribute('filter','[0-9\\_\\*]');
	    return VH_setdatatype(V,"string",20);
    } else
    if (dt == "customerno") {
		V.control.setAttribute('filter','[0-9]');
	    return VH_setdatatype(V,"string",10);
    } else
    if (dt == "customernokeyword") {
		V.control.setAttribute('filter','[0-9\\_\\*]');
	    return VH_setdatatype(V,"string",10);
    } else
    if (dt == "appgroupno") {
		V.control.setAttribute('filter','[0-9]');
	    return VH_setdatatype(V,"string",15);
    } else
    if (dt == "appgroupnokeyword") {
		V.control.setAttribute('filter','[0-9\\_\\*]');
	    return VH_setdatatype(V,"string",15);
    } else
    if (dt == "applicationno") {
		V.control.setAttribute('filter','[0-9]');
	    return VH_setdatatype(V,"string",15);
    } else
    if (dt == "applicationnokeyword") {
		V.control.setAttribute('filter','[0-9\\_\\*]');
	    return VH_setdatatype(V,"string",15);
    } else
    if (dt == "contractno") {
		V.control.setAttribute('filter','[0-9]');
	    return VH_setdatatype(V,"string",15);
    } else
    if (dt == "contractnokeyword") {
		V.control.setAttribute('filter','[0-9\\_\\*]'); //-- added by edyarto on 1 Okt'03
	    return VH_setdatatype(V,"string",15);
    } else
    if (dt == "chasisno") {
		V.control.setAttribute('filter','[a-zA-Z0-9\\.\\(\\)\\-]');
		params=50;
	    return VH_setdatatype(V,"string",params);
    } else
    if (dt == "engineno") {
		V.control.setAttribute('filter','[a-zA-Z0-9\\.\\(\\)\\-]');
	   params=50;
	    return VH_setdatatype(V,"string",params);
    } else
    if (dt == "factoryno") {
		V.control.setAttribute('filter','[0-9\\.\\(\\)\\-]');
	    if (params == null) params=50;
	    return VH_setdatatype(V,"string",params);
    } else
    if (dt == "invoiceno") {
		V.control.setAttribute('filter','[0-9\\.\\(\\)\\-]');
	    params=30;
	    return VH_setdatatype(V,"string",params);
    } else
    if (dt == "serialno") {
		V.control.setAttribute('filter','[0-9\\.\\(\\)\\-]');
	    if (params == null) params=50;
	    return VH_setdatatype(V,"string",params);
    } else
    if (dt == "stnkno") {
		V.control.setAttribute('filter','[a-zA-Z\\-\\/0-9]');
	    if (params == null) params=25;
	    return VH_setdatatype(V,"string",params);
    } else
    if (dt == "bpkbno") {
		V.control.setAttribute('filter','[0-9]');
	    if (params == null) params=50;
	    return VH_setdatatype(V,"string",params);
    } else
	//"id" added by pram
    if (dt == "id") {
		V.control.setAttribute('uppercase','');
		V.control.setAttribute('filter','[a-zA-Z0-9.\\-\\_\\/]');
	    return VH_setdatatype(V,"string",params);
    } else
	//"rtrw" added by pram
    if (dt == "rtrw") {
		V.control.setAttribute('rtrw','');
		V.control.setAttribute('filter','[0-9\\-\\/]');
	    return VH_setdatatype(V,"string",9);
    } else
    if (dt == "file") {
       V.control.setAttribute('filter','[\\\\0-9a-zA-Z\\:\\_\\-\\. ]');
	    return VH_setdatatype(V,"string");
    } else
    if (dt == "vhpolicenumkeyword") {
       V.control.setAttribute('filter','[a-zA-Z0-9\\-\\_\\*]');
	    return VH_setdatatype(V,"string");
    } else
    if (dt == "vhpolicenum") {
		V.control.setAttribute('uppercase','');
		V.control.setAttribute('filter','[a-zA-Z0-9\\-]');

		V.RE = new RegExp('^[A-Z]{1,2}[-][0-9]{1,5}([-][A-Z]{1,2})?$');
		V.fRE = new RegExp('^[A-Z]{0,2}[-]?([0-9]{0,5}|([0-9]{1,5}[-]?[A-Z]{0,2}))$');

	   params = 11;
    } else
    if (dt == "phone") {
        V.control.setAttribute('filter','[0-9]');
        return VH_setdatatype(V,"string",params);
    } else
    if (dt == "password") {
        return VH_setdatatype(V,"string",params);
    } else
    if (dt == "string") {
    } else
    if (dt == "email") {
        V.control.setAttribute('email','');
        V.control.setAttribute('filter','[a-zA-Z0-9@_\\-.]');
    } else
    if (dt == "year") {
        VH_setdatatype(V,"integer",4)
        V.setrange(minYear,maxYear);
        return;
    } else
    if (dt == "zip") {
        V.control.setAttribute('integer','');
        V.control.setAttribute('filter','[0-9]');
        VH_setmaxlen(V,9);
    } else
    if (dt == "label") {
        V.control.setAttribute('uppercase','');
        V.control.setAttribute('filter','[A-Z0-9_-]');
        VH_setmaxlen(V,9);
    } else
    if (dt == "date") {
        V.control.setAttribute('date','');
        V.control.setAttribute('filter','[0-9/]');
        VH_setmaxlen(V,10);

        if (document.getElementById(V.control.id+'d') != null) {
           V.control.beforevalidate = function() {
              this.value =  this.document.getElementById(this.id+'d').value + '/' +
                            this.document.getElementById(this.id+'m').value + '/' +
                            this.document.getElementById(this.id+'y').value;

              if (this.value == '//') this.value='';
              this.focuscontrol = this.document.getElementById(this.id+'d');
              return true;
           }
        }
        V.setrange=VH_setdaterange;
    } else
    if (dt == "datetime") {
        V.control.setAttribute('datetime','');
        V.control.setAttribute('filter','[0-9/ \\:]');
        VH_setmaxlen(V,16);

        V.setrange=VH_setdaterange;
    } else
    if (dt == "dateday") {
        VH_setdatatype(V,"integer",2)
        V.setrange(1,31);
        return;
    } else
    if (dt == "datemonth") {
        VH_setdatatype(V,"integer",2)
        V.setrange(1,12);
        return;
    } else
    if (dt == "dateyear") {
        VH_setdatatype(V,"integer",4)
        V.setrange(minYear,maxYear);
        return;
    } else
    if (dt == "time") {
        V.control.setAttribute('time','');
        V.control.setAttribute('filter','[0-9:]');
        VH_setmaxlen(V,5);

        V.setrange=VH_settimerange;
    } else
    if (dt == "money") {
        V.control.setAttribute('money','');
        V.control.setAttribute('filter','[0-9.\\,\\-]');
        if (V.control.style) V.control.style.textAlign='right';
    } else
    if (dt == "float") {
        V.control.setAttribute('money','');
        V.control.setAttribute('filter','[0-9.\\-]');
        if (V.control.style) V.control.style.textAlign='right';
    } else
    if (dt == "rate") {
        //return VH_setdatatype(V,'float4',params);
        VH_setdatatype(V,'float5.2');
        V.setrange(0,100);

        return;
    } else
    if (dt == "uppercase") {
        V.control.setAttribute('uppercase','');
    } else
        window.alert('unknown data type : '+dt);

    VH_assignfilter(V);

    if (params != null)
        VH_setmaxlen(V,params);

}

function VM_findVAP(control) {
    for (i=0;i<VAP.length;i++) {
        if (VAP[i].control == control) return VAP[i];
    }
    return null;
}

/**
when onfocus event is occur on any control object, this function will
check if the previous control object is changed already
*/
function VM_onfocus(){
    ctl = window.event.srcElement;
    for (var i=0;i<VAP.length;i++) {
        if ((VAP[i].control == ctl) || (VAP[i].control2 == ctl)) {
            if(prvControl != null && (ctl != prvControl.control)){
                r = VM_validate(prvControl);
                if (!r) {
                    ctl = prvControl.control;
                    try {
                       ctl.focus();
                       ctl.select();
                    } catch(e) {
                    }

                    if (prvControl.control.changeevent != null)
                      prvControl.control.changeevent();

                    return false;
                }
            }
            prvControl = VAP[i];
            if(VAP[i].control.focusevent != null)
                VAP[i].control.focusevent();

        }
    }
    return true;
}

/**
this method is used to keep the current control which it's value has been changed
where the validation of its value will be done later when the onfocus event occur
on any other control object
added by: windu
*/
function VM_validateonfocus2(V){
    ctl = window.event.srcElement;

    vt = ctl.value.trim();

    if (vt != ctl.value)
            ctl.value = vt;

    if (ctl.value == ctl.lastval2)
        return;

     ctl.lastval2 = ctl.value;

    for (var i=0;i<VAP.length;i++) {
        if ((VAP[i].control == ctl) || (VAP[i].control2 == ctl)){
          prvControl = VAP[i]; //hold this control object for later validation
          if (prvControl == null) {
              window.alert('Invalid control : ' + window.event.srcElement.id);
              return;
          }

          if (prvControl.control.changeevent != null)
             prvControl.control.changeevent();
        }
    }
    return;
}

function VM_validateonfocus(V) {

    //alert('focVal:'+V.controlname);

    //V = VM_findVAP(window.event.srcElement);

    var ctl;

    if (window.event != null)
      ctl = window.event.srcElement;

    if (ctl == null) ctl = this;

    if(ctl.value==undefined)return;

    vt = ctl.value.trim();

    if (vt != ctl.value)
            ctl.value = vt;

    if (ctl.value == ctl.lastval2)
        return;

     ctl.lastval2 = ctl.value;

    for (var i=0;i<VAP.length;i++) {
        if (
             (VAP[i].control == ctl) || (VAP[i].control2 == ctl)
          )
        {
          V = VAP[i];

          //alert('ctl '+ctl.id+'('+VAP[i].datatype+') : val=['+ctl.value+'] lastval=['+ctl.lastval2+']');

          if (V == null) {
              window.alert('Invalid control : ' + window.event.srcElement.id);
              return;
          }

          //if (V.control.changeevent != null)
          //   V.control.changeevent();

          //alert('validating : '+ctl.name+' ('+V.datatype+')');

          r = VM_validate(V);

          if (!r) {
            if (ctl.select) {
               try {
                  ctl.focus();
                  ctl.select();
               } catch(e) {
               }
            }

            if (ctl.tagName.toUpperCase() == 'SELECT')
               if (V.control.changeevent != null)
                  V.control.changeevent();

            //if (V.control.changeevent != null)
            //  V.control.changeevent();

            return false;
         }

          if (!r) {
              window.event.returnValue=false;
              VM_errmsg=''; // clear out validation.js error message
          }

          if (r) {// passes standard validation test ? check out validation.js 's test
              if (!VD_validate(VM_errmsg == '',V.control,VM_errmsg)) { // this cascades error messages generated from validation.js
                  VM_errmsg='';
                  return false;
              }

           }



          if (r)
            if (V.control.changeevent != null)
              V.control.changeevent();

          if (!r) return false;
        }

     }

    return;
}


function VA_req(control,fieldname,datatype,maxlen) {
    //if ((control == null) || (control+"" == "undefined")){
    //    window.alert('undefined control ('+fieldname+') !');
    //    return;
    //}

    //VM_setmandatory(control,true);
    V = VA_reg(control,fieldname,datatype,maxlen,true);
    return V;
}

function VA_trigger2(control,fieldname,VObject) {

    var V=newVAP();

    V.control = control;
    V.controlname = fieldname;
    V.form = control.form;

    V.datatype = 'TriggerV';

    V.triggerV = VObject;
}

function VA_trigger(control,fieldname,VObject) {

   var V = VA_reg(control, fieldname, 'TriggerV');
   V.triggerV = VObject;
}

function VA_regradio(control,fieldname,datatype,params) {
    var V=newVAP();

    //alert('VARegradio');

    V.datatype = "radio";
    V.control = control;
    V.fieldname = fieldname;
    V.params = params;
    V.controlname=V.control[0].id;
    V.form = V.control[0].form;
    V.arraycontrol = true;

    return V;
}

function VD_isvalid(ctl) {
     V = ctl.validateobject;
     if (V!=null) {
          VM_silent=true;
          if (!VM_validate(V)) {
               VM_silent=false;
               return false;
          }
          VM_silent=false;
          return true;
     }

     return false;
}

function VM_propchange(ctl) {
    if (ctl == null) ctl = window.event.srcElement;

    if (ctl.nopropchange == true) return;

    var V = ctl.validateobject;

    if (V!=null && V.descField!=null) {
      docEl(V.descField).value = ctl.options[ctl.selectedIndex].text;
      //alert(V.descField);
      //alert(docEl(V.descField).value);
      //alert(ctl.text);
    }

    if (ctl.VE_onchange == null) return;

    //if (ctl.value=='') alert('empty (lastval='+ctl.lastval+')');

    //if (window.event)
    //if (window.event.propertyName == 'value') {
      //alert(ctl.name+' has changed value');

    if (ctl.lastval == null) ctl.lastval = ctl.value;

    if (ctl.lastval != ctl.value) {

      //if (ctl.value != '') {
      //   var RE = ctl.validateobject.fRE;
      //   alert('bledar');
      //   if (RE)
      //     if (!RE.test(Sr)) {
      //         ctl.value = ctl.lastval;
      //         return false;
      //      }
      //}

      //alert('changed ('+ctl.name+') from "'+ctl.lastval+'" to "'+ctl.value+'"');
      ctl.lastval = ctl.value;
      //if (V!=null) {
      //   VM_silent=true;
      //   if (!VM_validate(V)) {
      //
      //      VM_silent=false;
      //      return;
      //   }
      //   VM_silent=false;
      //}

      if (ctl.VE_onchange != null) {
         //alert('oak');
         //alert('changed ('+ctl.name+') from "'+ctl.lastval+'" to "'+ctl.value+'"');

         //if (VD_isvalid(ctl))
         {
            ctl.nopropchange = true;
            //alert('calling');
            ctl.VE_onchange()
            //alert('out');
            ctl.nopropchange = false;
         }
      }
      ctl.lastval = ctl.value;
    }
}

var V_autotab = null;

function VM_focus() {
   if (VM_fireFocusChange!=undefined)
      VM_fireFocusChange();

   var V = this.validateobject;
   if ((V.datatype == 'money') && (this.readOnly != true) && (this.disabled != true)) {
      this.value=moneytofloat(this.value);
      try {
         this.select();
      } catch (e) {
      }
   }
   if (this.oldfocus)
      this.oldfocus();
}

function VM_blur() {
   var V = this.validateobject;
   if ((V.datatype == 'money') && (this.readOnly != true) && (this.disabled != true)) {
      this.value=floattomoney(this.value,V.floatconst[1]);
   }
   if (this.oldblur)
      this.oldblur();
}

function VM_getTab(howmuch) {
   var i = V_autotab;
   V_autotab += howmuch;

   return i;
}

function findControlByTabIndex(tbx) {
     var x = document.all;
     for (var i=0;i<x.length;i++) {
          if (x[i].tabIndex == tbx) {
               var c=x[i];
               return c;
          }
     }

     return null;
}

function VM_keydown() {
     if (window.event) {
      var c = String.fromCharCode(window.event.keyCode).toUpperCase();
      c = c.charCodeAt(0);

      if (c==9) {
          var x = document.all;

          var mintab=999999;
          var maxtab=0;
          var o,t,maxtabc,mintabc;

          for (var i=0;i<x.length;i++) {
               o = x[i];
               if (o.tabIndex>0)
                  if ( ((o.readOnly != true) && (o.disabled != true))) {
                       t = o.tabIndex;
                       if (t>0) {
                            if (t>maxtab) {
                                 maxtab=t;
                                 maxtabc = o;
                            }
                            if (t<mintab) {
                                 mintab=t;
                                 mintabc = o;
                            }
                       }
                  }
          }

          //alert('maxtab='+maxtab+' ;mintab='+mintab);

          if (!window.event.shiftKey) {
               if (this.tabIndex == maxtab) {
                    //c = findControlByTabIndex(mintab);
                    var c=mintabc;
                    if (c!=null) {
                         try {
                              c.focus();
                              //c.select();
                         } catch (e) {
                         }
                         window.event.returnValue = false;
                    }
               }
          } else {
               if (this.tabIndex == mintab) {
                    //c = findControlByTabIndex(maxtab);
                    var c = maxtabc;
                    if (c!=null) {
                         try {
                              c.focus();
                              //c.select();
                         } catch (e) {
                         }
                         window.event.returnValue = false;
                    }
               }
          }
      }
      //VM_fireFocusChange();
   }

   if (this.hk_keydown != 0)
      this.hk_keydown();
}

function isSubmittableForm(F) {
     var x = F.elements;

     for (var i=0; i<x.length;i++) {
          ct = x[i].getAttribute('type').toUpperCase();

          if (ct == "SUBMIT") {
               return true;
          }
     }

     return false;
}

function VM_keypress() {

   if (window.event) {
      var c = String.fromCharCode(window.event.keyCode).toUpperCase();
      c = c.charCodeAt(0);

      if ((this.tagName.toUpperCase() == 'SELECT') && (c==13)) {
         if (isSubmittableForm(this.form))
           if (this.form.onSubmit()) this.form.submit();
      }
   }

   if (this.hk_keypress != 0)
      this.hk_keypress();
}

function VH_isValid() {
     var V = this.validateobject;

     if (V == null) alert('this control ('+this.id+') is not registered');

     return VM_validate(V);
}

function VA_reg(control,fieldname,datatype,params,mdt) {

    //if ((control == null) || (control+"" == "undefined")){
    //    window.alert('undefined control ('+fieldname+') !');
    //    return;
    //}

    if (
         //(datatype != 'TriggerV') &&
         //(datatype != 'dateconstrain') &&
         (control.validateobject == null) // first time registration .. ?
       ) {

       if (control.tabIndex != null) {
          if (V_autotab == null) {
            if (control.tabIndex == 0) {V_autotab = 1;} else {V_autotab=0};
          }

          if (control.tabIndex == 0)
             if ((V_autotab > 0) && ((control.readOnly != true) && (control.disabled != true) || (datatype==null))) {
               control.tabIndex = V_autotab++;
               try {
                  if ((control.tabIndex == 1) && (control.focus)) control.focus();
               } catch(e) {
               }
             }
       }
    }

    //if (datatype=="datesequence") {
    //    VA_regdatesequence(control,fieldname,params);
    //
    //    return
    //}

    if (!control) {
       window.alert('You must provide the control');
       return;
    }

    if (control.hk_keypress == null) {
        control.hk_keypress = control.onkeypress == null ? 0 : control.onkeypress;
        control.onkeypress = VM_keypress;
    }

    if (control.hk_keydown == null) {
        control.hk_keydown = control.onkeydown == null ? 0 : control.onkeydown;
        control.onkeydown = VM_keydown;
    }

    if ((fieldname == null) && (datatype==null)) return; // if only specify control then do not process validation

    control.isValid = VH_isValid;

    //alert('registering : '+fieldname+'['+datatype+']');

    if (control.length>1) {
      if (control[0].className == "radio") {
         return VA_regradio(control,fieldname,datatype,params);
      }
      // else alert('error : '+fieldname+'['+datatype+']');
    }

    if (!control.form) {
       window.alert('Form control not found ('+control[0].id+','+fieldname+') !');
       return;
    }

    if (datatype == null) datatype = 'string';

    var V=newVAP();

    V.setgroup = function(x) {
      this.vgroup = x;
      return this;
    }

    V.control = control;
    V.controlname = control.id;

    if (V.controlname==null) V.controlname=control.name;
    if (V.controlname=='') V.controlname=control.name;

    V.form = control.form;

    //if (control.validateobject == null)
    control.validateobject = V;

    //if (V.form == null) V.form = VD_form;

    VM_initform(V.form);

    if (control.options!=undefined) {
      control.getSelectedAttribute=function(a) {
         if (this.value=='') return '';
         if (a==null) return this.options[this.selectedIndex].text;
         return this.options[this.selectedIndex].getAttribute(a);
      }
    }

    if ((V.control.onchange != VM_validateonfocus) && (V.control.changeevent == null)){
      V.control.changeevent = control.onchange;
      V.control.onchange = VM_validateonfocus;
    }

    if ((V.control.onpropertychange != VM_propchange) && (V.control.propertychangeevt == null)){
      V.control.propertychangeevt = control.onpropertychange;
      V.control.onpropertychange = VM_propchange;
    }

    if ((V.control.onfocus!= VM_focus) && (V.control.oldfocus == null)) {
      V.control.oldfocus=V.control.onfocus;
      V.control.onfocus = VM_focus;
    }

    if ((V.control.onblur!= VM_blur) && (V.control.oldblur == null)){
      V.control.oldblur=V.control.onblur;
      V.control.onblur = VM_blur;
    }

    if((V.control.onfocus != VM_onfocus) && (V.control.getAttribute('keyword') != null) && (V.control.focusevent == null)){
        V.control.focusevent = V.control.onfocus; //store onfocus function
        V.control.onfocus = VM_onfocus;
        V.control.onchange = VM_validateonfocus2; //override onchange function
    }

    V.fieldname = fieldname;
    if (control.fieldname == null)
      control.fieldname = fieldname;

    if (sRE = control.getAttribute("regexp")) {
        V.RE = new RegExp(sRE);
        //window.alert('RE = '+sRE);
    }

    if (sRE = control.getAttribute("filterre")) {
        V.fRE = new RegExp(sRE);
        //window.alert('fRE = '+sRE);
        //window.alert(V.fRE.test('1'));
    }

    V.setDescField = VM_setDescField;

    if (datatype != null)
        VH_setdatatype(V,datatype,params);
    else
        VH_setdatatype(V,"string",params);

    VM_setmandatory(control,mdt==null?false:mdt);

    return V;
};

function VM_setDescField(x) {
   this.descField=x;
}

function VM_setmandatory(ctl,state) {
   ctl.reqd = state;

   if (ctl.setAttribute) {
      if (state==true) {
         ctl.setAttribute('required','');
         ctl.originalcolor=ctl.style.backgroundColor;
         if (ctl.readOnly != true)
            ctl.style.backgroundColor = mandatory_color;
      } else {
         ctl.removeAttribute('required');
         if (ctl.readOnly == true) {
            ctl.style.backgroundColor = dimColor;
            //ctl.className = 'dimText';
           // alert('P');
         }
         else {
            if (ctl.style.backgroundColor.toUpperCase == mandatory_color.toUpperCase)
               ctl.style.backgroundColor = editable_color;
         }
      }
   }
}

function VM_getconstrainedstate(ctl) {
   var o = ctl.VR_constraint;
   if (ctl.VR_controlOR == true) {
      for (var i=0;i<o.length;i++) {
        if (o[i].checked) return true;
      }
      return false;
   }
   for (var i=0;i<o.length;i++) {
     if (!o[i].checked) return false;
   }
   return true;
}

function VE_controllerclick(c) {
   if (c==null)
      c = window.event.srcElement;

   state = c.checked;
   if (c.controls != null)
      for (i=0;i<c.controls.length;i++) {
         var vstate = VM_getconstrainedstate(c.controls[i]);

         if (c.controls[i].VR_noDisable == true) {
            VM_setreadonly(c.controls[i],!vstate);
         } else {
            VM_setenable(c.controls[i],vstate);
         }

         //var vstate = state;
         VM_setmandatory(c.controls[i],vstate);

         if (c.controls[i].onpropertychange != null)
            c.controls[i].onpropertychange(c.controls[i]);
      }
}

function VM_disable(ctl) {
   VM_setmandatory(ctl,false);
   VM_setenable(ctl,false);
}

function VM_setreadonly(ctl,state) {
   ctl.readOnly = state;
}

function VM_setenable(c,state) {
   //if (c.length>1) {
   if (c.id == null) {
      for (var i=0;i<c.length;i++) {
         //c[i].disabled = !state;
         //alert(c[i].readOnly);

         c[i].disabled = !state;
         c[i].style.backgroundcolor = 'black';
      }

      return;
   }

   //c.readOnly = !state;
   c.disabled = !state;
}

function VA_controlreq(control,ctls) {
   for (var i=0;i<ctls.length;i++) {
      var R = ctls[i].VR_constraint;
      if (R == null) R = new Array();
      R[R.length] = control;
      ctls[i].VR_constraint = R;
   }

   control.onpropertychange=VE_controllerclick;
   control.controls=ctls;

   VE_controllerclick(control);
}

function VM_validate(V) {

   //alert('validating '+V.controlname+'['+V.datatype+']');

   if (document.getElementById(V.controlname) == null) return true;

   if (V.control == null) return true;

   if (V.control.value) V.control.value = V.control.value.trim();

   if (!V.arraycontrol) {

      vduser = V.control.getAttribute("beforevalidate");
      if (vduser==null) vduser=V.control.beforevalidate;

      if (vduser != null) {
         var z;
         if (typeof vduser == 'string')
            z = V.control.eval(vduser);
         else
            z = V.control.beforevalidate();
         if (VD_validate(z == true, V.control, z)==false) return false;
      }
   }


   if (!VM_validate2(V)) return false;

   if (!V.arraycontrol) {

      vduser = V.control.getAttribute("validate");
      if (vduser==null) vduser=V.control.validate;

      if (vduser != null) {
         var z;
         if (typeof vduser == 'string') {
            z = eval(vduser);
         } else {
            z = V.control.validate();
         }
         return VD_validate(z == true, V.control, z);
      }
   }

   return true;
}

function VD_radiochecked(ctl) {
   var i;

   for (i=0;i<ctl.length;i++) {
      if (ctl[i].checked) return true;
   }

   return false;
}

function VM_isrequired(control) {
    if ((control.reqd == true) || (control.reqd == false)) {
      return control.reqd;
    } else {
      return control.getAttribute("required") != null;
    }
}

var REvhautofixnum = new RegExp("^([A-Z]{1,2})[-]?([0-9]{1,5})([-]?([A-Z]{1,2}))?$");

function VD_autofixpolicenumber(z) {
   z = z.toUpperCase();
   var m = REvhautofixnum.exec(z);

   if (m==null) return z;

   if (m[4] == '')
      return m[1]+'-'+m[2];
   return m[1]+'-'+m[2]+'-'+m[4];
}

function VM_validate2(V) {
    if (document.getElementById(V.controlname) == null) { // control already deleted
        V.control = null;
        return true;
    }

    //if ((V.control.getAttribute('required') == null)) {
    //    if (V.control.value=='') return true;
    //}

    if (V.control.value != null) {

       if (V.control.disabled) return true;



       if (!VM_isrequired(V.control)) {
         if (V.control.value=='') return true;
       }

       if (!VD_validate((V.control.value != ''),V.control,V.control.fieldname+' should not be empty')) return false;

       if (V.minlen != null) {
           if (!VD_validate(((new String(V.control.value)).length >= V.minlen),V.control,V.control.fieldname + ' has a minimum length of ' + V.minlen+' characters')) return false;
       };

       if (V.maxlen != null) {
           if (!VD_validate(((new String(V.control.value)).length <= V.maxlen),V.control,V.control.fieldname + ' has a maximum length of ' + V.maxlen+' characters')) return false;
       };

       //if (V.RE)
       //    return VD_validate(V.RE.test(V.control.value),V.control,'You must enter a valid value for ' + V.fieldname );

       if (V.filterRE == null) {
          filter = V.control.getAttribute("filter");
          if (filter) {
              V.filterRE = new RegExp('^'+filter+'*$');
          }
       }
       if (V.filterRE != null)
         //if (!VD_validate((V.filterRE.test(V.control.value)), V.control, 'Only '+V.control.getAttribute('filter')+' characters allowed for '+V.fieldname)) return false;
         if (!VD_validate((V.filterRE.test(V.control.value)), V.control, 'Special characters not allowed for '+V.control.fieldname)) return false;

       if (V.filterRE == null) {

          if (!VD_validate((defaultSFilter.test(V.control.value)), V.control, 'Special characters not allowed for '+V.control.fieldname)) return false;

          if (V.control.getAttribute('strict')!=null)
            if (!VD_validate((strictSFilter.test(V.control.value)), V.control, 'Special characters not allowed for '+V.control.fieldname)) return false;
       }

       //check if this control object is has "keyword" attribute to trigger for wildcard checking
       if (V.control.getAttribute('keyword') != null)
         if(!VD_validate((wildCard.test(V.control.value)), V.control, 'Wildcard (' + wildCard.wildCard + ') only not allowed for ' + V.fieldname)) return false;

    } else {
       if (V.control[0].disabled) return true;
    }

   if (V.control.getAttribute)
      if (V.control.getAttribute('uppercase') != null) {
         V.control.value = V.control.value.toUpperCase();
       }

    if (V.datatype == "uppercase") {
        V.control.value = V.control.value.toUpperCase();
        return true;
    } else
	//"id" added by pram
   if (V.datatype == "id") {
        V.control.value = V.control.value.toUpperCase();
        return true;
    } else
	//"rtrw" added by pram
    if (V.datatype == "rtrw") {
        return true;
    } else
    if (V.datatype == "zip") {
        return true;
    } else
    if (V.datatype == "radio") {
        return (
         VD_validate(VD_radiochecked(V.control),V.control[0],'You must select '+V.fieldname)
        );
    } else
    if (V.datatype == "string") {
        return true;
    } else
    if (V.datatype == "label") {
        return true;
    } else
    if (V.datatype == "TriggerV") {
        //alert('Triggered validation');
        return VM_validate(V.triggerV);
    } else
    if (V.datatype == "date") {
        return (
           VD_datevalid(V.control,'You must enter a valid date for '+V.fieldname) &&
           (
              (V.control.datemin == null) ||
              VD_validate(compareDate(V.control.value,V.control.datemin)>=0, V.control, V.control.fieldname+ " should not be earlier than "+V.control.datemin)
           ) &&
           (
              (V.control.datemax == null) ||
              VD_validate(compareDate(V.control.value,V.control.datemax)<=0, V.control, V.control.fieldname+ " should not be later than "+V.control.datemax)
           )
        );
    } else
    if (V.datatype == "datetime") {
        return (
           VD_datetimevalid(V.control,'You must enter a valid date/time for '+V.fieldname) &&
           (
              (V.control.datemin == null) ||
              VD_validate(compareDate(V.control.value,V.control.datemin)>=0, V.control, V.control.fieldname+ " should not be earlier than "+V.control.datemin)
           ) &&
           (
              (V.control.datemax == null) ||
              VD_validate(compareDate(V.control.value,V.control.datemax)<=0, V.control, V.control.fieldname+ " should not be later than "+V.control.datemax)
           )
        );
    } else
    if (V.datatype == "time") {
        return (
           VD_timevalid(V.control,'You must enter a valid time for '+V.control.fieldname) &&
           (
              (V.control.timemin == null) ||
              VD_validate(compareTime(V.control.value,V.control.timemin)>=0, V.control, V.control.fieldname+ " should not be earlier than "+V.control.timemin)
           ) &&
           (
              (V.control.timemax == null) ||
              VD_validate(compareTime(V.control.value,V.control.timemax)<=0, V.control, V.control.fieldname+ " should not be later than "+V.control.timemax)
           )
        );
    } else
    if (V.datatype == "integer") {
        return (
                   VD_integer(V.control,'You must enter a valid number for '+V.fieldname) &&
                   (
                         (V.control.integermin == null) ||
                         VD_validate(parseInt(V.control.value)>=V.control.integermin, V.control, V.fieldname+' should not be less than '+V.control.integermin)
                   ) &&
                   (
                         (V.control.integermax == null) ||
                         VD_validate(parseInt(V.control.value)<=V.control.integermax, V.control, V.fieldname+' should not be greater than '+V.control.integermax)
                   )
                   && (
                     //(V.fnozero != true) ||
                     (VH_isZeroAllowed(V)) ||
                     VD_validate(parseFloat(V.control.value)!=0, V.control, V.fieldname+' should not be zero')
                   )
              );
    } else
    if (V.datatype == "vhpolicenum") {
      V.control.value = VD_autofixpolicenumber(V.control.value);
      return VD_validate(V.RE.test(V.control.value),
               V.control,
                'You must enter a valid vehicle police number for '
                + V.fieldname + ' (XX-9999-YY)');
    } else
    if (V.datatype == "float") {
        return (
            VD_validate(V.RE.test(V.control.value),
                V.control,
                'You must enter a valid number for '
                + V.fieldname
                + ' ('+(parseInt(V.floatconst[0])+parseInt(V.floatconst[1]))+'.'+V.floatconst[1]+' digits)')
                  && (
                         (V.control.floatmin == null) ||
                         VD_validate(parseFloat(V.control.value)>=V.control.floatmin, V.control, V.fieldname+' should not be less than '+V.control.floatmin)
                   ) &&
                   (
                         (V.control.floatmax == null) ||
                         VD_validate(parseFloat(V.control.value)<=V.control.floatmax, V.control, V.fieldname+' should not be greater than '+V.control.floatmax)
                   )
                   && (
                     //(V.fnozero != true) ||
                     (VH_isZeroAllowed(V)) ||
                     VD_validate(parseFloat(V.control.value)!=0, V.control, V.fieldname+' should not be zero')
                   )
            );
    } else
    if (V.datatype == "money") {
        var valid =  (
            VD_validate(VD_money(V),
                V.control,
                'You must enter a valid number for '
                + V.fieldname
                + ' ('+(parseInt(V.floatconst[0])+parseInt(V.floatconst[1]))+'.'+V.floatconst[1]+' digits)')

             && (
                   (V.control.floatmin == null) ||
                   VD_validate(getFloat(V.control.value)>=V.control.floatmin, V.control, V.fieldname+' should not be less than '+V.control.floatmin)
             ) &&
             (
                   (V.control.floatmax == null) ||
                   VD_validate(getFloat(V.control.value)<=V.control.floatmax, V.control, V.fieldname+' should not be greater than '+V.control.floatmax)
             )
             && (
                     //(V.fnozero != true) ||
                     (VH_isZeroAllowed(V)) ||
                     VD_validate(getFloat(V.control.value)!=0, V.control, V.fieldname+' should not be zero')
                   )
            //&& VD_money(V.control,'You must enter a valid number for '+V.fieldname)
            );

        if (valid) V.control.setvalue(V.control.value);

        return valid;
    } else
    if (V.datatype == "rate") {
        return VD_float(V.control,'You must enter a float integer number for '+V.fieldname);
    } else
    if (V.datatype == "email") {
        //return true;
        return VD_email(V.control,'You must enter a valid email address for '+V.fieldname);
    } else
    if (V.datatype == "datesequence") {
        return VD_datesequence(V.control,V.control2,V.control.fieldname+' should be earlier than '+V.control2.fieldname);
    } else
    if (V.datatype == "datesequence2") {
        if (V.inverted == true)
            return VD_datesequenceinverse(V.control,V.control2,V.control.fieldname+' should not be earlier than '+V.control2.fieldname);
        else
            return VD_datesequence(V.control,V.control2,V.control.fieldname+' should not be later than '+V.control2.fieldname);
    } else
    if (V.datatype == "timesequence") {
        return VD_timesequence(V.control,V.control2,V.control.fieldname+' should be earlier than '+V.control2.fieldname);
    } else
    if (V.datatype == "dateconstrain") {
        //return VD_dateconstrain(V.control,V.control2,V.constraincontrols,'Date constrain violation for  '+V.fieldname);
        return VD_dateconstrain2(V.control,V.control2,'Date constrain violation for  '+V.fieldname);
    } else
        window.alert('Unknown data type : '+V.datatype);
};

var antiloop=0;
var VD_form=null;

function VM_dovalidate(F, groupno) {
   srcF = null;
   if(F == null){
       if (this.tagName == 'FORM') {
          srcF = this;
       }
       else if (window.event != null) {
          srcF = window.event.srcElement;
          if (srcF.tagName != "FORM")
             srcF = null;
       }
   }


   if (srcF)
      F = srcF;

   if (F == null) F = VD_form;

   if (F == null) {
      //window.alert('validator not properly initialized!');
      return true;
   }

   if (F.submitGroup!=undefined) {
      alert('overriding valgroup');
      groupno=F.submitGroup;
      F.submitGroup=undefined;
   }

    var rt = VM_dovalidate2(F, groupno);

    antiloop = 0;

    return rt;
}

function logObject(o) {
   var s = '';
   for (key in o) {
      s+=key+'='+o[key]+'\n';
   }
   return s;
}

function cancelEvent() {
   if (NS_event)
      NS_event.cancelBubble = true;
   else
      window.event.returnValue=false;
}

function VM_dovalidate2(F, groupno) {
    var i;

    for (i=0;i<VAP.length;i++) {
        if ((VAP[i].form == F) && (VAP[i].vgroup == groupno)) {
           //try {
              if (!VM_validate(VAP[i])) {
                  cancelEvent();

                  return false;
              }
           //} catch (e) {
           //   alert('Error while validating '+logObject(VAP[i].controlname));
           //}
        }
    }

    if (antiloop == 0)
      if (F.onsubmitv != null) {
            antiloop = 1;

            F.VR_formsubmit = true;
            if (F.onsubmitv() == false)
               F.VR_formsubmit = false;

            if (window.event) {
               if (window.event.returnValue == false)
                  F.VR_formsubmit = false;
            }

            return F.VR_formsubmit;
      }

   if (antiloop == 1) {
      window.alert('validator warning : infinite loop detected, check your onsubmit event');
      antiloop=2;
   }

   F.VR_formsubmit = true;

   return F.VR_formsubmit;
};

function VM_clearexit() {
   fs = document.forms;

   var i;

   for (i=0;i<fs.length;i++) {
      var F = fs[i];
      if (F != null) {
         if (F.VR_formsubmit != true)
            if (F.VE_onexit != null) {
               F.VE_onexit = null;
            }
      }
   }
}

function VH_windowunload() {
   var i;

   fs = document.forms;

   for (i=0;i<fs.length;i++) {
      var F = fs[i];
      if (F != null) {
         if (F.VR_formsubmit != true)
            if (F.VE_onexit != null) {
               F.VE_onexit();
               F.VE_onexit = null;
            }
      }
   }

   for (i=0;i<VAP.length;i++) {
      var V = VAP[i];
      if (V.control != null) {
         var F = VAP[i].control.form;
         if (F != null) {
            if (F.VR_formsubmit != true)
               if (F.VE_onexit != null) {
                  F.VE_onexit();
                  F.VE_onexit = null;
               }
         }
      }
   }

   if (this.VH_onunloadevent != null)
      this.VH_onunloadevent();
}

function VM_dovalidateNS(e) {
   if (e==null) return VM_dovalidate(null);
   NS_event = e;
   var x=VM_dovalidate(e.currentTarget);
   NS_event=null;
   return x;
}

var submitted = false;

function formGuard(e) {
   // if (submitted) {
   //   alert('Request is being processed, please be patient');
   //   return false;
   //}

   var a = VM_dovalidateNS(e);
   if (a) submitted = true;
   return a;
}

function VM_CancelSubmit() {
   submitted=false;
   return false;
}

function VM_initform(F) {
    if (F.tagName != "FORM") return;

    VD_form = F;

    if (F.VM_initialized != null) return;

    F.VM_initialized = true;

    F.onsubmitv = F.onsubmit;
    F.onSubmit = formGuard;
    F.onsubmit = formGuard;
    F.setAttribute('mark','');
    F.setAttribute('year4','');
    F.setAttribute('invalidcolor','yellow');
    F.setAttribute('validate','onchange');

    document.onvalalert = VM_alert;

    if (window.onbeforeunload != VH_windowunload) {
       if (window.onbeforeunload != null) {
         window.VH_onunloadevent = window.onbeforeunload;
       }

       window.onbeforeunload = VH_windowunload;
    }
};


function VM_submit(F) {
   if (F==null) F = VD_form;

   if (F != null) {
      F.onSubmit();
      F.submit();
   }
}

function VM_getpagespath() {

    v = new String(window.location.pathname);

    vx = v.split('/');

    r = '';

    for (i=vx.length-3;i>=0;i--) {

        if (vx[i].toUpperCase() == 'PAGES') break;

        r = r + '../';

    }

    return r;
}

function VA_unreg(control){
    if(VAP != null && (VAP.length > 0)){
        for(i = 0; i < VAP.length; i++){
            if(VAP[i].control.id == control.id){
                control.style.backgroundColor = 'white';
                control.readOnly = "true";
                for(j = i; j < (VAP.length - 1); j++){
                    VAP[i] = VAP[j+1];
                }
                VAP[VAP.length - 1] = null;
                VAP.length = VAP.length - 1;
            }
        }
    }
}

function VA_unregUnabled(control){
    if(VAP != null && (VAP.length > 0)){
        for(i = 0; i < VAP.length; i++){
            if(VAP[i].control.id == control.id){
                control.style.backgroundColor = 'white';
                control.removeAttribute('required');
                if(VAP[i].datatype){
					VAP[i].filterRE = null;
					VAP[i].RE = null;
                    control.removeAttribute(VAP[i].datatype);
                    control.removeAttribute('filter');
					control.removeAttribute('filterre');
                    control.removeAttribute('maxlength');
                    control.removeAttribute('V');
					control.removeAttribute('validateobject');
                }
                for(j = i; j < (VAP.length - 1); j++){
                    VAP[i] = VAP[j+1];
                }
                VAP[VAP.length - 1] = null;
                VAP.length = VAP.length - 1;
            }
        }
    }
}

function VA_unregbyname(controlnamepattern) { // use this to clear out invalid validator objects
   var aVAP = new Array();
   for (var i=0;i<VAP.length;i++) {
      if (VAP[i].controlname.indexOf(controlnamepattern)>=0) {
         VAP[i] = null;
      } else
         aVAP[aVAP.length] = VAP[i];
   }
   VAP = null;
   VAP = aVAP;
}

var VM_scriptpath = VM_getpagespath();

function VM_initvalidate() {
}

function VM_report() {
    i=0;

    for (i=0;i<VAP.length;i++) {
        window.alert(VAP[i].fieldname);
    }
}

function trim(inputString) {
   // Removes leading and trailing spaces from the passed string. Also removes
   // consecutive spaces and replaces it with one space. If something besides
   // a string is passed in (null, custom object, etc.) then return the input.
   if (typeof inputString != "string") { return inputString; }
   var retValue = inputString;
   var ch = retValue.substring(0, 1);
   while (ch == " ") { // Check for spaces at the beginning of the string
      retValue = retValue.substring(1, retValue.length);
      ch = retValue.substring(0, 1);
   }
   ch = retValue.substring(retValue.length-1, retValue.length);
   while (ch == " ") { // Check for spaces at the end of the string
      retValue = retValue.substring(0, retValue.length-1);
      ch = retValue.substring(retValue.length-1, retValue.length);
   }
   while (retValue.indexOf("  ") != -1) { // Note that there are two spaces in the string - look for multiple spaces within the string
      retValue = retValue.substring(0, retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ")+1, retValue.length); // Again, there are two spaces in each of the strings
   }
   return retValue; // Return the trimmed string back to the user
} // Ends the "trim" function


String.prototype.trim=function (){
   return this.replace(/^\s+/,"").replace(/\s+$/,"");
}

function removespaces(s) {

   var i,s,r="";

   for (i=0;i<s.length;i++)
      if (s.charAt(i) != ' ')
         r += s.charAt(i);

   return r;
}

function VNL(x,y) {
   if (x==null) return y;

   return x;
}

function moneytofloat(x) {
   //var z = (new String(x)).replace('[\\'+VC_FLOATGROUPSEPARATOR+']','');
   var z = '';

   x = new String(x);

   for (var i=0;i<x.length;i++) {
      var y = x.charAt(i);
      if (y == VC_FLOATGROUPSEPARATOR) continue;
      z = z + y;
   }

   return z;
}

function floattointeger(x) {
   if (x=='') return '';
   return Math.floor(parseFloat(x));
}

function floattomoney(x,d) {
   x+='';
   if (x=='') return '';

   x = parseMoney(x);

   x = x.toFixed(d);
   var j = x.indexOf('.');
   if (j < 0) j = x.length;

   var i;
   var y = '';

   for (i=0;i<x.length;i++) {
      var a = x.charAt(i)

      y = y + x.charAt(i);

      j--;

      if (a == '-') continue;

      if (j>0)
         if (j%3 == 0) y = y + ',';
   }

   return y;
}

function parseMoney(x) {
   return parseFloat(moneytofloat(x));
}

function getFloat(x) {
   x = parseFloat(moneytofloat(x));
   if (isNAN(x)) return 0;
   return x;
}

function isNAN(x) {
   if (x==null) return true;
   if (x=='') return true;
   return isNaN(x);
}

function ISNAN(x,y) {
   if (isNAN(moneytofloat(x))) return y; else return x;
}

function unFormatted(val) {
   if (document.numberFormat_js == null) {
      window.alert('you must include numberFormat.js to format number');
      return
   }
   if (nf == null) {
      nf = new NumberFormat();
   }
   nf.setNumber(val);
   return nf.toUnformatted();
}

function format(val,numDecimal) {
   if (document.numberFormat_js == null) {
      window.alert('you must include numberFormat.js to format number');
      return
   }
   if (nf == null) {
      nf = new NumberFormat();
   }
   nf.setPlaces(numDecimal);
   nf.setNumber(val);
   nf.setCurrency(false);

   return nf.toFormatted();
}

function VA_controlen(control, ctls) {
   control.controls=ctls;

   control.onpropertychange=function(ctl) {
      if (ctl == null) ctl = window.event.srcElement;
      if (ctl == null) ctl = this;

      for (var i=0;i<ctl.controls.length;i++)
         VM_enable(ctl.controls[i], ctl.checked);
   }

   control.onpropertychange(control);
}


function VM_enable(ctl, status) {
   if (ctl==null) return;

   if (ctl.tagName == 'INPUT') {
      if (ctl.type.toUpperCase() == 'BUTTON') {
         ctl.disabled = !status;
         return;
      }
   }

   if (ctl.RequiredFlag == null) {
      ctl.RequiredFlag = ctl.required != null;
   }

   var clr;

   if (status)
      clr = ctl.RequiredFlag ? mandatory_color:editable_color;
   else
      clr = dimColor;

   //   alert(dimColor);

   if (!status) {
      ctl.readOnly = true;

      ctl.removeAttribute('required');

      if (ctl.tagName == 'TEXTAREA') {
         //ctl.className='dimtextarea';
         ctl.readOnly = true;
         //ctl.disabled = true;
      }
      else if (ctl.tagName == 'INPUT') {
        //ctl.className='dimtext';
        ctl.readOnly = true;
      }
      else {
         ctl.disabled = true;
      }

   } else {
     if (ctl.tagName == 'TEXTAREA') {
        //ctl.className='textarea';
     } else if (ctl.tagName == 'INPUT') {
       //ctl.className='text';
     }
     ctl.disabled = false;
     ctl.readOnly = false;
   }

   ctl.style.backgroundColor = clr;

   if (ctl.propagateStatusChange)
         ctl.propagateStatusChange();
}

function getImageStyle(status) {
   return status ? "style=\"vertical-align:top; cursor:hand;\"" : "disabled style=\"vertical-align:top; cursor:hand; filter:progid:DXImageTransform.Microsoft.Alpha( Opacity=20)\""
}

function switchPg(c,n,i) {
   c.value = i;
   eval('document.getElementById(\'ePg'+n+'\').value=\'y\';');
   eval('var cpgf = changePage'+n+';');
   eval('if (cpgf != null) cpgf(i);');
}

function switchSort(c,n,i) {
   c.value = i;
   eval('document.getElementById(\'ePg'+n+'\').value=\'y\';');
   eval('var cpgf = changePage'+n+';');
   eval('if (cpgf != null) cpgf(i);');
}

function enablebutton(x,s) {
   if (x) x.disabled = !s;
}

function DatePicker(name) {

     this.id = name;

     this.dt = new Date();

     document.write('<span id="' + name +

        '" style="z-index:60000; position:absolute;visibility:hidden;"' +

        'class="DatePicker"></span>');

}

DatePicker.prototype.show = function(dt, x, y, callback) {

     if ( dt ) this.dt = dt;

     this.callback = callback;


     // if not rendered yet, do so

     //if ( !this.oSpan ) this.render();

     if (!this.oSpan.rendered) this.render();

     if (this.oSpan.lastHeight)
        this.oSpan.style.height = this.oSpan.lastHeight;

     // set coordinates

     this.oSpan.style.left = x;

     this.oSpan.style.top= y;


     this.fill();


     this.oSpan.style.visibility = "visible";

     //this.oMonth.focus();

}


DatePicker.prototype.hide = function() {

     if ( this.oSpan ) this.oSpan.style.visibility = "hidden";
     if (this.oSpan)
     if (this.oSpan.lastHeight == null) {
         this.oSpan.lastHeight = this.oSpan.offsetHeight;
     }
     if ( this.oSpan ) this.oSpan.style.height = '1px';

}

//The render method uses DOM functions to create the structure of the datepicker control:


DatePicker.prototype.render = function() {

     var oT1, oTR1, oTD1, oTH1;

     var oT2, oTR2, oTD2;

     this.oSpan.rendered = true;

     //this.oSpan = document.getElementById('sp1');

     this.oSpan.appendChild(oT1 = document.createElement("table"));

     oT1.border = 1;


     oTR1 = oT1.insertRow(oT1.rows.length);

     oTD1 = oTR1.insertCell(oTR1.cells.length);

     oTD1.colSpan = 7;

     oTD1.className = 'DatePickerHdr';


     oT2 = document.createElement("table");

     oTD1.appendChild(oT2);

     oT2.border = 0;


     oTR2 = oT2.insertRow(oT2.rows.length);



     oTD2 = oTR2.insertCell(oTR2.cells.length);

     oTD2.title = this.texts.prevMonth;

     oTD2.onclick = function() { this.oDatePicker.onPrev(); }

     oTD2.oDatePicker = this;

     oTD2.innerHTML = "&lt;&lt;";

     oTD2.className = 'DatePickerHdrBtn';


     oTD2 = oTR2.insertCell(oTR2.cells.length);

     this.oMonth = document.createElement("select");

     oTD2.appendChild(this.oMonth);

     this.oMonth.oDatePicker = this;

     this.oMonth.onchange = this.oMonth.onkeyup =

        function() { this.oDatePicker.onMonth(); }

     for ( var i = 0; i < 12; i++ ) {

        this.oMonth.add(new Option(this.texts.months[i], i),undefined);

     }


     this.oYear = oTR2.insertCell(oTR2.cells.length);

     this.oYear.title = this.texts.yearTitle;

     this.oYear.oDatePicker = this;

     this.oYear.onclick = function() { this.oDatePicker.onYear(); }

     this.oYear.className = 'DatePickerHdrBtn';


     oTD2 = oTR2.insertCell(oTR2.cells.length);

     oTD2.title = this.texts.nextMonth;

     oTD2.onclick = function() { this.oDatePicker.onNext(); }

     oTD2.oDatePicker = this;

     oTD2.innerHTML = "&gt;&gt;";

     oTD2.className = 'DatePickerHdrBtn';


     oTR1 = oT1.insertRow(oT1.rows.length);

     for ( i = 0; i < 7; i++ ) {

        oTH1 = document.createElement("th");

        oTR1.appendChild(oTH1);

        oTH1.innerHTML = this.texts.days[i];

        oTH1.className = 'DatePicker';

     }


     this.aCells = new Array;

     for ( var j = 0; j < 6; j++ ) {

        this.aCells.push(new Array);

        oTR1 = oT1.insertRow(oT1.rows.length);

        for ( i = 0; i < 7; i++ ) {

           this.aCells[j][i] = oTR1.insertCell(oTR1.cells.length);

           this.aCells[j][i].oDatePicker = this;

           this.aCells[j][i].onclick =

              function() { this.oDatePicker.onDay(this); }

        }

     }

}
DatePicker.prototype.fill = function() {

     // first clear all

     this.clear();


     // place the dates in the calendar

     var nRow = 0;

     var d = new Date(this.dt.getTime());

     var m = d.getMonth();

     for ( d.setDate(1); d.getMonth() == m; d.setTime(d.getTime() + 86400000) ) {

        var nCol = d.getDay();

        this.aCells[nRow][nCol].innerHTML = d.getDate();


        if ( d.getDate() == this.dt.getDate() ) {

           this.aCells[nRow][nCol].className = 'DatePickerBtnSelect';

        }

        if ( nCol == 6 ) nRow++;

     }


     // set the month combo

     this.oMonth.value = m;


     // set the year text

     this.oYear.innerHTML = this.dt.getFullYear();

}


DatePicker.prototype.clear = function() {

     for ( var j = 0; j < 6; j++ )

        for ( var i = 0; i < 7; i++ ) {

           this.aCells[j][i].innerHTML = "&nbsp;"

           this.aCells[j][i].className = 'DatePickerBtn';

        }

}

DatePicker.prototype.onPrev = function() {

     if ( this.dt.getMonth() == 0 ) {

        this.dt.setFullYear(this.dt.getFullYear() - 1);

        this.dt.setMonth(11);

     } else {

        this.dt.setMonth(this.dt.getMonth() - 1);

     }

     this.fill();

}


DatePicker.prototype.onNext = function() {

     if ( this.dt.getMonth() == 11 ) {

        this.dt.setFullYear(this.dt.getFullYear() + 1);

        this.dt.setMonth(0);

     } else {

        this.dt.setMonth(this.dt.getMonth() + 1);

     }

     this.fill();

}

//The onMonth() method performs a similar function by reacting to changes in the month combo box:


DatePicker.prototype.onMonth = function() {

     this.dt.setMonth(this.oMonth.value);

     this.fill();

}

//When the year field is clicked on, the onYear() method asks the user to enter a new one:


DatePicker.prototype.onYear = function() {

     var y = parseInt(prompt(this.texts.yearQuestion, this.dt.getFullYear()));

     if ( !isNaN(y) ) {

        this.dt.setFullYear(parseInt(y));

        this.fill();

     }

}

//When a day is clicked on, the datepicker takes this as the date selection. The date is set and the callback function is called with the selected date.


DatePicker.prototype.onDay = function(oCell) {

     var d = parseInt(oCell.innerHTML);

     if ( d > 0 )

     {

        this.dt.setDate(d);

        this.hide();

        this.callback(this.dt);

     }

}

//To make internationalization easier, all forms of displayed text are held here in this definition. Feel free to change these values to the language of your choice (the days of the week must start on Sunday):


DatePicker.prototype.texts = {

     months: [

        "January", "February", "March",

        "April", "May", "June",

        "July", "August", "September",

        "October", "November", "December"

     ],

     days: ["S", "M", "T", "W", "T", "F", "S"],

     prevMonth: "Previous Month",

     nextMonth: "Next Month",

     yearTitle: "Year. Click to modify.",

     yearQuestion: "Enter a new year:"

};

//Now we’re ready to make use of the datepicker control. In this example, a textbox is used to hold the date value in some format. When the user clicks on the text box, control is passed to the datepicker control which handles the user input. The following HTML shows how easy this is:


//The showDP() and callback functions are shown here:


function showDP(oTxt,spanname) {

     if ( !document.getElementById ) return;

     oDatePicker.hide();

     // since we control the text format in callback(), getting the date is easy

     var aDt = oTxt.value.split('/');

     var dt = null;

     if ( aDt && (aDt.length == 3) ) {

        dt = new Date(getFloat(aDt[2]),getFloat(aDt[1])-1,getFloat(aDt[0]));

     }

     // store the textbox for use in the client

     oDatePicker.client = oTxt;

     oDatePicker.oSpan = document.getElementById(spanname);

     oDatePicker.show(dt, window.event.offsetX, window.event.offsetY, callback);
}


function callback(dt)
{
     oDatePicker.client.value =

     dt.getDate() + "/" +

     (dt.getMonth() + 1) + "/" +

     dt.getFullYear();
}

//The DatePicker has been designed to fit in with the look and feel of the surrounding page with the use of the following styles:
var oDatePicker = new DatePicker('theDatePicker');

function openDialog(url, width, height, callback,vz) {
   if (url.indexOf('?')<0) url+='?'; else url+='&';
   url+='t='+(new Date().getTime());
   url+='&pop=y';
   var o = window.showModalDialog('frame.ctl?EVENT=FRAME_GEN&url='+escape(url),vz,'dialogHeight:'+height+'px;dialogWidth:'+width+'px;center:yes;resizable:no;scroll:yes;status:no;');
   if (callback!=null)
      callback(o);
   return o;
}

function openDialog2(url, width, height, callback,vz) {
   if (url.indexOf('?')<0) url+='?'; else url+='&';
   url+='t='+(new Date().getTime());
   url+='&pop=y';
   var o = window.open('frame.ctl?EVENT=FRAME_GEN&url='+escape(url),vz,'dialogHeight:'+height+'px;dialogWidth:'+width+'px;center:yes;resizable:no;scroll:yes;status:no;');
   if (callback!=null)
      callback(o);
   return o;
}

function openWindow(url, width, height) {
   if (url.indexOf('?')<0) url+='?'; else url+='&';
   url+='t='+(new Date().getTime());
   url+='&pop=y';
    window.open('frame.ctl?EVENT=FRAME_GEN&url='+escape(url),height=height,width=width);
}

function openDialog3(url, width, height, callback,vz) {
   if (url.indexOf('?')<0) url+='?'; else url+='&';
   url+='t='+(new Date().getTime());
   url+='&pop=y';
   var o = window.showModalDialog('frame.ctl?EVENT=FRAME_GEN&url='+escape(url),vz,'dialogHeight:'+height+'px;dialogWidth:'+width+'px;center:yes;resizable:yes;scroll:yes;status:no;fullscreen:yes;maximize:true;');
   if (callback!=null)
      callback(o);
   return o;
}

function openDialog4(url, width, height, callback,vz) {
   if (url.indexOf('?')<0) url+='?'; else url+='&';
   url+='t='+(new Date().getTime());
   url+='&pop=y';
   var o = window.open('frame.ctl?EVENT=FRAME_GEN&url='+escape(url),vz,'dialogHeight:'+height+'px;dialogWidth:'+width+'px;center:yes;resizable:yes;scroll:yes;status:no;fullscreen:yes;maximize:true;');
   if (callback!=null)
      callback(o);
   return o;
}

function dialogReturn(x) {
   window.returnValue =x;
}

function submitEvent(x) {
   var c = window.event.srcElement;
   c.form.EVENT.value=x;
   c.form.submit();
}

//if (!window.showModalDialog) window.showModalDialog = window.openModalDialog;

function addOpt(c,value,text) {
   c.options.add(new Option(text,value));
}


function Is (){

  // convert all characters to lowercase to simplify testing
  var agt=navigator.userAgent.toLowerCase();

  this.major = parseInt(navigator.appVersion);
  this.minor = parseFloat(navigator.appVersion);

  //alert(navigator.appVersion);

  this.ns      = ((agt.indexOf('mozilla')!=-1) && (agt.indexOf('spoofer')==-1) &&
                  (agt.indexOf('compatible') == -1) && (agt.indexOf('opera')==-1) &&
                  (agt.indexOf('webtv')==-1) && (agt.indexOf('hotjava')==-1));
  this.ns2     = (this.ns && (this.major == 2));
  this.ns3     = (this.ns && (this.major == 3));
  this.ns4     = (this.ns && (this.major == 4));
  this.ns4up   = (this.ns && (this.major >= 4));
  this.nsonly  = (this.ns && _
                  ((agt.indexOf(";nav") != -1) || (agt.indexOf("; nav") != -1)));
  this.ns6     = (this.ns && (this.major == 5));
  this.ns6up   = (this.ns && (this.major >= 5));
  this.gecko   = (agt.indexOf('gecko') != -1);

  this.ie      = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
  this.ie3     = (this.ie && (this.major < 4));
  this.ie4     = (this.ie && (this.major == 4) && (agt.indexOf("msie 4")!=-1) );
  this.ie4up   = (this.ie  && (this.major >= 4));
  this.ie5     = (this.ie && (this.major == 4) && (agt.indexOf("msie 5.0")!=-1));
  this.ie5_5   = (this.ie && (this.major == 4) && (agt.indexOf("msie 5.5") !=-1));
  this.ie5up   = (this.ie  && !this.ie3 && !this.ie4);
  this.ie5_5up = (this.ie && !this.ie3 && !this.ie4 && !this.ie5);
  this.ie6     = (this.ie && (this.major == 4) && (agt.indexOf("msie 6.")!=-1) );
  this.ie6up   = (this.ie  && !this.ie3 && !this.ie4 && !this.ie5 && !this.ie5_5);

  // BUG: On AOL4, returns false if IE3 is embedded browser or if this is the first
  //      browser window opened.  Thus the variables is.aol, is.aol3, and is.aol4
  //      aren't 100% reliable.
  this.aol     = (agt.indexOf("aol") != -1);
  this.aol3    = (this.aol &&this.ie3);
  this.aol4    = (this.aol &&this.ie4);
  this.aol5    = (agt.indexOf("aol 5") != -1);
  this.aol6    = (agt.indexOf("aol 6") != -1);

  // NOTE: Opera and WebTV spoof Navigator.  We do strict client detection. If you
  //       want to allow spoofing, take out the tests for opera and webtv.
  this.opera   = (agt.indexOf("opera") != -1);
  this.opera2  = (agt.indexOf("opera 2") != -1 || agt.indexOf("opera/2") != -1);
  this.opera3  = (agt.indexOf("opera 3") != -1 || agt.indexOf("opera/3") != -1);
  this.opera4  = (agt.indexOf("opera 4") != -1 || agt.indexOf("opera/4") != -1);
  this.opera5  = (agt.indexOf("opera 5") != -1 || agt.indexOf("opera/5") != -1);
  this.opera5up= (this.opera && !this.opera2 && !this.opera3 && !this.opera4);

  this.webtv   = (agt.indexOf("webtv") != -1);

  this.aoltv   = ((agt.indexOf("navio") != -1) ||
                  (agt.indexOf("navio_aoltv") != -1));

  this.hotjava = (agt.indexOf("hotjava") != -1);
  this.hotjava3 = (this.hotjava && (this.major == 3));
  this.hotjava3up = (this.hotjava && (this.major >= 3));

 }

 var is =new Is();

/*
  Calculate layer's position
   By Stephen Miller at 3LA Pty Ltd (www.3la.com.au)
   - Uses Mozilla's 'Ultimate JavaScript Client Sniffer'
   - May also use code derived from public domain sources
 */
 function Pos(div){
  if (!div &&this)
    div =this;

  this.L = div.offsetLeft;
  this.T = div.offsetTop;
  this.W = div.offsetWidth;
  this.H = div.offsetHeight;

  // identify first offset parent element
  var divParent = div.offsetParent;

  // itterate through element hierarchy
  while (divParent !=null){
   if (divParent.tagName!='BODY'){
    if(is.ie){
     // if parent a table cell, then append cell border width to calcs
     if(divParent.tagName =="TD"){
      this.L += divParent.clientLeft;
      this.T += divParent.clientTop;
      this.W += divParent.clientWidth;
      this.H += divParent.clientHeight;
     }
    }
    else if (is.gecko) {
     // if parent is a table, then get its border as a number
     if(divParent.tagName =="TABLE"){
      var nParBorder = parseInt(divParent.border);
      // if no valid border attribute, then check the table's frame attribute
      if(isNaN(nParBorder)){
       var nParFrame = divParent.getAttribute('frame');
       // if frame has ANY value, then append one pixel to counter
       if(nParFrame !=null)
        nLeftPos += 1;
      }
      // if a border width is specified, then append the border width to counter
      else if(nParBorder > 0)
       nLeftPos += nParBorder;
     }
    }
    // append left offset of parent
    this.L += divParent.offsetLeft;
    this.T += divParent.offsetTop;
    this.W += divParent.offsetWidth;
    this.H += divParent.offsetHeight;
   }
   divParent = divParent.offsetParent;
  }
 }


var listenFocusTab = new Array();

function VM_fireFocusChange() {
   if (listenFocusTab)
      for (var i=0;i<listenFocusTab.length;i++) {
         var x = listenFocusTab[i];
         if (typeof x == 'string') eval(x); else x();
         //eval(x);
      }
}

var genCtr=0;

function docEl(x) {
   return document.getElementById(x);
}

function docElParent(x) {
   return window.opener.document.getElementById(x);
}

function VM_switchReadOnly(ctlname, stat) {
   var c=docEl(ctlname);

   if (c.oldReadOnly==null) c.oldReadOnly=c.readOnly;

   if (c.oldReadOnly) return;

   VM_enable(c,stat);
}

function lovPop(fldName, fldName2, caption, lovname, params) {
   openDialog('dlg.ctl?EVENT=LOVPOP&cap='+caption+'&lov='+lovname+'&'+params,700,400,
      function (a) {
         if (a!=null) {
            var f=docEl(fldName);
            var f2=docEl(fldName2);
            f.value=a.code;
            f2.value=a.desc;
            window.lovPopResult=a;
            if (f.VE_onchange)
               f.VE_onchange(this);
         }
      }
   );
}

function lovPop2(fldName, fldName2, date, caption, lovname, params) {
   openDialog('dlg.ctl?EVENT=LOVPOP&date='+date+'&cap='+caption+'&lov='+lovname+'&'+params,700,400,
      function (a) {
         if (a!=null) {
            var f=docEl(fldName);
            var f2=docEl(fldName2);
            f.value=a.code;
            f2.value=a.desc;
            window.lovPopResult=a;
            if (f.VE_onchange)
               f.VE_onchange(this);
         }
      }
   );
}

function lovPop3(fldName, fldName2, date, caption, lovname, params) {
   openDialog('dlg.ctl?EVENT=LOVPOP&cap='+caption+'&lov='+lovname+'&'+params,700,400,
      function (a) {
         if (a!=null) {
            var f=docEl(fldName);
            var f2=docEl(fldName2);
            
            f.value=a.code;
            f2.value=a.desc;
            window.lovPopResult=a;
            if (f.VE_onchange)
               f.VE_onchange(this);
         }
      }
   );
}

function uploadDoc(par, f) {
   var p="";
   for (x in par) {
      p+='&'+x+'='+par[x];
   }

   openDialog('w.ctl?EVENT=FILE_UPLOAD'+p,600,300,f);
}

function ireload(x,n) {
   var p =docEl(x+'descdiv');
   if (p==null) return;

   if (n!=null)
      if (n.image=='Y') {
         p.innerHTML='<img src="thumb.ctl?EVENT=FILE&fileid='+n.id+'&thumb=64">';
      } else {
         p.innerHTML='<div style="width:100%; border:solid 1px black">'+n.text+'</div>';
      }

   /*if (p.tagName.toUpperCase()=="IMG") {
      p.style.width="auto";
      p.style.height="auto";
      p.src = "thumb.ctl?EVENT=FILE&fileid="+n.id+"&thumb=64";
      p.style.width="auto";
      p.style.height="auto";
   }*/
}