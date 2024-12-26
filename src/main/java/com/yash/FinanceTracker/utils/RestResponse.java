package com.yash.FinanceTracker.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {

    T data;
    Boolean success;
    String errorMessage;
    Integer errorCode;
    String meta;
}
