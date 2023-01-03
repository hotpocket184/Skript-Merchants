package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprVillagerExperience extends SimplePropertyExpression<MerchantRecipe, Integer> {

    static {
        register(ExprVillagerExperience.class, Integer.class, "villager experience", "merchantrecipes");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "villager experience";
    }

    @Override
    public @Nullable Integer convert(MerchantRecipe merchantRecipe) {
        return merchantRecipe.getVillagerExperience();
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
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
                        recipe.setVillagerExperience(((Number) delta[0]).intValue());
                    break;
                case RESET:
                case DELETE:
                    recipe.setVillagerExperience(0);
                    break;
            }
        }
    }
}
