import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class YearlyReport {
    private final HashMap<Integer, Double> expenses;
    private final HashMap<Integer, Double> income;

    public YearlyReport() {
        expenses = new HashMap<>();
        income = new HashMap<>();
    }

    public void addReportItem(int monthNumber, boolean isExpense, Double value) {
        if (isExpense) {
            if (expenses.containsKey(monthNumber)) {
                double expenseValue = expenses.get(monthNumber);
                expenses.remove(monthNumber);
                expenseValue += value;
                expenses.put(monthNumber, expenseValue);
            } else {
                expenses.put(monthNumber, value);
            }
        } else {
            if (income.containsKey(monthNumber)) {
                double expenseValue = income.get(monthNumber);
                income.remove(monthNumber);
                expenseValue += value;
                income.put(monthNumber, expenseValue);
            } else {
                income.put(monthNumber, value);
            }
        }
    }

    public double getExpensesByMonth(int monthNumber) {
        return expenses.get(monthNumber);
    }

    public double getIncomeByMonth(int monthNumber) {
        return income.get(monthNumber);
    }

    public double getAverageExpenses() {
        return getAverageValue(true);
    }

    public double getAverageIncomes() {

        return getAverageValue(false);
    }

    private double getAverageValue(boolean isForExpenses)
    {
        Collection<Double> itemValues;
        if (isForExpenses && !expenses.isEmpty()) {
            itemValues =  expenses.values();
        }
        else if  (!isForExpenses && !income.isEmpty())
        {
            itemValues = income.values();
        }
        else
        {
            return -1;
        }

            double sum = 0;
            for (var value : itemValues) {
                sum += value;
            }
            return sum / itemValues.size();

    }

    public ArrayList<Double> getBalancesPerMonth()
    {
        ArrayList<Double> result = new ArrayList<>();
       int counter = Integer.max(expenses.size(), income.size());
        for (int i = 0; i <= counter; i++) {
            if (expenses.containsKey(i) && income.containsKey(i))
            {
                result.add(income.get(i)-expenses.get(i));
            }
            else if (expenses.containsKey(i) && !income.containsKey(i))
            {
                result.add(-expenses.get(i));
            }
            else if (!expenses.containsKey(i) && income.containsKey(i))
            {
                result.add(income.get(i));
            }
        }
        return result;
    }
}





