package kg.birge.bazar.authservice.service;

import jakarta.servlet.http.HttpServletRequest;
import kg.birge.bazar.authservice.dao.type.UserEventType;
import kg.birge.bazar.authservice.dto.PageableResponseDto;
import kg.birge.bazar.authservice.dto.UserEventDto;

public interface UserEventService {

    PageableResponseDto<UserEventDto> searchEvents(int page, int pageSize);

    void createEvent(UserEventType eventType, String clientId, HttpServletRequest request);

    /**
     * Удалить события пользователя, являющиеся устаревшими.
     */
    void deleteOldEvents();
}
