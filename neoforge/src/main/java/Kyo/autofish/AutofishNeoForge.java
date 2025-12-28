package Kyo.autofish;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(afkfish)
public class AutofishNeoForge {
    public AutofishNeoForge(IEventBus modEventBus) {
        // Đăng ký sự kiện FMLClientSetupEvent nếu cần init gì đó
        modEventBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        // Gọi logic khởi tạo từ Common
        Autofish.init(); 
    }
}