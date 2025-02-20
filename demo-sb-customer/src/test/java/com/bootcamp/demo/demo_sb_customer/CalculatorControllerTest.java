package com.bootcamp.demo.demo_sb_customer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bootcamp.demo.demo_sb_customer.controller.impl.CalculatorController;
import com.bootcamp.demo.demo_sb_customer.service.CalculatorService;

@WebMvcTest(controllers = CalculatorController.class)
public class CalculatorControllerTest {
  @MockBean
  private CalculatorService calculatorService;

  @Autowired
  private MockMvc mockMvc;

  // ! this unit test case is to test 2 things:
  // 1. jsonPath("$.value") -> THE RESPONSE MUST BE AN OBJECT
  // 2. the logic is to test if the result of sum() minus the result of subtract()
  @Test
  void calculeTest() throws Exception{
    Mockito.when(this.calculatorService.sum(1, 2)).thenReturn(100);
    Mockito.when(this.calculatorService.subtract(1, 2)).thenReturn(200);
    //test
    mockMvc.perform(MockMvcRequestBuilders.get("/calculate/{x}/{y}", 1, 2))
    .andExpect(jsonPath("$.value").value(-100));

  }
  
}
