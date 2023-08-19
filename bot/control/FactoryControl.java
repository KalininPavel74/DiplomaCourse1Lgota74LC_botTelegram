package bot.control;

import bot.interfaces.*;

public class FactoryControl implements IFactoryControl {
    public IControl create(IView view, IModel model, IMail mail, String userAddress, IDBSystem dbSystem) {
        return new Control(view, model, mail, userAddress, dbSystem);
    }
}
