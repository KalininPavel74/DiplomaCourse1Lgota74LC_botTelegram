package bot.interfaces;

public interface IFactoryMail {
    IMail create(boolean auth, String username, String password,
                 String hostSMTP, String portSMTP, boolean debug, IMail.KRIPTO kripto);
}
