package com.sample.springredditclone.exception;

import org.springframework.mail.MailException;

public class SpringRedditException extends RuntimeException {

    public SpringRedditException(String excecptionMessage) {
        super(excecptionMessage);
    }

    public SpringRedditException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }
}
