package ch.hsr;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {

    // TODO custom exception
    // TODO logger config https://dzone.com/articles/configuring-logback-with-spring-boot

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO hello text or banner
        // TODO don't print init stuff of spring
    }
}
