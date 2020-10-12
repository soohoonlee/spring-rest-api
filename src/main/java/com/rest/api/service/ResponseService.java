package com.rest.api.service;

import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.ListResult;
import com.rest.api.model.response.SingleResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rest.api.service.ResponseService.CommonResponse.FAIL;
import static com.rest.api.service.ResponseService.CommonResponse.SUCCESS;

@Service
public class ResponseService {

    @AllArgsConstructor
    @Getter
    public enum CommonResponse {

        SUCCESS(0, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");

        int code;
        String message;
    }

    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    public CommonResult getFailResult() {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(FAIL.code);
        result.setMessage(FAIL.message);
        return result;
    }

    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(SUCCESS.code);
        result.setMessage(SUCCESS.message);
    }
}
