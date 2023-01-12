package com.augery.vesta;

import com.augery.vesta.configuration.GlobalConfiguration;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GlobalConfiguration.class);

        JDA jda = context.getBean(JDA.class);
    }
}
