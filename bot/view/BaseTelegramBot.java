package bot.view;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.logging.Logger;

public class BaseTelegramBot extends TelegramLongPollingBot {

    static Logger logger = Logger.getLogger(BaseTelegramBot.class.getName());

    private String token = "";

    public BaseTelegramBot(String token) {
        this.token = token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("BaseTelegramBot onUpdateReceived(Update update)");
        print(update);
    }

    public void print(Update update) {
        logger.info("--------------------------------------------");

        if (update != null) getFieldsOfObject2(true,"", update.toString(), 0);

        if (update.hasMessage()) logger.info("1 update.hasMessage()=" + update.hasMessage());
        if (update.hasCallbackQuery()) logger.info("2 update.hasCallbackQuery()=" + update.hasCallbackQuery());
        if (update.hasChannelPost()) logger.info("3 update.hasChannelPost()=" + update.hasChannelPost());
        if (update.hasInlineQuery()) logger.info("4 update.hasInlineQuery()=" + update.hasInlineQuery());
        if (update.hasChatMember()) logger.info("5 update.hasChatMember()=" + update.hasChatMember());
        if (update.hasEditedMessage()) logger.info("6 update.hasEditedMessage()=" + update.hasEditedMessage());
        if (update.hasPoll()) logger.info("7 update.hasPoll()=" + update.hasPoll());
        if (update.hasPollAnswer()) logger.info("8 update.hasPollAnswer()=" + update.hasPollAnswer());
        if (update.hasShippingQuery()) logger.info("9 update.hasShippingQuery()=" + update.hasShippingQuery());
        if (update.hasChosenInlineQuery())
            logger.info("10 update.hasChosenInlineQuery()=" + update.hasChosenInlineQuery());
        if (update.hasChatJoinRequest())
            logger.info("11 update.hasChatJoinRequest()=" + update.hasChatJoinRequest());
        if (update.hasPreCheckoutQuery())
            logger.info("12 update.hasPreCheckoutQuery()=" + update.hasPreCheckoutQuery());



        if (update.getUpdateId() != null) logger.info("13 update.getUpdateId()=" + update.getUpdateId());
//        if (update.getMessage() != null) logger.info("14 update.getMessage()=" + update.getMessage());
//        if (update.getMessage() != null) logger.info(true,"", update.getMessage().toString());

        if (update.getCallbackQuery() != null) {
            logger.info("15 update.getCallbackQuery()=" + update.getCallbackQuery());
            getFieldsOfObject2(true,"", update.getCallbackQuery().toString(), 0);
        }
        if (update.getChatMember() != null) logger.info("16 update.getChatMember()=" + update.getChatMember());
        if (update.getMyChatMember() != null)
            logger.info("17 update.getMyChatMember()=" + update.getMyChatMember());
        if (update.getChannelPost() != null) logger.info("18 update.getChannelPost()=" + update.getChannelPost());
        if (update.getInlineQuery() != null) logger.info("19 update.getInlineQuery()=" + update.getInlineQuery());

        if (update.hasMessage()) {
            //logger.info("update.getMessage()...");
            if (update.getMessage().hasText())
                logger.info("21 update.getMessage().hasText()=" + update.getMessage().hasText());
//            logger.info("21_");
            if (update.getMessage().hasPhoto())
                logger.info("22 update.getMessage().hasPhoto()=" + update.getMessage().hasPhoto());
//            logger.info("22_");
            if (update.getMessage().hasPoll())
                logger.info("23 update.getMessage().hasPoll()=" + update.getMessage().hasPoll());
//            logger.info("23_");
            if (update.getMessage().hasReplyMarkup())
                logger.info("24 update.getMessage().hasReplyMarkup()=" + update.getMessage().hasReplyMarkup());
//            logger.info("24_");
            if (update.getMessage().hasDocument())
                logger.info("25 update.getMessage().hasDocument()=" + update.getMessage().hasDocument());
//            logger.info("25_");
            if (update.getMessage().getCaption() != null)
                logger.info("26 update.getMessage().getCaption()=" + update.getMessage().getCaption());
//            logger.info("26_");
            if (update.getMessage().getText() != null)
                logger.info("27 update.getMessage().getText()=" + update.getMessage().getText());
//            logger.info("27_");
            if (update.getMessage().getChatId() != null)
                logger.info("28 update.getMessage().getChatId()=" + update.getMessage().getChatId());
//            logger.info("28_");
            if (update.getMessage().getNewChatMembers() != null && update.getMessage().getNewChatMembers().size() > 0)
                logger.info("29 update.getMessage().getNewChatMembers()=" + update.getMessage().getNewChatMembers());
//            logger.info("29_");
            if (update.getMessage().getPhoto() != null)
                logger.info("30 update.getMessage().getPhoto()=" + update.getMessage().getPhoto());
//            logger.info("30_");
//        if (update.getMessage().getChat() != null)
//            logger.info("update.getMessage().getChat()=" + update.getMessage().getChat());
//        if (update.getMessage().getChat() != null) {
//            getFieldsOfObject2(true,"", update.getMessage().getChat().toString());
//        }
        }

        logger.info("--------------------------------------------");

    }


