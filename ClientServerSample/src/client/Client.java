package client;

import core.communication.MessageReader;
import core.communication.MessageReader.UniqueMessage;
import core.communication.MessageWriter;
import core.requests.HandshakeRequest;
import core.responses.HandshakeResponse;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final InetAddress host;
    private final int port;

    public Client(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    //Создает сокет, ридер райтер и запускает логику
    public void start() {
        //Создаем клиентский сокет
        try (Socket socket = new Socket(this.host, this.port)) {
            //Создаем ридер и райтер для обмена сообщениями
            MessageReader reader = new MessageReader(socket.getInputStream());
            MessageWriter writer = new MessageWriter(socket.getOutputStream());

            //Шлем серверу первое сообщение "рукопожатие"
            writer.writeRequest(new HandshakeRequest());
            //Получаем ответ
            UniqueMessage msg = reader.readMessage();
            //Проверяем, что это ответ на рукопожатие
            if(!(msg.message instanceof HandshakeResponse)) {// если нет ответа
                return;
            }

            //Запуск логики приложения
            this.logicStart();

            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        // тут будет запуск парсера
    public void logicStart() {

        //Логика приложения
        //.....
        // протокол обмена сообщениями с сервером, передачу каких-либо данных
    }
}
