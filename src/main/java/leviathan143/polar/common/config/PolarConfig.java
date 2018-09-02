package leviathan143.polar.common.config;

import daomephsta.umbra.TimeOfDay;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import leviathan143.polar.common.Polar;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Polar.MODID, category = "")
public class PolarConfig
{
	public static Anomalies anomalies = new Anomalies();
	
	public static class Anomalies
	{
		@Name("Spawning Time")
		public int spawnTime = TimeOfDay.SUNSET.getTicks();
		@Name("Minimum Spawn Percentage")
		public float minChunkPercentage = 0.1F;
		@Name("Maximum Spawn Percentage")
		public float maxChunkPercentage = 0.2F;
		@Name("Minimum Spawn Y")
		public int minSpawnY = 0;
		@Name("Maximum Spawn Y")
		public int maxSpawnY = 256;
		@Name("Maximum Anomaly Count")
		public int maxAnomalyCount = 50;
		@Name("Dimension Blacklist")
		public int[] __dimBlackList = {};
		private IntSet dimBlackList;
		
		public boolean isDimensionBlacklisted(int dimID)
		{
			return dimBlackList.contains(dimID);
		}
	}
	
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent e)
	{
		if (e.getModID().equals(Polar.MODID))
		{
			ConfigManager.sync(Polar.MODID, Type.INSTANCE);
			finishLoading();
		}
	}

	public static void finishLoading()
	{
		anomalies.dimBlackList = new IntArraySet(anomalies.__dimBlackList);
	}
}
