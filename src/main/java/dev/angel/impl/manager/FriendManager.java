package dev.angel.impl.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.util.friend.Friend;
import dev.angel.api.util.logging.Logger;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    private final File directory = new File(ConfigManager.PATH, "friends.json");
    private List<Friend> friends = new ArrayList<>();

    public void init() {
        if (!directory.exists()) {
            try {
                Logger.getLogger().log(Level.INFO, "Friend file " + (directory.createNewFile() ? "was created successfully" : "failed to create"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        loadFriends();
    }

    public void saveFriends() {
        if (directory.exists()) {
            try (Writer writer = new FileWriter(directory)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(friends));
            } catch (IOException e) {
                Logger.getLogger().log(Level.ERROR, "Friend file " + (directory.delete() ? "was successfully deleted" : "failed to delete"));
            }
        }
    }

    public void loadFriends() {
        if (!directory.exists()) {
            return;
        }

        try (FileReader inFile = new FileReader(directory)) {
            friends = new ArrayList<>(new GsonBuilder().setPrettyPrinting().create().fromJson(inFile, new TypeToken<ArrayList<Friend>>() {
            }.getType()));
        } catch (Exception ignored) { }
    }

    public void addFriend(String name) {
        if (isFriend(name)) {
            return;
        }

        friends.add(new Friend(name));
    }

    public Friend getFriend(String label) {
        for (Friend friend : friends) {
            if (friend.getLabel().equalsIgnoreCase(label)) {
                return friend;
            }
        }
        return null;
    }

    public boolean isFriend(String label) {
        return getFriend(label) != null;
    }

    public boolean isFriend(PlayerEntity player) {
        return getFriend(EntityUtil.getName(player)) != null;
    }

    public void removeFriend(String name) {
        Friend friend = getFriend(name);
        if (friend != null) {
            friends.remove(friend);
        }
    }

    public List<Friend> getFriends() {
        return friends;
    }
}
