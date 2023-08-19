package bot.interfaces;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
public interface IModelInfo extends IModelMessage {
    ReplyKeyboardMarkup getReplyKeyboardMarkup();
    void setReplyKeyboardMarkup(ReplyKeyboardMarkup replyKeyboardMarkup);
    int getIdLPU();
    void setIdLPU(int idLPU);
    int getIdLC();
    void setIdLC(int idLC);
    boolean isModeLC();
    void setModeLC(boolean modeLC);
    boolean isModeLPU();
    void setModeLPU(boolean modeLPU);
}