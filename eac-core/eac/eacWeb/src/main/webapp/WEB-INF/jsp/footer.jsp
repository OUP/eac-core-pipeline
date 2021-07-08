<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@page import="java.util.*"%>

<div class="stickyFooter_footer">
	<footer class="footerBottom">
		<div class="row">
			<div class="col">
				<nav>
					<ul class="horizontalNav footerBottom_horizontalNav">
						<li class="horizontalNav_item"><a
							href="http://global.oup.com/contact_us/">Contact Us</a></li>
						<li class="horizontalNav_item"><a href="accessibility.htm">Accessibility</a></li>
						<li class="horizontalNav_item"><a
							href="https://global.oup.com/cookiepolicy">Cookie Policy</a></li>
						<li class="horizontalNav_item"><a href="privacyAndLegal.htm">Terms &
								Conditions</a></li>
						<li class="horizontalNav_item"><a
							href="https://global.oup.com/privacy">Privacy Policy</a></li>
					</ul>

				</nav>
				<p class="footerBottom_copyright">
					<spring:message code="footer.copyright" text="?footer.copyright?"
						arguments="<%=String.valueOf(Calendar.getInstance().get(Calendar.YEAR))%>" />
				</p>
			</div>
		</div>
	</footer>
</div>





