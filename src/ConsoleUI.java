import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    Scanner scanner;

    public ConsoleUI() {
        scanner = new Scanner(System.in);
    }

    public void printMenu() {
        System.out.println("Что вы хотите сделать (для выхода введите 'exit' и нажмите Enter)? ");
        System.out.println("1 - Считать все месячные отчёты");
        System.out.println("2 - Считать годовой отчёт");
        System.out.println("3 - Сверить отчёты");
        System.out.println("4 - Вывести информацию о всех месячных отчётах");
        System.out.println("5 - Вывести информацию о годовом отчёте");
    }

    public void printExitMessage() {
        System.out.println("Программа завершена.");
    }

    public void printNoExistCommandMessage() {
        System.out.println("Извините, такой команды пока нет.");
    }

    public void printMonthlyReportErrorMessage() {
        System.out.println("Ошибка при загрузке месячых отчётов");
    }

    public void printYearlyReportErrorMessage() {
        System.out.println("Ошибка при загрузке годового отчётов");
    }

    public void printLog(List<String> errorsList) {
        if (!errorsList.isEmpty()) {
            for (String error : errorsList) {
                System.out.println(error);
            }
        }
    }

    public String getCommand() {
        return scanner.nextLine().replaceAll("\\s", "");
    }

    public void close() {
        scanner.close();
    }
}
