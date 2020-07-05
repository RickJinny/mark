package com.rickjinny.mark.controller.p22_apidesign.t01_apiresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    private String status;

    private Long orderId;
}
