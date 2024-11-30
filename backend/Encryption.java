package backend;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class Encryption {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;
    

    public static SecretKey genSecretKey()throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }


    public static String encrypt(String password, SecretKey secretKey)throws Exception{
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
 
    }

    public static String decrypt(String cipherText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText); 
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    public static String encodeSecretKey(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());  // Convert key to string
    }

    public static SecretKey decodeSecretKey(String base64EncodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(base64EncodedKey);  // Decode from String
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);  // Create SecretKey from bytes
    }

}
