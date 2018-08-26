package com.ferreusveritas.dtphc;

import com.pam.harvestcraft.HarvestCraft;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ModConstants.MODID, name=ModConstants.NAME, version=ModConstants.VERSION, dependencies=ModConstants.DEPENDENCIES)
public class DynamicTreesPHC {
	
	@Mod.Instance(ModConstants.MODID)
	public static DynamicTreesPHC instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ModTrees.init();
		if(HarvestCraft.fruitTreeConfigManager.enableFruitTreeGeneration) {
			new BiomeDataBasePopulator().populate();
		}
		preparePHC();
	}
	
	public void preparePHC() {
		//Force disable harvestcraft tree worldgen
		HarvestCraft.fruitTreeConfigManager.enableFruitTreeGeneration = false;

		//Change fruit block behavior to remove fruit completely on harvest
		BlockPamFruit.fruitRemoval = true;
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			//ModBlocks.registerBlocks(event.getRegistry());
		}
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			ModItems.registerItems(event.getRegistry());
		}
		
		@SubscribeEvent
		public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
			ModRecipes.registerRecipes(event.getRegistry());
		}
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void registerModels(ModelRegistryEvent event) {
			//ModModels.registerModels(event);
		}

	}
}
