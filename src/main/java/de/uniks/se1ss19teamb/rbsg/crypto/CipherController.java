package de.uniks.se1ss19teamb.rbsg.crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CipherController {


    //This string needs to be related with data
    public void encryptMessage(String msg){
        String messagetobeSaved = msg;

        try{
            //Turns the desired message into a byte Array
            byte[]  message = messagetobeSaved.getBytes("UTF8");

            //2.Encrypts the desired message
            byte[] secret = CipherUtils.encrypt(CipherConstant.publicKey,message);

            //3 Turns the encrypted message into Array
            char[] secretchars = new char[secret.length];

            for(int i = 0;i<secret.length;i++){
                secretchars[i] = (char) secret[i];
            }

            //4.Stores the secret message into text file
            FileWriter fw = new FileWriter("src/main/resources/de/uniks/se1ss19teamb/rbsg/secrets.txt");
            fw.write(Base64.encodeBase64String(secret));
            fw.close();



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //decrypt the message
    public String decryptMessage(){

        String decrypted_message = "NOT_DECRYPTED!!!!" ;

        //1.Reads the encrypted message
        try {
            FileReader fr = new FileReader("src/main/resources/de/uniks/se1ss19teamb/rbsg/secrets.txt");
            BufferedReader br = new BufferedReader(fr);

            //2.Constructs the encrypted message
            int t;
            char c;
            String recoveredSecret = "";
            while((t = fr.read()) != -1){
                c=  (char) t;
                recoveredSecret += c;
            }

            byte[] recSecret = Base64.decodeBase64(recoveredSecret);

            //4.Decrypts the message
            byte[] recovered_message = CipherUtils.decrypt(CipherConstant.privateKey,recSecret);

            decrypted_message =(new String(recovered_message,"UTF8"));
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decrypted_message;

    }

}
