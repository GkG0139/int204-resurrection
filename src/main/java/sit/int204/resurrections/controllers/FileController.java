package sit.int204.resurrections.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int204.resurrections.configs.FileStorageProperties;
import sit.int204.resurrections.services.FileService;

import java.net.URLConnection;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileStorageProperties fileStorageProperties;
    private final FileService service;

    @Autowired
    public FileController(FileStorageProperties fileStorageProperties, FileService service) {
        this.fileStorageProperties = fileStorageProperties;
        this.service = service;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testPropertiesMapping() {
        return ResponseEntity.ok(fileStorageProperties.getUploadDir());
    }

    @GetMapping("/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> loadFileAsResource(@PathVariable String fileName) {
        Resource foundFile = service.loadFileAsResource(fileName);
        String foundFileFilename = foundFile.getFilename();
//        String fileExtension = foundFileFilename.substring(foundFileFilename.lastIndexOf("."));
//        return switch (fileExtension) {
//            case ".pdf" -> ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(foundFile);
//            case ".png" -> ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(foundFile);
//            case ".jpg", ".jpeg" -> ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(foundFile);
//            case ".gif", ".jfif" -> ResponseEntity.ok().contentType(MediaType.IMAGE_GIF).body(foundFile);
//            default -> ResponseEntity.ok().contentType(MediaType.ALL).body(foundFile);
//        };
        String mimeType = URLConnection.guessContentTypeFromName(foundFileFilename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(foundFile);
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String uploadedFile = service.store(file);
        return ResponseEntity.ok("Successfully uploaded: " + uploadedFile);
    }

    @DeleteMapping("/{fileName:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        service.removeResource(fileName);
        return ResponseEntity.ok("Successfully deleted: " + fileName);
    }
}
