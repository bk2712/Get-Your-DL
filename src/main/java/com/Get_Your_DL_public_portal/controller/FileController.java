package com.Get_Your_DL_public_portal.controller;

import com.Get_Your_DL_public_portal.entity.Document;
import com.Get_Your_DL_public_portal.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Autowired
    DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String fileType) throws IOException {
        return documentService.saveFile(file, fileType);
    }

    @GetMapping("/get")
    public byte[][] fetchFile() throws IOException {
        return documentService.getFile();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Document>> listUserFiles() {
        return documentService.listAllUserFiles();
    }

    @GetMapping("/get/{docId}")
    public ResponseEntity<Resource> previewFile(@PathVariable Long docId) throws IOException {
        return documentService.getFileById(docId);
    }

    @PostMapping("/report/orderReciept/generate")
    public  ResponseEntity<byte[]> generateOrderReciept(@RequestBody Map<String, String> payload){
        return documentService.generateOrderReciept(payload);
    }
}
