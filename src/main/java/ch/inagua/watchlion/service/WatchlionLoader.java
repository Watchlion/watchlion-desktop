package ch.inagua.watchlion.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Environment;
import ch.inagua.watchlion.model.Watchlion;

public class WatchlionLoader {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
	
	private static final String KEY_APP_OS = "os";
	private static final String KEY_APP_ID = "id";
	private static final String KEY_APP_NAME = "name";
	private static final String KEY_APP_BRIEF = "brief";
	private static final String KEY_APP_TYPE = "type";
	private static final String KEY_APP_CATEGORY = "category";
	private static final String KEY_APP_URL = "url";
	private static final String KEY_APP_INSTRUCTIONS = "instructions";
	private static final String KEY_APP_INSTALL = "install";
	private static final String KEY_APP_REQUIRED = "required";
	private static final String KEY_APP_USERNAME = "username";
	private static final String KEY_APP_SERIAL = "serial";
	private static final String KEY_APP_UPDATE = "update";
	private static final String KEY_APP_IGNORED = "ignored";
	
	private static final String KEY_ENV_APPLICATIONS = "applications";
	private static final String KEY_ENV_UPDATE = "update";
	private static final String KEY_ENV_VERSION = "version";
	private static final String KEY_ENV_PROTOCOL = "protocol";
	
	private static final String KEY_OS_ID = "id";
	private static final String KEY_OS_ID_MACOSX = "macosx";
	private static final String KEY_OS_PLUGINS = "plugins";
	private static final String KEY_OS_VERSIONS = "versions";

	private static final String KEY_VERSION_ID = "id";
	private static final String KEY_VERSION_NAME = "name";
	private static final String KEY_VERSION_UPDATE = "update";
	private static final String KEY_VERSION_INSTRUCTIONS = "instructions";
	private static final String KEY_VERSION_INSTALL = "install";
	// private static final String KEY_VERSION_INSTALLED = "installed";
	private static final String KEY_VERSION_IGNORED = "ignored";

