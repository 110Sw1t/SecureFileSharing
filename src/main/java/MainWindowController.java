/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.ResourceBundle;
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
    private KeyPair keys ; 
    private final String PublicKeyString = "PublicKey";
    private final String PrivateKeyString = "PrivateKey";
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private int lengthBefore;
    private String s;
    private final String GREEN = "#008000";
    private final String RED = "#FF0000";
    private final String WHITE = "#FFF";
    private final String DASHEDLINE = "\n-------------------------------------------------------------------------------------------------------------------------\n";
    private final String FONTBIG = "15";
    private final String FONTSMALL= "11";
    private final String FONTWEIGHTBIG= "700";
    private final String FONTWEIGHTSMALL= "200";
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //buttons Action listener
        ChooseUploadBtn.setOnAction(e->ChooseFile(ChoosedFile));
        AESKeyToEncryptBtn.setOnAction(e->ChooseFile(AESKeyToEncryptTxt));
        PublicKeyToEncryptWithBtn.setOnAction(e->ChooseFile(PublicKeyToEncryptWithTxt));
        EncryptedFileToBeDecBtn.setOnAction(e->ChooseFile(EncryptedFileToBeDecTxt));
        EncryptBtn.setOnAction(e->EncryptAESwithPublic());
        DecryptBtn.setOnAction(e->DecryptFile());
        //listview selection listener
        ListFiles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            System.out.println("enteredddd......");
            
            FileToDecTxt.setText(newValue);
            logAppendTex("Choosed File From List:\n "  , RED , FONTBIG, FONTWEIGHTBIG);
            logAppendTex(newValue +"\n"  , WHITE , FONTSMALL, FONTWEIGHTSMALL);

                
            }
        });
        
        
        
        try {
            // TODO
            keys = RSA.buildKeyPair();
            publicKey = keys.getPublic();
            privateKey= keys.getPrivate();
            logAppendTex("Public Key and Private Key Created Successfully \n------------------------------------------------\n", GREEN , FONTBIG, FONTWEIGHTBIG);
            logAppendTex("your public key is: \n"+ keys.getPublic().toString(), WHITE , FONTSMALL, FONTSMALL);
            SaveKey(publicKey, PublicKeyString);
            SaveKey(privateKey, PrivateKeyString);
            logAppendTex("\nyour public and private Keys saved successfully in the project folder"+DASHEDLINE, GREEN , FONTBIG, FONTWEIGHTBIG);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void SaveKey(Key k, String kt) {
        try {
            String fileName = kt;
            FileOutputStream fos;
            fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            if (kt.equals(PrivateKeyString)) {
                oos.writeObject((PrivateKey) k);
            } else {
                oos.writeObject((PublicKey) k);
            }

            oos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readKey(Key k, String kt) {
        try {
            String fileName = kt;
            FileInputStream fin = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            if (kt.equals(PrivateKeyString)) {
                privateKey = (PrivateKey) ois.readObject();
            } else {
                publicKey = (PublicKey) ois.readObject();
            }
            ois.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {

        }

    }

    void setDriveHandle(DriveHandle handle) {
        this.handle = handle;
    }

    public void ChooseFile(JFXTextField tf) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        if (file != null) {
            tf.setText(file.getAbsolutePath());
            logAppendTex(tf.getPromptText()+":\n "  , RED , FONTBIG, FONTWEIGHTBIG);
            logAppendTex(file.getAbsolutePath() +"\n"  , WHITE , FONTSMALL, FONTWEIGHTSMALL);

        }
    }

    public void ChooseDirectory() {

        logAppendTex("Listing Files please Wait....\n", RED , FONTBIG, FONTWEIGHTBIG);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ListFiles.getItems().clear();
                List<com.google.api.services.drive.model.File> fList = handle.getFilesList();

        for (com.google.api.services.drive.model.File f : fList) {

            ListFiles.getItems().add(fList.toString());
        }
                logAppendTex("File Listed Successfully!\n", GREEN , FONTBIG, FONTWEIGHTBIG);

            }
        });

//        new Thread(new Runnable(){
//            public void run(){
//                ListFiles.getItems().clear();
//                List<com.google.api.services.drive.model.File> fList = handle.getFilesList();
//
//        for (com.google.api.services.drive.model.File f : fList) {
//
//            ListFiles.getItems().add(fList);
//        }
//            }
//            
//        }).start();
    }

    public void ClearList() {
        ListFiles.getItems().clear();
    }

    public void UploadFile() {
        //TO DO upload file from this function
        String FileToUpload = ChoosedFile.getText().toString();
    }
    
    public void logAppendTex(String s , String Color  , String FontSize , String FontWeight)
    {
        lengthBefore = log.getText().length();
        
        log.appendText(s);
        
        log.setStyle(lengthBefore,(lengthBefore+s.length()),
                "-fx-fill:" + Color + "; -fx-font-size:" + FontSize+ "px; -fx-font-weight:"+ FontWeight +";");
        log.requestFollowCaret();
    }

    private void EncryptAESwithPublic() {
        String AESFilePath = AESKeyToEncryptTxt.getText();
        String PublicKeyFilePath = PublicKeyToEncryptWithTxt.getText();
        if(AESFilePath.equals("")||PublicKeyFilePath.equals(""))
        {
            logAppendTex("Please Choose The AES Key File and Public Key File to Encrypt with\n", RED , FONTBIG, FONTWEIGHTBIG);
        }
        else
        {
            logAppendTex("File Encrypted Successfully Please send it to the user who wants it\n", GREEN , FONTBIG, FONTWEIGHTBIG);

        }
    }

    private void DecryptFile() {
        String AESKey = EncryptedFileToBeDecTxt.getText();
        String FilePath = FileToDecTxt.getText();
        if(AESKey.equals("")||FilePath.equals(""))
        {
            logAppendTex("Please Choose The AES Key Encrypted File and the file you want to decrypt\n", RED , FONTBIG, FONTWEIGHTBIG);
        }
        else
        {
            logAppendTex("File Decrypted Successfully and You will find it on the project folder\n", GREEN , FONTBIG, FONTWEIGHTBIG);

        }
    }

}
