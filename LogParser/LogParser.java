package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.*;
import com.sun.prism.impl.Disposer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.ErrorManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
    private Path logDir;
    private List<Record> records;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    /*должен возвращать всех пользователей.*/
    @Override
    public Set<String> getAllUsers() {
        Set<String> allUsers= new HashSet<>();
        for(Record line: records){
            allUsers.add(line.user);
        }
        return allUsers;
    }

    /*должен возвращать количество уникальных пользователей*/
    @Override
    public int getNumberOfUsers(Date after, Date before) {
        Set<Record> filteredRecords = getFilteredEntries(after, before);
        Set<String> users = new HashSet<>();

        for (Record record : filteredRecords) {
            users.add(record.user);
        }

        return users.size();
    }
    /*должен возвращать количество событий от определенного пользователя.*/
    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<Record> filteredRecords = getFilteredEntries(after, before);
        Set<Event> userEvents= new HashSet<>();
        for(Record record: filteredRecords){
            if(user.equals(record.user)) {
                userEvents.add(record.event);
            }
        }
        return userEvents.size();
    }
    //должен возвращать пользователей с определенным IP.Несколько пользователей могут использовать один и тот же IP.
    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> userIp= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line:filteredRecords){
            if(ip.equals(line.ip)){
                userIp.add(line.user);
            }
        }
        return userIp;
    }
    /*должен возвращать пользователей, которые были залогинены.*/
    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> loggedUsers= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after,before);
        for(Record line: filteredRecords){
            if(Event.LOGIN.equals(line.event) ){
                loggedUsers.add(line.user);
            }
        }

        return loggedUsers;
    }
    //должен возвращать пользователей, которые скачали плагин.
    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> usersPluginDownladed= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after,before);
        for(Record line: filteredRecords){
            if(Event.DOWNLOAD_PLUGIN.equals(line.event)){
                usersPluginDownladed.add(line.user);
            }
        }
        return usersPluginDownladed;
    }
    //должен возвращать пользователей, которые отправили сообщение.
    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> usersWroteMessage= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after,before);
        for(Record line: filteredRecords){
            if(Event.WRITE_MESSAGE.equals(line.event)){
                usersWroteMessage.add(line.user);
            }
        }
        return usersWroteMessage;
    }
    //должен возвращать пользователей, которые решали любую задачу.
    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> usersSolvedTask= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after,before);
        for(Record line: filteredRecords){
            if(Event.SOLVE_TASK.equals(line.event)){
                usersSolvedTask.add(line.user);
            }
        }
        return usersSolvedTask;
    }
    //должен возвращать пользователей, которые решали задачу с номером task.
    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> usersSolvedTask= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after,before);
        for(Record line: filteredRecords){
            if(Event.SOLVE_TASK.equals(line.event) && line.taskNumber.equals(task) ){
                usersSolvedTask.add(line.user);
            }
        }
        return usersSolvedTask;
    }
    //должен возвращать пользователей, которые решали любую задачу.
    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String>usersDoneTask= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(Event.DONE_TASK.equals(line.event)){
                usersDoneTask.add(line.user);
            }
        }
        return usersDoneTask;
    }
    //должен возвращать пользователей, которые решали задачу с номером task.
    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        Set<String>usersDoneTask= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(Event.DONE_TASK.equals(line.event) && line.taskNumber.equals(task)){
                usersDoneTask.add(line.user);
            }
        }
        return usersDoneTask;
    }

    //должен возвращать даты, когда определенный пользователь произвел определенное событие.
    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        Set<Date>datesForUserAndEvent= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(line.user.equals(user) && line.event.equals(event)){
                datesForUserAndEvent.add(line.date);
            }
        }

        return datesForUserAndEvent;
    }
    //должен возвращать даты, когда любое событие не выполнилось (статус FAILED).
    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        Set<Date>datesWhenSomethingFailed= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(Status.FAILED.equals(line.status)){
                datesWhenSomethingFailed.add(line.date);
            }
        }
        return datesWhenSomethingFailed;
    }
    //должен возвращать даты, когда любое событие закончилось ошибкой (статус ERROR).
    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        Set<Date>datesWhenErrorHappened= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(Status.ERROR.equals(line.status)){
                datesWhenErrorHappened.add(line.date);
            }
        }
        return datesWhenErrorHappened;
    }
    //должен возвращать дату, когда пользователь залогинился впервые за указанный период. Если такой даты в логах нет - null.
    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        Date datesWhenUserLoggedFirstTime = new Date(Long.MAX_VALUE);
        boolean isDateChanged = false;
        Set<Record> filteredRecords= getFilteredEntries(after, before);// берем отсортированный упорядоченный промежуток
        for(Record line: filteredRecords){
            if(Event.LOGIN.equals(line.event) && line.user.equals(user) && line.date.getTime()<datesWhenUserLoggedFirstTime.getTime() &&Status.OK.equals(line.status)){
                    datesWhenUserLoggedFirstTime = line.date;
                    isDateChanged= true;
            }
        }
        return isDateChanged ? datesWhenUserLoggedFirstTime : null;
    }
    //должен возвращать дату, когда пользователь впервые попытался решить определенную задачу. Если такой даты в логах нет - null.
    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        Date datesWhenUserSolvedTask= new Date(Long.MAX_VALUE);
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        boolean isSolved=false;
        for(Record line: filteredRecords){
            if(Event.SOLVE_TASK.equals(line.event) && line.user.equals(user)&&line.taskNumber.equals(task) && line.date.getTime()<datesWhenUserSolvedTask.getTime()){
                datesWhenUserSolvedTask= line.date;
                isSolved= true;
            }
        }
        return isSolved? datesWhenUserSolvedTask: null;
    }
    // должен возвращать дату, когда пользователь впервые решил определенную задачу. Если такой даты в логах нет - null.
    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        Date datesWhenUserDoneTask= new Date(Long.MAX_VALUE);
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        boolean isDone=false;
        for(Record line: filteredRecords){
            if(Event.DONE_TASK.equals(line.event)&&line.taskNumber.equals(task) && line.user.equals(user) &&line.date.getTime()<datesWhenUserDoneTask.getTime()){
                datesWhenUserDoneTask= line.date;
                isDone= true;
            }
        }
        return isDone?datesWhenUserDoneTask:null;
    }
    //должен возвращать даты, когда пользователь написал сообщение.
    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        Set<Date>datesWhenUserWroteMessage= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(Event.WRITE_MESSAGE.equals(line.event) && line.user.equals(user)){
                datesWhenUserWroteMessage.add(line.date);
            }
        }
        return datesWhenUserWroteMessage;
    }
    //должен возвращать даты, когда пользователь скачал плагин.
    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        Set<Date>datesWhenUserWhenDownloadedPlugin= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(Event.DOWNLOAD_PLUGIN.equals(line.event)&& line.user.equals(user)){
                datesWhenUserWhenDownloadedPlugin.add(line.date);
            }
        }
        return datesWhenUserWhenDownloadedPlugin;
    }
    // должен возвращать количество событий за указанный период.
    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        Set<Event> eventsNumber= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            eventsNumber.add(line.event);
        }
        return eventsNumber.size();
    }

    // должен возвращать события, которые происходили с указанного IP.
    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        Set<Event> eventsForIp= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(line.ip.equals(ip))
            eventsForIp.add(line.event);
        }
        return eventsForIp;

    }
    //должен возвращать события, которые инициировал  определенный пользователь.
    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        Set<Event> eventsForUser= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(line.user.equals(user))
                eventsForUser.add(line.event);
        }
        return eventsForUser;
    }
    //должен возвращать события, которые не выполнились.
    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        Set<Event> eventsFailed= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(Status.FAILED.equals(line.status))
                eventsFailed.add(line.event);
        }
        return eventsFailed;
    }
    //должен возвращать события, которые завершились ошибкой.
    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        Set<Event> eventsError= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            if(Status.ERROR.equals(line.status))
                eventsError.add(line.event);
        }
        return eventsError;
    }
    //должен возвращать количество попыток решить определенную задачу.
    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
       // Set<Event> eventsNumberOfAttemptToSolveTask= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        int count=0;
        for(Record line: filteredRecords){
            if (isBetween(line.date,after,before) &&  line.event == Event.SOLVE_TASK && line.taskNumber ==task ) {
                count++;
                //eventsNumberOfAttemptToSolveTask.add(line.event);
            }
        }
        return count;
    }
    //должен возвращать количество успешных решений определенной задачи.
    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
       // Set<Event> eventsNumberOfSuccessfulAttemptToSolveTask= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        int count=0;
        for(Record line: filteredRecords){
            if(isBetween(line.date,after,before) && line.event == Event.DONE_TASK  && line.taskNumber == task )
                count++;
               // eventsNumberOfSuccessfulAttemptToSolveTask.add(line.event);
        }
        return count;
    }
    //должен возвращать мапу (номер_задачи :количество_попыток_решить_ее).
    //необходимо подсчитать количество попыток решить задачу за определенный период
    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        Map<Integer,Integer> allSolvedTasksAndTheirNumber= new HashMap<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);// смотрим промежуток
        int count;
        for(Record line: filteredRecords){
            if (Event.SOLVE_TASK.equals(line.event)) {
                if(allSolvedTasksAndTheirNumber.containsKey(line.taskNumber)) {
                    count = allSolvedTasksAndTheirNumber.get(line.taskNumber) + 1;
                    allSolvedTasksAndTheirNumber.put(line.taskNumber, count);
                }else{
                    allSolvedTasksAndTheirNumber.put(line.taskNumber,1);
                }
            }
        }
        return allSolvedTasksAndTheirNumber;
    }
    //должен возвращать мапу (номер_задачи :сколько_раз_ее_решили).
    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        Map<Integer,Integer> allDoneTasksAndTheirNumber= new HashMap<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);// смотрим промежуток
        int count;
        for(Record line: filteredRecords){
            if (Event.DONE_TASK.equals(line.event)) {
                if(allDoneTasksAndTheirNumber.containsKey(line.taskNumber)) {
                    count =allDoneTasksAndTheirNumber.get(line.taskNumber) + 1;
                    allDoneTasksAndTheirNumber.put(line.taskNumber, count);
                }else{
                    allDoneTasksAndTheirNumber.put(line.taskNumber,1);
                }
            }
        }
        return allDoneTasksAndTheirNumber;
    }
    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        Set<Event> eventsAll= new HashSet<>();
        Set<Record> filteredRecords= getFilteredEntries(after, before);
        for(Record line: filteredRecords){
            eventsAll.add(line.event);
        }
        return eventsAll;
    }

   /*Общий формат запроса:
      get field1 for field2 = "value1" and date between "after" and "before"*/
    //get ip for user = "Eduard Petrovich Morozko" and date between "11.12.2013 0:00:00" and "03.01.2014 23:59:59".
    @Override
    public Set<Object> execute(String query) {
        String[] params = queryParser(query);
        return getQuery(params[0], params[1], params[2],params[3],params[4]);
    }

    private String[] queryParser(String query) {

        //get ip for user="Amigo"
        String[] result = new String[5];
        String str = query.substring(query.indexOf(' ') + 1);
        if (query.indexOf("for") > 0) {
            result[0] = str.substring(0, str.indexOf(' '));//ip
            str = query.substring(query.indexOf("for") + 4);
            result[1] = str.substring(0, str.indexOf(' '));//user
            str = query.substring(query.indexOf(" = ") + 4);
            result[2] = str.substring(0, str.indexOf('"'));//Amigo
            //get ip for user = "Eduard Petrovich Morozko" and date between "11.12.2013 0:00:00" and "03.01.2014 23:59:59".
            if(query.indexOf("and")>0){
                str= query.substring(query.indexOf("between")+9);
                result[3]= ((str.substring(0,str.indexOf('"'))));
                str=query.substring(query.indexOf("between")+34);
                result[4]=str.substring(0,str.indexOf('"'));
            }
        }
        else {
            result[0] = str;
        }
        return result;
    }

    private Set<Object> getQuery(String field1, String field2, String value1, String dateAdvanceFrom, String dateAdvanceTo) {
        Set<Object> results = new HashSet<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(logDir, "*.log")) {
            for (Path path : directoryStream) {
                BufferedReader fileReader = new BufferedReader(new FileReader(path.toString()));
                while (fileReader.ready()) {
                    String str = fileReader.readLine();
                    Map<String, String> data = getDataFromString(str);
                    // информация из лога о пользователе
                    String ipFromData = data.get("ip");
                    String userFromData = data.get("user");
                    Date date = parseDate(data.get("date"));
                    String eventFromData = data.get("event");
                    String statusFromData = data.get("status");


                    //String taskFromData = data.get("task");
                    if (    field2 == null && value1 == null
                            || field2.equals("ip") && value1.equals(ipFromData)
                            || field2.equals("user") && value1.equals(userFromData)
                            || field2.equals("date") && parseDate(value1).equals(date)
                            || field2.equals("event") && value1.equals(eventFromData)
                            || field2.equals("status") && value1.equals(statusFromData)
                            && (dateAdvanceFrom==null && dateAdvanceTo==null)// если нет дополнительного условия
                            ) {
                        switch (field1) {
                            case "ip" :
                                    results.add(ipFromData);
                                break;
                            case "user" :
                                results.add(userFromData);
                                break;
                            case "date" :
                                results.add(date);
                                break;
                            case "event" :
                                results.add(Event.valueOf(eventFromData));
                                break;
                            case "status" :
                                results.add(Status.valueOf(statusFromData));
                                break;
                        }
                    }else if((dateAdvanceFrom!=null && dateAdvanceTo!=null)){// если нет дополнительного условия)
                        switch (field1) {
                            case "ip":
                                if (isBetween(date, parseDate(dateAdvanceFrom), parseDate(dateAdvanceTo))) {

                                    results.add(ipFromData);
                                }
                                break;
                            case "user":
                                if (isBetween(date, parseDate(dateAdvanceFrom), parseDate(dateAdvanceTo))) {

                                    results.add(userFromData);
                                }
                                break;
                            case "date":
                                if (isBetween(date, parseDate(dateAdvanceFrom), parseDate(dateAdvanceTo))) {

                                    results.add(date);
                                }
                                break;
                            case "event":
                                if (isBetween(date, parseDate(dateAdvanceFrom), parseDate(dateAdvanceTo))) {

                                    results.add(Event.valueOf(eventFromData));
                                }
                                break;
                            case "status":
                                if (isBetween(date, parseDate(dateAdvanceFrom), parseDate(dateAdvanceTo))) {

                                    results.add(Status.valueOf(statusFromData));
                                }
                                break;
                        }
                    }
                }
                fileReader.close();
                path.getFileName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    //считывает информацию с файла лога о всех пользователях
    private Map<String, String> getDataFromString(String s) {
        Map<String, String> result = new HashMap<>();

        String ip = s.substring(0, s.indexOf('\t'));
        result.put("ip", ip);
        String str = s.substring(s.indexOf('\t')+1);

        String user = str.substring(0, str.indexOf('\t'));
        result.put("user", user);
        str = str.substring(str.indexOf('\t')+1);

        String date = str.substring(0, str.indexOf('\t'));
        result.put("date", date);
        str = str.substring(str.indexOf('\t')+1);

        String event = str.substring(0, str.indexOf('\t'));
        if (event.indexOf(' ') > 0) {
            String taskNumber = event.substring(event.indexOf(' ')+1);
            event = event.substring(0, event.indexOf(' '));
            result.put("task", taskNumber);
        }
        result.put("event", event);
        str = str.substring(str.indexOf('\t')+1);

        String status = str;
        result.put("status", status);

        return result;
    }

    //парсинг даты
    private Date parseDate(String s) {
        String string = s;
        int date = Integer.parseInt(string.substring(0, string.indexOf('.')));
        string = string.substring(string.indexOf('.')+1);
        int month = Integer.parseInt(string.substring(0, string.indexOf('.'))) - 1;
        string = string.substring(string.indexOf('.')+1);
        int year = Integer.parseInt(string.substring(0, string.indexOf(' ')));
        string = string.substring(string.indexOf(' ')+1);
        int hrs = Integer.parseInt(string.substring(0, string.indexOf(':')));
        string = string.substring(string.indexOf(':')+1);
        int min = Integer.parseInt(string.substring(0, string.indexOf(':')));
        string = string.substring(string.indexOf(':')+1);
        int sec = Integer.parseInt(string);
        return (new GregorianCalendar(year, month, date, hrs, min, sec)).getTime();
    }

    private class Record{
        /*набор данных , представленный в логе*/
        String ip;
        String user;
        Date date;
        Event event;
        Integer taskNumber;
        Status status;

        @Override
        public String toString() {
            return "Record{" +
                    "ip='" + ip + '\'' +
                    ", user='" + user + '\'' +
                    ", date=" + date +
                    ", event=" + event +
                    ", taskNumber=" + taskNumber +
                    ", status=" + status +
                    '}';
        }
    }

    public LogParser(Path logDir) {
        records= new ArrayList<>();/*набор данных, представленных в логе*/
        this.logDir = logDir;
        readRecords(logDir);// сразу прочитаем информацию из директории при загрузки класса

    }
    /*метод для чтения данных из файла*/
    private void readRecords(Path logDir){
            try(DirectoryStream<Path> directoryStream= Files.newDirectoryStream(logDir)){
                for(Path log: directoryStream){/*просматриваем каждый файл в директории*/
                    if(Files.isRegularFile(log) && log.toString().endsWith(".log")){// если файл оканчивается на лог
                        BufferedReader reader= Files.newBufferedReader(log, StandardCharsets.UTF_8);
                        while (reader.ready()){
                            Record record= new Record();
                            String[] entry= reader.readLine().split("\t");
                            record.ip= entry[0];
                            record.user= entry[1];
                            record.date= formatter.parse(entry[2]);

                            if(entry[3].indexOf(' ')==-1){// если нет свойства event то
                                record.event= Event.valueOf(entry[3]);
                                record.taskNumber= null;
                            }else {
                                String[] event= entry[3].split(" ");
                                record.event= Event.valueOf(event[0]);
                                record.taskNumber= Integer.parseInt(event[1]);
                            }
                            record.status= Status.valueOf(entry[4]);
                            records.add(record);
                        }
                        reader.close();
                    }else{/*если у файла нет расширения log*/
                        if(Files.isDirectory(log)){//если сам файл является директорией
                            readRecords(log);// считать информацию с каждого файла в директории
                        }
                    }
                }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    /*должен возвращать количество уникальных IP адресов за выбранный период. */
    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        Set<String> ips = getUniqueIPs(after, before);
        return ips.size();
    }

    /*должен возвращать множество, содержащее все не повторяющиеся IP. Тип в котором будем хранить IP будет String.*/
    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        Set<Record> filteredRecors= getFilteredEntries(after,before);// отсортированные записи
        Set<String> ips = new HashSet<>();

        for(Record record: filteredRecors){
            ips.add(record.ip);
        }

        return ips;
    }

    private Set<Record> getFilteredEntries(Date after, Date before) {
        Set<Record> filteredRecords= new HashSet<>();
        for(Record record:records){
            if(isBetween(record.date,after,before)){
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }
/*Если параметр after равен null, то нужно обработать все записи, у которых дата меньше или равна before.
Если параметр before равен null, то нужно обработать все записи, у которых дата больше или равна after.
Если и after, и before равны null, то нужно обработать абсолютно все записи (без фильтрации по дате).*/
    private boolean isBetween(Date date, Date after, Date before) {
        return (after == null || date.after(after) ||date.equals(after)) &&
                (before == null || date.before(before) || date.equals(before));
    }

    /*должен возвращать IP, с которых работал переданный пользователь.*/
    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        Set<Record> filteredRecords = getFilteredEntries(after, before);
        Set<String> ips = new HashSet<>();

        for (Record record : filteredRecords) {
            if (record.user.equals(user))
                ips.add(record.ip);
        }

        return ips;
    }
    //должен возвращать IP, с которых было произведено переданное событие.
    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        Set<Record> filteredRecords = getFilteredEntries(after, before);
        Set<String> ips = new HashSet<>();

        for (Record record : filteredRecords) {
            if (record.event.equals(event))
                ips.add(record.ip);
        }
        return ips;
    }
    //должен возвращать IP, события с которых закончилось переданным статусом.
    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        Set<Record> filteredRecords = getFilteredEntries(after, before);
        Set<String> ips = new HashSet<>();

        for (Record record : filteredRecords) {
            if (record.status.equals(status))
                ips.add(record.ip);
        }

        return ips;
    }
}