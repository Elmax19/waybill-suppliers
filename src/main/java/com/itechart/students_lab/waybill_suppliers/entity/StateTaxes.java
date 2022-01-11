package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class StateTaxes {

    private final Map<String, Double> stateTaxes = Map.ofEntries(
           Map.entry("Alabama", 4.000),
            Map.entry("Alaska", 0.000),
            Map.entry("Arizona", 5.600),
            Map.entry("Arkansas", 6.500),
            Map.entry("California", 7.250),
            Map.entry("Colorado", 2.900),
            Map.entry("Connecticut", 6.350),
            Map.entry("Delaware", 0.000),
            Map.entry("Columbia", 6.000),
            Map.entry("European Union", 0.000),
            Map.entry("Florida", 6.000),
            Map.entry("Georgia", 4.000),
            Map.entry("Hawaii", 4.000),
            Map.entry("Idaho", 6.000),
            Map.entry("Illinois", 6.250),
            Map.entry("Indiana", 7.000),
            Map.entry("Iowa", 6.000),
            Map.entry("Kansas", 6.500),
            Map.entry("Kentucky", 6.000),
            Map.entry("Louisiana", 4.450),
            Map.entry("Maine", 5.500),
            Map.entry("Maryland", 6.000),
            Map.entry("Massachusetts", 6.250),
            Map.entry("Michigan", 6.000),
            Map.entry("Minnesota", 6.875),
            Map.entry("Mississippi", 7.000),
            Map.entry("Missouri", 4.225),
            Map.entry("Montana", 0.000),
            Map.entry("Nebraska", 5.500),
            Map.entry("Nevada", 6.850),
            Map.entry("New Hampshire", 0.000),
            Map.entry("New Jersey", 6.625),
            Map.entry("New Mexico", 5.125),
            Map.entry("New York", 4.000),
            Map.entry("New Carolina", 4.750),
            Map.entry("North Dakota", 5.000),
            Map.entry("Ohio", 5.750),
            Map.entry("Oklahoma", 4.500),
            Map.entry("Oregon", 0.000),
            Map.entry("Pennsylvania", 6.000),
            Map.entry("Puerto Rice", 0.000),
            Map.entry("Rhode Island", 11.500),
            Map.entry("South Carolina", 6.000),
            Map.entry("South Dakota", 4.500),
            Map.entry("Tennessee", 7.000),
            Map.entry("Texas", 6.250),
            Map.entry("Utah", 4.850),
            Map.entry("Vermont", 6.000),
            Map.entry("Virginia", 4.300),
            Map.entry("Washington", 6.500),
            Map.entry("West Virginia", 6.000),
            Map.entry("Wisconsin", 5.000),
            Map.entry("Wyoming", 4.000)
    );

    public Double getTaxByState(String state){
        return stateTaxes.get(state);
    }
}
