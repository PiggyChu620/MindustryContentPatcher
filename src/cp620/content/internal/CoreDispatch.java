package cp620.content.internal;

import arc.func.Boolp;
import arc.func.Floatp;
import arc.func.Intp;
import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.type.Item;
import mindustry.type.Liquid;
import cp620.content.api.*;

public final class CoreDispatch
{
    private CoreDispatch()
    {
    }

    public static boolean acceptItem(Building self, Building source, Item item, Boolp fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        for(int i = 0; i < hooks.size; i++)
        {
            HookDecision d = hooks.get(i).hook.acceptItem(self, source, item);
            if(!d.isPass()) return d.asBoolean();
        }

        return fallback.get();
    }

    public static boolean acceptLiquid(Building self, Building source, Liquid liquid, Boolp fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        for(int i = 0; i < hooks.size; i++)
        {
            HookDecision d = hooks.get(i).hook.acceptLiquid(self, source, liquid);
            if(!d.isPass()) return d.asBoolean();
        }

        return fallback.get();
    }

    public static boolean dump(Building self, Item item, Boolp fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        for(int i = 0; i < hooks.size; i++)
        {
            HookDecision d = hooks.get(i).hook.dump(self, item);
            if(!d.isPass()) return d.asBoolean();
        }

        return fallback.get();
    }

    public static void handleItem(Building self, Building source, Item item, Runnable fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        for(int i = 0; i < hooks.size; i++)
        {
            if(hooks.get(i).hook.handleItemPre(self, source, item).isHandled())
            {
                return;
            }
        }

        fallback.run();

        for(int i = 0; i < hooks.size; i++)
        {
            hooks.get(i).hook.handleItemPost(self, source, item);
        }
    }

    public static void handleLiquid(Building self, Building source, Liquid liquid, float amount, Runnable fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        for(int i = 0; i < hooks.size; i++)
        {
            if(hooks.get(i).hook.handleLiquidPre(self, source, liquid, amount).isHandled())
            {
                return;
            }
        }

        fallback.run();

        for(int i = 0; i < hooks.size; i++)
        {
            hooks.get(i).hook.handleLiquidPost(self, source, liquid, amount);
        }
    }

    public static int acceptStack(Building self, Item item, int amount, Teamc source, Intp fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        for(int i = 0; i < hooks.size; i++)
        {
            int v = hooks.get(i).hook.acceptStack(self, item, amount, source);
            if(v != HookConst.PassInt) return v;
        }

        return fallback.get();
    }

    public static void handleStack(Building self, Item item, int amount, Teamc source, Runnable fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        for(int i = 0; i < hooks.size; i++)
        {
            if(hooks.get(i).hook.handleStackPre(self, item, amount, source).isHandled())
            {
                return;
            }
        }

        fallback.run();

        for(int i = 0; i < hooks.size; i++)
        {
            hooks.get(i).hook.handleStackPost(self, item, amount, source);
        }
    }

    public static void updateTile(Building self, Runnable fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        boolean skipSuper = false;
        for(int i = 0; i < hooks.size; i++)
        {
            if(hooks.get(i).hook.updatePre(self).isHandled())
            {
                skipSuper = true;
            }
        }

        if(!skipSuper)
        {
            fallback.run();
        }

        for(int i = 0; i < hooks.size; i++)
        {
            hooks.get(i).hook.updatePost(self);
        }
    }

    public static void updateTransport(Building self, Building other, Runnable fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        for(int i = 0; i < hooks.size; i++)
        {
            if(hooks.get(i).hook.updateTransportPre(self, other).isHandled())
            {
                return;
            }
        }

        fallback.run();

        for(int i = 0; i < hooks.size; i++)
        {
            hooks.get(i).hook.updateTransportPost(self, other);
        }
    }

    public static void doDump(Building self, Runnable fallback)
    {
        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();

        for(int i = 0; i < hooks.size; i++)
        {
            if(hooks.get(i).hook.doDumpPre(self).isHandled())
            {
                return;
            }
        }

        fallback.run();

        for(int i = 0; i < hooks.size; i++)
        {
            hooks.get(i).hook.doDumpPost(self);
        }
    }

    public static float powerProduction(Building self, Floatp fallback)
    {
        float base = fallback.get();

        Seq<HookRegistry.Entry<BuildHook>> hooks = CoreHooks.buildHooks().hooks();
        float mul = 1f;

        for(int i = 0; i < hooks.size; i++)
        {
            mul *= hooks.get(i).hook.powerProductionMultiplier(self);
        }

        return base * mul;
    }

    // --- Per-building state bag helpers (optional, lazy) ---
    public static <T> T coreGetState(HookState self, Object key)
    {
        return self.coreGetState(key);
    }

    public static <T> T coreGetState(HookState self, Object key, Prov<T> factory)
    {
        return self.coreGetState(key, factory);
    }

    public static void corePutState(HookState self, Object key, Object value)
    {
        self.corePutState(key, value);
    }

    // Utility for implementations.
    public static final class StateImpl
    {
        private ObjectMap<Object, Object> state;

        public <T> T get(Object key)
        {
            if(state == null) return null;
            @SuppressWarnings("unchecked")
            T v = (T)state.get(key);
            return v;
        }

        public <T> T get(Object key, Prov<T> factory)
        {
            if(state == null)
            {
                state = new ObjectMap<>();
            }

            @SuppressWarnings("unchecked")
            T v = (T)state.get(key);

            if(v == null)
            {
                v = factory.get();
                state.put(key, v);
            }

            return v;
        }

        public void put(Object key, Object value)
        {
            if(state == null)
            {
                state = new ObjectMap<>();
            }

            state.put(key, value);
        }
    }
}
