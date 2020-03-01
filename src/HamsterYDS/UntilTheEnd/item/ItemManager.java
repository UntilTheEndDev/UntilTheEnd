package HamsterYDS.UntilTheEnd.item;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.basics.AnimateWood;
import HamsterYDS.UntilTheEnd.item.basics.Ashes;
import HamsterYDS.UntilTheEnd.item.basics.BlueGum;
import HamsterYDS.UntilTheEnd.item.basics.CatTail;
import HamsterYDS.UntilTheEnd.item.basics.CowHair;
import HamsterYDS.UntilTheEnd.item.basics.DogTooth;
import HamsterYDS.UntilTheEnd.item.basics.Gear;
import HamsterYDS.UntilTheEnd.item.basics.Horn;
import HamsterYDS.UntilTheEnd.item.basics.PigSkin;
import HamsterYDS.UntilTheEnd.item.basics.RabbitFur;
import HamsterYDS.UntilTheEnd.item.basics.RedGum;
import HamsterYDS.UntilTheEnd.item.basics.Scale;
import HamsterYDS.UntilTheEnd.item.basics.Sclerite;
import HamsterYDS.UntilTheEnd.item.basics.SpiderGland;
import HamsterYDS.UntilTheEnd.item.basics.Spit;
import HamsterYDS.UntilTheEnd.item.clothes.BushesHat;
import HamsterYDS.UntilTheEnd.item.clothes.Earmuff;
import HamsterYDS.UntilTheEnd.item.clothes.EyeballUmbrella;
import HamsterYDS.UntilTheEnd.item.clothes.Garland;
import HamsterYDS.UntilTheEnd.item.clothes.StrawHat;
import HamsterYDS.UntilTheEnd.item.combat.BlowArrow1;
import HamsterYDS.UntilTheEnd.item.combat.BlowArrow2;
import HamsterYDS.UntilTheEnd.item.combat.BlowArrow3;
import HamsterYDS.UntilTheEnd.item.magic.FireWand;
import HamsterYDS.UntilTheEnd.item.materials.Brick;
import HamsterYDS.UntilTheEnd.item.materials.Coin;
import HamsterYDS.UntilTheEnd.item.materials.Fern;
import HamsterYDS.UntilTheEnd.item.materials.Hail;
import HamsterYDS.UntilTheEnd.item.materials.NightMare;
import HamsterYDS.UntilTheEnd.item.materials.Plank;
import HamsterYDS.UntilTheEnd.item.materials.PurpleGum;
import HamsterYDS.UntilTheEnd.item.materials.Reed;
import HamsterYDS.UntilTheEnd.item.materials.Rope;
import HamsterYDS.UntilTheEnd.item.science.Detector;
import HamsterYDS.UntilTheEnd.item.science.Element;
import HamsterYDS.UntilTheEnd.item.science.Hygrometer;
import HamsterYDS.UntilTheEnd.item.science.IceFlingomatic;
import HamsterYDS.UntilTheEnd.item.science.LightningArrester;
import HamsterYDS.UntilTheEnd.item.science.Refridgerator;
import HamsterYDS.UntilTheEnd.item.science.Thermometer;
import HamsterYDS.UntilTheEnd.item.survival.ACDDrug;
import HamsterYDS.UntilTheEnd.item.survival.FlowerUmbrella;
import HamsterYDS.UntilTheEnd.item.survival.HealingSalve;
import HamsterYDS.UntilTheEnd.item.survival.HoneyPoultice;
import HamsterYDS.UntilTheEnd.item.survival.MovablePack;
import HamsterYDS.UntilTheEnd.item.survival.NormalPack;
import HamsterYDS.UntilTheEnd.item.survival.PigPack;
import HamsterYDS.UntilTheEnd.item.survival.Reviver;
import HamsterYDS.UntilTheEnd.item.survival.Umbrella;
import HamsterYDS.UntilTheEnd.item.survival.WarmStone;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ItemManager implements Listener{
	public static UntilTheEnd plugin;
	public static HashMap<String,String> idsAndNames=new HashMap<String,String>();
	public static HashMap<String,ItemStack> namesAndItems=new HashMap<String,ItemStack>();
	public static HashMap<ItemStack,String> itemsAndIds=new HashMap<ItemStack,String>();
	public static HashMap<ItemStack,NamespacedKey> nsks=new HashMap<ItemStack,NamespacedKey>();
	public static HashMap<String,ItemStack> canPlaceBlocks=new HashMap<String,ItemStack>();
	public static HashMap<ItemStack,HashMap<ItemStack,Integer>> recipes=new HashMap<ItemStack,HashMap<ItemStack,Integer>>();
	public ItemManager(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		File file=new File(plugin.getDataFolder(),"itemsets.yml");
		File file2=new File(plugin.getDataFolder(),"items.yml");
		if(!file.exists()) 
			plugin.saveResource("itemsets.yml",true);
		if(!file2.exists()) 
			plugin.saveResource("items.yml",true);
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		YamlConfiguration yaml2=YamlConfiguration.loadConfiguration(file2);
		
		for(String path:yaml2.getKeys(false)) {
			if(!yaml2.getBoolean(path+".enable")) continue;
			String id=yaml2.getString(path+".id");
			ItemStack item=yaml.getItemStack(path);
			idsAndNames.put(id,"§6"+path);
			namesAndItems.put("§6"+path,item);
			itemsAndIds.put(item,id); 
			nsks.put(item,new NamespacedKey(plugin,"ute."+id.toLowerCase()));
		}
		new Brick();
		new Plank();
		new Rope();
		new NightMare();
		new Coin();
		new Fern();
		new Hail();
		new Reed();

		new CatTail();
		new CowHair();
		new DogTooth();
		new Horn();
		new PigSkin();
		new RabbitFur();
		new Scale();
		new Sclerite();
		new SpiderGland();
		new Spit();
		new Gear();
		new Ashes();
		new BlueGum();
		new RedGum();
		new PurpleGum();
		new AnimateWood();

		new StrawHat();
		new Garland();
		new Earmuff();
		new BushesHat();
		new EyeballUmbrella();

		new MovablePack();
		new NormalPack();
		new PigPack();
		new Reviver();
		new WarmStone();
		new Umbrella();
		new FlowerUmbrella();
		new HealingSalve();
		new HoneyPoultice();
		new ACDDrug();

		new BlowArrow1();
		new BlowArrow2();
		new BlowArrow3();

		new Element();
		new Thermometer();
		new Hygrometer();
		new LightningArrester();
		new IceFlingomatic();
		new Refridgerator();
		new Detector();

		new FireWand();
		
		new ItemProvider(plugin);
		if(plugin.getConfig().getBoolean("item.sawer.enable")) 
			new ItemSawer().runTaskTimer(plugin,0L,20L);
	}
	private static int[] slots=new int[] {15,14,16,13};
	public static void registerRecipe(HashMap<ItemStack,Integer> materials,ItemStack result,String category) {
		ShapelessRecipe recipe=new ShapelessRecipe(nsks.get(result),result);
		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,result);
		int tot=0;
		for(ItemStack material:materials.keySet()) {
			ItemStack materialClone=material.clone();
			materialClone.setAmount(materials.get(material));
			inv.setItem(slots[tot++],materialClone);
			recipe.addIngredient(materials.get(material),material.getType());
		}
		Bukkit.addRecipe(recipe);
		UntilTheEndApi.GuideApi.addCraftToItem(result,inv);
		UntilTheEndApi.GuideApi.addItemToCategory(category,result);
		recipes.put(result,materials);
	}
	@EventHandler public void onPlace(BlockPlaceEvent event) {
		if(event.isCancelled()) return;
		ItemStack item=event.getItemInHand().clone(); item.setAmount(1);
		if(itemsAndIds.containsKey(item)) {
			String id=itemsAndIds.get(item);
			if(canPlaceBlocks.containsKey(id)) return;
			event.setCancelled(true);
			event.getPlayer().sendMessage("§6[§cUntilTheEnd§6]§r 您不能将该物品放置于地上！");
		}
	}
	@EventHandler public void onCraft(CraftItemEvent event) {
		Recipe recipe=event.getRecipe();
		ItemStack result=recipe.getResult().clone();result.setAmount(1);
		if(itemsAndIds.containsKey(result)) return;
		for(ItemStack item:event.getClickedInventory().getContents()) {
			if(item==null) return;
			ItemStack itemClone=item.clone();itemClone.setAmount(1);
			if(itemsAndIds.containsKey(itemClone)) {
				event.setCancelled(true);
				event.getWhoClicked().sendMessage("§6[§cUntilTheEnd§6]§r 您不能用UTE物品合成其它物品！");
			}
		}
	}
	@EventHandler public void onCraft1(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult().clone();
        item.setAmount(1);
        if (itemsAndIds.containsKey(item)) 
        	for(ItemStack material:recipes.get(item).keySet()) 
        		if(!event.getInventory().containsAtLeast(material,recipes.get(item).get(material)))
        			event.setCancelled(true);
	}
}
