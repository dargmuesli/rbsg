package de.uniks.se1ss19teamb.rbsg.crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CipherController{

    //This string needs to be related with data
    public void encryptMessage(String msg, String filename){
        Charset utf8 = StandardCharsets.UTF_8;

        try {
            //Turns the desired message into a byte Array
            byte[] message = msg.getBytes(utf8);

            //2.Encrypts the desired message
            byte[] secret = CipherUtils.encrypt(CipherConstant.publicKey,message);

            //4.Stores the secret message into text file
            FileWriter fw = new FileWriter(filename);
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
        Charset utf8 = StandardCharsets.UTF_8;

        //1.Reads the encrypted message
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            StringBuilder stringBuilder = new StringBuilder();
            String line ;

            //2.Constructs the encrypted message
            while((line = br.readLine()) != null){
                stringBuilder.append(line);
            }
            String recoveredSecret = stringBuilder.toString();

            byte[] recSecret = Base64.decodeBase64(recoveredSecret);

            //4.Decrypts the message
            byte[] recovered_message = CipherUtils.decrypt(CipherConstant.privateKey,recSecret);
            decrypted_message = (new String(recovered_message, utf8));
            br.close();

        } catch (IOException | NoSuchAlgorithmException |InvalidKeyException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException e ) {
            e.printStackTrace();
        }
        return decrypted_message;
    }

}
