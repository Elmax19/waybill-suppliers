package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.Car;
import com.itechart.students_lab.waybill_suppliers.entity.CarStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.dto.CarDto;
import com.itechart.students_lab.waybill_suppliers.mapper.CarMapper;
import com.itechart.students_lab.waybill_suppliers.repository.AddressRepo;
import com.itechart.students_lab.waybill_suppliers.repository.CarRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private static final String CAR_WITH_ID_NOT_FOUND = "Car with id %d not found";
    private static final String CAR_WITH_NUMBER_EXISTS = "Car with such number exists";

    private final CarRepo carRepo;
    private final CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    private final CustomerService customerService;
    private final AddressRepo addressRepo;

    public List<CarDto> findByPage(int page, int size, Long customerId) {
        Example<Car> carExample = Example.of(new Car(new Customer(customerId)));
        return carMapper.carsListToCarDtoList(
                carRepo.findAll(carExample,
                        PageRequest.of(page, size, Sort.by("carNumber"))).getContent());
    }

    public CarDto findById(Long id) {
        Car car = carRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format(CAR_WITH_ID_NOT_FOUND, id)));
        return carMapper.carToCarDto(car);
    }

    public CarDto create(CarDto carDto) {
        Car car = carMapper.carDtoToCar(carDto);
        Customer customer = customerService.getActiveCustomer(carDto.getCustomerId());
        car.setCustomer(customer);
        car.setStatus(carDto.getStatus());
        car = carRepo.save(car);
        return carMapper.carToCarDto(car);
    }

    @Transactional
    public void deleteByIdIn(List<Long> ids) {
        carRepo.deleteByIdIn(ids);
    }

    @Transactional
    public void setCarsStatus(List<Long> id, CarStatus status) {
        carRepo.updateCarsStatus(id, status);
    }
}
