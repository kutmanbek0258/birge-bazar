package kg.birge.bazar.userservice.controller;

import kg.birge.bazar.userservice.dto.UserProfileDto;
import kg.birge.bazar.userservice.service.UserProfileService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/user-profile")
@RestController
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Validated UserProfileDto userProfileDto) {
        userProfileService.save(userProfileDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> findById(@PathVariable("id") Long id) {
        UserProfileDto userProfile = userProfileService.findById(id);
        return ResponseEntity.ok(userProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional.ofNullable(userProfileService.findById(id)).orElseThrow(() -> {
//            log.error("Unable to delete non-existent data！");
            return new ResourceNotFoundException("Unable to delete non-existent data！");
        });
        userProfileService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page-query")
    public ResponseEntity<Page<UserProfileDto>> pageQuery(UserProfileDto userProfileDto, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserProfileDto> userProfilePage = userProfileService.findByCondition(userProfileDto, pageable);
        return ResponseEntity.ok(userProfilePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Validated UserProfileDto userProfileDto, @PathVariable("id") Long id) {
        userProfileService.update(id, userProfileDto);
        return ResponseEntity.ok().build();
    }
}