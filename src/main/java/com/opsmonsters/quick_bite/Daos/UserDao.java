package com.opsmonsters.quick_bite.Daos;


import com.opsmonsters.quick_bite.Dtos.UserDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    @PersistenceContext
    EntityManager entityManager;

    public UserDto getUserById(long userId) {
        Object[] userObject = (Object[]) entityManager.createNativeQuery(
                        "SELECT * FROM users WHERE user_id = :userId")
                .setParameter("userId", userId)
                .getSingleResult();

        UserDto userDto = new UserDto();
        userDto.setUserId(((Number) userObject[0]).longValue());
        userDto.setFirstName((String) userObject[1]);
        userDto.setLastName((String) userObject[2]);
        userDto.setEmail((String) userObject[3]);
        userDto.setPhoneNumber((String) userObject[4]);
        userDto.setProfileImageUrl((String) userObject[5]);
        return userDto;
    }


    public List<UserDto> getAllUsers() {
        List<Object[]> userObjects = entityManager.createNativeQuery("SELECT * FROM users").getResultList();
        List<UserDto> userDtoList = new ArrayList<>();

        for (Object[] object : userObjects) {
            UserDto userDto = new UserDto();
            userDto.setUserId(((Number) object[0]).longValue());
            userDto.setFirstName((String) object[1]);
            userDto.setLastName((String) object[2]);
            userDto.setEmail((String) object[3]);
            userDto.setPhoneNumber((String) object[4]);
            userDto.setProfileImageUrl((String) object[5]);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }


    public void updateUser(long userId, UserDto userDto) {
        try {
            entityManager.createNativeQuery(
                            "UPDATE users SET first_name = :firstName, last_name = :lastName, " +
                                    "email = :email, phone_number = :phoneNumber, " +
                                    "profile_image_url = :profileImageUrl WHERE user_id = :userId")
                    .setParameter("userId", userId)
                    .setParameter("firstName", userDto.getFirstName())
                    .setParameter("lastName", userDto.getLastName())
                    .setParameter("email", userDto.getEmail())
                    .setParameter("phoneNumber", userDto.getPhoneNumber())
                    .setParameter("profileImageUrl", userDto.getProfileImageUrl())
                    .executeUpdate();
        } catch (Exception e) {
            System.err.println("Error in updateUser: " + e.getMessage());
        }
    }


    public void deleteUser(long userId) {
        try {
            entityManager.createNativeQuery("DELETE FROM users WHERE user_id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
        } catch (Exception e) {
            System.err.println("Error in deleteUser: " + e.getMessage());
        }
    }
}
