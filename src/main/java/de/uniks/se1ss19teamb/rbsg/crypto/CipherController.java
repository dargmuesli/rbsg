package de.uniks.se1ss19teamb.rbsg.crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CipherController{

    //This string needs to be related with data
    public void encryptMessage(String msg,String filename){
        String messagetobeSaved = msg;

        try{
            //Turns the desired message into a byte Array
            byte[] message = messagetobeSaved.getBytes("UTF8");

            //2.Encrypts the desired message
            byte[] secret = CipherUtils.encrypt(CipherConstant.publicKey,message);

            //3 Turns the encrypted message into Array
            char[] secretchars = new char[secret.length];

            for(int i = 0;i<secret.length;i++){
                secretchars[i] = (char) secret[i];
            }

            //4.Stores the secret message into text file
            FileWriter fw = writeFile(filename);
            fw.write(Base64.encodeBase64String(secret));
            fw.close();

        } catch ( NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }

    //decrypt the message
    public String decryptMessage(String filename){

        String decrypted_message = "" ;

        //1.Reads the encrypted message
        try {
            BufferedReader br = new BufferedReader(readFile(filename));
            FileReader fr = readFile(filename);

            //2.Constructs the encrypted message
            int t;
            char c;
            String recoveredSecret = "";

            while((t = fr.read()) != -1){
                c =  (char) t;
                recoveredSecret += c;
            }

            byte[] recSecret = Base64.decodeBase64(recoveredSecret);

            //4.Decrypts the message
            byte[] recovered_message = CipherUtils.decrypt(CipherConstant.privateKey,recSecret);
            decrypted_message =(new String(recovered_message,"UTF8"));
            //PrintWriter pw = new PrintWriter("src/main/java/de/uniks/se1ss19teamb/rbsg/crypto/Dummy.der");
            //pw.close();
            br.close();
            fr.close();

        } catch (IOException | NoSuchAlgorithmException |InvalidKeyException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException e ) {
            e.printStackTrace();
        }
        return decrypted_message;
    }

    public static FileReader readFile(String filename) throws FileNotFoundException {
        FileReader fr = new FileReader(filename);
        return fr;
    }

    public static FileWriter writeFile(String filename) throws IOException {
        FileWriter wr = new FileWriter(filename);
        return wr;
    }
}
