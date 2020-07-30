package cn.edu.lingnan.util;

public class Result {
    public static String SUCCESS_CODE = "success";
    public static String FAIL_CODE = "fail";

    String status;
    String message;
    Object data;
    Object req_data;

    // 不加会抛出missing default constructor or creator, or perhaps need to add/enable type information
    public Result(){

    }

    private Result(String status, String message, Object data, Object req_data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.req_data=req_data;
    }

    public static Result success() {
        return new Result(SUCCESS_CODE,null,null, null);
    }
    public static Result success(String message) {
        return new Result(SUCCESS_CODE,message,null, null);
    }
    public static Result success(Object data) {
        return new Result(SUCCESS_CODE,"",data, null);
    }
    public static Result success(Object data, Object req_data) {
        return new Result(SUCCESS_CODE,"", data, req_data);
    }

    public static Result fail(String message) {
        return new Result(FAIL_CODE,message,null, null);
    }
    public static Result fail(String message, Object req_data) {
        return new Result(FAIL_CODE,message,null, req_data);
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Object getReq_data() {
        return req_data;
    }
    public void setReq_data(Object req_data) {
        this.req_data = req_data;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

}