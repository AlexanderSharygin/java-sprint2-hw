import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthlyReport {
    private int MonthNumber;
    private HashMap<String, Double> expenses;
    private HashMap<String, Double> income;

    public MonthlyReport()
    {
        MonthNumber=-1;
        expenses = new HashMap<>();
        income = new HashMap<>();
    }

    public void addReportItem(int monthNumber, String name, boolean isExpense, Double value) {
        if (isExpense) {
            if (expenses.containsKey(name)) {
                double expenseValue = expenses.get(name);
                expenses.remove(name);
                expenseValue += value;
                expenses.put(name, expenseValue);
            } else {
                expenses.put(name, value);
                this.MonthNumber=monthNumber;
            }
        } else {
            if (income.containsKey(name)) {
                double incomeValue = expenses.get(name);
                expenses.remove(name);
                incomeValue += value;
                expenses.put(name, incomeValue);
            } else {
                income.put(name, value);
                this.MonthNumber=monthNumber;
            }
        }
    }

    public int getMonthNumber() {
        return MonthNumber;
    }

    public  HashMap<String, Double> getExpenses()
    {
        return  expenses;
    }

    public  HashMap<String, Double> getIncome()
    {
        return  income;
    }

}
