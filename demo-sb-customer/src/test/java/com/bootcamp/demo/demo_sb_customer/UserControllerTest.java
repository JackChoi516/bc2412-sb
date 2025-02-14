package com.bootcamp.demo.demo_sb_customer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bootcamp.demo.demo_sb_customer.controller.impl.UserController;
import com.bootcamp.demo.demo_sb_customer.dto.UserDTO;
import com.bootcamp.demo.demo_sb_customer.dto.mapper.UserDTOMapper;
import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;
import com.bootcamp.demo.demo_sb_customer.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.MatcherAssert.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
  @MockBean
  private UserServiceImpl userServiceImpl; // 10 method, mock 10 method

  @SpyBean
  private UserDTOMapper userDTOMapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void contextLoads(){

  }

  @Test
  void testGetUsers() throws Exception{
    UserDto userDto1 = UserDto.builder().id(1L).name("John Wong").username("John").build();
    UserDto userDto2 = UserDto.builder().id(2L).name("Mary Chan").username("Mary").build();
    List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

    Mockito.when(this.userServiceImpl.getUsers()).thenReturn(userDtos);

    ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/users"));
    result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

    String json = result.andReturn().getResponse().getContentAsString();

    List<UserDTO> userDTOs = new ObjectMapper().readValue(json, new TypeReference <List<UserDTO>>(){});

    assertThat(userDTOs, Matchers.containsInAnyOrder(
        Matchers.hasProperty("name", Matchers.equalTo("John Wong")),
        Matchers.hasProperty("name", Matchers.equalTo("Mary Chan"))
    ));

    assertThat(userDTOs, Matchers.containsInAnyOrder(Matchers.instanceOf(UserDTO.class)));
  }

}
