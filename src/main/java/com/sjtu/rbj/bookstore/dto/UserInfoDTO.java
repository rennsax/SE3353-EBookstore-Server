package com.sjtu.rbj.bookstore.dto;

import com.sjtu.rbj.bookstore.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Necessary user info for common user who interact with the bookstore.
 * DTO for {@link com.sjtu.rbj.bookstore.entity.User}
 *
 * @author Bojun Ren
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    /** aka. the primary key */
    private Integer id;

    /** Current order with statue "pending" */
    private Integer orderId;

    private String name;
    private String avatarId;

    static public UserInfoDTO from(User user) {
        return new UserInfoDTO(user.getId(), null, user.getName(), user.getAvatarId());
    }
}