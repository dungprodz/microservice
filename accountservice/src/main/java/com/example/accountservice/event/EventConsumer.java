package com.example.accountservice.event;

import com.example.accountservice.model.AccountDTO;
import com.example.accountservice.service.AccountService;
import com.example.commonservice.model.ProfileDTO;
import com.example.commonservice.ultis.Constant;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Collections;

@Service
@Slf4j
public class EventConsumer {
    Gson gson = new Gson();

    @Autowired
    AccountService accountService;

    @Autowired
    EventProducer eventProducer;


    public EventConsumer(ReceiverOptions<String,String> receiverOptions){
        KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PROFILE_ONBOARDING_TOPIC)))
                .receive().subscribe(this::profileOnboarding);
    }
    public void profileOnboarding(ReceiverRecord<String,String> receiverRecord){
        System.out.println(receiverRecord.value());
        log.info(receiverRecord.value());
//        log.info("Profile Onboarding event");
//        ProfileDTO dto = gson.fromJson(receiverRecord.value(),ProfileDTO.class);
//        AccountDTO accountDTO = new AccountDTO();
//        accountDTO.setEmail(dto.getEmail());
//        accountDTO.setReserved(0);
//        accountDTO.setBalance(dto.getInitialBalance());
//        accountDTO.setCurrency("USD");
//        accountService.createNewAccount(accountDTO).subscribe(res ->{
//            dto.setStatus(Constant.STATUS_PROFILE_ACTIVE);
//            eventProducer.send(Constant.PROFILE_ONBOARDED_TOPIC,gson.toJson(dto)).subscribe();
//        });
    }

}
