import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ReportManager {
    MonthlyReport[] monthlyReports;
    YearlyReport yearlyReport;
    ArrayList<String> log = new ArrayList<>();

    public void readMonthlyReports(String path) {
        monthlyReports = new MonthlyReport[12];
        for (int i = 1; i <= 12; i++) {
            MonthlyReport report = new MonthlyReport();
            String monthNumber = String.valueOf(i);
            if (i < 10) {
                monthNumber = "0" + monthNumber;
            }
            String reportFileBody = readFileContentsOrNull(path + "m.2021" + monthNumber + ".csv", ReportType.MONTHLY, i);
            if (reportFileBody != null) {
                String[] rows = reportFileBody.split(System.lineSeparator());
                for (int j = 1; j < rows.length; j++) {
                    String[] cells = rows[j].split(",");
                    String name = cells[0];
                    boolean isExpense = false;
                    Double value = Integer.parseInt(cells[2]) * Double.parseDouble(cells[3]);
                    if (cells[1].toLowerCase(Locale.ROOT).equals("true")) {
                        isExpense = true;
                    }
                    report.addReportItem(i, name, isExpense, value);
                    monthlyReports[i - 1] = report;
                }
            }
        }
    }

    public void readYearReport(String path) {
        yearlyReport = new YearlyReport();
        String reportFileBody = readFileContentsOrNull(path + "y.2021.csv", ReportType.MONTHLY, null);
        String[] rows = reportFileBody.split(System.lineSeparator());
        for (int i = 1; i < rows.length; i++) {
            String[] cells = rows[i].split(",");
            int monthNumber = Integer.parseInt(cells[0]);
            boolean isExpense = false;
            if (cells[2].toLowerCase(Locale.ROOT).equals("true")) {
                isExpense = true;
            }
            double value = Double.parseDouble(cells[1]);
            yearlyReport.addReportItem(monthNumber, isExpense, value);
        }
    }

    private boolean isReportsUploaded(boolean checkMonthlyReports, boolean checkYearlyReport) {
        boolean result = true;
        if (checkYearlyReport && yearlyReport == null) {
            log.add("Годоввой отчёт не загружен!");
            result = false;
        }
        if (checkMonthlyReports && monthlyReports == null) {
            log.add("Не загружено ни одного отчёта за месяц!");
            result = false;
        }
        return result;
    }

    public ArrayList<String> compareReports() {
        if (!isReportsUploaded(true, true)) {
            return log;
        }
        boolean isReportsCorrect = true;
        for (int i = 0; i < monthlyReports.length; i++) {
            if (monthlyReports[i] == null) {
                continue;
            }
            int monthNumber = monthlyReports[i].getMonthNumber();
            if (monthNumber != -1) {
                HashMap<String, Double> monthlyExpenses = monthlyReports[i].getExpenses();
                double expensesSum = getHasMapValuesSum(monthlyExpenses);
                HashMap<String, Double> monthlyIncome = monthlyReports[i].getIncome();
                double incomeSum = getHasMapValuesSum(monthlyIncome);
                double yearReportExpensesSum = yearlyReport.getExpensesByMonth(monthNumber);
                double yearReportIncomeSum = yearlyReport.getIncomeByMonth(monthNumber);
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

    private double getHasMapValuesSum(HashMap<String, Double> hashMap) {
        double sum = 0;
        for (var key : hashMap.keySet()) {
            Double value = hashMap.get(key);
            sum += value;
        }
        return sum;
    }

    private String readFileContentsOrNull(String path, ReportType reportType, Integer monthNumber) {
        try {
            return Files.readString(Path.of(path));

        } catch (IOException e) {
            return null;
        } catch (Exception e) {
            if (reportType == ReportType.MONTHLY) {
                log.add("Ошибка при загрузке месячного отчёта за месяц " + monthNumber + "!");
            } else if (reportType == ReportType.YEAR) {
                log.add("Ошибка при загрузке годового отчёта!");
            }
            return null;
        }
    }

    public ArrayList<String> getYearlyReportsInfo(String yearNumber) {
        if (!isReportsUploaded(false, true)) {
            return log;
        }
        log.add(("Отчёт за " + yearNumber + " год.").toUpperCase());
        double averageExpense = yearlyReport.getAverageExpenses();
        if (averageExpense == -1) {
            log.add("В этом году не было расходов");
        } else {
            log.add(String.format("Средний расход за все месяцы в году:  %.2f", averageExpense));
        }
        double averageIncome = yearlyReport.getAverageIncomes();
        if (averageIncome == -1) {
            log.add("В этом году не было доходов");
        } else {
            log.add(String.format("Средний доход за все месяцы в году:  %.2f", averageIncome));

        }
        List<Double> balancePerMonth = yearlyReport.getBalancesPerMonth();
        for (int i = 0; i < balancePerMonth.size(); i++) {
            Month month = Month.of(i + 1);
            Locale loc = Locale.forLanguageTag("ru");
            String monthName = month.getDisplayName(TextStyle.FULL_STANDALONE, loc);
            log.add(String.format("Прибыль за месяц " + monthName + ": %.2f", balancePerMonth.get(i)));
        }
        return log;
    }

    public ArrayList<String> getMonthlyReportsInfo() {
        if (!isReportsUploaded(true, false)) {
            return log;
        }
        for (MonthlyReport monthlyReport : monthlyReports) {
            if (monthlyReport != null) {
                Month month = Month.of(monthlyReport.getMonthNumber());
                Locale loc = Locale.forLanguageTag("ru");
                log.add(("Отчёт за " + month.getDisplayName(TextStyle.FULL_STANDALONE, loc) + ":").toUpperCase());
                ArrayList<String> maxIncomeData = geMaxReportItem(monthlyReport, false);
                log.add("Самый прибыльный товар:");
                if (maxIncomeData.size() == 1) {
                    log.add(maxIncomeData.get(0));
                } else {
                    log.add(maxIncomeData.get(0) + ". " + "Сумма прибыли: " + maxIncomeData.get(1));
                }
                ArrayList<String> maxExpenseData = geMaxReportItem(monthlyReport, true);
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

    private ArrayList<String> geMaxReportItem(MonthlyReport monthlyReport, boolean isExpense) {
        HashMap<String, Double> items;
        if (isExpense) {
            items = monthlyReport.getExpenses();
        } else {
            items = monthlyReport.getIncome();
        }
        String maxIncomeName = "";
        Double maxIncomeValue = 0.0;
        ArrayList<String> result = new ArrayList<>();
        if (!items.isEmpty()) {
            for (var key : items.keySet()) {
                if (items.get(key) > maxIncomeValue) {
                    maxIncomeValue = items.get(key);
                    maxIncomeName = key;
                }
            }
            result.add(maxIncomeName);
            result.add(String.valueOf(maxIncomeValue));

        } else {
            isExpense = true ? result.add("В этом месяце нет затрат") : result.add("В этом месяце нет прибыли");
        }
        return result;
    }

    public void clearErrorsLog() {
        log.clear();
    }
}
