package com.example.gateway.clients;

import com.example.gateway.dtos.DownloadRowDto;
import com.example.gateway.dtos.ZipFileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "download-service")
@Component
public interface DownloadServiceClient{

    @GetMapping("/download/all/{id}")
    List<DownloadRowDto> getDownloads(@PathVariable("id") String id);

    @GetMapping("/download/zip/{bucket}")
    ZipFileResponse getDownload(@PathVariable("bucket") String bucket);
}
