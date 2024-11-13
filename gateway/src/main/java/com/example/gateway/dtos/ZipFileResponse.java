package com.example.gateway.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ZipFileResponse {

    private String fileName;
    private long fileSize;
    private byte[] fileData;

}
