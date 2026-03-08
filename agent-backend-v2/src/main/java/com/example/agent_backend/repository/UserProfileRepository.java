package com.example.agent_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.agent_backend.model.UserProfile;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

@Repository
public class UserProfileRepository {

    private final DynamoDbTable<UserProfile> userTable;

    public UserProfileRepository(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("UserProfiles", TableSchema.fromBean(UserProfile.class));
    }

    public void save(UserProfile userProfile) {
        userTable.putItem(userProfile);
    }

    public Optional<UserProfile> findById(String userId) {
        UserProfile user = userTable.getItem(Key.builder().partitionValue(userId).build());
        return Optional.ofNullable(user);
    }

    public Optional<UserProfile> findByEmail(String email) {
        return userTable.scan().items().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public List<UserProfile> findAll() {
        return userTable.scan().items().stream().toList();
    }

    public void delete(String userId) {
        userTable.deleteItem(Key.builder().partitionValue(userId).build());
    }
}
