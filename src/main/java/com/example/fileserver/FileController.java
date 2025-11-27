package com.example.file_server;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileController {

    private final Path storagePath;

    public FileController(@Value("${file.storage.location}") String storageLocation) throws IOException {
        this.storagePath = Paths.get(storageLocation);
        Files.createDirectories(storagePath);
    }

    @GetMapping("/")
    public String index(Model model) throws IOException {
        List<String> fileNames = getfileNames();
        model.addAttribute("files", fileNames);
        return "upload"; // templates/upload.html
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             Model model) throws IOException {

        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "upload";
        }

        String originalName = file.getOriginalFilename();
        String safeName = originalName != null ? originalName : "file";

        // Strip path separators for extra safety
        safeName = safeName.replace("/", "").replace("\\", "");

        // Resolve and normalize
        Path target = storagePath.resolve(safeName).normalize();

        // Ensure target is under storagePath
        if (!target.startsWith(storagePath)) {
            throw new SecurityException("Invalid file path");
        }

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        model.addAttribute("message", "File uploaded successfully: " + safeName);

        // Reload file list
        List<String> fileNames = getfileNames();
        model.addAttribute("files", fileNames);

        return "upload";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName)
            throws MalformedURLException {

        // Same: strip slashes for safety
        String safeName = fileName.replace("/", "").replace("\\", "");

        Path filePath = storagePath.resolve(safeName).normalize();

        // Ensure still under storagePath
        if (!filePath.startsWith(storagePath)) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filePath.getFileName() + "\"")
                .body(resource);
    }

    private List<String> getfileNames() throws IOException {
        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(storagePath)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    fileNames.add(path.getFileName().toString());
                }
            }
        }
        return fileNames;
    }
}
