package ch.application;

public interface Command {

    void checkValues(String... values) throws IllegalArgumentException;

    String execute(String... values) throws IllegalArgumentException;

}
