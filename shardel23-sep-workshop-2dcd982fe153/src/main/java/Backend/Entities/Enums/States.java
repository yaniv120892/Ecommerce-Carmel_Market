package Backend.Entities.Enums;

import java.awt.*;

public enum States {
    ISRAEL, USA, UK, URUGUAY,SPAIN, BRAZIL, GERMANY;

    public static boolean contains(String test){
        for (Enum c : States.values()){
            if (c.name().equals(test))
                return true;
        }
        return false;
    }
}
