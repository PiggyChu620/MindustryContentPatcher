package cp620.content.api;

import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.type.Item;
import mindustry.type.Liquid;

public interface BuildHook
{
    // --- Update ---
    default HandleDecision updatePre(Building self)
    {
        return HandleDecision.Pass;
    }

    default void updatePost(Building self)
    {
    }

    // --- Items ---
    default HookDecision acceptItem(Building self, Building source, Item item)
    {
        return HookDecision.Pass;
    }

    default HandleDecision handleItemPre(Building self, Building source, Item item)
    {
        return HandleDecision.Pass;
    }

    default void handleItemPost(Building self, Building source, Item item)
    {
    }

    default HookDecision dump(Building self, Item item)
    {
        return HookDecision.Pass;
    }

    // --- Liquids ---
    default HookDecision acceptLiquid(Building self, Building source, Liquid liquid)
    {
        return HookDecision.Pass;
    }

    default HandleDecision handleLiquidPre(Building self, Building source, Liquid liquid, float amount)
    {
        return HandleDecision.Pass;
    }

    default void handleLiquidPost(Building self, Building source, Liquid liquid, float amount)
    {
    }

    // --- Stacks ---
    // Return HookConst.PassInt for PASS; otherwise return accepted amount (0..amount).
    default int acceptStack(Building self, Item item, int amount, Teamc source)
    {
        return HookConst.PassInt;
    }

    default HandleDecision handleStackPre(Building self, Item item, int amount, Teamc source)
    {
        return HandleDecision.Pass;
    }

    default void handleStackPost(Building self, Item item, int amount, Teamc source)
    {
    }

    // --- Item bridge specific (optional) ---
    default HandleDecision updateTransportPre(Building self, Building other)
    {
        return HandleDecision.Pass;
    }

    default void updateTransportPost(Building self, Building other)
    {
    }

    default HandleDecision doDumpPre(Building self)
    {
        return HandleDecision.Pass;
    }

    default void doDumpPost(Building self)
    {
    }

    // --- Power production modifier (optional) ---
    // Core simply multiplies all hooks (it does not interpret meaning).
    default float powerProductionMultiplier(Building self)
    {
        return 1f;
    }
}
