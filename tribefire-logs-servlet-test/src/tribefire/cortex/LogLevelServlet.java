package tribefire.cortex;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.stream.Stream;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogLevelServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		var levels = LoggingTools.getLogLevels();
		
		resp.setContentType("text/html");
		
		PrintWriter writer = resp.getWriter();
		
		writer.println("<form>");
		
		writer.println("<input spellchecks='false' size='50' list='loggers' name='levels'/>");
		writer.println("  <select name=\"logLevel\" id=\"logLevel\">");
		writer.println("    <option value=\"TRACE\">TRACE</option>");
		writer.println("    <option value=\"DEBUG\" selected>DEBUG</option>");
		writer.println("    <option value=\"INFO\">INFO</option>");
		writer.println("    <option value=\"WARN\">WARN</option>");
		writer.println("    <option value=\"ERROR\">ERROR</option>");
		writer.println("    <option value=\"FATAL\">FATAL</option>");
		writer.println("    <option value=\"INHERIT\">INHERIT</option>");
		writer.println("  </select>");
		
		writer.println("<button>add</button>");
		
		writer.println("<datalist id='loggers'>");
		
		SortedSet<String> pkgs = new TreeSet<>(new StructuredPackageComparator());
		
		Stream.of(Package.getPackages()).map(Package::getName).forEach(pkgs::add);
		
		for (var pkg: pkgs) {
			writer.print("<option value='");
			writer.print(pkg);
			writer.println("'/>");
		}
		
		writer.println("</datalist>");

		
		writer.println("</form>");
		
		writer.println("<textarea style='width:100%' rows='50' wrap='off' spellcheck='false'>");
		
		for (var entry: levels.entrySet()) {
			String loggerName = entry.getKey();
			Level level = entry.getValue();
			
			writer.print(loggerName);
			writer.print(" = ");
			writer.print(level.getName());
			writer.println();
		}
		
		writer.println("</textarea>");
	}

}
