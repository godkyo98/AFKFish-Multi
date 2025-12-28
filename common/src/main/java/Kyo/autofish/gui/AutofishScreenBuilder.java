package Kyo.autofish.gui;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen; // Mapping chuẩn: screens (có 's')
import net.minecraft.network.chat.Component;   // Mapping chuẩn: Component (thay cho Text/class_2561)
import Kyo.autofish.config.ConfigManager;
import Kyo.autofish.config.Config;

import java.util.function.Function;

public class AutofishScreenBuilder {

    private static final Function<Boolean, Component> yesNoTextSupplier = bool -> {
        if (bool) return Component.translatable("options.autofish.toggle.on");
        else return Component.translatable("options.autofish.toggle.off");
    };

    // Nhận vào ConfigManager thay vì class của Fabric
    public static Screen buildScreen(ConfigManager configManager, Screen parentScreen) {

        Config defaults = new Config();
        Config config = configManager.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parentScreen)
                .setTitle(Component.translatable("options.autofish.title"))
                .transparentBackground()
                .setSavingRunnable(() -> configManager.writeConfig(true));

        ConfigCategory configCat = builder.getOrCreateCategory(Component.translatable("options.autofish.general.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // --- CÁC CONFIG ENTRY (Copy logic cũ nhưng dùng biến 'config') ---

        AbstractConfigListEntry<Boolean> toggleAutofish = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.toggle.title"), config.isAutofishEnabled())
                .setDefaultValue(defaults.isAutofishEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setTooltip(
                        Component.translatable("options.autofish.toggle.tooltip_0"),
                        Component.translatable("options.autofish.toggle.tooltip_1")
                )
                .setSaveConsumer(config::setAutofishEnabled)
                .build();

        AbstractConfigListEntry<Boolean> toggleMultiRod = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.multirod.title"), config.isMultiRodEnabled())
                .setDefaultValue(defaults.isMultiRodEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setTooltip(
                        Component.translatable("options.autofish.multirod.tooltip_0"),
                        Component.translatable("options.autofish.multirod.tooltip_1")
                )
                .setSaveConsumer(config::setMultiRodEnabled)
                .build();

        // ... (Bạn làm tương tự cho các option còn lại: BreakProtection, PersistentMode...)
        // Mẹo: Dùng method reference `config::set...` cho gọn code saveConsumer

        AbstractConfigListEntry<Boolean> toggleBreakProtection = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.protection.title"), config.isBreakProtectionEnabled())
                .setDefaultValue(defaults.isBreakProtectionEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setSaveConsumer(config::setBreakProtectionEnabled)
                .build();

        AbstractConfigListEntry<Boolean> togglePersistentMode = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.persistent.title"), config.isPersistentModeEnabled())
                .setDefaultValue(defaults.isPersistentModeEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setSaveConsumer(config::setPersistentModeEnabled)
                .build();

        AbstractConfigListEntry<Boolean> toggleSoundDetection = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.sound.title"), config.isSoundDetectionEnabled())
                .setDefaultValue(defaults.isSoundDetectionEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setSaveConsumer(config::setSoundDetectionEnabled)
                .build();

        AbstractConfigListEntry<Boolean> toggleForceMPDetection = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.force_mp.title"), config.isForceMPDetectionEnabled())
                .setDefaultValue(defaults.isForceMPDetectionEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setSaveConsumer(config::setForceMPDetectionEnabled)
                .build();

        AbstractConfigListEntry<Integer> recastDelaySlider = entryBuilder.startIntSlider(Component.translatable("options.autofish.recast_delay.title"), config.getRecastDelay(), 10, 400)
                .setDefaultValue(defaults.getRecastDelay())
                .setSaveConsumer(config::setRecastDelay)
                .build();

        AbstractConfigListEntry<String> clearLagRegexField = entryBuilder.startStrField(Component.translatable("options.autofish.clear_regex.title"), config.getClearLagRegex())
                .setDefaultValue(defaults.getClearLagRegex())
                .setSaveConsumer(config::setClearLagRegex)
                .build();

        // --- ADD VÀO CATEGORY ---
        SubCategoryBuilder subCatBuilderBasic = entryBuilder.startSubCategory(Component.translatable("options.autofish.basic.title"));
        subCatBuilderBasic.add(toggleAutofish);
        subCatBuilderBasic.add(toggleMultiRod);
        subCatBuilderBasic.add(toggleBreakProtection);
        subCatBuilderBasic.add(togglePersistentMode);
        subCatBuilderBasic.setExpanded(true);

        SubCategoryBuilder subCatBuilderAdvanced = entryBuilder.startSubCategory(Component.translatable("options.autofish.advanced.title"));
        subCatBuilderAdvanced.add(toggleSoundDetection);
        subCatBuilderAdvanced.add(toggleForceMPDetection);
        subCatBuilderAdvanced.add(recastDelaySlider);
        subCatBuilderAdvanced.add(clearLagRegexField);
        subCatBuilderAdvanced.setExpanded(true);

        configCat.addEntry(subCatBuilderBasic.build());
        configCat.addEntry(subCatBuilderAdvanced.build());

        return builder.build();
    }
}