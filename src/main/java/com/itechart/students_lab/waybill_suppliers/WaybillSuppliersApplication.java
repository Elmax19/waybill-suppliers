package com.itechart.students_lab.waybill_suppliers;

import com.itechart.students_lab.waybill_suppliers.config.ModelMapperConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class WaybillSuppliersApplication {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(ModelMapperConfig.class);
		context.refresh();
		SpringApplication.run(WaybillSuppliersApplication.class, args);
	}

}
