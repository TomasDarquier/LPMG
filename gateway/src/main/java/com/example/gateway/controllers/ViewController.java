package com.example.gateway.controllers;

import com.example.gateway.clients.DownloadServiceClient;
import com.example.gateway.clients.RequestServiceClient;
import com.example.gateway.clients.UserClient;
import com.example.gateway.dtos.DownloadRowDto;
import com.example.gateway.dtos.ZipFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequestMapping("")
@RequiredArgsConstructor
@RestController
public class ViewController {

    final
    RequestServiceClient requestServiceClient;

    final
    UserClient userClient;

    final
    DownloadServiceClient downloadServiceClient;

    @GetMapping("")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user) {
        Long userId = userClient.getIdByEmail(user.getEmail());
        return new ModelAndView("index", Collections.singletonMap("modelId",userId));
    }

    @PostMapping("/submit/{id}")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public ResponseEntity<Void> test(@PathVariable String id, @RequestBody String jsonValue){
        requestServiceClient.requestCodeGeneration(jsonValue, Long.valueOf(id));
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public ModelAndView profile(@AuthenticationPrincipal OidcUser user) {
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("username", user.getGivenName());

        Long userId = userClient.getIdByEmail(user.getEmail());
        List<DownloadRowDto> downloadRows = downloadServiceClient.getDownloads(String.valueOf(userId));

        downloadRows.sort(Comparator.comparing(DownloadRowDto::date).reversed());
        List<DownloadRowDto> limitedDownloads = downloadRows.size() > 5
                ? downloadRows.subList(0, 5)
                : downloadRows.isEmpty() ? Collections.emptyList() : new ArrayList<>(downloadRows);

        modelAndView.addObject("downloadRows", limitedDownloads);

        return modelAndView;
    }


    @GetMapping("/documentation")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public ModelAndView documentation(@AuthenticationPrincipal OidcUser user) {
        return new ModelAndView("documentation");
    }

    @GetMapping("/guide")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public ModelAndView guide(@AuthenticationPrincipal OidcUser user) {
        return new ModelAndView("guide");
    }

    @GetMapping("/zip/{bucket}")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public ResponseEntity<byte[]> downloadZip(@PathVariable String bucket) {
        ZipFileResponse zipFile = downloadServiceClient.getDownload(bucket);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getFileName());
        headers.set(HttpHeaders.CONTENT_TYPE, "application/zip");
        headers.setContentLength(zipFile.getFileSize());

        return new ResponseEntity<>(zipFile.getFileData(), headers, HttpStatus.OK);
    }
}
