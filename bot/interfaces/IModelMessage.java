package bot.interfaces;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface IModelMessage {

    Integer getMessageId();

    void setMessageId(Integer messageId);

    String getText();

    void setText(String text);

    void clear();
}
