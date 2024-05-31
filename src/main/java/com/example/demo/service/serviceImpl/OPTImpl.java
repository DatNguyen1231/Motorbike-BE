package com.example.demo.service.serviceImpl;

import com.example.demo.Util.GmailService;
import com.example.demo.Util.PasswordGenerator;
import com.example.demo.constants.ConstantsOtp;
import com.example.demo.model.Dto.Messenger;
import com.example.demo.model.entity.DAOUser;
import com.example.demo.model.entity.OTP;
import com.example.demo.repositories.OTPRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Slf4j
@Service
public class OPTImpl implements OTPService {

    private final PasswordEncoder bcryptEncoder;
    private final OTPRepository otpRepository;
    public final GmailService gmailService;
    private final UserRepository userRepository;
    private final Messenger messenger;

    public OPTImpl(PasswordEncoder bcryptEncoder, OTPRepository otpRepository, GmailService gmailService, UserRepository userRepository, Messenger messenger) {
        this.bcryptEncoder = bcryptEncoder;
        this.otpRepository = otpRepository;
        this.gmailService = gmailService;
        this.userRepository = userRepository;
        this.messenger = messenger;
    }

    // tạo OTP và lưu vào csdl
    public String generateAndSaveOTPForUser(Long userId) {
        String otp = generateOTP();
        Instant expirationTime = Instant.now().plus(Duration.ofMinutes(1));
        OTP newOTP = new OTP(userId, otp, expirationTime);
        otpRepository.save(newOTP);
        return otp;
    }

    // tạo otp
    private String generateOTP() {
        final String OTP_CHARACTERS = "0123456789";
        final int OTP_LENGTH = 6;
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(OTP_CHARACTERS.charAt(random.nextInt(OTP_CHARACTERS.length())));
        }
        return otp.toString();
    }

    @Override
    public ResponseEntity<?> sendOTP(String email) {
        try {
            DAOUser user = userRepository.findByEmail(email.trim()).orElse(null);

            if (Objects.isNull(user) ) {
                messenger.setMessenger(ConstantsOtp.INVALID_EMAIL);
                return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
            }
            //tạo và lưu OTP vào csdl
            String otp = generateAndSaveOTPForUser(user.getId());
            gmailService.constructEmailWithHTML("[Motorbike Ecommerce] - Đặt lại mật khẩu", generateOTPContent(user.getFullName(), otp), user.getEmail());

            messenger.setMessenger(ConstantsOtp.SEND_OTP_UCCESSFULLY);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ConstantsOtp.ERROR_SEND_OTP);
        }

    }

    private String generateOTPContent(String fullName, String otp) {
        StringBuilder emailContentBuilder = new StringBuilder();
        emailContentBuilder.append("<html><body style='font-family: Arial, sans-serif;'>");
        emailContentBuilder.append("<h2 style='color: #333333;'>Kính gửi quý khách ").append(fullName).append(",</h2>");
        emailContentBuilder.append("<p style='color: #333333;'>Chúng tôi nhận được yêu cầu đặt lại mật khẩu từ phía quý khách. Dưới đây là mã OTP của bạn:</p>");
        emailContentBuilder.append("<p style='color: #333333;'><strong>Mã OTP của bạn là:</strong> ").append(otp).append("</p>");
        emailContentBuilder.append("<p style='color: #333333;'>Đừng ngần ngại liên hệ với chúng tôi nếu quý khách có bất kỳ thắc mắc nào.</p>");
        emailContentBuilder.append("<p style='color: #333333;'>Trân trọng,<br>");
        emailContentBuilder.append("Motorbike Ecommerce</p>");
        emailContentBuilder.append("<p style='color: #333333;'>Địa chỉ: Đại Lộ Khoa Học, TP. Quy Nhơn<br>");
        emailContentBuilder.append("Email: motobikes@gmail.com<br>");
        emailContentBuilder.append("Số điện thoại: 0123456789<br>");
        emailContentBuilder.append("Giờ làm việc: 08:00 AM - 17:30 PM</p>");
        emailContentBuilder.append("</body></html>");
        return emailContentBuilder.toString();
    }

    /// check otp
    public boolean isOTPValid(Long userId, String otpRequest) {
        OTP otp = otpRepository.findById(userId).orElse(null);
        // Không tìm thấy mã OTP cho người dùng hoacv
        // Trả về true nếu mã OTP còn hạn, ngược lại trả về false
        return !Objects.isNull(otp) && Objects.equals(otp.getOtp(), otpRequest);
    }

    public boolean isExpiredOTP(Long userId) {
        OTP otp = otpRepository.findById(userId).orElse(null);
        return !Objects.isNull(otp) && otp.getExpirationTime().isAfter(Instant.now());
    }

    @Override
    public ResponseEntity<?> resetPassword(String email, String otp) {
        try {
            DAOUser user = userRepository.findByEmail(email.trim()).orElse(null);
            if (ObjectUtils.isEmpty(user)) {
                messenger.setMessenger(ConstantsOtp.INVALID_EMAIL);
                return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
            }

            if (!isOTPValid(user.getId(), otp)) {
                messenger.setMessenger(ConstantsOtp.WRONG_OTP);
                return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
            }
            if (!isExpiredOTP(user.getId())) {
                messenger.setMessenger(ConstantsOtp.EXPIRED_OTP);
                return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
            }
            // Tạo mật khẩu ngẫu nhiên
            final String passwordReset = PasswordGenerator.generatePassword(10);
            // Mã hóa và lưu mật khẩu mới
            user.setPassword(bcryptEncoder.encode(passwordReset));
            userRepository.save(user);

            //Gửi email thông báo về mật khẩu mới
            String emailSubject = "[Motorbike Ecommerce] - Đặt lại mật khẩu";
            String emailContent = generateResetPasswordEmailContent(user.getFullName(), passwordReset);
            gmailService.constructEmailWithHTML(emailSubject, emailContent, user.getEmail());

            // xóa otp sau khi đặt lại mật khẩu thành công
            otpRepository.deleteById(user.getId());
            messenger.setMessenger(ConstantsOtp.SEND_PASSWORD_SUCCESSFULLY);
            return new ResponseEntity<>(messenger, HttpStatus.OK);


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ConstantsOtp.EXPIRED_OTP);
        }
    }

    private String generateResetPasswordEmailContent(String fullName, String passwordReset) {
        StringBuilder emailContentBuilder = new StringBuilder();
        emailContentBuilder.append("<html><body style='font-family: Arial, sans-serif;'>")
                .append("<h2 style='color: #333333;'>Kính gửi quý khách ").append(fullName).append(",</h2>")
                .append("<p style='color: #333333;'>Chúng tôi nhận được yêu cầu đặt lại mật khẩu từ phía quý khách. Dưới đây là mật khẩu mới của bạn:</p>")
                .append("<p style='color: #333333;'><strong>Mật khẩu mới:</strong> ").append(passwordReset).append("</p>")
                .append("<p style='color: #333333;'>Đừng ngần ngại liên hệ với chúng tôi nếu quý khách có bất kỳ thắc mắc nào.</p>")
                .append("<p style='color: #333333;'>Trân trọng,<br>")
                .append("Motorbike Ecommerce</p>")
                .append("<p style='color: #333333;'>Địa chỉ: Đại Lộ Khoa Học, TP. Quy Nhơn<br>")
                .append("Email: motobikes@gmail.com<br>")
                .append("Số điện thoại: 0123456789<br>")
                .append("Giờ làm việc: 08:00 AM - 17:30 PM</p>")
                .append("</body></html>");
        return emailContentBuilder.toString();
    }

}
