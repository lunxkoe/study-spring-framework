package hello;

import hello.boot.MySpringApplication;
import hello.boot.MySpringBootApplication;

@MySpringBootApplication
public class MySpringBootMain {

    public static void main(String[] args) {
        MySpringApplication.run(MySpringBootMain.class, args);
        // - 내부에서 컴포넌트 스캔을 실행해서 빈을 등록함
    }
}

// - 이것이 스프링 부트!