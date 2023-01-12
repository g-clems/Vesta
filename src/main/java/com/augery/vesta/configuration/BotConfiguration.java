package com.augery.vesta.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;

@Configuration
@Slf4j
public class BotConfiguration {

    @Bean
    ConfigModel config() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path configuration = Path.of("config", "config.json");

        if (!Files.exists(configuration)) {
            Files.createDirectories(configuration.getParent());
            Files.createFile(configuration);

            ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
            ConfigModel model = new ConfigModel(
                    "TOKEN HERE", "postgresql://localhost", 5432, "postgres", "postgres", "vesta");

            BufferedWriter fileWriter = Files.newBufferedWriter(configuration);
            objectWriter.writeValue(fileWriter, model);

            fileWriter.close();

            log.error("Please fill the config file and restart the bot");
            System.exit(1);
            return null;
        }

        ObjectReader objectReader = mapper.readerFor(ConfigModel.class);
        BufferedReader fileReader = Files.newBufferedReader(configuration);

        ConfigModel model = objectReader.readValue(fileReader);
        fileReader.close();

        model = loadFromEnvironment(model);

        return model;
    }

    @Lazy
    @Bean
    JDA bot(ConfigModel model) {
        return JDABuilder.create(model.token(), GUILD_VOICE_STATES).build();
    }

    private ConfigModel loadFromEnvironment(ConfigModel config) {
        if (System.getenv("TOKEN") != null) config = config.withToken(System.getenv("TOKEN"));
        if (System.getenv("DBHOST") != null) config = config.withDbHost(System.getenv("DBHOST"));
        if (System.getenv("DBPORT") != null) config = config.withDbPort(Integer.parseInt(System.getenv("DBPORT")));
        if (System.getenv("DBUSER") != null) config = config.withDbUser(System.getenv("DBUSER"));
        if (System.getenv("DBPASSWORD") != null) config = config.withDbPassword(System.getenv("DBPASSWORD"));
        if (System.getenv("DBNAME") != null) config = config.withDbName(System.getenv("DBNAME"));

        return config;
    }

    public record ConfigModel(
            String token, String dbHost, int dbPort, String dbUser, String dbPassword, String dbName) {
        public ConfigModel withToken(String token) {
            return new ConfigModel(token, dbHost, dbPort, dbUser, dbPassword, dbName);
        }
        public ConfigModel withDbHost(String dbHost) {
            return new ConfigModel(token, dbHost, dbPort, dbUser, dbPassword, dbName);
        }
        public ConfigModel withDbPort(int dbPort) {
            return new ConfigModel(token, dbHost, dbPort, dbUser, dbPassword, dbName);
        }
        public ConfigModel withDbUser(String dbUser) {
            return new ConfigModel(token, dbHost, dbPort, dbUser, dbPassword, dbName);
        }
        public ConfigModel withDbPassword(String dbPassword) {
            return new ConfigModel(token, dbHost, dbPort, dbUser, dbPassword, dbName);
        }
        public ConfigModel withDbName(String dbName) {
            return new ConfigModel(token, dbHost, dbPort, dbUser, dbPassword, dbName);
        }
    }
}


