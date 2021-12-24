package com.itechart.students_lab.waybill_suppliers.validation;

import com.itechart.students_lab.waybill_suppliers.entity.Application;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.UserRole;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class WaybillValidator {
    private static final String TRYING_ADD_TO_ONE_CUSTOMER_WAYBILL_APPLICATIONS_OF_ANOTHER
            = "Trying to add to one's customer waybill applications of another";
    private static final String TRYING_ADD_TO_ONES_WAREHOUSE_WAYBILL_APPLICATIONS_OF_ANOTHER
            = "Trying to add to one's warehouse waybill applications of another";
    private static final String EMPLOYEE_ROLE_NOT_MATCH
            = "Some employee roles don't match needed";
    private static final String SOME_APPLICATIONS_IN_ANOTHER_WAYBILLS
            = "Some of the specified applications are already in another waybills";

    public void waybillApplicationCompatibilityConstraint(Warehouse waybillWarehouse,
                                                          Set<Application> waybillApplications) {
        Long waybillWarehouseId = waybillWarehouse.getId();
        Long waybillCustomerId = waybillWarehouse.getCustomer().getId();

        for (Application a : waybillApplications) {
            Warehouse applicationWarehouse = a.getWarehouse();

            Long applicationCustomerId = applicationWarehouse.getCustomer().getId();
            if (!applicationCustomerId.equals(waybillCustomerId)) {
                throw new ServiceException(HttpStatus.CONFLICT,
                        TRYING_ADD_TO_ONE_CUSTOMER_WAYBILL_APPLICATIONS_OF_ANOTHER);
            }

            Long applicationWarehouseId = applicationWarehouse.getId();
            if (!applicationWarehouseId.equals(waybillWarehouseId)) {
                throw new ServiceException(HttpStatus.CONFLICT,
                        TRYING_ADD_TO_ONES_WAREHOUSE_WAYBILL_APPLICATIONS_OF_ANOTHER);
            }
        }
    }

    public void employeeRoleCompatibilityConstraint(Employee creator,
                                                    Employee updater,
                                                    Employee driver) {
        if (creator != null && creator.getRole() != UserRole.ROLE_LOGISTICS_SPECIALIST
                || updater != null && updater.getRole() != UserRole.ROLE_LOGISTICS_SPECIALIST
                || driver != null && driver.getRole() != UserRole.ROLE_DRIVER) {
            throw new ServiceException(HttpStatus.CONFLICT, EMPLOYEE_ROLE_NOT_MATCH);
        }
    }

    public void noInWaybillApplicationsConstraint(Set<Application> applications) {
        if (applications.stream().anyMatch(a -> a.getWaybill() != null)) {
            throw new ServiceException(HttpStatus.CONFLICT, SOME_APPLICATIONS_IN_ANOTHER_WAYBILLS);
        }
    }
}
