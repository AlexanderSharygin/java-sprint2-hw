import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class YearlyReport {
    private final HashMap<Integer, Double> expenses;
    private final HashMap<Integer, Double> incomes;

    public YearlyReport() {
        expenses = new HashMap<>();
        incomes = new HashMap<>();
    }

    public void addReportEntry(int monthNumber, boolean isExpense, Double value) {
        HashMap<Integer, Double> items;
        if (isExpense) {
            items = expenses;
        } else {
            items = incomes;
        }
        if (items.containsKey(monthNumber)) {
            double itemValue = items.get(monthNumber);
            items.remove(monthNumber);
            itemValue += value;
            items.put(monthNumber, itemValue);
        } else {
            items.put(monthNumber, value);
        }
    }

    public double getExpenseForMonth(int monthNumber) {
        return expenses.get(monthNumber);
    }

    public double getIncomeForMonth(int monthNumber) {
        return incomes.get(monthNumber);
    }

    public double getAvgExpense() {
        return getFinancialDataAvgValue(true);
    }

    public double getAvgIncome() {

        return getFinancialDataAvgValue(false);
    }

    public ArrayList<Double> getBalancePerMonths() {
        ArrayList<Double> result = new ArrayList<>();
        int counter = Integer.max(expenses.size(), incomes.size());
        for (int i = 0; i <= counter; i++) {
            if (expenses.containsKey(i) && incomes.containsKey(i)) {
                result.add(incomes.get(i) - expenses.get(i));
            } else if (expenses.containsKey(i) && !incomes.containsKey(i)) {
                result.add(-expenses.get(i));
            } else if (!expenses.containsKey(i) && incomes.containsKey(i)) {
                result.add(incomes.get(i));
            }
        }
        return result;
    }

    private double getFinancialDataAvgValue(boolean isForExpenses) {
        Collection<Double> itemValues;
        if (isForExpenses && !expenses.isEmpty()) {
            itemValues = expenses.values();
        } else if (!isForExpenses && !incomes.isEmpty()) {
            itemValues = incomes.values();
        } else {
            return -1;
        }
        double sum = 0;
        for (var value : itemValues) {
            sum += value;
        }
        return sum / itemValues.size();
    }
}