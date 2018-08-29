
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
import java.security.SecureRandom;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Farida Abouish
 */
public class AES {
    
    
	private static final String key = "aesEncryptionKey";
private static final String initVector = "encryptionIntVec";

public  String encrypt(String value) {
	try {
		IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		byte[] encrypted = cipher.doFinal(value.getBytes());
		return Base64.encodeBase64String(encrypted);
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	return null;
}
public  String decrypt(String encrypted) {
	try {
		IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

		return new String(original);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	return null;
}
}


//    private static final String TOKEN = "passwd";
//	private String salt;
//	private int pwdIterations = 65536;
//	private int keySize = 256;
//	private byte[] ivBytes;
//	private String keyAlgorithm = "AES";
//	private String encryptAlgorithm = "AES/CBC/PKCS5Padding";
//	private String secretKeyFactoryAlgorithm = "PBKDF2WithHmacSHA1";
//        Cipher cipher;
//
//    public AES() {
//        this.salt = getSalt();
//    }
//	
//	
//	
//	private String getSalt(){
//		SecureRandom random = new SecureRandom();
//		byte bytes[] = new byte[20];
//		random.nextBytes(bytes);
//		String text = new String(bytes);
//		return text;
//	}
//        public Object[] GenerateKey() throws Exception
//            {
//            //generate key
//		byte[] saltBytes = salt.getBytes("UTF-8");
//		
//		SecretKeyFactory skf = SecretKeyFactory.getInstance(this.secretKeyFactoryAlgorithm);
//		PBEKeySpec spec = new PBEKeySpec(TOKEN.toCharArray(), saltBytes, this.pwdIterations, this.keySize);
//		SecretKey secretKey = skf.generateSecret(spec);
//		SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), keyAlgorithm);
//                Object[] ob = new Object[2];
//                //AES initialization
//                   cipher  = Cipher.getInstance(encryptAlgorithm);
//		cipher.init(Cipher.ENCRYPT_MODE, key);
//                this.ivBytes = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
//                ob[0]= key;
//                ob[1] = ivBytes;
//                
//                
//                return ob;
//        }
//	
//	/**
//	 * 
//	 * @param plainText
//	 * @return encrypted text
//	 * @throws Exception
//	 */
//	public byte [] encyrpt(byte [] plainText, SecretKeySpec key) throws Exception{
//		
//		
//		
//		
//		//generate IV
////		byte[] encryptedText = cipher.doFinal(plainText.getBytes("UTF-8"));
//		byte[] encryptedText = cipher.doFinal(plainText);
//
//                return encryptedText;
//	}
//	
//	/**
//	 * 
//	 * @param encryptText
//	 * @return decrypted text
//	 * @throws Exception
//	 */
//	public byte[] decrypt(byte[] encryptText, SecretKeySpec key) throws Exception {
////		byte[] encryptTextBytes = new Base64().decode(encryptText);
//		
//		
//		
//		//decrypt the message
//		Cipher cipher = Cipher.getInstance(encryptAlgorithm);
//		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
//		
//		byte[] decyrptTextBytes = null;
//		try {
//			decyrptTextBytes = cipher.doFinal(encryptText);
//		} catch (IllegalBlockSizeException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} catch (BadPaddingException e) {
//			e.printStackTrace();
//		}
////		String text = new String(decyrptTextBytes);
//		return decyrptTextBytes;
//	}
//    
//    public static Object[] keyGenerate() throws NoSuchAlgorithmException {
//        KeyGenerator key = KeyGenerator.getInstance("AES");
//        key.init(128);
//        SecretKey finalKey = key.generateKey();
//        byte[] iv = new byte[128/8];
//        new Random().nextBytes(iv);
//        IvParameterSpec ivspec = new IvParameterSpec(iv);
//        Object[] ob = new Object[2];
//        ob[0] = finalKey;
//        ob[1] = ivspec;
//
//        return ob;
//    }
//
////    public static byte[] encrypt(SecretKey key, File file,IvParameterSpec ivspec) throws Exception {
////        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
////        OutputStream out = new FileOutputStream(file);
////       
////        ci.init(Cipher.ENCRYPT_MODE, key, ivspec);
////        try (FileInputStream in = new FileInputStream(file)) {
////            processFile(ci, in, out);
////}
////        return cipherText;
////    }
//
////    public static byte[] decrypt(SecretKey key, byte[] cipherText,IvParameterSpec ivspec) throws Exception {
////        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
////        ci.init(Cipher.DECRYPT_MODE, key,ivspec);
////        
////        byte[] plainText = ci.doFinal(cipherText);
////        return plainText;
////    }
//    
////    public static byte[] encrypt(SecretKey key, byte[] initVector, byte [] file) {
////        try {
////            IvParameterSpec iv = new IvParameterSpec(initVector);
////            //SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
////
////            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
////            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
////
////            byte[] encrypted = cipher.doFinal(file);
////            return encrypted;
////        } catch (Exception ex) {
////            ex.printStackTrace();
////        }
////
////        return null;
////    }
////
////    public static byte[] decrypt(SecretKey key, byte []  initVector, byte [] encrypted) {
////        try {
////            IvParameterSpec iv = new IvParameterSpec(initVector);
////            //SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
////
////            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
////            cipher.init(Cipher.DECRYPT_MODE, key, iv);
////
////            byte[] original = cipher.doFinal(encrypted);
////
////            return original;
////        } catch (Exception ex) {
////            ex.printStackTrace();
////        }
////
////        return null;
////    }
//
//
//}
