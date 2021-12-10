package com.itechart.students_lab.waybill_suppliers.controller.filter;

import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.exception.NoAccessException;
import com.itechart.students_lab.waybill_suppliers.repository.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Component
@WebFilter("/customer/*")
@Order(1)
@RequiredArgsConstructor
public class EmployeeBelongsCustomerFilter implements Filter {
    private final EmployeeRepo employeeRepo;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath();
        if (path.startsWith("/customer/")) {
            Principal principal = req.getUserPrincipal();
            Employee employee = employeeRepo.findByLogin(principal.getName());
            if (!employee.getCustomer().getId().equals(Long.parseLong(String.valueOf(path.split("/")[2])))) {
                resolver.resolveException(req, resp, null, new NoAccessException("No access for not your customer"));
            }
        }
        chain.doFilter(request, response);
    }
}

