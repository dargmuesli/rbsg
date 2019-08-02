package de.uniks.se1ss19teamb.rbsg.util;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import de.uniks.se1ss19teamb.rbsg.TestUtil;
import de.uniks.se1ss19teamb.rbsg.ui.PopupController;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import javax.swing.*;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.FieldSetter;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SerializeUtil.class)
@PowerMockIgnore({"javax.management.*", "javax.script.*", "javax.swing.*"})
public class SerializeUtilTest {
    static class TestClass {
        String name = "my name";
        int mtr = 123456;
    }

    private PopupController popupControllerMock = mock(PopupController.class);
    private String fileString = "file.json";
    private String testClassString = "{\"name\":\"my name\",\"mtr\":123456}";
    private TestClass testClass = new TestClass();

    @Before
    public void before() {
        NotificationHandler.getInstance().setPopupController(popupControllerMock);
        TestUtil.setupHeadlessMode();
    }

    @Test
    public void chooseFileTest() throws Exception {
        File file = new File("");

        JFileChooser fileChooserMock = mock(JFileChooser.class);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(fileChooserMock);
        when(fileChooserMock.showSaveDialog(any())).thenReturn(0);
        when(fileChooserMock.getSelectedFile()).thenReturn(file);
        Assert.assertEquals(Optional.of(file), SerializeUtil.chooseFile());

        when(fileChooserMock.showSaveDialog(any())).thenReturn(1);
        Assert.assertEquals(Optional.empty(), SerializeUtil.chooseFile());
    }

    @Test
    public void deserializeTest() throws Exception {
        SerializeUtil.serialize(fileString, this.testClass);

        // from file
        TestClass fromFile = SerializeUtil.deserialize(new File(fileString), TestClass.class);
        assert fromFile != null;
        Assert.assertEquals(this.testClass.name, fromFile.name);
        Assert.assertEquals(this.testClass.mtr, fromFile.mtr);

        // from string
        TestClass fromString = SerializeUtil.deserialize(this.testClassString, TestClass.class);
        Assert.assertEquals(this.testClass.name, fromString.name);
        Assert.assertEquals(this.testClass.mtr, fromString.mtr);

        Files.delete(Paths.get(fileString));

        FieldSetter.setField(SerializeUtil.class, SerializeUtil.class.getDeclaredField("logger"),
            mock(Logger.class));

        whenNew(FileReader.class).withArguments(new File(fileString)).thenThrow(new IOException());
        Assert.assertNull(SerializeUtil.deserialize(new File(fileString), TestClass.class));
        verify(popupControllerMock).displayError("Could not deserialize file.json to"
            + " de.uniks.se1ss19teamb.rbsg.util.SerializeUtilTest$TestClass!");
    }

    @Test
    public void serializeTest() throws Exception {
        SerializeUtil.serialize(fileString, this.testClass);

        Assert.assertEquals(this.testClassString,
            new String(Files.readAllBytes(Paths.get(fileString)), StandardCharsets.UTF_8));

        Assert.assertEquals(this.testClassString, SerializeUtil.serialize(this.testClass));

        Files.delete(Paths.get(fileString));

        FieldSetter.setField(SerializeUtil.class, SerializeUtil.class.getDeclaredField("logger"),
            mock(Logger.class));

        whenNew(FileWriter.class).withArguments(fileString).thenThrow(new IOException());
        SerializeUtil.serialize(fileString, this.testClass);
        verify(popupControllerMock).displayError(matches("Could not serialize"
            + " de\\.uniks\\.se1ss19teamb\\.rbsg\\.util\\.SerializeUtilTest\\$TestClass@[a-z0-9]+ to file\\.json!"));
    }
}
