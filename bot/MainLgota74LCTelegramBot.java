package bot;

import bot.interfaces.*;
import bot.control.*;
import bot.model.*;
import bot.view.*;
import bot.util.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MainLgota74LCTelegramBot {

    static private Logger logger = Logger.getLogger(MainLgota74LCTelegramBot.class.getName());
    static public final String INFO_FILE = "init.txt"; // файл с настройками
    static public final String INFO_FILE2 = "init2.txt"; // файл с паролями - лучше не использовать
    static public final String INFO_FILE_CHARSET = "UTF-8";
    static private String token = ""; // идентификатор бота в Телеграм
    static private String serverSMTP = ""; // адрес SMTP сервера
    static private String serverSMTPport = ""; // порт SMTP сервера
    static private String userAddress = ""; // адрес электронной почты специалиста (example@mail.ru)
    static private boolean auth = false; // необходимость аутентификации на SMTP сервере (true, false)
    static private String userSMTP = ""; // логин пользователя на SMTP сервере
    static private String userSMTPpassword = ""; // пароль пользователя на SMTP сервере
    static private String serverDBMSname = null; // адрес сервера СУБД информационной Системы
    static private int serverDBMSport; // порт сервера СУБД информационной Системы
    static private String serverDBMSlogin = null; // логин пользователя не сервере СУБД информационной Системы
    static private String serverDBMSpassword = null; // пароль пользователя на сервере СУБД информационной Системы
    static private boolean isInitData = false; // состояние заполнения данный из иницилизирующего файла
    static private boolean isInitData2 = false; // состояние заполнения данный из иницилизирующего файла 2 (пароли)

    public static void main(String[] args) {

        // Инициализация логера
        MyLog.loggerInit(MyLog.LOG_FILE);
        // Получить логины-пароли сервисов окружения
        getInitData();

        if (isInitData && isInitData2) {
            IFactoryView factoryView = new FactoryView();
            IView view = factoryView.create(token);

            IFactoryModel factoryModel = new FactoryModel();
            IModel model = factoryModel.create();

            IFactoryMail factoryMail = new FactoryMail();
            IMail mail = factoryMail.create(auth, userSMTP, userSMTPpassword, serverSMTP, serverSMTPport, false, IMail.KRIPTO.FREE);

            IFactoryDBSystem factoryDBSystem = null;
            IDBSystem dbSystem = null;
            if(serverDBMSpassword != null) {
                factoryDBSystem = new FactoryDBSystem();
                try {
                    dbSystem = factoryDBSystem.create(serverDBMSname, serverDBMSport, serverDBMSlogin, serverDBMSpassword);
                } catch (SQLException | ClassNotFoundException e) {
                    logger.warning("Нет достпуа к системной БД \n"+e.getMessage());
                }
            }

            IFactoryControl factoryControl = new FactoryControl();
            IControl control = factoryControl.create(view, model, mail, userAddress, dbSystem);
            try {
                control.run();
                logger.warning("Программа запущена.");
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            logger.warning("Отсутствуют данные для инициализации");
        }
    }

    // Получить логины-пароли сервисов окружения
    private static void getInitData() {

        try (FileReader fr = new FileReader(INFO_FILE, StandardCharsets.UTF_8)) {
            try (BufferedReader br = new BufferedReader(fr)) {
                logger.warning("Ввод инициализирующих данных из файла "+INFO_FILE);

                serverSMTP = br.readLine();
                if(serverSMTP != null) logger.info("serverSMTP: "+serverSMTP.length());

                serverSMTPport = br.readLine();
                if(serverSMTPport != null) logger.info("serverSMTPport: "+serverSMTPport.length());

                userAddress = br.readLine();
                if(userAddress != null) logger.info("userAddress: "+userAddress.length());

                String s = br.readLine();
                if(s != null) logger.info("auth: "+s.length());
                auth = s != null && s.equals("true");

                userSMTP = br.readLine();
                if(userSMTP != null) logger.info("userSMTP: "+userSMTP.length());

                serverDBMSname = br.readLine();
                if(serverDBMSname != null) logger.info("serverDBMSname: "+serverDBMSname.length());

                serverDBMSport = Integer.valueOf(br.readLine());
                if(serverDBMSport != 0) logger.info("serverDBMSport: "+ String.valueOf(serverDBMSport).length());

                serverDBMSlogin = br.readLine();
                if(serverDBMSlogin != null) logger.info("serverDBMSlogin: "+serverDBMSlogin.length());

                isInitData =   serverSMTP != null && !serverSMTP.isBlank()
                        && serverSMTPport != null && !serverSMTPport.isBlank()
                        && userAddress != null && !userAddress.isBlank()
                        && (!auth || auth && userSMTP != null && !userSMTP.isBlank());
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            return;
        }

        try (FileReader fr = new FileReader(INFO_FILE2, StandardCharsets.UTF_8)) {
            try (BufferedReader br = new BufferedReader(fr)) {
                logger.warning("Ввод инициализирующих данных из файла "+INFO_FILE2);

                token = br.readLine();
                if(token != null) logger.info("token: "+token.length());

                if(auth) {
                    userSMTPpassword = br.readLine();
                    if (userSMTPpassword != null) logger.info("userSMTPpassword: " + userSMTPpassword.length());
                }

                serverDBMSpassword = br.readLine();
                if(serverDBMSpassword != null) logger.info("serverDBMSpassword: "+serverDBMSpassword.length());

                isInitData2 = token != null && !token.isBlank()
                        && (!auth || auth && userSMTPpassword != null && !userSMTPpassword.isBlank());
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }

        if (!isInitData2) {
            logger.warning("Ввод инициализирующих данных с консоли.");
            Console console = System.console();
            if (console == null) {
                logger.warning("Нет консольного интерфейса. Нет возможности ввести пароли.");
                System.exit(0);
            }
            char[] passwordArray = null;
            passwordArray = console.readPassword("token: ");
            token = new String(passwordArray);
            if(token != null) logger.warning("token: "+token.length());

            if(auth) {
                passwordArray = console.readPassword("userSMTPpassword: ");
                userSMTPpassword = new String(passwordArray);
                if (userSMTPpassword != null) logger.warning("userSMTPpassword: " + userSMTPpassword.length());
            }

            passwordArray = console.readPassword("serverDBMSpassword: ");
            serverDBMSpassword = new String(passwordArray);
            if(serverDBMSpassword != null) logger.warning("serverDBMSpassword: "+token.length());

            isInitData2 = token != null && !token.isBlank()
                    && (!auth || auth && userSMTPpassword != null && !userSMTPpassword.isBlank());
        }
    }

}
