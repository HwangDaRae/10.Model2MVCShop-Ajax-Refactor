<%@ page contentType="text/html; charset=euc-kr" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>Insert title here</title>
</head>

<body>

<form name="updatePurchase" action="/purchase/updatePurchaseView/0" method="post">

������ ���� ���Ű� �Ǿ����ϴ�.

<c:forEach var="i" begin="0" end="${ fn:length(prodList) -1 }" step="1">
<table border=1>
	<tr class="ct_list_pop" id="divDataId">
		<td align="center"><img height="250" width="250" src="/images/uploadFiles/${ prodList[i].fileName }"/></td>
		<td></td>
		<td align="left">${ prodList[i].prodName }</td>
		<td></td>
		<td align="left">${ prodList[i].prodDetail }</td>
		<td></td>
		<td align="left">${ prodList[i].price }��</td>
		<td></td>
		<td align="left">${ list[i].amount }��</td>
		<td></td>
		<td align="left">${ prodList[i].price * list[i].amount }��</td>
	</tr>
</table>
</c:forEach>
<table border=1>
	<tr>
		<td>�ֹ���ȣ�� �� ����ϼ���</td>
		<td>${ list[0].tranId }<%-- ${ purchaseVO.purchaseProd.prodNo } --%></td>
		<td></td>
	</tr>
	<tr>
		<td>�����ھ��̵�</td>
		<c:if test="${ list[0].buyer.userId == 'non-member' }">
		<td>��ȸ��<%-- ${ purchaseVO.buyer.userId } --%></td>
		</c:if>
		<c:if test="${ list[0].buyer.userId != 'non-member' }">
		<td>${ list[0].buyer.userId }<%-- ${ purchaseVO.buyer.userId } --%></td>
		</c:if>
		<td></td>
	</tr>
	<tr>
		<td>���Ź��</td>
		<td>${ (fn:trim(list[0].paymentOption) == '1')?"���ݱ���":"�ſ뱸��" }<%-- ${ (purchaseVO.paymentOption == '1')?"���ݱ���":"�ſ뱸��" } --%></td>
		<td></td>
	</tr>
	<tr>
		<td>�������̸�</td>
		<td>${ list[0].receiverName }<%-- ${ purchaseVO.receiverName } --%></td>
		<td></td>
	</tr>
	<tr>
		<td>�����ڿ���ó</td>
		<td>${ list[0].receiverPhone }<%-- ${ purchaseVO.receiverPhone } --%></td>
		<td></td>
	</tr>
	<tr>
		<td>�������ּ�</td>
		<td>${ list[0].divyAddr }<%-- ${ purchaseVO.divyAddr } --%></td>
		<td></td>
	</tr>
		<tr>
		<td>���ſ�û����</td>
		<td>${ list[0].divyRequest }<%-- ${ purchaseVO.divyRequest } --%></td>
		<td></td>
	</tr>
	<tr>
		<td>����������</td>
		<td>${ list[0].divyDate }<%-- ${ purchaseVO.divyDate } --%></td>
		<td></td>
	</tr>
	<tr>
		<td>�� ����</td>
		<td>${ list[0].totalPrice }��<%-- ${ purchaseVO.totalPrice } --%></td>
		<td></td>
	</tr>
</table>
</form>

</body>
</html>