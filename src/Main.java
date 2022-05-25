import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String reportFilesPath = "resources\\";
        ConsoleUI consoleUI = new ConsoleUI();
        ReportManager reportManager = new ReportManager();
        DataReader dataReader = new DataReader(reportFilesPath);
        final String yearNumber = "2021";
        while (true) {
            consoleUI.printMenu();
            reportManager.clearErrorsLog();
            String command = consoleUI.getCommand();
            if (command.equals("1")) {
                try {
                    ArrayList<String> monthlyReportsFileBody = dataReader.getMonthlyReportsText(yearNumber);
                    reportManager.parseMonthlyReports(monthlyReportsFileBody);
                } catch (ReadMonthlyReportException e) {
                    consoleUI.printMonthlyReportErrorMessage();
                }
            } else if (command.equals("2")) {
                try {
                    String yearlyReportFileBody = dataReader.getYearlyReportText(yearNumber);
                    reportManager.parseYearlyReport(yearlyReportFileBody);
                } catch (ReadYearlyReportException e) {
                    consoleUI.printYearlyReportErrorMessage();
                }
            } else if (command.equals("3")) {
                List<String> errors = reportManager.compareReports();
                consoleUI.printLog(errors);
            } else if (command.equals("4")) {
                List<String> result = reportManager.getMonthlyReportsInfo();
                consoleUI.printLog(result);
            } else if (command.equals("5")) {
                List<String> result = reportManager.getYearlyReportsInfo(yearNumber);
                consoleUI.printLog(result);
            } else if (command.equals("exit")) {
                consoleUI.printExitMessage();
                consoleUI.close();
                break;
            } else {
                consoleUI.printNoExistCommandMessage();
            }
        }
    }
}

