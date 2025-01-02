package com.opsmonsters.quick_bite.Daos;

import com.opsmonsters.quick_bite.models.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    public Users getUserById(long userId) {
        try {
            return entityManager.find(Users.class, userId);
        } catch (Exception e) {
            logger.error("Error retrieving user with ID {}: {}", userId, e.getMessage());
            return null;
        }
    }

    public List<Users> getAllUsers() {
        try {
            return entityManager.createQuery("SELECT u FROM Users u", Users.class).getResultList();
        } catch (Exception e) {
            logger.error("Error retrieving all users: {}", e.getMessage());
            return List.of(); // Return an empty list if an error occurs
        }
    }

    @Transactional
    public void updateUser(long userId, Users updatedUser) {
        try {
            Users existingUser = entityManager.find(Users.class, userId);
            if (existingUser != null) {
                if (updatedUser.getFirstName() != null) existingUser.setFirstName(updatedUser.getFirstName());
                if (updatedUser.getLastName() != null) existingUser.setLastName(updatedUser.getLastName());
                if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
                if (updatedUser.getPhoneNumber() != null) existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
                if (updatedUser.getProfileImageUrl() != null) existingUser.setProfileImageUrl(updatedUser.getProfileImageUrl());

                entityManager.merge(existingUser);
                logger.info("User updated successfully: ID {}", userId);
            } else {
                logger.warn("User with ID {} not found for update.", userId);
            }
        } catch (Exception e) {
            logger.error("Error updating user with ID {}: {}", userId, e.getMessage());
        }
    }

    @Transactional
    public void deleteUser(long userId) {
        try {
            Users user = entityManager.find(Users.class, userId);
            if (user != null) {
                entityManager.remove(user);
                logger.info("User deleted successfully: ID {}", userId);
            } else {
                logger.warn("User with ID {} not found for deletion.", userId);
            }
        } catch (Exception e) {
            logger.error("Error deleting user with ID {}: {}", userId, e.getMessage());
        }
    }
}
