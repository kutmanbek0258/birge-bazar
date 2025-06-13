package kg.birge.bazar.userservice.service;

import cn.hutool.core.bean.BeanUtil;
import jakarta.transaction.Transactional;
import kg.birge.bazar.userservice.dto.EntityHistoryDto;
import kg.birge.bazar.userservice.mapper.EntityHistoryMapper;
import kg.birge.bazar.userservice.models.EntityHistory;
import kg.birge.bazar.userservice.repository.EntityHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class EntityHistoryService {
    private final EntityHistoryRepository repository;
    private final EntityHistoryMapper historyMapper;

    public EntityHistoryDto save(EntityHistoryDto historyDto) {
        EntityHistory entity = historyMapper.toEntity(historyDto);
        return historyMapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public EntityHistoryDto findById(Long id) {
        return historyMapper.toDto(repository.findById(id).orElseThrow(() -> new RuntimeException("EntityHistory not found with id: " + id)));
    }

    public Page<EntityHistoryDto> findByCondition(EntityHistoryDto historyDto, Pageable pageable) {
        Page<EntityHistory> entityPage = repository.findAll(pageable);
        List<EntityHistory> entities = entityPage.getContent();
        return new PageImpl<>(historyMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }

    public EntityHistoryDto update(EntityHistoryDto historyDto, Long id) {
        EntityHistoryDto data = findById(id);
        EntityHistory entity = historyMapper.toEntity(historyDto);
        BeanUtil.copyProperties(entity, data, "id");
        return save(historyMapper.toDto(entity));
    }
}