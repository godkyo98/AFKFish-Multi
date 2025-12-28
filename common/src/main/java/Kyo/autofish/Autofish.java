package Kyo.autofish;

import Kyo.autofish.config.ConfigManager;
import Kyo.autofish.monitor.FishMonitorMP;
import Kyo.autofish.monitor.FishMonitorMPMotion;
import Kyo.autofish.monitor.FishMonitorMPSound;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Kyo.autofish.scheduler.AutofishScheduler;


public class Autofish {
    public static final String MOD_ID = "AFKFish";
    public static final String MOD_NAME = "AFKFish";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    private AutofishScheduler scheduler;
    // Singleton Instance
    private static final Autofish INSTANCE = new Autofish();

    private final Minecraft client;
    private ConfigManager configManager;
    private FishMonitorMP fishMonitorMP;

    // Các biến trạng thái logic
    private boolean hookExists = false;
    private long hookRemovedAt = 0L;
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
        this.configManager = new ConfigManager();

        // Khởi tạo scheduler
        this.scheduler = new AutofishScheduler();

        setDetection();
        LOG.info("[Autofish] Common logic initialized.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    // Cài đặt chế độ theo dõi dựa trên Config
    public void setDetection() {
        if (Autofish.getConfig().isUseSoundDetection()) {
            fishMonitorMP = new FishMonitorMPSound();
        } else {
            fishMonitorMP = new FishMonitorMPMotion();
        }
    }

    // --- LOGIC CHÍNH ---

    // Được gọi mỗi tick (Bạn cần gọi hàm này từ ClientTickEvent ở 2 loader)
    public void onTick() {
        if (client.player == null || client.level == null) return;

        // Cập nhật thời gian
        timeMillis++;

        // Logic tự động recast, check inventory... sẽ đặt ở đây
        // (Bạn có thể copy thêm logic từ code cũ vào đây nếu cần)
    }

    // Xử lý Packet từ Mixin Network
    public void handlePacket(Packet<?> packet) {
        if (configManager.getConfig().isAutofishEnabled() && fishMonitorMP != null) {
            // Logic xử lý packet (Sound/Motion) delegating cho Monitor
            // Tạm thời giả lập, bạn cần đảm bảo class FishMonitorMP có phương thức này
            // fishMonitorMP.handlePacket(packet);
        }
    }

    // Xử lý Logic câu cá từ Mixin FishingHook
    public void tickFishingLogic(Entity owner, int nibble) {
        if (client.player != null && owner.getUUID().equals(client.player.getUUID())) {
            // Logic phát hiện cá cắn câu dựa trên nibble (countdown)
            // Nếu nibble > 0 và giảm dần -> Cá sắp cắn
            // Code chi tiết tùy thuộc vào logic cũ của bạn
        }
    }

    // Helper: Kiểm tra item
    public boolean isHoldingFishingRod() {
        return isItemFishingRod(getHeldItem().getItem());
    }

    private ItemStack getHeldItem() {
        if (client.player == null) return ItemStack.EMPTY;

        if (!configManager.getConfig().isMultiRod()) {
            if (isItemFishingRod(client.player.getOffhandItem().getItem()))
                return client.player.getOffhandItem();
        }
        return client.player.getMainHandItem();
    }

    private boolean isItemFishingRod(Item item) {
        return item == Items.FISHING_ROD || item instanceof FishingRodItem;
    }

    public void catchFish() {
    }
}