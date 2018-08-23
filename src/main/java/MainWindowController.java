/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

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
    private JFXButton ChooseBtn;
    @FXML
    private JFXButton ListFilesBtn;
    @FXML
    private JFXButton UploadBtn;
    private Thread ListFilesThread;
    private DriveHandle handle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("lerpr");

    }

    void setDriveHandle(DriveHandle handle) {
        this.handle = handle;
        System.out.println("handled");
    }

    public void ChooseFile() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        if (file != null) {
            ChoosedFile.setText(file.getAbsolutePath());
        }
    }

    public void ChooseDirectory() {

        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ListFiles.getItems().clear();
                List<com.google.api.services.drive.model.File> fList = handle.getFilesList();

        for (com.google.api.services.drive.model.File f : fList) {

            ListFiles.getItems().add(fList);
        }
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

}
