package me.hotpocket.skriptmerchants.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExprAllTradesOfMerchant extends SimpleExpression<MerchantRecipe> {

    static {
        Skript.registerExpression(ExprAllTradesOfMerchant.class, MerchantRecipe.class, ExpressionType.SIMPLE, "[all [[of] the]] trades of %inventories%");
    }

    private Expression<Inventory> inventories;

    @Override
    protected @Nullable MerchantRecipe[] get(Event e) {
        List<MerchantRecipe> recipes = new ArrayList<>();
        for (Inventory inventory : inventories.getArray(e)) {
            if (inventory instanceof MerchantInventory merchant) {
                recipes.addAll(merchant.getMerchant().getRecipes());
            }
        }
        return recipes.toArray(new MerchantRecipe[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends MerchantRecipe> getReturnType() {
        return MerchantRecipe.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the trades of " + inventories.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        inventories = (Expression<Inventory>) exprs[0];
        return true;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE, DELETE, RESET -> CollectionUtils.array(MerchantRecipe.class);
            default -> null;
        };
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (Inventory inventory : inventories.getAll(e)) {
            if (inventory instanceof MerchantInventory merchant) {
                String title = "Villager";
                if (!merchant.getViewers().isEmpty())
                    title = merchant.getViewers().get(0).getOpenInventory().getTitle();
                Merchant merch = Bukkit.createMerchant(title);
                switch (mode) {
                    case SET -> {
                        List<MerchantRecipe> recipeList = new ArrayList<>();
                        recipeList.add((MerchantRecipe) delta[0]);
                        merch.setRecipes(recipeList);
                    }
                    case ADD -> {
                        List<MerchantRecipe> recipeList = new ArrayList<>(merchant.getMerchant().getRecipes());
                        recipeList.add((MerchantRecipe) delta[0]);
                        merch.setRecipes(recipeList);
                    }
                    case REMOVE -> {
                        if (merchant.getMerchant().getRecipes().contains((MerchantRecipe) delta[0])) {
                            List<MerchantRecipe> recipeList = new ArrayList<>(merchant.getMerchant().getRecipes());
                            recipeList.remove((MerchantRecipe) delta[0]);
                            merch.setRecipes(recipeList);
                        }
                    }
                    case RESET, DELETE -> {
                        merch.setRecipes(Collections.emptyList());
                    }
                }
                for (HumanEntity player : new ArrayList<>(merchant.getViewers())) {
                    player.openMerchant(merch, true);
                }
            } else
                return;
        }
    }
}
