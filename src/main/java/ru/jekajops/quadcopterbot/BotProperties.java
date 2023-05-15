package ru.jekajops.quadcopterbot;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Data
@Component
@ConfigurationProperties("bot")
public class BotProperties {
    private String token;

    //private List<BotConfig> botConfigs;
    private int apikey; // your api key
    private String apihash; // your api hash
    private String phonenumber; // Your phone number

    @Builder
    @Data
    public static class BotConfig {
        private final Long id;
        private final String phoneNumber;
        private final String botToken;
        private final boolean isBot;
    }
}
