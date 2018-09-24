package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulator;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;
import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.util.ResourceBundle;

/**
 * Created by Admin on 23.09.2018.
 */
 class InfoCommand implements Command {
    /*для каждого манипулятора выведи: "код валюты - общая сумма денег для выбранной валюты",
     если денег нет в банкомате выведи фразу,
     "No money available.".*/
    private ResourceBundle res= ResourceBundle.getBundle(CashMachine.RESOURCE_PATH+"info_en");
    @Override
    public void execute() throws InterruptOperationException{
        ConsoleHelper.writeMessage(res.getString("before"));
        boolean money = false;

        for (CurrencyManipulator currency : CurrencyManipulatorFactory.getAllCurrencyManipulators())
            if (currency.hasMoney()) {
                if (currency.getTotalAmount() > 0) {
                    ConsoleHelper.writeMessage(currency.getCurrencyCode() + " - " + currency.getTotalAmount());
                    money = true;
                }
            }

        if (!money)
            ConsoleHelper.writeMessage(res.getString("no.money"));
    }
}