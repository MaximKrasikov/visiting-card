package com.javarush.task.task26.task2613;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 21.09.2018.
 */
/*фабрика для валют*/
public final class CurrencyManipulatorFactory {
    private static Map<String, CurrencyManipulator> currency = new HashMap<>();

    private CurrencyManipulatorFactory() {}

    public static CurrencyManipulator getManipulatorByCurrencyCode(String currencyCode) {
        if (!currency.containsKey(currencyCode))
            currency.put(currencyCode, new CurrencyManipulator(currencyCode));

        return currency.get(currencyCode);
    }

    public static Collection<CurrencyManipulator> getAllCurrencyManipulators() {
        return currency.values();
    }
}