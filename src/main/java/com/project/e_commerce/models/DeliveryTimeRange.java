package com.project.e_commerce.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;


@Getter
@AllArgsConstructor
public class DeliveryTimeRange {
    private Date fromDate;
    private Date toDate;
}
