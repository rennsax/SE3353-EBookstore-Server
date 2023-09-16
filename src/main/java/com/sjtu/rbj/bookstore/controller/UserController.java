package com.sjtu.rbj.bookstore.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sjtu.rbj.bookstore.annotation.Administer;
import com.sjtu.rbj.bookstore.constant.Constants;
import com.sjtu.rbj.bookstore.dto.ApiErrorResponse;
import com.sjtu.rbj.bookstore.dto.UserForAdminDTO;
import com.sjtu.rbj.bookstore.dto.UserInfoDTO;
import com.sjtu.rbj.bookstore.dto.UserStatisticDTO;
import com.sjtu.rbj.bookstore.entity.User;
import com.sjtu.rbj.bookstore.entity.UserType;
import com.sjtu.rbj.bookstore.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Deprecated
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such user!")
class NoSuchUserException extends NoSuchElementException {
}

/**
 * @author Bojun Ren
 * @data 2023/04/23
 * @apiNote This api need to be re-designed until some security features are
 *          added.
 */
@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin(Constants.ALLOW_ORIGIN)
public class UserController {

    @GetMapping
    public UserInfoDTO getUserInfoByAccount(
            @RequestParam(value = "account", defaultValue = "") String account) {
        return userService.getUserInfoByAccount(account);
    }

    @PutMapping
    public ResponseEntity<?> addUser(@RequestBody Map<String, String> params) {
        String userName = params.get("userName");
        String account = params.get("account");
        String passwd = params.get("passwd");
        if (userService.addUser(userName, account, passwd)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiErrorResponse.builder()
                .errorCode("A0111").errorMessage("The account already exists!").build());
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserInfo(@PathVariable("userId") Integer userId,
            @RequestBody Map<String, String> params) {
        String newName = params.get("name");
        userService.changeUserName(userId, newName);
    }

    @GetMapping("/{userId}/stat")
    public UserStatisticDTO getUserStatistic(@PathVariable("userId") Integer userId,
            @RequestParam(name = "beginDate", required = false) String beginDate,
            @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {
        if ((beginDate == null) ^ (endDate == null)) {
            throw new UnsupportedOperationException("Don't provide beginDate/endDate alone!");
        }
        long beginTimestamp = 0;
        long endTimestamp = Long.MAX_VALUE;
        if (beginDate != null) {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            beginTimestamp = parser.parse(beginDate + " 00:00:00").getTime();
            endTimestamp = parser.parse(endDate + " 23:59:59").getTime();
        }
        return userService.getUserStatistic(userId, beginTimestamp, endTimestamp);
    }

    @Administer
    @GetMapping({ "/all", "/dev" })
    public List<UserForAdminDTO> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserForAdminDTO> userForAdminList = new ArrayList<>();
        for (User user : userList) {
            UserForAdminDTO userInfo = UserForAdminDTO.from(user);
            userInfo.setAccount(user.getUserAccount().getAccount());
            userInfo.setPasswd(user.getUserAccount().getPasswd());
            userForAdminList.add(userInfo);
        }
        return userForAdminList;
    }

    // All methods below, are ill-designed, temporarily used.

    @Administer
    @PatchMapping("/dev/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable("id") Integer id) {
        try {
            if (!userService.changeState(id, UserType.FORBIDDEN)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        } catch (UnsupportedOperationException ex) {
            log.info(ex.toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException ex) {
            log.info(ex.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

    @Administer
    @PatchMapping("/dev/unlock/{id}")
    public ResponseEntity<?> unlockUser(@PathVariable("id") Integer id) {
        try {
            if (!userService.changeState(id, UserType.NORMAL)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        } catch (UnsupportedOperationException ex) {
            log.info(ex.toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException ex) {
            log.info(ex.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/dev/passwd/{id}")
    public ResponseEntity<?> changePasswd(@PathVariable Integer id,
            @RequestBody Map<String, String> params) {
        String newPasswd = params.get("passwd");
        if (newPasswd == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!userService.changePasswdById(id, newPasswd)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Response for uri like "/user/by?account=..".
     *
     * @deprecated
     * @see #getUserInfoByAccount
     */
    @Deprecated
    @GetMapping("/dev/by")
    public UserInfoDTO getUserInfoBy(
            @RequestParam(value = "account", defaultValue = "") String account) {
        return userService.getUserInfoByAccount(account);
    }

    @Autowired
    private UserService userService;
}
