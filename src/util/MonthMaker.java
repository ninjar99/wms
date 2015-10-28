package util;

import java.util.*;

class MonthMaker {

  private static Calendar maker = null;
  private MonthMaker() {}


  public static String[][] makeMonth(Calendar showMonth) {

    maker = showMonth;

    int dayCount = 1;

    String[][] date = new String[6][7];
    for (int f = 0; f < 6; f++) {
      java.util.Arrays.fill(date[f], "");

    }

    maker.set(Calendar.DATE, dayCount);

    for (int i = maker.get(Calendar.DAY_OF_WEEK) - 1; i < 7; i++) {
      date[0][i] = "" + dayCount;
      dayCount++;
    }

    for (int i = 1; i < 4; i++) {
      for (int j = 0; j < 7; j++) {
        date[i][j] = "" + dayCount;
        dayCount++;
      }
    }

    //制作月份表第 5 行---------------//
    for (int i = dayCount, j = 0;
         i <= maker.getActualMaximum(Calendar.DAY_OF_MONTH) && j < 7;
         i++, j++) {

      maker.set(Calendar.DATE, i);
      date[4][maker.get(Calendar.DAY_OF_WEEK) - 1] = "" + dayCount;

      dayCount++;
    }

    //制作月份表第 6 行--------------//
    for (int i = dayCount; i <= maker.getActualMaximum(
        Calendar.DAY_OF_MONTH); i++) {

      maker.set(Calendar.DATE, i);
      date[5][maker.get(Calendar.DAY_OF_WEEK) - 1] = "" + dayCount;

      dayCount++;
    }

    return date;
  }
}
