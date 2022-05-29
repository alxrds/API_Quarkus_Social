package com.alexandre.quarkussocial.rest.repositories;

import com.alexandre.quarkussocial.rest.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
