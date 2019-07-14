package de.uniks.se1ss19teamb.rbsg.model;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArmyTest {
    private String id = "123";
    private String name = "myName";
    private List<Unit> units = Arrays.asList(new Unit("123"), new Unit("456"));
    private String newId = "987";
    private String newName = "myNewName";
    private List<Unit> newUnits = Arrays.asList(new Unit("456"), new Unit("789"));
    private Army army;

    @Before
    public void prepareTests() {
        army = new Army(id, name, units);
    }

    @Test
    public void getIdTest() {
        Assert.assertEquals(id, army.getId());
    }

    @Test
    public void setIdTest() {
        army.setId(newId);
        Assert.assertEquals(newId, army.getId());
    }

    @Test
    public void getNameTest() {
        Assert.assertEquals(name, army.getName());
    }

    @Test
    public void setNameTest() {
        army.setName(newName);
        Assert.assertEquals(newName, army.getName());
    }

    @Test
    public void getUnitsTest() {
        Assert.assertEquals(units, army.getUnits());
    }

    @Test
    public void setUnitsTest() {
        army.setUnits(newUnits);
        Assert.assertEquals(newUnits, army.getUnits());
    }
}
