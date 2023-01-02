package me.hotpocket.skriptmerchants.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.Nullable;

public class TypMerchantRecipe {

    static {
        if (Classes.getExactClassInfo(MerchantRecipe.class) == null) {
            Classes.registerClass(new ClassInfo<>(MerchantRecipe.class, "merchantrecipe")
                    .user("merchant ?recipes?")
                    .name("Merchant Recipe")
                    .description("Represents a merchant recipe.")
                    .since("3.0")
                    .parser(new Parser<>() {
                        @Override
                        public @Nullable MerchantRecipe parse(String s, ParseContext context) {
                            return null;
                        }

                        @Override
                        public boolean canParse(ParseContext context) {
                            return false;
                        }

                        @Override
                        public String toString(MerchantRecipe o, int flags) {
                            return o.toString();
                        }

                        @Override
                        public String toVariableNameString(MerchantRecipe o) {
                            return "merchantrecipe: " + o.toString();
                        }
                    })
            );
        }
    }
}