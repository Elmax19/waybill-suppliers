package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    @Query("select e from Employee e where e.customer.id=:id")
    Page<Employee> findAllByCustomerId(@Param("id") Long id, Pageable pageable);

    @Query("select e from Employee e where e.customer.id=:id and e.activeStatus='ACTIVE'")
    Page<Employee> findAllByActiveStatus(@Param("id") Long id, Pageable pageable);

    Employee findByLogin(String login);

    @Query("select e.id from Employee e where e.login=:login")
    Long findIdByLogin(String login);

    @Modifying
    @Transactional
    @Query(value = "update user" +
            " set user.is_active = :status" +
            " where user.id IN :ids", nativeQuery = true)
    void setEmployeeActiveStatus(@Param("status") String newStatus,
                                 @Param("ids") List<Long> ids);

    @Query(value = "SELECT * "
            + "FROM user "
            + "WHERE role = 'ROLE_DRIVER' "
            + "AND customer_id = :id "
            + "AND id NOT IN ("
            + "SELECT driver_id "
            + "FROM waybill "
            + "WHERE state != 'FINISHED' "
            + "AND driver_id IS NOT NULL) "
            + "LIMIT :offset, :size", nativeQuery = true)
    List<Employee> findFreeDrivers(@Param("id") Long customerId,
                                   @Param("offset") int offset,
                                   @Param("size") int size);
}
