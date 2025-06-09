package kg.birge.bazar.userservice.controller;

import kg.birge.bazar.userservice.dto.SellerProfileDto;
import kg.birge.bazar.userservice.service.SellerProfileService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/seller-profile")
@RestController
public class SellerProfileController {
    private final SellerProfileService sellerProfileService;

    public SellerProfileController(SellerProfileService sellerProfileService) {
        this.sellerProfileService = sellerProfileService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Validated SellerProfileDto sellerProfileDto) {
        sellerProfileService.save(sellerProfileDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerProfileDto> findById(@PathVariable("id") Long id) {
        SellerProfileDto sellerProfile = sellerProfileService.findById(id);
        return ResponseEntity.ok(sellerProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional.ofNullable(sellerProfileService.findById(id)).orElseThrow(() -> {
//            log.error("Unable to delete non-existent data！");
            return new ResourceNotFoundException("Unable to delete non-existent data！");
        });
        sellerProfileService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page-query")
    public ResponseEntity<Page<SellerProfileDto>> pageQuery(SellerProfileDto sellerProfileDto, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SellerProfileDto> sellerProfilePage = sellerProfileService.findByCondition(sellerProfileDto, pageable);
        return ResponseEntity.ok(sellerProfilePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Validated SellerProfileDto sellerProfileDto, @PathVariable("id") Long id) {
        sellerProfileService.update(sellerProfileDto, id);
        return ResponseEntity.ok().build();
    }
}