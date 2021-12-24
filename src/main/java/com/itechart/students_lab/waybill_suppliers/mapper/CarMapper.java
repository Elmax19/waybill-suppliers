package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Car;
import com.itechart.students_lab.waybill_suppliers.entity.dto.CarDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "customerId", expression = "java(car.getCustomer().getId())")
    CarDto carToCarDto(Car car);

    Car carDtoToCar(CarDto carDto);

    List<CarDto> carsListToCarDtoList(List<Car> cars);
}
