package io.github.apfelcreme.MbPetsNoLD.Tasks;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.PathEntity;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftHumanEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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
public class FollowTask {

    public static int taskId = -1;

    /**
     * runs the follow task for every currently spawned pet
     */
    public static void create() {
        taskId = MbPets.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(MbPets.getInstance(), new Runnable() {
            @Override
            public void run() {

                for (Pet pet : PetManager.getInstance().getPets().values()) {
                    Entity entity = pet.getEntity();
                    if (pet.getTarget() != null) {
                        if (entity.getWorld().equals(MbPets.getInstance().getServer().getPlayer(pet.getOwner()).getWorld())) {
                            Object handle = ((CraftEntity) entity).getHandle();
                            PathEntity path;

                            // create a path the pet is going to follow
                            if (pet.getTarget() instanceof Player) {
                                // let the pet stand next to the owner, otherwise its quite annoying as the pet tries to
                                // stand at your exact location and bumps into you continuously
                                float radX = 2.2f;
                                float radZ = 2.2f;
                                switch (getDirection(pet.getTarget())) {
                                    case NORTH:
                                        radX = 2.2f;
                                        radZ = -2.2f;
                                        break;
                                    case EAST:
                                        radX = 2.2f;
                                        radZ = 2.2f;
                                        break;
                                    case SOUTH:
                                        radX = -2.2f;
                                        radZ = 2.2f;
                                        break;
                                    case WEST:
                                        radX = -2.2f;
                                        radZ = -2.2f;
                                        break;
                                }

                                path = ((EntityInsentient) handle).getNavigation().a(
                                        pet.getTarget().getLocation().getX() + radX,
                                        pet.getTarget().getLocation().getY(),
                                        pet.getTarget().getLocation().getZ() + radZ);
                            } else {
                                // let the pet walk directly to the entity its supposed to kill
                                path = ((EntityInsentient) handle).getNavigation().a(
                                        pet.getTarget().getLocation().getX(),
                                        pet.getTarget().getLocation().getY(),
                                        pet.getTarget().getLocation().getZ());
                            }

                            if (path != null) {
//                               // let the pet walk the path
                                ((EntityInsentient) handle).getNavigation().a(path, pet.getSpeed());

                                // if there is an entity nearby the owner has attacked, let the pet attack that target as well
                                if (!(pet.getTarget() instanceof Player)   //target isnt a player
                                        && (pet.getEntity().getLocation().distance(pet.getTarget().getLocation()) < 3.5) //target is nearby
                                        && (PetManager.getInstance().getPetByEntity(pet.getTarget()) == null)) {  //target isnt a pet

                                    if (MbPets.getInstance().getPluginAnimalProtect() == null // is the Plugin "AnimalProtect" activated? ?
                                            || (MbPets.getInstance().getPluginAnimalProtect() != null && !MbPets.getInstance().getPluginAnimalProtect().hasOwner(pet.getTarget().getUniqueId()))) { // if it is: is the target protected?

                                        // launch the target into the air and do some damage depending on the pets attack strength and active modifiers
                                        pet.getTarget().setVelocity(new Vector(0, 0.5, 0));
                                        DamageSource reason = DamageSource.playerAttack(((CraftHumanEntity) MbPets.getInstance().getServer().getPlayer(pet.getOwner())).getHandle());
                                        net.minecraft.server.v1_11_R1.Entity damagedEntity = ((CraftEntity) pet.getTarget()).getHandle();
                                        damagedEntity.damageEntity(reason, (float) (MbPetsConfig.getPetAttackStrength(pet.getType()) * pet.getLevel().getAttackStrengthModifier()));
                                    }
                                }
                            }

                            // distance to the owner > 15 ? teleport
                            if (MbPets.getInstance().getServer().getPlayer(pet.getOwner()).getWorld().equals(pet.getEntity().getWorld())) {
                                if (MbPets.getInstance().getServer().getPlayer(pet.getOwner()).getLocation()
                                        .distance(entity.getLocation()) > 15) {
                                    entity.teleport(MbPets.getInstance().getServer().getPlayer(pet.getOwner()).getLocation());
                                }
                            } else {
                                pet.uncall();
                            }
                        }
                    }
                }
            }
        }, 0, MbPetsConfig.getFollowTaskDelay());
    }


    /**
     * kills the task
     */
    public static void kill() {
        MbPets.getInstance().getServer().getScheduler().cancelTask(taskId);
        taskId = -1;
    }

    /**
     * is the task running at the moment?
     *
     * @return true or false
     */
    public static boolean isActive() {
        return taskId != -1;
    }

    /**
     * returns the direction an entity is looking at
     *
     * @param entity the target entity
     * @return a direction
     */
    public static BlockFace getDirection(Entity entity) {
        float yaw = entity.getLocation().getYaw();
        if (yaw < 0) {
            yaw += 360;
        }
        yaw %= 360;

        //south = 315° - 45°
        //west = 45° - 135°
        //north = 135° - 225°
        //east = 225° - 315°
        if (yaw < 45) yaw = 315;

        int i = (int) ((yaw - 45) / 90);
        if (i == 0) return BlockFace.WEST;
        else if (i == 1) return BlockFace.NORTH;
        else if (i == 2) return BlockFace.EAST;
        else if (i == 3) return BlockFace.SOUTH;
        else return BlockFace.WEST;
    }

}