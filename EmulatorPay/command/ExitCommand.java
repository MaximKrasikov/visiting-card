package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;
import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Admin on 23.09.2018.
 */
class ExitCommand implements Command {
/*Метод execute() класса ExitCommand должен вызывать метод writeMessage() у ConsoleHelper.*/
private ResourceBundle res= ResourceBundle.getBundle(CashMachine.RESOURCE_PATH+"exit_en");
    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("exit.question.y.n"));
        try {
            String s = ConsoleHelper.readString().toLowerCase();
            if (s.equalsIgnoreCase("y")) {
                ConsoleHelper.writeMessage(res.getString("thank.message"));
            }
        } catch (InterruptOperationException e) {
            throw new InterruptOperationException ();
        }
    }
}
