package kg.birge.bazar.authservice.components;


import kg.birge.bazar.authservice.dto.RegistrationDto;

public interface RegistrationStore {

    /**
     * Сохранить данные
     *
     * @param dto       данные которые сохраняем
     * @param sessionId идентификатор сессии пользователя
     */
    void save(RegistrationDto dto, String sessionId) throws Exception;

    /**
     * Взять и удалить из хранилища данные по sessionId
     *
     * @param sessionId идентификатор сессии пользователя
     *
     * @return сохранённые данные или null
     */
    RegistrationDto take(String sessionId) throws Exception;
}
