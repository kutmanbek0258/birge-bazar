package kg.birge.bazar.authservice.components;

import java.util.Map;


public interface ConfirmationStore {

    /**
     * Сохранить данные под ключом sessionId
     *
     * @param item      данные для сохранения
     * @param sessionId ключ
     */
    void save(StoreItem item, String sessionId) throws Exception;

    /**
     * Достать и удалить данные по ключу sessionId. Если ключ не будет найден, будет возвращено null
     *
     * @param sessionId ключ
     */
    StoreItem take(String sessionId) throws Exception;

    /**
     * Данные для хранения в сторе
     *
     * @param email       email адрес пользователя
     * @param confirmCode Код подтверждения
     */
    record StoreItem(String email, String confirmCode, Map<String, String> extraData) {
    }
}
