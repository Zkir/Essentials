package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Console;
import com.earth2me.essentials.OfflinePlayerStub;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.AdventureUtil;
import com.earth2me.essentials.utils.FormatUtil;
import net.ess3.api.TranslatableException;
import net.essentialsx.api.v2.events.UserBanEvent;
import org.bukkit.BanList;
import org.bukkit.Server;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static com.earth2me.essentials.I18n.tlLiteral;

public class Commandban extends EssentialsCommand {
    public Commandban() {
        super("ban");
    }

    @Override
    public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception {
        boolean nomatch = false;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final User user = sender.isPlayer() ? ess.getUser(sender.getPlayer()) : null;
        User target;
        try {
            target = getPlayer(server, args, 0, true, true);
        } catch (final PlayerNotFoundException e) {
            nomatch = true;
            target = ess.getUser(new OfflinePlayerStub(args[0], ess.getServer()));
        }
        if (!target.getBase().isOnline()) {
            if (sender.isPlayer() && !ess.getUser(sender.getPlayer()).isAuthorized("essentials.ban.offline")) {
                throw new TranslatableException("banExemptOffline");
            }
        } else if (target.isAuthorized("essentials.ban.exempt") && sender.isPlayer()) {
            throw new TranslatableException("banExempt");
        }

        final String senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : Console.NAME;
        final String senderDisplayName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : Console.DISPLAY_NAME;
        String banReason;
        if (args.length > 1) {
            banReason = FormatUtil.replaceFormat(getFinalArg(args, 1).replace("\\n", "\n").replace("|", "\n"));
        } else {
            banReason = tlLiteral("defaultBanReason");
        }

        final UserBanEvent event = new UserBanEvent(target, user, banReason, null);
        ess.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        banReason = event.getReason();

        ess.getServer().getBanList(BanList.Type.NAME).addBan(target.getName(), banReason, null, senderName);

        final String banDisplay = tlLiteral("banFormat", banReason, senderDisplayName);

        target.getBase().kickPlayer(AdventureUtil.miniToLegacy(banDisplay));
        ess.getLogger().log(Level.INFO, AdventureUtil.miniToLegacy(tlLiteral("playerBanned", senderDisplayName, target.getName(), banDisplay)));

        if (nomatch) {
            sender.sendTl("userUnknown", target.getName());
        }

        ess.broadcastTl(null, u -> !u.isAuthorized("essentials.ban.notify"), "playerBanned", senderDisplayName, target.getName(), banReason);
    }

    @Override
    protected List<String> getTabCompleteOptions(final Server server, final CommandSource sender, final String commandLabel, final String[] args) {
        if (args.length == 1) {
            return getPlayers(server, sender);
        } else {
            return Collections.emptyList();
        }
    }
}
