package server;

import java.io.IOException;
import java.net.Socket;

import core.communication.MessageReader;
import core.communication.MessageReader.UniqueMessage;
import core.communication.MessageWriter;
import core.requests.*;
import core.responses.*;

//Основная логика клиента
/*описан основной алгоритм работы с клиентом , обмен сообщениями , данными */
public class ClientSession extends Thread {
    private final Socket socket;
    private final MessageReader reader;
    private final MessageWriter writer;
    private final Context context;

    public ClientSession(final Socket socket, final Context context) throws IOException {
        this.socket = socket;
        this.reader = new MessageReader(socket.getInputStream());
        this.writer = new MessageWriter(socket.getOutputStream());
        this.context = context;
    }

    public void run() {
        UniqueMessage msg;
        try {
            msg = reader.readMessage();

            //Рукопожатие
            if(msg.message instanceof HandshakeRequest) {
                if(((HandshakeRequest)msg.message).match()) {
                    writer.writeResponse(new HandshakeResponse(), msg.uniqueId);
                }
            }

            //Обменялись рукопожатиями, начинаем работу
            this.doWork();

            //выход
            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // передача управления парсеру
    private void doWork() {}
}
