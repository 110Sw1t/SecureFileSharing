/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.api.client.repackaged.com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.FileUtils;
import org.fxmisc.richtext.InlineCssTextArea;

/**
 * FXML Controller class
 *
 * @author KyrillosNagyWadieYas
 */
public class MainWindowController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label IDTxt;
    @FXML
    private Label EmailTxt;
    @FXML
    private JFXTextField ChoosedFile;
    @FXML
    private JFXListView ListFiles;
    @FXML
    private JFXButton ChooseUploadBtn;
    @FXML
    private JFXButton ListFilesBtn;
    @FXML
    private JFXButton UploadBtn;
    @FXML
    private InlineCssTextArea log;
    @FXML
    private JFXTextField PublicKeyToEncryptWithTxt;
    @FXML
    private JFXTextField AESKeyToEncryptTxt;
    @FXML
    private JFXTextField EncryptedFileToBeDecTxt;
    @FXML
    private JFXTextField FileToDecTxt;
    @FXML
    private JFXButton AESKeyToEncryptBtn;
    @FXML
    private JFXButton EncryptBtn;
    @FXML
    private JFXButton PublicKeyToEncryptWithBtn;
    @FXML
    private JFXButton EncryptedFileToBeDecBtn;
    @FXML
    private JFXButton DecryptBtn;
    @FXML
    private Label SizeLbl;
    @FXML
    private Label OwnerIDLbl;
    @FXML
    private Label OwnerEmailLbl;

    private DriveHandle handle;
   
   
    private final byte headersLengthIndicatorLengthInBytes = 3;
    private int lengthBefore;
    private final String GREEN = "#008000";
    private final String RED = "#FF0000";
    private final String WHITE = "#FFF";
    private final String DASHEDLINE = "\n-------------------------------------------------------------------------------------------------------------------------\n";
    private final String FONTBIG = "15";
    private final String FONTSMALL = "11";
    private final String FONTWEIGHTBIG = "700";
    private final String FONTWEIGHTSMALL = "200";
    
    HashMap<String, String> userInfo;
    private final RandomString AESKeyGenerator = new RandomString(16);
    private HashMap<String, String> fileIDs = new HashMap<>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handle = DriveHandle.getDriveHandle();
        userInfo = handle.getUserInfo();
        OwnerIDLbl.setText(userInfo.get(DriveHandle.USERID));
        OwnerEmailLbl.setText(userInfo.get(DriveHandle.USEREMAIL));
        
//buttons Action listener
        ChooseUploadBtn
                .setOnAction(e -> ChooseFile(ChoosedFile));
        AESKeyToEncryptBtn.setOnAction(e -> ChooseFile(AESKeyToEncryptTxt));
        PublicKeyToEncryptWithBtn.setOnAction(e -> ChooseFile(PublicKeyToEncryptWithTxt));
        EncryptedFileToBeDecBtn.setOnAction(e -> ChooseFile(EncryptedFileToBeDecTxt));
