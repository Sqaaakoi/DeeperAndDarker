package com.kyanite.deeperdarker.util;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DDCreativeModeTab {
    public static final CreativeModeTab DD_TAB = new CreativeModeTab("deeperdarker") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.SCULK);
        }
    };
}