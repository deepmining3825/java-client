package com.diquest.disa4.client.command;

import com.diquest.frame.util.msg.Message;

import java.util.LinkedHashMap;

/**
 * 분석
 */
public class CommandAnalyze extends BaseCommand {

    public CommandAnalyze() {

    }

    public CommandAnalyze(String ip) { super(ip); }

    public CommandAnalyze(String ip, int port) { super(ip, port); }

    public CommandAnalyze(String ip, int port, int timeout) {
        super(ip, port, timeout);
    }

    public LinkedHashMap<String, String> analyze(String categoryName, String content, String module) {
        LinkedHashMap<String, String> viewResult = new LinkedHashMap<String, String>();
        Message response;
        content = content.replaceAll("&quot;","\"").replaceAll("&#39;","\'");
        Message request = new Message(4);
        request.setString(0, categoryName); // TODO: category null 체크 front에서
        request.setString(1, content);

        if ("ALL".equals(module)) {
            // JIANA
            request.setString(2, "JIANA"); // as-is: commandName
            request.setInt(3,  AnalyzeTextOption.JIANA);
            response = execute("", request); // TODO: reflector 추가
            if (response == null || response.get(0) == null || !"OK".equals(response.getString(0))) {
                return null;
            }
            viewResult.put("JIANA", response.getString(1));

            // PLOT
            request.setString(2, "PLOT"); // as-is: commandName
            request.setInt(3,  AnalyzeTextOption.PLOT);
            response = execute("", request); // TODO: reflector 추가
            if (response == null || response.get(0) == null || !"OK".equals(response.getString(0))) {
                return null;
            }
            viewResult.put("PLOT", response.getString(1));
            viewResult.put("BRAT_ENTITY_FORMAT", "[" + response.getString(2) + "]");

            // DISA
            request.setString(2, "DISA");
            request.setInt(3, AnalyzeTextOption.DISA);
            response = execute("", request);
            if (response == null || response.get(0) == null || !"OK".equals(response.getString(0))) {
                return null;
            }
            viewResult.put("DISA", response.getString(1).replaceAll("\r", " "));
            String result = response.getString(2) == null ? "" : response.getString(2).trim();
            viewResult.put("BRAT_RELATION_FORMAT", "[" + result + "]");
        }
        else if ("JIANA".equals(module)) {
            request.setString(2, "JIANA"); // as-is: commandName
            request.setInt(3,  AnalyzeTextOption.JIANA);
            response = execute("", request); // TODO: reflector 추가
            if (response == null || response.get(0) == null || !"OK".equals(response.getString(0))) {
                return null;
            }
            viewResult.put("JIANA", response.getString(1));
        }
        else if ("PLOT".equals(module)) {
            request.setString(2, "PLOT"); // as-is: commandName
            request.setInt(3,  AnalyzeTextOption.PLOT);
            response = execute("", request); // TODO: reflector 추가
            if (response == null || response.get(0) == null || !"OK".equals(response.getString(0))) {
                return null;
            }
            viewResult.put("PLOT", response.getString(1));
        }
        else if ("DISA".equals(module)) {
            request.setString(2, "DISA");
            request.setInt(3, AnalyzeTextOption.DISA);
            response = execute("", request); // TODO: reflector 추가
            if (response == null || response.get(0) == null || !"OK".equals(response.getString(0))) {
                return null;
            }
            viewResult.put("DISA", response.getString(1).replaceAll("\r", " "));
        }

        return viewResult;
    }

    public class AnalyzeTextOption {
        public static final int JIANA = 1;
        public static final int PLOT = 2;
        public static final int DISA = 4;
        public static final int DISA_HIGHLIGHT = 8;

        public AnalyzeTextOption() {
        }
    }
}
