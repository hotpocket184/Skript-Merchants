package me.hotpocket.skriptmerchants.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffOpenMerchant extends Effect {

    static {
        Skript.registerEffect(EffOpenMerchant.class, "open [a] [new] merchant [inventory] [(named|titled) %-string%] (to|for) %players%");
    }

    @Nullable
    private Expression<String> title;
    private Expression<Player> players;

    @Override
    protected void execute(Event e) {
        String title = this.title == null ? null : this.title.getSingle(e);
        for (Player player : players.getArray(e)) {
            player.openMerchant(Bukkit.createMerchant(title), false);
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "open merchant inventory "
                + (title == null ? "" : "named " + title.toString(e, debug))
                + " to " + players.toString(e, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        title = (Expression<String>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }
}
