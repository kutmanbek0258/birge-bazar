package kg.birge.bazar.authservice.dto.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.io.IOException;

public class AuthMethodJsonDeserializer extends JsonDeserializer<ClientAuthenticationMethod> {

    @Override
    public ClientAuthenticationMethod deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        String strValue = jsonParser.getText();
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }
        return new ClientAuthenticationMethod(strValue);
    }
}
