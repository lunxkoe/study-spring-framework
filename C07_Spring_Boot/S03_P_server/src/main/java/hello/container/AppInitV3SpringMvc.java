package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

// WebApplicationInitializer를 구현하기만 하면 자동으로 호출됨
public class AppInitV3SpringMvc implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("AppInitV3SpringMvc.onStartup");

        // 스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class);

        // 스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcher = new DispatcherServlet(appContext);

        // 디스패처 서블릿을 서블릿 컨테이너에 등록 (이름 주의!)
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV3", dispatcher);

        // 모든 요청이 디스패처 서블릿을 통하도록 설정
        // - /hello-spring
        servlet.addMapping("/");
    }
}

// 요청의 우선순위
// - /spring/hello-spring: dispatcherV2 -> helloController
// - /hello-spring: dispatcherV3 -> helloController
// - /hello-servlet: helloServlet
// - 구체적인 것이 우선순위를 가짐
//      - /hello-spring => dispatcherV3

// 동작 원리
// - @HandlerTypes(WebApplicationInitializer.class)
// - SpringServletContainerInitializer implements ServletContainerInitializer {}
// - 이게 라이브러리에 등록되어있음