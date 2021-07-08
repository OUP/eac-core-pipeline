<%@ include file="/WEB-INF/jsp/include.jsp" %>
<eac:homeMenuLinkGenerator var="homeMenuLinks"/>
<ul id="navbar" class="navbar">
	<li class="">
		<spring:message var="homeLabel" code="label.home" text="?label.home?" />
		<c:choose>
			<c:when test="${fn:length(homeMenuLinks) <= 1}">
				<a class="dc-mega" href="${homeMenuLinks[0].url}">${homeLabel}</a>
			</c:when>
			<c:otherwise>
				<a class="dc-mega" href="#">${homeLabel}</a>
				<div class="sub-container non-mega">
					<ul class="sub" style="">
						<c:forEach items="${homeMenuLinks}" var="menuLink">
							<li class=""><a href="${menuLink.url}">${menuLink.siteName}</a></li>
						</c:forEach>
					</ul>
				</div>
			</c:otherwise>
		</c:choose>
	</li>
</ul>
<script type="text/javascript">
	/* <![CDATA[ */
	jQuery(document).ready(function($) {
		jQuery.fx.off = true;
	    jQuery('#navbar').dcMegaMenu({
	    	sensitivity: 0
	    });
	});
	/* ]]> */	
</script>