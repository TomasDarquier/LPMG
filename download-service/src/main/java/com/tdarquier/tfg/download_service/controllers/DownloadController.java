package com.tdarquier.tfg.download_service.controllers;

import com.tdarquier.tfg.download_service.clients.ActivitiesClient;
import com.tdarquier.tfg.download_service.dtos.DownloadRowDto;
import com.tdarquier.tfg.download_service.dtos.ZipFileResponse;
import com.tdarquier.tfg.download_service.services.MinioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/download")
public class DownloadController {

    private final MinioServiceImpl minioService;

    private final ActivitiesClient activitiesClient;

    @GetMapping("/all/{id}")
    public List<DownloadRowDto> getDownloads(@PathVariable("id") String id) {
        List<String> buckets = minioService.listBucketsById(Long.valueOf(id));
        List<DownloadRowDto> downloads = new ArrayList<>();
        if(buckets == null || buckets.isEmpty()) {
            return downloads;
        }
        for (String bucket : buckets) {
            downloads.add(new DownloadRowDto(
                    generateDate(bucket.substring(bucket.indexOf("-") + 1)),
                    minioService.getSize(bucket),
                    "/zip/" + bucket
            ));
        }
        return downloads;
    }

    private Date generateDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datePart = date.substring(0,14);
        try {
            Date parsedDate = inputFormat.parse(datePart);
            String formattedDate = outputFormat.format(parsedDate);
            return outputFormat.parse(formattedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/zip/{bucket}")
    public ZipFileResponse downloadZip(@PathVariable String bucket) {
            ZipFileResponse response = minioService.getZip(bucket);
            activitiesClient.registerDownloadActivity(Long.valueOf(bucket.substring(0,bucket.indexOf('-'))));
            return response;
    }
}
