package bot.interfaces;

public interface IUser {
    Long getChatId();
    String getName();
    void setName(String name);

    IModelMail getModelMail();
    IModelInfo getModelInfo();
    Integer getWaitMessageId();
    void setWaitMessageId(Integer waitMessageId);
    long getBusyUntilTelegram();
    void setBusyUntilTelegram(long busyUntilTelegram);
    long getBusyUntilThirdParty();
    void setBusyUntilThirdParty(long busyUntilThirdParty);

    boolean isInit();

    void setIsInit();

    String getChatIdAndName();
}
