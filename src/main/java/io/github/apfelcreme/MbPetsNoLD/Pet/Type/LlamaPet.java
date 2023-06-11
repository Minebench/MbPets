package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Dyeable;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

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
public class LlamaPet extends Pet<Llama> implements Dyeable<Llama.Color>, Ageable {

    private Boolean isBaby = null;
    private Llama.Color color = null;

    public LlamaPet(UUID owner, Integer number) {
        super(owner, PetType.LLAMA, number);
    }


    /**
     * is the pet a baby
     *
     * @return true or false
     */
    @Override
    public Boolean isBaby() {
        return isBaby;
    }

    /**
     * set the "age"
     *
     * @param isBaby true or false
     */
    @Override
    public void setBaby(Boolean isBaby) {
        this.isBaby = isBaby;
    }


    /**
     * returns the llama color
     *
     * @return the llama color
     */
    @Override
    public Llama.Color getColor() {
        return color;
    }

    /**
     * sets the llama color
     *
     * @param color the color
     */
    @Override
    public void setColor(Llama.Color color) {
        this.color = color;
    }

    @Override
    public Llama.Color parseColor(String color) {
        return MbPetsConfig.parseLlamaColor(color);
    }

    /**
     * applies all attributes to the entity
     */
    @Override
    public void applyAttributes() {
        super.applyAttributes();
        getEntity().setColor(color);
        getEntity().setOwner(MbPets.getInstance().getServer().getPlayer(getOwner()));
        getEntity().setAgeLock(true);

        if (isBaby) {
            getEntity().setBaby();
        } else {
            getEntity().setAdult();
        }
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.LLAMA_ATTACK_WOLF);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.LLAMA_FOLLOW_CARAVAN);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.TRADER_LLAMA_DEFEND_WANDERING_TRADER);
    }

    /**
     * remove the carpet from the llamas inventory
     */
    @Override
    public void uncall() {
        Player player = MbPets.getInstance().getServer().getPlayer(getOwner());
        if (player != null) {
            if (getEntity().getInventory().getDecor() != null) {
                if (player.getInventory().firstEmpty() >= 0) {
                    player.getInventory().addItem(getEntity().getInventory().getDecor());
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(),
                            getEntity().getInventory().getDecor());
                }
            }
        }
        super.uncall();
    }
    
    @Override
    public boolean onRightClick(Player player, PlayerInteractEntityEvent event) {
        boolean r = super.onRightClick(player, event);
        if (player.getInventory().getItemInMainHand().getType() == Material.CHEST) {
            player.getInventory().addItem(new ItemStack(Material.CHEST, 1));
            player.getInventory().removeItem(new ItemStack(Material.CHEST, 1));
            event.setCancelled(true);
        }
        return r;
    }
}
