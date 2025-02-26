package com.ex.bcmtr.bc_mtr.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ex.bcmtr.bc_mtr.dto.EarliestDTO;
import com.ex.bcmtr.bc_mtr.dto.LineSignalDTO;
import com.ex.bcmtr.bc_mtr.dto.mapper.DTOMapper;
import com.ex.bcmtr.bc_mtr.model.MtrDataDto;
import com.ex.bcmtr.bc_mtr.model.MtrLine;
import com.ex.bcmtr.bc_mtr.model.MtrStation;
import com.ex.bcmtr.bc_mtr.service.MtrLineDataService;
import com.ex.bcmtr.bc_mtr.service.TrainUpdateService;
@Service
public class TrainUpdateServiceImpl implements TrainUpdateService{
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private DTOMapper dtoMapper;
  @Autowired
  private MtrLineDataService mtrLineDataService;

  @Value("${api.mtr.domain}")
  private String domain;

  @Value("${api.mtr.endpoints.getSchedule}")
  private String getSchedule;

  @Override
  public MtrDataDto getMtrDataDto(String line, String station){
    String url = UriComponentsBuilder.newInstance().scheme("https") //
      .host(domain).path(getSchedule)
      .queryParam("line", line)
      .queryParam("sta", station).build()
      .toUriString();

    MtrDataDto result = this.restTemplate.getForObject(url, MtrDataDto.class);
    // MtrDataDto.LineStation lineStation = result.getData().get(line + "-" + station);
    return result;
  }

  @Override
  public EarliestDTO getEarliest(String line, String station){
    MtrDataDto mtrDataDto = this.getMtrDataDto(line, station);

     List<EarliestDTO.TrainData> trainData = new ArrayList<>();
    Set<String> destUp = new HashSet<>();
    Set<String> destDn = new HashSet<>();
    mtrDataDto.getData().values().stream().forEach(e -> {
      e.getUP().stream().forEach(u -> {
        if (Integer.valueOf(u.getSeq()) == 1){
          trainData.add(this.dtoMapper.map("UP", u));
          destUp.add(u.getDest().toString());
        }
      });
      e.getUP().stream().forEach(u2 -> {
        if (destUp.add(u2.getDest().toString())){
          trainData.add(this.dtoMapper.map("UP", u2));
        }
      });

      e.getDOWN().stream().forEach(d -> {
        if (Integer.valueOf(d.getSeq()) == 1){
          trainData.add(this.dtoMapper.map("DOWN", d));
          destDn.add(d.getDest().toString());
        }
      });
      e.getDOWN().stream().forEach(d2 -> {
        if (destDn.add(d2.getDest().toString())){
          trainData.add(this.dtoMapper.map("DOWN", d2));
        }
      });
    });

    return EarliestDTO.builder().curr_time(mtrDataDto.getCurr_time()) //
      .sys_time(mtrDataDto.getSys_time()) //
      .currentStation(station).trains(trainData).build();
  }

  @Override
  public LineSignalDTO getLineSignal(String line){
    LineSignalDTO result = new LineSignalDTO();
    result.setLine(line);
    result.setDelayedStation(new ArrayList<>());
    
    MtrLine mtrLine = MtrLine.valueOf(line);
    List<String> stations = this.mtrLineDataService.getByLine(mtrLine) //
      .stream().map(e -> e.toString()).collect(Collectors.toList());
    
    stations.stream().forEach(e ->{
      if (this.getMtrDataDto(line, e).getIsdelay().equals("Y")){
        result.getDelayedStation().add(e);
      }
    });

    MtrDataDto data = this.getMtrDataDto(line, stations.get(0));
    result.setCurr_time(data.getCurr_time());
    result.setSys_time(data.getSys_time());
    if (result.getDelayedStation().size() == 0){
      result.setSignal("GREEN");
    }else if (result.getDelayedStation().size() == 1){
      result.setSignal("YELLOW");
    }else {
      result.setSignal("RED");
    }
    return result;
  }

  @Override
  public List<LineSignalDTO> getAllLineSignals(){
    Map<MtrLine, List<MtrStation>> allLines = this.mtrLineDataService.getAllLines();
    List<LineSignalDTO> result = new ArrayList<>();
    for (MtrLine line : allLines.keySet()){
      result.add(this.getLineSignal(line.toString()));
    }
    return result;
  }
}
