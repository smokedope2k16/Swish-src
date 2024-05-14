package dev.angel.asm.ducks;

public interface IChatHudLineVisible extends IChatHudLine {
    boolean isStartOfEntry();

    void setStartOfEntry(boolean start);
}
