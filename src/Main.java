import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReportManager reportManager = new ReportManager();
        final String reportFilesPath = "resources/";
        final String yearNumber = "2021";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            reportManager.clearErrorsLog();
            String command = scanner.nextLine();
            if (command.equals("1")) {
                reportManager.readMonthlyReports(reportFilesPath, yearNumber);
            } else if (command.equals("2")) {
                reportManager.readYearlyReport(reportFilesPath, yearNumber);
            } else if (command.equals("3")) {
                List<String> errors = reportManager.compareReports();
                printLog(errors);
            } else if (command.equals("4")) {
                List<String> result = reportManager.getMonthlyReportsInfo();
                printLog(result);
            } else if (command.equals("5")) {
                List<String> result = reportManager.getYearlyReportsInfo(yearNumber);
                printLog(result);
            } else if (command.equals("exit")) {
                System.out.println("Программа завершена.");
                scanner.close();
                break;
            } else {
                System.out.println("Извините, такой команды пока нет.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("Что вы хотите сделать (для выхода введите 'exit' и нажмите Enter)? ");
        System.out.println("1 - Считать все месячные отчёты");
        System.out.println("2 - Считать годовой отчёт");
        System.out.println("3 - Сверить отчёты");
        System.out.println("4 - Вывести информацию о всех месячных отчётах");
        System.out.println("5 - Вывести информацию о годовом отчёте");
    }

    public static void printLog(List<String> errorsList) {
        if (!errorsList.isEmpty()) {
            for (String error : errorsList) {
                System.out.println(error);
            }
        }
    }
}

