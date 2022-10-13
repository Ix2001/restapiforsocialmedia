package com.example.restapiforsocialmedia.posts;

import com.example.restapiforsocialmedia.comments.Comments;
import com.example.restapiforsocialmedia.comments.CommentsPublicDTO;
import com.example.restapiforsocialmedia.content.MediaContent;
import com.example.restapiforsocialmedia.content.MediaContentRepository;
import com.example.restapiforsocialmedia.exceptions.UserAlreadyExistsException;
import com.example.restapiforsocialmedia.exceptions.UserNotFoundException;
import com.example.restapiforsocialmedia.followers.Followers;
import com.example.restapiforsocialmedia.followers.FollowersRepository;
import com.example.restapiforsocialmedia.likes.PostLike;
import com.example.restapiforsocialmedia.likes.PostLikeRepository;
import com.example.restapiforsocialmedia.user.UserData;
import com.example.restapiforsocialmedia.user.UserDataBasicPublicDTO;
import com.example.restapiforsocialmedia.user.UserDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserDataRepository userDataRepository;
    private final PostLikeRepository postLikeRepository;
    private final MediaContentRepository mediaContentRepository;
    private final FollowersRepository followersRepository;


    public PostService(PostRepository postRepository, UserDataRepository userDataRepository, PostLikeRepository postLikeRepository, MediaContentRepository mediaContentRepository, FollowersRepository followersRepository) {
        this.postRepository = postRepository;
        this.userDataRepository = userDataRepository;
        this.postLikeRepository = postLikeRepository;
        this.mediaContentRepository = mediaContentRepository;
        this.followersRepository = followersRepository;
    }

    public UserDataBasicPublicDTO getPersonBasicByUsername(String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        UserDataBasicPublicDTO userDataBasicPublicDTO = new UserDataBasicPublicDTO();
        BeanUtils.copyProperties(userData, userDataBasicPublicDTO);
        return userDataBasicPublicDTO;
    }

    public PostPublicDTO postDTOtoPost(Post post) {
        PostPublicDTO postDTO = new PostPublicDTO();
        BeanUtils.copyProperties(post, postDTO);
        postDTO.setAuthor(getPersonBasicByUsername(post.getAuthor().getUsername()));
        postDTO.setLikesCount(postLikeRepository.findAllByPostId(post).size());
        postDTO.setPostPhotos(post.getMediaContents());
        List<Comments> comments = post.getComments();
        List<CommentsPublicDTO> commentsPublicDTOs = new ArrayList<>();
        comments.stream().map(comment -> {
            CommentsPublicDTO commentsPublicDTO = new CommentsPublicDTO();
            BeanUtils.copyProperties(comment, commentsPublicDTO);
            commentsPublicDTO.setAuthorOfComment(getPersonBasicByUsername(comment.getAuthorOfComment().getUsername()));
            return commentsPublicDTO;
        }).forEach(commentsPublicDTOs::add);
        postDTO.setComments(commentsPublicDTOs);
        return postDTO;
    }



    public List<PostPublicDTO> getFollowingPosts(String name, Integer page) {
        PageRequest pageable = PageRequest.of(page - 1, 10);
        UserData userData = userDataRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Followers> following = followersRepository.findByFrom(userData);
        List<PostPublicDTO> postPublicDTOS = new ArrayList<>();
        following.stream().map(Followers::getTo).map(UserData::getUsername).map(username -> postRepository.findByAuthor_Username_OrderByDateOfPostDesc(username, pageable)).forEach(posts -> posts.stream().map(this::postDTOtoPost).forEach(postPublicDTOS::add));
        return postPublicDTOS;
    }



    public void registerPost(PostRegisterDTO postRegisterDTO, String username) {
        log.info("Registering post: {}", postRegisterDTO);
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        Post post = new Post();
        post.setAuthor(userData);
        post.setDateOfPost(LocalDateTime.now());
        post.setText(postRegisterDTO.getText());
        postRepository.save(post);
        List<String> base64Photos = postRegisterDTO.getBase64Photos();
        for (String base64Photo : base64Photos) {
            String[] base64Array = base64Photo.split("[:/;]");
            String type = base64Array[1];
            if (type.equals("image")) {
                String rootLocation = "src/main/resources/static/content/" + username + "/post/images/";
                loadContent(username, base64Photo, rootLocation, post);
            }
            else if (type.equals("video")) {
                String rootLocation = "src/main/resources/static/content/" + username + "/post/videos/";
                loadContent(username, base64Photo, rootLocation, post);
            }
            else {
                log.error("Wrong type of file");
                throw new RuntimeException("Wrong type of file");
            }
        }
    }

    private void loadContent(String username, String base64, String rootLocation, Post post) {
        Path path = Paths.get(rootLocation);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String filename = rootLocation + username + "." + System.currentTimeMillis() + ".txt";
        try {
            PrintWriter writer = new PrintWriter(filename, UTF_8);
            writer.println(base64);
            writer.close();
            MediaContent mediaContent = new MediaContent();
            mediaContent.setPath(filename);
            mediaContentRepository.save(mediaContent);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new UserAlreadyExistsException("Post not found"));
        postRepository.delete(post);
    }

    public void editPost(Long id, String text) {
        Post post = postRepository.findById(id).orElseThrow(() -> new UserAlreadyExistsException("Post not found"));
        post.setText(text);
        postRepository.save(post);
    }

    public void getMostPopular(int page) {
        PageRequest pageable = PageRequest.of(page, 10);
        List<Post> posts = postRepository.findAll(pageable).getContent();
        List<PostPublicDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            postDTOs.add(postDTOtoPost(post));
        }
    }



    public List<PostPublicDTO> getPostsByUsername(String name, Integer page) {
        PageRequest pageable = PageRequest.of(page - 1, 10);
        List<Post> posts = postRepository.findByAuthor_Username_OrderByDateOfPostDesc(name, pageable);
        List<PostPublicDTO> postDTOs = new ArrayList<>();
        posts.stream().map(this::postDTOtoPost).forEach(postDTOs::add);
        return postDTOs;
    }

    public void likePost(String name, Long id) {
        UserData userData = userDataRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException("User not found"));
        Post post = postRepository.findById(id).orElseThrow(() -> new UserAlreadyExistsException("Post not found"));
        PostLike postLike = new PostLike();
        postLike.setAuthorOfLike(userData);
        postLike.setPostId(post);
        postLikeRepository.save(postLike);
    }
}

