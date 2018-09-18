package ch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    // TODO custom exception

    // TODO logger config https://dzone.com/articles/configuring-logback-with-spring-boot

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        // TODO hello text or banner
        // TODO don't print init stuff of spring
    }
}
