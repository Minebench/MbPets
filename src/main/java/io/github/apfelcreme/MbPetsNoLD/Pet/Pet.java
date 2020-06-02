package io.github.apfelcreme.MbPetsNoLD.Pet;

import java.sql.Connection;
import java.sql.SQLException;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Tasks.FollowTask;
import io.github.apfelcreme.MbPetsNoLD.Tasks.ParticleTask;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
 * <p>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme
 */
public class Pet<T extends Mob> {

    private UUID owner;
    private String name = null;
    private PetType type;
    private double price;
    private T entity = null;
    private int number ;
    private double speed;
    private int exp;
    private LivingEntity target = null;
    private PetLevel level = null;
    private EntityTargetEvent.TargetReason targetReason = EntityTargetEvent.TargetReason.UNKNOWN;
    
    public Pet(UUID owner, PetType type, Integer number) {
        this.owner = owner;
        this.type = type;
        this.number = number;
        this.price = MbPetsConfig.getPetPrice(type);
        this.speed = MbPetsConfig.getPetSpeed(type);
    }

    /**
     * returns the owner uuid
     *
     * @return the owner uuid
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * sets the owner uuid
     *
     * @param owner the owner uuid
     */
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    /**
     * returns the name
     *
     * @return the pet name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = stripName(name);
    }

    /**
     * returns the pet type
     *
     * @return the pet type
     */
    public PetType getType() {
        return type;
    }

    /**
     * sets the pet type
     *
     * @param type the pet type
     */
    public void setType(PetType type) {
        this.type = type;
    }

    /**
     * returns the price
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * sets the price
     *
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * returns the entity object
     *
     * @return the entity object
     */
    public T getEntity() {
        return entity;
    }

    /**
     * sets the entity object
     *
     * @param entity the entity object
     */
    public void setEntity(T entity) {
        this.entity = entity;
    }

    /**
     * returns the number
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * sets the entity number
     *
     * @param number the number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * returns the speed
     *
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * sets the speed
     *
     * @param speed the speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * returns the exp
     *
     * @return the exp
     */
    public int getExp() {
        return exp;
    }

    /**
     * sets the exp
     *
     * @param exp the exp
     */
    public void setExp(int exp) {
        this.exp = exp;
        this.level = PetLevel.from(exp);
    }

    /**
     * gets the target
     *
     * @return the target
     */
    public LivingEntity getTarget() {
        return target;
    }

    /**
     * sets the target
     *
     * @param target the target
     */
    public void setTarget(LivingEntity target, EntityTargetEvent.TargetReason reason) {
        this.target = target;
        this.targetReason = reason;
        entity.setTarget(null);
    }
    
    /**
     * Get the reason for the current target
     * @return  The TargetReason
     */
    public EntityTargetEvent.TargetReason getTargetReason() {
        return targetReason;
    }

    /**
     * returns the pet level
     *
     * @return the pet level
     */
    public PetLevel getLevel() {
        return level;
    }

    /**
     * replaces chat color codes like "&6" and replaces it with "ChatColor.GOLD"
     *
     * @param oldName the name
     * @return the new name
     */
    public static String stripName(String oldName) {
        if (oldName == null) {
            return null;
        }
        String newName = oldName;
        newName = ChatColor.translateAlternateColorCodes('&', newName);
        return newName;
    }

