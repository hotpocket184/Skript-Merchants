package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExprMerchantReward extends SimpleExpression<ItemType> {

    static {
        Skript.registerExpression(ExprMerchantReward.class, ItemType.class, ExpressionType.SIMPLE, "[the] [merchant] reward[s] of %merchantrecipes%");
    }

    private Expression<MerchantRecipe> recipes;

    @Override
    protected @Nullable ItemType[] get(Event e) {
        List<ItemType> rewards = new ArrayList<>();
        for (MerchantRecipe recipe : recipes.getAll(e)) {
            rewards.add(new ItemType(recipe.getResult()));
        }
        return rewards.toArray(new ItemType[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the rewards of ";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}
