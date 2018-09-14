package au.com.quaysystems.aidx.web;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AIDXServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String webAppPath;
       
     public AIDXServlet() {
        super();
    }

	public void init (ServletConfig config) throws ServletException{
		super.init(config);
		webAppPath = getServletContext().getRealPath("/");
	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)   throws ServletException, IOException {
		System.out.println(String.format("====> %s  :  %s  %s", request.getRemoteAddr(), request.getRequestURI(), request.getQueryString()));
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		System.out.println(String.format("====> %s  :  %s  %s", request.getRemoteAddr(), request.getRequestURI(), request.getQueryString()));
	}
}
