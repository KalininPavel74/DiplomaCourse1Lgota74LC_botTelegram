package bot.interfaces;

public interface IFactoryControl {
    IControl create(IView view, IModel model, IMail mail, String userAddress, IDBSystem dbSystem);
}
