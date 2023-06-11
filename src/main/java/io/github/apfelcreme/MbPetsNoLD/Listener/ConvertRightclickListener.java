package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.Interface.Dyeable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Sizeable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Styleable;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetConfiguration;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import io.papermc.paper.entity.CollarColorable;
import net.zaiyers.AnimalProtect.Protection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.material.Colorable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
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
public class ConvertRightclickListener implements Listener {

    private Map<UUID, BukkitTask> timers = new HashMap<UUID, BukkitTask>();

    /**
     * registers a convert-request for a given player
     *
     * @param player
     */
    public void addConvert(final Player player) {
        if (!player.hasPermission("MbPets.convert")) {
            MbPets.sendMessage(player, MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        BukkitTask task = MbPets.getInstance().getServer().getScheduler()
                .runTaskLater(MbPets.getInstance(), () -> {
                    timers.remove(player.getUniqueId());
                    MbPets.sendMessage(player, MbPetsConfig.getTextNode("info.petRightclickEnd"));
                }, 200L);

        stopTimer(player);
        MbPets.sendMessage(player, MbPetsConfig.getTextNode("info.petRightclick"));
        timers.put(player.getUniqueId(), task);
    }

    /**
     * converts an entity into a pet after the player entered /pet convert
     *
     * @param e
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerRightClick(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof LivingEntity && timers.containsKey(e.getPlayer().getUniqueId())) {
            MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), () -> {
                if (PetManager.getInstance().getPetByEntity(e.getRightClicked()) != null) {
                    // check whether the right-clicked entity isn't already a pet
                    MbPets.sendMessage(e.getPlayer(), MbPetsConfig.getTextNode("error.entityIsAlreadyAPet"));
                    stopTimer(e.getPlayer());
                    return;
                }
                if (MbPets.getInstance().getPluginAnimalProtect() != null) {
                    Protection protection = MbPets.getInstance().getPluginAnimalProtect().getProtection(e.getRightClicked().getUniqueId());
                    if (protection.getOwnerId() != null && !protection.getOwnerId().equals(e.getPlayer().getUniqueId())) {
                        // check whether the animal the player right-clicked is
                        // either
                        // unprotected or his own.
                        e.getPlayer().sendMessage(
                                MbPetsConfig.getTextNode("error.notYourPet"));
                        stopTimer(e.getPlayer());
                        return;
                    }
                }
                if (e.getRightClicked() instanceof Tameable
                        && ((Tameable) e.getRightClicked()).getOwner() != null
                        && !((Tameable) e.getRightClicked()).getOwner().equals(
                        e.getPlayer())) {
                    // User rightclicked a pet, that is an other players tamed
                    // animal
                    MbPets.sendMessage(e.getPlayer(), MbPetsConfig.getTextNode("error.notYourPet"));
                    stopTimer(e.getPlayer());
                    return;
                }
                PetType petType = MbPetsConfig.parseType(e.getRightClicked().getType().name());
                if (petType == null) {
                    // allow only available pets for a convert
                    MbPets.sendMessage(e.getPlayer(), MbPetsConfig.getTextNode("help.TYPE"));
                    stopTimer(e.getPlayer());
                    return;
                }
                if (!e.getPlayer().hasPermission("MbPets.pet." + petType.name().toLowerCase())) {
                    MbPets.sendMessage(e.getPlayer(), MbPetsConfig.getTextNode("error.noPermission"));
                    stopTimer(e.getPlayer());
                    return;
                }
                PetConfiguration petConfiguration = new PetConfiguration(
                        e.getPlayer().getUniqueId(),
                        petType,
                        PetConfiguration.ConfigurationType.CONVERSION);

                petConfiguration.setName((e.getRightClicked()).getCustomName());
                petConfiguration.setPrice(MbPetsConfig.getPetPrice(petConfiguration.getType()));

                // set the age
                if (io.github.apfelcreme.MbPetsNoLD.Interface.Ageable.class.isAssignableFrom(petConfiguration.getType().getPetClass())
                        && e.getRightClicked() instanceof Ageable ageable) {
                    petConfiguration.setBaby(!ageable.isAdult());
                }

                // set the colors
                if (Dyeable.class.isAssignableFrom(petConfiguration.getType().getPetClass())) {
                    if (e.getRightClicked() instanceof Colorable colorable && colorable.getColor() != null) {
                        petConfiguration.setColor(colorable.getColor().name());
                    } else if (e.getRightClicked() instanceof CollarColorable colorable) {
                        petConfiguration.setColor(colorable.getCollarColor().name());
                    } else if (e.getRightClicked() instanceof Horse horse) {
                        petConfiguration.setColor(horse.getColor().name());
                    } else if (e.getRightClicked() instanceof Parrot parrot) {
                        petConfiguration.setColor(parrot.getVariant().name());
                    } else if (e.getRightClicked() instanceof MushroomCow mushroomCow) {
                        petConfiguration.setColor(mushroomCow.getVariant().name());
                    }
                }

                // set the styles
                if (Styleable.class.isAssignableFrom(petConfiguration.getType().getPetClass())) {
                    if (e.getRightClicked() instanceof Horse horse) {
                        petConfiguration.setStyle(horse.getStyle().name());
                    } else if (e.getRightClicked() instanceof Cat cat) {
                        petConfiguration.setStyle(cat.getCatType().name());
                    } else if (e.getRightClicked() instanceof MushroomCow mushroomCow) {
                        petConfiguration.setStyle(mushroomCow.getVariant().name());
                    } else if (e.getRightClicked() instanceof Rabbit rabbit) {
                        petConfiguration.setStyle(rabbit.getRabbitType().name());
                    } else if (e.getRightClicked() instanceof Parrot parrot) {
                        petConfiguration.setStyle(parrot.getVariant().name());
                    } else if (e.getRightClicked() instanceof Fox fox) {
                        petConfiguration.setStyle(fox.getFoxType().name());
                    }
                }

                // set the size
                if (Sizeable.class.isAssignableFrom(petConfiguration.getType().getPetClass())) {
                    if (e.getRightClicked() instanceof Slime slime) {
                        petConfiguration.setSize(slime.getSize());
                    }
                }

                if (e.getRightClicked().getCustomName() != null) {
                    petConfiguration.setName(e.getRightClicked().getCustomName());
                }
                petConfiguration.setConvertedEntity(e.getRightClicked()); // for a later despawn
                PetManager.getInstance().getConfigurations().put(e.getPlayer().getUniqueId(), petConfiguration);
                MbPets.sendMessage(e.getPlayer(), petConfiguration.getPetDescription().getDescription());
                stopTimer(e.getPlayer());
            });
        }

    }

    private void stopTimer(Player player) {
        if (timers.containsKey(player.getUniqueId())) {
            timers.get(player.getUniqueId()).cancel();
            timers.remove(player.getUniqueId());
        }
    }

}
