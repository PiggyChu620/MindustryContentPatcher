package cp620.content.api;

public final class CoreHooks
{
    private static final HookRegistry<BuildHook> buildHooks = new HookRegistry<>();

    private CoreHooks()
    {
    }

    public static void registerBuildHook(int priority, BuildHook hook)
    {
        buildHooks.register(priority, hook);
    }

    public static HookRegistry<BuildHook> buildHooks()
    {
        return buildHooks;
    }

    public static boolean hasAnyHooks()
    {
        return buildHooks.size() > 0;
    }

    public static void freezeAll()
    {
        buildHooks.freeze();
    }
}
