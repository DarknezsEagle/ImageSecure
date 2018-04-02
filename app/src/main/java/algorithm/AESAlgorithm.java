package algorithm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESAlgorithm {
    private static final String FORMAT_ENCRYPTION = ".mcl";
    private static final String AES = "AES";
    private static final String AES_ALGORITHM = "PBEWithMD5AndDES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    private static byte[] weight = {(byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99};

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT);
    }

    public static void encryptWithImage(String password, String uri, String fileName) throws Exception {
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(AES_ALGORITHM);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(weight, 20);

        FileInputStream file = new FileInputStream(uri);
        FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName + FORMAT_ENCRYPTION);

        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Cipher pbeCipher = Cipher.getInstance(AES_ALGORITHM);
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        CipherOutputStream cos = new CipherOutputStream(output, pbeCipher);
        byte[] buf = new byte[1024];
        int read;
        while ((read = file.read(buf)) != -1) {
            cos.write(buf, 0, read);
        }
        file.close();
        output.flush();
        cos.close();

    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.decode(encryptedData, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    public static Bitmap decryptWithImage(String password, String uri, String fileName) throws Exception {

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(weight, 20);
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(AES_ALGORITHM);
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Cipher c = Cipher.getInstance(AES_ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

        FileInputStream file = new FileInputStream(uri);
        FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName);

        CipherOutputStream cos = new CipherOutputStream(output, c);
        byte[] buf = new byte[1024];
        int read;
        while ((read = file.read(buf)) != -1) {
            cos.write(buf, 0, read);
        }
        file.close();
        output.flush();
        cos.close();

        return BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName);
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, AES);
    }

}
