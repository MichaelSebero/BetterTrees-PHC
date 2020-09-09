package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockBranchPamSpecial;
import com.ferreusveritas.dynamictreesphc.dropcreators.DropCreatorFruitLogProduct;

import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamFruitLog;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeMaple extends TreeFamilyPHC {
	public static final String speciesName = FruitRegistry.MAPLE;
	
	//Species need not be created as a nested class.  They can be created after the tree has already been constructed.
	public class TreeMapleSpecies extends Species {
		
		public TreeMapleSpecies(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModBlocks.mapleLeavesProperties);
			
			//Setup the same as biomes o' plenty addon
			setBasicGrowingParameters(0.15f, 14.0f, 4, 4, 1.05f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();

			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.JUNGLE);
		}
		
	}
		
	public TreeMaple() {
		super(new ResourceLocation(ModConstants.MODID, speciesName));

		setPrimitiveLog(FruitRegistry.getLog(speciesName).getDefaultState());

		ModBlocks.mapleLeavesProperties.setTree(this);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new TreeMapleSpecies(this));
	}

	@Override
	public BlockBranch createBranch() {
		return new BlockBranchPamSpecial(
				getName()+"branch",
				speciesName,
				0.5f);
	}
}
