<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:if test="${pageInfo.requestedPage ne 1 || !pageInfo.lastPage}">
	<tr>
		<td colspan="8" align="center">
			<div style="margin-left:40px;margin-right:40px;">
				<div style="float:left">
					<c:if test="${pageInfo.requestedPage ne 1}">
					<a id="previousPage" href="#"><spring:message code="label.previous" /></a>
						<script type="text/javascript">
							$('#previousPage').click(function() {
								sendAjaxRequest('previousPage');
								return false;
							});
						</script>
					</c:if>
				</div>
				<div style="float:right">
					<c:if test="${!pageInfo.lastPage}">
						<a id="nextPage" href="#"><spring:message code="label.next" /></a>
						<script type="text/javascript">
							$('#nextPage').click(function() {
								sendAjaxRequest('nextPage');
								return false;
							});
						</script>
					</c:if>
				</div>
			</div>
		</td>
	</tr>
</c:if>