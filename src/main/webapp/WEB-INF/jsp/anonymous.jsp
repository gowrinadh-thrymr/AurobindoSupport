<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>


<html>
<head>
<link rel="stylesheet" href="/wps/contenthandler/dav/fs-type1/themes/Aurobindo-dashboard-theme/css/jquery-ui.css" type="text/css">
<link rel="stylesheet" href="/wps/contenthandler/dav/fs-type1/themes/Aurobindo-dashboard-theme/css/jquery.dataTables.min.css" type="text/css">

<script type="text/javascript" src="/wps/contenthandler/dav/fs-type1/themes/Aurobindo-dashboard-theme/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/wps/contenthandler/dav/fs-type1/themes/Aurobindo-dashboard-theme/js/dataTables.jqueryui.min.js"></script>

<style type="text/css">
.table-support thead tr th {
    color: #fff;
    background: #0179b5;
    font-size: 14px;
    padding: 5px;
}
.table-support tbody tr td {
    border: 1px solid #efefef;
}

#aurosupport_wrapper .fg-toolbar {
    padding: 5px 5px;
}

#aurosupport_length select,
#aurosupport_filter input[type="search"] {
    box-shadow: 0 2px 8px #ccc;
    padding: 5px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555;
    background-color: #fff;
    background-image: none;
    border: 1px solid #ccc;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
}
#aurosupport_filter label {
    padding-top: 3px;
}

.table-support tbody tr td {
    border: 1px solid #efefef;
    padding: 5px !important;
    vertical-align: middle;
}
.table-support thead tr th {
    color: #fff;
    background: #0179b5;
    font-size: 14px;
    padding: 6px 10px !important;
    vertical-align: middle;
    white-space: nowrap;
}
table.dataTable.display tbody tr.odd>.sorting_1, table.dataTable.order-column.stripe tbody tr.odd>.sorting_1 {
    background-color: #ffffff !important;
}
table.dataTable.display tbody tr:hover>.sorting_1, table.dataTable.order-column.hover tbody tr:hover>.sorting_1{
	    background-color: #ffffff !important;
}

.w-100{width:100%;float:left;}

.unit-addrs{
	float: left;
    width: 290px;
}

</style>

</head>


<body>




<table id="aurosupport" class=" table-support display" style="width:100%">
        <thead>
            <tr>
                <th>Sr.#</th>
                <th>Division</th>
                <th>Unit ID</th>
                <th>Helpdesk Email</th>
                <!-- <th>Internal Code</th> -->
                <th>HelpDesk Contact No</th>
                <th>IS Manager Name</th> 
                <th>IS Manager Mail ID's</th>
                <th>IS Manager Mobile Number</th>
                <th>Ext</th>
                <th>Unit Address</th>
              </tr>
        </thead>
        <tbody>
        
        <c:forEach var="unit" items="${supportjson}" varStatus="loopCounter" >
              <tr>
					<td><c:out value="${loopCounter.count}" /></td>
					<td><c:out value="${unit.division}" /></td>
					<td><c:out value="${unit.unitId}" /></td>
					<td><c:out value="${unit.helpdeskEmail}" /></td>
					<%-- <td><c:out value="${unit.internalCode}" /></td> --%>
					<td><c:out value="${unit.helpdeskContactNo}" /></td>
					<td><c:out value="${unit.isManagerName}" /></td>
					<td><c:out value="${unit.isManagerMailId}" /></td>
					<td><c:out value="${unit.isManagerMobileNo}" /></td>
					<td><c:out value="${unit.ext}" /></td>
					<td><span class="unit-addrs"><c:out value="${unit.unitAddress}" /></span></td>
				</tr>
       </c:forEach>


        
            
            
            
            
                    </tbody>
       <!-- <tfoot>
            <tr>
                <th>Sr.#</th>
                <th>Division</th>
                <th>Unit ID</th>
                <th>Helpdesk Email</th>
                <th>Internal Code</th>
                <th>HelpDesk Contact No</th>
                <th>IS Manager Name</th> 
                <th>IS Manager Mail ID's</th>
                <th>IS Manager Mobile Number</th>
                <th>Ext</th>
                <th>Unit Address</th>
            </tr>
        </tfoot>-->
    </table>



</body>
<script type="text/javascript">
$(document).ready(function() {
	//var support_data = '${supportjson}';
	//support_data=JSON.parse(support_data);
//	console.log(support_data);
    $('#aurosupport').DataTable({
        "bLengthChange": false,
        "bFilter": true,
        "bInfo": false,
        "bAutoWidth": false,
        "iDisplayLength": 10,
    });
    $("#aurosupport").wrap("<div class='table-responsive w-100'></div>");
} );
</script>

</html>