package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 17.09.2018.
 */
public class HtmlView implements View {
    private Controller controller;
    // путь к файлу с vac
    private final String filePath = "./4.JavaCollections/src/" + this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";

    /*
    * 1.1. сформируй новое тело файла vacancies.html, которое будет содержать вакансии,
        1.2. запиши в файл vacancies.html его обновленное тело,
        1.3. Все исключения должны обрабатываться в этом методе - выведи стек-трейс, если возникнет исключение.*/
    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            updateFile(getUpdatedFileContent(vacancies));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Some exception");
        }
    }
    protected org.jsoup.nodes.Document getDocument()  throws IOException {
        return Jsoup.parse(new File(filePath), "UTF-8");
    }

    // запись в файл html
    private void updateFile(String content) {
        /*принимает тело файла в виде строки. Нужно его записать в файл, который находится по пути filePath.*/

        /*открываем поток для записи в файл*/
        try {
            FileWriter fileWriter = new FileWriter(new File(filePath));
            fileWriter.write(content);// пишем в файл
            fileWriter.close();// закрываем поток
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
    // эмулятор событий пользователя
    public void userCitySelectEmulationMethod() {
        //проброс вызова в контроллер для конкретного города
        controller.onCitySelect("Moscow");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        /*
5. Верни html код всего документа в качестве результата работы метода.

6. В случае возникновения исключения, выведи его стек-трейс и верни строку "Some exception occurred".

7. Запусти приложение, убедись, что все вакансии пишутся в файл vacancies.html.*/
        Document document = null;
        try {
            document = getDocument();


            //Получили элемент, у которого есть класс template.
            Element templateOriginal = document.getElementsByClass("template").first();
            //сделаем  копию этого объекта.
            Element copyTemplate = templateOriginal.clone();
            //удалим из нее атрибут "style" и класс "template"
            copyTemplate.removeAttr("style");
            copyTemplate.removeClass("template");

             /*удаляем  все добавленные ранее вакансии. У них единственный класс "vacancy".
                В файле backup.html это одна вакансия - Junior Java Developer.
                Нужно удалить все теги tr, у которых class="vacancy".
                Но тег tr, у которого class="vacancy template", не удалять.
                Использем метод remove.*/
            document.select("tr[class=vacancy]").remove().not("tr[class=vacancy template");

            //Используем этот элемент в качестве шаблона для добавления новой строки в таблицу вакансий

            for (Vacancy vacancy : vacancies) {
                Element localClone = copyTemplate.clone();// клонируем шаблон тега
                localClone.getElementsByClass("city").first().text(vacancy.getCity());
                localClone.getElementsByClass("companyName").first().text(vacancy.getCompanyName());
                localClone.getElementsByClass("salary").first().text(vacancy.getSalary());

                Element link =localClone.getElementsByTag("a").first();
                link.text(vacancy.getTitle());
                link.attr("href", vacancy.getUrl());

                templateOriginal.before(localClone.outerHtml());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }
        //Возвращаем html код всего документа в качестве результата работы метода.
        return document.html();
    }

}
