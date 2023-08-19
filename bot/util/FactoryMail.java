package bot.util;

import bot.interfaces.IFactoryMail;
import bot.interfaces.IMail;

public class FactoryMail implements IFactoryMail {
    public IMail create(boolean auth, String username, String password,
                 String hostSMTP, String portSMTP, boolean debug, IMail.KRIPTO kripto) {
        return new MailJakarta(auth, username, password, hostSMTP, portSMTP, debug, kripto);
    }
}
