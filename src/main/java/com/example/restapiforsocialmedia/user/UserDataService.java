package com.example.restapiforsocialmedia.user;

import com.example.restapiforsocialmedia.content.MediaContent;
import com.example.restapiforsocialmedia.content.MediaContentRepository;
import com.example.restapiforsocialmedia.exceptions.PageException;
import com.example.restapiforsocialmedia.exceptions.UserAlreadyExistsException;
import com.example.restapiforsocialmedia.exceptions.UserAuthException;
import com.example.restapiforsocialmedia.exceptions.UserNotFoundException;
import com.example.restapiforsocialmedia.followers.Followers;
import com.example.restapiforsocialmedia.followers.FollowersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
@Service
public class UserDataService implements UserDetailsService {
    private final UserDataRepository userDataRepository;
    private final FollowersRepository followersRepository;
    private final PasswordEncoder passwordEncoder;
    private final MediaContentRepository mediaContentRepository;



    public UserDataService(UserDataRepository userDataRepository, FollowersRepository followersRepository, PasswordEncoder passwordEncoder, MediaContentRepository mediaContentRepository) {
        this.userDataRepository = userDataRepository;
        this.mediaContentRepository = mediaContentRepository;
        this.followersRepository = followersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPerson(UserDataRegisterDTO userDataRegisterDTO) {
        if (userDataRepository.findByUsername(userDataRegisterDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(userDataRegisterDTO.getUsername());
        } else {
            UserData userData = new UserData();
            BeanUtils.copyProperties(userDataRegisterDTO, userData);
            userData.setPassword(passwordEncoder.encode(userDataRegisterDTO.getPassword()));
            userData.setRoles(Collections.singleton(Role.USER));
            userData.setIsActive(false);
            userData.setIsBanned(false);
            userData.setIsDeleted(false);

            userDataRepository.save(userData);
        }
    }


    public UserDataPublicDTO getPersonByUsername(String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        UserDataPublicDTO userDataPublicDTO = new UserDataPublicDTO();
        BeanUtils.copyProperties(userData, userDataPublicDTO);
        userDataPublicDTO.setFollowersCount(userData.getFollowers().size());
        userDataPublicDTO.setFollowingCount(userData.getFollowing().size());
        userDataPublicDTO.setProfilePictures(userData.getPersonMediaContent());
        return userDataPublicDTO;
    }

    public List<UserDataBasicPublicDTO> getFollowers(String username, int page) {
        if (page < 1) {
            throw new PageException("Page number can't be less than 0");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        List<UserDataBasicPublicDTO> followers = new ArrayList<>();
        List<Followers> followersList = followersRepository.findByTo(userData, pageRequest);
        followersList.stream().map(follower -> {
            UserDataBasicPublicDTO userDataBasicPublicDTO = new UserDataBasicPublicDTO();
            BeanUtils.copyProperties(follower.getFrom(), userDataBasicPublicDTO);
            return userDataBasicPublicDTO;
        }).forEach(followers::add);
        return followers;
    }

    public List<UserDataBasicPublicDTO> getFollowing(String name, int page) {
        if (page < 1) {
            throw new PageException("Page number can't be less than 0");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        UserData userData = userDataRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException(name));
        List<UserDataBasicPublicDTO> following = new ArrayList<>();
        List<Followers> followingList = followersRepository.findByFrom(userData, pageRequest);
        followingList.stream().map(Followers::getTo).forEach(person1 -> {
            UserDataBasicPublicDTO userDataBasicPublicDTO = new UserDataBasicPublicDTO();
            BeanUtils.copyProperties(person1, userDataBasicPublicDTO);
            following.add(userDataBasicPublicDTO);
        });
        return following;
    }


    public void deletePerson(String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        userData.setIsDeleted(true);
        userDataRepository.save(userData);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserAuthException(username));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userData.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
        return new User(userData.getUsername(), userData.getPassword(), authorities);
    }

    public void uploadProfilePicture(String username, UserDataBasicPublicDTO base64) {
        String base64String = base64.getProfilePicture();
        String[] base64Array = base64String.split("[:/;]");
        String type = base64Array[1];
        if (type.equals("image")) {
            String rootLocation = "src/main/resources/static/content/" + username + "/person/images/";
            loadContent(username, base64String, rootLocation);

        } else if (type.equals("video")) {
            String rootLocation = "src/main/resources/static/content/" + username + "/person/videos/";
            loadContent(username, base64String, rootLocation);
        } else {
            throw new RuntimeException("Wrong type of file");
        }
    }

    private void loadContent(String username, String base64, String rootLocation) {
        Path path = Paths.get(rootLocation);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String filename = rootLocation + username + "." + System.currentTimeMillis() + ".txt";
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        MediaContent mediaContent = new MediaContent();
        mediaContent.setPath(filename);
        mediaContentRepository.save(mediaContent);
        userDataRepository.save(userData);
        try {
            PrintWriter writer = new PrintWriter(filename, UTF_8);
            writer.println(base64);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePerson(String name, UserDataPublicDTO userDataPublicDTO) {
        UserData userData = userDataRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException(name));
        BeanUtils.copyProperties(userDataPublicDTO, userData, getNullPropertyNames(userDataPublicDTO));
        userDataRepository.save(userData);
    }


    public void banPerson(String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        userData.setIsBanned(true);
        userDataRepository.save(userData);
    }

    public void unbanPerson(String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        userData.setIsBanned(false);
        userDataRepository.save(userData);
    }

    public void undeletePerson(String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        userData.setIsDeleted(false);
        userDataRepository.save(userData);
    }

    public void makeAdmin(String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        userData.setRoles(Collections.singleton(Role.ADMIN));
        userDataRepository.save(userData);
    }

    public void makeUser(String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        userData.setRoles(Collections.singleton(Role.USER));
        userDataRepository.save(userData);
    }



    public void followPerson(String name, String username) {
        UserData userData = userDataRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException(name));
        UserData userDataToFollow = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        if (userData.getFollowing().stream().anyMatch(followers -> followers.getTo().equals(userDataToFollow))) {
            followersRepository.delete(followersRepository.findByFromAndTo(userData, userDataToFollow));
        } else {
            Followers followers = new Followers();
            followers.setFrom(userData);
            followers.setTo(userDataToFollow);
            followersRepository.save(followers);
        }
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public List<UserDataBasicPublicDTO> searchPersonByUsernameContaining(String username, int page) throws PageException {
        if (page - 1 < 0) {
            throw new PageException("Page number cannot be less than 0");
        } else {
            PageRequest pageable = PageRequest.of(page - 1, 10);
            List<UserData> userData = userDataRepository.findByUsernameContaining(username, pageable);
            List<UserDataBasicPublicDTO> userDataBasicPublicDTOS = new ArrayList<>();
            userData.stream().map(person -> {
                UserDataBasicPublicDTO userDataBasicPublicDTO = new UserDataBasicPublicDTO();
                BeanUtils.copyProperties(person, userDataBasicPublicDTO);
                return userDataBasicPublicDTO;
            }).collect(Collectors.toList());
            return userDataBasicPublicDTOS;
        }
    }

    /*public List<UserDataBasicPublicDTO> getMostPopular() {
        if (page < 1) {
            throw new PageException("Page number cannot be less than 0");
        } else {
            List<Person> persons = personRepository.findByOrderByFollowersDesc(PageRequest.of(page - 1, 10));
            return persons.stream()
                    .map(person -> {
                        UserDataBasicPublicDTO personBasicPublicDTO = new UserDataBasicPublicDTO();
                        BeanUtils.copyProperties(person, personBasicPublicDTO);
                        return personBasicPublicDTO;
                    })
                    .collect(Collectors.toList());
        }
    }*/
}
