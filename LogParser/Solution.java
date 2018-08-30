package com.javarush.task.task39.task3913;

import java.nio.file.Paths;
import java.util.Date;
import java.util.Set;

public class Solution {
    public static void main(String[] args) {
        LogParser logParser = new LogParser(Paths.get("D:\\Users\\Admin\\Downloads\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs\\"));

       // System.out.println(logParser.execute("get ip for user = \"Eduard Petrovich Morozko\" and date between \"13.09.2013 5:04:50\" and \"03.01.2014 03:45:23\"."));
        //System.out.println(logParser.execute("get user for ip = \"127.0.0.1\" and date between \"13.09.2013 5:04:50\" and \"03.01.2014 03:45:23\"."));
        System.out.println(logParser.execute("get user for ip = \"127.0.0.1\" and date between \"13.09.2013 5:04:50\" and \"13.09.2013 5:04:50\"."));
/*

        System.out.println(logParser.execute("get ip for date = \"30.08.2012 16:08:40\""));
        System.out.println(logParser.execute("get ip for user = \"Amigo\""));
        System.out.println(logParser.execute("get ip for event = \"LOGIN\""));
        System.out.println(logParser.execute("get ip for status = \"OK\""));

        System.out.println(logParser.execute("get user for status = \"OK\""));
        System.out.println(logParser.execute("get user for event = \"LOGIN\""));
        System.out.println(logParser.execute("get user for date = \"13.09.2013 5:04:50\""));

        System.out.println(logParser.execute("get user"));
        */

    }
}