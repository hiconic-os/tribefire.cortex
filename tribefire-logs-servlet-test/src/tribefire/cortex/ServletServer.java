package tribefire.cortex;
import java.io.IOException;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletInfo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletServer {
    public static void main(String[] args) throws Exception {
        ServletInfo myServlet = Servlets.servlet("MyServlet", LogLevelServlet.class)
                .addMapping("/hello");

        DeploymentInfo deployment = Servlets.deployment()
                .setClassLoader(ServletServer.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName("myapp.war")
                .addServlets(myServlet);

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(deployment);
        manager.deploy();

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(manager.start())
                .build();

        server.start();
        System.out.println("Server started on http://localhost:8080/hello");
    }

    public static class MyServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.getWriter().write("Hello from MyServlet!");
        }
    }
}
