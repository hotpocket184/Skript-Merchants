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

public class ExprPriceMultiplier extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprPriceMultiplier.class, Number.class, ExpressionType.SIMPLE, "[the] price multiplier of %merchantrecipes%");
    }

    private Expression<MerchantRecipe> recipes;

    @Override
    protected @Nullable Number[] get(Event e) {
        List<Number> multiplier = new ArrayList<>();
        for (MerchantRecipe recipe : recipes.getAll(e))
            multiplier.add(recipe.getPriceMultiplier());
        return multiplier.toArray(new Number[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "price multiplier of " + recipes.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        recipes = (Expression<MerchantRecipe>) exprs[0];
        return true;
    }

    @Override
    public @javax.annotation.Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, DELETE, RESET -> CollectionUtils.array(Number.class);
            default -> null;
        };
    }

    @Override
    public void change(Event e, @javax.annotation.Nullable Object[] delta, Changer.ChangeMode mode) {
        for (MerchantRecipe recipe : recipes.getAll(e)) {
            switch (mode) {
                case SET -> {
                    if (delta != null)
                        recipe.setPriceMultiplier(((Number) delta[0]).floatValue());
                }
                case RESET, DELETE -> {
                    recipe.setPriceMultiplier(1f);
                }
            }
        }
    }
}
