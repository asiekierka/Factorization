package factorization.client.render;

import net.minecraft.src.EntitySmokeFX;
import net.minecraft.src.World;

public class EntitySteamFX extends EntitySmokeFX {

    public EntitySteamFX(World w, double x, double y, double z) {
        super(w, x, y, z, 0, 0.05, 0);
        particleRed = particleGreen = particleBlue = 0.95F;
        setParticleTextureIndex(0);
    }

}
