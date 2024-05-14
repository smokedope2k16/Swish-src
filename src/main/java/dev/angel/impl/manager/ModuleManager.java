package dev.angel.impl.manager;

import dev.angel.api.event.bus.Listener;
import dev.angel.api.event.bus.SubscriberImpl;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.module.Module;
import dev.angel.api.util.keyboard.KeyPressAction;
import dev.angel.impl.events.KeyPressEvent;
import dev.angel.impl.module.combat.aimassist.AimAssist;
import dev.angel.impl.module.combat.anchorbreaker.AnchorBreaker;
import dev.angel.impl.module.combat.anchorplacer.AnchorPlacer;
import dev.angel.impl.module.combat.antibots.AntiBots;
import dev.angel.impl.module.combat.autobowrelease.AutoBowRelease;
import dev.angel.impl.module.combat.autocrystal.AutoCrystal;
import dev.angel.impl.module.combat.autodoublehand.AutoDoubleHand;
import dev.angel.impl.module.combat.autoobsidian.AutoObsidian;
import dev.angel.impl.module.combat.autototem.AutoTotem;
import dev.angel.impl.module.combat.autoxp.AutoXP;
import dev.angel.impl.module.combat.blocker.Blocker;
import dev.angel.impl.module.combat.criticals.Criticals;
import dev.angel.impl.module.combat.killaura.Killaura;
import dev.angel.impl.module.combat.trigger.Trigger;
import dev.angel.impl.module.combat.wtap.WTap;
import dev.angel.impl.module.misc.antihunger.AntiHunger;
import dev.angel.impl.module.misc.autorekit.AutoRekit;
import dev.angel.impl.module.misc.autorespawn.AutoRespawn;
import dev.angel.impl.module.misc.deathcoordslog.DeathCoordsLog;
import dev.angel.impl.module.misc.keypearl.KeyPearl;
import dev.angel.impl.module.misc.nobreakdelay.NoBreakDelay;
import dev.angel.impl.module.misc.packetlogger.PacketLogger;
import dev.angel.impl.module.misc.payloadspoof.PayloadSpoof;
import dev.angel.impl.module.misc.popcounter.PopCounter;
import dev.angel.impl.module.misc.reach.Reach;
import dev.angel.impl.module.misc.timer.Timer;
import dev.angel.impl.module.misc.visualrange.VisualRange;
import dev.angel.impl.module.movement.elytrafly.ElytraFly;
import dev.angel.impl.module.movement.holesnap.Holesnap;
import dev.angel.impl.module.movement.inventorymove.InventoryMove;
import dev.angel.impl.module.movement.noaccel.NoAccel;
import dev.angel.impl.module.movement.noslow.NoSlow;
import dev.angel.impl.module.movement.step.Step;
import dev.angel.impl.module.movement.velocity.Velocity;
import dev.angel.impl.module.other.clickgui.ClickGUI;
import dev.angel.impl.module.other.colours.Colours;
import dev.angel.impl.module.other.hud.HUD;
import dev.angel.impl.module.player.fakelag.FakeLag;
import dev.angel.impl.module.player.fastbreak.FastBreak;
import dev.angel.impl.module.player.fastplace.FastPlace;
import dev.angel.impl.module.player.optimizer.Optimizer;
import dev.angel.impl.module.player.sprint.Sprint;
import dev.angel.impl.module.render.betterchat.BetterChat;
import dev.angel.impl.module.render.blockhighlight.BlockHighlight;
import dev.angel.impl.module.render.esp.ESP;
import dev.angel.impl.module.render.fovmodifier.FOVModifier;
import dev.angel.impl.module.render.freelook.Freelook;
import dev.angel.impl.module.render.fullbright.Fullbright;
import dev.angel.impl.module.render.holeesp.HoleESP;
import dev.angel.impl.module.render.killeffect.KillEffect;
import dev.angel.impl.module.render.modelchanger.ModelChanger;
import dev.angel.impl.module.render.nametags.Nametags;
import dev.angel.impl.module.render.newchunks.NewChunks;
import dev.angel.impl.module.render.norender.NoRender;
import dev.angel.impl.module.render.xray.XRay;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModuleManager extends SubscriberImpl implements Minecraftable {
    private final Map<Class<? extends Module>, Module> modules = new LinkedHashMap<>();

    @SuppressWarnings("DanglingJavadoc")
    public ModuleManager() {
           this.listeners.add(new Listener<>(KeyPressEvent.class) {
               @Override
               public void call(KeyPressEvent event) {
                   if (event.getKey() > 0 && mc.currentScreen == null) {
                       for (Module module : getModules()) {
                           if (module instanceof Freelook freelook && event.getKey() == module.getBind()) {
                               freelook.handleHold(event);
                               return;
                           }

                           if (event.getAction() == KeyPressAction.PRESS && event.getKey() == module.getBind()) {
                               module.toggle();
                               event.setCanceled(true);
                           }
                       }
                   }
               }
           });


        /**
         *      Combat
         */

        register(new Trigger());
        register(new AutoCrystal());
        register(new AutoXP());
        register(new AutoBowRelease());
        register(new AnchorPlacer());
        register(new AnchorBreaker());
        register(new AutoTotem());
        register(new AutoDoubleHand());
        register(new AntiBots());
        register(new AimAssist());
        register(new WTap());
        register(new AutoObsidian());
        register(new Killaura());
        register(new Criticals());
        register(new Blocker());

        /**
         *      Misc
         */

        register(new AntiHunger());
        register(new AutoRekit());
        register(new AutoRespawn());
        register(new DeathCoordsLog());
        register(new NoBreakDelay());
        register(new KeyPearl());
        register(new PayloadSpoof());
        register(new PacketLogger());
        register(new PopCounter());
        register(new Reach());
        register(new VisualRange());
        register(new Timer());

        /**
         *      Movement
         */

        register(new InventoryMove());
        register(new NoSlow());
        register(new Velocity());
        register(new Step());
        register(new Holesnap());
        register(new NoAccel());
        register(new ElytraFly());

        /**
         *      Other
         */

        register(new ClickGUI());
        register(new HUD());
        register(new Colours());

        /**
         *      Player
         */

        register(new FakeLag());
        register(new FastBreak());
        register(new FastPlace());
        register(new Optimizer());
        register(new Sprint());

        /**
         *      Render
         */

        register(new BetterChat());
        register(new BlockHighlight());
        register(new ESP());
        register(new FOVModifier());
        register(new Freelook());
        register(new Fullbright());
        register(new HoleESP());
        register(new KillEffect());
        register(new Nametags());
        register(new NewChunks());
        register(new NoRender());
        register(new ModelChanger());
        register(new XRay());
    }

    private void register(Module module) {
        modules.put(module.getClass(), module);
    }

    public Collection<Module> getModules() {
        return modules.values();
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> clazz) {
        return (T) modules.get(clazz);
    }

    public Module getModuleByAlias(String alias) {
        for (Module module : modules.values()) {
            for (String aliases : module.getAliases()) {
                if (aliases.equalsIgnoreCase(alias)) {
                    return module;
                }
            }
        }
        return null;
    }
}
