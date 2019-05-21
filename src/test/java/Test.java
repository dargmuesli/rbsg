import de.uniks.se1ss19teamb.rbsg.crypto.CipherController;
import org.junit.Assert;

public class Test {

    @org.junit.Test
    public void Test(){

        String msg = "gå til helvete!!!";
        CipherController cip = new CipherController();
        cip.encryptMessage(msg);

        String returnedmsg = cip.decryptMessage();

        Assert.assertEquals("gå til helvete!!!",returnedmsg);

        System.out.println();
        System.out.println(""+returnedmsg);


    }
}
