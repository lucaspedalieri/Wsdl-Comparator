package businesslogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

public class Caller {

	public static String readPostRequest(String uri, String object) throws MalformedURLException, Throwable {
		URL url;
		url = new URL(uri);

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		// if (uri.startsWith("https") && context != null && CertName != null &&
		// !CertName.isEmpty())
		// client = new HttpsClient(context, CertName, CertAlias, CertPass,
		// HttpPort, HttpsPort);
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 30000); // Timeout
		StringBuilder arg = new StringBuilder();
		String concat = "";

		HttpPost httpPost = new HttpPost(url.toURI());
		httpPost.setEntity(new StringEntity(object, "UTF-8"));
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpPost.setHeader("Accept-Language", "en-US");
		httpPost.setHeader("Accept", "*/*");

		try {
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				throw new HttpException(builder.toString());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return builder.toString();
	}

	public static InputStream readGetRequest(String uri) throws ClientProtocolException, IOException, HttpException {
		HttpClient client = new DefaultHttpClient();

		// if (uri.startsWith("https") && context != null && CertName != null &&
		// !CertName.isEmpty()) {
		// client = new HttpsClient(context, CertName, CertAlias, CertPass,
		// HttpPort, HttpsPort);
		// }

		HttpConnectionParams.setConnectionTimeout(client.getParams(), 6000); // Timeout

		HttpGet httpGet = new HttpGet(uri);
		HttpResponse response = client.execute(httpGet);

		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		InputStream inputStream = null;
		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

		} else {
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
			throw new HttpException(InputStreamToString(inputStream));
		}
		return inputStream;
	}

	public static String InputStreamToString(InputStream inputStream) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}

}
