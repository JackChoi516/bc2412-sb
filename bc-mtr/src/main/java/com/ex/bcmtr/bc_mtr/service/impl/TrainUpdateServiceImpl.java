package com.ex.bcmtr.bc_mtr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ex.bcmtr.bc_mtr.model.MtrDataDto;
import com.ex.bcmtr.bc_mtr.service.TrainUpdateService;
@Service
public class TrainUpdateServiceImpl implements TrainUpdateService{
  @Autowired
  private RestTemplate restTemplate;

  @Value("${api.mtr.domain}")
  private String domain;

  @Value("${api.mtr.endpoints.getSchedule}")
  private String getSchedule;

  @Override
  public MtrDataDto getMtrDataDto(String line, String station){
    // String baseUrl = "https://rt.data.gov.hk/v1/transport/mtr/getSchedule.php";
    String url = UriComponentsBuilder.newInstance().scheme("https") //
      .host(domain).path(getSchedule)
      .queryParam("line", line)
      .queryParam("sta", station).build()
      .toUriString();


    // String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
    //         .queryParam("line", line)
    //         .queryParam("sta", station)
    //         .toUriString();
    MtrDataDto result = this.restTemplate.getForObject(url, MtrDataDto.class);
    MtrDataDto.LineStation lineStation = result.getData().get(line + "-" + station);
    return result;
  }  
}
