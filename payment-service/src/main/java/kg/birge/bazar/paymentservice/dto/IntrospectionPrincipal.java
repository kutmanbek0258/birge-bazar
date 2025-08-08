package kg.birge.bazar.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class IntrospectionPrincipal {
    private UUID id;
    private String username;
    private boolean enabled;
    private List<String> roles;
    private List<String> permissions;
}
