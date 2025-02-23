//package com.spring.mvc.service;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.FileContent;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.File;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.List;
//
//@Service
//public class GoogleDriveService {
//
//    private static final String APPLICATION_NAME = "Muadima Seller Portal"; // CHANGE THIS
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//
//    @Value("${google.drive.folder.id}")
//    private String folderId;
//
//    @Value("${google.credentials.file.path}")
//    private String credentialsFilePath;
//
//    // No-argument constructor is no longer needed, as Spring handles instantiation
//
//    public Res uploadImageToDrive(MultipartFile file) throws IOException, GeneralSecurityException {
//        Res res = new Res();
//
//        if (file.isEmpty()) {
//            res.setStatus(400);
//            res.setMessage("Uploaded file is empty.");
//            return res;
//        }
//
//        try {
//            Drive driveService = createDriveService();
//            File fileMetadata = new File();
//            fileMetadata.setName(file.getOriginalFilename());
//
//            if (folderId != null && !folderId.isEmpty()) {
//                fileMetadata.setParents(Collections.singletonList(folderId));
//            }
//
//            java.io.File tempFile = java.io.File.createTempFile("upload-", null);
//            file.transferTo(tempFile);
//            FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
//
//            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
//                    .setFields("id,webViewLink") // Get the webViewLink!
//                    .execute();
//
//            tempFile.delete(); // Clean up the temp file
//
//            res.setStatus(200);
//            res.setMessage("Image Successfully Uploaded To Drive");
//            res.setUrl(uploadedFile.getWebViewLink()); // Use webViewLink
//            return res;
//
//        } catch (IOException | GeneralSecurityException e) {
//            System.err.println("Error uploading to Google Drive: " + e.getMessage());
//            e.printStackTrace(); // Log the full stack trace
//            res.setStatus(500);
//            res.setMessage("Error uploading image: " + e.getMessage());
//            return res;
//        }
//    }
//
//
//    public void deleteFile(String fileId) throws IOException, GeneralSecurityException {
//        Drive driveService = createDriveService(); // Create Drive instance
//        try {
//            driveService.files().delete(fileId).execute();
//        } catch (IOException e) {
//            // Handle the exception appropriately.  Log it, and potentially re-throw
//            // a custom exception to be handled higher up in the call stack.
//            System.err.println("Error deleting file from Google Drive: " + e.getMessage());
//            throw new IOException("Failed to delete file from Google Drive", e);
//        }
//    }
//
//
//    //  Add update/get methods
//    public String getFileUrl(String fileId) throws IOException, GeneralSecurityException{
//        Drive driveService = createDriveService();
//        File file = driveService.files().get(fileId).setFields("webViewLink").execute();
//        return  file.getWebViewLink();
//
//    }
//    public void updateFile(String fileId, MultipartFile newFile) throws IOException, GeneralSecurityException {
//        if (newFile.isEmpty()) {
//            throw new IllegalArgumentException("New file cannot be empty."); // Or handle as you see fit
//        }
//        Drive driveService = createDriveService();
//        //Create Metadata
//        File fileMetadata = new File();
//        fileMetadata.setName(newFile.getOriginalFilename()); // Set the new file name
//
//        //Create file content
//        java.io.File tempFile = java.io.File.createTempFile("update-", null);
//        newFile.transferTo(tempFile);
//        FileContent mediaContent = new FileContent(newFile.getContentType(), tempFile);
//
//        try {
//            //Update
//            driveService.files().update(fileId, fileMetadata, mediaContent).execute();
//
//        } finally {
//            tempFile.delete(); // Always delete the temp file, even if update fails.
//        }
//    }
//
//
//    private Drive createDriveService() throws GeneralSecurityException, IOException {
//        try (InputStream in = new FileInputStream(credentialsFilePath)) {
//            GoogleCredential credential = GoogleCredential.fromStream(in)
//                    .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));
//
//            return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();
//        }
//    }
//
//    public String uploadFile(MultipartFile file) {
//        return null;
//    }
//
//
//    public static class Res {
//        private int status;
//        private String message;
//        private String url;
//
//        // Getters and setters
//        public int getStatus() {
//            return status;
//        }
//
//        public void setStatus(int status) {
//            this.status = status;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//    }
//}