package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Console;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.OfflinePlayerStub;
import com.earth2me.essentials.utils.AdventureUtil;
import net.ess3.api.TranslatableException;
import net.essentialsx.api.v2.events.UserPardonEvent;
import org.bukkit.BanList;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

import java.util.logging.Level;

import static com.earth2me.essentials.I18n.tlLiteral;

public class Commandunban extends EssentialsCommand {
    public Commandunban() {
        super("unban");
    }

    @Override
    public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }

        final User kicker = sender.isPlayer() ? ess.getUser(sender.getPlayer()) : null;

        String name;
        User user;
        try {
            user = getPlayer(server, args, 0, true, true);
            name = user.getName();
        } catch (final PlayerNotFoundException e) {
            user = ess.getUser(new OfflinePlayerStub(args[0], ess.getServer()));
            final OfflinePlayer player = server.getOfflinePlayer(args[0]);
            name = player.getName();
            if (!player.isBanned()) {
                throw new TranslatableException("playerNeverOnServer", args[0]);
            }
        }

        final UserPardonEvent event = new UserPardonEvent(user, kicker);
        ess.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        ess.getServer().getBanList(BanList.Type.NAME).pardon(name);

        final String senderDisplayName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : Console.DISPLAY_NAME;
        ess.getLogger().log(Level.INFO, AdventureUtil.miniToLegacy(tlLiteral("playerUnbanned", senderDisplayName, name)));

        ess.broadcastTl((IUser) null, "essentials.ban.notify", "playerUnbanned", senderDisplayName, name);
    }
}
