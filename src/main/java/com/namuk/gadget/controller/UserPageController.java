package com.namuk.gadget.controller;

import com.namuk.gadget.dto.member.ChangePasswordRequestDTO;
import com.namuk.gadget.dto.member.UserProfileResponseDTO;
import com.namuk.gadget.service.user.UserModificationService;
import com.namuk.gadget.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserPageController {

    private final UserModificationService userModificationService;

    private final HttpServletRequest request;

    private final UserService userService;


//    public UserPageController(UserModificationService userModificationService, HttpServletRequest request) {
//        this.userModificationService = userModificationService;
//        this.request = request;
//    }

    public String sessionId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");
        return id;
    }

    @PostMapping(value = "/modify-password")
    public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        HttpSession session = request.getSession();
        String userId = String.valueOf(session.getAttribute("id"));

        String updated;
        try {
            updated = userModificationService.updatePassword(changePasswordRequestDTO, userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .body(updated);
    }

    /**
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/my-page/profile")
    public UserProfileResponseDTO getUserProfile(HttpServletRequest request) {
        String id = sessionId(request);
        return userService.getUserProfile(id);
    }
}
