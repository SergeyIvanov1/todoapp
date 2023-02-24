package com.ivanov_sergey.todoapp.security.events;

import com.ivanov_sergey.todoapp.dto.UserDTO;
import com.ivanov_sergey.todoapp.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private UserDTO userDTO;

    public OnRegistrationCompleteEvent(
            UserDTO userDTO, Locale locale, String appUrl) {
        super(userDTO);

        this.userDTO = userDTO;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}
