import java.util.ArrayList;
import java.util.HashMap;

public class MonthlyReport {
    private int monthNumber;
    private final HashMap<String, Double> expenses;
    private final HashMap<String, Double> incomes;

    public MonthlyReport() {
        monthNumber = -1;
        expenses = new HashMap<>();
        incomes = new HashMap<>();
    }

    public void addReportEntry(int monthNumber, String name, boolean isExpense, Double value) {
        HashMap<String, Double> items;
        if (isExpense) {
            items = expenses;
        } else {
            items = incomes;
        }
        if (items.containsKey(name)) {
            double itemValue = items.get(name);
            items.remove(name);
            itemValue += value;
            items.put(name, itemValue);
        } else {
            items.put(name, value);
            this.monthNumber = monthNumber;
        }
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public ArrayList<String> getMaxExpenseData() {
        return getFinancialDataMaxValue(true);
    }

    public ArrayList<String> getMaxIncomeData() {
        return getFinancialDataMaxValue(false);
    }

    public double getExpensesSum() {
        return getHasMapValuesSum(true);
    }

    public double getIncomesSum() {
        return getHasMapValuesSum(false);
    }

    private ArrayList<String> getFinancialDataMaxValue(boolean isExpense) {
        HashMap<String, Double> items = new HashMap<>();
        ArrayList<String> result = new ArrayList<>();
        if (isExpense && !expenses.isEmpty()) {
            items = this.expenses;
        } else if (!isExpense && !incomes.isEmpty()) {
            items = this.incomes;
        } else {
            if (isExpense) {
                result.add("В этом месяце нет затрат");
            } else {
                result.add("В этом месяце нет прибыли");
            }
        }
        String maxItemName = "";
        Double maxItemValue = 0.0;
        if (!items.isEmpty()) {
            for (var key : items.keySet()) {
                if (items.get(key) > maxItemValue) {
                    maxItemValue = items.get(key);
                    maxItemName = key;
                }
            }
            result.add(maxItemName);
            result.add(String.valueOf(maxItemValue));
        }
        return result;
    }

    private double getHasMapValuesSum(boolean isExpense) {
        HashMap<String, Double> items;
        if (isExpense && !expenses.isEmpty()) {
            items = this.expenses;
        } else {
            items = this.incomes;
        }
        double sum = 0;
        for (var key : items.keySet()) {
            Double value = items.get(key);
            sum += value;
        }
        return sum;
    }
}