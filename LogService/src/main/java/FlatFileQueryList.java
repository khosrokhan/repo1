import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlatFileQueryList extends LogCollector implements IQueryProvider {

    String input;
    String path;

    public FlatFileQueryList(String input, String path) {
        this.input = input;
        this.path = path;
    }

    private boolean containsLogType(Log log, List<LogType> logTypes) {
        if (logTypes.size() == 0)
            return true;

        boolean found = false;
        for (LogType item : logTypes) {
            if (log.getType() == item) {
                found = true;
                break;
            }
        }
        return found;
    }

    private boolean containsModule(Log log, List<String> modules) {
        if (modules.size() == 0)
            return true;

        boolean found = false;
        for (String item : modules) {
            if (log.getModule().toLowerCase().contains(item)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private boolean containsText(Log log, List<String> texts) {
        if (texts.size() == 0)
            return true;

        boolean found = false;
        for (String item : texts) {
            if (log.getText().toLowerCase().contains(item)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private List<Date> getDaysBetweenDates(ArrayList<String> list) throws ParseException {

        List<Date> dates = new ArrayList<Date>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (list.size() == 0) {
            Date currentDate = new Date();
            dates.add(currentDate);
        } else if (list.size() == 1) {
            Date date = (Date) formatter.parse(list.get(0));
            dates.add(date);
        } else {
            Date startDate = (Date) formatter.parse(list.get(0));
            Date endDate = (Date) formatter.parse(list.get(1));

            long interval = 24 * 1000 * 60 * 60;
            long endTime = endDate.getTime();
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                dates.add(new Date(curTime));
                curTime += interval;
            }
            for (int i = 0; i < dates.size(); i++) {
                Date lDate = (Date) dates.get(i);
                String ds = formatter.format(lDate);
            }
        }
        return dates;
    }

    @Override
    public List<String> getResult() throws  Exception {
        try {
            clear();
            parse(input);

            List<String> result = new ArrayList<>();
            List<Log> logs = new ArrayList<>();
            List<Date> dateList = getDaysBetweenDates(new ArrayList<String>(getDateList()));
            for (Date date : dateList) {
                String fileName = "log-" + new SimpleDateFormat("yyyy-MM-dd").format(date);
                String temPath = path + fileName;
                File file = new File(temPath);
                if (file.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(temPath));
                    String line = br.readLine();
                    while (line != null) {
                        Log log = Log.parse(line);

                        if (log != null)
                            logs.add(log);

                        line = br.readLine();
                    }
                }
            }

            int count = 0;
            for (Log l : logs) {
                if (containsLogType(l, getTypeList()) && containsModule(l, getModuleList()) && containsText(l, getTextList())) {
                    result.add(l.toString());
                    count++;
                }
            }

            result.add("end of file. " + count + " record(s) found");
            return (ArrayList<String>) result;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public boolean broadcast() {
        return false;
    }
}
