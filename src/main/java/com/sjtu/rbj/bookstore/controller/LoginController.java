package com.sjtu.rbj.bookstore.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sjtu.rbj.bookstore.constant.Constants;
import com.sjtu.rbj.bookstore.entity.UserType;
import com.sjtu.rbj.bookstore.service.UserService;

@Deprecated
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Wrong account or password!")
class LoginErrorException extends RuntimeException {
}

/**
 * @author Bojun Ren
 * @date 2023/04/08
 */
@RestController
@CrossOrigin(Constants.ALLOW_ORIGIN)
public class LoginController {

    /** See below */
    @Deprecated
    public void getUser(@RequestBody Map<String, String> params) {
        String account = params.get(Constants.ACCOUNT);
        String passwd = params.get(Constants.PASSWORD);
        if (account == null || passwd == null || !userService.enableLogin(account, passwd)) {
            throw new LoginErrorException();
        }
    }

    /** Response for uri "/login" with method POST */
    @PostMapping("/login")
    public ResponseEntity<?> checkLogin(@RequestBody Map<String, String> params) {
        String account = params.get(Constants.ACCOUNT);
        String passwd = params.get(Constants.PASSWORD);
        if (account == null || passwd == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<UserType> maybeUserType = userService.login(account, passwd);
        if (!maybeUserType.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, String> jsonNode = new HashMap<>(1);
        jsonNode.put("userType", maybeUserType.get().toString());
        return ResponseEntity.ok().body(jsonNode);
    }

    @Autowired
    private UserService userService;
}
