package leviathan143.polar.client;

import leviathan143.polar.client.render.entities.RenderAnomaly;
import leviathan143.polar.common.AbstractProxy;
import leviathan143.polar.common.entities.anomalies.EntityAnomaly;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends AbstractProxy 
{	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityAnomaly.class, rm -> new RenderAnomaly(rm));
		ModelLoaderRegistry.registerLoader(ModelStabilisedBlock.ModelStabilisedBlockLoader.INSTANCE);
	}
}
