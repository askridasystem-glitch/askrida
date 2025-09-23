function validate()
{
    F = document.FileInsertForm;
    F.txtFile.style.background = "Yellow";
}

function checkImageExt()
{
    var stFile =  document.FileInsertForm.txtFile.value;
    var idx = 0;
    var stImgExt = "";
    for(i=0; i<stFile.length;i++)
    {
        if(stFile.charAt(i)==".")
        {
            idx = i;
        }
    }
    stImgExt = stFile.substring(idx+1,stFile.length)
    stImgExt = stImgExt.toUpperCase();
    if(stImgExt != "XML")
    {
        alert("File extention must be .xml");
        document.FileInsertForm.txtFile.focus();
        return false;
    }
    return true;
}

function save()
{
   if(document.FileInsertForm.txtFile.value=="")
   {
     alert("File should not null");
     document.FileInsertForm.txtFile.focus();
     return;
   }
   if(!checkImageExt()) return;
   document.cookie = document.FileInsertForm.txtFile.value;

   document.FileInsertForm.EVENTNAME.value = "UPLOAD_IMG";
   document.FileInsertForm.CALLOUT_EVENT.value = "UPLOAD_IMG";
   document.FileInsertForm.target = "PictureFrame";
   document.FileInsertForm.submit();
}

function processOK()
{
    if(iFileSize < (50*1024))
    {
        if (paramPic.frm.STATUS_CUST)
       {
          if (paramPic.frm.STATUS_CUST.value == 'SHAREHOLDER')
          {
             if (paramPic.entity=="P")
             {
                paramPic.frm.document.all.foto.src = stSrc;
             }
             else
             {
                paramPic.frm.document.all.foto1.src = stSrc;
             }
          }
          else if (paramPic.frm.STATUS_CUST.value == 'FAMILY')
          {
             paramPic.frm.document.all.foto.src = stSrc;
          }
       }
       else
       {
         paramPic.frm.document.all.foto.src = stSrc;
       }
       window.close();
    }
    else
    {
        alert("Maximum file is 50 Kb or 51200 bytes");
        document.FileInsertForm.target = "PictureFrame";
        document.FileInsertForm.action = "goToInsertImage.screen?request_id=CUSTMAINT_PICTURE_INSERT";
        document.FileInsertForm.submit();
    }
}