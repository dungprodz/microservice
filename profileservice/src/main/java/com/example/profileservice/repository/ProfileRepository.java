package com.example.profileservice.repository;

import com.example.profileservice.data.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProfileRepository extends ReactiveCrudRepository<Profile,Long> {
    Mono<Profile> findByEmail(String email);
}
