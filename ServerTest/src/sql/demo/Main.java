package sql.demo;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*Для имитации множественного обращения клиентов к серверу,
 создадим и запустим (после запуска серверной части)
фабрику Runnable клиентов которые будут подключаться серверу и писать сообщения в цикле:

 Имитация множественного обращения клиентов к серверу.*/
public class Main {

    // private static ServerSocket server;

    public static void main(String[] args) throws IOException, InterruptedException {

        // запустим пул нитей в которых колличество возможных нитей ограничено -
        // 10-ю.
        ExecutorService exec = Executors.newFixedThreadPool(10);
        int j = 0;

        // стартуем цикл в котором с паузой в 10 милисекунд стартуем Runnable
        // клиентов,
        // которые пишут какое-то количество сообщений

        // запустили 10 клиентов, так сказать на скотобазу
        while (j < 10) {
            j++;
            exec.execute(new TestRunnableClientTester());
            Thread.sleep(10);
        }

        // закрываем фабрику
        exec.shutdown();
    }
}