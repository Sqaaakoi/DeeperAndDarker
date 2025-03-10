package com.kyanite.deeperdarker.forge.datagen.advancements;

import com.kyanite.deeperdarker.DeeperAndDarker;
import com.kyanite.deeperdarker.registry.blocks.DDBlocks;
import com.kyanite.deeperdarker.registry.items.DDItems;
import com.kyanite.deeperdarker.registry.world.biomes.OthersideBiomes;
import com.kyanite.deeperdarker.registry.world.dimension.DDDimensions;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DDAdvancementsProvider extends AdvancementProvider {
    public DDAdvancementsProvider(DataGenerator pGenerator, ExistingFileHelper pExistingFileHelper) {
        super(pGenerator, pExistingFileHelper);
    }

    @Override
    protected void registerAdvancements(@NotNull Consumer<Advancement> consumer, @NotNull ExistingFileHelper fileHelper) {
        String path = DeeperAndDarker.MOD_ID + ":main/";
        String id = "advancements.deeperdarker.";

        Advancement root = Advancement.Builder.advancement().display(Blocks.SCULK,
                        Component.translatable(id + "root.title"),
                        Component.translatable(id + "root.description"),
                        new ResourceLocation(DeeperAndDarker.MOD_ID, "textures/gui/advancements/root.png"),
                        FrameType.TASK, false, false, false)
                .addCriterion("phantom", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.PHANTOM)))
                .save(consumer, path + "root");

        Advancement obtainMembrane = Advancement.Builder.advancement().parent(root).display(Items.PHANTOM_MEMBRANE,
                        Component.translatable(id + "obtain_membrane.title"),
                        Component.translatable(id + "obtain_membrane.description"),
                        null, FrameType.TASK, true, true, false)
                .addCriterion("membrane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PHANTOM_MEMBRANE))
                .save(consumer, path + "obtain_membrane");

        Advancement locateAncientCity = Advancement.Builder.advancement().parent(obtainMembrane).display(Blocks.DEEPSLATE_TILES,
                        Component.translatable(id + "locate_ancient_city.title"),
                        Component.translatable(id + "locate_ancient_city.description"),
                        null, FrameType.GOAL, true, true, false)
                .rewards(AdvancementRewards.Builder.experience(40))
                .addCriterion("ancient_city", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(BuiltinStructures.ANCIENT_CITY)))
                .save(consumer, path + "locate_ancient_city");

        Advancement killWarden = Advancement.Builder.advancement().parent(locateAncientCity).display(DDItems.HEART_OF_THE_DEEP.get(),
                        Component.translatable(id + "kill_warden.title"),
                        Component.translatable(id + "kill_warden.description"),
                        null, FrameType.CHALLENGE, true, true, false)
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("warden", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.WARDEN)))
                .save(consumer, path + "kill_warden");

        Advancement enterOtherside = Advancement.Builder.advancement().parent(killWarden).display(Blocks.REINFORCED_DEEPSLATE,
                        Component.translatable(id + "enter_otherside.title"),
                        Component.translatable(id + "enter_otherside.description"),
                        null, FrameType.GOAL, true, true, false)
                .addCriterion("otherside", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(DDDimensions.OTHERSIDE_LEVEL))
                .save(consumer, path + "enter_otherside");

        Advancement.Builder.advancement().parent(enterOtherside).display(DDBlocks.ECHO_LOG.get(),
                        Component.translatable(id + "explore_otherside.title"),
                        Component.translatable(id + "explore_otherside.description"),
                        null, FrameType.CHALLENGE, true, true, false)
                .rewards(AdvancementRewards.Builder.experience(120))
                .addCriterion("otherside_deeplands", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(OthersideBiomes.OTHERSIDE_DEEPLANDS)))
                .addCriterion("overcast_columns", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(OthersideBiomes.OVERCAST_COLUMNS)))
                .addCriterion("echoing_forest", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(OthersideBiomes.ECHOING_FOREST)))
                .requirements(RequirementsStrategy.AND).save(consumer, path + "explore_otherside");

        Advancement.Builder.advancement().parent(killWarden).display(DDItems.REINFORCED_ECHO_SHARD.get(),
                        Component.translatable(id + "reinforce_shard.title"),
                        Component.translatable(id + "reinforce_shard.description"),
                        null, FrameType.TASK, true, true, false)
                .addCriterion("reinforced_shard", InventoryChangeTrigger.TriggerInstance.hasItems(DDItems.REINFORCED_ECHO_SHARD.get()))
                .save(consumer, path + "reinforce_shard");

        Advancement.Builder.advancement().parent(enterOtherside).display(DDItems.SOUL_ELYTRA.get(),
                        Component.translatable(id + "obtain_soul_elytra.title"),
                        Component.translatable(id + "obtain_soul_elytra.description"),
                        null, FrameType.TASK, true, true, false)
                .addCriterion("obtain_soul_elytra", InventoryChangeTrigger.TriggerInstance.hasItems(DDItems.SOUL_ELYTRA.get()))
                .save(consumer, path + "obtain_soul_elytra");

        Advancement.Builder.advancement().parent(enterOtherside).display(DDItems.SCULK_TRANSMITTER.get(),
                        Component.translatable(id + "obtain_transmitter.title"),
                        Component.translatable(id + "obtain_transmitter.description"),
                        null, FrameType.TASK, true, true, false)
                .addCriterion("transmitter", InventoryChangeTrigger.TriggerInstance.hasItems(DDItems.SCULK_TRANSMITTER.get()))
                .save(consumer, path + "obtain_transmitter");

        Advancement.Builder.advancement().parent(enterOtherside).display(DDBlocks.SCULK_STONE.get(),
                        Component.translatable(id + "obtain_sculk_stone.title"),
                        Component.translatable(id + "obtain_sculk_stone.description"),
                        null, FrameType.TASK, false, false, true)
                .addCriterion("sculk_stone", InventoryChangeTrigger.TriggerInstance.hasItems(DDBlocks.COBBLED_SCULK_STONE.get()))
                .save(consumer, path + "obtain_sculk_stone");

        Advancement.Builder.advancement().parent(enterOtherside).display(DDItems.WARDEN_CARAPACE.get(),
                        Component.translatable(id + "obtain_warden_carapace.title"),
                        Component.translatable(id + "obtain_warden_carapace.description"),
                        null, FrameType.TASK, false, false, true)
                .addCriterion("carapace", InventoryChangeTrigger.TriggerInstance.hasItems(DDItems.WARDEN_CARAPACE.get()))
                .save(consumer, path + "obtain_warden_carapace");


    }
}
