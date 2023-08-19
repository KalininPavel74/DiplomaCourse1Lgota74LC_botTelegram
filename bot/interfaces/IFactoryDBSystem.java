package bot.interfaces;

import java.sql.SQLException;

public interface IFactoryDBSystem {
    IDBSystem create(String serverDBMSname, int serverDBMSport
            , String serverDBMSlogin, String serverDBMSpassword) throws SQLException, ClassNotFoundException;
}