    /**
     * calls the pet
     */
    public void call() {
        //is there a pet currently spawned by this player?
        Pet oldPet = PetManager.getInstance().getPets().get(owner);
        if (oldPet != null) {
            oldPet.uncall();
        }
    
        Player player = MbPets.getInstance().getServer().getPlayer(owner);
        if (player == null) {
            return;
        }
        if (!player.hasPermission("MbPets.pet." + type.name().toLowerCase())) {
            MbPets.sendMessage(player, MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        //is there a cooldown on the call?
        if (!player.hasPermission("MbPets.bypass.cooldown")
                && PetManager.getInstance().getCooldowns().containsKey(owner)
                && (System.currentTimeMillis() < PetManager.getInstance().getCooldowns().get(owner) + MbPetsConfig.getPetDeathCooldown())) {
            MbPets.sendMessage(owner, MbPetsConfig.getTextNode("error.deathCooldown")
                    .replace("{0}", new DecimalFormat("0").format((
                            PetManager.getInstance().getCooldowns().get(owner) + MbPetsConfig.getPetDeathCooldown()
                                    - System.currentTimeMillis()) / 1000)));
            return;
        }
        //check for gamemode and worldguard flags
        if (player.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            MbPets.sendMessage(player, MbPetsConfig.getTextNode("error.gamemodeCreative"));
            return;
        } else {
            if (MbPets.getInstance().isWorldGuardEnabled()) {
                LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);
                RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(lp.getWorld());
                if (rm != null) {
                    BlockVector3 vector = lp.getLocation().toVector().toBlockPoint();
                    boolean mobSpawning = rm.getApplicableRegions(vector).queryState(lp, Flags.MOB_SPAWNING) != StateFlag.State.DENY;
                    if (!mobSpawning) {
                        MbPets.sendMessage(player, MbPetsConfig.getTextNode("error.inFlaggedRegion"));
                        return;
                    }

                    Set<String> blockedCommands = rm.getApplicableRegions(vector).queryValue(lp, Flags.BLOCKED_CMDS);
                    if (blockedCommands != null && blockedCommands.contains("/pet")) {
                        MbPets.sendMessage(player, MbPetsConfig.getTextNode("error.inFlaggedRegion"));
                        return;
                    }

                    Set<String> allowedCommands = rm.getApplicableRegions(vector).queryValue(lp, Flags.ALLOWED_CMDS);
                    if (allowedCommands != null && !allowedCommands.contains("/pet")) {
                        MbPets.sendMessage(player, MbPetsConfig.getTextNode("error.inFlaggedRegion"));
                        return;
                    }
                }
            }
        }
        spawn();
    }

    /**
     * uncalls the pet
     */
    public void uncall() {
        if (entity != null) {
            PetManager.getInstance().getPets().remove(owner);
            PetManager.getInstance().getPetEntities().remove(entity.getUniqueId());
            entity.remove();
        }

        //Was the pet that was unspawned the last pet? if yes, kill the tasks
        if (PetManager.getInstance().getPets().size() == 0) {
            ParticleTask.kill();
            FollowTask.kill();
        }
    }

    /**
     * spawns the pet
     */
    private void spawn() {
        target = MbPets.getInstance().getServer().getPlayer(owner);
        PetManager.getInstance().getPets().put(owner, this);
        MbPets.getInstance().getServer().getScheduler().runTask(MbPets.getInstance(), () -> {
            entity = (T) target.getWorld().spawnEntity(target.getLocation(), type.getEntityType());
            PetManager.getInstance().getPetEntities().put(entity.getUniqueId(), this);
            if (!FollowTask.isActive()) {
                FollowTask.create();
            }
            if (!ParticleTask.isActive() && (getLevel().getParticle() != null)) {
                ParticleTask.create();
            }
            applyAttributes();
        });
    }

    /**
     * removes the pet from the database
     */
    public void delete() {
        PetConfiguration petConfiguration = PetManager.getInstance().getConfigurations().get(owner);
        if (petConfiguration != null) {
            if (petConfiguration.getNumber() > 0 && petConfiguration.getNumber() == number) {
                PetManager.getInstance().getConfigurations().remove(owner);
            }
        }
        MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), () -> {
            try (Connection connection = MbPets.getInstance().getDatabaseConnector().getConnection();
                 PreparedStatement statement = connection
                         .prepareStatement("DELETE from MbPets_Pet WHERE playerid = (Select playerid from MbPets_Player where uuid=?) AND number = ?")){
                statement.setString(1, owner
                        .toString());
                statement.setInt(2, number);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Called when a pet is right clicked by a player
     * @param player    The player that clicked the pet
     * @param event     The event of the click
     * @return          Whether this should actually happen or not
     */
    public boolean onRightClick(Player player, PlayerInteractEntityEvent event) {
        return true;
    }
    
    /**
     * Called when a player specifies a target for this entity
     * @param target    The target entity
     * @param reason    The reason why this target happened
     * @param event     The event that triggered this, null if it wasn't an event
     */
    public void onSpecifyTarget(LivingEntity target, EntityTargetEvent.TargetReason reason, Event event) {
        setTarget(target, reason);
        setSpeed(MbPetsConfig.getEnhancedPetSpeed(getType()));
        getEntity().getWorld().playSound(getEntity().getLocation(), MbPetsConfig.getPetSound(getType()), 5, 1);
    }
    
    /**
     * Called when an entity teleports for some reason
     * @param event The teleport event
     */
    public void onTeleport(EntityTeleportEvent event) {}
    
    /**
     * Called when the pet is damaged
     * @param event The damage event
     * @return      Whether this should actually happen or not
     */
    public boolean onDamage(EntityDamageEvent event) {
        // cancels some damage events to make pets not as vulnerable to the environment
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL
                || event.getCause() == EntityDamageEvent.DamageCause.LAVA
                || event.getCause() == EntityDamageEvent.DamageCause.DROWNING
                || event.getCause() == EntityDamageEvent.DamageCause.CRAMMING) {
            return false;
        }
        event.setDamage(event.getDamage() * getLevel().getReceivedDamageModifier());
        return true;
    }

    /**
     * Called when this pet attacks something
     * @return Whether or not the attack should happen
     */
    public boolean onAttack() {
        return true;
    }
    
    /**
     * Called when this pet kills an entity
     * @param killed    The entity killed
     * @param event     The event that triggered it
     */
    public void onKill(LivingEntity killed, EntityDeathEvent event) {
        if (killed != null) {
            addExp(MbPetsConfig.getTargetExpReward(killed.getType()));
        }
    }
    
    /**
     * Called when this pet dies
     * @param event The death event
     */
    public void onDeath(EntityDeathEvent event) {}

    /**
     * removes the pet from the db and returns a specific percentage of the price
     */
    public Double sell() {
        Double price = MbPetsConfig.getPetPrice(type) * MbPets.getInstance().getConfig().getDouble("prices.SELL", 0.75);
        EconomyResponse response = MbPets.getInstance().getEconomy().depositPlayer(MbPets.getInstance().getServer().getPlayer(owner), price);
        if (response.transactionSuccess()) {
            this.delete();
            return price;
        }
        return -1.0;
    }

    /**
     * adds exp
     *
     * @param exp the amount of exp
     */
    public void addExp(final int exp) {
        setExp(getExp() + exp);
        MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), () -> {
            try (Connection connection = MbPets.getInstance().getDatabaseConnector().getConnection();
                 PreparedStatement statement = connection
                         .prepareStatement("UPDATE MbPets_Pet SET exp = exp + ? WHERE playerid = (Select playerid from MbPets_Player where uuid=?) AND number = ?")){
                statement.setInt(1, exp);
                statement.setString(2, owner.toString());
                statement.setInt(3, number);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * returns the string that is displayed when you rightclick a pet
     *
     * @return the string that is displayed when you rightclick a pet
     */
    public String getRightClickName() {
        double healthRatio = 100 / getEntity().getMaxHealth() * getEntity().getHealth();
        ChatColor color;
        if (healthRatio < 33.3) {
            color = ChatColor.RED;
        } else if (healthRatio < 66.6) {
            color = ChatColor.YELLOW;
        } else {
            color = ChatColor.GREEN;
        }
        return MbPetsConfig.getTextNode("info.rightclickName")
                .replace("{0}", MbPets.getInstance().getServer().getPlayer(owner).getName())
                .replace("{1}", color.toString())
                .replace("{2}", new DecimalFormat("0.0").format(getEntity().getHealth()))
                .replace("{3}", new DecimalFormat("0.0").format(getEntity().getMaxHealth()))
                .replace("{4}", Integer.toString(exp))
                .replace("{5}", Integer.toString(PetLevel.fromLevel(level.getLevel() + 1).getExpThreshold()));
    }

    /**
     * returns a description object
     *
     * @return a description object containing all information about this pet
     */
    public PetDescription getPetDescription() {
        return new PetDescription(this);
    }

    /**
     * applies all attributes to the entity
     */
    public void applyAttributes() {
        getEntity().setCustomName(getName());
        getEntity().setCustomNameVisible(true);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.NEAREST_ATTACKABLE_TARGET);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.EAT_TILE);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.USE_ITEM);
        if (getEntity() instanceof Creature) {
            Bukkit.getMobGoals().removeGoal((Creature) getEntity(), VanillaGoal.TEMPT);
            Bukkit.getMobGoals().removeGoal((Creature) getEntity(), VanillaGoal.REMOVE_BLOCK);
            Bukkit.getMobGoals().removeGoal((Creature) getEntity(), VanillaGoal.RESTRICT_SUN);
            Bukkit.getMobGoals().removeGoal((Creature) getEntity(), VanillaGoal.STROLL_VILLAGE);
            Bukkit.getMobGoals().removeGoal((Creature) getEntity(), VanillaGoal.NEAREST_VILLAGE);
        }
    }
    
    /**
     * Return whether or not this pet can navigate with a custom PathFinder
     * @return
     */
    public boolean canNavigate() {
        return true;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "owner=" + owner +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", entity=" + entity +
                ", number=" + number +
                ", speed=" + speed +
                ", exp=" + exp +
                ", target=" + target +
                ", level=" + level +
                '}';
    }
}
