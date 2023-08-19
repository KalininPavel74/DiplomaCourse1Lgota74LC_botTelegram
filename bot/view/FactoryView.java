package bot.view;

import bot.interfaces.IControl;
import bot.interfaces.IFactoryView;
import bot.interfaces.IView;

public class FactoryView implements IFactoryView {
    public IView create(String token) {
        return new View(token);
    }
}
