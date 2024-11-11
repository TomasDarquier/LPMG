package com.tdarquier.tfg.download_service.dtos;

import java.util.Date;

public record DownloadRowDto(
        Date date,
        String size,
        String downloadUrl
) {
}
