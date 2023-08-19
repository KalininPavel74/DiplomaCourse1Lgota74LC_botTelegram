package bot.interfaces;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public interface IModelMail extends IModelMessage {
    InlineKeyboardMarkup getInlineKeyboardMarkup();
    void setInlineKeyboardMarkup(InlineKeyboardMarkup inlineKeyboardMarkup);
    List<IPhotoLinkFile> getPhotosLinkFiles();
    IPhotoLinkFile addPhotoLinkFile(String fullname, String userName, String id, long size);
    boolean getStart();
    void setStart(boolean start);
    void resetToNull(int index, IPhotoLinkFile photoLinkFile); // убрать
    // если файлы подобны, то обнулить меньший и удалить его с диска, но не менять размер хранилища
    void resetToNullUnnecessaryPhoto(int index1, IPhotoLinkFile photoLinkFile1, int index2, IPhotoLinkFile photoLinkFile2); // убрать
    // если файлы подобны, то обнулить меньший и удалить его с диска, но не менять размер хранилища
    void resetToNullUnnecessaryPhotos();
    void clearPhotosLinkFiles();
    int sizePhotosLinkFiles();
}
