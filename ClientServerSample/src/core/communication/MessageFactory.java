package core.communication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import core.IMessage;
import core.requests.*;
import core.responses.*;

public class MessageFactory {
    //Идентификаторы запросов
    public static final int REQUEST_HANDSHAKE = 1;

    //Идентификаторы ответов
    private static final int RESPONSE_BASE = 0x40000000;
    public static final int RESPONSE_HANDSHAKE = RESPONSE_BASE + 1;

    //Ассоциативный массив: класс сообщения => идентификатор сообщения
    private static final Map<Class<? extends IMessage>, Integer> idMap = new HashMap<Class<? extends IMessage>, Integer>();

    static {
        idMap.put(HandshakeRequest.class, REQUEST_HANDSHAKE);

        idMap.put(HandshakeResponse.class, RESPONSE_HANDSHAKE);
    }

    private MessageFactory() {
    }

    //Создает сообщение по идентификатору
    public static IMessage createMessage(int messageId) throws IOException {
        if (messageId > RESPONSE_BASE) {
            switch (messageId) {
                case RESPONSE_HANDSHAKE:
                    return new HandshakeResponse();

                default:
                    throw new IOException("Unknown message type " + messageId);
            }
        } else {
            switch (messageId) {
                case REQUEST_HANDSHAKE:
                    return new HandshakeRequest();

                default:
                    throw new IOException("Unknown message type " + messageId);
            }
        }
    }

    public static int getMessageId(final IMessage message) {
        Integer id = idMap.get(message.getClass());
        return id.intValue();
    }

}
