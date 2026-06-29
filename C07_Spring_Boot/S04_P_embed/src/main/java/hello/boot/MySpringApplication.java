package hello.boot;

import hello.spring.HelloConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class MySpringApplication {

    public static void run(Class configClass, String[] args) {
        System.out.println("MySpringApplication.run args=" + List.of(args));
        Tomcat tomcat = new Tomcat();

        String baseDir = null;
        try {
            baseDir = Files.createTempDirectory("tomcat-base.").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tomcat.setBaseDir(baseDir);

        Connector connector = new Connector();
        connector.setPort(8080);
        tomcat.setConnector(connector);

        File docBase = new File(baseDir, "webapps");
        if (!docBase.exists()) {
            docBase.mkdirs();
        }

        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(configClass);

        DispatcherServlet dispatcher = new DispatcherServlet(appContext);

        Context context = tomcat.addContext("", docBase.getAbsolutePath());
        tomcat.addServlet("", "dispatcher", dispatcher);
        context.addServletMappingDecoded("/", "dispatcher");

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
