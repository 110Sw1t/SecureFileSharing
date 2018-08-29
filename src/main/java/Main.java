
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.SwingUtilities;

public class Main extends Application {

    Parent root;

    /**
     * @param args the command line arguments
     *
     *
     * /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */

//    public static void main(String... args) throws Exception {
//        
////        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
////
////        int returnValue = jfc.showOpenDialog(null);
////        // int returnValue = jfc.showSaveDialog(null);
////
////        if (returnValue == JFileChooser.APPROVE_OPTION) {
////            File selectedFile = jfc.getSelectedFile();
////            System.out.println(handle.uploadFile(selectedFile));;
////        }
//        //DriveHandle.printFiles(handle.getFilesList());
//        final JFXPanel fxPanel = new JFXPanel();
////        final CountDownLatch latch = new CountDownLatch(1);
////        SwingUtilities.invokeLater(new Runnable() {
////    public void run() {
////        new JFXPanel(); // initializes JavaFX environment
////        latch.countDown();
////    }
////});
////latch.await();
//        GUIMain main = new GUIMain();
//        main.openGUI(handle);
//    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        DriveHandle handle = DriveHandle.getDriveHandle();

//        KeyGenerator key = KeyGenerator.getInstance("AES");
//        key.init(128);
//        SecretKey finalKey = key.generateKey();
//        System.out.println("finalKey = " + finalKey.toString());
//        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
////
//        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

//        if (returnValue == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = jfc.getSelectedFile();
//            String name = selectedFile.getName();
//            byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
//            new FileOutputStream("C:\\Users\\fadia\\Desktop\\notes\\"+name).write(fileContent);
//            System.out.println(handle.uploadFile(selectedFile));;
//        }
//        DriveHandle.printFiles(handle.getFilesList());
//          System.out.println(handle.getUserInfo());
//        handle.downloadFile("1RRLjz4YBw9BIPAwUMyXe5hgMa7Xkq7U0");
//        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
////
////        int returnValue = jfc.showOpenDialog(null);
////        // int returnValue = jfc.showSaveDialog(null);
////
////        if (returnValue == JFileChooser.APPROVE_OPTION) {
////            File selectedFile = jfc.getSelectedFile();
////            System.out.println(handle.uploadFile(selectedFile));;
////        }
////        DriveHandle.printFiles(handle.getFilesList());
////        System.out.println(handle.getUserInfo());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getClassLoader().getResource("fxml\\MainWindow.fxml"));
        MainWindowController controller = new MainWindowController();
        controller.setDriveHandle(handle);
        loader.setController(controller);
        root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("imgs/icon.png")));
        stage.setTitle("Cloud File Encryptor");
        String css = Main.class.getClassLoader().getResource("styles\\mainwindow.css").toExternalForm();
        stage.getScene().getStylesheets().add(css);
//        stage.setHeight(440);
//        stage.setWidth(770);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 4);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> closeWindow());
        stage.show();
    }

    private void closeWindow() {
        System.exit(0);
    }

}
