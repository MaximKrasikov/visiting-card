package com.javarush.task.task28.task2810;

import com.javarush.task.task28.task2810.model.HHStrategy;
import com.javarush.task.task28.task2810.model.Model;
import com.javarush.task.task28.task2810.model.Provider;
import com.javarush.task.task28.task2810.vo.Vacancy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 16.09.2018.
 */
/* в нем будет содержаться бизнес логика.*/
public class Controller {

    private Model model;


    public Controller(Model model) {
        if(model==null){
            throw new IllegalArgumentException();
        }
        this.model = model;
    }
    /*вызов  нужного метода модели.*/
    public void onCitySelect(String cityName){
        model.selectCity(cityName);
    }
}