	// TODO PROPERTY 5
	public Environment loadFromJSONFile(String jsonFilePath) throws IOException, ParseException, java.text.ParseException {
		String json = loadFileContent(jsonFilePath);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(json);
		JSONObject jsonObject = (JSONObject) obj;

		Environment environment = new Environment();
		environment.setPath(jsonFilePath);
		if (jsonObject.get(KEY_ENV_PROTOCOL) != null) {
			environment.setProtocol((String) jsonObject.get(KEY_ENV_PROTOCOL));
		}
		if (jsonObject.get(KEY_ENV_VERSION) != null) {
			environment.setVersion((String) jsonObject.get(KEY_ENV_VERSION));
		}
		if (jsonObject.get(KEY_ENV_UPDATE) != null) {
			environment.setUpdateDate(toDate(jsonObject.get(KEY_ENV_UPDATE)));
		}
		{
			JSONArray jsonApplications = (JSONArray) jsonObject.get(KEY_ENV_APPLICATIONS);
			if (jsonApplications != null) {
				for (int i = 0; i < jsonApplications.size(); i++) {
					JSONObject jsonApp = (JSONObject) jsonApplications.get(i);
					Application app = new Application();
					app.setId((String) jsonApp.get(KEY_APP_ID));
					app.setName((String) jsonApp.get(KEY_APP_NAME));
					app.setBrief((String) jsonApp.get(KEY_APP_BRIEF));
					app.setURL((String) jsonApp.get(KEY_APP_URL));
					app.setType((String) jsonApp.get(KEY_APP_TYPE));
					app.setCategory((String) jsonApp.get(KEY_APP_CATEGORY));
					app.setInstructions((String) jsonApp.get(KEY_APP_INSTRUCTIONS));
					app.setInstall((String) jsonApp.get(KEY_APP_INSTALL));
					// app.setRequired((String) jsonApp.get(KEY_APP_REQUIRED));
					app.setUsername((String) jsonApp.get(KEY_APP_USERNAME));
					app.setSerial((String) jsonApp.get(KEY_APP_SERIAL));
					app.setUpdateDate(toDate(jsonApp.get(KEY_APP_UPDATE)));
					Boolean appIgnored = (Boolean) jsonApp.get(KEY_APP_IGNORED);
					if (appIgnored != null) { app.setIgnored(appIgnored); }
					environment.addApplication(app);

					JSONArray jsonOss = (JSONArray) jsonApp.get(KEY_APP_OS);
					if (jsonOss != null) {
						for (int j = 0; j < jsonOss.size(); j++) {
							JSONObject jsonOs = (JSONObject) jsonOss.get(j);
							if (KEY_OS_ID_MACOSX.endsWith((String) jsonOs.get(KEY_OS_ID))) {

								JSONArray jsonVersions = (JSONArray) jsonOs.get(KEY_OS_VERSIONS);
								if (jsonVersions != null) {
									for (int k = 0; k < jsonVersions.size(); k++) {
										JSONObject jsonVersion = (JSONObject) jsonVersions.get(k);
										Application.Version version = new Application.Version();
										version.setId((String) jsonVersion.get(KEY_VERSION_ID));
										version.setName((String) jsonVersion.get(KEY_VERSION_NAME));
										version.setUpdateDate(toDate(jsonVersion.get(KEY_VERSION_UPDATE)));
										version.setInstructions((String) jsonVersion.get(KEY_VERSION_INSTRUCTIONS));
										version.setInstall((String) jsonVersion.get(KEY_VERSION_INSTALL));
										Boolean ignored = (Boolean) jsonVersion.get(KEY_VERSION_IGNORED);
										if (ignored != null) { version.setIgnored(ignored); }
										app.addVersion(version);
									}
								}

								JSONArray jsonPlugins = (JSONArray) jsonOs.get(KEY_OS_PLUGINS);
								if (jsonPlugins != null) {
									for (int k = 0; k < jsonPlugins.size(); k++) {
										JSONObject jsonPlugin = (JSONObject) jsonPlugins.get(k);
										Application.Version plugin = new Application.Version();
										plugin.setId((String) jsonPlugin.get(KEY_VERSION_ID));
										plugin.setName((String) jsonPlugin.get(KEY_VERSION_NAME));
										plugin.setUpdateDate(toDate(jsonPlugin.get(KEY_VERSION_UPDATE)));
										plugin.setInstructions((String) jsonPlugin.get(KEY_VERSION_INSTRUCTIONS));
										plugin.setInstall((String) jsonPlugin.get(KEY_VERSION_INSTALL));
										Boolean ignored = (Boolean) jsonPlugin.get(KEY_VERSION_IGNORED);
										if (ignored != null) {
											plugin.setIgnored(ignored);
										}
										app.addPlugin(plugin);
									}
								}
							}
						}
					}
				}
			}
		}
		return environment;
	}

	static Date toDate(Object object) throws java.text.ParseException {
		if (object == null) {
			return null;
		}
		String source = (String) object;
		return DATE_FORMAT.parse(source);
	}

	static String toString(Date date) {
		if (date == null) {
			return null;
		}
		return DATE_FORMAT.format(date);
	}

	public void save(Watchlion watchlion) throws IOException {
		saveToJSONFile(watchlion.getReference());
		saveToJSONFile(watchlion.getLocal());
	}

