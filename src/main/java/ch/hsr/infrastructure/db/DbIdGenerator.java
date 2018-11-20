package ch.hsr.infrastructure.db;

import java.util.Random;

public class DbIdGenerator {

    private final Random random;

    public DbIdGenerator() {
        this.random = new Random((System.currentTimeMillis()));
    }

    public Long getId() {
        long number = 0;

        do {
            number = Math.abs(random.nextLong()) >> 1;
        } while (String.valueOf(number).length() != 18);

        return number;
    }

}
