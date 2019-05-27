package de.uniks.se1ss19teamb.rbsg.crypto;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CryptoTest {
    private String dataPath = "src/main/resources/de/uniks/se1ss19teamb/rbsg/data.txt";
    private Charset utf8 = StandardCharsets.UTF_8;

    @org.junit.Test
    public void testEncryption() throws IOException {
        FileWriter fileR = new FileWriter(dataPath);
        File file = new File(dataPath);
        
        String encrypted_message ;
        String msg = "gå til helvete!!!";
        CipherController cip = new CipherController();
        cip.encryptMessage(msg, dataPath);

        //1.Reads the encrypted message
        try {
            FileReader fr = new FileReader(dataPath);

            //2.Constructs the encrypted message
            int t;
            char c;
            StringBuilder recoveredSecret = new StringBuilder();

            while ((t = fr.read()) != -1) {
                c = (char) t;
                recoveredSecret.append(c);
            }

            encrypted_message = encryptReturn(recoveredSecret.toString());
            Assert.assertEquals(encrypted_message, msg, "gå til helvete!!!");
            Assert.assertNotEquals(encrypted_message, msg);
            
            if (!file.delete()) {
                throw new IOException("Could not delete file!");
            }

            fileR.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testDecryption() throws IOException {
        FileWriter fileR = new FileWriter(dataPath);
        File file = new File(dataPath);

        String msg = "gå til helvete!!!";
        CipherController cip = new CipherController();
        cip.encryptMessage(msg, dataPath);
        String decrypted_message = cip.decryptMessage(dataPath);

        //1.Reads the encrypted message
        try {
            FileReader fr = new FileReader(dataPath);

            //2.Constructs the encrypted message
            int t;
            char c;
            StringBuilder recoveredSecret = new StringBuilder();

            while ((t = fr.read()) != -1) {
                c = (char) t;
                recoveredSecret.append(c);
            }

            String encrypted_message = encryptReturn(recoveredSecret.toString());

            Assert.assertNotEquals(encrypted_message, msg);
            Assert.assertEquals(decrypted_message, msg);

            if (!file.delete()) {
                throw new IOException("Could not delete file!");
            }

            fileR.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String encryptReturn (String m) {
        byte[] recSecret = Base64.decodeBase64(m);
        return new String(recSecret, utf8);
    }
}
