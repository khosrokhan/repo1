import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogCollector {
    private List<LogType> typeList = new ArrayList<LogType>();

    public List<LogType> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<LogType> typeList) {
        this.typeList = typeList;
    }

    private void addToTypeList(LogType logType) {
        typeList.add(logType);
    }

    private void clearTypeList() {
        typeList.clear();
    }

    private List<String> moduleList = new ArrayList<String>();

    public List<String> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<String> moduleList) {
        this.moduleList = moduleList;
    }

    private void clearModuleList() {
        moduleList.clear();
    }

    private List<String> textList = new ArrayList<String>();

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    private void clearTextList() {
        textList.clear();
    }


    private List<String> dateList = new ArrayList<String>();

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    private void clearDateList() {
        dateList.clear();
    }

    public void clear() {
        clearTypeList();
        clearTextList();
        clearModuleList();
        clearDateList();
    }

    public void parse(String input) throws Exception {
        try {
            if (input.contains("type:")) {
                Pattern p = Pattern.compile("type:(?<type>[^\\s]+)");
                Matcher m = p.matcher(input);
                if (m.find()) {
                    String typeValue = m.group("type");
                    String[] parts = typeValue.split(",");
                    for (String s : parts) {
                        addToTypeList(LogType.valueOf(s));
                    }
                }
            }
            if (input.contains("text:")) {
                Pattern p = Pattern.compile("text:(?<text>[^\\s]+)");
                Matcher m = p.matcher(input);
                if (m.find()) {
                    String textValue = m.group("text");
                    String[] parts = textValue.split(",");
                    setTextList(Arrays.asList(parts));
                }
            }
            if (input.contains("module:")) {
                Pattern p = Pattern.compile("module:(?<module>[^\\s]+)");
                Matcher m = p.matcher(input);
                if (m.find()) {
                    String moduleValue = m.group("module");
                    String[] parts = moduleValue.split(",");
                    setModuleList(Arrays.asList(parts));
                }
            }
            if (input.contains("date:")) {
                Pattern p = Pattern.compile("date:(?<date>[^\\s]+)");
                Matcher m = p.matcher(input);
                if (m.find()) {
                    String moduleValue = m.group("date");
                    String[] parts = moduleValue.split(",");
                    setDateList(Arrays.asList(parts));
                }
            }
        } catch (Exception ex) {
            throw ex;
        }

    }
}
