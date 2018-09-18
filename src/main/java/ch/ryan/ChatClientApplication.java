package ch.ryan;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ChatClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ChatClientApplication.class)
            .bannerMode(Banner.Mode.OFF)
            .run(args);
    }

    @Override
    public void run(String... args) {
        
    }

}
