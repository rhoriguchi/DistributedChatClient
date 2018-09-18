package ch.application;

public interface Command {

    String run(String... values) throws IllegalArgumentException;

}
