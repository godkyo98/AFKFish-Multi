package Kyo.autofish;

import Kyo.autofish.config.ConfigManager;
import Kyo.autofish.monitor.FishMonitorMP;
import Kyo.autofish.monitor.FishMonitorMPMotion;
import Kyo.autofish.monitor.FishMonitorMPSound;
import Kyo.autofish.scheduler.AutofishScheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Autofish {
    public static final String MOD_ID = "autofish";
    public static final String MOD_NAME = "Autofish";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // Singleton Instance
    private static final Autofish INSTANCE = new Autofish();

    private final Minecraft client;
    private ConfigManager configManager;
    private AutofishScheduler scheduler;
    private FishMonitorMP fishMonitorMP;

    // Các biến trạng thái logic
    public long timeMillis = 0L;

    // Constructor private
    private Autofish() {
        this.client = Minecraft.getInstance();
    }

    public static Autofish getInstance() {
        return INSTANCE;
    }

    // Hàm khởi tạo (Gọi từ FabricModAutofish hoặc AutofishNeoForge)
    public void init() {
        // 1. Khởi tạo Config
        this.configManager = new ConfigManager();

        // 2. Khởi tạo Scheduler
        this.scheduler = new AutofishScheduler();

        // 3. Cài đặt bộ theo dõi mặc định
        setDetection();

        LOG.info("[Autofish] Common logic initialized successfully.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public AutofishScheduler getScheduler() {
        return scheduler;
    }

    // Cài đặt chế độ theo dõi dựa trên Config
    public void setDetection() {
        if (configManager.getConfig().isUseSoundDetection()) {
            fishMonitorMP = new FishMonitorMPSound();
        } else {
            fishMonitorMP = new FishMonitorMPMotion();
        }
    }

    // --- LOGIC CHÍNH ---

    // Được gọi mỗi tick (Phải gọi từ ClientTickEvent ở các loader)
    public void onTick() {
        if (client.player == null || client.level == null) return;

        // Cập nhật thời gian
        timeMillis++;

        // Chạy Scheduler
        if (scheduler != null) {
            scheduler.tick(client);
        }
    } // <--- Dấu ngoặc này trước đây có thể bị thiếu gây lỗi

    // Xử lý Packet từ Mixin Network
    public void handlePacket(Packet<?> packet) {
        if (configManager != null && configManager.getConfig().isAutofishEnabled() && fishMonitorMP != null) {
            // Logic xử lý packet (Bạn cần implement hàm này trong FishMonitorMP nếu chưa có)
            // fishMonitorMP.handlePacket(packet);
        }
    }

    // Xử lý Logic câu cá từ Mixin FishingHook
    public void tickFishingLogic(Entity owner, int nibble) {
        if (client.player != null && owner.getUUID().equals(client.player.getUUID())) {
            // Logic phát hiện cá cắn câu
            // Ví dụ: Nếu nibble > 0 và giảm dần -> Cá sắp cắn
        }
    }

    // Helper: Kiểm tra item trên tay
    public boolean isHoldingFishingRod() {
        return isItemFishingRod(getHeldItem().getItem());
    }

    private ItemStack getHeldItem() {
        if (client.player == null) return ItemStack.EMPTY;

        if (configManager != null && !configManager.getConfig().isMultiRod()) {
            if (isItemFishingRod(client.player.getOffhandItem().getItem()))
                return client.player.getOffhandItem();
        }
        return client.player.getMainHandItem();
    }

    private boolean isItemFishingRod(Item item) {
        return item == Items.FISHING_ROD || item instanceof FishingRodItem;
    }
}