package br.com.renatosantos.quarkussocial.domain.repository;

import br.com.renatosantos.quarkussocial.domain.model.Follower;
import br.com.renatosantos.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * Repository for managing follower relationships in the Quarkus Social application.
 * Implements PanacheRepository for efficient database operations.
 *
 * @author <NAME>
 */
@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    /**
     * Checks if the given follower is following themselves.
     *
     * @param follower The user who is potentially following themselves.
     * @param user The user being followed.
     * @return True if the follower is the same as the user, false otherwise.
     */
    public boolean isSelfFollow(User follower, User user){
        return follower.equals(user);
    }

    /**
     * Checks if the given follower is following the specified user.
     *
     * @param follower The user who is potentially following the other user.
     * @param user The user being followed.
     * @return True if the follower is following the user, false otherwise.
     */
    public boolean follows(User follower, User user){

        var params =
                Parameters.with("follower", follower).and("user", user).map();
        find("follower =:follower and user =:user", params);

        PanacheQuery<Follower> query = find("follower =:follower and user " +
                "=:user",params);
        Optional<Follower> result = query.firstResultOptional();
        return result.isPresent();
    }
}


