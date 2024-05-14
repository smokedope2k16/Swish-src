package dev.angel.asm.ducks;

import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;

public interface IClientPlayerInteractionManager {
    void syncItem();
    void setBlockHitDelay(int delay);
    void sendPacketWithSequence(ClientWorld world, SequencedPacketCreator packetCreator);
}