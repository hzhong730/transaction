package com.hsbc.transaction.model.vo;

public class ResultVO {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILED";
    public static final String INVALID = "INVALID";

    private String result;
    private String message;

    public static ResultVO buildFailure(String message) {
        ResultVO vo = new ResultVO();
        vo.message = message;
        vo.result = FAILURE;
        return vo;
    }

    public static ResultVO buildSuccess() {
        ResultVO vo = new ResultVO();
        vo.result = SUCCESS;
        return vo;
    }

    public static ResultVO buildSuccessWithTradeId(String id) {
        ResultVO vo = new ResultVO();
        vo.result = SUCCESS;
        vo.message = id;
        return vo;
    }

    public static ResultVO buildInvalid(String message) {
        ResultVO vo = new ResultVO();
        vo.message = message;
        vo.result = INVALID;
        return vo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
