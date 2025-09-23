<%@ page import="com.webfin.contact.forms.MasterBusinessSourceForm,
                 com.webfin.contact.forms.MasterMarketterForm,
                 com.crux.ff.model.FlexTableView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   MasterMarketterForm form = (MasterMarketterForm) frame.getForm();

   String formMode = form.getFormMode();

   FlexTableView fft = form.getFft();
   boolean isNew = fft!=null &&  fft.isNew();


%>
<c:evaluate when="<%="LIST".equalsIgnoreCase(formMode)%>" >

<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <c:listbox name="list" paging="true" >
            <c:listcol name="stFFTID" title="ID" selectid="selected" />
            <c:listcol name="stReference1" title="Kode" />
            <c:listcol name="stReference2" title="Deskripsi" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="New"  event="btAdd" />
         <c:button text="Edit"  event="btEdit" />
      </td>
   </tr>
</table>
</c:evaluate>
<c:evaluate when="<%="EDIT".equalsIgnoreCase(formMode)%>" >
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <c:field name="fft.stReference1" readonly="<%=!isNew%>" caption="Kode" type="string" presentation="standard" mandatory="true" />
               <c:field name="fft.stReference2"  caption="Deskripsi" type="string" presentation="standard" mandatory="true" />
            </table>
         </td>
      </tr>
      <tr>
         <td>
            <c:button text="Save" event="btnSave" />
            <c:button text="Cancel" event="btnCancel" />
         </td>
      </tr>
   </table>
</c:evaluate>

</c:frame>