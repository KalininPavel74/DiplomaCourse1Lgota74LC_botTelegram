package bot.util;

import bot.interfaces.IDBSystem;
import bot.interfaces.IFactoryDBSystem;

import java.sql.SQLException;

public class FactoryDBSystem implements IFactoryDBSystem {

    public IDBSystem create(String serverDBMSname, int serverDBMSport
            , String serverDBMSlogin, String serverDBMSpassword) throws SQLException, ClassNotFoundException {
        if (serverDBMSpassword == null) return null;
        return new DBSystem(serverDBMSname, serverDBMSport, serverDBMSlogin, serverDBMSpassword);
    }
}
