package org.opentosca.csarrepo.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opentosca.csarrepo.exception.AuthenticationException;
import org.opentosca.csarrepo.model.User;
import org.opentosca.csarrepo.model.WineryServer;
import org.opentosca.csarrepo.service.ShowWineryServerService;

import freemarker.template.Template;

/**
 * Servlet implementation class HelloWorldServlet
 */
@SuppressWarnings("serial")
@WebServlet(WineryServerDetailsServlet.PATH)
public class WineryServerDetailsServlet extends AbstractServlet {

	private static final Logger LOGGER = LogManager.getLogger(WineryServerDetailsServlet.class);

	private static final String TEMPLATE_NAME = "wineryServerDetailsServlet.ftl";
	public static final String PATH = "/wineryserver/*";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WineryServerDetailsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User user = checkUserAuthentication(request, response);

			Map<String, Object> root = getRoot(request);
			Template template = getTemplate(this.getServletContext(), TEMPLATE_NAME);

			// TODO: length-check
			String[] pathInfo = request.getPathInfo().split("/");
			// TODO: handle exception
			long wineryServerId = Long.parseLong(pathInfo[1]); // {id}

			// TODO: add real UserID
			ShowWineryServerService showWineryService = new ShowWineryServerService(user.getId(), wineryServerId);
			if (showWineryService.hasErrors()) {
				this.addErrors(request, showWineryService.getErrors());
				throw new ServletException("WineryServerDetailsServlet has errors");
			}

			WineryServer result = showWineryService.getResult();
			root.put("title", String.format("Winery server: %s", result.getName()));

			root.put("wineryServer", result);

			template.process(root, response.getWriter());
		} catch (AuthenticationException e) {
			return;
		} catch (Exception e) {
			this.addError(request, e.getMessage());
			this.redirect(request, response, DashboardServlet.PATH);
			LOGGER.error(e);
		}
	}

}
