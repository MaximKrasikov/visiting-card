package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulator;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;
import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Admin on 23.09.2018.
 */
  class DepositCommand implements Command {
  private ResourceBundle res= ResourceBundle.getBundle(CashMachine.RESOURCE_PATH+"deposit_en");
    @Override
    public void execute() throws InterruptOperationException {
      ConsoleHelper.writeMessage(res.getString("before"));
      String currencyCode = null;
      try {
        currencyCode = ConsoleHelper.askCurrencyCode();// спрашиваем код
        CurrencyManipulator currencyManipulator = CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);
        String[] s = ConsoleHelper.getValidTwoDigits(currencyCode);
        int nominal = Integer.parseInt(s[0]);
        int total = Integer.parseInt(s[1]);
        currencyManipulator.addAmount(nominal, total);
        ConsoleHelper.writeMessage(res.getString("success.format"));
      } catch (NumberFormatException e) {
        ConsoleHelper.writeMessage(res.getString("invalid.data"));
      }
    }
}
