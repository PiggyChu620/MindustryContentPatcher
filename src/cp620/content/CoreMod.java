package cp620.content;

import arc.Events;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import cp620.content.internal.CoreInstaller;

public class CoreMod extends Mod
{
    public CoreMod()
    {
        Events.on(EventType.ContentInitEvent.class, e ->
        {
            CoreInstaller.onContentInit();
        });
    }
}
