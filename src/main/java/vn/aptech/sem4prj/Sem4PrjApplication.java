package vn.aptech.sem4prj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@SpringBootApplication
public class Sem4PrjApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sem4PrjApplication.class, args);
    }

}
