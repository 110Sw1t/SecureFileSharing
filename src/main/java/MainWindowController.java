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
import java.io.ByteArrayInputStream;
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

    private final byte NUMBEROFHEADERS = 3;
    private int lengthBefore;
    private final String LOCALSTATEENCRYPTIONKEY = "xI9TcPZbGVdTPtrr";
    private final String GREEN = "#008000";
    private final String RED = "#FF0000";
    private final String WHITE = "#FFF";
    private final String DASHEDLINE = "\n-------------------------------------------------------------------------------------------------------------------------\n";
    private final String FONTBIG = "15";
    private final String FONTSMALL = "11";
    private final String FONTWEIGHTBIG = "700";
    private final String FONTWEIGHTSMALL = "200";
    private final String PUBLICKEYFILENAME = "PublicKey.txt";
    private final String ENCRYPTEDKEYMAPFILENAME = "EncryptedKeyMap.txt";

    private PrivateKey privateKey;

    HashMap<String, String> userInfo;
    private final RandomString AESKeyGenerator = new RandomString(16);
    private HashMap<String, String> fileIDs = new HashMap<>();
    private HashMap<String, String> fileNameKeyPairs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handle = DriveHandle.getDriveHandle();
        userInfo = handle.getUserInfo();
        loadState();
        IDTxt.setText(userInfo.get(DriveHandle.USERID));
        EmailTxt.setText(userInfo.get(DriveHandle.USEREMAIL));

//buttons Action listener
        ChooseUploadBtn.setOnAction(e -> ChooseFile(ChoosedFile));
