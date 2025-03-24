package com.bootcamp.demo_swagger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bootcamp.demo_swagger.dto.StockDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface StockOperation {
  @Operation(summary = "Get Stock by Symbol.", 
  description = "This endpoint is to get stock by symbol from DB.")
  @ApiResponses({ //
    @ApiResponse(responseCode = "200", 
      content = {
      @Content(schema = @Schema(implementation = StockDTO.class), 
      mediaType = MediaType.APPLICATION_JSON_VALUE)}),
    @ApiResponse(responseCode = "400", 
      content = {@Content(schema = @Schema())}) //
  })
  @GetMapping(value = "/stock")
  @ResponseStatus(value = HttpStatus.OK)
  public StockDTO getStock(@RequestParam String symbol);

  @Operation(summary = "Create Stock.", 
  description = "This endpoint is to create a new stock in DB.")
  @ApiResponses({ //
    @ApiResponse(responseCode = "201", 
      content = {
      @Content(schema = @Schema(implementation = StockDTO.class), 
      mediaType = MediaType.APPLICATION_JSON_VALUE)}),
    @ApiResponse(responseCode = "400", 
      content = {@Content(schema = @Schema())}) //
  })
  @PostMapping(value = "/stock")
  @ResponseStatus(value = HttpStatus.CREATED) // 201
  public StockDTO createStock(@RequestBody StockDTO stockDTO);

  @DeleteMapping(value = "/stock")
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteStock(@RequestParam String symbol);

  @PutMapping(value = "/stock")
  @ResponseStatus(value = HttpStatus.OK)
  public StockDTO updateStock(@RequestParam String symbol, @RequestBody StockDTO stockDTO);

  @PatchMapping(value = "/stock")
  @ResponseStatus(value = HttpStatus.OK)
  public StockDTO patchStockDesvription(@RequestParam String symbol, @RequestParam String stockDescription);
}
