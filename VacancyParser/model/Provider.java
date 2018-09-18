package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 16.09.2018.
 */
/*Этот класс будет обобщать способ получения данных о вакансиях.*/
public class Provider {
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
    /*будет возвращать список вакансий.*/
    public List<Vacancy> getJavaVacancies(String searchString){
        return  strategy.getVacancies(searchString);
    }
    Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }
}
