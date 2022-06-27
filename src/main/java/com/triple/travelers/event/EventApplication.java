package com.triple.travelers.event;

import com.triple.travelers.event.controller.EventController;
import com.triple.travelers.event.dao.ReviewEventDao;
import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.service.logic.ReviewEventServiceLogic;
import com.triple.travelers.event.service.logic.UserServiceLogic;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class EventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventApplication.class, args);
    }

}
