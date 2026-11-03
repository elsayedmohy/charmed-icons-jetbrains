package dev.charmed.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileSystemItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/**
 * Project trees ask PSI icon providers before file icon providers, and some IDEs
 * (e.g. Rider for C#) register their own there, so Charmed has to answer first too.
 */
public final class CharmedPsiIconProvider extends IconProvider implements DumbAware {
    @Override
    public @Nullable Icon getIcon(@NotNull PsiElement element, int flags) {
        if (!(element instanceof PsiFileSystemItem item)) return null;
        VirtualFile file = item.getVirtualFile();
        return file == null ? null : CharmedIconProvider.iconFor(file);
    }
}
