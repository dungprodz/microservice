package com.example.profileservice.controller;

import com.example.commonservice.ultis.CommonFunction;
import com.example.profileservice.data.Profile;
import com.example.profileservice.model.ProfileDTO;
import com.example.profileservice.model.requestbody.CreateProfileRequestBody;
import com.example.profileservice.model.requestbody.DuplicateRequestBody;
import com.example.profileservice.model.responsebody.GetProfileResponseBody;
import com.example.profileservice.service.ProfileService;
import com.example.profileservice.ulti.Common;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {
    Gson gson = new Gson();
    private final ProfileService profileService;
    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    @GetMapping
    public ResponseEntity<Flux<GetProfileResponseBody>> getAllProfile(){
        return ResponseEntity.ok(profileService.getAllProfile());
    }

    @PostMapping("/checkDuplicate")
    public ResponseEntity<Mono<Boolean>> checkDuplicate(@RequestBody DuplicateRequestBody requestBody){
        return ResponseEntity.ok(profileService.checkDuplicate(requestBody.getEmail()));
    }

//    @PostMapping("/create")
//    public ResponseEntity<Mono<ProfileDTO>> createNewProfile(@RequestBody ProfileDTO profileDTO){
//        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createNewProfile(profileDTO));
//    }

    @PostMapping("/create")
    public ResponseEntity<Mono<ProfileDTO>> createNewProfile(@RequestBody String requestStr){
        Gson gson = new Gson();
        InputStream inputStream = ProfileController.class.getClassLoader().getResourceAsStream(Common.JSON_REQ_CREATE_PROFILE);
        CommonFunction.jsonValidate(inputStream,requestStr);
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createNewProfile(gson.fromJson(requestStr, ProfileDTO.class)));
    }
}
