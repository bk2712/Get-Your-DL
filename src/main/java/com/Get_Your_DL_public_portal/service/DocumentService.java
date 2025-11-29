package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.dto.DL_UserDets;
import com.Get_Your_DL_public_portal.entity.ApplyForLicense;
import com.Get_Your_DL_public_portal.entity.Document;
import com.Get_Your_DL_public_portal.entity.LicenseDetail;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import com.Get_Your_DL_public_portal.repository.ApplyForLicenseRepo;
import com.Get_Your_DL_public_portal.repository.DocumentRepo;
import com.Get_Your_DL_public_portal.repository.UserDetailsRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    ApplyForLicenseRepo applyForLicenseRepo;

    @Autowired
    UserDetailsRepo userDetailsRepo;

    private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

    private static final String FOLDER_PATH= "C:/Get_your_dl_docs/";

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

    public ResponseEntity<byte[]> generateOrderReciept(Map<String, String> payload) {
        LicenseDetail licenseDets= applyForLicenseRepo.getById(UUID.fromString(payload.get("applicationId")));

        UserDetail user= userDetailsRepo.getReferenceById(UUID.fromString(licenseDets.getCreatedBy()));
        DL_UserDets users= new DL_UserDets(licenseDets, user);
        Map<String, Object> params = new HashMap<>();
        params.put("firstname", users.getUserDetail().getFirstname());
        params.put("lastname", users.getUserDetail().getLastname());
        params.put("address", users.getUserDetail().getAddress());
        params.put("city", users.getUserDetail().getCity());
        params.put("state", users.getUserDetail().getState());
        params.put("zip", String.valueOf(users.getUserDetail().getZip()));
        params.put("phone", users.getUserDetail().getPhone());
        Timestamp createdAt = users.getLicenseDetail().getCreatedAt();
        String createdAtStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdAt);
        params.put("createdAt", createdAtStr);
        try(var inputStream= new ClassPathResource("reports/orderReciept.jrxml").getInputStream()) {
            var report= JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            byte[] bytes= JasperExportManager.exportReportToPdf(jasperPrint);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }


    }
}
