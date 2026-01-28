package cp620.content.internal;

import cp620.content.api.CoreHooks;

public final class CoreInstaller
{
    private static boolean ran;

    private CoreInstaller()
    {
    }

    public static void onContentInit()
    {
        if(ran) return;
        ran = true;

        CoreHooks.freezeAll();
        CorePatcher.patchAll();
    }
}
