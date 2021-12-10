package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.CarStatus;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CarDto {
    @Min(value = 1L, message = "Car id must be positive number")
    private Long id;
    @NotBlank(message = "Car number must not be empty")
    private String carNumber;
    @Valid
    @NotNull(message = "Car last location must not be null")
    private AddressDto lastAddress;
    @NotNull(message = "Car total capacity must be specified")
    @Min(value = 0, message = "Car total capacity must be positive number")
    private Integer totalCapacity;
    private CarStatus status = CarStatus.FREE;
    @Min(value = 1L, message = "Car customer id must be positive number")
    private Long customerId;
}
