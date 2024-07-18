package com.namuk.gadget.controller;

import com.namuk.gadget.security.filters.JsonAuthenticationFilter;
import com.namuk.gadget.domain.Native;
import com.namuk.gadget.domain.User;
import com.namuk.gadget.dto.login.LoginRequestDTO;
import com.namuk.gadget.dto.member.MemberJoinRequestDTO;
import com.namuk.gadget.dto.nativeUser.NativeJoinRequestDto;
import com.namuk.gadget.repository.Native.NativeRepository;
import com.namuk.gadget.repository.member.MemberRepository;
import com.namuk.gadget.service.Native.NativeService;
import com.namuk.gadget.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    private final NativeService nativeService;

    private final MemberRepository memberRepository;

    private final NativeRepository nativeRepository;

    private final HttpServletRequest request;

    private final AuthenticationManager authenticationManager;

    private final JsonAuthenticationFilter jsonAuthenticationFilter;

//    @Autowired
//    public LoginController(UserService userService, NativeService nativeService, MemberRepository memberRepository, NativeRepository nativeRepository, HttpServletRequest request) {
//        this.userService = userService;
//        this.nativeService = nativeService;
//        this.memberRepository = memberRepository;
//        this.nativeRepository = nativeRepository;
//        this.request = request;
//    }

    /**
     * 사용자 회원가입을 처리하는 엔드포인트
     *
     * @param memberJoinRequestDto 사용자 회원가입 요청 DTO 객체
     * @param bindingResult        바인딩 결과 객체
     * @return                     회원가입 결과에 대한 응답 메시지
     */
    @PostMapping(value = "/signUp/user")
    public String signUpUser(@Valid @RequestBody MemberJoinRequestDTO memberJoinRequestDto, NativeJoinRequestDto nativeJoinRequestDto, BindingResult bindingResult) {
//      if(userService.checkForDuplicateFields(memberJoinRequestDto)) {
//          bindingResult.addError(new ObjectError("memberJoinRequestDto", "중복된 아이디, 이메일 또는 전화번호가 있습니다."));
//      }

        if(userService.checkForDuplicateLoginId(memberJoinRequestDto.getUserId())) {
            bindingResult.addError(new FieldError("memberJoinRequestDto", "userId", "중복된 아이디가 있습니다."));
        }

        if(userService.checkForDuplicateEmail(memberJoinRequestDto.getUserEmail())) {
            bindingResult.addError(new FieldError("memberJoinRequestDto", "userEmail", "중복된 이메일이 있습니다."));
        }

        if(userService.checkForDuplicatePhoneNumber(memberJoinRequestDto.getUserPhoneNumber())) {
            bindingResult.addError(new FieldError("memberJoinRequestDto", "userPhoneNumber", "중복된 전화번호가 있습니다."));
        }

        if (bindingResult.hasErrors()) {
            if(bindingResult.getFieldErrorCount() == 1) {
                return bindingResult.getFieldError().getDefaultMessage();
            } else {
                StringBuilder errors = new StringBuilder();
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                for (ObjectError error : allErrors) {
                    errors.append(error.getDefaultMessage()).append("\n");
                }
                return errors.toString();
            }
        }
        userService.memberJoin(memberJoinRequestDto); // 회원가입 서비스 호출
        return memberJoinRequestDto.getUserName() + "님 회원가입 성공";
    }

    /**
     * 현지인 회원가입을 처리하는 엔드포인트
     *
     * @param nativeJoinRequestDto 현지인 회원가입 요청 DTO 객체
     * @param bindingResult        바인딩 결과 객체
     * @return                     회원가입 결과에 대한 응답 메시지
     */
    @PostMapping(value = "/signUp/native")
    public String signUpNative(@Valid @RequestBody NativeJoinRequestDto nativeJoinRequestDto, BindingResult bindingResult) {

        if(nativeService.checkForDuplicateLoginId(nativeJoinRequestDto.getNativeId())) {
            bindingResult.addError(new FieldError("nativeJoinRequestDto", "nativeId", "중복된 아이디가 있습니다."));
        }

        if(nativeService.checkForDuplicateEmail(nativeJoinRequestDto.getNativeEmail())) {
            bindingResult.addError(new FieldError("nativeJoinRequestDto", "nativeEmail", "중복된 이메일이 있습니다."));
        }

        if(nativeService.checkForDuplicatePhoneNumber(nativeJoinRequestDto.getNativePhoneNumber())) {
            bindingResult.addError(new FieldError("nativeJoinRequestDto", "nativePhoneNumber", "중복된 전화번호가 있습니다."));
        }

        if (bindingResult.hasErrors()) {
            if(bindingResult.getFieldErrorCount() == 1) {
                return bindingResult.getFieldError().getDefaultMessage();
            } else {
                StringBuilder errors = new StringBuilder();
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                for (ObjectError error : allErrors) {
                    errors.append(error.getDefaultMessage()).append("\n");
                }
                return errors.toString();
            }
        }
        nativeService.nativeJoin(nativeJoinRequestDto); // 회원가입 서비스 호출
        return nativeJoinRequestDto.getNativeId() + "님 회원가입 성공";
    }

