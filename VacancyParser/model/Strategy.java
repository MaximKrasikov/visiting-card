package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;

import java.util.List;

/**
 * Created by Admin on 16.09.2018.
 */
/*будет отвечать за получение данных с сайта.*/
public interface Strategy {
    //будет возвращать список вакансий.
    List<Vacancy> getVacancies(String searchString);
}
