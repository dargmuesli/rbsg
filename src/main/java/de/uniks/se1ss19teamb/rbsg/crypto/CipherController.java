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
            //Turned the desired message into a byte Array
            byte[]  message = messagetobeSaved.getBytes("UTF8");

            //2.Encrypt the desired message
            byte[] secret = CipherUtils.encrypt(CipherConstant.publicKey,message);
            System.out.print(new String(secret,"UTF8"));

            //3 Turn the encrypted message into Array
            char[] secretchars = new char[secret.length];

            for(int i = 0;i<secret.length;i++){
                secretchars[i] = (char) secret[i];
            }


            //4.Store the secret message into text file for now
            //TODO:filelocation need to be mentioned
            FileWriter fw = new FileWriter("");
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
    public void decryptMessage(){
        System.out.println("decrypt message : ");

        //1.Read the encrypted message
        //here files location need to be given
        try {
            //TODO:filelocation need be mentioned
            FileReader fr = new FileReader("");
            BufferedReader br = new BufferedReader(fr);

            //2.Construct the encrypted message
            int t;
            char c;
            String recoveredSecret = "";
            while((t = fr.read()) != -1){
                c=  (char) t;
                recoveredSecret += c;
            }

            byte[] recSecret = Base64.decodeBase64(recoveredSecret);

            //4.Decrypt the message and print it on the users screen
            byte[] recovered_message = CipherUtils.decrypt(CipherConstant.privateKey,recSecret);
            System.out.println(new String(recovered_message,"UTF8"));

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
    }

}
