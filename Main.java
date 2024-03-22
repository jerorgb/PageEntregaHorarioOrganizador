

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        int actualYear = 2024;
        String locale = "es";

        List<String> months = new ArrayList<>();
        List<String> weekDays = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            String monthName = Month.of(i + 1).getDisplayName(TextStyle.FULL, new Locale(locale));
            months.add(monthName);
        }

        for (int i = 1; i <= 7; i++) {
            String dayName = DayOfWeek.of(i).getDisplayName(TextStyle.SHORT, new Locale(locale));
            weekDays.add(dayName);
        }

        List<String> calendar = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Month month = Month.of(i + 1);
            int daysOfMonth = month.length(false);
            int startsOn = LocalDate.of(actualYear, month, 1).getDayOfWeek().getValue();

            StringBuilder htmlDaysName = new StringBuilder();
            for (String dayName : weekDays) {
                htmlDaysName.append("<li class='day-name'>").append(dayName).append("</li>");
            }

            StringBuilder htmlDays = new StringBuilder();
            for (int j = 0; j < daysOfMonth; j++) {
                if (j == 0) {
                    htmlDays.append("<li class='first-day' style='--first-day-start: ").append(startsOn).append("'>").append(j + 1).append("</li>");
                } else {
                    htmlDays.append("<li>").append(j + 1).append("</li>");
                }
            }

            String html = "<h2>" + months.get(i) + " " + actualYear + "</h2><ol>" + htmlDaysName.toString() + htmlDays.toString() + "</ol>";
            calendar.add(html);
        }

        StringBuilder finalHtml = new StringBuilder();
        for (String html : calendar) {
            finalHtml.append(html);
        }

        System.out.println(finalHtml.toString());
    }
}


