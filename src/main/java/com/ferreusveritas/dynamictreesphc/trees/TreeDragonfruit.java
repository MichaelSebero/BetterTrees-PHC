package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockDynamicLeavesPalm;
import com.ferreusveritas.dynamictreesphc.blocks.BlockPamFruitPalm;
import com.ferreusveritas.dynamictreesphc.dropcreators.FeatureGenFruitPalm;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TreeDragonfruit extends TreePalm {

    public class SpeciesDragonfruit extends SpeciesPalm {

        public SpeciesDragonfruit(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModBlocks.dragonfruitLeavesProperties, FruitRegistry.DRAGONFRUIT, BlockPamSapling.SaplingType.WARM);
        }

        @Override
        public float getEnergy(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month
            return super.getEnergy(world, pos) * biomeSuitability(world, pos) - (CoordUtils.coordHashCode(pos.up(month), 3) % 2); // Vary the height energy by a psuedorandom hash function
        }

        public void setFruitBlock (BlockFruit fruitBlock){
            fruitBlockState = fruitBlock.getDefaultState();
            addGenFeature(new FeatureGenFruitPalm(fruitBlock, 2, fruitBlock instanceof BlockPamFruitPalm));
        }

        @Override
        public boolean canBoneMeal() {
            return false;
        }

        @Override
        public ResourceLocation getSaplingName() {
            return new ResourceLocation(ModConstants.MODID, FruitRegistry.DRAGONFRUIT);
        }

        @Override
        public SoundType getSaplingSound() {
            return SoundType.CLOTH;
        }

        @Override
        public AxisAlignedBB getSaplingBoundingBox() {
            return new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D);
        }

        @Override
        protected void fruitTreeDefaults(String name) {
            setBasicGrowingParameters(1.0f, 2.0f, 1, 4, 1.0f, fruitingRadius);
        }

        @Override
        public void addJoCodes() {
            joCodeStore.addCodesFromFile(this, "assets/" + getRegistryName().getResourceDomain() + "/trees/dragonfruit.txt");
        }

    }

    public TreeDragonfruit() {
        super(new ResourceLocation(ModConstants.MODID, "dragonfruit"));

        ModBlocks.dragonfruitLeavesProperties.setTree(this);
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesDragonfruit(this));
    }


}