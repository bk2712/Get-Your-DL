package com.Get_Your_DL_public_portal.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class EmailTemplateLoader {
    public static String loadVerificationTemplate(String name, String link) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email_verification_template.html");
            String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            template = template.replace("{{name}}", name);
            template = template.replace("{{link}}", link);

            return template;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load email template");
        }
    }
}
