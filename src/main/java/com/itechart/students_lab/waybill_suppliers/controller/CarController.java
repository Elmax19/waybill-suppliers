package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.CarStatus;
import com.itechart.students_lab.waybill_suppliers.entity.dto.AddressDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.CarDto;
import com.itechart.students_lab.waybill_suppliers.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CarController {
    private final CarService carService;

    @PreAuthorize("hasAuthority('cars:read')")
    @GetMapping("/customer/{id}/cars")
    ResponseEntity<List<CarDto>> getByPage(
            @Min(value = 1L, message = "Customer id must be positive number")
            @PathVariable Long id,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        List<CarDto> cars = minCapacity == null
                ? carService.findByPage(page, size, id)
                : carService.findByPageAndMinCapacity(page, size, id, minCapacity);
        return cars.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('cars:read')")
    @GetMapping("/car/{id}")
    ResponseEntity<CarDto> getById(@Min(value = 1L,
            message = "Car id must be positive number")
                                   @PathVariable Long id) {
        CarDto car = carService.findById(id);
        return car == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(car, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('cars:write')")
    @PostMapping("/car")
    @ResponseStatus(code = HttpStatus.CREATED)
    CarDto create(@Valid @RequestBody CarDto carDto) {
        return carService.create(carDto);
    }

    @PreAuthorize("hasAuthority('cars:write')")
    @DeleteMapping("/cars")
    void removeWarehouses(@NotNull(message = "At least one car's id must be specified")
                          @RequestParam(required = false) List<Long> id) {
        carService.deleteByIdIn(id);
    }

    @PreAuthorize("hasAuthority('cars:write')")
    @PatchMapping("/car/{id}/status")
    void setCarStatus(@Min(value = 1L, message = "Car id must be positive number")
                      @PathVariable Long id,
                      @NotNull(message = "Car status must be specified (s parameter)")
                      @RequestParam(required = false) CarStatus s) {
        carService.updateCarStatus(id, s);
    }

    @PreAuthorize("hasAuthority('cars:write')")
    @PatchMapping("/car/{id}/address")
    void setCarLastAddress(@Min(value = 1L, message = "Car id must be positive number")
                           @PathVariable Long id,
                           @Valid @RequestBody AddressDto addressDto) {
        carService.updateCarLastAddress(id, addressDto);
    }
}
