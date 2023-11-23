package tinqle.tinqleServer.config;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    @Bean
    public JSONParser jsonParser() {
        return new JSONParser();
    }
}
