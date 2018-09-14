package au.com.quaysystems.aidx.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.quaysystems.aidx.MessageManager;

public class SampleServlet extends AIDXServlet {
	private static final long serialVersionUID = 1L;
	private String nowYear;

	public SampleServlet() {
		super();
		ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
		nowYear = new Integer(now.getYear()).toString();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
		System.out.println("Message Handler called");
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml; charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Access-Control-Allow-Origin", "*");

		//Gather some common parameter
		int num = 1;		
		try {
			num = Integer.parseInt(request.getParameter("numMsg"));
		} catch (NumberFormatException e) {
			num = 1;
		}

		boolean showEnclosing = Boolean.parseBoolean(request.getParameter("showEnclosing"));
		String fromSys = (request.getParameter("from") == null)?"AODB":request.getParameter("from");
		String apt = (request.getParameter("apt") == null)?"BAH":request.getParameter("apt");
		String year =  (request.getParameter("year") == null)?nowYear:request.getParameter("year");
		
		if (apt.length() > 3) {
			apt = apt.substring(0, 3);
		}

		MessageManager mm = MessageManager.getMessageManager(apt, year, this.webAppPath);
		mm.originator = fromSys;
		mm.delSystem = fromSys;
		mm.bNumericID = Boolean.parseBoolean(request.getParameter("numericID"));		
		mm.schemaVersion = (request.getParameter("schemaVersion") == null)?"16.1":request.getParameter("schemaVersion");

		if (showEnclosing) out.println("<messages>");
		out.println(mm.getRandomFlightsXML(num, (request.getParameter("type") != null)?request.getParameter("type"):"Normal"));	
		if (showEnclosing) out.println("</messages>");

	}
}
