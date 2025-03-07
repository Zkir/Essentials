package net.essentialsx.api.v2.events;

import com.earth2me.essentials.IUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Date;

/**
 * Called when a user is kicked with the /kick command.
 */
public class UserBanEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final IUser kicked;
    private final IUser kicker;
    private String reason;
    private final Date expires;
    private boolean cancelled;

    public UserBanEvent(IUser kicked, IUser kicker, String reason, Date expires) {
        this.kicked = kicked;
        this.kicker = kicker;
        this.reason = reason;
        this.expires = expires;
    }

    public IUser getKicked() {
        return kicked;
    }

    public IUser getKicker() {
        return kicker;
    }

    public String getReason() {
        return reason;
    }

    public Date getExpires(){
        return expires;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
