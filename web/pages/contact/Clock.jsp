
<?xml version="1.0"?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

   
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="refresh" content="60" />
        
        <title>Contoh JSP Sederhana</title>
        
        <style type="text/css">
            .big{font-family: helvetica, arial, sans-serif;
            font-weight: bold;
            font-size: 2em;}
        </style>
    </head>
    
    <body>
        <p class="big">Contoh JSP Sederhana</p>
        
        <table style="border: 6px outset;">
            <tr>
                <td style="background-color: black;">
                    <p class="big" style="color: cyan;">
                        
                        <%= new java.util.Date() %>
                        
                    </p>
                </td>
            </tr>
        </table>
    </body>
</html>