	// TODO PROPERTY 6
	public void saveToJSONFile(Environment environment) throws IOException {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(KEY_ENV_PROTOCOL, environment.getProtocol());
		jsonObject.put(KEY_ENV_VERSION, environment.getVersion());
		jsonObject.put(KEY_ENV_UPDATE, toString(environment.getUpdateDate()));

		{
			JSONArray jsonApplications = new JSONArray();
			jsonObject.put(KEY_ENV_APPLICATIONS, jsonApplications);

			for (Application app : environment.getApplications()) {
				JSONObject jsonApp = new JSONObject();
				jsonApplications.add(jsonApp);
				jsonApp.put(KEY_APP_ID, app.getId());
				jsonApp.put(KEY_APP_NAME, app.getName());
				jsonApp.put(KEY_APP_INSTRUCTIONS, app.getInstructions());

				jsonApp.put(KEY_APP_BRIEF, app.getBrief());
				jsonApp.put(KEY_APP_URL, app.getURL());
				jsonApp.put(KEY_APP_TYPE, app.getType());
				jsonApp.put(KEY_APP_CATEGORY, app.getCategory());
				jsonApp.put(KEY_APP_INSTALL, app.getInstall());
				// jsonApp.put(KEY_APP_REQUIRED, app.getRequired());
				jsonApp.put(KEY_APP_USERNAME, app.getUsername());
				jsonApp.put(KEY_APP_SERIAL, app.getSerial());
				jsonApp.put(KEY_APP_UPDATE, toString(app.getUpdateDate()));
				jsonApp.put(KEY_APP_IGNORED, app.isIgnored());
				
				{
					JSONArray jsonOss =  new JSONArray();
					jsonApp.put(KEY_APP_OS, jsonOss);

					{
						JSONObject jsonMacosx =  new JSONObject();
						jsonOss.add(jsonMacosx);

						jsonMacosx.put(KEY_OS_ID, KEY_OS_ID_MACOSX);
						{
							JSONArray jsonVersions =  new JSONArray();
							jsonMacosx.put(KEY_OS_VERSIONS, jsonVersions);
							for (Application.Version version : app.getVersions()) {
								JSONObject jsonVersion =  new JSONObject();
								jsonVersions.add(jsonVersion);
								jsonVersion.put(KEY_VERSION_ID, version.getId());
								jsonVersion.put(KEY_VERSION_NAME, version.getName());
								jsonVersion.put(KEY_VERSION_UPDATE, toString(version.getUpdateDate()));

								jsonVersion.put(KEY_VERSION_INSTRUCTIONS, version.getInstructions());
								jsonVersion.put(KEY_VERSION_INSTALL, version.getInstall());
								jsonVersion.put(KEY_VERSION_IGNORED, version.isIgnored());
							}

							JSONArray jsonPlugins =  new JSONArray();
							jsonMacosx.put(KEY_OS_PLUGINS, jsonPlugins);
							for (Application.Version plugin : app.getPlugins()) {
								JSONObject jsonPlugin =  new JSONObject();
								jsonPlugins.add(jsonPlugin);
								jsonPlugin.put(KEY_VERSION_ID, plugin.getId());
								jsonPlugin.put(KEY_VERSION_NAME, plugin.getName());
								jsonPlugin.put(KEY_VERSION_UPDATE, toString(plugin.getUpdateDate()));

								jsonPlugin.put(KEY_VERSION_INSTRUCTIONS, plugin.getInstructions());
								jsonPlugin.put(KEY_VERSION_INSTALL, plugin.getInstall());
								jsonPlugin.put(KEY_VERSION_IGNORED, plugin.isIgnored());
							}
						}
					}
				}
			}
		}

		saveIntoFile(environment.getPath(), jsonObject);
	}

	private String loadFileContent(String jsonFilePath) throws IOException {
		String path = jsonFilePath;
		if ("/".equals(jsonFilePath.substring(0, 1))) {
			path = jsonFilePath.substring(1);
		}

		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			throw new IllegalArgumentException("File Not Found: " + path);
		}
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}

	private void saveIntoFile(String jsonFilePath, JSONObject jsonobject) throws IOException {
		String path = jsonFilePath;
		if ("/".equals(jsonFilePath.substring(0, 1))) {
			path = jsonFilePath.substring(1);
		}

		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			throw new IllegalArgumentException("File Not Found: " + path);
		}
		
		backupFile(jsonFilePath);
		
		FileWriter fileWriter = new FileWriter(path);
		fileWriter.write(jsonobject.toJSONString());
		fileWriter.flush();
		fileWriter.close();
	}

	private void backupFile(String filePath) {
		// import org.apache.commons.io.FilenameUtils;
		String pathWithoutExtension = FilenameUtils.removeExtension(filePath);
		String extension = FilenameUtils.getExtension(filePath);
		String newPath = pathWithoutExtension + "-" + new Date().getTime() + "." + extension;

		File file = new File(filePath);
		if (!file.renameTo(new File(newPath))) {
			throw new IllegalStateException("Unable to backup " + filePath + " into " +newPath);
		}
	}

}
