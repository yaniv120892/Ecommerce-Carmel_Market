package Backend.Addons;

import org.junit.Test;

import static org.junit.Assert.*;

public class SecurityTest {

    @Test
    public void sha256string() throws Exception {
        String textToHash = "This is some text to be hashed!";
        String expected = "9xsMuWm5I63whOYVjuqucf1XZkAOztSVZj1QLYcVtHU=";
        String result = Security.sha256string(textToHash);
        assertEquals(expected, result);
    }

}