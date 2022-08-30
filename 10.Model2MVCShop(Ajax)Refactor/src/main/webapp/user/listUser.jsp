<%@ page contentType="text/html; charset=euc-kr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>회원 목록 조회</title>

<link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/css/admin.css" type="text/css">
<script src="//code.jquery.com/jquery-2.1.4.js" type="text/javascript"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>

<script type="text/javascript">
function init(){
	// userId 클릭시 dialog로 유저 정보가 보여진다
	$( ".ct_list_pop td:nth-child(3)" ).on("click" , function() {
		var displayValue;
		//Debug..
		//alert(  $( this ).text().trim() );
		var userId = $(this).text().trim();
		$.ajax(
				{
					url : "/user/json/getUser/"+userId ,
					method : "GET" ,
					dataType : "json" ,
					headers : {
						"Accept" : "application/json",
						"Content-Type" : "application/json"
					},
					success : function(JSONData , status) {
						displayValue = "아이디 : "+JSONData.userId
										+"이  름 : "+JSONData.userName
										+"이메일 : "+JSONData.email
										+"ROLE : "+JSONData.role
										+"주소 : "+JSONData.addr
						$("#dialog").text(displayValue);
						$("#dialog").dialog();
					}
			});
	});//end of getUser
}

$(function(){	
	page = 1;
	$(window).scroll(function() {
		if ($(document).height() - 1000 <= parseInt($(window).scrollTop()) ){
			//현재페이지 증가
			page++;
			searchCondition = $("option:selected").val();
			searchKeyword = $("input[name='searchKeyword']").val();
			console.log("searchCondition : " + searchCondition);
			console.log("searchKeyword : " + searchKeyword);
			console.log("page : " + page);
			
			$.ajax(
	    			{
	    				url : "/user/json/listUser",
	    				data : JSON.stringify({
	    					currentPage : page,
	    					searchCondition : searchCondition,
	    					searchKeyword : searchKeyword
	    				}),
						method : "POST",
						dataType : "json",
						headers : {
							"Accept" : "application/json",
							"Content-Type" : "application/json"
						},
						success : function(JSONData, status) {
							beforeIndex = $("td[align='center'][height='120']:last").text() //마지막의 첫번째 자식
							
							// userId 클릭시 dialog로 유저 정보가 보여진다
							init();
							
							$.each(JSONData, function(index, item){
								
								if(item.email === null){
									item.email = "";
								}
								
								//form의 3번째 자식 table에 붙인다
								var displayValue = 
										"<tr class='ct_list_pop'>"
											+"<td align='center' height='120'>"+(parseInt(beforeIndex)+index+1)+"</td>"
											+"<td height='120'></td>"
											+"<td align='left' height='120'>"+item.userId+"</td>"
											+"<td height='120'></td>"
											+"<td align='left' height='120'>"+item.userName+"</td>"
											+"<td height='120'></td>"
											+"<td align='left' height='120'>"+item.email+"</td>"
										+"</tr>"
										+"<tr>"
											+"<td colspan='11' bgcolor='D6D7D6' height='1'></td>"
										+"</tr>"
										
										console.log("displayValue : " + displayValue);
										$( $("form").children()[3] ).append(displayValue);
								
							})//$.each
					}//success
	    		});//end of ajax
		}//end of if
	});//end of scroll
	
	//table 간격 알 수 있게 색깔 입히기 그래서 ajax로 받은 데이터 정렬 잘 맞추기, ajax dialog 나오게 하기
	//$("form").children()[3]
})

</script>

<script type="text/javascript">
	// 검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScrpt 이용  
	function fncGetUserList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();		
	}
</script>

</head>

<body bgcolor="#ffffff" text="#000000" onload="javascript:init()">

	<div id="dialog" title="Basic dialog"></div>

	<div style="width: 98%; margin-left: 10px;">

		<form name="detailForm" action="/user/listUser" method="post">

			<table width="100%" height="37" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="15" height="37"><img src="/images/ct_ttl_img01.gif" width="15" height="37" /></td>
					<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="93%" class="ct_ttl01">회원 목록조회</td>
							</tr>
						</table>
					</td>
					<td width="12" height="37"><img src="/images/ct_ttl_img03.gif" width="12" height="37" /></td>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
				<tr>
					<td align="right">
					<select name="searchCondition" class="ct_input_g" style="width: 80px">
							<option value="0" ${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>회원ID</option>
							<option value="1" ${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>회원명</option>
					</select> <input type="text" name="searchKeyword" value="${! empty search.searchKeyword ? search.searchKeyword : ""}"  class="ct_input_g" style="width: 200px; height: 20px">
					</td>
					<td align="right" width="70">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="17" height="23"><img src="/images/ct_btnbg01.gif" width="17" height="23"></td>
								<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top: 3px;">
									<a href="javascript:fncGetUserList('1');">검색</a>
								</td>
								<td width="14" height="23"><img src="/images/ct_btnbg03.gif" width="14" height="23"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
				<tr>
					<td colspan="11">전체 ${resultPage.totalCount } 건수, 현재 ${resultPage.currentPage} 페이지</td>
				</tr>
				<tr>
					<td class="ct_list_b" width="100">No</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">회원ID</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">회원명</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">이메일</td>
				</tr>
				<tr>
					<td colspan="11" bgcolor="808285" height="1"></td>
				</tr>

				<c:set var="i" value="0" />
				<c:forEach var="user" items="${list}">
					<c:set var="i" value="${ i+1 }" />
					<tr class="ct_list_pop">
						<td align="center" height="120">${ i }</td>
						<td height="120"></td>
						<td align="left" height="120">${user.userId}</td>
						<td height="120"></td>
						<td align="left" height="120">${user.userName}</td>
						<td height="120"></td>
						<td align="left" height="120">${user.email}</td>
					</tr>
					<tr>
						<td colspan="11" bgcolor="D6D7D6" height="1"></td>
					</tr>
				</c:forEach>
			</table>

			<!-- PageNavigation Start... -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
				<tr>
					<td align="center"><input type="hidden" id="currentPage" name="currentPage" value="" />
						<%-- <jsp:include page="../common/pageNavigator.jsp"/> --%>
					</td>
				</tr>
			</table>
			<!-- PageNavigation End... -->

		</form>
	</div>

</body>
</html>
