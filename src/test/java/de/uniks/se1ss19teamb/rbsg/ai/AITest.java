package de.uniks.se1ss19teamb.rbsg.ai;

import org.junit.Assert;
import org.junit.Test;

public class AITest {
    
    @Test
    public void testInstantiation() {
        AI testInstance = AI.instantiate("", -1);
        Assert.assertNotNull(testInstance);
    }
}
