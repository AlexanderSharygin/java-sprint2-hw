import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class DataReader {

    String pathToFiles;

    public DataReader(String pathToFiles) {
        this.pathToFiles = pathToFiles;
    }

    public String getYearlyReportText(String yearNumber) {
        return readFileContentsOrNull("y." + yearNumber + ".csv", false, null);
    }

    public ArrayList<String> getMonthlyReportsText(String yearNumber) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String monthNumber = String.valueOf(i);
            if (i < 10) {
                monthNumber = "0" + monthNumber;
            }
            String reportFileBody = readFileContentsOrNull("m." + yearNumber + monthNumber + ".csv",
                    true, i);
            if (reportFileBody != null) {
                result.add(reportFileBody);
            }
        }
        return result;
    }

    private String readFileContentsOrNull(String fileName, boolean isMonthlyReport, Integer monthNumber) {
        try {
            return Files.readString(Path.of(pathToFiles + fileName));
        } catch (IOException e) {
            return null;

        } catch (Exception e) {
            if (isMonthlyReport) {
                throw new ReadMonthlyReportException("Ошибка при загрузке месячных отчётов");
            } else {
                throw new ReadYearlyReportException("Ошибка при загрузке годового отчёта!");
            }
        }
    }
}
