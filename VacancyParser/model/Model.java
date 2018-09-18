package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.view.View;
import com.javarush.task.task28.task2810.vo.Vacancy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17.09.2018.
 */
public class Model {
    private View view;
    private Provider [] providers;


    public Model(View view, Provider ... providers) throws IllegalArgumentException {// пробрасывает ошибку с неверным аргуменом при вызове метода
        if (view == null || providers == null) {
            throw new IllegalArgumentException();
        }if (providers.length == 0) {
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.providers = providers;
    }
    //должен получать вакансии с каждого провайдера и передавать в метод update у вью.
    // будет вызываться контроллером
    public void selectCity(String city){
        /* получить вакансии с каждого провайдера,
           обновить вью списком вакансий,т.к. он принимает решение, какую модель использовать.*/
        List<Vacancy> vacancyList= new ArrayList<>();
        for(Provider provider:providers){
            vacancyList.addAll(provider.getJavaVacancies(city));
        }
        view.update(vacancyList);
    }
}
