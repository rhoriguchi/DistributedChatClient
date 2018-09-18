package ch.application;

public interface Command {

    void checkValues(String... values) throws IllegalArgumentException;

    String run(String... values) throws IllegalArgumentException;

}
