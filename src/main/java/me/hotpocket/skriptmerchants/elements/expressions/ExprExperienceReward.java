package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprExperienceReward extends SimplePropertyExpression<MerchantRecipe, Boolean> {

    static {
        register(ExprExperienceReward.class, Boolean.class, "experience reward", "merchantrecipes");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "experience reward";
    }

    @Override
    public @Nullable Boolean convert(MerchantRecipe merchantRecipe) {
        return merchantRecipe.hasExperienceReward();
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
                        recipe.setExperienceReward((Boolean) delta[0]);
                    break;
                case RESET:
                case DELETE:
                    recipe.setExperienceReward(false);
                    break;
            }
        }
    }
}
