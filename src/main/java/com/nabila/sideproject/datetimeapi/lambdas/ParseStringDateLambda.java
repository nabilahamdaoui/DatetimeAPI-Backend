package com.nabila.sideproject.datetimeapi.lambdas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nabila.sideproject.datetimeapi.DateTimeAPI;
import com.nabila.sideproject.datetimeapi.lambdas.dto.Request;

public class ParseStringDateLambda implements RequestHandler<Request, String> {

    @Override
    public String handleRequest(Request request, Context context) {
        return DateTimeAPI.parse(request.getInput(), request.getNow()).toString();
    }
}
