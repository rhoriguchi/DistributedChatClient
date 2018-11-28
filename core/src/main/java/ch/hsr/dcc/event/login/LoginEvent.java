package ch.hsr.dcc.event.login;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LoginEvent extends ApplicationEvent {

    private final boolean failed;

    public LoginEvent(Object source, boolean failed) {
        super(source);
        this.failed = failed;
    }
}
