/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : LocalEncryptor.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */
package dip.xbasov00.util;

import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Singleton;

import org.apache.commons.codec.binary.Base64;

@Singleton
public class LocalEncryptor {

    private static String ALGORITHM = "AES";

    private static int PASSWORD_LENGTH = 16;

    private static char FILLING_CHAR = 'x';

    private static Key generateKey(String password) throws Exception {

        // Adjust password to string of length 16 chars.
        int endIndex = (password.length() < 16) ? (password.length() - 1) : (PASSWORD_LENGTH-1);
        String key = password.substring(0, endIndex);
        while(key.length() < PASSWORD_LENGTH) key += FILLING_CHAR;

        return new SecretKeySpec(password.getBytes(), ALGORITHM);
    }


    public static String encrypt(String password, String plaintext) throws InvalidKeyException, Exception {

        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, generateKey(password));

        byte[] encValue = c.doFinal(plaintext.getBytes());

        byte[] decodedEncValue = new Base64().encode(encValue);

        return new String(decodedEncValue);

    }

    public static String decrypt(String password, String encryptedText) throws InvalidKeyException, Exception {

        Cipher c = Cipher.getInstance(ALGORITHM);

        c.init(Cipher.DECRYPT_MODE, generateKey(password));

        byte[] decordedEncValue = new Base64().decode(encryptedText.getBytes());

        byte[] enctVal = c.doFinal(decordedEncValue);

        return new String(enctVal);
    }
}
