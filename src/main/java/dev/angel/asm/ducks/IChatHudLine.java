package dev.angel.asm.ducks;

import com.mojang.authlib.GameProfile;

public interface IChatHudLine {
    String getText();

    int getId();

    void setId(int id);

    GameProfile getSender();

    void setSender(GameProfile profile);
}