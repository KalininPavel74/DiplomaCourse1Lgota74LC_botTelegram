package bot.model;

import bot.interfaces.IFactoryModel;
import bot.interfaces.IModel;

public class FactoryModel implements IFactoryModel {
    public IModel create() {
        return new Model();
    }
}
