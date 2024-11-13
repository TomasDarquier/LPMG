package com.tdarquier.tfg.download_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ZipFileResponse {

    private String fileName;
    private long fileSize;
    private byte[] fileData;

}