//        AESKeyToEncryptBtn.setOnAction(e -> ChooseFile(AESKeyToEncryptTxt));
        PublicKeyToEncryptWithBtn.setOnAction(e -> ChooseFile(PublicKeyToEncryptWithTxt));
        EncryptedFileToBeDecBtn.setOnAction(e -> ChooseFile(EncryptedFileToBeDecTxt));
        EncryptBtn.setOnAction(e -> encryptAESwithPublic());
        DecryptBtn.setOnAction(e -> DownloadFile());
        UploadBtn.setOnAction(e -> UploadFile());
        //listview selection listener
        ListFiles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                FileToDecTxt.setText(newValue);
                AESKeyToEncryptTxt.setText(newValue);
                logAppendText("Choosed File From List:\n ", RED, FONTBIG, FONTWEIGHTBIG);
                logAppendText(newValue + "\n", WHITE, FONTSMALL, FONTWEIGHTSMALL);

            }
        });

        try {
            // TODO
            KeyPair kp = RSA.buildKeyPair();
            PublicKey pu = kp.getPublic();
            privateKey = kp.getPrivate();

            logAppendText("Public Key and Private Key Created Successfully \n", GREEN, FONTBIG, FONTWEIGHTBIG);
            logAppendText("your public key is: \n" + pu.toString(), WHITE, FONTSMALL, FONTSMALL);
            FilesUtils.writeObjectToFile(pu, PUBLICKEYFILENAME);
            logAppendText("\nYour public key is saved successfully in the project folder" + DASHEDLINE, GREEN, FONTBIG, FONTWEIGHTBIG);

        } catch (Exception ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void encryptAESwithPublic() {
        String fileName = AESKeyToEncryptTxt.getText();
        String PublicKeyFilePath = PublicKeyToEncryptWithTxt.getText();
        if (fileName.equals("") || PublicKeyFilePath.equals("")) {
            logAppendText("Please Enter File Name and Choose Public Key File path to start encryption\n", RED, FONTBIG, FONTWEIGHTBIG);
        } else {
            if (fileNameKeyPairs.get(fileName) == null) {
                logAppendText("File name entered doesn't have a corresponding key\n", RED, FONTBIG, FONTWEIGHTBIG);
            } else {
                try {
                    String AESKey = fileNameKeyPairs.get(fileName);
                    PublicKey publicKey = (PublicKey) FilesUtils.readObjectFromFile(PUBLICKEYFILENAME);
                    byte[] encryptedAESKey = RSA.encrypt(publicKey, AESKey);
                    FilesUtils.writeObjectToFile(encryptedAESKey, "EncryptedAESKeyFor-" + fileName + ".txt");
                    logAppendText("File Encrypted Successfully Please send it to the user who wants it (Check Application folder)\n", GREEN, FONTBIG, FONTWEIGHTBIG);
                } catch (Exception ex) {
                    Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void ChooseFile(JFXTextField tf) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        if (file != null) {
            tf.setText(file.getAbsolutePath());
            logAppendText(tf.getPromptText() + ":\n ", RED, FONTBIG, FONTWEIGHTBIG);
            logAppendText(file.getAbsolutePath() + "\n", WHITE, FONTSMALL, FONTWEIGHTSMALL);

        }
    }

    public void ChooseDirectory() {

        logAppendText("Listing Files please Wait....\n", RED, FONTBIG, FONTWEIGHTBIG);
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
                logAppendText("File Listed Successfully!\n", GREEN, FONTBIG, FONTWEIGHTBIG);

            }
        });

    }

    public void ClearList() {
        ListFiles.getItems().clear();
    }

    private void DownloadFile() {
        String AESKeyFileName = EncryptedFileToBeDecTxt.getText();
        String FileName = FileToDecTxt.getText();
        if (AESKeyFileName.equals("") || FileName.equals("")) {
            logAppendText("Please Choose The AES Key Encrypted File and the file you want to decrypt\n", RED, FONTBIG, FONTWEIGHTBIG);
        } else {
            try {

                //download file
                ByteArrayOutputStream downloadedFileContentStream = handle.downloadFile(fileIDs.get(FileName));
                byte[] downloadedFileContentByteArr = downloadedFileContentStream.toByteArray();
                System.out.println("downloadedFileContentByteArr length = " + downloadedFileContentByteArr.length);
                //remove headers
                byte[] downloadedFileContentWithoutHeader = removeHeader(downloadedFileContentByteArr);
                //get AES key
                byte[] encryptedAESKey = (byte[]) FilesUtils.readObjectFromFile(AESKeyFileName);
                //decrypt AES key with private key
                byte[] decryptedAESKey = RSA.decrypt(privateKey, encryptedAESKey);
                String key = new String(decryptedAESKey);
                //decrypt file
                AdvancedEncryptionStandard aes = new AdvancedEncryptionStandard(key.getBytes(StandardCharsets.UTF_8));
                System.out.println("downloadedFileContentWithoutHeader length = " + downloadedFileContentWithoutHeader.length);
                byte[] decryptedFile = aes.decrypt(downloadedFileContentWithoutHeader);
                //Write to file
                FileOutputStream fos = new FileOutputStream(FileName, false);
                fos.write(decryptedFile);
                fos.flush();
                fos.close();
                logAppendText("File Decrypted Successfully and You will find it on the project folder\n", GREEN, FONTBIG, FONTWEIGHTBIG);

            } catch (Exception ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void UploadFile() {
        //TO DO upload file from this function
        String FileToUploadPath = ChoosedFile.getText().toString();

        if (ChoosedFile.getText().equals("")) {
            logAppendText("Please Choose the file you want to upload \n", RED, FONTBIG, FONTWEIGHTBIG);
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
                //save filename/AES key 
                fileNameKeyPairs.put(fileName, AESKey);
                //upload encrypted file
                String fileID = handle.uploadFile(encrypteFileWithHeadersToUpload);
                //print log update
                logAppendText("File Encrypted & Uploaded Successfully, Please RE-LIST files\n", GREEN, FONTBIG, FONTWEIGHTBIG);
                //delete temporary file
                encrypteFileWithHeadersToUpload.delete();

            } catch (Exception ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void logAppendText(String s, String Color, String FontSize, String FontWeight) {
        lengthBefore = log.getText().length();

        log.appendText(s);

        log.setStyle(lengthBefore, (lengthBefore + s.length()),
                "-fx-fill:" + Color + "; -fx-font-size:" + FontSize + "px; -fx-font-weight:" + FontWeight + ";");
        log.requestFollowCaret();
    }

    public byte[] prependHeader(byte[] baEncryptedContent) {
        byte[] ID = (userInfo.get(DriveHandle.USERID) + ".").getBytes(StandardCharsets.UTF_8);
        byte[] Email = (userInfo.get(DriveHandle.USEREMAIL) + ".").getBytes(StandardCharsets.UTF_8);
        byte[] Size = (baEncryptedContent.length + "bytes.").getBytes(StandardCharsets.UTF_8);

        assert Email.length <= 127 && ID.length <= 127 && Size.length <= 127 : "Each header string can't be larger than 127 bytes";

        byte[] headersLengths = {(byte) ID.length, (byte) Email.length, (byte) Size.length};
        byte[] encryptedFileWithHeaders
                = ByteUtils.byteArrayMerge(
                        ByteUtils.byteArrayMerge(
                                ByteUtils.byteArrayMerge(
                                        ByteUtils.byteArrayMerge(
                                                headersLengths,
                                                 ID),
                                         Email),
                                 Size),
                         baEncryptedContent);
        return encryptedFileWithHeaders;
    }

    public byte[] removeHeader(byte[] contentWithHeader) {

        byte[][] result = ByteUtils.byteArraySplit(contentWithHeader, NUMBEROFHEADERS);
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

    public void saveState() {
        try {
            byte[] keyMap = ByteUtils.serialize(fileNameKeyPairs);
            byte[] encryptedKeyMap = new AdvancedEncryptionStandard(LOCALSTATEENCRYPTIONKEY.getBytes(StandardCharsets.UTF_8))
                    .encrypt(keyMap);
            FilesUtils.writeObjectToFile(encryptedKeyMap, ENCRYPTEDKEYMAPFILENAME);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadState(){
        try {
            byte[] encryptedKeyMap = (byte[])FilesUtils.readObjectFromFile(ENCRYPTEDKEYMAPFILENAME);
            byte[] keyMap = new AdvancedEncryptionStandard(LOCALSTATEENCRYPTIONKEY.getBytes(StandardCharsets.UTF_8))
                    .decrypt(encryptedKeyMap);
            fileNameKeyPairs = (HashMap<String, String>)ByteUtils.deserialize(keyMap);
        } catch (Exception ex) {
            fileNameKeyPairs = new HashMap<>();
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
