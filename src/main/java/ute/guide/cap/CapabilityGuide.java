package ute.guide.cap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import ute.Config;
import ute.UntilTheEnd;
import ute.api.PlayerApi;
import ute.api.event.cap.HumidityChangeEvent;
import ute.api.event.cap.SanityChangeEvent;
import ute.api.event.cap.TemperatureChangeEvent;
import ute.api.event.cap.TirednessChangeEvent;
import ute.internal.NPCChecker;
import ute.internal.ResidenceChecker;
import ute.nms.reflect.ActionBarManagerImpl;

public class CapabilityGuide implements Listener {

    public CapabilityGuide(){
        new StateGuider().runTaskTimer(UntilTheEnd.getInstance(),0L,10L);
        Bukkit.getPluginManager().registerEvents(this, UntilTheEnd.getInstance());
    }

    @EventHandler
    public void onSanityChange(SanityChangeEvent event){
        Player player=event.getPlayer();
        if(player.getGameMode()== GameMode.CREATIVE||player.getGameMode()== GameMode.SPECTATOR) return;
        if(event.getChange()==0.0) return;
        switch(event.getCause()){
            case INVENTORYITEM:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l注意！背包内的物品会影响您的理智值！");
                break;
            case INVENTORYCLOTHES:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l注意！您穿着的饰品和衣物会影响您的理智值！");
                break;
            case CREATUREAURA:
                if(Math.random()<=0.2)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l注意！身边的生物会影响您的理智值！");
                break;
            case PLAYER:
                if(Math.random()<=0.1)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l周围有玩家让你感觉更安全");
                break;
            case MONSTER:
                if(Math.random()<=0.1)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l周围有怪物让你惊慌");
                break;
            case HUMIDITY:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l太潮湿了，心情变差");
                break;
            case EVENING:
                if(Math.random()<=0.02)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l上半夜会较慢降低你的理智值");
                break;
            case NIGHT:
                if(Math.random()<=0.02)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l下半夜会较快降低你的理智值");
                break;
            case USEWAND:
                if(Math.random()<=1.0)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l使用魔杖，您消耗了一些精力");
                break;
            case FURROLL:
                if(Math.random()<=0.3)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l安睡让你更有精神");
                break;
            case STRAWROLL:
                if(Math.random()<=0.3)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l安睡让你更有精神");
                break;
            case SIESTALEANTO:
                if(Math.random()<=0.3)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l安睡让你更有精神");
                break;
            case DARKWARN:
                if(Math.random()<=1.0)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l黑暗中的未知让你害怕");
                break;
            case DARKATTACK:
                if(Math.random()<=1.0)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l是什么在偷袭你？");
                break;
            case FOOD:
                if(Math.random()<=1.0)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l食物好难吃");
                break;
            case WILLOW:
                if(Math.random()<=0.5)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l微洛自带的火属性影响了你的理智值");
                break;
        }
    }
    @EventHandler
    public void onTemperatureChange(TemperatureChangeEvent event){
        Player player=event.getPlayer();
        if(player.getGameMode()== GameMode.CREATIVE||player.getGameMode()== GameMode.SPECTATOR) return;
        if(event.getChange()==0.0) return;
        switch(event.getCause()){
            case WARMSTONE:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l身上的石头向你提供温度");
                break;
            case ENVIRONMENT:
                if(Math.random()<=0.01)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你的体表温度正随着环境而改变");
                break;
            case HUMIDITY:
                if(Math.random()<=0.2)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你身上很潮湿，体温下降");
                break;
            case CLOTHES:
                if(Math.random()<=0.03)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你穿着的衣物改变了你的体表温度");
                break;
            case ITEMS:
                if(Math.random()<=0.5)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你使用的物品改变了你的体表温度");
                break;
        }
    }
    @EventHandler
    public void onHumidityChange(HumidityChangeEvent event){
        Player player=event.getPlayer();
        if(player.getGameMode()== GameMode.CREATIVE||player.getGameMode()== GameMode.SPECTATOR) return;
        if(event.getChange()==0.0) return;
        switch(event.getCause()){
            case WATER:
                if(Math.random()<=0.1)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你未穿戴任何防水设备，在水中浸湿了");
                break;
            case RAIN:
                if(Math.random()<=0.1)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你在雨中，感觉很不舒服");
                break;
            case VAPOUR:
                if(Math.random()<=0.005)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你身上的水逐渐蒸发干燥");
                break;
            case ITEMS:
                if(Math.random()<=0.5)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你使用的物品改变了你的湿度");
                break;
        }
    }
    @EventHandler
    public void onTirednessChange(TirednessChangeEvent event){
        Player player=event.getPlayer();
        if(player.getGameMode()== GameMode.CREATIVE||player.getGameMode()== GameMode.SPECTATOR) return;
        if(event.getChange()==0.0) return;
        switch(event.getCause()){
            case TELEPORT:
                if(Math.random()<=0.5)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l传送过后，你觉得整个人像是被拉扯了一下，好累");
                break;
            case TAB:
                if(Math.random()<=0.3)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l搜索可输入的字符花了你一些精力");
                break;
            case DAMAGE:
                if(Math.random()<=0.3)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你用力使用武器，使你感到僵直");
                break;
            case BE_DAMAGED:
                if(Math.random()<=0.3)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你被攻击了，伤口使得你行动不便");
                break;
            case ENCHANT:
                if(Math.random()<=0.3)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你冥思附魔文字，消耗了大量精力");
                break;
            case BREAK:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你破坏了一个方块，感到有些劳累");
                break;
            case TALK:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你大声说话，发觉喉咙有点哑");
                break;
            case SPRINT:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你全力跑步，上气不接下气");
                break;
            case DRIVE:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你坐在椅子上，感觉不错");
                break;
            case SLEEP:
                if(Math.random()<=0.2)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你小憩着，舒服极了");
                break;
            case BLOCK:
                if(Math.random()<=0.2)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l长时间挥动工具挖掘，让你感到枯燥乏味");
                break;
            case GLIDE:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你紧张地操纵滑翔翼");
                break;
            case MOVE:
                if(Math.random()<=0.03)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你迈出了无力的步伐");
                break;
            case NOTMOVE:
                if(Math.random()<=0.03)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l你站定休息，给了你感到重新行动的动力");
                break;
            case ITEMWEIGHT:
                if(Math.random()<=0.05)
                    ActionBarManagerImpl.sendActionBar(player,"§6§l背包里厚重的物品压得你不堪重负");
                break;
        }
    }
    public class StateGuider extends BukkitRunnable{

        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player) || ResidenceChecker.isProtected(player.getLocation())) continue;
                    if(player.getGameMode()== GameMode.CREATIVE||player.getGameMode()== GameMode.SPECTATOR) continue;
                    if (PlayerApi.SanityOperations.getSanity(player)<=120){
                        if(Math.random()<=0.05)
                            ActionBarManagerImpl.sendActionBar(player,"§6§l你可以使用待在宠物或玩家身边、使用稻草卷、小木鹏来提升理智值");
                    }
                    if (PlayerApi.TemperatureOperations.getTemperature(player)<=20){
                        if(Math.random()<=0.02)
                            ActionBarManagerImpl.sendActionBar(player,"§6§l你可以使用暖石（热）、待在热源旁边为自己升温");
                    }
                    if (PlayerApi.TemperatureOperations.getTemperature(player)>=45){
                        if(Math.random()<=0.02)
                            ActionBarManagerImpl.sendActionBar(player,"§6§l你可以使用暖石（冷）、待在冷源旁边为自己降温");
                    }
                    if (PlayerApi.HumidityOperations.getHumidity(player)>=20){
                        if(Math.random()<=0.05)
                            ActionBarManagerImpl.sendActionBar(player,"§6§l你可以待在遮蔽物下避雨，或脱离水体，或制作伞和防水服来防止湿度继续上升");
                    }
                    if (PlayerApi.TirednessOperations.getTiredness(player)>=30){
                        if(Math.random()<=0.05)
                            ActionBarManagerImpl.sendActionBar(player,"§6§l你可以慢慢走、停下、坐下、睡一会来提升疲劳值");
                    }
                    if(Math.random()<=0.01)
                        ActionBarManagerImpl.sendActionBar(player,"§6§l你可以使用/ute guide获取合成指南~");
                }
            }
        }
    }
}
