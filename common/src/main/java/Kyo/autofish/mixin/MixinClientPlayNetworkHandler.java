package Kyo.autofish.mixin;

import Kyo.autofish.Autofish; // Import Common class
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPlayNetworkHandler extends ClientCommonPacketListenerImpl implements TickablePacketListener, ClientGamePacketListener {

    protected MixinClientPlayNetworkHandler(Minecraft client, Connection connection, CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "handleSoundEvent", at = @At("HEAD"))
    public void onPlaySound(ClientboundSoundPacket packet, CallbackInfo ci) {
        // Gọi thẳng vào Common Singleton
        if (minecraft.isSameThread()) Autofish.getInstance().handlePacket(packet);
    }

    @Inject(method = "handleSetEntityMotion", at = @At("HEAD"))
    public void onVelocityUpdate(ClientboundSetEntityMotionPacket packet, CallbackInfo ci) {
        if (minecraft.isSameThread()) Autofish.getInstance().handlePacket(packet);
    }

    @Inject(method = "handleSystemChat", at = @At("HEAD"))
    public void onChatMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {
        if (minecraft.isSameThread()) Autofish.getInstance().handlePacket(packet);
    }
}