package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.items.DendroPotion;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.trees.SpeciesFruit;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class ModRecipes {
	
	public static void register(IForgeRegistry<IRecipe> registry) {
		//Create dirt bucket exchange recipes
		ModTrees.phcFruitSpecies.values().forEach(ModRecipes::speciesRecipes);
		//Cinnamon Maple and Paperbark arent included in phcFruitSpecies so we add them separately
		for (String name : new String[]{FruitRegistry.CINNAMON, FruitRegistry.MAPLE, FruitRegistry.PAPERBARK})
			speciesRecipes(TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, name)));

		//Add apple sapling recipe separately as the PHC apple tree didnt get a dynamic version
		Species appleSpecies = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictrees.ModConstants.MODID, "apple"));
		ItemStack appleSapling = new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", "apple_sapling"))));
		com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(appleSapling, appleSpecies.getSeedStack(1), false, "seedfromsapling");

		//Add the fruit recipes for the ripe peppercorn
		Species peppercornSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, FruitRegistry.PEPPERCORN));
		ItemStack peppercornSapling = new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", "peppercorn_sapling"))));
		com.ferreusveritas.dynamictrees.ModRecipes.createFruitOnlyExchangeRecipes(peppercornSapling, peppercornSpecies.getSeedStack(1), new ItemStack(ModItems.ripePeppercorn), true, new ResourceLocation(ModConstants.MODID, FruitRegistry.PEPPERCORN+"ripe"), false);

		//Add passionfruit recipes separately since the whole thing is handled in a different way
		ItemStack passionfruitSeed = new ItemStack(ModItems.passionfruitSeed);
		ItemStack passionfruit = new ItemStack(FruitRegistry.getFood(FruitRegistry.PASSIONFRUIT));
		ItemStack passionfruitSapling = new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", "passionfruit_sapling"))));
		ItemStack passionfruitVine = new ItemStack(ModItems.passionfruitVine);
		com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipesWithFruit(passionfruitSapling, passionfruitSeed, passionfruit, true, "seedfromsapling", new ResourceLocation(ModConstants.MODID, FruitRegistry.PASSIONFRUIT),false);
		//Add a special recipe to turn the vines to saplings
		GameRegistry.addShapelessRecipe(new ResourceLocation(ModConstants.MODID, "passionfruitsaplingfromvines"), null, passionfruitSapling, Ingredient.fromStacks(passionfruitVine), Ingredient.fromItem(com.ferreusveritas.dynamictrees.ModItems.dirtBucket));
	}
	
	private static void speciesRecipes(Species species) {
		addSeedExchange(species);
//		addTransformationPotion(species); //Transformation potions only work at the TreeFamily level for now
	}

	private static void addSeedExchange(Species species) {
		Block saplingBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", species.getRegistryName().getResourcePath()+"_sapling"));
		if (saplingBlock == null) return;
		ItemStack saplingStack = new ItemStack(saplingBlock);
		ItemStack seedStack = species.getSeedStack(1);
		String fruit = species.getRegistryName().getResourcePath();

		//if the seed is not extractable from the fruit, or there is no fruit, the normal seed <-> sapling recipes are created.
		if (ModConstants.FRUITISNOTSEED.contains(fruit) || ModConstants.NOFRUIT.contains(fruit)){
			com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(saplingStack, seedStack, true, "seedfromsapling");
		} else if(species instanceof SpeciesFruit){
			Item fruitItem = ((SpeciesFruit)species).getFruitBlock().getFruitDrop().getItem();
			ItemStack fruitStack = new ItemStack(fruitItem);
			com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipesWithFruit(saplingStack, seedStack, fruitStack, true, "seedfromsapling", species.getRegistryName(), ModConstants.NUTS.contains(fruit));
		}
	}

	private static void addTransformationPotion(Species species) {
		ItemStack outputStack = com.ferreusveritas.dynamictrees.ModItems.dendroPotion.setTargetTree(new ItemStack(com.ferreusveritas.dynamictrees.ModItems.dendroPotion, 1, DendroPotion.DendroPotionType.TRANSFORM.getIndex()), species.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(com.ferreusveritas.dynamictrees.ModItems.dendroPotion, 1, DendroPotion.DendroPotionType.TRANSFORM.getIndex()), species.getSeedStack(1), outputStack);
	}

	
}
