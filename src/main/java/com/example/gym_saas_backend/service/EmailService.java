package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.EmailRequest;
import jakarta.annotation.PreDestroy;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final BlockingQueue<EmailRequest> emailQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        startEmailWorker();
    }

    private void startEmailWorker() {
        executorService.submit(() -> {
            while (true) {
                try {
                    EmailRequest request = emailQueue.take(); // blocking
                    processEmail(request);
                } catch (InterruptedException e) {
                    // Handle thread interruption (e.g., during shutdown)
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                    System.out.println("Email worker thread interrupted. Exiting.");
                    break;
                } catch (Exception e) {
                    System.err.println("Failed to send email: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void processEmail(EmailRequest request) {
        if (request.isHtml()) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(request.getTo());
                helper.setSubject(request.getSubject());
                helper.setText(request.getBody(), true); // true for HTML

                mailSender.send(message);
            } catch (Exception e) {
                System.err.println("HTML Email failed: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getBody());
            mailSender.send(message);
        }
    }

    public void sendEmail(String to, String subject, String body) {
        EmailRequest request = new EmailRequest(to, subject, body, false);
        emailQueue.offer(request); // enqueue plain email
    }

    public void sendHTMLEmail(String to, String subject, String htmlBody) {
        EmailRequest request = new EmailRequest(to, subject, htmlBody, true);
        emailQueue.offer(request); // enqueue HTML email
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdownNow();
    }
}


