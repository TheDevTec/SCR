package me.DevTec.ServerControlReloaded.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.thapiutils.LoaderClass;

public class Metrics {

	static {
		// You can use the property to disable the check in your test environment
		if (System.getProperty("bstats.relocatecheck") == null
				|| !System.getProperty("bstats.relocatecheck").equals("false")) {
			// Maven's Relocate is clever and changes strings, too. So we have to use this
			// little "trick" ... :D
			final String defaultPackage = new String(
					new byte[] { 'o', 'r', 'g', '.', 'b', 's', 't', 'a', 't', 's', '.', 'b', 'u', 'k', 'k', 'i', 't' });
			final String examplePackage = new String(
					new byte[] { 'y', 'o', 'u', 'r', '.', 'p', 'a', 'c', 'k', 'a', 'g', 'e' });
			// We want to make sure nobody just copy & pastes the example and use the wrong
			// package names
			if (Metrics.class.getPackage().getName().equals(defaultPackage)
					|| Metrics.class.getPackage().getName().equals(examplePackage)) {
				throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
			}
		}
	}

	// The version of this bStats class
	public static final int B_STATS_VERSION = 1;

	// The url to which the data is sent
	private static final String URL = "https://bStats.org/submitData/bukkit";

	// Is bStats enabled on this server?
	private boolean enabled;

	// The uuid of the server
	private static String serverUUID;

	// A list with all custom charts
	private final List<CustomChart> charts = new ArrayList<>();

	public Metrics() {
		// Load the data
		enabled = true;
		serverUUID = UUID.randomUUID().toString();

		if (enabled) {
			boolean found = false;
			// Search for all other bStats Metrics classes to see if we are the first one
			for (Class<?> service : Bukkit.getServicesManager().getKnownServices()) {
				try {
					service.getField("B_STATS_VERSION"); // Our identifier :)
					found = true; // We aren't the first
					break;
				} catch (NoSuchFieldException ignored) {
				}
			}
			// Register our service
			Bukkit.getServicesManager().register(Metrics.class, this, LoaderClass.plugin, ServicePriority.Normal);
			if (!found) {
				// We are the first!
				startSubmitting();
			}
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void addCustomChart(CustomChart chart) {
		if (chart == null) {
			throw new IllegalArgumentException("Chart cannot be null!");
		}
		charts.add(chart);
	}

	private int t;
	public int getTask() {
		return t;
	}
	
	private void startSubmitting() {
		t=new Tasker() {
			public void run() {
				if (!LoaderClass.plugin.isEnabled()) {
					cancel();
					return;
				}
				submitData();
			}
		}.runRepeating(20*60, 20*60*5);
	}

	@SuppressWarnings("unchecked")
	public JSONObject getPluginData() {
		JSONObject data = new JSONObject();
		data.put("pluginName", "ServerControlReloaded"); // Append the name of the plugin
	    data.put("id", 10560); // Append the id of the plugin
		data.put("pluginVersion", LoaderClass.plugin.getDescription().getVersion()); // Append the version of the plugin
		JSONArray customCharts = new JSONArray();
		for (CustomChart customChart : charts) {
			// Add the data of the custom charts
			JSONObject chart = customChart.getRequestJsonObject();
			if (chart == null) { // If the chart is null, we skip it
				continue;
			}
			customCharts.add(chart);
		}
		data.put("customCharts", customCharts);
		return data;
	}

	@SuppressWarnings("unchecked")
	private JSONObject getServerData() {
		// Minecraft specific data
		int playerAmount = TheAPI.getOnlineCount();
		int onlineMode = Bukkit.getOnlineMode() ? 1 : 0;
		String bukkitVersion = Bukkit.getVersion();

		// OS/Java specific data
		String javaVersion = System.getProperty("java.version");
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String osVersion = System.getProperty("os.version");
		int coreCount = Runtime.getRuntime().availableProcessors();

		JSONObject data = new JSONObject();

		data.put("serverUUID", serverUUID);
		data.put("playerAmount", playerAmount);
		data.put("onlineMode", onlineMode);
		data.put("bukkitVersion", bukkitVersion);
		data.put("javaVersion", javaVersion);
		data.put("osName", osName);
		data.put("osArch", osArch);
		data.put("osVersion", osVersion);
		data.put("coreCount", coreCount);
		return data;
	}

	@SuppressWarnings("unchecked")
	private void submitData() {
		final JSONObject data = getServerData();
		JSONArray pluginData = new JSONArray();
		// Search for all other bStats Metrics classes to get their plugin data
		for (Class<?> service : Bukkit.getServicesManager().getKnownServices()) {
			try {
				service.getField("B_STATS_VERSION"); // Our identifier :)
				for (RegisteredServiceProvider<?> provider : Bukkit.getServicesManager().getRegistrations(service)) {
					try {
						pluginData.add(provider.getService().getMethod("getPluginData").invoke(provider.getProvider()));
					} catch (NullPointerException | NoSuchMethodException | IllegalAccessException
							| InvocationTargetException ignored) {
					}
				}
			} catch (NoSuchFieldException ignored) {
			}
		}

		data.put("plugins", pluginData);

		// Create a new thread for the connection to the bStats server
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Send the data
					sendData(LoaderClass.plugin, data);
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private static void sendData(Plugin plugin, JSONObject data) throws Exception {
		if (data == null) {
			throw new IllegalArgumentException("Data cannot be null!");
		}
		if (Bukkit.isPrimaryThread()) {
			throw new IllegalAccessException("This method must not be called from the main thread!");
		}
		HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();

		// Compress the data to save bandwidth
		byte[] compressedData = compress(data.toString());

		// Add headers
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Accept", "application/json");
		connection.addRequestProperty("Connection", "close");
		connection.addRequestProperty("Content-Encoding", "gzip"); // We gzip our request
		connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
		connection.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
		connection.setRequestProperty("User-Agent", "MC-Server/" + B_STATS_VERSION);

		// Send data
		connection.setDoOutput(true);
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.write(compressedData);
		outputStream.flush();
		outputStream.close();

		InputStream inputStream = connection.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			builder.append(line);
		}
		bufferedReader.close();
	}

	private static byte[] compress(final String str) throws IOException {
		if (str == null) {
			return null;
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
		gzip.write(str.getBytes(StandardCharsets.UTF_8));
		gzip.close();
		return outputStream.toByteArray();
	}

	/**
	 * Represents a custom chart.
	 */
	public static abstract class CustomChart {

		// The id of the chart
		final String chartId;

		/**
		 * Class constructor.
		 *
		 * @param chartId The id of the chart.
		 */
		CustomChart(String chartId) {
			if (chartId == null || chartId.isEmpty()) {
				throw new IllegalArgumentException("ChartId cannot be null or empty!");
			}
			this.chartId = chartId;
		}

		@SuppressWarnings("unchecked")
		private JSONObject getRequestJsonObject() {
			JSONObject chart = new JSONObject();
			chart.put("chartId", chartId);
			try {
				JSONObject data = getChartData();
				if (data == null) {
					return null;
				}
				chart.put("data", data);
			} catch (Throwable t) {
				return null;
			}
			return chart;
		}

		protected abstract JSONObject getChartData() throws Exception;

	}
}