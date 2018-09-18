package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;

import java.util.List;

/**
 * Created by Admin on 17.09.2018.
 */
public interface View {
    //передается список вакансий для отображения
    void update(List<Vacancy> vacancies);

    //посылаем контроллеру информацию
    void setController(Controller controller);
}
