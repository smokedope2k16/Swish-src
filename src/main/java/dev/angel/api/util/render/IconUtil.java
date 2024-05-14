package dev.angel.api.util.render;

import com.google.common.collect.Lists;
import dev.angel.api.util.logging.Logger;
import net.minecraft.resource.InputSupplier;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.Level;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class IconUtil {

    public static List<InputSupplier<InputStream>> getAllPngResources() {
        List<String> icons = Lists.newArrayList();
        icons.add("icon_16x16.png");
        icons.add("icon_32x32.png");
        icons.add("icon_48x48.png");
        icons.add("icon_128x128.png");
        icons.add("icon_256x256.png");
        return icons.stream().map(IconUtil::getResource).toList();
    }

    public static InputSupplier<InputStream> getResource(String path) {
        final String fullPath = "/assets/icons/" + path;

        InputStream inputstream = null;
        byte[] data = null;

        try {
            inputstream = IconUtil.class.getResourceAsStream(fullPath);

            if (inputstream != null) {
                data = IOUtils.toByteArray(inputstream);
            }

        } catch (IOException e) {
            Logger.getLogger().log(Level.INFO, "Couldn't set icon: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputstream);
        }

        if (data == null) {
            throw new RuntimeException("Unexpected resource path " + path);
        }

        byte[] finalData = data;
        return () -> new ByteArrayInputStream(finalData);
    }
}
