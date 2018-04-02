package algorithm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESAlgorithm {
    private static final String FORMAT_ENCRYPTION = ".mcl";
    private static final String ALGO = "AES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    private static byte[] weight = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT);
    }

    public static void encryptWithImage(String password, String uri, String fileName) throws Exception {
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(weight, 20);

        FileInputStream file  = new FileInputStream(uri);
        FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName + FORMAT_ENCRYPTION);

        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        CipherOutputStream cos = new CipherOutputStream(output, pbeCipher);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
       // output.write(bytes.toByteArray());
        byte[] buf = new byte[1024];
        int read;
        while((read=file.read(buf))!=-1){
            cos.write(buf,0,read);
        }
        file.close();
        output.flush();
        cos.close();

        /*Key key = generateKey();
        File file = new File(uri);
        FileInputStream fis = new FileInputStream(file);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, pbeKey);
        CipherInputStream cis = new CipherInputStream(fis, c);
        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName + FORMAT_ENCRYPTION);
        Log.d("AESAlgorithm", "file: " + file);
        Log.d("AESAlgorithm", "file Name: " + file.getName());
        Log.d("AESAlgorithm", "path: " + Environment.getExternalStorageDirectory().getAbsolutePath());
        byte[] encVal = c.doFinal(password.getBytes());
        byte[] encOut = AESAlgorithm.encrypt(password).getBytes();
        Log.wtf("AESAlgorithm","encOut: "+Arrays.toString(encOut));
        int i = cis.read(encOut);
        while (i != -1) {
            fos.write(encOut, 0, i);
            i = cis.read(encOut);
        }
        fos.close();*/
    }


    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.decode(encryptedData, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    public static Bitmap decryptWithImage(String password, String uri, String fileName) throws Exception {


        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        // Set up other parameters to be used by the password-based
        // encryption.
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(weight, 20);
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Cipher c = Cipher.getInstance("PBEWithMD5AndDES");
        c.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

        FileInputStream file = new FileInputStream(uri);
        FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName);

        CipherOutputStream cos = new CipherOutputStream(output, c);
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int read;
        while((read=file.read(buf))!=-1){
            cos.write(buf,0,read);
        }
        file.close();
        output.flush();
        cos.close();




/*        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(ALGO);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Key key = generateKey();
        File file = new File(uri);
        FileInputStream fis = new FileInputStream(file);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, pbeKey);
        CipherInputStream cis = new CipherInputStream(fis, c);
        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName);
        byte[] decordedValue = Base64.decode(AESAlgorithm.encrypt(password), Base64.DEFAULT);
        Log.wtf("AESAlgorithm","decordedValue: "+Arrays.toString(decordedValue));
        byte[] decValue = c.doFinal(decordedValue);
        int i = cis.read(decValue);
        while (i != -1) {
            fos.write(decValue, 0, i);
            i = cis.read(decValue);
        }
        fos.close();
        Log.d("AESAlgorithm", "Unlock: " + Arrays.toString(decValue));
        return new String(decValue);*/
        return BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName);
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
    }private static Key generateKey(byte[] password) throws Exception {
        return new SecretKeySpec(password, ALGO);
    }


}
