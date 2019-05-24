package de.uniks.se1ss19teamb.rbsg.crypto;

import org.apache.commons.net.ftp.FTPClient;
import java.io.*;

public class CipherConnect {
    public static void connect() throws Exception {
        String string = "src/main/java/de/uniks/se1ss19teamb/rbsg/crypto/Dummy.der";
        String download1 = "Schreibtisch/private.der";
        FTPClient client = new FTPClient();
        try (OutputStream os = new FileOutputStream(string)) {
            client.connect("10.10.155.88");
            client.login("kip", "ilmysk911");

            // Download file from FTP server.
            boolean status = client.retrieveFile(download1, os);
            System.out.println("status = " + status);
            System.out.println("reply  = " + client.getReplyString());
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
