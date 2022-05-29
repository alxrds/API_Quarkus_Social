package com.alexandre.quarkussocial.rest.repositories;

import com.alexandre.quarkussocial.rest.models.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
