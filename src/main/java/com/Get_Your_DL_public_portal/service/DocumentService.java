package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.entity.Document;
import com.Get_Your_DL_public_portal.repository.DocumentRepo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    DocumentRepo docRepo;

    @Autowired
    JwtServiceImpl jwtService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    MyProfileServiceImpl myProfileService;

    private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

    private static final String FOLDER_PATH= "C:/Users/Bhavesh Kaushik/Documents/GetYourDLDocs/";

    public ResponseEntity<?> saveFile(MultipartFile file, String fileType) throws IOException {
        String filePath= FOLDER_PATH+file.getOriginalFilename();
        Document doc= new Document();
        doc.setFileName(file.getOriginalFilename());
        doc.setFileMimeType(file.getContentType());
        doc.setFilePath(filePath);
        doc.setFileType(fileType);
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        UUID fetchedUserId= myProfileService.fetchLoggedInUserIdFromToken(token);
        doc.setUserId(fetchedUserId);
        file.transferTo(new File(filePath));
        Document previousDoc= docRepo.findByFileType(fileType, fetchedUserId);
        if(previousDoc != null){
            // update details;
            docRepo.update(doc.getFileMimeType(), doc.getFileName(), doc.getFilePath(), previousDoc.getId().intValue());
        }
        else docRepo.save(doc);
        return ResponseEntity.ok("File is uploaded successfully!");
    }

    public byte[][] getFile() throws IOException{
        String authHeader = request.getHeader("Authorization");
        try{
            String token = authHeader.substring(7);
            UUID fetchedUserId= myProfileService.fetchLoggedInUserIdFromToken(token);
            List<Document> documents = docRepo.findByUserId(fetchedUserId);

            if (documents.isEmpty()) {
                throw new RuntimeException("No documents found for user: " + fetchedUserId);
            }

            byte[][] files = new byte[documents.size()][];
            for (int i = 0; i < documents.size(); i++) {
                files[i] = Files.readAllBytes(new File(documents.get(i).getFilePath()).toPath());
            }
            return files;
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch documents", e);
        }
    }

    public ResponseEntity<List<Document>> listAllUserFiles() {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        UUID fetchedUserId = myProfileService.fetchLoggedInUserIdFromToken(token);

        List<Document> documents = docRepo.findByUserId(fetchedUserId);

        return ResponseEntity.ok(documents);
    }

    public ResponseEntity<Resource> getFileById(Long docId) throws IOException{
        Document doc = docRepo.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        try {
            Path path = Paths.get(doc.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found or not readable: " + path.toString());
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(doc.getFileMimeType()))
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch document", e);
        }
    }
}
