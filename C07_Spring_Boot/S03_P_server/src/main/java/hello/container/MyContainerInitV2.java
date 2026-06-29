package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

import java.util.Set;

@HandlesTypes(AppInit.class)
// - AppInit에 대한 구현체가 Set<Class<?>> c로 딸려옴
public class MyContainerInitV2 implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        System.out.println("MyContainerInitV2.onStartup");
        System.out.println("c = " + c);
        System.out.println("ctx = " + ctx);
        // - c = [class hello.container.AppInitV1Servlet]
        // - ctx = org.apache.catalina.core.ApplicationContextFacade@35ecab49

        for (Class<?> appInitClass : c) {
            try {
                AppInit appInit = (AppInit) appInitClass.getDeclaredConstructor().newInstance();
                // - new AppInitV1Servlet()과 동일한 코드(리플렉션 사용)
                appInit.onStartup(ctx);
                // - 실행되면서 서블릿이 등록됨
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }
}
