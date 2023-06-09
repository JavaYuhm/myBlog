package com.myblog.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 타임리프 예제 컨트롤러
 */
@Controller
public class ExampleController {
    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model){
        Person person = new Person();
        person.setId(1L);
        person.setName("test");
        person.setAge(20);
        person.setHobbies(List.of("운동", "독서"));

        model.addAttribute("person", person);
        model.addAttribute("today", LocalDateTime.now());
        return "example";
    }

    @Data
    private class Person {
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}
