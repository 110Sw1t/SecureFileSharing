
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fadia
 */
public class FilesUtils {
    public static Object readObjectFromFile(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            ois.close();
            System.out.println("The Object  was succesfully written to a file");
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static void writeObjectToFile(Object serObj, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(serObj);
            oos.close();
            System.out.println("The Object  was succesfully read from a file");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
