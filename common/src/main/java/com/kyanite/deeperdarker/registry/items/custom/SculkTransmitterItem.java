package com.kyanite.deeperdarker.registry.items.custom;

import java.util.List;

import com.kyanite.deeperdarker.miscellaneous.DDTags;
import com.kyanite.deeperdarker.registry.sounds.DDSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class SculkTransmitterItem extends Item {
    public SculkTransmitterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if(isLinked(stack)) {
            if (pLevel.isClientSide()) {
                pPlayer.playSound(DDSounds.SCULK_TRANSMIT.get(), 0.5f, pLevel.getRandom().nextFloat() * 0.4F + 0.8F);
                return InteractionResultHolder.success(stack);
            }
            transmit(getLinkedDimension(stack, pLevel.getServer()), pPlayer, pUsedHand);
            return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide());
        }
        
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (isLinked(pContext.getItemInHand())) {
            if (pContext.getLevel().isClientSide()) {
                pContext.getPlayer().playSound(DDSounds.SCULK_TRANSMIT.get(), 0.5f, pContext.getLevel().getRandom().nextFloat() * 0.4F + 0.8F);
                return InteractionResult.SUCCESS;
            }
            transmit(getLinkedDimension(pContext.getItemInHand(), pContext.getPlayer().getServer()), pContext.getPlayer(), pContext.getHand());
            return InteractionResult.CONSUME;
        }

        if (!pContext.getLevel().getBlockState(pContext.getClickedPos()).is(DDTags.Blocks.TRANSMITTABLE)) {
            pContext.getPlayer().displayClientMessage(Component.translatable("item.deeperdarker.sculk_transmitter.not_container"), true);
            return InteractionResult.FAIL;
        }

        if (!isLinked(pContext.getItemInHand())) {
            pContext.getPlayer().playSound(DDSounds.SCULK_LINK.get(), 0.5f, pContext.getLevel().getRandom().nextFloat() * 0.4F + 0.8F);
            setBlock(pContext.getItemInHand(), pContext.getPlayer(), pContext.getHand(), pContext.getClickedPos());
            return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide());
        }

        return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide());
    }

    public boolean transmit(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        BlockPos linked = getLinkedBlockPos(pPlayer.getItemInHand(pUsedHand));
        if(isLinked(pPlayer.getItemInHand(pUsedHand))) {
            BlockState state = pLevel.getBlockState(linked);

            if(state == null || linked == null || pPlayer.isCrouching()) {
                setBlock(pPlayer.getItemInHand(pUsedHand), pPlayer, pUsedHand, null);
                return false;
            }

            if(!pLevel.getBlockState(linked).is(DDTags.Blocks.TRANSMITTABLE)) {
                pPlayer.displayClientMessage(Component.translatable("item.deeperdarker.sculk_transmitter.not_found"), true);
                setBlock(pPlayer.getItemInHand(pUsedHand), pPlayer, pUsedHand, null);
                return false;
            }

            pPlayer.playSound(DDSounds.SCULK_TRANSMIT.get(), 0.5f, pLevel.getRandom().nextFloat() * 0.4F + 0.8F);
            if(!pPlayer.isCreative()) {
                if(pPlayer.totalExperience > 1) pPlayer.giveExperiencePoints(-1);
            }

            pLevel.gameEvent(GameEvent.ENTITY_INTERACT, pPlayer.blockPosition(), GameEvent.Context.of(pPlayer));

            MenuProvider menuProvider = state.getMenuProvider(pLevel, linked);

            if(menuProvider != null) {
                pPlayer.openMenu(menuProvider);
                BlockEntity blockEntity = pLevel.getBlockEntity(linked);
                if (blockEntity instanceof ChestBlockEntity chestBlockEntity) {
                    chestBlockEntity.startOpen(pPlayer);
                } else if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                    shulkerBoxBlockEntity.startOpen(pPlayer);
                }
            }

            return true;
        }

        return false;
    }

    public BlockPos getLinkedBlockPos(ItemStack stack) {
        if(!stack.hasTag()) return null;
        if(stack.getTag().contains("linked")) {
            return new BlockPos(
                    stack.getTag().getIntArray("linked")[0],
                    stack.getTag().getIntArray("linked")[1],
                    stack.getTag().getIntArray("linked")[2]
            );
        }

        return null;
    }

    public Level getLinkedDimension(ItemStack stack, MinecraftServer server) {
        if(!stack.hasTag()) server.getLevel(Level.OVERWORLD);
        if(stack.getTag().contains("dimension")) {
            return server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(stack.getTag().getString("dimension"))));
        }
        return server.getLevel(Level.OVERWORLD);
    }

    public boolean isLinked(ItemStack stack) {
        if(!stack.hasTag()) return false;
        return stack.getTag().contains("linked");
    }

    public void setModelData(ItemStack stack, Player player, InteractionHand hand, int data) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("CustomModelData", data);
        player.getItemInHand(hand).setTag(tag);
    }

    public void setBlock(ItemStack stack, Player player, InteractionHand hand, BlockPos pos) {
        CompoundTag tag = stack.getOrCreateTag();
        if(pos == null) {
            setModelData(stack, player, hand, 0);
            tag.remove("linked");
            tag.remove("dimension");
            player.getItemInHand(hand).setTag(tag);
            return;
        }

        tag.putIntArray("linked", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        tag.putString("dimension", player.getLevel().dimension().location().toString());
        setModelData(stack, player, hand, 1);
        player.getItemInHand(hand).setTag(tag);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(isLinked(pStack))
            pTooltipComponents.add(Component.translatable("item.deeperdarker.sculk_transmitter.linked"));
        else pTooltipComponents.add(Component.translatable("item.deeperdarker.sculk_transmitter.not_linked"));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
