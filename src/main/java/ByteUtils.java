
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fadia
 */
public class ByteUtils {
    
    public static byte[] byteArrayMerge(byte[] left, byte[] right){
        byte[] combined = new byte[left.length+right.length];
        System.arraycopy(left, 0, combined, 0, left.length);
        System.arraycopy(right, 0, combined, left.length, right.length);
        return combined;
    }
    public static byte[][] byteArraySplit(byte[] arr, int splitFromIndex){
        byte[][] result = new byte[2][];
        result[0] = new byte[splitFromIndex];
        result[1] = new byte[arr.length-splitFromIndex];
        System.arraycopy(arr, 0, result[0], 0, result[0].length);
        System.arraycopy(arr, splitFromIndex, result[1], 0, result[1].length);
        return result;
    }
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
