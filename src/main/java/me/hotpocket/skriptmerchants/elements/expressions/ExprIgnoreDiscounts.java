package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprIgnoreDiscounts extends SimplePropertyExpression<MerchantRecipe, Boolean> {

    static {
        register(ExprIgnoreDiscounts.class, Boolean.class, "ignore discounts", "merchantrecipes");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "ignore discounts";
    }

    @Override
    public @Nullable Boolean convert(MerchantRecipe merchantRecipe) {
        return merchantRecipe.shouldIgnoreDiscounts();
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, RESET, DELETE -> CollectionUtils.array(Boolean.class);
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (MerchantRecipe recipe : getExpr().getAll(event)) {
            switch (mode) {
                case SET:
                    if (delta != null)
                        recipe.setIgnoreDiscounts((Boolean) delta[0]);
                    break;
                case RESET:
                case DELETE:
                    recipe.setIgnoreDiscounts(false);
                    break;
            }
        }
    }
}
