package com.Tubes.code.Service;

import com.Tubes.code.Configuration.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//@Service
public class FileStorageService {
//    private final String uploadDir;
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public FileStorageService(FileStorageProperties fileStorageProperties, JdbcTemplate jdbcTemplate) {
//        this.uploadDir = fileStorageProperties.getUploadDir();
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public String uploadFile(MultipartFile file) throws IOException {
//        // Simpan file ke folder
//        Path uploadPath = Paths.get(uploadDir);
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//        String filePath = uploadPath.resolve(file.getOriginalFilename()).toString();
//        file.transferTo(new File(filePath));
//
//        // Simpan path ke basis data
//        String sql = "INSERT INTO files (file_name, file_path) VALUES (?, ?)";
//        jdbcTemplate.update(sql, file.getOriginalFilename(), filePath);
//
//        return "File uploaded successfully!";
//    }
//
//    public byte[] downloadFile(int fileId) throws IOException {
//        // Ambil path file dari basis data
//        String sql = "SELECT file_path FROM files WHERE id = ?";
//        String filePath = jdbcTemplate.queryForObject(sql, new Object[]{fileId}, String.class);
//
//        // Baca file dari path
//        Path file = Paths.get(filePath);
//        return Files.readAllBytes(file);
//    }
//
//

}
