package io.github.apfelcreme.MbPetsNoLD.Pet;

import java.sql.Connection;
import java.sql.SQLException;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Tasks.FollowTask;
import io.github.apfelcreme.MbPetsNoLD.Tasks.ParticleTask;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.sql.PreparedStatement;
import java.text.DecimalFormat;
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
public abstract class Pet {

    private UUID owner = null;
    private String name = null;
    private PetType type = null;
    private Double price = 0.0;
    private LivingEntity entity = null;
    private Integer number = null;
    private Double speed = null;
    private Integer exp = null;
    private Entity target = null;
    private PetLevel level = null;

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
    public Double getPrice() {
        return price;
    }

    /**
     * sets the price
     *
     * @param price the price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * returns the entity object
     *
     * @return the entity object
     */
    public LivingEntity getEntity() {
        return entity;
    }

    /**
     * sets the entity object
     *
     * @param entity the entity object
     */
    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    /**
     * returns the number
     *
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * sets the entity number
     *
     * @param number the number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * returns the speed
     *
     * @return the speed
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * sets the speed
     *
     * @param speed the speed
     */
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    /**
     * returns the exp
     *
     * @return the exp
     */
    public Integer getExp() {
        return exp;
    }

    /**
     * sets the exp
     *
     * @param exp the exp
     */
    public void setExp(Integer exp) {
        this.exp = exp;
        this.level = PetLevel.from(exp);
    }

    /**
     * gets the target
     *
     * @return the target
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * sets the target
     *
     * @param target the target
     */
    public void setTarget(Entity target) {
        this.target = target;
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

        //is there a cooldown on the call?
        if (PetManager.getInstance().getCooldowns().containsKey(owner)
                && (System.currentTimeMillis() < PetManager.getInstance().getCooldowns().get(owner) + MbPetsConfig.getPetDeathCooldown())) {
            MbPets.sendMessage(MbPets.getInstance().getServer().getPlayer(owner), MbPetsConfig.getTextNode("error.deathCooldown")
                    .replace("{0}", new DecimalFormat("0").format((
                            PetManager.getInstance().getCooldowns().get(owner) + MbPetsConfig.getPetDeathCooldown()
                                    - System.currentTimeMillis()) / 1000)));
            return;
        }

        //check for gamemode and worldguard flags
        if (MbPets.getInstance().getServer().getPlayer(owner).getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            MbPets.sendMessage(MbPets.getInstance().getServer().getPlayer(owner), MbPetsConfig.getTextNode("error.gamemodeCreative"));
            return;
        } else {
            if (MbPets.getInstance().getPluginWorldGuard() != null) {
                for (ProtectedRegion region : WorldGuardPlugin.inst()
                        .getRegionManager(MbPets.getInstance().getServer().getPlayer(owner).getWorld())
                        .getApplicableRegions(MbPets.getInstance().getServer().getPlayer(owner).getLocation())) {
                    if (region.getFlags() != null &&
                            region.getFlag(DefaultFlag.MOB_SPAWNING) != null &&
                            region.getFlags().get(DefaultFlag.MOB_SPAWNING).equals(StateFlag.State.DENY)) {
                        MbPets.sendMessage(MbPets.getInstance().getServer().getPlayer(owner),
                                MbPetsConfig.getTextNode("error.inFlaggedRegion"));
                        return;
                    }
                    if (region.getFlags() != null && region.getFlags().get(DefaultFlag.BLOCKED_CMDS) != null
                            && region.getFlags().get(DefaultFlag.BLOCKED_CMDS).toString().contains("/pet")) {
                        MbPets.sendMessage(MbPets.getInstance().getServer().getPlayer(owner),
                                MbPetsConfig.getTextNode("error.inFlaggedRegion"));
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
        this.target = MbPets.getInstance().getServer().getPlayer(owner);
        PetManager.getInstance().getPets().put(owner, this);
        MbPets.getInstance().getServer().getScheduler().runTask(MbPets.getInstance(), () -> {
            entity = (LivingEntity) MbPets.getInstance().getServer().getPlayer(owner).getWorld()
                    .spawnEntity(MbPets.getInstance().getServer().getPlayer(owner).getLocation(), type.getEntityType());
            PetManager.getInstance().getPetEntities().put(entity.getUniqueId(), this);
            if (!FollowTask.isActive()) {
                FollowTask.create();
            }
            if (!ParticleTask.isActive() && (getLevel().getEffect() != null)) {
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
            if (petConfiguration.getNumber() != null && petConfiguration.getNumber().equals(number)) {
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
    public abstract void applyAttributes();

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
