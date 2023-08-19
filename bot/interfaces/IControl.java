package bot.interfaces;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface IControl {
    void run() throws TelegramApiException;
    IUser getUser(Long chatId);
    void start(IUser user);
    void replayInfoMenu(IUser user, Integer messageId, String inputText);
    void replayMailMenu(IUser user, String text);
    void photoToMail(IUser user, String caption);
    void documentToMail(IUser user, String caption, String fileUserName);

}
