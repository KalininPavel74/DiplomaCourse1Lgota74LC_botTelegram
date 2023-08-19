package bot.control;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ControlData {

    // ----------------------------------------------------------------------------
    // Данные для информационного меню, встроенного в телеграм меню (нижнего).
    //-----------------------------------------------------------------------------

    protected static final String EMOJI_CAPSULE = String.valueOf(Character.toChars(0x1F48A)); // капсула ЛС
    protected static final String EMOJI_NEWS = String.valueOf(Character.toChars(0x1F4F0)); // новости
    protected static final String EMOJI_ARROW_UP = String.valueOf(Character.toChars(0x1F861)); // стрелка вверх
    protected static final String EMOJI_CORNER = "\u11ab"; // угол
    protected static final String EMOJI_CORNER_ARROW = "\u2ba9"; // угол со стрелкой

    protected static final String[] menuItemsKbTopS = {"0. Больше кнопок ..."};
    protected static final ReplyKeyboardMarkup menuKbTopS = createKbMenu(menuItemsKbTopS);
    protected static final String[] menuItemsKbTop = {"1. "+EMOJI_CAPSULE+" Вопрос по ЛС", "2. "+EMOJI_NEWS + " Новости", "3. Вопрос - Ответ"};
    protected static final ReplyKeyboardMarkup menuKbTop = createKbMenu(menuItemsKbTop);
    protected static final String[] menuItemsKbQuestionAnswer = { EMOJI_ARROW_UP + " Вернуться", "3.1 "+EMOJI_CORNER+" Для ЛПУ информация", "3.2 "+EMOJI_CORNER+" Новый врач", "3.3 "+EMOJI_CORNER+" Горячие Контакты", "3.4 "+EMOJI_CORNER+" e-mail МЗ ЧО"};
    protected static final ReplyKeyboardMarkup menuKbQuestionAnswer = createKbMenu(menuItemsKbQuestionAnswer);
    protected static final String[] menuItemsKbQuestionLC_LPU = { EMOJI_ARROW_UP + " Вернуться", "1.1 "+EMOJI_CORNER_ARROW+" Ввести код   ЛС", "1.2 "+EMOJI_CORNER_ARROW+" Ввести код ЛПУ"};
    protected static final ReplyKeyboardMarkup menuKbQuestionLC_LPU = createKbMenu(menuItemsKbQuestionLC_LPU);
    //protected static final String[] menuItemsKbFastMailLC = {"Вернуться", "Отправить"};
    //protected static final ReplyKeyboardMarkup menuKbFastMailLC = createKbMenu(menuItemsKbFastMailLC);
    //protected static final String[] menuItemsKbTest = {"Вернуться", "Очистить"};
    //protected static final ReplyKeyboardMarkup menuKbTest = createKbMenu(menuItemsKbTest);

    private static ReplyKeyboardMarkup createKbMenu(String[] ss) {
        // Создать клавиатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboard);
        for (String s : ss) {
            KeyboardRow row = new KeyboardRow();
            row.add(s); // KeyboardButton kb = new KeyboardButton(s);  row.add(kb); -- настроить пересылку в другие чаты или другим пользователям?
            keyboard.add(row);
        }
        return replyKeyboardMarkup;
    }

    // ----------------------------------------------------------------------------
    // Данные для меню быстрого письма.
    //-----------------------------------------------------------------------------

    protected static final String EMOJI_THINK = String.valueOf(Character.toChars(0x1F914)); //"\U0001F914";
    protected static final String EMOJI_GOOD_LUCK = String.valueOf(Character.toChars(0x1F91E)); //"\U0001F91E";
    protected static final String EMOJI_MONOCLE = String.valueOf(Character.toChars(0x1F9D0)); //"\U0001F9D0";
    protected static final String EMOJI_LIGHTNING = "\u26a1\ufe0f";
    protected static final String EMOJI_PAPER_CLIP = String.valueOf(Character.toChars(0x1F4CE)); //"\\U0001F4CE" 128206;;
    protected static final String EMOJI_THUMBS_UP = String.valueOf(Character.toChars(0x1F44D)); //"\\U0001F44D";
    protected static final String EMOJI_THUMBS_DOWN = String.valueOf(Character.toChars(0x1F44E)); //"\\U0001F44E";
    protected static final String EMOJI_TORTLE = String.valueOf(Character.toChars(0x1F422)); //"\\U0001F422";
    protected static final String ASCII_ARROW_DOWN = "\u2193";
    protected static final String ASCII_CROSS = "\u274C"; // крест красный
    protected static final String EMOJI_QUESTION = "\u2753"; // красный вопрос

    //String emoji = new String(Character.toChars(0x1f3c6));

    private static final String[] menuRowInline0 = {"Больше кнопок ..."};
    private static final String[] menuRowInline1 = {EMOJI_TORTLE + " Тормозит", EMOJI_MONOCLE + " Не вижу"};
    private static final String[] menuRowInline2 = {EMOJI_THINK + " Непонятное сообщение"};
    //private static final String[] menuRowInline3 = {EMOJI_MONOCLE + " Не вижу программу"};
    private static final String[] menuRowInline4 = {"Спасибо!", "Успехов!"};
    private static final String[] menuRowInline5 = {"-"};
    //private static final String[] menuRowInline6 = {EMOJI_THUMBS_UP + " Отправить письмо"};
    //private static final String[] menuRowInline7 = {"--"};
    //private static final String[] menuRowInline8 = {ASCII_CROSS + " Очистить письмо"};

    private static final String[] menuRowInline9 = {EMOJI_THUMBS_UP + " Отправить", ASCII_CROSS + " Удалить письмо"};

    protected static final InlineKeyboardMarkup menuInline = createInlineMenu(new String[][]{menuRowInline1, menuRowInline2, menuRowInline4, menuRowInline9});
    protected static final InlineKeyboardMarkup menuInlineS = createInlineMenu(new String[][]{menuRowInline0, menuRowInline9});

    protected static final String MORE_MAIL_BUTTON = menuRowInline0[0];
    protected static final String SEND_MAIL = menuRowInline9[0];
    protected static final String CLEAR_MAIL = menuRowInline9[1];

    private static InlineKeyboardMarkup createInlineMenu(String[][] sss) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(rowList);
        for (String[] ss : sss) {
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            rowList.add(keyboardButtonsRow);
            for (String s : ss) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(s);
                inlineKeyboardButton.setCallbackData(s);
                keyboardButtonsRow.add(inlineKeyboardButton);
            }
        }
        return inlineKeyboardMarkup;
    }

    protected static final String FIRST_MAIL_MESSAGE = ".\n\nполе для НОВОГО быстрого письма: ";
    protected static final String NO_TEXT = "информация не запрошена и не введена";
    protected static final String REPEAT_TEXT = "\n\n" + EMOJI_LIGHTNING + "!!! Повторите отправку. !!!";
    protected static final String SENDED = EMOJI_THUMBS_UP + " Письмо отправлено";
//    protected static final String READY_TO_SEND = EMOJI_QUESTION + "Хотите отравить" + EMOJI_QUESTION + " - нажмите кнопку \n" + EMOJI_THUMBS_UP + "'Отправить' или кнопку " + ASCII_CROSS + "'Удалить'\n.";
    protected static final String READY_TO_SEND = EMOJI_QUESTION + "Хотите отравить" + EMOJI_QUESTION + " - нажмите кнопку \n" + EMOJI_THUMBS_UP + "'Отправить' или   " + ASCII_CROSS;
    protected static final String FIRST_MESSAGE = EMOJI_PAPER_CLIP + " Скрепка - для добавления фото. Строка с текстом 'Написать сообщение ...' для добавления текста к письму (по желанию).";
    protected static final String NEWS_MESSAGE = "Предлагаю, сфотографировать ошибку, загрузить фото (по скрепке) и нажать кнопку 'Отправить'. Персональные данные фотографировать НЕ нужно.";

}
