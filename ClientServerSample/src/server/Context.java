package server;

//Данные, общие для всех клиентских сессий
public class Context {
    private SessionsManager sessinonsManager;
    public boolean stopFlag;
    //Другие важные поля, которые должны знать все клиенты
    //...

    public Context() {
        this.stopFlag = false;
        this.sessinonsManager = new SessionsManager();
    }

    public SessionsManager getSessionsManger() {
        return this.sessinonsManager;
    }
}
