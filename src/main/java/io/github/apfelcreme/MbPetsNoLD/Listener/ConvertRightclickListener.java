package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.*;
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
import java.util.HashSet;

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

    private HashSet<Player> registeredConverts = new HashSet<Player>();
    private HashMap<Player, BukkitTask> timers = new HashMap<Player, BukkitTask>();

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
        registeredConverts.add(player);
        BukkitTask task = MbPets.getInstance().getServer().getScheduler()
                .runTaskLater(MbPets.getInstance(), new Runnable() {
                    public void run() {
                        registeredConverts.remove(player);
                        MbPets.sendMessage(player, MbPetsConfig.getTextNode("info.petRightclickEnd"));
                    }

                }, 200L);

        if (timers.containsKey(player)) {
            timers.get(player).cancel();
        }
        MbPets.sendMessage(player, MbPetsConfig.getTextNode("info.petRightclick"));
        timers.put(player, task);
    }

    /**
     * converts an entity into a pet after the player entered /pet convert
     *
     * @param e
     */
    @EventHandler
    public void onPlayerRightClick(final PlayerInteractEntityEvent e) {
        MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
            @Override
            public void run() {

                if (registeredConverts.contains(e.getPlayer())) {
                    if (PetManager.getInstance().getPetByEntity(e.getRightClicked()) != null) {
                        // check whether the right-clicked entity isn't already a
                        // pet
                        MbPets.sendMessage(e.getPlayer(), MbPetsConfig.getTextNode("error.entityIsAlreadyAPet"));
                        registeredConverts.remove(e.getPlayer());
                        timers.get(e.getPlayer()).cancel();
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
                            registeredConverts.remove(e.getPlayer());
                            timers.get(e.getPlayer()).cancel();
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
                        registeredConverts.remove(e.getPlayer());
                        return;
                    }
                    if (MbPetsConfig.parseType(e.getRightClicked().getType().name()) == null) {
                        // allow only available pets for a convert
                        MbPets.sendMessage(e.getPlayer(), MbPetsConfig.getTextNode("help.TYPE"));
                        registeredConverts.remove(e.getPlayer());
                        timers.get(e.getPlayer()).cancel();
                        return;
                    }
                    PetConfiguration petConfiguration = new PetConfiguration(
                            e.getPlayer().getUniqueId(),
                            MbPetsConfig.parseType(e.getRightClicked().getType().name()),
                            PetConfiguration.ConfigurationType.CONVERSION);

                    petConfiguration.setName((e.getRightClicked()).getCustomName());
                    petConfiguration.setPrice(MbPetsConfig.getPetPrice(petConfiguration.getType()));
                    switch (petConfiguration.getType()) {
                        case HORSE:
                            petConfiguration.setBaby(!((Horse) e.getRightClicked()).isAdult());
                            petConfiguration.setHorseColor(((Horse) e.getRightClicked()).getColor());
                            petConfiguration.setHorseStyle(((Horse) e.getRightClicked()).getStyle());
                            break;
                        case PIG:
                            petConfiguration.setBaby(!((Pig) e.getRightClicked()).isAdult());
                            break;
                        case SHEEP:
                            petConfiguration.setBaby(!((Sheep) e.getRightClicked()).isAdult());
                            petConfiguration.setSheepColor(((Sheep) e.getRightClicked()).getColor());
                            break;
                        case WOLF:
                            petConfiguration.setBaby(!((Wolf) e.getRightClicked()).isAdult());
                            petConfiguration.setWolfColor(((Wolf) e.getRightClicked()).getCollarColor());
                            break;
                        case CHICKEN:
                            petConfiguration.setBaby(!((Chicken) e.getRightClicked()).isAdult());
                            break;
                        case COW:
                            petConfiguration.setBaby(!((Cow) e.getRightClicked()).isAdult());
                            break;
                        case MUSHROOM_COW:
                            petConfiguration.setBaby(!((MushroomCow) e.getRightClicked()).isAdult());
                            break;
                        case OCELOT:
                            petConfiguration.setBaby(!((Ocelot) e.getRightClicked()).isAdult());
                            petConfiguration.setOcelotType(((Ocelot) e.getRightClicked()).getCatType());
                            break;
                        case POLAR_BEAR:
                            petConfiguration.setBaby(!((PolarBear) e.getRightClicked()).isAdult());
                            break;
                        case BAT:
                            break;
                        case IRON_GOLEM:
                            break;
                        case RABBIT:
                            petConfiguration.setBaby(!((Rabbit) e.getRightClicked()).isAdult());
                            petConfiguration.setRabbitType(((Rabbit) e.getRightClicked()).getRabbitType());
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
                        case ENDERMAN:
                            break;
                        case MAGMA_CUBE:
                            petConfiguration.setSlimeSize(((MagmaCube) e.getRightClicked()).getSize());
                            break;
                        case SLIME:
                            petConfiguration.setSlimeSize(((Slime) e.getRightClicked()).getSize());
                            break;
                        default:
                            MbPets.sendMessage(e.getPlayer(), MbPetsConfig.getTextNode("help.TYPE"));
                            registeredConverts.remove(e.getPlayer());
                            timers.get(e.getPlayer()).cancel();
                            return;
                    }
//					if (((LivingEntity) e.getRightClicked()).getCustomName() != null) pet.setName(((LivingEntity) e.getRightClicked()).getCustomName());
//					pet.setOwner(e.getPlayer());
                    petConfiguration.setConvertedEntity(e.getRightClicked()); // for a later despawn
                    PetManager.getInstance().getConfigurations().put(e.getPlayer().getUniqueId(), petConfiguration);
                    MbPets.sendMessage(e.getPlayer(), petConfiguration.getPetDescription().getDescription());
                    registeredConverts.remove(e.getPlayer());
                    timers.get(e.getPlayer()).cancel();

                }
            }
        });

    }

}