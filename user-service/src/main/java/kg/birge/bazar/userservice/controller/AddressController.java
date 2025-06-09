package kg.birge.bazar.userservice.controller;

import kg.birge.bazar.userservice.dto.AddressDto;
import kg.birge.bazar.userservice.service.AddressService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/address")
@RestController
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Validated AddressDto addressDto) {
        addressService.save(addressDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> findById(@PathVariable("id") Long id) {
        AddressDto address = addressService.findById(id);
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional.ofNullable(addressService.findById(id)).orElseThrow(() -> {
//            log.error("Unable to delete non-existent data！");
            return new ResourceNotFoundException("Unable to delete non-existent data！");
        });
        addressService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page-query")
    public ResponseEntity<Page<AddressDto>> pageQuery(AddressDto addressDto, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AddressDto> addressPage = addressService.findByCondition(addressDto, pageable);
        return ResponseEntity.ok(addressPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Validated AddressDto addressDto, @PathVariable("id") Long id) {
        addressService.update(addressDto, id);
        return ResponseEntity.ok().build();
    }
}