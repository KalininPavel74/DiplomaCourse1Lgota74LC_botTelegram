package bot.control;

import bot.interfaces.*;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.logging.Logger;

public class Control implements IControl {

    static private Logger logger = Logger.getLogger(Control.class.getName());
    static private final int MAIL_ATTEMPT = 10; // кол-во попыток отправить письмо - рабочий сервер странно ведет себя
    static private final long MAIL_DELAY_BETWEEN_ATTEMPT = 2000L; // задержка между неудачными попытками отправить письмо
    static private final long TIME_BUSY_UNTIL_THIRD_PARTY = 20_000L; // минимальная задержка между желаниями пользователся повзаимодействовать со сторонними серверами
    private String userAddress = ""; // почтоый адрес специалиста информационной Системы
    TelegramBotsApi botsApi = null; // объект библиотеки Телеграм, для возможности обновить связь без перезагрузки программы
    private IView view = null; // объект для взаимодействия с Телеграм
    private IModel model = null; // список пользователей
    private IMail mail = null; // почтовый объект
    private IDBSystem dbSystem = null; // объект для связи с СУБД информационной Системы

    public Control(IView view, IModel model, IMail mail, String userAddress, IDBSystem dbSystem) {
        this.view = view;
        this.model = model;
        this.mail = mail;
        this.userAddress = userAddress;
        this.dbSystem = dbSystem;
        view.setConrol(this);
    }

    public void run() throws TelegramApiException {
        if (botsApi != null) botsApi = null;
        botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(view);
    }

    public IUser getUser(Long chatId) {
        return model.getUser(chatId);
    }

    // для /start
    public void start(IUser user) {
        logger.info("start " + user.getChatIdAndName());
        clearAndInit(user);
        user.setIsInit();
        view(user);
    }

