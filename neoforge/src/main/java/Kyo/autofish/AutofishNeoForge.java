import Kyo.autofish.Autofish;
import Kyo.autofish.gui.AutofishScreenBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(afkfish)
public AutofishNeoForge(IEventBus modEventBus, ModContainer modContainer) {
    // Gọi hàm init chung
    Autofish.getInstance().init();

    // Đăng ký màn hình config
    modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) ->
            AutofishScreenBuilder.buildScreen(Autofish.getInstance().getConfigManager(), parent)
    );
}

void main() {
}