package com.example.restapiforsocialmedia.user;

import com.example.restapiforsocialmedia.content.MediaContent;
import com.example.restapiforsocialmedia.conversation.Conversation;
import com.example.restapiforsocialmedia.followers.Followers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "userdata")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Size(min = 2, max = 20)
    private String username;
    @NonNull
    @Size(min = 2, max = 20)
    private String name;
    @NonNull
    @Size(min = 2, max = 20)
    private String surname;
    @NonNull
    @Size(min = 2, max = 50)
//    @Email(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])\n",message = "Email should be valid")
    private String email;
    @NonNull
    @Size(min = 2, max = 64)
    private String password;
    private LocalDate birthday;
    private Boolean isActive;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "person_role", joinColumns = @JoinColumn(name = "person_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    private Boolean isBanned;
    private Boolean isDeleted;
    @OneToMany(mappedBy = "to", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Followers> followers;
    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Followers> following;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MediaContent> personMediaContent;
    @JsonIgnore
    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Conversation> conversations;
}
