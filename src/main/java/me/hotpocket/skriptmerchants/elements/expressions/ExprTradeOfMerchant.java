package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ExprTradeOfMerchant extends PropertyExpression<Inventory, MerchantRecipe> {

    static {
        register(ExprTradeOfMerchant.class, MerchantRecipe.class, "trade %number%", "merchantinventories");
    }

    private Expression<Number> tradeNumber;

    @Override
    protected MerchantRecipe[] get(Event e, Inventory[] source) {
        Number tradeNumber = this.tradeNumber.getSingle(e);
        if (tradeNumber == null)
            return null;
        int trade = tradeNumber.intValue();
        List<MerchantRecipe> recipes = new ArrayList<>();
        for (Inventory inventory : source) {
            if (inventory instanceof MerchantInventory merchant) {
                if (merchant.getMerchant().getRecipeCount() >= trade)
                    recipes.add(merchant.getMerchant().getRecipe(trade - 1));
            }
        }
        return recipes.toArray(new MerchantRecipe[0]);
    }

    @Override
    public Class<? extends MerchantRecipe> getReturnType() {
        return MerchantRecipe.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "trade " + tradeNumber.toString(e, debug) + " of " + getExpr().toString(e, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        tradeNumber = (Expression<Number>) exprs[0];
        return true;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> CollectionUtils.array(MerchantRecipe.class);
            default -> null;
        };
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        int slot = tradeNumber.getSingle(e).intValue();
        for (Inventory inventory : getExpr().getArray(e)) {
            if (inventory instanceof MerchantInventory merchant) {
                switch (mode) {
                    case SET -> {
                        if (merchant.getMerchant().getRecipeCount() < slot) {
                            List<MerchantRecipe> recipeList = new ArrayList<>(merchant.getMerchant().getRecipes());
                            recipeList.add((MerchantRecipe) delta[0]);
                            merchant.getMerchant().setRecipes(recipeList);
                        } else {
                            merchant.getMerchant().setRecipe(slot - 1, (MerchantRecipe) delta[0]);
                        }
                    }
                }
                for (HumanEntity player : new ArrayList<>(merchant.getViewers())) {
                    player.openMerchant(merchant.getMerchant(), true);
                }
            }
        }
    }
}