//        EncryptBtn.setOnAction(e -> EncryptAESwithPublic());
        DecryptBtn.setOnAction(e -> DownloadFile());
        UploadBtn.setOnAction(e -> UploadFile());
        //listview selection listener
        ListFiles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("enteredddd......");

                FileToDecTxt.setText(newValue);
                logAppendTex("Choosed File From List:\n ", RED, FONTBIG, FONTWEIGHTBIG);
                logAppendTex(newValue + "\n", WHITE, FONTSMALL, FONTWEIGHTSMALL);

            }
        });

        try {
            // TODO
//            keys = RSA.buildKeyPair();
//            publicKey = keys.getPublic();
//            privateKey = keys.getPrivate();
//          ob = aes.GenerateKey();
            logAppendTex("Public Key and Private Key Created Successfully \n", GREEN, FONTBIG, FONTWEIGHTBIG);
//            logAppendTex("your public key is: \n" + keys.getPublic().toString(), WHITE, FONTSMALL, FONTSMALL);
            logAppendTex("AES Key Created Successfully \n------------------------------------------------\n", GREEN, FONTBIG, FONTWEIGHTBIG);
            logAppendTex("\nyour public, private and AES Keys saved successfully in the project folder" + DASHEDLINE, GREEN, FONTBIG, FONTWEIGHTBIG);

        } catch (Exception ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

  


    

    public void ChooseFile(JFXTextField tf) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        if (file != null) {
            tf.setText(file.getAbsolutePath());
            logAppendTex(tf.getPromptText() + ":\n ", RED, FONTBIG, FONTWEIGHTBIG);
            logAppendTex(file.getAbsolutePath() + "\n", WHITE, FONTSMALL, FONTWEIGHTSMALL);

        }
    }

    public void ChooseDirectory() {

        logAppendTex("Listing Files please Wait....\n", RED, FONTBIG, FONTWEIGHTBIG);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ListFiles.getItems().clear();
                fileIDs = new HashMap<>();
                List<com.google.api.services.drive.model.File> fList = handle.getFilesList();

                for (com.google.api.services.drive.model.File f : fList) {
                    String fileName = f.getName();
                    fileIDs.put(fileName, f.getId());
                    ListFiles.getItems().add(fileName);
                }
                logAppendTex("File Listed Successfully!\n", GREEN, FONTBIG, FONTWEIGHTBIG);

            }
        });

    }

    public void ClearList() {
        ListFiles.getItems().clear();
    }

    private void DownloadFile() {
        String AESKey = EncryptedFileToBeDecTxt.getText();
        String FileName = FileToDecTxt.getText();
        if (AESKey.equals("") || FileName.equals("")) {
            logAppendTex("Please Choose The AES Key Encrypted File and the file you want to decrypt\n", RED, FONTBIG, FONTWEIGHTBIG);
        } else {
            try {
                
                //download file
                ByteArrayOutputStream downloadedFileContentStream = handle.downloadFile(fileIDs.get(FileName));
                byte[] downloadedFileContentByteArr = downloadedFileContentStream.toByteArray();
                System.out.println("downloadedFileContentByteArr length = " + downloadedFileContentByteArr.length);
                //remove headers
                byte[] downloadedFileContentWithoutHeader = removeHeader(downloadedFileContentByteArr);
                //Code to be removed
                Scanner sc = new Scanner(System.in);
                String key = sc.nextLine();
                //decrypt file
                AdvancedEncryptionStandard aes = new AdvancedEncryptionStandard(key.getBytes(StandardCharsets.UTF_8));
                System.out.println("downloadedFileContentWithoutHeader length = " + downloadedFileContentWithoutHeader.length);
                byte[] decryptedFile = aes.decrypt(downloadedFileContentWithoutHeader);
                //Write to file
                FileOutputStream fos = new FileOutputStream(FileName, false);
                fos.write(decryptedFile);
                fos.flush();
                fos.close();
                logAppendTex("File Decrypted Successfully and You will find it on the project folder\n", GREEN, FONTBIG, FONTWEIGHTBIG);

            } catch (Exception ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
   
    public void UploadFile() {
        //TO DO upload file from this function
        String FileToUploadPath = ChoosedFile.getText().toString();

        if (ChoosedFile.getText().equals("")) {
            logAppendTex("Please Choose the file you want to upload \n", RED, FONTBIG, FONTWEIGHTBIG);
        } else {
            try {
                
                //get AES instance
                String AESKey = AESKeyGenerator.nextString();
                System.out.println("AESKey = " + AESKey);
                AdvancedEncryptionStandard aes = new AdvancedEncryptionStandard(AESKey.getBytes(StandardCharsets.UTF_8));
                //get selected file to be uploaded
                File selectedFileForUploading = new File(FileToUploadPath);
                //encrypt file
                byte[] encryptedFileContent = aes.encrypt(Files.readAllBytes(selectedFileForUploading.toPath()));
                System.out.println("encryptedFileContent length = " + encryptedFileContent.length);
                //add header to encrypted file content
                byte[] encryptedFileContentWithHeaders = prependHeader(encryptedFileContent);
                System.out.println("encryptedFileContentWithHeaders length = " + encryptedFileContentWithHeaders.length);
                //Create temporary file instance
                String fileName = selectedFileForUploading.getName();
                FileOutputStream encryptedFileOutputStream = new FileOutputStream(fileName, false);
                encryptedFileOutputStream.write(encryptedFileContentWithHeaders);
                encryptedFileOutputStream.flush();
                File encrypteFileWithHeadersToUpload = new File(fileName);
                //upload encrypted file
                String fileID = handle.uploadFile(encrypteFileWithHeadersToUpload);
                //print log update
                logAppendTex("File Encrypted & Uploaded Successfully, Please RE-LIST files\n", GREEN, FONTBIG, FONTWEIGHTBIG);
                //delete temporary file
                encrypteFileWithHeadersToUpload.delete();
                
            } catch (Exception ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }


    public void logAppendTex(String s, String Color, String FontSize, String FontWeight) {
        lengthBefore = log.getText().length();

        log.appendText(s);

        log.setStyle(lengthBefore, (lengthBefore + s.length()),
                "-fx-fill:" + Color + "; -fx-font-size:" + FontSize + "px; -fx-font-weight:" + FontWeight + ";");
        log.requestFollowCaret();
    }


    


    public byte[] prependHeader(byte[] baEncryptedContent) {
        byte[] ID = (userInfo.get(DriveHandle.USERID)+".").getBytes(StandardCharsets.UTF_8);
        byte[] Email = (userInfo.get(DriveHandle.USEREMAIL)+".").getBytes(StandardCharsets.UTF_8);
        byte[] Size = (baEncryptedContent.length + "bytes.").getBytes(StandardCharsets.UTF_8);
        
        assert Email.length <= 127 && ID.length <= 127 && Size.length <= 127: "Each header string can't be larger than 127 bytes";
        
        byte[] headersLengths = {(byte)ID.length, (byte)Email.length, (byte)Size.length};
        byte[] encryptedFileWithHeaders = 
                ByteUtils.byteArrayMerge(
                ByteUtils.byteArrayMerge(
                ByteUtils.byteArrayMerge(
                ByteUtils.byteArrayMerge(
                headersLengths
                , ID)
                , Email)
                , Size)
                , baEncryptedContent);
        return encryptedFileWithHeaders;
    }

    public byte[] removeHeader(byte[] contentWithHeader) {
        
        byte[][] result = ByteUtils.byteArraySplit(contentWithHeader, headersLengthIndicatorLengthInBytes);
        byte[] headersLengths = result[0];
        
        result = ByteUtils.byteArraySplit(result[1], headersLengths[0]);
        OwnerIDLbl.setText(new String(result[0]));
        
        result = ByteUtils.byteArraySplit(result[1], headersLengths[1]);
        OwnerEmailLbl.setText(new String(result[0]));
        
        result = ByteUtils.byteArraySplit(result[1], headersLengths[2]);
        SizeLbl.setText(new String(result[0]));
        
        System.out.println("encryptedFile string length = " + result[1].length);
        
        return result[1];

    }

   
}
