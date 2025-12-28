package Kyo.autofish;

import Kyo.autofish.Autofish;
import Kyo.autofish.gui.AutofishScreenBuilder;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;

// ID Mod phải khớp với file neoforge.mods.toml (thường là 'autofish')
@Mod(Autofish.MOD_ID)
public class AutofishNeoForge {

    public static KeyMapping autofishGuiKey;

    public AutofishNeoForge(ModContainer modContainer) {
        // 1. Khởi động bộ não chung
        Autofish.getInstance().init();

        // 2. Đăng ký màn hình Config cho menu Mods của NeoForge
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) ->
                AutofishScreenBuilder.buildScreen(Autofish.getInstance().getConfigManager(), parent)
        );
    }

    // Class đăng ký sự kiện khởi tạo (Chạy trên Mod Event Bus)
    @EventBusSubscriber(modid = Autofish.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            autofishGuiKey = new KeyMapping(
                    "key.autofish.open_gui",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_V,
                    "category.autofish.title"
            );
            event.register(autofishGuiKey);
        }
    }

    // Class lắng nghe sự kiện trong game (Chạy trên Forge Event Bus)
    @EventBusSubscriber(modid = Autofish.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    public static class ClientGameEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft client = Minecraft.getInstance();

            // Xử lý mở GUI
            while (autofishGuiKey.consumeClick()) {
                client.setScreen(AutofishScreenBuilder.buildScreen(Autofish.getInstance().getConfigManager(), client.screen));
            }

            // Gọi logic tick chung
            Autofish.getInstance().onTick();
        }
    }
}