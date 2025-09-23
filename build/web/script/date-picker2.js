function DatePicker(name) {

     this.name = name;

     this.dt = new Date();

     document.write('<span id="' + name +

        '" style="z-index:1; position:absolute;visibility:hidden;"' +

        'class="DatePicker"></span>');

}

DatePicker.prototype.show = function(dt, x, y, callback) {

     if ( dt ) this.dt = dt;

     this.callback = callback;


     // if not rendered yet, do so

     if ( !this.oSpan ) this.render();


     // set coordinates

     this.oSpan.style.left = x;

     this.oSpan.style.top= y;


     this.fill();


     this.oSpan.style.visibility = "visible";

     this.oMonth.focus();

}


DatePicker.prototype.hide = function() {

     if ( this.oSpan ) this.oSpan.style.visibility = "hidden";

}

//The render method uses DOM functions to create the structure of the datepicker control:


DatePicker.prototype.render = function() {

     var oT1, oTR1, oTD1, oTH1;

     var oT2, oTR2, oTD2;


     this.oSpan = document.getElementById(this.name);

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


function showDP(oTxt) {

     if ( !document.getElementById ) return;


     // since we control the text format in callback(), getting the date is easy

     var aDt = '20/01/2002'.split('/');

     var dt = null;

     if ( aDt && (aDt.length == 3) ) {

        dt = new Date(parseInt(aDt[2]),parseInt(aDt[0])-1,parseInt(aDt[1]));

     }


     // store the textbox for use in the client

     oDatePicker.client = oTxt;


     oDatePicker.show(dt, oTxt.offsetLeft, oTxt.offsetTop, callback);

}


function callback(dt)

{

     oDatePicker.client.value =

        (dt.getMonth() + 1) + "/" +

        dt.getDate() + "/" +

        dt.getFullYear();

}

//The DatePicker has been designed to fit in with the look and feel of the surrounding page with the use of the following styles:
