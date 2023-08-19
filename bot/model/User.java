package bot.model;

import bot.interfaces.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class User implements IUser {
    static Logger logger = Logger.getLogger(User.class.getName());
    private Long chatId = null; // идентификатор пользователя = чата
    private String name = null; // имя пользователя из Телеграм

    private IModelMail modelMail = null; // данные быстрого письма для отображения
    private IModelInfo modelInfo = null; // информационные данные для отображения
    private Integer waitMessageId = 0; // номер сообщения об ожидании - для удаления
    private long busyUntilTelegram = 0; // дата и время до которого игнорировать сообщения пользователя (для фильта от дребезга)
    private long busyUntilThirdParty = 0; // дата и время до которого не контактировать со сторонними серверами (антиспамер)

    private boolean isInit = false; // флаг первичной инициализации данных для отображения

    public User(Long chatId) {
        this.chatId = chatId;
        this.modelMail = new ModelMail();
        this.modelInfo = new ModelInfo();
    }

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", name='" + name +
                ", isInit='" + isInit +
                ", modelMail=" + modelMail +
                ", modelInfo=" + modelInfo +
                '}';
    }

    public String getChatIdAndName() {
        return this.chatId + " " + this.name;
    }


    @Override
    public Long getChatId() {
        return chatId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public IModelMail getModelMail() {
        return modelMail;
    }

    @Override
    public IModelInfo getModelInfo() {
        return modelInfo;
    }

    @Override
    public Integer getWaitMessageId() {
        return waitMessageId;
    }

    @Override
    public void setWaitMessageId(Integer waitMessageId) {
        this.waitMessageId = waitMessageId;
    }

    @Override
    public long getBusyUntilTelegram() {
        return busyUntilTelegram;
    }

    @Override
    public void setBusyUntilTelegram(long busyUntilTelegram) {
        this.busyUntilTelegram = busyUntilTelegram;
    }

    @Override
    public long getBusyUntilThirdParty() {
        return busyUntilThirdParty;
    }

    @Override
    public void setBusyUntilThirdParty(long busyUntilThirdParty) {
        this.busyUntilThirdParty = busyUntilThirdParty;
    }

    public boolean isInit() {
        return this.isInit;
    }

    public void setIsInit() {
        this.isInit = true;
    }

    public class ModelInfo implements IModelInfo {

        private Integer messageId = 0; // код сообщения для последующего удаления при обновлении информации
        private ReplyKeyboardMarkup replyKeyboardMarkup = null; // меню

        private String text = "[нет данных]"; // текст сообщения

        private int idLPU = -1; // код ЛПУ
        private int idLC = -1; // код ЛС
        private boolean modeLPU = false; // флаг ожидания ввода кода ЛПУ (переделать на пересисление)
        private boolean modeLC = false; // флаг ожидания ввода кода ЛС (переделать на пересисление)

        @Override
        public String toString() {
            return "ModelInfo{" +
                    ", messageId=" + messageId +
                    ", idLPU=" + idLPU +
                    ", idLC=" + idLC +
                    ", modeLPU=" + modeLPU +
                    ", modeLC=" + modeLC +
                    ", text='" + ((text != null) ? text.replace("\n", " -> ") : "null") + '\'' +
                    //", replyKeyboardMarkup=" + replyKeyboardMarkup +
                    '}';
        }

        @Override
        public Integer getMessageId() {
            return messageId;
        }

        @Override
        public void setMessageId(Integer messageId) {
            this.messageId = messageId;
        }

        @Override
        public ReplyKeyboardMarkup getReplyKeyboardMarkup() {
            return replyKeyboardMarkup;
        }

        @Override
        public void setReplyKeyboardMarkup(ReplyKeyboardMarkup replyKeyboardMarkup) {
            this.replyKeyboardMarkup = replyKeyboardMarkup;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public void setText(String text) {
            this.text = text;
        }

        private void clearAllModes() {
            this.modeLC = false;
            this.modeLPU = false;
        }
        public int getIdLPU() {
            return idLPU;
        }
        public void setIdLPU(int idLPU) {
            this.idLPU = idLPU;
        }
        public int getIdLC() {
            return idLC;
        }
        public void setIdLC(int idLC) {
            this.idLC = idLC;
        }

        @Override
        public boolean isModeLC() {
            return modeLC;
        }

        @Override
        public void setModeLC(boolean modeLC) {
            clearAllModes();
            this.modeLC = modeLC;
        }

        @Override
        public boolean isModeLPU() {
            return modeLPU;
        }

        @Override
        public void setModeLPU(boolean modeLPU) {
            clearAllModes();
            this.modeLPU = modeLPU;
        }

        @Override
        public void clear() {
            this.modeLC = false;
            this.modeLPU = false;
            this.idLC = -1;
            //this.idLPU = -1; запомню, чтобы повторно не вводить (обычно 1 пользователь = 1 ЛПУ)
            this.text = "[нет данных]";
        }

    }

    public class ModelMail implements IModelMail {

        private Integer messageId = 0; // код сообщения; для удаления сообщения после обновления данных
        private InlineKeyboardMarkup inlineKeyboardMarkup = null; // меню
        private String text = "[нет данных]"; // текст сообщения
        private List<IPhotoLinkFile> photosLinkFiles = null; // список файлов (фотографий)
        private boolean start = false; // флаг состояния формирования быстрого письма

        public ModelMail() {
            photosLinkFiles = new ArrayList<IPhotoLinkFile>();
        }

        @Override
        public String toString() {
            return "ModelMail{" +
                    ", messageId=" + messageId +
                    ", start=" + start +
                    ", text='" + ((text != null) ? text.replace("\n", " -> ") : "null") + '\'' +
                    //", inlineKeyboardMarkup=" + inlineKeyboardMarkup +
                    ", photos=" + photosLinkFiles +
                    '}';
        }

        @Override
        public void clear() {
            this.start = false;
            this.text = "[нет данных]";
            clearPhotosLinkFiles();
        }

        @Override
        public Integer getMessageId() {
            return messageId;
        }

        @Override
        public void setMessageId(Integer messageId) {
            this.messageId = messageId;
        }

        @Override
        public InlineKeyboardMarkup getInlineKeyboardMarkup() {
            return inlineKeyboardMarkup;
        }

        @Override
        public void setInlineKeyboardMarkup(InlineKeyboardMarkup inlineKeyboardMarkup) {
            this.inlineKeyboardMarkup = inlineKeyboardMarkup;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public void setText(String text) {
            this.text = text;
        }

        @Override
        public List<IPhotoLinkFile> getPhotosLinkFiles() {
            return photosLinkFiles;
        }

        @Override
        public IPhotoLinkFile addPhotoLinkFile(String fullname, String userName, String id, long size) {
            IPhotoLinkFile photoLinkFile = new PhotoLinkFile(fullname, userName, id, size);
            photosLinkFiles.add(photoLinkFile);
            return photoLinkFile;
        }

        @Override
        public void clearPhotosLinkFiles() { // т.к. файлы храняться в файловой системе
            for (int i = 0; i < photosLinkFiles.size(); i++) {
                resetToNull(i, photosLinkFiles.get(i));
            }
            photosLinkFiles.clear();
        }

        @Override
        public int sizePhotosLinkFiles() { // т.к. при удалении файлов, размер хранилища не изменяется
            int qty = 0;
            for (IPhotoLinkFile photoLinkFile : photosLinkFiles) {
                if (photoLinkFile != null)
                    qty++;
            }
            return qty;
        }

        @Override
        public boolean getStart() {
            return start;
        }

        @Override
        public void setStart(boolean start) {
            this.start = start;
        }

        @Override
        public void resetToNullUnnecessaryPhotos() { // найти дубли и удалить
            if (photosLinkFiles.size() <= 1) return;
            for (int i = photosLinkFiles.size() - 1; i >= 0; i--) {
                IPhotoLinkFile photoLinkFile1 = photosLinkFiles.get(i);
                if (photoLinkFile1 == null) continue;
                for (int j = photosLinkFiles.size() - 2; j >= 0; j--) {
                    photoLinkFile1 = photosLinkFiles.get(i);
                    IPhotoLinkFile photoLinkFile2 = photosLinkFiles.get(j);
                    if (photoLinkFile1 == null || photoLinkFile2 == null) continue;
                    logger.info("перед " + i + " : " + j);
                    resetToNullUnnecessaryPhoto(i, photoLinkFile1, j, photoLinkFile2);
                }
            }
        }


        // если файлы подобны, то обнулить меньший и удалить его с диска, но не менять размер хранилища
        @Override
        public void resetToNullUnnecessaryPhoto(int index1, IPhotoLinkFile photoLinkFile1,
                                                int index2, IPhotoLinkFile photoLinkFile2) {
            if (photoLinkFile1 == null || photoLinkFile2 == null) return;
            if (photoLinkFile1.getId().equals(photoLinkFile2.getId())
                    || photoLinkFile1.getSize() > 1000L && photoLinkFile1.getSize() == photoLinkFile2.getSize()
            ) {
                resetToNull(index2, photoLinkFile2);
                return;
            }
            if (photoLinkFile1.getSize() > 1000L && similarity(photoLinkFile1.getId(), photoLinkFile2.getId()) > 0.97) {
                if (photoLinkFile1.getSize() >= photoLinkFile2.getSize())
                    resetToNull(index2, photoLinkFile2);
                else resetToNull(index1, photoLinkFile1);
            }
        }

        // обнулить и удалить с диска, но не менять размер хранилища
        public void resetToNull(int index, IPhotoLinkFile photoLinkFile) {
            if (photoLinkFile == null) return;
            try {
                java.io.File f = new java.io.File(photoLinkFile.getFullname());
                if (!f.delete())
                    logger.info(index + " Ошибка. Файл не был удален с диска. " + photoLinkFile.getFullname());
                else logger.info(index + " Файл был удален с диска. " + photoLinkFile.getFullname());
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            getPhotosLinkFiles().set(index, null);
        }

        private double similarity(String s1, String s2) { // поиск подобных файлов - дублей; глупость, но приятно
            if (s1 == null || s2 == null || s1.isBlank() || s2.isBlank()) return 0;
            int len_min = (s1.length() > s2.length()) ? s2.length() : s1.length();
            int len_max = (s1.length() > s2.length()) ? s1.length() : s2.length();
            int qty = 0;
            for (int i = 0; i < len_min; i++)
                if (s1.charAt(i) == s2.charAt(i)) qty++;
            double res = qty * 1.0 / len_max;
            logger.info("" + res);
            return res;
        }

    }

    public class PhotoLinkFile implements IPhotoLinkFile {
        private String fullname; // абсолютный путь к файлу в файловой системе
        private String userName; // имя файла у пользователя
        private String id; // идентификатор файла в Телеграм
        private long size; // размер файла, используется при удалении дублей файлов

        public PhotoLinkFile(String fullname, String userName, String id, long size) {
            this.fullname = fullname;
            this.userName = userName;
            this.id = id;
            this.size = size;
        }

        @Override
        public String toString() {
            return "Photo{" +
                    "fullname='" + fullname + '\'' +
                    ", userName='" + userName + '\'' +
                    ", id='" + ((id.length() > 30) ? id.substring(16, 20) + "---" + id.substring(25, 28) : "???") + '\'' +
                    ", size=" + size +
                    '}';
        }

        @Override
        public String getFullname() {
            return fullname;
        }

        @Override
        public String getUserName() {
            return userName;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public long getSize() {
            return size;
        }

    }

}
