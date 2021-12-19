package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.Waybill;
import com.itechart.students_lab.waybill_suppliers.entity.WaybillState;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillRecordDto;
import com.itechart.students_lab.waybill_suppliers.mapper.WaybillRecordMapper;
import com.itechart.students_lab.waybill_suppliers.repository.WaybillRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaybillService {
    private final WaybillRepo waybillRepo;
    private final WaybillRecordMapper waybillRecordMapper
            = Mappers.getMapper(WaybillRecordMapper.class);

    public List<WaybillRecordDto> findByPage(int page,
                                             int size,
                                             Long customerId,
                                             Long creatorId,
                                             WaybillState state) {
        Example<Waybill> waybillExample = Example.of(
                new Waybill(
                        new Warehouse(
                                new Customer(customerId)
                        ),
                        new Employee(creatorId),
                        state
                )
        );
        return waybillRecordMapper.waybillListToWaybillRecordDtoList(
                waybillRepo.findAll(waybillExample,
                        PageRequest.of(page, size)).getContent());
    }
}
