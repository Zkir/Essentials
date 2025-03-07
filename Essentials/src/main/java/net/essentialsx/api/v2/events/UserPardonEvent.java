package net.essentialsx.api.v2.events;

import com.earth2me.essentials.IUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a user is kicked with the /kick command.
 */
public class UserPardonEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final IUser kicked;
    private final IUser kicker;
    private boolean cancelled;

    public UserPardonEvent(IUser kicked, IUser kicker) {
        this.kicked = kicked;
        this.kicker = kicker;
    }

    public IUser getKicked() {
        return kicked;
    }

    public IUser getKicker() {
        return kicker;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
