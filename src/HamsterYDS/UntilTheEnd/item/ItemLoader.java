package HamsterYDS.UntilTheEnd.item;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
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
import HamsterYDS.UntilTheEnd.item.magic.WandCooldown;
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
public class ItemLoader implements Listener{
	public static UntilTheEnd plugin;
	public ItemLoader(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		File file=new File(plugin.getDataFolder(),"itemsets.yml");
		File file2=new File(plugin.getDataFolder(),"items.yml");
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		YamlConfiguration yaml2=YamlConfiguration.loadConfiguration(file2);
		if(!file.exists()) {
			plugin.saveResource("itemsets.yml",true);
			new ItemLoader(plugin);
			return;
		}
		if(!file2.exists()) {
			plugin.saveResource("items.yml",true);
			new ItemLoader(plugin);
			return;
		}
		Brick.item=yaml.getItemStack("石砖");
		Plank.item=yaml.getItemStack("板条");
		Rope.item=yaml.getItemStack("绳子");
		NightMare.item=yaml.getItemStack("噩梦燃料");
		Coin.item=yaml.getItemStack("金币");
		Fern.item=yaml.getItemStack("蕨类植物");
		Hail.item=yaml.getItemStack("冰雹");
		Reed.item=yaml.getItemStack("芦苇");
		
		CatTail.item=yaml.getItemStack("猫尾");
		CowHair.item=yaml.getItemStack("牛毛");
		DogTooth.item=yaml.getItemStack("狗牙");
		Horn.item=yaml.getItemStack("牛角");
		PigSkin.item=yaml.getItemStack("猪皮");
		RabbitFur.item=yaml.getItemStack("兔毛");
		Scale.item=yaml.getItemStack("鳞片");
		Sclerite.item=yaml.getItemStack("骨片");
		SpiderGland.item=yaml.getItemStack("蜘蛛腺体");
		Spit.item=yaml.getItemStack("痰");
		Gear.item=yaml.getItemStack("齿轮");
		Ashes.item=yaml.getItemStack("灰烬");
		BlueGum.item=yaml.getItemStack("蓝宝石");
		RedGum.item=yaml.getItemStack("红宝石");
		PurpleGum.item=yaml.getItemStack("紫宝石");
		AnimateWood.item=yaml.getItemStack("有生命的木头");
		
		StrawHat.item=yaml.getItemStack("草帽");
		Earmuff.item=yaml.getItemStack("兔毛耳罩");
		Garland.item=yaml.getItemStack("花环");
		BushesHat.item=yaml.getItemStack("灌木丛帽子");
		EyeballUmbrella.item=yaml.getItemStack("眼球伞");
		
		MovablePack.item=yaml.getItemStack("便携包");
		NormalPack.item=yaml.getItemStack("背包");
		PigPack.item=yaml.getItemStack("皮质背包");
		Reviver.item=yaml.getItemStack("救赎之心");
		WarmStone.item=yaml.getItemStack("暖石");
		Umbrella.item=yaml.getItemStack("伞");
		FlowerUmbrella.item=yaml.getItemStack("花伞");
		HealingSalve.item=yaml.getItemStack("治疗药膏");
		HoneyPoultice.item=yaml.getItemStack("止血剂");
		ACDDrug.item=yaml.getItemStack("ACD试验药");
		
		BlowArrow1.item=yaml.getItemStack("吹箭");
		BlowArrow2.item=yaml.getItemStack("火吹箭");
		BlowArrow3.item=yaml.getItemStack("麻醉吹箭");
		
		Element.item=yaml.getItemStack("电器元件");
		Thermometer.item=yaml.getItemStack("温度计");
		Hygrometer.item=yaml.getItemStack("湿度计");
		LightningArrester.item=yaml.getItemStack("避雷针");
		IceFlingomatic.item=yaml.getItemStack("雪球发射机");
		Refridgerator.item=yaml.getItemStack("冰箱");
		Detector.item=yaml.getItemStack("探测器");
		
		FireWand.item=yaml.getItemStack("火魔杖");
		
		if(yaml2.getBoolean("石砖"))
			new Brick();
		if(yaml2.getBoolean("板条"))
			new Plank();
		if(yaml2.getBoolean("绳子"))
			new Rope();
		if(yaml2.getBoolean("噩梦燃料"))
			new NightMare();
		if(yaml2.getBoolean("金币"))
			new Coin();
		if(yaml2.getBoolean("蕨类植物"))
			new Fern();
		if(yaml2.getBoolean("冰雹"))
			new Hail();
		if(yaml2.getBoolean("芦苇"))
			new Reed();
		
		if(yaml2.getBoolean("猫尾"))
			new CatTail();
		if(yaml2.getBoolean("牛毛"))
			new CowHair();
		if(yaml2.getBoolean("狗牙"))
			new DogTooth();
		if(yaml2.getBoolean("牛角"))
			new Horn();
		if(yaml2.getBoolean("猪皮"))
			new PigSkin();
		if(yaml2.getBoolean("兔毛"))
			new RabbitFur();
		if(yaml2.getBoolean("鳞片"))
			new Scale();
		if(yaml2.getBoolean("骨片"))
			new Sclerite();
		if(yaml2.getBoolean("蜘蛛腺体"))
			new SpiderGland();
		if(yaml2.getBoolean("痰"))
			new Spit();
		if(yaml2.getBoolean("齿轮"))
			new Gear();
		if(yaml2.getBoolean("灰烬"))
			new Ashes();
		if(yaml2.getBoolean("蓝宝石"))
			new BlueGum();
		if(yaml2.getBoolean("红宝石"))
			new RedGum();
		if(yaml2.getBoolean("紫宝石"))
			new PurpleGum();
		if(yaml2.getBoolean("有生命的木头"))
			new AnimateWood();

		if(yaml2.getBoolean("草帽"))
			new StrawHat();
		if(yaml2.getBoolean("花环"))
			new Garland();
		if(yaml2.getBoolean("兔毛耳罩"))
			new Earmuff();
		if(yaml2.getBoolean("灌木丛帽子"))
			new BushesHat();
		if(yaml2.getBoolean("眼球伞"))
			new EyeballUmbrella();
		
		if(yaml2.getBoolean("便携包"))
			new MovablePack();
		if(yaml2.getBoolean("背包"))
			new NormalPack();
		if(yaml2.getBoolean("皮质背包"))
			new PigPack();
		if(yaml2.getBoolean("救赎之心"))
			new Reviver();
		if(yaml2.getBoolean("暖石"))
			new WarmStone();
		if(yaml2.getBoolean("伞"))
			new Umbrella();
		if(yaml2.getBoolean("花伞"))
			new FlowerUmbrella();
		if(yaml2.getBoolean("治疗药膏"))
			new HealingSalve();
		if(yaml2.getBoolean("止血剂"))
			new HoneyPoultice();
		if(yaml2.getBoolean("ACD试验药"))
			new ACDDrug();
		
		if(yaml2.getBoolean("吹箭"))
			new BlowArrow1();
		if(yaml2.getBoolean("火吹箭"))
			new BlowArrow2();
		if(yaml2.getBoolean("麻醉吹箭"))
			new BlowArrow3();
		
		if(yaml2.getBoolean("电器元件"))
			new Element();
		if(yaml2.getBoolean("温度计"))
			new Thermometer();
		if(yaml2.getBoolean("湿度计"))
			new Hygrometer();
		if(yaml2.getBoolean("避雷针"))
			new LightningArrester();
		if(yaml2.getBoolean("雪球发射机"))
			new IceFlingomatic();
		if(yaml2.getBoolean("冰箱"))
			new Refridgerator();
		if(yaml2.getBoolean("探测器"))
			new Detector();
		
		if(yaml2.getBoolean("火魔杖"))
			new FireWand();
		
		new ItemProvider(plugin);
		if(plugin.getConfig().getBoolean("item.sawer.enable")) 
			new ItemSawer().runTaskTimer(plugin,0L,20L);
		new WandCooldown();
	}
	public static HashMap<String,String> canPlace=new HashMap<String,String>();
	@EventHandler public void onPlace(BlockPlaceEvent event) {
		if(event.isCancelled()) return;
		ItemStack item=event.getItemInHand().clone(); item.setAmount(1);
		if(item.getItemMeta()==null) return; 
		if(item.getItemMeta().getDisplayName()==null) return;
		if(!item.getItemMeta().getDisplayName().contains("§6")) return;
		String name=item.getItemMeta().getDisplayName();
		if(canPlace.containsKey(name)) return;
		List<Recipe> recipes=Bukkit.getRecipesFor(item);
		if(ItemProvider.items.values().contains(item))  {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§6[§c凌域§6]§r 您不能将该物品放置于地上！");
		}
	}
	@EventHandler public void onCraft(CraftItemEvent event) {
		Recipe recipe=event.getRecipe();
		if(!ItemProvider.items.values().contains(recipe.getResult())) {
			for(ItemStack item:event.getClickedInventory().getContents()) {
				if(item==null) return;
				ItemStack item2=item.clone();
				item2.setAmount(1);
				if(ItemProvider.items.values().contains(item2)) {
					event.setCancelled(true);
					event.getWhoClicked().sendMessage("§6[§c凌域§6]§r 您不能用UTE物品合成其它物品！");
				}
			}
		}
	}
}
