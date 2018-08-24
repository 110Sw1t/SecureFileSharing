
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class Main {

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

    public static void main(String... args) throws Exception {
        DriveHandle handle = DriveHandle.getDriveHandle();
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
        DriveHandle.printFiles(handle.getFilesList());

//        handle.downloadFile("1RRLjz4YBw9BIPAwUMyXe5hgMa7Xkq7U0");
    }
}
