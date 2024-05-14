package dev.angel.impl.module.player.sprint;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;


public class Sprint extends Module {

    protected final Value<Boolean> cancelInvis = new Value<>(
            new String[]{"CancelInvis", "InvisCancel", "invis"},
            "Stops sprinting when invisible to avoid footsteps",
            false
    );

    public Sprint() {
        super("Sprint", new String[]{"Sprint", "AutoSprint", "AutoRun"}, "Automatically sprints", Category.PLAYER);
        this.offerListeners(new ListenerTick(this));
        this.offerValues(cancelInvis);
    }
}
