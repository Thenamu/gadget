package com.namuk.gadget.service.user;

import com.namuk.gadget.domain.User;
import com.namuk.gadget.dto.login.LoginRequestDTO;
import com.namuk.gadget.dto.member.MemberJoinRequestDTO;
import com.namuk.gadget.dto.member.UserProfileResponseDTO;
import com.namuk.gadget.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;

    @Autowired
    public UserServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 중복된 로그인 아이디가 있는지 확인하는 메서드
     *
     * @param id 확인할 로그인 아이디
     * @return 중복된 아이디가 있으면 true, 없으면 false를 반환
     */
    @Override
    public boolean checkForDuplicateLoginId(String id) {
        return memberRepository.existsByUserId(id);
    }

    /**
     * 중복된 전화번호가 있는지 확인하는 메서드
     *
     * @param phoneNumber 확인할 전화번호
     * @return 중복된 전화번호가 있으면 true, 없으면 false를 반환
     */
    @Override
    public boolean checkForDuplicatePhoneNumber(String phoneNumber) {
        return memberRepository.existsByUserPhoneNumber(phoneNumber);
    }

    /**
     * 중복된 이메일이 있는지 확인하는 메서드
     *
     * @param email 확인할 이메일
     * @return 중복된 이메일이 있으면 true, 없으면 false를 반환
     */
    @Override
    public boolean checkForDuplicateEmail(String email) {
        return memberRepository.existsByUserEmail(email);
    }

//    @Override
//    public boolean checkForDuplicateFields(MemberJoinRequestDto memberJoinRequestDto) {
//        return memberRepository.existsByUserId(memberJoinRequestDto.getId()) ||
//                memberRepository.existsByUserEmail(memberJoinRequestDto.getEmail()) ||
//                memberRepository.existsByUserPhoneNumber(memberJoinRequestDto.getPhoneNumber());
//    }

    /**
     * 사용자가 제공한 회원 로그인 정보를 사용하여 회원을 로그인
     *
     * @param loginRequestDTO 회원 로그인 정보를 포함하는 DTO 객체
     * @return 로그인된 회원의 정보를 포함하는 User 객체
     */
    public User memberLogin(LoginRequestDTO loginRequestDTO) {
        User userInfo = memberRepository.findByUserId(loginRequestDTO.getId());
        return userInfo;
    }

    /**
     * 사용자 로그인을 처리하여 해당 사용자의 정보를 Optional 객체로 반환
     *
     * @param memberLoginDto 로그인에 필요한 사용자 정보를 포함한 DTO 객체
     * @return 사용자 정보를 담은 Optional<User> 객체. 사용자가 존재하지 않거나 비밀번호가 일치하지 않을 경우 빈 Optional 반환
     */
    /* public Optional<User> memberLogin2(MemberLoginDto memberLoginDto) {
        return memberRepository.findByuId(memberLoginDto.getUserLoginId())
                .filter(user -> user.getUserPassword().equals(memberLoginDto.getUserPassword()));
    } */


    /**
     * 회원가입을 처리하는 메서드
     *
     * @param memberJoinRequestDto 사용자 회원가입 요청 DTO 객체
     * @return 회원 이름
     */
    @Override
    public String memberJoin(MemberJoinRequestDTO memberJoinRequestDto) {
        memberRepository.save(memberJoinRequestDto.toUserEntity());
        return memberJoinRequestDto.getUserName();
    }

    @Override
    public UserProfileResponseDTO getUserProfile(String id) {
        return memberRepository.findUserById(id);
    }
}