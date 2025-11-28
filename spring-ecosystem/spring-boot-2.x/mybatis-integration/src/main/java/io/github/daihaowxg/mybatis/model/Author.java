package io.github.daihaowxg.mybatis.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Author {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String bio;

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}