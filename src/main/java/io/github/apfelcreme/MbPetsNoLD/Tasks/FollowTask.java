package io.github.apfelcreme.MbPetsNoLD.Tasks;

import com.destroystokyo.paper.entity.Pathfinder;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.event.entity.EntityTargetEvent;
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

    private static int taskId = -1;

    /**
     * runs the follow task for every currently spawned pet
     */
    public static void create() {
        taskId = MbPets.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(MbPets.getInstance(), () -> {
            for (Pet pet : PetManager.getInstance().getPets().values()) {
                Mob entity = pet.getEntity();
                Player owner = MbPets.getInstance().getServer().getPlayer(pet.getOwner());
                if (owner != null && entity.getWorld().equals(owner.getWorld())) {
                    Pathfinder pathfinder = entity.getPathfinder();
                    //EntityInsentient handle = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();

                    if (pet.getTarget() == null || pet.getTarget().isDead()) {
                        // if target is dead make it return to the owner
                        pet.showParticles();
                        pet.setTarget(owner, EntityTargetEvent.TargetReason.TARGET_DIED);
                    }

                    double distance = owner.getLocation().distanceSquared(entity.getLocation());
                    if (distance > 24 * 24) { // distance to the owner > 24 ? teleport
                        pet.showParticles();
                        entity.teleport(getLocationNextTo(owner, 2.2));
                    } else if (distance > 16 * 16) { // distance to the owner > 16 ? set target to owner
                        pet.showParticles();
                        pet.setTarget(owner, EntityTargetEvent.TargetReason.FORGOT_TARGET);
                    }
                    
                    if (entity.getLocation().getBlock().getType().isSolid()) {
                        entity.setGlowing(true);
                    } else if (entity.isGlowing()) {
                        entity.setGlowing(false);
                    }

                    // create a path the pet is going to follow
                    if (pet.getTarget() == owner) {
                        if (distance > 3 * 3) { // only try to navigate to player if it isn't already next to it

                            // Set the speed back to normal when targeting the owner
                            pet.setSpeed(MbPetsConfig.getPetSpeed(pet.getType()));

                            // let the pet stand next to the owner, otherwise its quite annoying as the pet tries to
                            // stand at your exact location and bumps into you continuously
                            if (!pathfinder.moveTo(getLocationNextTo(owner, 2.2), pet.getSpeed())) {
                                fallbackMove(entity, owner, 1.5, pet.getSpeed());
                            }
                        }
                    } else {
                        // let the pet walk directly to the entity its supposed to kill
                        if (pathfinder.moveTo(pet.getTarget(), pet.getSpeed())) {
                            fallbackMove(entity, owner, 0.5, pet.getSpeed());
                        }

                        // if there is an entity nearby the owner has attacked, let the pet attack that target as well
                        if ((!(pet.getTarget() instanceof Player) || pet.getTarget().getWorld().getPVP()) // target isn't a player and pvp isn't enabled
                                && pet.getEntity().getLocation().distanceSquared(pet.getTarget().getLocation()) < 3.5 * 3.5 // target is nearby
                                && PetManager.getInstance().getPetByEntity(pet.getTarget()) == null  // target isn't a pet
                                && (MbPets.getInstance().getPluginAnimalProtect() == null // is the Plugin "AnimalProtect" activated? ?
                                        || !MbPets.getInstance().getPluginAnimalProtect().hasOwner(pet.getTarget().getUniqueId()))) { // if it is: is the target protected?

                            if (!pet.onAttack()) {
                                return;
                            }
                            
                            Vector jumpTarget = entity.getLocation().subtract(pet.getTarget().getLocation()).toVector().normalize().multiply(-0.5);
                            entity.teleport(entity.getLocation().setDirection(jumpTarget));
                            entity.setVelocity(jumpTarget);

                            pet.showParticles();
                            
                            // launch the target into the air and do some damage depending on the pets attack strength and active modifiers
                            pet.getTarget().setVelocity(new Vector(0, 0.5, 0));
                            // can't use owner as that's not compatible with NoCheatPlus :/ TODO: Add compatibility
                            //((Damageable) pet.getTarget()).damage((float) (MbPetsConfig.getPetAttackStrength(pet.getType()) * pet.getLevel().getAttackStrengthModifier()), owner);
                            
                            pet.getTarget().damage((float) (MbPetsConfig.getPetAttackStrength(pet.getType()) * pet.getLevel().getAttackStrengthModifier()), entity);
                            // sound only necessary if pet entity is damager and not the player
                            pet.getEntity().getWorld().playSound(pet.getEntity().getLocation(), MbPetsConfig.getPetSound(pet.getType()), 5, 1);
                        }
                    }

                    double maxHealth = entity.getMaxHealth();
                    if (entity.getHealth() < maxHealth && pet.getLastCombat() + MbPetsConfig.getRegenerationOutOfCombat() * 1000 < System.currentTimeMillis()) {
                        double newHealth = entity.getHealth() + MbPetsConfig.getRegenerationAmount() * pet.getLevel().getRegenerationModifier();

                        if ((int) Math.round(newHealth) != (int) Math.round(entity.getHealth())) {
                            entity.getWorld().spawnParticle(
                                    Particle.HEART,
                                    entity.getLocation().add(0, 1, 0),
                                    3,
                                    0.5, 0.5, 0.5
                            );
                        }

                        entity.setHealth(Math.min(newHealth, maxHealth));
                        pet.updateDisplayName();
                    }
                } else {
                    pet.uncall();
                }
            }
        }, 0, MbPetsConfig.getFollowTaskDelay());
    }

    private static void fallbackMove(Mob entity, Player owner, double distance, double speed) {
        Location nextTo = getLocationNextTo(owner, distance);
        Vector toMove = nextTo.subtract(entity.getLocation()).toVector().normalize().multiply(speed * MbPetsConfig.getFollowTaskDelay() / 20);
        Location targetLocation = entity.getLocation().add(toMove);
        if (targetLocation.isChunkLoaded() && targetLocation.getBlock().isPassable() || entity instanceof Vex) {
            entity.teleport(targetLocation);
        }
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
     * returns the direction a location is looking at
     *
     * @param location the location
     * @return a direction
     */
    public static BlockFace getDirection(Location location) {
        float yaw = location.getYaw();
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

    /**
     * Get a location that is next to an entity
     *
     * @param entity    The entity to get the location next to
     * @param distance  The distance of the location
     * @return          The new location
     */
    public static Location getLocationNextTo(Entity entity, double distance) {
        return getLocationNextTo(entity.getLocation(), distance);
    }
    
    /**
     * Get a location that is next to an entity
     *
     * @param loc       The entity to get the location next to
     * @param distance  The distance of the location
     * @return          The new location
     */
    public static Location getLocationNextTo(Location loc, double distance) {
        loc = loc.clone();
        switch (getDirection(loc)) {
            case NORTH:
                loc.add(distance, 0, -distance);
                break;
            case EAST:
                loc.add(distance, 0, -distance);
                break;
            case SOUTH:
                loc.add(-distance, 0, distance);
                break;
            case WEST:
                loc.add(-distance, 0, -distance);
                break;
        }
        return loc;
    }

}
