package Kyo.autofish;

import Kyo.autofish.gui.AutofishScreenBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;

public class FabricModAutofish implements ClientModInitializer {

    private static KeyMapping autofishGuiKey;

    @Override
    public void onInitializeClient() {
        // 1. Khởi động bộ não chung (Common Logic)
        Autofish.getInstance().init();

        // 2. Đăng ký phím tắt (Mặc định phím V)
        autofishGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.autofish.open_gui",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.autofish.title"
        ));

        // 3. Đăng ký sự kiện Tick (Chạy mỗi frame game)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Kiểm tra phím tắt để mở GUI cài đặt
            while (autofishGuiKey.consumeClick()) {
                // Lấy ConfigManager từ Common để truyền vào GUI Builder
                client.setScreen(AutofishScreenBuilder.buildScreen(Autofish.getInstance().getConfigManager(), client.screen));
            }

            // Gọi hàm onTick chung của mod
            Autofish.getInstance().onTick();
        });
    }
}