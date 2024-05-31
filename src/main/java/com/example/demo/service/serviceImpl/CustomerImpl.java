package com.example.demo.service.serviceImpl;

import com.example.demo.Util.CheckEmptys;
import com.example.demo.Util.GetInfoUser;
import com.example.demo.constants.ConstantsOtp;
import com.example.demo.constants.ConstantsUser;
import com.example.demo.model.Dto.Messenger;
import com.example.demo.model.Dto.UserDTO;
import com.example.demo.model.Dto.UserPage;
import com.example.demo.model.Dto.UserRequestDto;
import com.example.demo.model.entity.DAOUser;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.jni.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CustomerImpl implements CustomerService {
    private final Messenger messenger;
    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;

    public CustomerImpl(Messenger messenger, UserRepository userRepository, PasswordEncoder bcryptEncoder) {
        this.messenger = messenger;
        this.userRepository = userRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    ResponseEntity<?> checkPassword(String password) {
        if (password.length() < 6) {
            messenger.setMessenger(ConstantsUser.USERNAME_PASSWORD_MIN_SIX_CHARACTERS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+]).{6,}$")) {
            messenger.setMessenger(ConstantsUser.PASSWORD_REQUIREMENTS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getInformation() {
        DAOUser user = getUserByToken();
        UserDTO userDTO = new UserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changePassword(String oldPassword, String newPassword, String enterPassword) {
        DAOUser user = getUserByToken();

        if (!bcryptEncoder.matches(oldPassword, user.getPassword())) {
            messenger.setMessenger("Mật khẩu không chính xác");
            return new ResponseEntity<>(messenger, HttpStatus.UNAUTHORIZED);
        }
        if (!Objects.equals(newPassword, enterPassword)) {
            messenger.setMessenger("Mật khẩu nhập lại không khớp");
            return new ResponseEntity<>(messenger, HttpStatus.UNAUTHORIZED);
        }
        ResponseEntity<?> validationResponse = checkPassword(newPassword);
        if (!ObjectUtils.isEmpty(validationResponse)) {
            return validationResponse;
        }

        user.setPassword(bcryptEncoder.encode(newPassword));
        userRepository.save(user);
        messenger.setMessenger("Đổi mật khẩu thành công");
        return new ResponseEntity<>(messenger, HttpStatus.OK);
    }

    private ResponseEntity<?> validateRegisterInfo(  DAOUser user ,UserRequestDto userRequestDto) {

        if (userRepository.existsByEmail(userRequestDto.getEmail()) && !Objects.equals(user.getEmail(), userRequestDto.getEmail())) {
            messenger.setMessenger(ConstantsUser.EMAIL_ALREADY_EXISTS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByPhoneNumber(userRequestDto.getPhoneNumber()) && !Objects.equals(user.getPhoneNumber(), userRequestDto.getPhoneNumber())) {
            messenger.setMessenger(ConstantsUser.PHONE_NUMBER_ALREADY_EXISTS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (userRequestDto.getFullName().length() < 6) {
            messenger.setMessenger(ConstantsUser.FULL_NAME_AT_LEAST_SIX_CHARACTERS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }

        String regex = "^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*@gmail.com$";
        if (!Pattern.matches(regex, userRequestDto.getEmail())) {
            messenger.setMessenger(ConstantsUser.INVALID_EMAIL);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }

        if (userRequestDto.getPhoneNumber().length() < 10) {
            messenger.setMessenger(ConstantsUser.PHONE_NUMBER_TEN_DIGITS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> changeCustomerInformation(UserRequestDto userRequestDto) {
        DAOUser user = getUserByToken();

        ResponseEntity<?> validationResponse = validateRegisterInfo(user,userRequestDto);
        if (!ObjectUtils.isEmpty(validationResponse)) {
            return validationResponse;
        }

        user.setEmail(userRequestDto.getEmail());
        user.setFullName(userRequestDto.getFullName());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setAddress(userRequestDto.getAddress());
        userRepository.save(user);

        messenger.setMessenger("Sửa thông tin thành công");
        return new ResponseEntity<>(messenger, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllUser(int page, int size, Long id) {

        Pageable pageable = PageRequest.of(page, size);
        UserPage userPage = new UserPage( userRepository.findByRole_Id(id,pageable));
        return  new ResponseEntity<>(userPage, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> lockAccount(Long id) {

        if(!userRepository.existsById(id)){
            messenger.setMessenger("Tài khoản không tồn tại ");
            return  new ResponseEntity<>(messenger, HttpStatus.OK);
        }
        DAOUser user= userRepository.findById(id).orElse(null);

        String  notLock="admin";
        if(user.getUsername().equals(notLock)){
            messenger.setMessenger("Không thể khóa tài khoản "+notLock);
            return  new ResponseEntity<>(messenger, HttpStatus.OK);
        }

        if(user.getStatus()==1){
            user.setStatus(0);
            messenger.setMessenger("Đã mở khóa tài khoản : "+user.getUsername());
        }
        else {
            user.setStatus(1);
            messenger.setMessenger("Đã khóa tài khoản : "+user.getUsername());
        }

        userRepository.save(user);

        return  new ResponseEntity<>(messenger, HttpStatus.OK);

    }

    private DAOUser getUserByToken() {
        return userRepository.findByUsername(GetInfoUser.getUsername());
    }

}
