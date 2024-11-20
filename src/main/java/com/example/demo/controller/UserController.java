package com.example.demo.controller;

import com.example.demo.jwt.JwtToken;
import com.example.demo.model.request.LoginRequest;
import com.example.demo.model.request.MemberCreationRequest;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/library")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> createMember (@RequestBody @Valid MemberCreationRequest memberCreationRequest) {
        userService.createMember(memberCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Signup Success! XD");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> loginMember (@RequestBody @Valid LoginRequest loginRequest) {
        JwtToken token = userService.login(loginRequest);
        return ResponseEntity.ok()
                .header("Authorization","Bearer " + token.getAccessToken())
                .header("RefreshToken", token.getRefreshToken())
                .body("Login Success♪♬");
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<String> reissueToken(@RequestHeader("RefreshToken") String refreshToken) {
        JwtToken newToken = userService.reissueToken(refreshToken);

        return ResponseEntity.ok()
                .header("Authorization","Bearer " + newToken.getAccessToken())
                .header("RefreshToken", newToken.getRefreshToken())
                .body("Token Reissued Successfully :D");
    }

    // 회원 탈퇴
    @DeleteMapping("/member/{username}")
    public ResponseEntity<String> deleteMember (@PathVariable("username") String username) {
        userService.deleteMember(username);
        return ResponseEntity.ok("The ID deleted successfully.");
    }
}
