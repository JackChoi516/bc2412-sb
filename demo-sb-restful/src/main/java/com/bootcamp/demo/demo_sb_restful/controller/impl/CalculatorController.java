package com.bootcamp.demo.demo_sb_restful.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bootcamp.demo.demo_sb_restful.model.Operation;
import com.bootcamp.demo.demo_sb_restful.service.CalculatorService;

// ! sum -> 1 + 2 -> return 3
// ! subtract -> 1 - 2 -> return -1
// ! multiply -> 1 * 2 -> return 2
// ! divide -> 9 / 3 -> return 3
@RestController // ! @RestCintroller = @Controller + @ReponseBody
// @Controller -> During Server Start -> Create an object of CalculatorController -> put it into context
@RequestMapping(value = "/v1")
public class CalculatorController {

  @Autowired
  private CalculatorService calculatorService;

  // http://localhost:8082/operation/sum?x=1&y=2
  @GetMapping(value = "/operation/{operation}")
  public Integer operate(@PathVariable Operation operation, @RequestParam Integer x, @RequestParam Integer y){
    return this.calculatorService.operate(operation, x, y);
  }

}
