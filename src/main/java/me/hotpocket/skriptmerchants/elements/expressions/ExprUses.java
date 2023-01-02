package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExprUses extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprUses.class, Integer.class, ExpressionType.SIMPLE, "[the] uses of %merchantrecipes%");
    }

    private Expression<MerchantRecipe> recipes;

    @Override
    protected @Nullable Integer[] get(Event e) {
        List<Integer> uses = new ArrayList<>();
        for (MerchantRecipe recipe : recipes.getAll(e))
            uses.add(recipe.getMaxUses());
        return uses.toArray(new Integer[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "uses of " + recipes.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        recipes = (Expression<MerchantRecipe>) exprs[0];
        return true;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, DELETE, RESET -> CollectionUtils.array(Integer.class);
            default -> null;
        };
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (MerchantRecipe recipe : recipes.getAll(e)) {
            switch (mode) {
                case SET -> {
                    if (delta != null)
                        recipe.setUses((Integer) delta[0]);
                }
                case RESET, DELETE -> {
                    recipe.setUses(0);
                }
            }
        }
    }
}