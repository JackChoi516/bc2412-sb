package com.bootcamp.demo.demo_sb_customer.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleConfig {

  // @Scheduled(cron = "0 40 17 * * MON")
  // public void testCornJob(){
  //   System.out.println("Test Corn Job");
  // }

  // @Scheduled(fixedDelay = 4000) // wait 9000 ms
  // public void sayHello() throws Exception{
  //   System.out.println(System.currentTimeMillis());
  //   Thread.sleep(5000);
  //   System.out.println("Hello World!");
  // }

  // @Scheduled(fixedDelay = 2000) // 2000 ms
  // public void sayGoodBye(){
  //   System.out.println("Good Bye!");
  // }

  // @Scheduled(fixedRate = 4000) // wait 5000 ms
  // public void sayGoodBye() throws Exception{
  //   System.out.println(System.currentTimeMillis());
  //   Thread.sleep(5000);
  //   System.out.println("ABCD!");
  // }
}
