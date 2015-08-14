package ch.inagua.watchlion.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Watchlion;

public class WatchlionLoader {

	public Watchlion loadFromJSONFile(String jsonFilePath) throws IOException, ParseException {
		String json = loadFileContent(jsonFilePath);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(json);
		JSONObject jsonObject = (JSONObject) obj;

		Watchlion watchlion = new Watchlion();
		// watchlion.setSource(jsonFilePath);
		watchlion.setProtocol((String) jsonObject.get("protocol"));
		watchlion.setVersion((String) jsonObject.get("version"));
		watchlion.setUpdate((String) jsonObject.get("update"));
		{
			JSONArray jsonApplications = (JSONArray) jsonObject.get("applications");
			 if (jsonApplications != null) {
				 for (int i = 0; i < jsonApplications.size(); i++) {
					 JSONObject jsonApp = (JSONObject) jsonApplications.get(i);
					 Application app = new Application();
					 app.setId((String) jsonApp.get("id"));
					 app.setName((String) jsonApp.get("name"));
					 app.setInstructions((String) jsonApp.get("instructions"));
					 watchlion.addApplication(app);
					 
					 JSONArray jsonOss = (JSONArray) jsonApp.get("os");
					 if (jsonOss != null) {
						 for (int j = 0; j < jsonOss.size(); j++) {
							 JSONObject jsonOs = (JSONObject) jsonOss.get(j);
							 if ("macosx".endsWith((String)jsonOs.get("id"))) {
								 JSONArray jsonVersions = (JSONArray) jsonOs.get("versions");
								 if (jsonVersions != null) {
									 for (int k = 0; k < jsonVersions.size(); k++) {
										 JSONObject jsonVersion = (JSONObject) jsonVersions.get(k);
										 Application.Version version = new Application.Version();
										 version.setId((String) jsonVersion.get("id"));
										 version.setName((String) jsonVersion.get("name"));
										 version.setUpdate((String) jsonVersion.get("update"));
										 app.addVersion(version);
									 }
								 }					 
							 }					 
						 }
					 }					 
				 }
			 }
		// watchlion.setSubversionStartRevision((String) subversionObject.get("startRevision"));
		// watchlion.setSubversionStartDate((String) subversionObject.get("startDate"));
		}
		// {
		// JSONObject qcObject = (JSONObject) jsonObject.get("qc");
		// watchlion.setQcOwner((String) qcObject.get("owner"));
		// watchlion.setQcNumber((String) qcObject.get("number"));
		// }
		// {
		// JSONObject reportObject = (JSONObject) jsonObject.get("report");
		// {
		// JSONObject revisionsObject = (JSONObject) reportObject.get("revisions");
		// watchlion.setReportRevisionsWithAuthor(TRUE.equals((String) revisionsObject.get("withAuthor")));
		// watchlion.setReportRevisionsWithPaths(TRUE.equals((String) revisionsObject.get("withPaths")));
		// watchlion.setReportRevisionsNumbers((String) revisionsObject.get("numbers"));
		// }
		// {
		// JSONObject pathsObject = (JSONObject) reportObject.get("paths");
		// watchlion.setReportPathsLevel((String) pathsObject.get("level"));
		// watchlion.setReportPathsPatternFilter((String) pathsObject.get("patternFilter"));
		// }
		// {
		// JSONObject postponeObject = (JSONObject) reportObject.get("postpone");
		// watchlion.setReportPostponeFromRelease((String) postponeObject.get("fromRelease"));
		// watchlion.setReportPostponeToRelease((String) postponeObject.get("toRelease"));
		// watchlion.setReportPostponeWithFile(TRUE.equals((String) postponeObject.get("withFile")));
		// }
		// }

		return watchlion;
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

}
