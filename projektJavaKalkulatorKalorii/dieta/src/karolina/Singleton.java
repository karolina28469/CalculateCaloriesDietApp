package karolina;

import java.sql.SQLException;

public class Singleton {
    private static DietaDb db;

    private Singleton() {}

    public static DietaDb initialize() throws SQLException {
        if(db == null) {
            db = new DietaDb();
        }
        return db;
    }
}
