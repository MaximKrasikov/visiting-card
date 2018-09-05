package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final int port;
    private Context context;

    public Server() {
        this.port = 5000;
        this.context = new Context();
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(this.port);

            //Цикл ожидания подключений
            while(!this.context.stopFlag) {// если произошло подключение
                System.out.println("Waiting connection on port:" + this.port);
                //Момент ухода в ожидание подключения
                Socket clientSocket = ss.accept();
                System.out.println("New client connected to server");

                //Создается клиентская сессия
                ClientSession clientSession = new ClientSession(clientSocket, this.context);
                this.context.getSessionsManger().addSession(clientSession);
                //Запуск логики работы с клиентом
                clientSession.start();
            }

            ss.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
