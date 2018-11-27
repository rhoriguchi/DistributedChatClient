package ch.hsr.dsa.event.login;

import org.springframework.context.ApplicationEventPublisher;

public class LoginEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public LoginEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void loginSuccess() {
        applicationEventPublisher.publishEvent(new LoginEvent(this, false));
    }

    public void loginFailed() {
        applicationEventPublisher.publishEvent(new LoginEvent(this, true));
    }
}
