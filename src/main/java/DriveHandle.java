/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fadia
 */
public class DriveHandle {
    
   
    private static final String SHAREDFOLDERID = "1WYePJKM-Ery3ecvdXsdzc-WRRHhuxN6w";
    private static final String APPLICATION_NAME = "Secure File Sharing";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";
    
    private static DriveHandle self = null;
    
    private Drive service;
    
    private DriveHandle(){
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            this.service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (Exception ex) {
            System.err.println("Something went wrong, when creating DriveHandle object.\n\n");
        } 
    }
    
    public static DriveHandle getDriveHandle(){
        if(self == null){
            self = new DriveHandle();
        }
        return self;
    }
    
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
    
    public static void printFiles(List<File> files){
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files: ");
            for (File file : files) {
                System.out.println(file);
            }
        }
    }
    
  
    //returns the uploaded file id
    public String uploadFile(java.io.File file) throws Exception{
        try {
            File fileMetadata = new File();
            fileMetadata.setName(file.getName());
            fileMetadata.setParents(Collections.singletonList(SHAREDFOLDERID));
            String mimeType = Files.probeContentType(Paths.get(file.getAbsolutePath()));
            FileContent mediaContent = new FileContent(mimeType, file);
            File uploadedFile = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            return uploadedFile.getId();
        } catch (IOException ex) {
            System.err.println("File is not uploaded!");
            return null;
        }
    }
    
    public List<File> getFilesList(){
        try {
            FileList result = service.files().list()
                    .setQ("'"+SHAREDFOLDERID+"' in parents")
                    .setSpaces("drive")
                    .execute();
            return result.getFiles();
        } catch (IOException ex) {
            System.err.println("\n\nSomething went wrong, when retrieving files.\n\n");
            return null;
        }
    }   
}
