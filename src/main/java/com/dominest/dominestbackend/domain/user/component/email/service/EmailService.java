package com.dominest.dominestbackend.domain.user.component.email.service;

import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender; // Bean 등록해둔 MailConfig 를 emailsender 라는 이름으로 autowired

    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입 메일 양식 작성
    private MimeMessage createJoinMessage(String email) {
        String authNum = emailVerificationService.generateCode(email);
        String setFrom = "gjwldud0719@naver.com";
        String title = "회원가입 인증 번호";
        String n = "";
        n += "<div style='display: flex; width: 100%; min-height: 100%; text-align: center; justify-content: center; background-color: #f0f0f0;'>";
        n += "<div style='display: flex; width: 80%; padding-top: 5%; background-color: white; text-align: left; justify-content: center;'>";
        n += "<div style='width: 90%; background-color: white; text-align: left;'>";
        n += "<h1 style='color: black; text-align: center;'>Dominest 회원가입 안내</h1>";
        n += "<br />";
        n += "<p style='text-align: center;'><strong style='color: #444444;'>\"회원가입\"</strong>을 위해 이메일 인증을 진행합니다.</p>";
        n += "<p style='text-align: center;'>아래에 발급된 이메일 인증번호를 복사하거나 직접 입력하여 인증을 완료해 주세요.</p>";
        n += "<p style='text-align: center;'><strong style='color: red;'>만약 본인이 회원가입한 게 아니라면 삭제해주세요.</strong></p>";
        n += "<br />";
        n += "<hr />";
        n += "<p style='text-align: center;'>인증번호: <strong style='letter-spacing: 5px; color: blue;'>" + authNum + "</strong></p>";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<hr />";
        n += "<div style='position: fixed; bottom: 0; width: 71%; background-color: #f0f0f0;'>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>본 메일은 발신 전용입니다.</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>궁금하신 사항은 기숙사 행정실에 문의 바랍니다.</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>성공회대 IT 도미도미 팀 제작</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>사업자 번호 : 405-06-30088</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>전화: 010-8408-9384 이메일: zzxx373014@gmail.com</p>";
        n += "<hr />";
        n += "</div>";
        n += "</div>";
        n += "</div>";
        n += "</div>";

        MimeMessage message = emailSender.createMimeMessage();
        try {
            message.addRecipients(MimeMessage.RecipientType.TO, email); // 보내는 대상
            message.setSubject(title);
            message.setFrom(setFrom);
            message.setText(n,"utf-8", "html");
        } catch (MessagingException e) {
            throw new AppServiceException(ErrorCode.EMAIL_CANNOT_BE_CREATED, e);
        }
        return message;
    }


    // 메일 양식 작성
    private MimeMessage createMessage(String toEmail) throws MessagingException{
        String authNum = emailVerificationService.generateCode(toEmail);
        String setFrom = "gjwldud0719@naver.com";
        String title = "회원가입 인증 번호";

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); // 보내는 대상
        message.setSubject(title);
        message.setFrom(setFrom);
        String n = "";
        n += "<div style='display: flex; width: 100%; min-height: 100%; text-align: center; justify-content: center; background-color: #f0f0f0;'>";
        n += "<div style='display: flex; width: 80%; padding-top: 5%; background-color: white; text-align: left; justify-content: center;'>";
        n += "<div style='width: 90%; background-color: white; text-align: left;'>";
        n += "<h1 style='color: black; text-align: center;'>Dominest 회원가입 안내</h1>";
        n += "<br />";
        n += "<p style='text-align: center;'><strong style='color: #444444;'>\"회원가입\"</strong>을 위해 이메일 인증을 진행합니다.</p>";
        n += "<p style='text-align: center;'>아래에 발급된 이메일 인증번호를 복사하거나 직접 입력하여 인증을 완료해 주세요.</p>";
        n += "<p style='text-align: center;'><strong style='color: red;'>만약 본인이 회원가입한 게 아니라면 삭제해주세요.</strong></p>";
        n += "<br />";
        n += "<hr />";
        n += "<p style='text-align: center;'>인증번호: <strong style='letter-spacing: 5px; color: blue;'>" + authNum + "</strong></p>";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<hr />";
        n += "<div style='position: fixed; bottom: 0; width: 71%; background-color: #f0f0f0;'>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>본 메일은 발신 전용입니다.</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>궁금하신 사항은 기숙사 행정실에 문의 바랍니다.</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>성공회대 IT 도미도미 팀 제작</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>사업자 번호 : 405-06-30088</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>전화: 010-8408-9384 이메일: zzxx373014@gmail.com</p>";
        n += "<hr />";
        n += "</div>";
        n += "</div>";
        n += "</div>";
        n += "</div>";

        message.setText(n,"utf-8", "html");
        return message;
    }

    private MimeMessage createChangeMessage(String email)  { // 임시 비밀번호 발송 메일 만들기
        String authNum = emailVerificationService.generateCode(email);

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            User foundUser = user.get();
            foundUser.changePassword(passwordEncoder.encode(authNum));
            userRepository.save(foundUser);
        }

        String setFrom = "gjwldud0719@naver.com";
        String title = "임시 비밀번호";
        String n = "";
        n += "<div style='display: flex; width: 100%; min-height: 100%; text-align: center; justify-content: center; background-color: #f0f0f0;'>";
        n += "<div style='display: flex; width: 80%; padding-top: 5%; background-color: white; text-align: left; justify-content: center;'>";
        n += "<div style='width: 90%; background-color: white; text-align: left;'>";
        n += "<h1 style='color: black; text-align: center;'>Dominest 임시 비밀번호 안내</h1>";
        n += "<br />";
        n += "<p style='text-align: center;'><strong style='color: #444444;'>\"임시 비밀번호\"</strong></p>";
        n += "<p style='text-align: center;'>아래에 발급된 이메일 인증번호를 복사하거나 직접 입력하여 인증을 완료해 주세요.</p>";
        n += "<br />";
        n += "<hr />";
        n += "<p style='text-align: center;'>인증번호: <strong style='letter-spacing: 5px; color: blue;'>" + authNum + "</strong></p>";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<hr />";
        n += "<div style='position: fixed; bottom: 0; width: 71%; background-color: #f0f0f0;'>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>본 메일은 발신 전용입니다.</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>궁금하신 사항은 기숙사 행정실에 문의 바랍니다.</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>성공회대 IT 도미도미 팀 제작</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>사업자 번호 : 405-06-30088</p>";
        n += "<p style='line-height: 13px; text-align: center; font-size: 12px;'>전화: 010-8408-9384 이메일: zzxx373014@gmail.com</p>";
        n += "<hr />";
        n += "</div>";
        n += "</div>";
        n += "</div>";
        n += "</div>";

        MimeMessage message = emailSender.createMimeMessage();
        try {
            message.addRecipients(MimeMessage.RecipientType.TO, email); // 보내는 대상
            message.setSubject(title);
            message.setFrom(setFrom);
            message.setText(n, "utf-8", "html");
        } catch (MessagingException e) {
            throw new AppServiceException(ErrorCode.EMAIL_CANNOT_BE_CREATED, e);
        }

        return message;
    }


    public void sendJoinMessage(String email) { // 회원가입 메일 발송
        MimeMessage message = createJoinMessage(email);
        try{
            emailSender.send((message));
        } catch(MailException e){
            throw new AppServiceException(ErrorCode.EMAIL_CANNOT_BE_SENT, e);
        }
    }

    public void sendChangeMessage(String email) { // 임시 비밀번호
        MimeMessage message = createChangeMessage(email);
        try{
            emailSender.send((message));
        } catch(MailException e){
            throw new AppServiceException(ErrorCode.EMAIL_CANNOT_BE_SENT, e);
        }
    }
}