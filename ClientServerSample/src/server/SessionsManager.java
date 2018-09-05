package server;

import java.util.HashSet;
import java.util.Set;

public class SessionsManager {

    private final Set<ClientSession> sessions = new HashSet<ClientSession>();

    public SessionsManager() {}
    // добавление новой сессии- клиента
    public synchronized void addSession(ClientSession session) {
        sessions.add(session);
    }
// удаление сессии
    public synchronized void removeSession(ClientSession session) {
        sessions.remove(session);
    }
}