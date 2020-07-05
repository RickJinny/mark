package com.rickjinny.mark.controller.p22_apidesign.t03_headerapiversion;

import lombok.Getter;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

public class APIVersionCondition implements RequestCondition<APIVersionCondition> {

    @Getter
    private String apiVersion;

    @Getter
    private String headerKey;

    public APIVersionCondition(String apiVersion, String headerKey) {
        this.apiVersion = apiVersion;
        this.headerKey = headerKey;
    }

    @Override
    public APIVersionCondition combine(APIVersionCondition apiVersionCondition) {
        return new APIVersionCondition(apiVersionCondition.getApiVersion(), apiVersionCondition.getHeaderKey());
    }

    @Override
    public APIVersionCondition getMatchingCondition(HttpServletRequest request) {
        String version = request.getHeader(headerKey);
        return apiVersion.equals(version) ? this : null;
    }

    @Override
    public int compareTo(APIVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        return 0;
    }
}
