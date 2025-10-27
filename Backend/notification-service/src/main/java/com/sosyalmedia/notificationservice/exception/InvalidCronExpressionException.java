package com.sosyalmedia.notificationservice.exception;

public class InvalidCronExpressionException extends RuntimeException {

    public InvalidCronExpressionException(String message) {
        super(message);
    }

    public InvalidCronExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    //  Static factory method  (constructor yerine)
    public static InvalidCronExpressionException forCronExpression(String cronExpression) {
        return new InvalidCronExpressionException(
                String.format("Invalid cron expression: '%s'", cronExpression)
        );
    }
}