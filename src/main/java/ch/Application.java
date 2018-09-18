package ch;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application implements CommandLineRunner {

    // TODO logging
    // TODO output handling
    // TODO custom exception
    // TODO exception handling

    public static void main(String... args) {
        new SpringApplicationBuilder(Application.class)
            // TODO move to config
            .bannerMode(Banner.Mode.OFF)
            .run(args);
    }

    @Override
    public void run(String... args) {
        // TODO don't print init stuff of spring
        // TODO print start text
    }
}
