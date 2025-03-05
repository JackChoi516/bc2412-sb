package com.bootcamp.demo.demo_sb_customer;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bootcamp.demo.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.demo.demo_sb_customer.controller.impl.CustomerController;
import com.bootcamp.demo.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.demo.demo_sb_customer.service.CustomerService;
import com.bootcamp.demo.demo_sb_customer.service.impl.CustomerServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


// Every Test file is a testing env,with your target bean cyvle.
// ! For Unit Test, we don't need service & repository layer (bean).
// Contest include all web related beans ONLY.
@WebMvcTest(controllers = CustomerController.class) 
class CustomerControllerTest {
  @MockBean
  private CustomerService customerService;

  // ! @WebMvcTest inject MockMvc Bean into context
  @Autowired
  private MockMvc mockMvc; // pretent Postman

  @Test
  void testGetAllCustomers() throws Exception{
    // Mock behavior for mock bean
    CustomerEntity customerEntity1 = CustomerEntity.builder().email("test123@gmail.com")
      .name("testname1").id(99L).build();
    CustomerEntity customerEntity2 = CustomerEntity.builder().email("test234@gmail.com")
      .name("testname1").id(100L).build();
    List<CustomerEntity> serviceResult = Arrays.asList(customerEntity1, customerEntity2);

    // Assume the behavior/result for the mock bean
    Mockito.when(customerService.getCustomers()).thenReturn(serviceResult);

    // Test
    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/customers"));

    // vertify result
    result.andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    
    // To check the data:
    // Approach 1
    result.andExpect(jsonPath("$.code").value("000000"))
      .andExpect(jsonPath("$.message").value("Success."))
      .andExpect(jsonPath("$.data[0].name").value("testname1"))
      .andExpect(jsonPath("$.data[0].email").value("test123@gmail.com"));

    // Approach 2
    String json = result.andReturn().getResponse().getContentAsString();
    // JSON -> Object (Deserialization)
    ApiResp<List<CustomerEntity>> customerEntityArray = //
      new ObjectMapper().readValue(json, new TypeReference<ApiResp<List<CustomerEntity>>>(){});

    System.out.println(json);

    assertThat(customerEntityArray.getCode(), Matchers.is("000000"));
    assertThat(customerEntityArray.getMessage(), Matchers.is("Success."));

    List<CustomerEntity> customerEntities = customerEntityArray.getData();

    assertThat(customerEntities, Matchers.containsInAnyOrder(
        Matchers.hasProperty("email", Matchers.equalTo("test123@gmail.com")),
        Matchers.hasProperty("email", Matchers.equalTo("test234@gmail.com"))
    ));
    // assertThat(customerEntities, Matchers.hasItem(customerEntity2));
  }

  @Test
  void testCreateCustomer() throws Exception {
    // Mock behavior (Pass Mary return John)
    CustomerEntity customerEntity1 = CustomerEntity.builder() //
      .name("John").id(10L).email("John@gmail.com").build();

    CustomerEntity customerEntityRequest = CustomerEntity.builder() //
    .name("Mary").id(10L).email("Mary@gmail.com").build();

    Mockito.when(customerService.createCustomer(customerEntityRequest)).thenReturn(customerEntity1);

    // ! Prepare Request Body Json (Mary)
    // Serialization
    String jsonRequestBody = 
        new ObjectMapper().writeValueAsString(customerEntityRequest);

    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/customer") //
      .contentType(MediaType.APPLICATION_JSON_VALUE) //
      .content(jsonRequestBody));

    result.andExpect(status().isCreated())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    // ! Get Response, extract response data to validate
    String json = result.andReturn().getResponse().getContentAsString();

    // Deserialization
    // !!! Key
    ApiResp<CustomerEntity> apiResp = new ObjectMapper().readValue(json, //
      new TypeReference<ApiResp<CustomerEntity>>(){});

    assertThat(apiResp.getCode(), Matchers.is("000000"));
    assertThat(apiResp.getMessage(), Matchers.is("Success."));

    CustomerEntity respData = apiResp.getData();

    assertThat(respData.getName(), Matchers.is("John"));
    assertThat(respData.getEmail(), Matchers.is("John@gmail.com"));

    verify(customerService, times(1)).createCustomer(customerEntityRequest);
  }
}
