package org.cagrid.gaards.websso.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Returns a writer to write to the browser
		PrintWriter out = response.getWriter();

		// Writes the string to the browser.
		out.println("Begin Printing User Attributes");
		Enumeration attributeNames = request.getSession().getAttributeNames();
		for (; attributeNames.hasMoreElements();) {
			// Get the name of the attribute
			String name = (String) attributeNames.nextElement();
			if (name.contains("CAGRID")) {
				Object value = (Object) request.getSession().getAttribute(name);
				out.println("<<<<< ATTRIBUTE NAME >>>>>" + name
						+ "<<<<< ATTRIBUTE VALUE >>>>>" + value);
			}
		}
		out.println("Done Printing User Attributes");
		out.close();
	}
}