    @Override
    public String getBotUsername() {
        return "Льготные ЛС 74";
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public static void main(String[] args) {
        logger.info("= new BaseTelegramBot()");
        BaseTelegramBot btb = new BaseTelegramBot("???????????? token");
        //btb.getFieldsOfObject("0(2(4)6(8)1)");
//        btb.getFieldsOfObject("0(2=2(4=4)6=6(8=8)1=1)");

//        btb.getFieldsOfObject2(true, "", "Update(a=1, message=Message(b=2, c=null), from=User(d=4), e=5)");
//        btb.getFieldsOfObject2(true,"", "Update(updateId=updateId_1, message=Message(messageId=messageId_, from=User(id=id_, isPremium=null), date=1685089692))");

        btb.getFieldsOfObject2(true,"", "Update(updateId=605381610, message=Message(messageId=86, messageThreadId=null, from=User(id=5609291842, firstName=Pavel, isPremium=null), date=1685089692, chat=Chat(id=5609291842, type=private, hasHiddenMembers=null), forwardFrom=null, text=777, entities=null, newChatMembers=[], chatShared=null), inlineQuery=null, chatJoinRequest=null)", 0);
        btb.getFieldsOfObject2(true,"", "Update(updateId=605381926, message=null, inlineQuery=null, chosenInlineQuery=null, callbackQuery=CallbackQuery(id=5644980945119524582, from=User(id=5609291842, firstName=Pavel, isBot=false, lastName=Kalinin, userName=null, languageCode=ru, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null), message=Message(messageId=610, messageThreadId=null, from=User(id=5966855055, firstName=Льгота74ЛС_Робот, isBot=true, lastName=null, userName=lgota74ls_robot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null), date=1687845415, chat=Chat(id=5609291842, type=private, title=null, firstName=Pavel, lastName=Kalinin, userName=null, photo=null, description=null, inviteLink=null, pinnedMessage=null, stickerSetName=null, canSetStickerSet=null, permissions=null, slowModeDelay=null, bio=null, linkedChatId=null, location=null, messageAutoDeleteTime=null, hasPrivateForwards=null, HasProtectedContent=null, joinToSendMessages=null, joinByRequest=null, hasRestrictedVoiceAndVideoMessages=null, isForum=null, activeUsernames=null, emojiStatusCustomEmojiId=null, hasAggressiveAntiSpamEnabled=null, hasHiddenMembers=null), forwardFrom=null, forwardFromChat=null, forwardDate=null, text=Текст быстрого письма:, entities=null, captionEntities=null, audio=null, document=null, photo=null, sticker=null, video=null, contact=null, location=null, venue=null, animation=null, pinnedMessage=null, newChatMembers=[], leftChatMember=null, newChatTitle=null, newChatPhoto=null, deleteChatPhoto=null, groupchatCreated=null, replyToMessage=null, voice=null, caption=null, superGroupCreated=null, channelChatCreated=null, migrateToChatId=null, migrateFromChatId=null, editDate=null, game=null, forwardFromMessageId=null, invoice=null, successfulPayment=null, videoNote=null, authorSignature=null, forwardSignature=null, mediaGroupId=null, connectedWebsite=null, passportData=null, forwardSenderName=null, poll=null, replyMarkup=InlineKeyboardMarkup(keyboard=[[InlineKeyboardButton(text=Тормозит, url=null, callbackData=Тормозит, callbackGame=null, switchInlineQuery=null, switchInlineQueryCurrentChat=null, pay=null, loginUrl=null, webApp=null)], [InlineKeyboardButton(text=Непонятное сообщение, url=null, callbackData=Непонятное сообщение, callbackGame=null, switchInlineQuery=null, switchInlineQueryCurrentChat=null, pay=null, loginUrl=null, webApp=null)], [InlineKeyboardButton(text=Не вижу программу, url=null, callbackData=Не вижу программу, callbackGame=null, switchInlineQuery=null, switchInlineQueryCurrentChat=null, pay=null, loginUrl=null, webApp=null)], [InlineKeyboardButton(text=Спасибо!, url=null, callbackData=Спасибо!, callbackGame=null, switchInlineQuery=null, switchInlineQueryCurrentChat=null, pay=null, loginUrl=null, webApp=null), InlineKeyboardButton(text=Успехов!, url=null, callbackData=Успехов!, callbackGame=null, switchInlineQuery=null, switchInlineQueryCurrentChat=null, pay=null, loginUrl=null, webApp=null)], [InlineKeyboardButton(text=-, url=null, callbackData=-, callbackGame=null, switchInlineQuery=null, switchInlineQueryCurrentChat=null, pay=null, loginUrl=null, webApp=null)], [InlineKeyboardButton(text=Отправить письмо, url=null, callbackData=Отправить письмо, callbackGame=null, switchInlineQuery=null, switchInlineQueryCurrentChat=null, pay=null, loginUrl=null, webApp=null)], [InlineKeyboardButton(text=--, url=null, callbackData=--, callbackGame=null, switchInlineQuery=null, switchInlineQueryCurrentChat=null, pay=null, loginUrl=null, webApp=null)], [InlineKeyboardButton(text=Очистить и -> МЕНЮ, url=null, callbackData=Очистить и -> МЕНЮ, callbackGame=null, switchInlineQuery=null, switchInlineQueryCurrentChat=null, pay=null, loginUrl=null, webApp=null)]]), dice=null, viaBot=null, senderChat=null, proximityAlertTriggered=null, messageAutoDeleteTimerChanged=null, isAutomaticForward=null, hasProtectedContent=null, webAppData=null, videoChatStarted=null, videoChatEnded=null, videoChatParticipantsInvited=null, videoChatScheduled=null, isTopicMessage=null, forumTopicCreated=null, forumTopicClosed=null, forumTopicReopened=null, forumTopicEdited=null, generalForumTopicHidden=null, generalForumTopicUnhidden=null, writeAccessAllowed=null, hasMediaSpoiler=null, userShared=null, chatShared=null), inlineMessageId=null, data=Спасибо!, gameShortName=null, chatInstance=-9047062910439390755), editedMessage=null, channelPost=null, editedChannelPost=null, shippingQuery=null, preCheckoutQuery=null, poll=null, pollAnswer=null, myChatMember=null, chatMember=null, chatJoinRequest=null)", 0);

        //        btb.getFieldsOfObject2(true, "", "Chat(id=5609291842, type=private, title=null, firstName=Pavel, lastName=Kalinin, userName=null, photo=null, description=null, inviteLink=null, pinnedMessage=null, stickerSetName=null, canSetStickerSet=null, permissions=null, slowModeDelay=null, bio=null, linkedChatId=null, location=null, messageAutoDeleteTime=null, hasPrivateForwards=null, HasProtectedContent=null, joinToSendMessages=null, joinByRequest=null, hasRestrictedVoiceAndVideoMessages=null, isForum=null, activeUsernames=null, emojiStatusCustomEmojiId=null, hasAggressiveAntiSpamEnabled=null, hasHiddenMembers=null)");


// Update(updateId=605381610, message=Message(messageId=86, messageThreadId=null, from=User(id=5609291842, firstName=Pavel, isPremium=null), date=1685089692, chat=Chat(id=5609291842, type=private, hasHiddenMembers=null), forwardFrom=null, text=777, entities=null, newChatMembers=[], chatShared=null), inlineQuery=null, chatJoinRequest=null)
// , from=User(id=5609291842, firstName=Pavel, isPremium=null)
// , chat=Chat(id=5609291842, type=private, hasHiddenMembers=null)
// , message=Message(messageId=86, messageThreadId=null, date=1685089692, forwardFrom=null, text=777, entities=null, newChatMembers=[], chatShared=null)
// Update(updateId=605381610, inlineQuery=null, chatJoinRequest=null)
    }

    public void getFieldsOfObject2(boolean first, String shift, String text, int deep) {
        deep++;
        if(deep>10) return;
        //logger.info("->" + text);
        if (text != null) {
            text = text.trim();
        }
        if (text == null || text.length() < 7) {
            logger.info(shift + text.trim());
            return;
        }
        if (first) {
            logger.info("->" + text);
            int index = text.indexOf("(");
            logger.info(text.substring(0, index));
            text = text.substring(index+1, text.length()-1);
        }
        int indexSpace = 0;
        int indexSpaceTemp = 0;
        int indexBeginBrace = -1;
        int indexEnd = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ')
                indexSpaceTemp = i;
            if (text.charAt(i) == '(') {
                indexBeginBrace = i;
                indexSpace = indexSpaceTemp;
            } else if (text.charAt(i) == ')') {
                indexEnd = i;
                break;
            }
        }
        if (indexBeginBrace != -1 && indexEnd != -1) {
            String capture = text.substring(indexSpace, indexBeginBrace);
            if (capture.length() > 0)
                logger.info(shift+ "    " + capture);

            String sub = text.substring(indexBeginBrace + 1, indexEnd);
            getFieldsOfObject2(false, shift + "    ", sub, deep);

            String rest = text.substring(0, indexSpace + 1) + text.substring(indexEnd + 1);
            getFieldsOfObject2(false, shift+"  " ,rest, deep);
        } else {
            String fields = text;
            String[] ss = fields.split(",");
            StringBuilder sb = new StringBuilder();
            for (String s : ss) {
                String[] ss2 = s.trim().split("=");
                //logger.info("ss2 "+ss2.length + " |"+ss2[0]+"|");
                if ( !ss2[0].isBlank() && !ss2[0].equals("]") && !(ss2.length > 1 && ss2[1].contains("null") ) ) {
                    //logger.info(shift + "    " + s.trim());
                    sb.append(s.trim()).append("; ");
                }
            }
            logger.info(shift + "    " + sb);
        }
    }

}

