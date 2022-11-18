package com.example.restapiforsocialmedia.followers;

import com.example.restapiforsocialmedia.user.UserData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "followers")
public class Followers {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="from_user_fk")
    private UserData from;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="to_user_fk")
    private UserData to;

}

