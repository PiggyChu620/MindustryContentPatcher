package cp620.content.api;

import arc.struct.Seq;

public final class HookRegistry<T>
{
    public static final class Entry<T>
    {
        public final int priority;
        public final T hook;

        public Entry(int priority, T hook)
        {
            this.priority = priority;
            this.hook = hook;
        }
    }

    private final Seq<Entry<T>> hooks = new Seq<>();
    private boolean frozen;

    public void register(int priority, T hook)
    {
        if(frozen)
        {
            throw new IllegalStateException("Registry frozen. Register earlier.");
        }

        hooks.add(new Entry<>(priority, hook));
    }

    public Seq<Entry<T>> hooks()
    {
        return hooks;
    }

    public int size()
    {
        return hooks.size;
    }

    public void freeze()
    {
        if(frozen) return;
        frozen = true;

        hooks.sort(e -> e.priority);
        hooks.shrink();
    }
}
