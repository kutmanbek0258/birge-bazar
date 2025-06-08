package kg.birge.bazar.authservice.service;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ResourceServerService {

    ResponseEntity<byte[]> getUserAvatar(UUID userId);
}
