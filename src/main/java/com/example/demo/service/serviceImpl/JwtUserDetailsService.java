package com.example.demo.service.serviceImpl;

import com.example.demo.Util.CheckEmptys;
import com.example.demo.config.JwtTokenUtil;
import com.example.demo.constants.ConstantsUser;
import com.example.demo.model.Dto.*;
import com.example.demo.model.entity.DAOUser;
import com.example.demo.model.entity.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final Messenger messenger;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private PasswordEncoder bcryptEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;

    @Autowired
    public JwtUserDetailsService(Messenger messenger, UserRepository userRepository, RoleRepository roleRepository) {
        this.messenger = messenger;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setBcryptEncoder(PasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setUserDetailsService(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DAOUser user = userRepository.findByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Create a list to store user's authorities (roles)
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add roles to authoritiesA
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRole()));
        // You can add more roles as needed

        // Create UserDetails object with roles
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    public UserDTO getUserDto(String username) {
        return new UserDTO(userRepository.findByUsername(username));
    }

    private boolean isInfo(UserRequestDto userRequestDto) {
        return CheckEmptys.checkEmpty(userRequestDto.getUsername())
                || CheckEmptys.checkEmpty(userRequestDto.getPassword())
                || CheckEmptys.checkEmpty(userRequestDto.getAddress())
                || CheckEmptys.checkEmpty(userRequestDto.getPhoneNumber())
                || CheckEmptys.checkEmpty(userRequestDto.getEmail())
                || CheckEmptys.checkEmpty(userRequestDto.getFullName());
    }

    //kiểm tra điều kiện
    private ResponseEntity<?>  validateRegisterInfo(UserRequestDto userRequestDto) {
        //check thông tin người dùng có để trống không
        if (isInfo(userRequestDto)) {
            messenger.setMessenger(ConstantsUser.PLEASE_ENTER_FULL_INFO);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (userRequestDto.getFullName().length() < 6) {
            messenger.setMessenger(ConstantsUser.FULL_NAME_AT_LEAST_SIX_CHARACTERS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            messenger.setMessenger(ConstantsUser.EMAIL_ALREADY_EXISTS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        String regex = "^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*@gmail.com$";
        if (!Pattern.matches(regex, userRequestDto.getEmail())) {
            messenger.setMessenger(ConstantsUser.INVALID_EMAIL);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByPhoneNumber(userRequestDto.getPhoneNumber())) {
            messenger.setMessenger(ConstantsUser.PHONE_NUMBER_ALREADY_EXISTS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (userRequestDto.getPhoneNumber().length() < 10) {
            messenger.setMessenger(ConstantsUser.PHONE_NUMBER_TEN_DIGITS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (!userRequestDto.getUsername().matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
            messenger.setMessenger(ConstantsUser.USERNAME_SPECIAL_CHARACTERS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            messenger.setMessenger(ConstantsUser.USERNAME_ALREADY_EXISTS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }

        if (userRequestDto.getUsername().length() < 6
                || userRequestDto.getPassword().length() < 6) {
            messenger.setMessenger(ConstantsUser.USERNAME_PASSWORD_MIN_SIX_CHARACTERS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        if (!userRequestDto.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+]).{6,}$")) {
            messenger.setMessenger(ConstantsUser.PASSWORD_REQUIREMENTS);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public ResponseEntity<?> register(UserRequestDto userRequestDto) {

        try {
            // check thông tin trước khi đăng kí tk
//            ResponseEntity<?> validationResponse = validateRegisterInfo(userRequestDto);
//            if (validationResponse != null) {
//                return validationResponse;
//            }
            //tạo 1 User để luu vào csdl
            DAOUser daoUser = new DAOUser();
            // gán thông tin cho user lấy từ dto
            daoUser.setUsername(userRequestDto.getUsername());
            daoUser.setPassword(bcryptEncoder.encode(userRequestDto.getPassword()));
            daoUser.setAddress(userRequestDto.getAddress());
            daoUser.setEmail(userRequestDto.getEmail());
            daoUser.setStatus(0);//tài khoản k bị khóa; 1 tài bị khóa
            daoUser.setFullName(userRequestDto.getFullName());
            daoUser.setPhoneNumber(userRequestDto.getPhoneNumber());

            //2 mac dinh laf user
            Role role = roleRepository.findById(2L).orElse(null);
            daoUser.setRole(role);
            //lưu
            userRepository.save(daoUser);
            log.info("Tạo tài khoản thành công, " + "username: " + daoUser.getUsername());
            messenger.setMessenger("Tạo tài khoản thành công");
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred ");
            messenger.setMessenger(ConstantsUser.ERROR_CREATING_ACCOUNT);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
    }

    // hàm check tk mk
    private ResponseEntity<?> validateLoginInfo(JwtRequest authenticationRequest) {

        if (CheckEmptys.checkEmpty(authenticationRequest.getUsername())
                && CheckEmptys.checkEmpty(authenticationRequest.getPassword())) {
            messenger.setMessenger(ConstantsUser.UNAUTHORIZED);
            return new ResponseEntity<>(messenger, HttpStatus.UNAUTHORIZED);
        }
        if (CheckEmptys.checkEmpty(authenticationRequest.getUsername())
        ) {
            messenger.setMessenger(ConstantsUser.PLEASE_ENTER_USERNAME);
            return new ResponseEntity<>(messenger, HttpStatus.UNAUTHORIZED);
        }
        if (CheckEmptys.checkEmpty(authenticationRequest.getPassword())
        ) {
            messenger.setMessenger(ConstantsUser.PLEASE_ENTER_PASSWORD);
            return new ResponseEntity<>(messenger, HttpStatus.UNAUTHORIZED);
        }

        return null;
    }

    //login
    public ResponseEntity<?> login(JwtRequest authenticationRequest) throws Exception {
        try {
            //check username password
            ResponseEntity<?> validationResponse = validateLoginInfo(authenticationRequest);
            if (validationResponse != null) {
                return validationResponse;
            }

            //xác thực thông tin đăng nhập
            authenticate(authenticationRequest.getUsername().trim(), authenticationRequest.getPassword());
            //check tk co bi khoa kho
            if(userDetailsService.userRepository.existsByUsernameAndStatus(authenticationRequest.getUsername(),1)){
                messenger.setMessenger("Tài khoản của bạn đã bị khóa, Vui lòng liên hệ admin");
                return new ResponseEntity<>(messenger, HttpStatus.UNAUTHORIZED);
            }
            //tạo 1 userDetails
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            // tạo token
            final String token = jwtTokenUtil.generateToken(userDetails, userDetailsService.getUserDto(authenticationRequest.getUsername()));

            // lâấy thông tin ueser trừ mật khẩu
            DAOUser daoUser = userRepository.findByUsername(authenticationRequest.getUsername());

            log.info(authenticationRequest.getUsername() + " đăng nhập");
            //tra về token
            return ResponseEntity.ok(new JwtResponse(token, daoUser));
        } catch (Exception e) {
            messenger.setMessenger(ConstantsUser.INVALID_CREDENTIALS);
            return new ResponseEntity<>(messenger, HttpStatus.UNAUTHORIZED);
        }
    }

    // xác thuực người dùng
    private void authenticate(String username, String password) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // găắn token chứa tt vào SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public ResponseEntity<?> deleteUser( Long idUser)  {

        if(userRepository.existsById(idUser)){
            userRepository.deleteById(idUser);

            messenger.setMessenger("Xóa thành công id: "+idUser);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        }
        messenger.setMessenger("Người dùng không tồn tại");
        return new ResponseEntity<>(messenger, HttpStatus.CREATED);
    }
}
