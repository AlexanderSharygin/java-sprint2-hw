import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportManager {
    MonthlyReport[] monthlyReports;
    YearlyReport yearlyReport;
    ArrayList<String> log;

    public ReportManager() {
        log = new ArrayList<>();
    }

    public void parseMonthlyReports(ArrayList<String> reportFilesText) {
        monthlyReports = new MonthlyReport[12];
        for (int i = 0; i < reportFilesText.size(); i++) {
            {
                MonthlyReport monthlyReport = new MonthlyReport();
                String[] reportRows = reportFilesText.get(i).split(System.lineSeparator());
                for (int j = 1; j < reportRows.length; j++) {
                    String[] rowCells = reportRows[j].split(",");
                    String name = rowCells[0];
                    boolean isExpense = false;
                    Double value = Integer.parseInt(rowCells[2]) * Double.parseDouble(rowCells[3]);
                    if (rowCells[1].toLowerCase(Locale.ROOT).equals("true")) {
                        isExpense = true;
                    }
                    monthlyReport.addReportEntry(i + 1, name, isExpense, value);
                }
                monthlyReports[i] = monthlyReport;
            }
        }
    }

    public void parseYearlyReport(String reportFileBody) {
        yearlyReport = new YearlyReport();
        String[] reportRows = reportFileBody.split(System.lineSeparator());
        for (int i = 1; i < reportRows.length; i++) {
            String[] rowCells = reportRows[i].split(",");
            int monthNumber = Integer.parseInt(rowCells[0]);
            boolean isExpense = rowCells[2].toLowerCase(Locale.ROOT).equals("true");
            double value = Double.parseDouble(rowCells[1]);
            yearlyReport.addReportEntry(monthNumber, isExpense, value);
        }
    }

    public ArrayList<String> compareReports() {
        if (isYearlyReportsNotExist() & isMonthlyReportsAreNotExist()) {
            return log;
        }
        boolean isReportsCorrect = true;
        for (MonthlyReport monthlyReport : monthlyReports) {
            if (monthlyReport == null) {
                continue;
            }
            int monthNumber = monthlyReport.getMonthNumber();
            if (monthNumber != -1) {
                double expensesSum = monthlyReport.getExpensesSum();
                double incomeSum = monthlyReport.getIncomesSum();
                double yearReportExpensesSum = yearlyReport.getExpenseForMonth(monthNumber);
                double yearReportIncomeSum = yearlyReport.getIncomeForMonth(monthNumber);
                if (expensesSum != yearReportExpensesSum) {
                    log.add("В отчёте ошибка - не совпадает сумма расходов за месяц " + monthNumber + "!");
                    isReportsCorrect = false;
                }
                if (incomeSum != yearReportIncomeSum) {
                    log.add("В отчёте ошибка - не совпадает сумма доходв за месяц " + monthNumber + "!");
                    isReportsCorrect = false;
                }
            }
        }
        if (isReportsCorrect) {
            log.add("В отчётах не обнаружено ошибок!");
        }
        return log;
    }

    public ArrayList<String> getYearlyReportsInfo(String yearNumber) {
        if (isYearlyReportsNotExist()) {
            return log;
        }
        log.add(("Отчёт за " + yearNumber + " год.").toUpperCase());
        double averageExpense = yearlyReport.getAvgExpense();
        if (averageExpense == -1) {
            log.add("В этом году не было расходов");
        } else {
            log.add(String.format("Средний расход за все месяцы в году:  %.2f", averageExpense));
        }
        double averageIncome = yearlyReport.getAvgIncome();
        if (averageIncome == -1) {
            log.add("В этом году не было доходов");
        } else {
            log.add(String.format("Средний доход за все месяцы в году:  %.2f", averageIncome));

        }
        List<Double> balancePerMonth = yearlyReport.getBalancePerMonths();
        for (int i = 0; i < balancePerMonth.size(); i++) {
            Month month = Month.of(i + 1);
            Locale loc = Locale.forLanguageTag("ru");
            String monthName = month.getDisplayName(TextStyle.FULL_STANDALONE, loc);
            log.add(String.format("Прибыль за месяц " + monthName + ": %.2f", balancePerMonth.get(i)));
        }
        return log;
    }

    public ArrayList<String> getMonthlyReportsInfo() {
        if (isMonthlyReportsAreNotExist()) {
            return log;
        }
        for (MonthlyReport monthlyReport : monthlyReports) {
            if (monthlyReport != null) {
                Month month = Month.of(monthlyReport.getMonthNumber());
                Locale loc = Locale.forLanguageTag("ru");
                log.add(("Отчёт за " + month.getDisplayName(TextStyle.FULL_STANDALONE, loc) + ":").toUpperCase());
                ArrayList<String> maxIncomeData = monthlyReport.getMaxIncomeData();
                log.add("Самый прибыльный товар:");
                if (maxIncomeData.size() == 1) {
                    log.add(maxIncomeData.get(0));
                } else {
                    log.add(maxIncomeData.get(0) + ". " + "Сумма прибыли: " + maxIncomeData.get(1));
                }
                ArrayList<String> maxExpenseData = monthlyReport.getMaxExpenseData();
                log.add("Максимальная трата:");
                if (maxExpenseData.size() == 1) {
                    log.add(maxExpenseData.get(0));
                } else {
                    log.add(maxExpenseData.get(0) + ". " + "Сумма прибыли: " + maxExpenseData.get(1));
                }
            }
        }
        return log;
    }

    public void clearErrorsLog() {
        log.clear();
    }

    private boolean isYearlyReportsNotExist() {
        boolean result = true;
        if (yearlyReport == null) {
            log.add("Годоввой отчёт не загружен!");
            result = false;
        }
        return !result;
    }

    private boolean isMonthlyReportsAreNotExist() {
        boolean result = true;
        if (monthlyReports == null) {
            log.add("Не загружено ни одного отчёта за месяц!");
            result = false;
        }
        return !result;
    }
}
