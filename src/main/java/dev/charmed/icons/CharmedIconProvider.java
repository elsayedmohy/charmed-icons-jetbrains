package dev.charmed.icons;

import com.intellij.ide.FileIconProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Resolves Charmed icons using the same rules as the VS Code icon theme:
 * exact file name first, then the longest matching extension, then folder names.
 */
public final class CharmedIconProvider implements FileIconProvider, DumbAware {
    private static final Map<String, String> FILE_NAMES = new HashMap<>();
    private static final Map<String, String> FILE_EXTENSIONS = new HashMap<>();
    private static final Map<String, String> FOLDER_NAMES = new HashMap<>();
    private static String defaultFolder = "folders/folder.svg";

    static {
        try (InputStream in = CharmedIconProvider.class.getResourceAsStream("/charmed/mapping.tsv")) {
            if (in != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t");
                    if (parts.length != 3) continue;
                    switch (parts[0]) {
                        case "name" -> FILE_NAMES.put(parts[1], parts[2]);
                        case "ext" -> FILE_EXTENSIONS.put(parts[1], parts[2]);
                        case "folder" -> FOLDER_NAMES.put(parts[1], parts[2]);
                        case "default-folder" -> defaultFolder = parts[2];
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public @Nullable Icon getIcon(@NotNull VirtualFile file, int flags, @Nullable Project project) {
        String name = file.getName().toLowerCase(Locale.ROOT);
        String path;
        if (file.isDirectory()) {
            path = FOLDER_NAMES.getOrDefault(name, defaultFolder);
        } else {
            path = FILE_NAMES.get(name);
            for (int dot = name.indexOf('.'); path == null && dot >= 0; dot = name.indexOf('.', dot + 1)) {
                path = FILE_EXTENSIONS.get(name.substring(dot + 1));
            }
        }
        return path == null ? null : IconLoader.getIcon("/icons/" + path, CharmedIconProvider.class);
    }
}
