package hello.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
		// - 스프링 컨테이너를 생성함
		// - WAS(내장 톰캣)를 생성함
	}

}

// 빌드와 배포
// - 만들어진 jar 파일
//		- fat jar와 다른 구조
//		- jar은 다른 jar를 포함할 수 없지만 포함하고 있음
//		- 어떻게 된 것일까?
//		- JarLauncher -> hello.boot.BootApplication을 실행(중간에서 훅킹을 해서 실행해줌)
// - 결국 META-INF/MANIFEST.MF를 보면 비밀을 알 수 있음