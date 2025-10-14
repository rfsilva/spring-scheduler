package com.taskscheduler.util;

import org.quartz.CronExpression;

public class CronExpressionValidator {

    /**
     * Validates if a string is a valid Quartz cron expression.
     *
     * @param cronExpression the cron expression to validate
     * @return true if the expression is valid, false otherwise
     */
    public static boolean isValidCronExpression(String cronExpression) {
        try {
            new CronExpression(cronExpression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}