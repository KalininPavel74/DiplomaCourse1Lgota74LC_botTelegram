package bot.model;

import bot.interfaces.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Model implements IModel {

    static private Logger logger = Logger.getLogger(Model.class.getName());

    private ArrayList<User> users;

    public Model() {
        this.users = new ArrayList<User>();
    }

    public IUser getUser(Long chatId) {
        for (IUser user : users)
            if (user.getChatId().equals(chatId))
                return user;
        User user = new User(chatId);
        users.add(user);
        return user;
    }
}
