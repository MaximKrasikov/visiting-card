package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;

import java.io.File;
import java.io.FileInputStream;
import java.util.ResourceBundle;

/**
 * Created by Admin on 24.09.2018.
 */
class LoginCommand implements Command {
    private ResourceBundle res= ResourceBundle.getBundle(CashMachine.RESOURCE_PATH+"login_en");
    /*123456789012 и 1234*/
    /*бедись, что поле validCreditCards проинициализировано из файла verifiedCards.properties и содержит ключ '234567890123'.*/
    private ResourceBundle validCreditCards = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "verifiedCards");
    @Override
    public void execute() throws InterruptOperationException {
    ConsoleHelper.writeMessage(res.getString("before"));
        while (true) {
            ConsoleHelper.writeMessage(res.getString("specify.data"));
            String s1 = ConsoleHelper.readString();
            String s2 = ConsoleHelper.readString();
            if (validCreditCards.containsKey(s1)) {
                if (validCreditCards.getString(s1).equals(s2)) {
                    ConsoleHelper.writeMessage(res.getString("success.format"));
                } else {
                    ConsoleHelper.writeMessage(res.getString("try.again.with.details"));
                    ConsoleHelper.writeMessage(res.getString("try.again.or.exit"));
                    continue;
                }
            } else {
                ConsoleHelper.writeMessage(res.getString("not.verified.format"));
                ConsoleHelper.writeMessage(res.getString("try.again.or.exit"));
                continue;
            }
            break;
        }
    }
}
