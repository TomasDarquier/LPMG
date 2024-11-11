package com.example.gateway.dtos;

import java.util.Date;

public record DownloadRowDto(
        Date date,
        String size,
        String downloadUrl
) {
}
