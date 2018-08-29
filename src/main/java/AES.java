
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Farida Abouish
 */
public class AES {

    
    static private void processFile(Cipher ci,InputStream in,OutputStream out)
    throws Exception
        {
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = ci.update(ibuf, 0, len);
                if ( obuf != null ) out.write(obuf);
            }
            byte[] obuf = ci.doFinal();
            if ( obuf != null ) out.write(obuf);
        }
    public static Object[] keyGenerate() throws NoSuchAlgorithmException {
        KeyGenerator key = KeyGenerator.getInstance("AES");
        key.init(128);
        SecretKey finalKey = key.generateKey();
        byte[] iv = new byte[128/8];
        new Random().nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        Object[] ob = new Object[2];
        ob[0] = finalKey;
        ob[1] = ivspec;
        return ob;
    }

    public static byte[] encrypt(SecretKey key, File file,IvParameterSpec ivspec) throws Exception {
        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
        OutputStream out = new FileOutputStream(file);
       
        ci.init(Cipher.ENCRYPT_MODE, key, ivspec);
        try (FileInputStream in = new FileInputStream(file)) {
            processFile(ci, in, out);
}
        return cipherText;
    }

    public static byte[] decrypt(SecretKey key, byte[] cipherText,IvParameterSpec ivspec) throws Exception {
        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ci.init(Cipher.DECRYPT_MODE, key,ivspec);
        
        byte[] plainText = ci.doFinal(cipherText);
        return plainText;
    }

}
