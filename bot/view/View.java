package bot.view;

//https://api.telegram.org/bot156XXXX5:AAXXXXXXXXXXXXXXXXXXXNno/messages.deletehistory?peer=5244XX57X&max_id=1000
//        Ответ: {"ok":false,"error_code":404,"description":"Not Found"}

import bot.interfaces.*;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class View extends BaseTelegramBot implements IView {

    static Logger logger = Logger.getLogger(View.class.getName());
    static public final String NO_TEXT = "нет текста";

    private static final String PHOTOS_FOLDER = "files"; // папка с файлами от пользователя
    private static final long TIME_INSENSITIVITY = 500; // Антидребезг. В течении этого времени будут отбрасываться входящие сообщения.
    private String botName = ""; // имя текущего бота (заполняется из Телеграм)
    private IControl control = null; // объект управляющий бизнес-просессом; сам себя передает

    public View(String token) {
        super(token);

        try {
            botName = this.getMe().getFirstName();
        } catch (Exception e) {
            e.printStackTrace();
            botName = "error";
        }
    }

    public void setConrol(IControl control) {
        this.control = control;
    }

    @Override
    public String getBotName() {
        return this.botName;
    }

    // визуализирует данные пользователя - два сообщения с меню
    public void view(IUser user) throws TelegramApiException {
        logger.info("view " + user.getModelMail() + "\n" + user.getModelInfo());
        String sDateTime = (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")).format(new Date());
        Integer beforeMailId = user.getModelMail().getMessageId();
        Integer beforeInfoId = user.getModelInfo().getMessageId();

        SendMessage message;
        // создать сообщение с меню для быстрого e-mail
        message = new SendMessage();

        //message.enableMarkdown(true); // начинаются проблемы со спец символами (file270.jpg - ok  file_270.jpg - не работает)
        //message.enableHtml(true);
        message.setReplyMarkup(user.getModelMail().getInlineKeyboardMarkup());
        message.setChatId(user.getChatId());
        logger.warning("user.modelMail.text = " + user.getModelMail().getText());
        message.setText(user.getModelMail().getText());
        //message.setText("(пред." + modelMail.messageId + ") " + modelMail.text);

        // если удалить последнее сообщение от бота (не путать с пользователем), то и клавиатурное меню тоже удаляется
        // если пользователь начнет писать текст - клавиатурное меню удаляется
        // если пользователь вставит фотографию (документ) - клавиатурное меню Остается
        Integer messageIdForDelete = null;
        try {
            messageIdForDelete = user.getModelMail().getMessageId();
            user.getModelMail().setMessageId(execute(message).getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            logger.info("modelMail. Не понятно. Начать с начала.");
            logger.info(user.toString());
            throw e;
        }
        // удалить прежнее сообщение
        if (messageIdForDelete != null) {
            logger.info("Удалить Mail " + messageIdForDelete);
            deleteMessageById(user.getChatId(), messageIdForDelete);
        } else logger.info("Не удалять Mail " + messageIdForDelete);

        // создать сообщение с меню для информации
        message = new SendMessage();
        //message.enableMarkdown(true); // начинаются проблемы со спец символами (file270.jpg - ok  file_270.jpg - не работает)
        //message.enableHtml(true);
        message.setReplyMarkup(user.getModelInfo().getReplyKeyboardMarkup());
        message.setChatId(user.getChatId());
        message.setText(user.getModelInfo().getText());
//        message.setText(user.getModelInfo().getText() + " \n" + sDateTime);
//        message.setText(sDateTime+" ("+beforeMailId+" "+beforeInfoId+") "+user.getModelInfo().getText());


        // если удалить последнее сообщение от бота (не путать с пользователем), то и клавиатурное меню тоже удаляется
        // если пользователь начнет писать текст - клавиатурное меню удаляется
        // если пользователь вставит фотографию (документ) - клавиатурное меню Остается
        messageIdForDelete = null;
        try {
            messageIdForDelete = user.getModelInfo().getMessageId();
            user.getModelInfo().setMessageId(execute(message).getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            logger.info("modelInfo. Не понятно. Начать с начала.");
            logger.info(user.toString());
            throw e;
        }
        // удалить прежнее сообщение
        if (messageIdForDelete != null) {
            logger.info("Удалить Info " + messageIdForDelete);
            deleteMessageById(user.getChatId(), messageIdForDelete);
        } else logger.info("Не удалять Info " + messageIdForDelete);

        if (user.getWaitMessageId() > 0) {
            deleteMessageById(user.getChatId(), user.getWaitMessageId());
            user.setWaitMessageId(0);
        }
        logger.info("---------------- To Telegram ----------------");
    }

    // служебное сообщение об необходимости ждать окончания обработки
    public void sendMessageWAIT(IUser user) {
        if (user.getWaitMessageId() > 0) {
            logger.warning("Сообщение ЖДЕМ уже отправлено.");
            return;
        }
        SendMessage message;
        message = new SendMessage();
        message.setChatId(user.getChatId());
        message.setText("ЖДЕМ ...");
        try {
            user.setWaitMessageId(execute(message).getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // произвольное сообщение, которое не будет удалено
    public void sendMessage(Long chatId, String text) {
        SendMessage message;
        message = new SendMessage();
        message.setChatId(chatId);
//        message.setText(text + (new SimpleDateFormat(" [dd.MM.yyyy HH:mm:ss]")).format(new Date()));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // установить дату и время для антидребезга
    public void setInsensitivity(IUser user) {
        user.setBusyUntilTelegram(new java.util.Date().getTime() + TIME_INSENSITIVITY);
    }

    // проверить истечение состояния антидребезга
    public boolean isInsensitivity(IUser user) {
        return new java.util.Date().getTime() < user.getBusyUntilTelegram();
    }

    // прием входящих сообщений от Телеграм
    @Override
    public void onUpdateReceived(Update update) {
        logger.info("----------------- From Telegram ----------------");
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            IUser user = control.getUser(chatId);
            Integer messageId = update.getMessage().getMessageId();
            if (!user.isInit()) {
                String login = "";
                if (update.getMessage().getChat().getLastName() != null && !update.getMessage().getChat().getLastName().isBlank())
                    login += update.getMessage().getChat().getLastName() + " ";
                if (update.getMessage().getChat().getFirstName() != null && !update.getMessage().getChat().getFirstName().isBlank())
                    login += update.getMessage().getChat().getFirstName();
                if (login == null || login.isBlank()) {
                    login = "пользователь не определен";
                    logger.info(chatId + " " + login);
                }
                user.setName(login);
                control.start(user);
            }

            String caption = update.getMessage().getCaption();
            if (caption == null || caption.isBlank()) caption = NO_TEXT;
            logger.warning(messageId + " " + user.getChatIdAndName() + " - " + caption);

            if (update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
                logger.warning("1>>> /start");
                control.start(user);
                deleteMessageById(chatId, messageId); // удалить польз. сообщение
                setInsensitivity(user);
            } else if (update.getMessage().hasText() && !update.getMessage().getText().equals("/start")) {
                if (isInsensitivity(user)) {
                    deleteMessageById(chatId, messageId); // удалить польз. сообщение
                    return;
                }
                String inputText = NO_TEXT;
                if (update.getMessage().getText() != null)
                    inputText = update.getMessage().getText();
                else if (update.getMessage().getCaption() != null)
                    inputText = update.getMessage().getText();
                logger.warning("2>>> " + inputText);
                control.replayInfoMenu(user, messageId, inputText);
                deleteMessageById(chatId, messageId); // удалить польз. сообщение
                setInsensitivity(user);
                logger.warning("2   >>> " + inputText);
            } else if (update.getMessage().hasPhoto()) {
                logger.warning("3>>> Photo");
                if (isInsensitivity(user)) {
                    deleteMessageById(chatId, messageId); // удалить польз. сообщение
                    return;
                }
                sendMessageWAIT(user);
                List<PhotoSize> photoList = update.getMessage().getPhoto();
                logger.info("photoList = " + photoList);
                for (int i = 0; i < photoList.size(); i++) {
                    PhotoSize photoSize = photoList.get(i);
                    GetFile getFile = new GetFile(photoSize.getFileId());
                    try {
                        File file_from = execute(getFile); //tg file obj
                        java.nio.file.Path path = java.nio.file.Path.of(file_from.getFilePath());
                        String bot_file_name = path.getFileName().toString();
                        String fileNameLocal = PHOTOS_FOLDER + "/" + createFileName(user.getName()) + "photo_" + bot_file_name;
                        java.io.File file_to = new java.io.File(fileNameLocal);
                        logger.info("saveFileToHDD " + file_to.getAbsolutePath());
                        downloadFile(file_from, file_to);
                        user.getModelMail().addPhotoLinkFile(fileNameLocal, bot_file_name, photoSize.getFileId(), file_to.length());
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    //  File f_path = new File("https://api.telegram.org/file/bot"+getBotToken()+"/"+file.getFilePath());
                }
                user.getModelMail().resetToNullUnnecessaryPhotos();
                logger.info("после обнуления дублей");
                control.photoToMail(user, caption);
                deleteMessageById(chatId, messageId); // удалить польз. сообщение
                setInsensitivity(user);
            } else if (update.getMessage().hasDocument()) {
                logger.warning("4>>> Document");
                if (isInsensitivity(user)) {
                    deleteMessageById(chatId, messageId); // удалить польз. сообщение
                    return;
                }
                sendMessageWAIT(user);
                Document document = update.getMessage().getDocument();
                String fileNameLocal = PHOTOS_FOLDER + "/" + createFileName(user.getName()) + "doc_" + document.getFileName();
                logger.info(user.getChatIdAndName()
                        + " fileNameHDD=" + fileNameLocal
                        + " document.getFileName()+" + document.getFileName()
                        + " caption=" + caption);

                GetFile getFile = new GetFile(document.getFileId());
                try {
                    File file_from = execute(getFile); //tg file obj
                    java.io.File file_to = new java.io.File(fileNameLocal);
                    logger.info("saveFileToHDD " + file_to.getAbsolutePath());
                    downloadFile(file_from, file_to);

                    user.getModelMail().addPhotoLinkFile(fileNameLocal, document.getFileName(), document.getFileId(), file_to.length());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                //  File f_path = new File("https://api.telegram.org/file/bot"+getBotToken()+"/"+file.getFilePath());
                user.getModelMail().resetToNullUnnecessaryPhotos();
                control.documentToMail(user, caption, document.getFileName());
                deleteMessageById(chatId, messageId); // удалить польз. сообщение
                setInsensitivity(user);
            }
        } else if (update.hasCallbackQuery()) {
            logger.warning("5>>> CallbackQuery");
            Message inputMessage = update.getCallbackQuery().getMessage();
            Long chatId = inputMessage.getChatId();
            IUser user = control.getUser(chatId);
            logger.info("chatId=" + chatId);

            if (!user.isInit()) {
                String login = "";
                if (inputMessage.getChat().getLastName() != null && !inputMessage.getChat().getLastName().isBlank())
                    login += inputMessage.getChat().getLastName() + " ";
                if (inputMessage.getChat().getFirstName() != null && !inputMessage.getChat().getFirstName().isBlank())
                    login += inputMessage.getChat().getFirstName();

                if (login == null || login.isBlank()) {
                    login = "пользователь не определен";
                    logger.warning(chatId + " " + login);
                }
                user.setName(login);
                control.start(user);
            }
            if (isInsensitivity(user)) return;

            String text = update.getCallbackQuery().getData();
            logger.warning(user.getChatIdAndName() + " >>> " + text);
            control.replayMailMenu(user, text);
            setInsensitivity(user);
        } else {
            logger.warning("6>>> ???");
            print(update);
        }
    }

    private String createFileName(String userName) {
        String yyyyMMddHHmmssS = "yyyyMMddHHmmssS";
        String sDateTime = (new SimpleDateFormat(yyyyMMddHHmmssS)).format(new Date());
        String prefix_file_name = sDateTime + "_" + userName.replaceAll("[^a-zA-Zа-яА-Я0-9_]", "") + "_";
        return prefix_file_name;
    }

    // удалить сообщение из Телеграм по его номеру
    private void deleteMessageById(Long chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), messageId);
        try {
            execute(deleteMessage);
        } catch (Exception tae) {
            ; //logger.info(messageId + ") " + tae.getMessage());
        }
    }


    // если leaveLast = 0, то ни одного сообещния не останется и появится кнопка НАЧАТЬ - то что нужно
    // при этом сообщения ответа не будет, т.к. оно ответ на пришедшее сообщение, которое удалили.
    // если leaveLast = 1, то останется последнее сообщение от бота и на него можно ответить
    private void deleteMessageBefore(Long chatId, int messageId, int qty) {
        int sum = 0, sumOk = 0, sumBad = 0;
        int begin = ((messageId - qty) < 0) ? 1 : (messageId - qty);
        for (int i = begin; i <= messageId + qty; i++) {
            sum++;
            DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), i);
            try {
                execute(deleteMessage);
                sumOk++; // если удаление не ушло по пути исключения.
            } catch (Exception tae) {
                sumBad++;
            }
        }
        logger.info("Попыток " + sum + ". Очищено " + sumOk + " сообщений. В холостую " + sumBad + " сообщений. (messageId=" + messageId + ")");
    }

}