package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.Car;
import com.itechart.students_lab.waybill_suppliers.entity.CarStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.dto.AddressDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.CarDto;
import com.itechart.students_lab.waybill_suppliers.mapper.CarMapper;
import com.itechart.students_lab.waybill_suppliers.repository.CarRepo;
import com.itechart.students_lab.waybill_suppliers.utils.ExceptionMessageParser;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private static final String CAR_WITH_ID_NOT_FOUND = "Car with id %d not found";
    private static final String CAR_WITH_NUMBER_EXISTS = "Car with such number exists";
    private static final String UNKNOWN_CAR_STATUS = "Unknown car status ";

    private final CarRepo carRepo;
    private final CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    private final CustomerService customerService;

    public String processSQLIntegrityConstraintViolationException
            (SQLIntegrityConstraintViolationException e) {
        String message = e.getLocalizedMessage();
        if (message.startsWith("Duplicate entry")) {
            String[] tableAndColumn = ExceptionMessageParser.parseSqlDuplicateEntryMessage(message);
            if ("car".equals(tableAndColumn[0])) {
                return CAR_WITH_NUMBER_EXISTS;
            }
        }
        return null;
    }

    public String processHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String message = e.getLocalizedMessage();
        if (message.startsWith("JSON parse error")) {
            return UNKNOWN_CAR_STATUS;
        }
        return null;
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
        car = carRepo.save(car);
        return carMapper.carToCarDto(car);
    }

    @Transactional
    public void deleteByIdIn(List<Long> ids) {
        carRepo.deleteByIdIn(ids);
    }

    @Transactional
    public void updateCarStatus(Long id, String statusString) {
        CarStatus status = CarStatus.of(statusString).orElseThrow(
                () -> new EntityNotFoundException(UNKNOWN_CAR_STATUS + statusString));
        carRepo.updateCarStatus(id, status);
    }

    @Transactional
    public void updateCarLastAddress(Long id, AddressDto addressDto) {
        Address address = carMapper.addressDtoToAddress(addressDto);
        carRepo.updateCarLastAddress(id, address.getState(), address.getCity(),
                address.getFirstAddressLine(), address.getSecondAddressLine());
    }
}
