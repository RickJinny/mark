package com.rickjinny.mark;

import com.rickjinny.mark.controller.p22_apidesign.t02_apiversion.APIVersionHandlerMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class MarkApplication implements WebMvcRegistrations {

	public static void main(String[] args) {
		SpringApplication.run(MarkApplication.class, args);
	}

	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new APIVersionHandlerMapping();
	}
}
