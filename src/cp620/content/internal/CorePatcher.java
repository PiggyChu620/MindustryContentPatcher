package cp620.content.internal;

import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.Block;

// production
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.production.Drill.DrillBuild;

// distribution
import mindustry.world.blocks.distribution.*;

// power
import mindustry.world.blocks.power.*;

// defense/support
import mindustry.world.blocks.defense.*;

import cp620.content.api.CoreHooks;
import cp620.content.api.HookState;

public final class CorePatcher
{
    private CorePatcher()
    {
    }

    public static void patchAll()
    {
        if(!CoreHooks.hasAnyHooks())
        {
            return;
        }

        for(Block b : Vars.content.blocks())
        {
            // Vanilla only
            if(b.minfo != null && b.minfo.mod != null)
            {
                continue;
            }

            // --- Production ---
            if(b instanceof BeamDrill beam)
            {
                patchBeamDrill(beam);
                continue;
            }

            if(b instanceof Drill drill)
            {
                if(drill instanceof BurstDrill burst)
                {
                    patchBurstDrill(burst);
                }
                else
                {
                    patchDrill(drill);
                }

                continue;
            }

            if(b instanceof SolidPump solid)
            {
                patchSolidPump(solid);
                continue;
            }

            if(b instanceof Pump pump)
            {
                patchPump(pump);
                continue;
            }

            if(b instanceof HeatCrafter hc)
            {
                patchHeatCrafter(hc);
                continue;
            }

            if(b instanceof AttributeCrafter ac)
            {
                patchAttributeCrafter(ac);
                continue;
            }

            if(b instanceof GenericCrafter gc)
            {
                patchGenericCrafter(gc);
                continue;
            }

            if(b instanceof WallCrafter wc)
            {
                patchWallCrafter(wc);
                continue;
            }

            // --- Power ---
            if(b instanceof HeaterGenerator hg)
            {
                patchHeaterGenerator(hg);
                continue;
            }

            if(b instanceof ConsumeGenerator cg)
            {
                patchConsumeGenerator(cg);
                continue;
            }

            if(b instanceof ThermalGenerator tg)
            {
                patchThermalGenerator(tg);
                continue;
            }

            if(b instanceof SolarGenerator sg)
            {
                patchSolarGenerator(sg);
                continue;
            }

            if(b instanceof NuclearReactor nr)
            {
                patchNuclearReactor(nr);
                continue;
            }

            if(b instanceof ImpactReactor ir)
            {
                patchImpactReactor(ir);
                continue;
            }

            if(b instanceof VariableReactor vr)
            {
                patchVariableReactor(vr);
                continue;
            }

            if(b instanceof PowerGenerator pg)
            {
                patchPowerGenerator(pg);
                continue;
            }

            // --- Distribution ---
            if(b instanceof BufferedItemBridge bib)
            {
                patchBufferedItemBridge(bib);
                continue;
            }

            if(b instanceof ItemBridge ib)
            {
                patchItemBridge(ib);
                continue;
            }

            if(b instanceof DuctJunction dj)
            {
                patchDuctJunction(dj);
                continue;
            }

            if(b instanceof Junction j)
            {
                patchJunction(j);
                continue;
            }

            if(b instanceof Sorter s)
            {
                patchSorter(s);
                continue;
            }

            // --- Defense/support ---
            if(b instanceof OverdriveProjector op)
            {
                patchOverdrive(op);
                continue;
            }

            if(b instanceof MendProjector mp)
            {
                patchMend(mp);
                continue;
            }

            if(b instanceof DirectionalForceProjector dfp)
            {
                patchDirectionalForce(dfp);
                continue;
            }

            if(b instanceof ForceProjector fp)
            {
                patchForce(fp);
            }
        }
    }

    // region ===== Shared hook-state implementation helper =====

    private static abstract class HookedState
    {
        protected final CoreDispatch.StateImpl state = new CoreDispatch.StateImpl();
    }

    // endregion

    // region ===== Production =====

