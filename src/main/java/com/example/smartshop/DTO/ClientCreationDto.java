package com.example.smartshop.DTO;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreationDto {
    private String username;
    private String password;
    private String name;
    private String email;

}
