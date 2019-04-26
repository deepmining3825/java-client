package com.diquest.disa4.client.exception;

import lombok.Data;


@Data
public class CommandException extends RuntimeException {

    private int code;

    public CommandException(int code) {
        this.code = code;
    }

    public CommandException(int code, String s) {
        super(s);
        this.code = code;
    }

    public CommandException(int code, String s, Throwable throwable) {
        super(s, throwable);
        this.code = code;
    }

    public CommandException(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    @Override
    public String getMessage() {
        return super.getMessage() != null ? super.getMessage() : getDefaultMessage(getCode());
    }

    public static String getDefaultMessage(int code) {
        // BaseCommand
        switch (code) {
            // 통신클래스가 맞지 않을 경우
            case -70000:
                return "Unknown data type";
            // Client Pool 에서 Worker 할당을 실패하는 경우
            case -70001:
                return "Worker Initialization Failed";
            // Unknown Error
            case -99999:
                return "Unknown Error";
        }
        // ExternalClientWorker
        switch (code) {
            // 접속 실패
            case -60003:
                return "Connection failed.";
            // 소켓 타임아웃
            case -60004:
                return "Timed out";
            // 
            case -60005:
                return "Cannot read data properly";
            // 
            case -60006:
                return "A message header packet is not received.";
            // 
            case -60011:
                return "A close packet is received.";
        }
        
        return null;
    }

}