    private static void patchDrill(Drill drill)
    {
        drill.buildType = () -> drill.new DrillBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            // HookState
            public <T> T coreGetState(Object key)
            {
                return st.get(key);
            }

            public <T> T coreGetState(Object key, arc.func.Prov<T> factory)
            {
                return st.get(key, factory);
            }

            public void corePutState(Object key, Object value)
            {
                st.put(key, value);
            }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchBurstDrill(BurstDrill drill)
    {
        drill.buildType = () -> drill.new BurstDrillBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key)
            {
                return st.get(key);
            }

            public <T> T coreGetState(Object key, arc.func.Prov<T> factory)
            {
                return st.get(key, factory);
            }

            public void corePutState(Object key, Object value)
            {
                st.put(key, value);
            }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchBeamDrill(BeamDrill drill)
    {
        drill.buildType = () -> drill.new BeamDrillBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key)
            {
                return st.get(key);
            }

            public <T> T coreGetState(Object key, arc.func.Prov<T> factory)
            {
                return st.get(key, factory);
            }

            public void corePutState(Object key, Object value)
            {
                st.put(key, value);
            }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchPump(Pump pump)
    {
        pump.buildType = () -> pump.new PumpBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key)
            {
                return st.get(key);
            }

            public <T> T coreGetState(Object key, arc.func.Prov<T> factory)
            {
                return st.get(key, factory);
            }

            public void corePutState(Object key, Object value)
            {
                st.put(key, value);
            }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchSolidPump(SolidPump pump)
    {
        pump.buildType = () -> pump.new SolidPumpBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key)
            {
                return st.get(key);
            }

            public <T> T coreGetState(Object key, arc.func.Prov<T> factory)
            {
                return st.get(key, factory);
            }

            public void corePutState(Object key, Object value)
            {
                st.put(key, value);
            }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchGenericCrafter(GenericCrafter crafter)
    {
        crafter.buildType = () -> crafter.new GenericCrafterBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key)
            {
                return st.get(key);
            }

            public <T> T coreGetState(Object key, arc.func.Prov<T> factory)
            {
                return st.get(key, factory);
            }

            public void corePutState(Object key, Object value)
            {
                st.put(key, value);
            }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchHeatCrafter(HeatCrafter crafter)
    {
        crafter.buildType = () -> crafter.new HeatCrafterBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key)
            {
                return st.get(key);
            }

            public <T> T coreGetState(Object key, arc.func.Prov<T> factory)
            {
                return st.get(key, factory);
            }

            public void corePutState(Object key, Object value)
            {
                st.put(key, value);
            }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchAttributeCrafter(AttributeCrafter crafter)
    {
        crafter.buildType = () -> crafter.new AttributeCrafterBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key)
            {
                return st.get(key);
            }

            public <T> T coreGetState(Object key, arc.func.Prov<T> factory)
            {
                return st.get(key, factory);
            }

            public void corePutState(Object key, Object value)
            {
                st.put(key, value);
            }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchWallCrafter(WallCrafter crafter)
    {
        crafter.buildType = () -> crafter.new WallCrafterBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key)
            {
                return st.get(key);
            }

            public <T> T coreGetState(Object key, arc.func.Prov<T> factory)
            {
                return st.get(key, factory);
            }

            public void corePutState(Object key, Object value)
            {
                st.put(key, value);
            }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    // endregion

    // region ===== Power (getPowerProduction is hookable) =====

    private static void patchPowerGenerator(PowerGenerator gen)
    {
        gen.buildType = () -> gen.new GeneratorBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public float getPowerProduction()
            {
                return CoreDispatch.powerProduction(this, super::getPowerProduction);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchConsumeGenerator(ConsumeGenerator gen)
    {
        gen.buildType = () -> gen.new ConsumeGeneratorBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public float getPowerProduction()
            {
                return CoreDispatch.powerProduction(this, super::getPowerProduction);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public boolean dump(Item item)
            {
                return CoreDispatch.dump(this, item, () -> super.dump(item));
            }
        };
    }

    private static void patchThermalGenerator(ThermalGenerator gen)
    {
        gen.buildType = () -> gen.new ThermalGeneratorBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public float getPowerProduction()
            {
                return CoreDispatch.powerProduction(this, super::getPowerProduction);
            }
        };
    }

    private static void patchSolarGenerator(SolarGenerator gen)
    {
        gen.buildType = () -> gen.new SolarGeneratorBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public float getPowerProduction()
            {
                return CoreDispatch.powerProduction(this, super::getPowerProduction);
            }
        };
    }

    private static void patchNuclearReactor(NuclearReactor gen)
    {
        gen.buildType = () -> gen.new NuclearReactorBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public float getPowerProduction()
            {
                return CoreDispatch.powerProduction(this, super::getPowerProduction);
            }
        };
    }

