package bot.interfaces;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.List;

public interface IView extends LongPollingBot {
    void setConrol(IControl control);
    String getBotName();
    void view(IUser user) throws TelegramApiException;
    void sendMessageWAIT(IUser user);
    void sendMessage(Long chatId, String text);
}
