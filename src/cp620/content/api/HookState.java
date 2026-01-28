package cp620.content.api;

import arc.func.Prov;

public interface HookState
{
    <T> T coreGetState(Object key);

    <T> T coreGetState(Object key, Prov<T> factory);

    void corePutState(Object key, Object value);
}
