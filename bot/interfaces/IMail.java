package bot.interfaces;

import java.io.InputStream;

public interface IMail {
    enum KRIPTO {SSL, TLS, FREE}
    boolean send(String aSubject, String aText, String aFromEmail, String[] aToEmails);

    boolean send(String aSubject, String aText, String aFromEmail, String[] aToEmails, String aFileName);

    boolean send(String aSubject, String aText, String aFromEmail, String[] aToEmails, String aFileName, InputStream aFileData);
}
