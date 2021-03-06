package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetConfiguration;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import net.zaiyers.AnimalProtect.Protection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
                switch (petConfiguration.getType()) {
                    case HORSE:
                        petConfiguration.setBaby(!((Horse) e.getRightClicked()).isAdult());
                        petConfiguration.setHorseColor(((Horse) e.getRightClicked()).getColor());
                        petConfiguration.setHorseStyle(((Horse) e.getRightClicked()).getStyle());
                        break;
                    case SHEEP:
                        petConfiguration.setBaby(!((Sheep) e.getRightClicked()).isAdult());
                        petConfiguration.setSheepColor(((Sheep) e.getRightClicked()).getColor());
                        break;
                    case WOLF:
                        petConfiguration.setBaby(!((Wolf) e.getRightClicked()).isAdult());
                        petConfiguration.setWolfColor(((Wolf) e.getRightClicked()).getCollarColor());
                        break;
                    case CAT:
                        petConfiguration.setBaby(!((Cat) e.getRightClicked()).isAdult());
                        petConfiguration.setCatType(((Cat) e.getRightClicked()).getCatType());
                        break;
                    case PIG:
                    case CHICKEN:
                    case COW:
                    case MUSHROOM_COW:
                    case OCELOT:
                    case PANDA:
                    case POLAR_BEAR:
                        petConfiguration.setBaby(!((Ageable) e.getRightClicked()).isAdult());
                        break;
                    case RABBIT:
                        petConfiguration.setBaby(!((Rabbit) e.getRightClicked()).isAdult());
                        petConfiguration.setRabbitType(((Rabbit) e.getRightClicked()).getRabbitType());
                        break;
                    case PARROT:
                        petConfiguration.setParrotColor(((Parrot) e.getRightClicked()).getVariant());
                        break;
                    case FOX:
                        petConfiguration.setFoxType(((Fox) e.getRightClicked()).getFoxType());
                        petConfiguration.setBaby(!((Fox) e.getRightClicked()).isAdult());
                        break;
                    case SKELETON_HORSE:
                        petConfiguration.setType(PetType.SKELETON_HORSE);
                        petConfiguration.setBaby(!((Horse) e.getRightClicked()).isAdult());
                        break;
                    case UNDEAD_HORSE:
                        petConfiguration.setType(PetType.UNDEAD_HORSE);
                        petConfiguration.setBaby(!((Horse) e.getRightClicked()).isAdult());
                        break;
                    case DONKEY:
                        petConfiguration.setType(PetType.DONKEY);
                        petConfiguration.setBaby(!((Horse) e.getRightClicked()).isAdult());
                        break;
                    case MULE:
                        petConfiguration.setType(PetType.MULE);
                        petConfiguration.setBaby(!((Horse) e.getRightClicked()).isAdult());
                        break;
                    case MAGMA_CUBE:
                        petConfiguration.setSlimeSize(((MagmaCube) e.getRightClicked()).getSize());
                        break;
                    case SLIME:
                        petConfiguration.setSlimeSize(((Slime) e.getRightClicked()).getSize());
                        break;
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
