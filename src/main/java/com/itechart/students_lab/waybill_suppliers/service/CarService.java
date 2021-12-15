package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.Car;
import com.itechart.students_lab.waybill_suppliers.entity.CarStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.dto.AddressDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.CarDto;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.mapper.CarMapper;
import com.itechart.students_lab.waybill_suppliers.repository.CarRepo;
import com.itechart.students_lab.waybill_suppliers.utils.ExceptionMessageParser;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private static final String CAR_WITH_ID_NOT_FOUND = "Car with id %d not found";
    private static final String CAR_WITH_NUMBER_EXISTS = "Car with such number exists";
    private static final String UNKNOWN_CAR_STATUS = "Unknown car status ";
    private static final String CAR_CUSTOMER_DEACTIVATED = "Car's customer is deactivated";

    private final CarRepo carRepo;
    private final CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    private final CustomerService customerService;
    private final AddressService addressService;

    public Optional<String> processSQLIntegrityConstraintViolationException
            (SQLIntegrityConstraintViolationException e) {
        String message = e.getLocalizedMessage();
        if (message.startsWith("Duplicate entry")) {
            String[] tableAndColumn = ExceptionMessageParser.parseSqlDuplicateEntryMessage(message);
            if ("car".equals(tableAndColumn[0])) {
                return Optional.of(CAR_WITH_NUMBER_EXISTS);
            }
        }
        return Optional.empty();
    }

    public Optional<String> processHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String message = e.getLocalizedMessage();
        if (message.startsWith("JSON parse error") && message.contains("CarStatus")) {
            return Optional.of(UNKNOWN_CAR_STATUS);
        }
        return Optional.empty();
    }

    public List<CarDto> findByPage(int page, int size, Long customerId) {
        Example<Car> carExample = Example.of(new Car(new Customer(customerId)));
        return carMapper.carsListToCarDtoList(
                carRepo.findAll(carExample,
                        PageRequest.of(page, size)).getContent());
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

        Address address = car.getLastAddress();
        address = addressService.find(address).orElse(address);
        car.setLastAddress(address);

        car = carRepo.save(car);
        return carMapper.carToCarDto(car);
    }

    @Transactional
    public void deleteByIdIn(List<Long> ids) {
        carRepo.deleteByIdIn(ids);
    }

    public void updateCarStatus(Long id, CarStatus status) {
        Car car = getActiveCustomerCar(id);
        car.setStatus(status);
        carRepo.save(car);
    }

    public void updateCarLastAddress(Long id, AddressDto addressDto) {
        Car car = getActiveCustomerCar(id);

        Address address = carMapper.addressDtoToAddress(addressDto);
        address = addressService.find(address).orElse(address);
        car.setLastAddress(address);

        carRepo.save(car);
    }

    public Car getActiveCustomerCar(Long id) {
        Car car = carRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(CAR_WITH_ID_NOT_FOUND, id)));
        if (car.getCustomer().getActiveStatus() == ActiveStatus.INACTIVE) {
            throw new ServiceException(HttpStatus.CONFLICT, CAR_CUSTOMER_DEACTIVATED);
        }
        return car;
    }
}
