package shelter.backend.rest.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Date;


@RedisHash("Token")
@AllArgsConstructor
@Getter
public class Token implements Serializable {

    private String id;

    private String token;

    private String username;

    private Date createdAt;
}
