package Kyo.autofish;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import Kyo.autofish.Autofish;
import Kyo.autofish.gui.AutofishScreenBuilder;

public class ModMenuApiAutofish implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // Truyền ConfigManager từ instance chung vào
        return AutofishScreenBuilder.buildScreen(Autofish.getInstance().getConfigManager(), parent);
    }
}
