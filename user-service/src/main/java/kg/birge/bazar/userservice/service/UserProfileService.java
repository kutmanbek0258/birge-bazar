package kg.birge.bazar.userservice.service;

import cn.hutool.core.bean.BeanUtil;
import jakarta.transaction.Transactional;
import kg.birge.bazar.userservice.dto.UserProfileDto;
import kg.birge.bazar.userservice.mapper.UserProfileMapper;
import kg.birge.bazar.userservice.models.UserProfile;
import kg.birge.bazar.userservice.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileDto save(UserProfileDto userProfileDto) {
        UserProfile entity = userProfileMapper.toEntity(userProfileDto);
        return userProfileMapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public UserProfileDto findById(Long id) {
        return userProfileMapper.toDto(repository.findById(id).orElseThrow());
    }

    public Page<UserProfileDto> findByCondition(UserProfileDto userProfileDto, Pageable pageable) {
        Page<UserProfile> entityPage = repository.findAll(pageable);
        List<UserProfile> entities = entityPage.getContent();
        return new PageImpl<>(userProfileMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }

//    @EntityHistoryAudit(entityType = "USER_PROFILE")
    public UserProfileDto update(Long id, UserProfileDto userProfileDto) {
        UserProfileDto data = findById(id);
        UserProfile entity = userProfileMapper.toEntity(userProfileDto);
        BeanUtil.copyProperties(entity, data, "id", "userId");
        return save(data);
    }
}