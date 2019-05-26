package de.uniks.se1ss19teamb.rbsg.crypto;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Test {
    String dataPath = "src/main/resources/de/uniks/se1ss19teamb/rbsg/data.txt";
   Charset utf8 = StandardCharsets.UTF_8;

    @org.junit.Test
    public void testEncryption() {

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
            String recoveredSecret = "";

            while ((t = fr.read()) != -1) {
                c = (char) t;
                recoveredSecret += c;
            }

            encrypted_message = encryptReturn(recoveredSecret);
            System.out.println(encrypted_message);
            Assert.assertEquals(encrypted_message, msg, "gå til helvete!!!");
            Assert.assertNotEquals(encrypted_message, msg);
            PrintWriter pw = new PrintWriter(dataPath);
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testDecryption(){

        String encrypted_message ;
        String decrypted_message ;
        String msg = "gå til helvete!!!";
        CipherController cip = new CipherController();
        cip.encryptMessage(msg, dataPath);
        decrypted_message = cip.decryptMessage(dataPath);

        //1.Reads the encrypted message
        try {
            FileReader fr = new FileReader(dataPath);

            //2.Constructs the encrypted message
            int t;
            char c;
            String recoveredSecret = "";

            while ((t = fr.read()) != -1) {
                c = (char) t;
                recoveredSecret += c;
            }

            encrypted_message = encryptReturn(recoveredSecret);
            System.out.println(encrypted_message);

            Assert.assertNotEquals(encrypted_message, msg);

            System.out.println(decrypted_message);
            Assert.assertEquals(decrypted_message, msg);
            PrintWriter pw = new PrintWriter(dataPath);
            pw.close();;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String encryptReturn (String m) throws FileNotFoundException {
        byte[] recSecret = Base64.decodeBase64(m);
        return new String(recSecret, utf8);
    }
}
