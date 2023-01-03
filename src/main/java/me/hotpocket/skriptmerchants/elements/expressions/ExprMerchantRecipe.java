package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import javax.annotation.Nullable;

public class ExprMerchantRecipe extends SimpleExpression<MerchantRecipe> {

    static {
        Skript.registerExpression(ExprMerchantRecipe.class, MerchantRecipe.class, ExpressionType.COMBINED, "%itemtype% [and %-itemtype%] for %itemtype%");
    }

    private Expression<ItemType> firstIngredient, result;
    @Nullable
    private Expression<ItemType> secondIngredient;

    @Override
    protected @Nullable MerchantRecipe[] get(Event e) {
        ItemType first = firstIngredient.getSingle(e);
        ItemType result = this.result.getSingle(e);
        if (first == null || result == null)
            return null;
        ItemStack itemResult = result.getRandom();
        MerchantRecipe recipe = new MerchantRecipe(itemResult, itemResult.getMaxItemUseDuration());
        recipe.addIngredient(first.getRandom());
        if (secondIngredient != null) {
            ItemType second = secondIngredient.getSingle(e);
            if (second != null)
                recipe.addIngredient(second.getRandom());
        }
        recipe.setMaxUses(999999);
        return new MerchantRecipe[]{recipe};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends MerchantRecipe> getReturnType() {
        return MerchantRecipe.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return firstIngredient.toString(e, debug)
                + (secondIngredient == null ? "" : " and " + secondIngredient.toString(e, debug))
                + " for " + result.toString(e, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        firstIngredient = (Expression<ItemType>) exprs[0];
        secondIngredient = (Expression<ItemType>) exprs[1];
        result = (Expression<ItemType>) exprs[2];
        return true;
    }
}
