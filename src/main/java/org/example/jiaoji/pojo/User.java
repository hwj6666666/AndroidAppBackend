package org.example.jiaoji.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private Integer level;
    private Integer state;
    private String email;
    private String username;
    private String avatar= "https://randomuser.me/api/portraits/men/36.jpg";
    private String note;
    private String password;
}
