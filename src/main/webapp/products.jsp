<%@ include file = "header.jsp" %>

<%@ page import="shop.Product"%>

<jsp:useBean id='db'
             scope='session'
             class='shop.ShopDB' />

<table>
<tr>
<th> Title </th> <th> Price </th> <th> Picture </th>
</tr>

<%
    for (Product product : db.getAllProducts() ) {
%>
        <tr>
             <td> <%= product.title %> </td>
             <td> <%= product.getPrice() %>&pound;</td>
             <td> <a href = '<%="viewProduct.jsp?pid="+product.PID%>'> <img src="<%= product.thumbnail %>"/> </a> </td>
        </tr>
<%
    }
%>

</table>

</body>
</html>