    private static void patchImpactReactor(ImpactReactor gen)
    {
        gen.buildType = () -> gen.new ImpactReactorBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public float getPowerProduction()
            {
                return CoreDispatch.powerProduction(this, super::getPowerProduction);
            }
        };
    }

    private static void patchVariableReactor(VariableReactor gen)
    {
        gen.buildType = () -> gen.new VariableReactorBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public float getPowerProduction()
            {
                return CoreDispatch.powerProduction(this, super::getPowerProduction);
            }
        };
    }

    private static void patchHeaterGenerator(HeaterGenerator gen)
    {
        gen.buildType = () -> gen.new HeaterGeneratorBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public float getPowerProduction()
            {
                return CoreDispatch.powerProduction(this, super::getPowerProduction);
            }
        };
    }

    // endregion

    // region ===== Distribution =====

    private static void patchJunction(Junction block)
    {
        block.buildType = () -> block.new JunctionBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchDuctJunction(DuctJunction block)
    {
        block.buildType = () -> block.new DuctJunctionBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchSorter(Sorter block)
    {
        block.buildType = () -> block.new SorterBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchItemBridge(ItemBridge block)
    {
        block.buildType = () -> block.new ItemBridgeBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public void updateTransport(Building other)
            {
                CoreDispatch.updateTransport(this, other, () -> super.updateTransport(other));
            }

            @Override
            public void doDump()
            {
                CoreDispatch.doDump(this, super::doDump);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    private static void patchBufferedItemBridge(BufferedItemBridge block)
    {
        block.buildType = () -> block.new BufferedItemBridgeBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public void updateTransport(Building other)
            {
                CoreDispatch.updateTransport(this, other, () -> super.updateTransport(other));
            }

            @Override
            public void doDump()
            {
                CoreDispatch.doDump(this, super::doDump);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }

            @Override
            public int acceptStack(Item item, int amount, Teamc source)
            {
                return CoreDispatch.acceptStack(this, item, amount, source, () -> super.acceptStack(item, amount, source));
            }

            @Override
            public void handleStack(Item item, int amount, Teamc source)
            {
                CoreDispatch.handleStack(this, item, amount, source, () -> super.handleStack(item, amount, source));
            }
        };
    }

    // endregion

    // region ===== Defense/support =====

    private static void patchOverdrive(OverdriveProjector block)
    {
        block.buildType = () -> block.new OverdriveBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }
        };
    }

    private static void patchMend(MendProjector block)
    {
        block.buildType = () -> block.new MendBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }
        };
    }

    private static void patchForce(ForceProjector block)
    {
        block.buildType = () -> block.new ForceBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }
        };
    }

    private static void patchDirectionalForce(DirectionalForceProjector block)
    {
        block.buildType = () -> block.new DirectionalForceProjectorBuild()
        {
            private final CoreDispatch.StateImpl st = new CoreDispatch.StateImpl();

            public <T> T coreGetState(Object key) { return st.get(key); }
            public <T> T coreGetState(Object key, arc.func.Prov<T> factory) { return st.get(key, factory); }
            public void corePutState(Object key, Object value) { st.put(key, value); }

            @Override
            public void updateTile()
            {
                CoreDispatch.updateTile(this, super::updateTile);
            }

            @Override
            public boolean acceptItem(Building source, Item item)
            {
                return CoreDispatch.acceptItem(this, source, item, () -> super.acceptItem(source, item));
            }

            @Override
            public void handleItem(Building source, Item item)
            {
                CoreDispatch.handleItem(this, source, item, () -> super.handleItem(source, item));
            }

            @Override
            public boolean acceptLiquid(Building source, Liquid liquid)
            {
                return CoreDispatch.acceptLiquid(this, source, liquid, () -> super.acceptLiquid(source, liquid));
            }

            @Override
            public void handleLiquid(Building source, Liquid liquid, float amount)
            {
                CoreDispatch.handleLiquid(this, source, liquid, amount, () -> super.handleLiquid(source, liquid, amount));
            }
        };
    }

    // endregion
}
