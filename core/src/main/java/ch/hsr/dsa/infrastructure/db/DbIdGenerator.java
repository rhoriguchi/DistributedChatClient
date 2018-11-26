package ch.hsr.dsa.infrastructure.db;

import org.apache.commons.lang3.RandomStringUtils;


public class DbIdGenerator {

    public static Long getId() {
        String number = RandomStringUtils.random(17, false, true);
        return Long.valueOf(number) + 10 ^ 17;
    }

}
