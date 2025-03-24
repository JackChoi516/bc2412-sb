package com.bootcamp.demo.goodbye.demo_sb_goodbye.controller;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodbyeController {
  @GetMapping(value = "/ipad/goodbye")
  public String goodbye(){
    return "Goodbye!";
  }

  public static void main(String[] args) {
    LocalDateTime result1 = LocalDateTime.ofInstant(Instant.ofEpochSecond(1742780911), ZoneId.systemDefault());
    System.out.println(result1 + " " + result1.getDayOfWeek());
    LocalDateTime result2 = LocalDateTime.ofInstant(Instant.ofEpochSecond(1742780613), ZoneId.systemDefault());
    System.out.println(result2 + " " + result2.getDayOfWeek());

    Long start = LocalDateTime.of(2005, 1, 1, 00, 00, 00).atZone(ZoneId.systemDefault()).toEpochSecond();
    System.out.println("start: " + start + " " + LocalDateTime.ofInstant(Instant.ofEpochSecond(start), ZoneId.systemDefault()).getDayOfWeek().toString());
    
    Long end = LocalDateTime.of(2025, 03, 20, 23, 59, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
    System.out.println("end: " + end + " " + LocalDateTime.ofInstant(Instant.ofEpochSecond(end), ZoneId.systemDefault()).getDayOfWeek().toString());

    // //
    // System.out.println(LocalDate.now(ZoneId.systemDefault()).getYear());
    // System.out.println(LocalDateTime.of(2025, 3, 1, 00, 00, 00));
    // System.out.println(LocalDate.of(2025, 1, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).atTime(0, 0).atZone(ZoneId.systemDefault()).toEpochSecond());

    // Long thisWkMon = LocalDate.now().with(DayOfWeek.MONDAY).atTime(0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();
    // System.out.println(thisWkMon);

    // System.out.println(LocalDate.now(ZoneId.systemDefault()).minusMonths(60).getMonthValue());

  }

}
