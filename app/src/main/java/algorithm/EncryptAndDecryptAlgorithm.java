package algorithm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptAndDecryptAlgorithm {
    private static final String FORMAT_ENCRYPTION = ".mcl";
    private static final String AES_ALGORITHM = "AES";
    private static final String PBE_WITH_MD5_AND_DES_ALGORITHM = "PBEWithMD5AndDES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    private static byte[] weight = {(byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99};
    private static final String EXTERNAL_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String DIRECTORY_NAME = "/MCL/";

    /**
     * Encrypt a string with AES_ALGORITHM algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(AES_ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT);
    }

    public static void encryptWithImage(String password, String uri, String fileName) throws Exception {
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES_ALGORITHM);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(weight, 20);

        createNewDirectory(EXTERNAL_FILE_PATH, DIRECTORY_NAME);
        FileInputStream file = new FileInputStream(uri);
        FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY_NAME + fileName + FORMAT_ENCRYPTION);

        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Cipher pbeCipher = Cipher.getInstance(PBE_WITH_MD5_AND_DES_ALGORITHM);
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

    public static void encryptWithImageAES(String password, String uri, String fileName) throws Exception {
        byte[] key = (password).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        createNewDirectory(EXTERNAL_FILE_PATH, DIRECTORY_NAME);
        FileInputStream file = new FileInputStream(uri);
        FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY_NAME + fileName + FORMAT_ENCRYPTION);
        SecretKeySpec secretKeySpec  = new SecretKeySpec(key,AES_ALGORITHM);
        Cipher enc = Cipher.getInstance(AES_ALGORITHM);
        enc.init(Cipher.ENCRYPT_MODE, secretKeySpec );
        CipherOutputStream cos = new CipherOutputStream(output, enc);
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
     * Decrypt a string with AES_ALGORITHM algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(AES_ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.decode(encryptedData, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    public static Bitmap decryptWithImage(String password, String uri, String fileName) throws Exception {

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(weight, 20);
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES_ALGORITHM);
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Cipher c = Cipher.getInstance(PBE_WITH_MD5_AND_DES_ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

        createNewDirectory(EXTERNAL_FILE_PATH, DIRECTORY_NAME);
        FileInputStream file = new FileInputStream(uri);
        FileOutputStream output = new FileOutputStream(EXTERNAL_FILE_PATH + DIRECTORY_NAME + fileName);

        CipherOutputStream cos = new CipherOutputStream(output, c);
        byte[] buf = new byte[1024];
        int read;
        while ((read = file.read(buf)) != -1) {
            cos.write(buf, 0, read);
        }
        file.close();
        output.flush();
        cos.close();

        return BitmapFactory.decodeFile(EXTERNAL_FILE_PATH + DIRECTORY_NAME + fileName);
    }

    public static Bitmap decryptWithImageAES(String password, String uri, String fileName) throws Exception {
        byte[] key = (password).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        createNewDirectory(EXTERNAL_FILE_PATH, DIRECTORY_NAME);
        FileInputStream file = new FileInputStream(uri);
        FileOutputStream output = new FileOutputStream(EXTERNAL_FILE_PATH + DIRECTORY_NAME + fileName);
        SecretKeySpec secretKeySpec  = new SecretKeySpec(key, AES_ALGORITHM);
        Cipher enc = Cipher.getInstance(AES_ALGORITHM);
        enc.init(Cipher.DECRYPT_MODE, secretKeySpec );
        CipherOutputStream cos = new CipherOutputStream(output, enc);
        byte[] buf = new byte[1024];
        int read;
        while ((read = file.read(buf)) != -1) {
            cos.write(buf, 0, read);
        }
        file.close();
        output.flush();
        cos.close();

        return BitmapFactory.decodeFile(EXTERNAL_FILE_PATH + DIRECTORY_NAME + fileName);
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, AES_ALGORITHM);
    }

    private static void createNewDirectory(String path, String directoryName) {
        File file = new File(path + directoryName);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }

}
