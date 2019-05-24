import de.uniks.se1ss19teamb.rbsg.crypto.CipherController;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;

import java.io.FileReader;
import java.io.IOException;

public class Test {

    @org.junit.Test
    public void testEncryption() {

        String encrypted_message = "";
        String msg = "gå til helvete!!!";
        CipherController cip = new CipherController();
        cip.encryptMessage(msg);

        //1.Reads the encrypted message
        try {
            FileReader fr = new FileReader("src/main/resources/de/uniks/se1ss19teamb/rbsg/data.txt");

            //2.Constructs the encrypted message
            int t;
            char c;
            String recoveredSecret = "";

            while ((t = fr.read()) != -1) {
                c = (char) t;
                recoveredSecret += c;
            }

            byte[] recSecret = Base64.decodeBase64(recoveredSecret);
            encrypted_message = (new String(recSecret, "UTF8"));
            System.out.println(encrypted_message);
            Assert.assertNotEquals(encrypted_message, msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testDecryption(){

        String encrypted_message = "";
        String decrypted_message = "";
        String msg = "gå til helvete!!!";
        CipherController cip = new CipherController();
        cip.encryptMessage(msg);
        decrypted_message = cip.decryptMessage();

        //1.Reads the encrypted message
        try {
            FileReader fr = new FileReader("src/main/resources/de/uniks/se1ss19teamb/rbsg/data.txt");

            //2.Constructs the encrypted message
            int t;
            char c;
            String recoveredSecret = "";

            while ((t = fr.read()) != -1) {
                c = (char) t;
                recoveredSecret += c;
            }

            byte[] recSecret = Base64.decodeBase64(recoveredSecret);
            encrypted_message = (new String(recSecret, "UTF8"));
            System.out.println(encrypted_message);

            Assert.assertNotEquals(encrypted_message, msg);

            System.out.println(decrypted_message);
            Assert.assertEquals(decrypted_message,msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}