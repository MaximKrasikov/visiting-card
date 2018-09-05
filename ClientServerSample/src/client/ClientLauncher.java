package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientLauncher {
    public static void main(String[] args) {
        try {
            InetAddress host = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);

            //System.out.println(id);
            Client client = new Client(host, port);
            //Запускаем логику клиента
            client.start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
