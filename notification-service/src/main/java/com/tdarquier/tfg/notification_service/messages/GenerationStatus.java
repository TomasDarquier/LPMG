package com.tdarquier.tfg.notification_service.messages;

import java.util.Date;

/**
 *
 * @param status descriptive message about the status of the code generation process
 * @param progress number from 1 to 4 to represent the progress in the status bar
 * @param timestamp only for logs
 */
public record GenerationStatus(
        Long userId,
        String status,
        int progress,
        Date timestamp
) {
}
