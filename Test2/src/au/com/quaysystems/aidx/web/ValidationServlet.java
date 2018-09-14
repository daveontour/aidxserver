package au.com.quaysystems.aidx.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.quaysystems.aidx.AIDXValidator;

public class ValidationServlet extends AIDXServlet {
	private static final long serialVersionUID = 1L;
	private AIDXValidator aidxValidator;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request,  response);
		response.setContentType("application/json; charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().print(readFile(webAppPath+"/schemas/schemas.json", StandardCharsets.UTF_8));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		if (this.aidxValidator == null) {
			this.aidxValidator = new AIDXValidator(webAppPath);
		}

		String requestBody  = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		response.getWriter().println(this.aidxValidator.validateUserXML(requestBody,(request.getParameter("schemaVersion") == null)?"16.1":request.getParameter("schemaVersion")));
	}
}
