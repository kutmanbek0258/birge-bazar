package kg.birge.bazar.userservice.service;

import cn.hutool.core.bean.BeanUtil;
import jakarta.transaction.Transactional;
import kg.birge.bazar.userservice.dto.SellerProfileDto;
import kg.birge.bazar.userservice.mapper.SellerProfileMapper;
import kg.birge.bazar.userservice.models.SellerProfile;
import kg.birge.bazar.userservice.repository.SellerProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SellerProfileService {
    private final SellerProfileRepository repository;
    private final SellerProfileMapper sellerProfileMapper;

    public SellerProfileDto save(SellerProfileDto sellerProfileDto) {
        SellerProfile entity = sellerProfileMapper.toEntity(sellerProfileDto);
        return sellerProfileMapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public SellerProfileDto findById(Long id) {
        return sellerProfileMapper.toDto(repository.findById(id).orElseThrow());
    }

    public Page<SellerProfileDto> findByCondition(SellerProfileDto sellerProfileDto, Pageable pageable) {
        Page<SellerProfile> entityPage = repository.findAll(pageable);
        List<SellerProfile> entities = entityPage.getContent();
        return new PageImpl<>(sellerProfileMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }

    public SellerProfileDto update(SellerProfileDto sellerProfileDto, Long id) {
        SellerProfileDto data = findById(id);
        SellerProfile entity = sellerProfileMapper.toEntity(sellerProfileDto);
        BeanUtil.copyProperties(entity, data, "id", "userId");
        return save(sellerProfileMapper.toDto(entity));
    }
}