//    /**
//     * 사용자 로그인을 처리하는 엔드포인트
//     *
//     * @param memberLoginDto 로그인 요청을 담은 DTO 객체
//     * @return               로그인 성공 시 사용자 정보와 세션 ID를 포함한 맵, 실패 시 오류 메시지를 포함한 맵
//     */
//    @PostMapping("/login/user")
//    public Map<String, String> loginUser(@Valid @RequestBody MemberLoginDTO memberLoginDto) {
//       User userInfo = userService.memberLogin(memberLoginDto);
//
//       if(userInfo == null || !(userInfo.getUserPassword().equals(memberLoginDto.getUserPassword()))) {
//           return Collections.singletonMap("error", "Invalid id or password");
//       }
//
//        String sessionRandomId = UUID.randomUUID().toString();
//        HttpSession session = request.getSession();
//
//        session.setAttribute("sessionRandomId", sessionRandomId);
//        session.setAttribute("id", memberLoginDto.getUserLoginId());
//        session.setAttribute("name", userInfo.getUserName());
//
//        // Map<String, Object> HashMap = new ConcurrentHashMap<>();
//        Map<String, String> user = new HashMap<>();
//        user.put("id", (String) session.getAttribute("id"));
//        user.put("name", (String) session.getAttribute("name"));
//        user.put("sessionId", (String) session.getAttribute("sessionRandomId"));
//
//        return user;
//    }

    // 수정 (날짜: 2024년 5월 6일)
    @PostMapping(value = "/login2")
    public Map<String, String> loginV2(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        String sessionRandomId = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        Object loginInfo;

//        if(memberRepository.findByUserId(loginRequestDTO.getId()) != null) {
//
//        }

        if (memberRepository.existsByUserId(loginRequestDTO.getId())) {
            loginInfo = userService.memberLogin(loginRequestDTO);
            setSessionAttributes(session, sessionRandomId, loginInfo);
        } else if (nativeRepository.existsByNativeId(loginRequestDTO.getId())) {
            loginInfo = nativeService.nativeLogin(loginRequestDTO);
            setSessionAttributes(session, sessionRandomId, loginInfo);
        } else {
            throw new IllegalArgumentException("User or native Invalid login request.");
        }

        Map<String, String> info = new HashMap<>();
        info.put("id", (String) session.getAttribute("id"));
        info.put("name", (String) session.getAttribute("name"));
        info.put("sessionId", (String) session.getAttribute("sessionRandomId"));
        return info;
    }

    private void setSessionAttributes(HttpSession session, String sessionRandomId, Object loginInfo) {
        if (loginInfo instanceof User) {
            User userInfo = (User) loginInfo;
            session.setAttribute("sessionRandomId", sessionRandomId);
            session.setAttribute("id", userInfo.getUserId());
            session.setAttribute("name", userInfo.getUserName());
        } else if (loginInfo instanceof Native){
            Native nativeInfo = (Native) loginInfo;
            session.setAttribute("sessionRandomId", sessionRandomId);
            session.setAttribute("id", nativeInfo.getNativeId());
            session.setAttribute("name", nativeInfo.getNativeProfile());
        }
    }

//    @PostMapping(name = "/login")
//    public Map<String, String> loginV1(@Valid @RequestBody MemberLoginDTO memberLoginDto, NativeLoginDTO nativeLoginDto) {
//
//        String sessionRandomId = UUID.randomUUID().toString();
//        HttpSession session = request.getSession();
//        Object loginInfo;
//
//        if (memberRepository.existsByUserId(memberLoginDto.getUserLoginId())) {
//            loginInfo = userService.memberLogin(memberLoginDto);
//            User userInfo = (User) loginInfo;
//            session.setAttribute("sessionRandomId", sessionRandomId);
//            session.setAttribute("id", userInfo.getUserId());
//            session.setAttribute("name", userInfo.getUserName());
//        } else {
//            loginInfo = nativeService.nativeLogin(nativeLoginDto);
//            Native nativeInfo = (Native) loginInfo;
//            session.setAttribute("sessionRandomId", sessionRandomId);
//            session.setAttribute("id", nativeInfo.getNativeId());
//            session.setAttribute("name", nativeInfo.getNativeProfile());
//        }
//
//        Map<String, String> info = new HashMap<>();
//        info.put("id", (String) session.getAttribute("id"));
//        info.put("name", (String) session.getAttribute("name"));
//        info.put("sessionId", (String) session.getAttribute("sessionRandomId"));
//
//        return info;
//    }

    /**
     * 로그아웃을 나타내는 엔드포인트
     *
     * @return 로그아웃 성공 문자열
     */
    @GetMapping(value = "/logout")
    public String logout() {
        HttpSession session = request.getSession();
        session.removeAttribute("sessionRandomId");
        session.removeAttribute("id");
        session.removeAttribute("name");

        return "로그아웃";
    }
}