    private void clearAndInit(IUser user) {
        logger.info("clearAndInit " + user);
        user.getModelMail().clear();
        user.getModelMail().setText(ControlData.FIRST_MAIL_MESSAGE); //"Текст быстрого письма: ";
        user.getModelMail().setInlineKeyboardMarkup(ControlData.menuInlineS);

        user.getModelInfo().clear();
        user.getModelInfo().setText(ControlData.FIRST_MESSAGE);
        user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbTop);
    }

    protected void view(IUser user) {
        try {
            view.view(user);
        } catch (Exception e) {
            // если сообщение не прошло
            // предположу, что проблема в самих данных
            // очистить данные и отправить пустышку
            // чтобы можно было начать сначала.
            e.printStackTrace();
            logger.info("view " + e.getMessage());
            clearAndInit(user);
            try {
                view.view(user);
            } catch (Exception e2) {
            }
        }
    }
    // обработка входящих сообщений связанных с информационным меню
    public void replayInfoMenu(IUser user, Integer messageId, String inputText) {

        if (inputText.equals(ControlData.menuItemsKbTopS[0])) {  // "Больше кнопок ..."
            user.getModelInfo().setText("Информационное меню раскрыто.");
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbTop);
        } else if (inputText.equals(ControlData.menuItemsKbTop[1])) {  // "Новости"
            user.getModelInfo().setText(ControlData.NEWS_MESSAGE);
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbTop);
        } else if (inputText.equals(ControlData.menuItemsKbTop[2])) {  // "Вопрос - Ответ"
            user.getModelInfo().setText("Выбран пункт меню " + ControlData.menuItemsKbTop[2]);
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbQuestionAnswer);
        } else if (inputText.equals(ControlData.menuItemsKbTop[0])) {  // "Вопрос по ЛС"
            user.getModelInfo().setText("Выбран пункт меню " + ControlData.menuItemsKbTop[0] + " \n Для быстрого ввода, например: 453 7412001. \n Или для повтора мышью /453_7412001_ \n");
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbQuestionLC_LPU);
        } else if (inputText.equals(ControlData.menuItemsKbQuestionAnswer[0]) || inputText.equals(ControlData.menuItemsKbQuestionLC_LPU[0])) {  // "Вернуться"
            user.getModelInfo().setText("Выбран пункт меню " + ControlData.menuItemsKbQuestionAnswer[0]);
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbTop);
        } else if (inputText.equals(ControlData.menuItemsKbQuestionAnswer[1])) {  // "Для ЛПУ информация"
            //modelInfo.text = "Выбран пункт меню " + menuItemsKbQuestionAnswer[1];
            user.getModelInfo().setText("(Домино ЛПУ) Кнопка 'Справочная система'");
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbQuestionAnswer);
        } else if (inputText.equals(ControlData.menuItemsKbQuestionAnswer[2])) {  // "Новый врач"
            user.getModelInfo().setText("(Домино ЛПУ) Кнопка 'Справочная система' - стр.1 контакт Спицина М.Л. - стр.2 файл Шаблон");
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbQuestionAnswer);
        } else if (inputText.equals(ControlData.menuItemsKbQuestionAnswer[3])) {  // "Горячие контакты"
            user.getModelInfo().setText("(Домино ЛПУ) Кнопка 'Справочная система' - стр.1");
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbQuestionAnswer);
        } else if (inputText.equals(ControlData.menuItemsKbQuestionAnswer[4])) {  // "e-mail МЗ ЧО"
            user.getModelInfo().setText("comfarm@minzdrav74.ru");
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbQuestionAnswer);
        } else if (inputText.equals(ControlData.menuItemsKbQuestionLC_LPU[2])) {  // "Введите код ЛПУ"
            user.getModelInfo().setModeLPU(true);
            user.getModelInfo().setText("Введите код ЛПУ (например: 92 или 092 или 123)");
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbQuestionLC_LPU);
            logger.warning("1 " + user.getModelInfo());
        } else if (inputText.equals(ControlData.menuItemsKbQuestionLC_LPU[1])) {  // "Введите код ЛС"
            user.getModelInfo().setModeLC(true);
            user.getModelInfo().setText("Введите код ЛС (например: 7412001 или 910123 или 220072)");
            user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbQuestionLC_LPU);
        } else if (inputText.matches("[/]?[0-9]{2,3}[_ ][0-9]{6,7}[_ ]?.*")) { // быстрый набор ЛПУ ЛС
/*
        System.out.println(""+f("/92_7412001_df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("/92_7412001_10df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("/092_7412001_df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("/492_7412001_1df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("92 7412001_df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("092 7412001_df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("092 7412001"));
        System.out.println(""+f("92 7412001"));
        System.out.println(""+f("/92_912001_df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("/92_912001_10df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("/092_912001_df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("/492_912001_1df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("92 912001_df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("092 912001_df asdf fвыа2343в_ыа 42_343 234"));
        System.out.println(""+f("092 912001"));
        System.out.println(""+f("92 912001"));
* */
            String[] ss = inputText.replaceAll("[a-zA-Zа-яА-Я_/]", " ").trim().split(" ");
            if (ss != null && ss.length > 1) {
                int lpu = 0;
                try {
                    lpu = Integer.valueOf(ss[0]);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                int lc = 0;
                try {
                    lc = Integer.valueOf(ss[1]);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                if (lpu >= 55 && lpu <= 750 && lc >= 200000 && lc <= 7499999) {
                    user.getModelInfo().setIdLPU(lpu);
                    user.getModelInfo().setModeLPU(false);
                    user.getModelInfo().setIdLC(lc);
                    user.getModelInfo().setModeLC(false);

                    if (processingLPU_LC(user)) {
                        user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbTopS);
                    }

                } else {
                    user.getModelInfo().setText("Не удалось извлечь из переданной сторки коды.");
                    logger.info("" + lpu + " " + lc);
                }
            } else {
                user.getModelInfo().setText("Не удалось извлечь из переданной сторки коды.");
                logger.info("" + ss + " ");
            }
        } else { // не быстрый набор
            if (user.getModelInfo().isModeLPU()) { // ожидание ввода кода ЛПУ
                int lpu = 0;
                try {
                    lpu = Integer.valueOf(inputText);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                if (lpu >= 55 && lpu <= 750) {
                    user.getModelInfo().setIdLPU(lpu);
                    user.getModelInfo().setModeLPU(false);

                    if (processingLPU_LC(user)) {
                        user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbTopS);
                    } else {
                        user.getModelInfo().setText("Код ЛПУ " + lpu + " принят.");
                    }
                } else {
                    user.getModelInfo().setText("Неправильный код ЛПУ.");
                }
            } else if (user.getModelInfo().isModeLC()) { // ожидание ввода кода ЛС
                int lc = 0;
                try {
                    lc = Integer.valueOf(inputText);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                if (lc >= 200000 && lc <= 7499999) {
                    user.getModelInfo().setIdLC(lc);
                    user.getModelInfo().setModeLC(false);

                    if (processingLPU_LC(user)) {
                        user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbTopS);
                    } else {
                        user.getModelInfo().setText("Код ЛC " + lc + " принят.");
                    }
                } else {
                    user.getModelInfo().setText("Неправильный код ЛС.");
                }
            } else {  // ввод данных не связанных с поиском информации по коду ЛПУ и ЛС
                // пользователь ввел свои данные - добавить к письму
                logger.warning("Control пользователь ввел свои данные >>> " + inputText);
                if (!user.getModelMail().getStart()) {
                    initMail(user, inputText);
                } else {
                    user.getModelMail().setText(user.getModelMail().getText() + "\n" + inputText);
                }
                user.getModelInfo().setText(ControlData.READY_TO_SEND);
            }
        }
        user.getModelMail().setInlineKeyboardMarkup(ControlData.menuInlineS);
        if (user.getModelMail().getText() == null) {
            user.getModelMail().setText(ControlData.FIRST_MAIL_MESSAGE);
        }
        view(user);
    }

    // обработка входящих сообщений связанных с почтовым меню
    public void replayMailMenu(IUser user, String text) {

        user.getModelInfo().setReplyKeyboardMarkup(ControlData.menuKbTopS);
        if (user.getModelMail().getText().contains(ControlData.SENDED)) {
            user.getModelMail().setText(ControlData.NO_TEXT);
        }
        if (user.getModelInfo().getText().contains(ControlData.SENDED)) {
            user.getModelInfo().setText(ControlData.NO_TEXT);
        }

        logger.warning("replayMailMenu " + user.getChatIdAndName() + " " + text);
        if (ControlData.SEND_MAIL.equals(text)) {
            if (!user.getModelMail().getStart()) {
                user.getModelMail().setText(user.getModelMail().getText() + ".");
                user.getModelMail().setInlineKeyboardMarkup(ControlData.menuInlineS);
                logger.info("Письмо не начато");
            } else {
                logger.info("Перед отправкой");
                view.sendMessageWAIT(user);
                boolean b;
                if (user.getModelMail().sizePhotosLinkFiles() == 0) {
                    b = sendMail(user, user.getModelMail().getText(), null, MAIL_ATTEMPT);
                    if (!b) {
                        if (!user.getModelMail().getText().contains(ControlData.REPEAT_TEXT)) {
                            user.getModelMail().setText(user.getModelMail().getText() + ControlData.REPEAT_TEXT);
                        }
                        user.getModelInfo().setText(ControlData.REPEAT_TEXT);
                        view(user);
                        return;
                    } else {
                        view.sendMessage(user.getChatId(), user.getModelMail().getText()); // оставить след
                    }
                } else {
                    for (int i = 0; i < user.getModelMail().getPhotosLinkFiles().size(); i++) {
                        IPhotoLinkFile photoLinkFile = user.getModelMail().getPhotosLinkFiles().get(i);
                        if (photoLinkFile != null) {
                            logger.info("Начать отправку " + photoLinkFile.getFullname());
                            b = sendMail(user, user.getModelMail().getText(), photoLinkFile.getFullname(), MAIL_ATTEMPT);
                            if (!b) {
                                if (!user.getModelMail().getText().contains(ControlData.REPEAT_TEXT))
                                    user.getModelMail().setText(user.getModelMail().getText() + ControlData.REPEAT_TEXT);
                                user.getModelInfo().setText(ControlData.REPEAT_TEXT);
                                view(user);
                                return;
                            } else {
                                view.sendMessage(user.getChatId(), user.getModelMail().getText()); // оставить след
                                user.getModelMail().resetToNull(i, photoLinkFile);
                                user.getModelMail().setText(user.getModelMail().getText() + "\n" + ControlData.EMOJI_PAPER_CLIP + " Фото отправлено.");
                            }
                        }
                    }
                    logger.info("Фото после отправки: " + user.getModelMail().getPhotosLinkFiles());
                }
                clearAndInit(user);
                user.getModelMail().setText(".\n\n" + ControlData.SENDED);
                user.getModelInfo().setText(ControlData.SENDED + "\n.");
            }
        } else if (ControlData.CLEAR_MAIL.equals(text)) {
            if (!user.getModelMail().getStart()) {
                user.getModelMail().setText(user.getModelMail().getText() + ".");
                user.getModelMail().setInlineKeyboardMarkup(ControlData.menuInlineS);
            } else {
                clearAndInit(user);
            }
        } else if (ControlData.MORE_MAIL_BUTTON.equals(text)) {
            user.getModelMail().setInlineKeyboardMarkup(ControlData.menuInline);
            if (user.getModelMail().getStart()) {
                user.getModelInfo().setText(ControlData.READY_TO_SEND);
            }
        } else if (user.getModelMail().getStart()) {
            user.getModelMail().setText(user.getModelMail().getText() + "\n" + text);
            user.getModelInfo().setText(ControlData.READY_TO_SEND);
        } else {
            initMail(user, text);
            user.getModelInfo().setText(ControlData.READY_TO_SEND);
        }
        logger.info("replayMailMenu отработано и на выдачу");
        view(user);
    }

    public void photoToMail(IUser user, String caption) {
        if (!user.getModelMail().getStart()) initMail(user, null);
        user.getModelMail().setText(user.getModelMail().getText() + "\n" + ControlData.EMOJI_PAPER_CLIP + " Файл " + user.getModelMail().sizePhotosLinkFiles() + " шт. " + ((caption == null) ? "" : " - " + caption));
        user.getModelInfo().setText(ControlData.READY_TO_SEND);
        view(user);
    }

    public void documentToMail(IUser user, String caption, String fileUserName) {
        if (!user.getModelMail().getStart()) initMail(user, null);
        user.getModelMail().setText(user.getModelMail().getText() + "\n" + ControlData.EMOJI_PAPER_CLIP + " Файл " + fileUserName + " - " + caption);
        user.getModelInfo().setText(ControlData.READY_TO_SEND);
        view(user);
    }

    private void initMail(IUser user, String text) {
        text = (text == null) ? "" : "\n" + text;
        user.getModelMail().setText(".\n\nКалинину от: " + user.getName() + " " + text);
        user.getModelMail().setStart(true);

        user.getModelInfo().setText(ControlData.READY_TO_SEND);
    }


    private boolean sendMail(IUser user, String text, String fileName, int attempt) {
        boolean result;
        runDelayBusyUntilThirdParty(user);
        result = sendMail_(text, fileName, attempt);
        if (result) user.setBusyUntilThirdParty(new java.util.Date().getTime() + TIME_BUSY_UNTIL_THIRD_PARTY);
        return result;
    }

    private boolean sendMail_(String text, String fileName, int attempt) {
        String subject = "Письмо из Telegram " + view.getBotName() + "\n";
        logger.info("attempt=" + attempt + " " + subject + text);
        for (int j = 0; j < attempt; j++) {
            try {
                boolean b;
                if (fileName == null || fileName.isBlank())
                    b = mail.send(subject, text, userAddress, new String[]{userAddress});
                else b = mail.send(subject, text, userAddress, new String[]{userAddress}, fileName);
                logger.warning((j + 1) + " попытка - " + b);
                if (b) return true;
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            try {
                Thread.sleep(MAIL_DELAY_BETWEEN_ATTEMPT);
            } catch (InterruptedException e1) {
                ;
            }
        }
        return false;
    }

    // запрос к информационной Системе
    private String getDataFromSystemDB_LPU_LC(IUser user) {
        String result;
        runDelayBusyUntilThirdParty(user);
        result = dbSystem.getProcedureData(" select temps1x.dbo.fsHospitalRestsSpec9(?,?); "
                , new String[]{String.valueOf(user.getModelInfo().getIdLPU()), String.valueOf(user.getModelInfo().getIdLC())});
        user.setBusyUntilThirdParty(new java.util.Date().getTime() + TIME_BUSY_UNTIL_THIRD_PARTY);
        return result;
    }

    private boolean processingLPU_LC(IUser user) {
        String s = null;
        // Если пользователь ввел коды.
        if (user.getModelInfo().getIdLPU() > 0 && user.getModelInfo().getIdLC() > 0) {
            // Строка для быстрого повторного запроса. Останется в истории.
            s = "/" + user.getModelInfo().getIdLPU() + "_" + user.getModelInfo().getIdLC() + "_" + " код ЛПУ: " + user.getModelInfo().getIdLPU() + ", код ЛС: " + user.getModelInfo().getIdLC();
            view.sendMessageWAIT(user); // ЖДАТЬ. т.к. будет обращение к базе или к почте

            if (dbSystem == null) {
                s = s + " [0]"; // оставить место для дополнительного информирования и флаг для отсылке по почте
            } else {

                // чтобы базу не донимали - переделать; устанавливать время не активное в данных пользователя
                int i = 0;
                // наполнить текстом результат запроса к базе
                s = s + "\n " + getDataFromSystemDB_LPU_LC(user);
                s = s.replace("[]", "\n--------------- ---------------");

                if (s.contains("[1]") || s.contains("[2]") || s.contains("[3]") || s.contains("[4]") || s.contains("[5]") || s.contains("[6]") || s.contains("[7]")) {
                    if (s.contains("[1]")) i++;
                    s = s.replace("[1]", "\n" + i + ") Доступно для выписки из неликвидов уп. : ");
                    if (s.contains("[2]")) i++;
                    s = s.replace("[2]", "\n" + i + ") Доступно для выписки по осн. заявке уп. : ");
                    if (s.contains("[3]")) i++;
                    s = s.replace("[3]", "\n" + i + ") Доступно для выписки по ПЕРС. заявке уп. : ");
                    if (s.contains("[4]")) i++;
                    s = s.replace("[4]", "\n" + i + ") Заблокировано уп. : ");
                    if (s.contains("[5]")) i++;
                    s = s.replace("[5]", "\n" + i + ") Льготник НЕ забрал ЛС из Аптеки уп. : ");
                    //if(s.contains("[7]")) i++;
                    s = s.replace("[7]", "\n");
                    if (s.contains("[6]")) {
                        i++;
                        s = s.replace("[6]", "\n" + i + ") Срок годности < 3 мес. уп. : ");
                        s = s + " \n [0]";
                    }
                } else {
                    s = s + "\n\n Нет информации из ЛПУ по ЛС.";
                }
            }
            // если есть флаг - отправить данные специалисту по почте
            if (s.contains("[0]")) {
                logger.info("Обнаружил флаг [0] - будут отправлять информацию по емаил");
                //logger.info(s);
                //logger.info("---");
                boolean b = sendMail(user, user.getChatIdAndName() + s, null, MAIL_ATTEMPT);
                if (b) {
                    user.getModelInfo().setText(s.replace("[0]", " \n Запрос отправлен на почту специалисту."));
                } else {
                    user.getModelInfo().setText(s.replace("[0]", " \n Не удалось отправить запрос на почту специалисту. (можно повторить, кликнув на коды)"));
                }
            } else {
                user.getModelInfo().setText(s);
            }
            logger.warning(s);
            view.sendMessage(user.getChatId(), s); // оставить след и для повторного нажатия
            return true;
        }
        return false;
    }


    // реалиация задержки при взаимодействии со сторонними серверами (антиспамер)
    private void runDelayBusyUntilThirdParty(IUser user) {
        long delay = user.getBusyUntilThirdParty() - new java.util.Date().getTime();
        if (delay <= 0) return;
        logger.warning("Защитный простой " + delay + " мс.");
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e1) {
            ;
        }
    }

}