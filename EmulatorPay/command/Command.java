package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.exception.InterruptOperationException;
import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.io.IOException;

/**
 * Created by Admin on 23.09.2018.
 */
 interface Command {
    void execute() throws InterruptOperationException;
}
