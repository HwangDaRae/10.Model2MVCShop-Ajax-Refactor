<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<script type="text/javascript">
function purchaseFinish(){
	var tranId = document.getElementById('tranId').value;
	if( tranId.length == 14 ){
		document.detailForm.submit();
	}
}
</script>
</head>
<body>
	<form name="detailForm" action="/purchase/getNonMemPurchase" method="post">
		<table>
			<tr>
				<td>�ֹ���ȣ : <input type="text" id="tranId" name="tranId"><input type="button" value="�ֹ���ȸ" onclick="purchaseFinish()"></td>
			</tr>
		</table>
	</form>
</body>
</html>
