package com.example.profileservice.service;

import com.example.commonservice.common.CommonException;
import com.example.profileservice.data.Profile;
import com.example.profileservice.model.ProfileDTO;
import com.example.profileservice.model.requestbody.CreateProfileRequestBody;
import com.example.profileservice.model.responsebody.GetProfileResponseBody;
import com.example.profileservice.repository.ProfileRepository;
import com.example.profileservice.ulti.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Flux<GetProfileResponseBody> getAllProfile() {
        return profileRepository.findAll()
                .map(profile -> {
                    GetProfileResponseBody getProfileResponseBody = new GetProfileResponseBody();
                    getProfileResponseBody.setId(profile.getId());
                    getProfileResponseBody.setName(profile.getName());
                    getProfileResponseBody.setEmail(profile.getEmail());
                    getProfileResponseBody.setRole(profile.getRole());
                    getProfileResponseBody.setStatus(profile.getStatus());
                    getProfileResponseBody.setInitialBalance(0);
                    return getProfileResponseBody;
                })
                .switchIfEmpty(Mono.error(new Exception("profile empty")));
    }

    public Mono<Boolean> checkDuplicate(String email) {
        return profileRepository.findByEmail(email)
                .flatMap(profile -> Mono.just(true))
                .switchIfEmpty(Mono.just(false));
    }

    public Mono<Profile> createNewProfile(CreateProfileRequestBody profileRequestBody) {
        return checkDuplicate(profileRequestBody.getEmail())
                .flatMap(aBoolean -> {
                    if(Boolean.TRUE.equals(aBoolean)){
                        return Mono.error(new CommonException("PF02","Duplicate profile !", HttpStatus.BAD_REQUEST));
                    }else{
                        profileRequestBody.setStatus(Common.PENDING);
                        return createProfile(profileRequestBody);
                    }
                });
    }


    public Mono<Profile> createProfile(CreateProfileRequestBody profileRequestBody) {
        return Mono.just(profileRequestBody)
                .map(CreateProfileRequestBody -> {
                    Profile profile = new Profile();
                    profile.setId(profileRequestBody.getId());
                    profile.setName(profileRequestBody.getName());
                    profile.setRole(profileRequestBody.getRole());
                    profile.setStatus(profileRequestBody.getStatus());
                    profile.setEmail(profileRequestBody.getEmail());
                    return profile;
                })
                .flatMap(profileRepository::save)
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .doOnSuccess(profile -> {
                });
    }
    public Mono<ProfileDTO> updateStatusProfile(ProfileDTO profileDTO){
        return getDetailProfileByEmail(profileDTO.getEmail())
                .map(ProfileDTO::dtoToEntity)
                .flatMap(profile -> {
                    profile.setStatus(profileDTO.getStatus());
                    return profileRepository.save(profile);
                })
                .map(ProfileDTO::entityToDto)
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }
    public Mono<ProfileDTO> getDetailProfileByEmail(String email){
        return profileRepository.findByEmail(email)
                .map(ProfileDTO::entityToDto)
                .switchIfEmpty(Mono.error(new CommonException("PF03", "Profile not found", HttpStatus.NOT_FOUND)));
    }
}
