package com.javarush.task.task26.task2613;

/**
 * Created by Admin on 21.09.2018.
 */
public enum Operation
{
     LOGIN,
     INFO,
     DEPOSIT,
     WITHDRAW,
     EXIT;


     public static Operation getAllowableOperationByOrdinal(Integer i){
          switch (i) {
               case 0:
                    return LOGIN;
                    //throw new IllegalArgumentException();

               case 1:
                    return INFO;
               case 2:
                    return DEPOSIT;
               case 3:
                    return WITHDRAW;
               case 4:
                    return EXIT;

               default:
                    throw new IllegalArgumentException();
          }
     }
}
