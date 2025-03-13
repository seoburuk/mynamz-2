package com.example.mynamz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    public boolean sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            
            mailSender.send(message);
            logger.info("이메일 발송 완료: {}", to);
            return true;
        } catch (MessagingException e) {
            logger.error("이메일 발송 실패", e);
            return false;
        }
    }

    public boolean sendTemporaryPassword(String to, String tempPassword) {
        String subject = "[MyNamez] 임시 비밀번호 발급 안내";
        String content = createPasswordResetEmailContent(tempPassword);
        return sendEmail(to, subject, content);
    }

    private String createPasswordResetEmailContent(String tempPassword) {
        return String.format("""
            <div style="font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', '맑은 고딕', sans-serif;">
                <h2>임시 비밀번호 발급 안내</h2>
                <p>안녕하세요. MyNamez입니다.</p>
                <p>요청하신 임시 비밀번호가 발급되었습니다.</p>
                <div style="background-color: #f4f4f4; padding: 20px; margin: 20px 0; border-radius: 5px;">
                    <p style="margin: 0;"><strong>임시 비밀번호: %s</strong></p>
                </div>
                <p>보안을 위해 로그인 후 반드시 비밀번호를 변경해 주시기 바랍니다.</p>
                <div style="margin-top: 30px; padding-top: 15px; border-top: 1px solid #eee;">
                    <p style="color: #666; font-size: 14px; margin: 0;">
                        본 메일은 발신전용이므로 회신이 되지 않습니다.<br>
                        문의사항이 있으시면 고객센터를 이용해 주시기 바랍니다.
                    </p>
                </div>
            </div>
            """, tempPassword);
    }
}