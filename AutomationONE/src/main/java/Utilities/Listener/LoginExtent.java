package Utilities.Listener;

import java.io.StringWriter;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.openqa.selenium.WebDriver;
import lombok.SneakyThrows;

public class LoginExtent {

    ExtentListener e = new ExtentListener();

    protected void extentReportFormatter(String content) {
        String prettyPrint = content.replace("\n", "<br>");
        e.logInfo("<pre>" + prettyPrint + "</pre>");
    }

    public static String prettyPrintXML(String xml) {
        final StringWriter sw;

        try {
            final OutputFormat format = OutputFormat.createPrettyPrint();
            final Document document = DocumentHelper.parseText(xml);
            sw = new StringWriter();
            final XMLWriter writer = new XMLWriter(sw, format);
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Error pretty printing XML:\n" + xml, e);
        }
        return sw.toString();
    }

    public static String prettyPrintJSON(String unformattedJsonString) {
        StringBuilder prettyJSONBuilder = new StringBuilder();
        int indentLevel = 0;
        boolean inQuote = false;

        for (char charFromUnformattedJson : unformattedJsonString.toCharArray()) {
            switch (charFromUnformattedJson) {
                case '"':
                    inQuote = !inQuote;
                    prettyJSONBuilder.append(charFromUnformattedJson);
                    break;

                case ' ':
                    if (inQuote) {
                        prettyJSONBuilder.append(charFromUnformattedJson);
                    }
                    break;

                case '{':
                case '[':
                    prettyJSONBuilder.append(charFromUnformattedJson);
                    indentLevel++;
                    appendIndentNewLine(indentLevel, prettyJSONBuilder);
                    break;

                case '}':
                case ']':
                    indentLevel--;
                    appendIndentNewLine(indentLevel, prettyJSONBuilder);
                    prettyJSONBuilder.append(charFromUnformattedJson);
                    break;

                case ',':
                    prettyJSONBuilder.append(charFromUnformattedJson);
                    if (!inQuote) {
                        appendIndentNewLine(indentLevel, prettyJSONBuilder);
                    }
                    break;

                default:
                    prettyJSONBuilder.append(charFromUnformattedJson);
            }
        }
        return prettyJSONBuilder.toString();
    }

    private static void appendIndentNewLine(int indentLevel, StringBuilder stringBuilder) {
        stringBuilder.append("\n");
        stringBuilder.append("  ".repeat(indentLevel));
    }

    public void logResults(String info, String code) {
        e.logInfo(info);
        e.logXMLInfo(code);
    }

    public void logJSONResults(String request, String response) {
        e.logInfo("----Request----");
        e.logXMLInfo(prettyPrintJSON(request));
        e.logInfo("----Response----");
        e.logXMLInfo(prettyPrintJSON(response));
    }

    public void logXMLResults(String request, String response) {
        e.logInfo("----Request----");
        e.logXMLInfo(prettyPrintXML(request));
        e.logInfo("----Response----");
        e.logXMLInfo(prettyPrintXML(response));
    }

    public void logReportPass(String message) {
        e.logReportPass(message);
    }

    public void logReportFail(String message) {
        e.logReportFail(message);
    }

    @SneakyThrows
    public void logInfo(String message) {
        e.logInfo(message);
    }

    @SneakyThrows
    public void reportUIResult(String status, String message, boolean ssFlag, WebDriver driver) {
        e.reoprtResult(status, message, ssFlag, driver);
    }

    @SneakyThrows
    public void reportUIResultRobot(String status, String message, boolean ssFlag, WebDriver driver) {
        e.reoprtResult(status, message, ssFlag, driver);
    }
}
