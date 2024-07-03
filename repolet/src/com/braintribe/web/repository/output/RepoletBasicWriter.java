// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.web.repository.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

/**
 * Basic implementation of {@link RepoletWriter} with hardcoded html structure
 */
public class RepoletBasicWriter implements RepoletWriter<PrintWriter> {

	@Override
	public void writeList(String path, Collection<BreadCrumb> breadCrumbs, Collection<String> entries, PrintWriter writer, Map<String, Object> attributes) throws IOException {

		writer.println("<html>");
		writer.print("<title>Collection: ");
		writer.print(path);
		writer.println("</title>");
		writer.println("</head>");
		writer.println("<body>");
			
		if (breadCrumbs != null) {
			writer.print("<h3>");
			for (BreadCrumb breadCrumb : breadCrumbs) {
				if (breadCrumb.getLink() != null || !breadCrumb.getLink().trim().isEmpty()) {
					writer.print("<a href=\"");
					writer.print(breadCrumb.getLink());
					writer.print("\">");
					writer.print(breadCrumb.getName());
					writer.print("</a>/");
				} else {
					writer.print(breadCrumb.getName());
				}
			}
			writer.println("</h3>");
		}
			
		writer.println("<ul>");

		for (String entry : entries) {
			writer.print("<li><a href=\"");
			writer.print(entry);
			writer.print("\">");
			writer.print(entry);
			writer.println("</a></li>");
		}

		writer.println("</ul>");
		writer.println("</body>");
		writer.println("</html>");
	}

	@Override
	public void writeNotFound(String path, boolean printInspectedPaths, Collection<String> inspectedPaths, PrintWriter writer, Map<String, Object> attributes) throws IOException {

		writer.println("<html>");
		writer.println("<head>");
		writer.println("<title>Error 404 Not a valid request path</title>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println("<h3>HTTP ERROR 404</h3>");
		writer.print("<p>");
		writer.print(path);
		writer.println(" not found</p>");
		
		if (printInspectedPaths) {
			writer.println("<p>inspected paths:</p>");
			writer.println("<ol>");
			
			for (String inspectedPath : inspectedPaths) {
				writer.print("<li>");
				writer.print(inspectedPath);
				writer.println("</li>");
			}
			
			writer.println("</ol>");
		}
		
		writer.println("</body>");
		writer.println("</html>");

	}

}
