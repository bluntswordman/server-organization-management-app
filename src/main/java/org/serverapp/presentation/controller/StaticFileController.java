package org.serverapp.presentation.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/static")
public class StaticFileController {
    @GetMapping("/images/profile_images/{fileName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        Path imgPath = Paths.get("src/main/resources/static/images/profile_images/" + fileName);
        if (!Files.exists(imgPath)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte[] bytes = Files.readAllBytes(imgPath);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
