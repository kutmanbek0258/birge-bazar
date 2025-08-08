package kg.birge.bazar.userservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String avatarUrl;
}
