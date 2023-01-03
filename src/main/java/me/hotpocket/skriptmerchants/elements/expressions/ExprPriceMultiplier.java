package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprPriceMultiplier extends SimplePropertyExpression<MerchantRecipe, Float> {

    static {
        register(ExprPriceMultiplier.class, Float.class, "price multiplier", "merchantrecipes");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "price multiplier";
    }

    @Override
    public @Nullable Float convert(MerchantRecipe merchantRecipe) {
        return merchantRecipe.getPriceMultiplier();
    }

    @Override
    public @NotNull Class<? extends Float> getReturnType() {
        return Float.class;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, RESET, DELETE -> CollectionUtils.array(Number.class);
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (MerchantRecipe recipe : getExpr().getArray(event)) {
            switch (mode) {
                case SET:
                    if (delta != null)
                        recipe.setPriceMultiplier(((Number) delta[0]).floatValue());
                    break;
                case RESET:
                case DELETE:
                    recipe.setPriceMultiplier(1);
                    break;
            }
        }
    }
}
