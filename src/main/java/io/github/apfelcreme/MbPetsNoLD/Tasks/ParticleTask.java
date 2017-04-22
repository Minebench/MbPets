package io.github.apfelcreme.MbPetsNoLD.Tasks;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;

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
public class ParticleTask {

    private static int taskId = -1;

    /**
     * starts a task
     */
    public static void create() {
        if (isActive()) {
            kill();
        }
        taskId = MbPets.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(MbPets.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Pet pet : PetManager.getInstance().getPets().values()) {
                    if (pet.getLevel() != null && pet.getLevel().getEffect() != null) {
                        for (int i = 0; i < 3; i++) {
                            pet.getEntity().getWorld().spigot().playEffect(pet.getEntity().getLocation(),
                                    pet.getLevel().getEffect(), 0, 0,
                                    (float) (-1 + Math.random() * 2),
                                    (float) (Math.random() * 2),
                                    (float) (-1 + Math.random() * 2), 0, 1, 50);
                        }
                    }
                }
            }
        }, 0, MbPetsConfig.getParticleTaskDelay());
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
}
