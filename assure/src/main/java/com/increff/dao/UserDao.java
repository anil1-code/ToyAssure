package com.increff.dao;

import com.increff.pojo.UserPojo;
import com.increff.util.UserType;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDao extends AbstractDao {
    private static final String deleteById = "delete from UserPojo p where id=:id";
    private static final String selectById = "select p from UserPojo p where id=:id";
    private static final String selectAll = "select p from UserPojo p";
    private static final String selectByNameAndId = "select p from UserPojo p where name=:name and type=:type";

    public List<UserPojo> getAll() {
        TypedQuery<UserPojo> query = getQuery(selectAll, UserPojo.class);
        return query.getResultList();
    }

    public UserPojo getById(Long id) {
        TypedQuery<UserPojo> query = getQuery(selectById, UserPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public UserPojo add(UserPojo userPojo) {
        em().persist(userPojo);
        return userPojo;
    }

    public UserPojo getByNameAndType(String name, UserType userType) {
        TypedQuery<UserPojo> query = getQuery(selectByNameAndId, UserPojo.class);
        query.setParameter("name", name);
        query.setParameter("type", userType);
        return getSingle(query);
    }

}
