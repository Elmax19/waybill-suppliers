package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.Car;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.WriteOff;
import com.itechart.students_lab.waybill_suppliers.entity.WriteOffItem;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WriteOffDto;
import com.itechart.students_lab.waybill_suppliers.exception.BadRequestException;
import com.itechart.students_lab.waybill_suppliers.exception.NotFoundException;
import com.itechart.students_lab.waybill_suppliers.mapper.WriteOffMapper;
import com.itechart.students_lab.waybill_suppliers.repository.CarRepo;
import com.itechart.students_lab.waybill_suppliers.repository.ItemRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WriteOffRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WriteOffService {
    private final WriteOffRepo writeOffRepo;
    private final WarehouseRepo warehouseRepo;
    private final CarRepo carRepo;
    private final ItemRepo itemRepo;
    private final WriteOffMapper writeOffMapper = Mappers.getMapper(WriteOffMapper.class);

    @Transactional
    public WriteOffDto create(WriteOffDto writeOffDto) {
        WriteOff writeOff = writeOffMapper.convertToEntity(writeOffDto);
        writeOffRepo.create(writeOff.getDateTime(), writeOff.getCreatingUser().getId(), writeOff.getWarehouse().getId(), writeOff.getCar().getId());
        int placeUnits = 0;
        for (WriteOffItem writeOffItem : writeOff.getWriteOffItems()){
            placeUnits+=writeOffItem.getAmount()*itemRepo.findById(writeOffItem.getItem().getId()).orElseThrow(
                    () -> new NotFoundException("No such item with id: " + writeOffItem.getItem().getId())).getUnits();
        }
        if(writeOffDto.getCarId()==null){
            Warehouse warehouse = warehouseRepo.findById(writeOff.getWarehouse().getId()).orElseThrow(
                    () -> new NotFoundException("No such warehouse with id: " + writeOff.getWarehouse().getId()));
            if (warehouse.getTotalCapacity()< warehouse.getAvailableCapacity() + placeUnits){
                throw new BadRequestException("No such place " + (warehouse.getAvailableCapacity() + placeUnits) + " in Warehouse");
            }
            warehouseRepo.updateAvailableCapacity(writeOff.getWarehouse().getId(), -placeUnits);
        } else {
            Car car = carRepo.findById(writeOff.getCar().getId()).orElseThrow(
                    () -> new NotFoundException("No such warehouse with id: " + writeOff.getCar().getId()));
            if (car.getTotalCapacity()< car.getAvailableCapacity() + placeUnits){
                throw new BadRequestException("No such place " + (car.getAvailableCapacity() + placeUnits) + " in Warehouse");
            }
            carRepo.updateAvailableCapacity(writeOff.getCar().getId(), -placeUnits);
        }
        return writeOffDto;
    }
}
