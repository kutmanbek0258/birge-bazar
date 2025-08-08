package kg.birge.bazar.sellerservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.birge.bazar.sellerservice.dto.*;
import kg.birge.bazar.sellerservice.mapper.SellerMapper;
import kg.birge.bazar.sellerservice.mapper.SellerStaffMapper;
import kg.birge.bazar.sellerservice.models.Seller;
import kg.birge.bazar.sellerservice.models.SellerStaff;
import kg.birge.bazar.sellerservice.repository.SellerRepository;
import kg.birge.bazar.sellerservice.repository.SellerStaffRepository;
import kg.birge.bazar.sellerservice.services.SellerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerDashboardServiceImpl implements SellerDashboardService {

    private final SellerRepository sellerRepository;
    private final SellerStaffRepository sellerStaffRepository;
    private final SellerMapper sellerMapper;
    private final SellerStaffMapper sellerStaffMapper;

    @Override
    @Transactional
    public SellerDto createSeller(UUID userId, CreateSellerDto createSellerDto) {
        if (sellerRepository.existsByUserId(userId)) {
            throw new IllegalStateException("A seller profile for this user already exists.");
        }
        Seller seller = sellerMapper.toEntity(createSellerDto);
        seller.setUserId(userId);
        Seller savedSeller = sellerRepository.save(seller);
        return sellerMapper.toDto(savedSeller);
    }

    @Override
    @Transactional(readOnly = true)
    public SellerDto getMySellerInfo(UUID userId) {
        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Seller profile not found for the current user."));
        return sellerMapper.toDto(seller);
    }

    @Override
    @Transactional
    public SellerDto updateMySellerInfo(UUID userId, UpdateSellerDto updateSellerDto) {
        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Seller profile not found for the current user."));
        sellerMapper.updateFromDto(updateSellerDto, seller);
        Seller updatedSeller = sellerRepository.save(seller);
        return sellerMapper.toDto(updatedSeller);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerStaffDto> getMyStaff(UUID userId) {
        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Seller profile not found for the current user."));
        return seller.getStaff().stream()
                .map(sellerStaffMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SellerStaffDto inviteStaff(UUID ownerId, InviteStaffDto inviteStaffDto) {
        Seller seller = sellerRepository.findByUserId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller profile not found for the current user."));
        // In a real app, you would check if the invited user exists via user-service
        // and if they are already part of another seller's staff.
        SellerStaff staff = sellerStaffMapper.toEntity(inviteStaffDto);
        staff.setSeller(seller);
        SellerStaff savedStaff = sellerStaffRepository.save(staff);
        return sellerStaffMapper.toDto(savedStaff);
    }

    @Override
    @Transactional
    public SellerStaffDto updateStaff(UUID ownerId, UUID staffUserId, UpdateStaffDto updateStaffDto) {
        Seller seller = sellerRepository.findByUserId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller profile not found for the current user."));
        SellerStaff staff = sellerStaffRepository.findBySellerAndUserId(seller, staffUserId)
                .orElseThrow(() -> new EntityNotFoundException("Staff member not found."));
        sellerStaffMapper.updateFromDto(updateStaffDto, staff);
        SellerStaff updatedStaff = sellerStaffRepository.save(staff);
        return sellerStaffMapper.toDto(updatedStaff);
    }

    @Override
    @Transactional
    public void removeStaff(UUID ownerId, UUID staffUserId) {
        Seller seller = sellerRepository.findByUserId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller profile not found for the current user."));
        SellerStaff staff = sellerStaffRepository.findBySellerAndUserId(seller, staffUserId)
                .orElseThrow(() -> new EntityNotFoundException("Staff member not found."));
        sellerStaffRepository.delete(staff);
    }
}
