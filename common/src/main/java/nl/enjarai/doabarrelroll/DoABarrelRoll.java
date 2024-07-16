package nl.enjarai.doabarrelroll;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.platform.Services;
import org.slf4j.Logger;

import java.nio.file.Path;

public class DoABarrelRoll {
    public static final String MODID = "do_a_barrel_roll";
    public static final Logger LOGGER = Services.PLATFORM.getLogger();

    public static final Identifier HANDSHAKE_CHANNEL = id("handshake");
    public static final Identifier SERVER_CONFIG_UPDATE_CHANNEL = id("server_config_update");
    public static final Identifier ROLL_CHANNEL = id("player_roll");

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }

    public static void init() {
        var configFile = Path.of("config");
    }

    public static PacketByteBuf createBuf() {
        return new PacketByteBuf(Unpooled.buffer());
    }
}
