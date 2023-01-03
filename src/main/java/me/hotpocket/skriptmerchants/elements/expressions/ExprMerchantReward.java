package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprMerchantReward extends SimplePropertyExpression<MerchantRecipe, ItemType> {

    static {
        register(ExprIgnoreDiscounts.class, Boolean.class, "reward", "merchantrecipes");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "reward";
    }

    @Override
    public @Nullable ItemType convert(MerchantRecipe merchantRecipe) {
        return new ItemType(merchantRecipe.getResult());
    }

    @Override
    public @NotNull Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }
}
