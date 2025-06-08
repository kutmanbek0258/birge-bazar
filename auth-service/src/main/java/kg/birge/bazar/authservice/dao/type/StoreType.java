package kg.birge.bazar.authservice.dao.type;

import lombok.Getter;

@Getter
public enum StoreType {
    AVATAR("avatars");

    /**
     * Имя раздела (папки)
     **/
    private final String bucket;

    StoreType(String bucket) {
        this.bucket = bucket;
    }
}
