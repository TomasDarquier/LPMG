package com.example.gateway.controllers;

import com.example.gateway.clients.DownloadServiceClient;
import com.example.gateway.clients.RequestServiceClient;
import com.example.gateway.clients.UserClient;
import com.example.gateway.dtos.DownloadRowDto;
import com.example.gateway.dtos.ZipFileResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ViewController {

    final
    RequestServiceClient requestServiceClient;

    final
    UserClient userClient;

    final
    DownloadServiceClient downloadServiceClient;

    public ViewController(RequestServiceClient requestServiceClient, UserClient userClient, DownloadServiceClient downloadServiceClient) {
        this.requestServiceClient = requestServiceClient;
        this.userClient = userClient;
        this.downloadServiceClient = downloadServiceClient;
    }

    @GetMapping("")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user) {
        Long userId = userClient.getIdByEmail(user.getEmail());
        return new ModelAndView("index", Collections.singletonMap("modelId",userId));
    }

    @PostMapping("/submit/{id}")
    public ResponseEntity<Void> test(@PathVariable String id, @RequestBody String jsonValue){
        requestServiceClient.requestCodeGeneration(jsonValue, Long.valueOf(id));
        // como devuelvo 204, el form no redirecciona
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    @GetMapping("/profile")
    public ModelAndView profile(@AuthenticationPrincipal OidcUser user) {
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("username", user.getGivenName());

        Long userId = userClient.getIdByEmail(user.getEmail());
        List<DownloadRowDto> downloadRows = downloadServiceClient.getDownloads(String.valueOf(userId));
        downloadRows.sort(Comparator.comparing(DownloadRowDto::date).reversed());

        //envia las descargas al view, con tope de 5 rows
        if(downloadRows.size() >= 5){
            modelAndView.addObject("downloadRows", downloadRows.subList(0,5));
        }else if(!downloadRows.isEmpty()){
            modelAndView.addObject("downloadRows", downloadRows.subList(0,downloadRows.size()));
        }else {
            modelAndView.addObject("downloadRows", new ArrayList<DownloadRowDto>());
        }

        return modelAndView;
    }

    @GetMapping("/documentation")
    public ModelAndView documentation(@AuthenticationPrincipal OidcUser user) {
        return new ModelAndView("documentation");
    }

    @GetMapping("/guide")
    public ModelAndView guide(@AuthenticationPrincipal OidcUser user) {
        return new ModelAndView("guide");
    }

    @GetMapping("/zip/{bucket}")
    public ResponseEntity<byte[]> downloadZip(@PathVariable String bucket) {
        ZipFileResponse zipFile = downloadServiceClient.getDownload(bucket);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getFileName());
        headers.set(HttpHeaders.CONTENT_TYPE, "application/zip");
        headers.setContentLength(zipFile.getFileSize());

        return new ResponseEntity<>(zipFile.getFileData(), headers, HttpStatus.OK);
    }
}
