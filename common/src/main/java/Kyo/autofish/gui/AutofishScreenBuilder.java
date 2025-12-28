package Kyo.autofish.gui;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
// XÓA: import net.minecraft.class_2561; (Đây là tên cũ, không dùng cho 1.21.11)
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
// XÓA: import Kyo.autofish.FabricModAutofish; (Gây lỗi bên NeoForge)
import Kyo.autofish.config.ConfigManager; // Thêm dòng này
import Kyo.autofish.config.Config;

import java.util.function.Function;

public class AutofishScreenBuilder {

    private static final Function<Boolean, Component> yesNoTextSupplier = bool -> {
        if (bool) return Component.translatable("options.autofish.toggle.on");
        else return Component.translatable("options.autofish.toggle.off");
    };

    // SỬA ĐỔI: Thay tham số FabricModAutofish thành ConfigManager
    public static Screen buildScreen(ConfigManager configManager, Screen parentScreen) {

        Config defaults = new Config();
        // Lấy config từ ConfigManager (đã được làm sạch ở bước trước)
        Config config = configManager.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parentScreen)
                .setTitle(Component.translatable("options.autofish.title"))
                .transparentBackground()
                // QUAN TRỌNG: Thêm dòng này để lưu file khi bấm Save
                .setSavingRunnable(() -> configManager.writeConfig(true));

        ConfigCategory configCat = builder.getOrCreateCategory(Component.translatable("options.autofish.general.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // --- Các phần dưới thay modAutofish thành configManager hoặc config trực tiếp ---

        AbstractConfigListEntry<Boolean> toggleAutofish = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.toggle.title"), config.isAutofishEnabled())
                .setDefaultValue(defaults.isAutofishEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setTooltip(
                        Component.translatable("options.autofish.toggle.tooltip_0"),
                        Component.translatable("options.autofish.toggle.tooltip_1")
                )
                .setSaveConsumer(newValue -> {
                    config.setAutofishEnabled(newValue);
                })
                .build();

        AbstractConfigListEntry<Boolean> toggleMultiRod = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.multirod.title"), config.isMultiRodEnabled())
                .setDefaultValue(defaults.isMultiRodEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setTooltip(
                        Component.translatable("options.autofish.multirod.tooltip_0"),
                        Component.translatable("options.autofish.multirod.tooltip_1")
                )
                .setSaveConsumer(newValue -> {
                    config.setMultiRodEnabled(newValue);
                })
                .build();

        AbstractConfigListEntry<Boolean> toggleBreakProtection = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.protection.title"), config.isBreakProtectionEnabled())
                .setDefaultValue(defaults.isBreakProtectionEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setTooltip(
                        Component.translatable("options.autofish.protection.tooltip_0"),
                        Component.translatable("options.autofish.protection.tooltip_1")
                )
                .setSaveConsumer(newValue -> {
                    config.setBreakProtectionEnabled(newValue);
                })
                .build();

        AbstractConfigListEntry<Boolean> togglePersistentMode = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.persistent.title"), config.isPersistentModeEnabled())
                .setDefaultValue(defaults.isPersistentModeEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setTooltip(
                        Component.translatable("options.autofish.persistent.tooltip_0"),
                        Component.translatable("options.autofish.persistent.tooltip_1")
                )
                .setSaveConsumer(newValue -> {
                    config.setPersistentModeEnabled(newValue);
                })
                .build();

        AbstractConfigListEntry<Boolean> toggleSoundDetection = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.sound.title"), config.isSoundDetectionEnabled())
                .setDefaultValue(defaults.isSoundDetectionEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setTooltip(
                        Component.translatable("options.autofish.sound.tooltip_0"),
                        Component.translatable("options.autofish.sound.tooltip_1")
                )
                .setSaveConsumer(newValue -> {
                    config.setSoundDetectionEnabled(newValue);
                })
                .build();

        AbstractConfigListEntry<Boolean> toggleForceMPDetection = entryBuilder.startBooleanToggle(Component.translatable("options.autofish.force_mp.title"), config.isForceMPDetectionEnabled())
                .setDefaultValue(defaults.isForceMPDetectionEnabled())
                .setYesNoTextSupplier(yesNoTextSupplier)
                .setTooltip(
                        Component.translatable("options.autofish.force_mp.tooltip_0"),
                        Component.translatable("options.autofish.force_mp.tooltip_1"),
                        Component.translatable("options.autofish.force_mp.tooltip_2")
                )
                .setSaveConsumer(newValue -> {
                    config.setForceMPDetectionEnabled(newValue);
                })
                .build();

        AbstractConfigListEntry<Integer> recastDelaySlider = entryBuilder.startIntSlider(Component.translatable("options.autofish.recast_delay.title"), config.getRecastDelay(), 10, 400)
                .setDefaultValue(defaults.getRecastDelay())
                .setTooltip(
                        Component.translatable("options.autofish.recast_delay.tooltip_0"),
                        Component.translatable("options.autofish.recast_delay.tooltip_1")
                )
                .setSaveConsumer(newValue -> {
                    config.setRecastDelay(newValue);
                })
                .build();

        AbstractConfigListEntry<String> clearLagRegexField = entryBuilder.startStrField(Component.translatable("options.autofish.clear_regex.title"), config.getClearLagRegex())
                .setDefaultValue(defaults.getClearLagRegex())
                .setTooltip(
                        Component.translatable("options.autofish.clear_regex.tooltip_0"),
                        Component.translatable("options.autofish.clear_regex.tooltip_1"),
                        Component.translatable("options.autofish.clear_regex.tooltip_2")
                )
                .setSaveConsumer(newValue -> {
                    config.setClearLagRegex(newValue);
                })
                .build();


        SubCategoryBuilder subCatBuilderBasic = entryBuilder.startSubCategory(Component.translatable("options.autofish.basic.title"));
        subCatBuilderBasic.add(toggleAutofish);
        subCatBuilderBasic.add(toggleMultiRod);
        subCatBuilderBasic.add(toggleBreakProtection);
        subCatBuilderBasic.add((togglePersistentMode));
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