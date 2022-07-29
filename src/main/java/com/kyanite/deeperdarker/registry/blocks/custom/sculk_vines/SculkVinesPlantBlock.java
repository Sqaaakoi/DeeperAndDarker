package com.kyanite.deeperdarker.registry.blocks.custom.sculk_vines;

import com.kyanite.deeperdarker.registry.blocks.DDBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SculkVinesPlantBlock extends GrowingPlantBodyBlock {
    public static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public SculkVinesPlantBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties, Direction.DOWN, SHAPE, false);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return DDBlocks.SCULK_VINES.get();
    }
}