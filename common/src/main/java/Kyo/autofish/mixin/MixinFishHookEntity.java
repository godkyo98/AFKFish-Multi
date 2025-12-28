package Kyo.autofish.mixin;

import Kyo.autofish.Autofish; // Import Common
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public class MixinFishHookEntity {

    @Shadow private int nibble;

    @Inject(method = "catchingFish", at = @At("TAIL"))
    private void catchingFish(BlockPos pos, CallbackInfo ci) {
        // Logic được chuyển về Common
        Autofish.getInstance().tickFishingLogic(((FishingHook) (Object) this).getOwner(), nibble);
    }
}