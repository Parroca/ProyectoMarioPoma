package cat.institutmarianao.proyecto.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}