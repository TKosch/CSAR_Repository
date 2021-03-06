package org.opentosca.csarrepo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opentosca.csarrepo.exception.AuthenticationException;
import org.opentosca.csarrepo.model.User;
import org.opentosca.csarrepo.service.CreateOpenToscaServerService;

/**
 * Servlet for creation of OpenTOSCA server
 */
@SuppressWarnings("serial")
@WebServlet(CreateOpenToscaServerServlet.PATH)
public class CreateOpenToscaServerServlet extends AbstractServlet {

	private static final Logger LOGGER = LogManager.getLogger(CreateOpenToscaServerServlet.class);
	private static final String PARAM_OPEN_TOSCA_SERVER_NAME = "serverName";
	private static final String PARAM_OPEN_TOSCA_SERVER_URL = "serverUrl";
	public static final String PATH = "/createopentosca";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateOpenToscaServerServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(405, "Method Not Allowed");
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			User user = checkUserAuthentication(request, response);

			String openToscaName = request.getParameter(PARAM_OPEN_TOSCA_SERVER_NAME);
			String openToscaUrl = request.getParameter(PARAM_OPEN_TOSCA_SERVER_URL);

			CreateOpenToscaServerService service = new CreateOpenToscaServerService(user.getId(), openToscaName,
					openToscaUrl);

			LOGGER.debug("Request to create opentosca server " + openToscaName + " handeled by servlet");

			if (service.hasErrors()) {
				AbstractServlet.addErrors(request, service.getErrors());
			}

			this.redirect(request, response, ListOpenToscaServerServlet.PATH);
		} catch (AuthenticationException e) {
			return;
		}
	}

}
