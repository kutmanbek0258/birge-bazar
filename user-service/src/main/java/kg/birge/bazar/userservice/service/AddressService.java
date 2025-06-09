package kg.birge.bazar.userservice.service;

import cn.hutool.core.bean.BeanUtil;
import jakarta.transaction.Transactional;
import kg.birge.bazar.userservice.dto.AddressDto;
import kg.birge.bazar.userservice.mapper.AddressMapper;
import kg.birge.bazar.userservice.models.Address;
import kg.birge.bazar.userservice.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class AddressService {
    private final AddressRepository repository;
    private final AddressMapper addressMapper;

    public AddressDto save(AddressDto addressDto) {
        Address entity = addressMapper.toEntity(addressDto);
        return addressMapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public AddressDto findById(Long id) {
        return addressMapper.toDto(repository.findById(id).orElseThrow());
    }

    public Page<AddressDto> findByCondition(AddressDto addressDto, Pageable pageable) {
        Page<Address> entityPage = repository.findAll(pageable);
        List<Address> entities = entityPage.getContent();
        return new PageImpl<>(addressMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }

    public AddressDto update(AddressDto addressDto, Long id) {
        AddressDto data = findById(id);
        Address entity = addressMapper.toEntity(addressDto);
        BeanUtil.copyProperties(entity, data, "id", "userId");
        return save(addressMapper.toDto(entity));
    }
}