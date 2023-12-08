package br.imd.rest;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import br.imd.rest.expections.RestRequestException;

public class HttpUtils {

	//@ spec_public
	private final CloseableHttpClient httpClient;

	//@ requires true;
	//@ ensures httpClient != null;
	public HttpUtils() {
		httpClient = HttpClientBuilder.create().build();
	}

	//@ requires expectStatus >= 100;
	//@ requires expectStatus <= 599;
	//@ requires headerParams != null ==> (\forall String key; headerParams.containsKey(key); key != null);
	//@ requires headerParams != null ==> (\forall String value; headerParams.containsValue(value); value != null);
	//@ requires uri != null;
	//@ ensures \result != null;
	public String httpPostRequest(String uri, Map<String, String> headerParams, String body, int expectStatus) throws RestRequestException {
		try {
			HttpPost request = new HttpPost(uri);

			for (Map.Entry<String, String> entry : headerParams.entrySet()) {
				String header = entry.getKey();
				String value = entry.getValue();
				request.addHeader(header, value);
			}

			if (body != null) {
				StringEntity bodyEntity = new StringEntity(body, "UTF-8");
				request.setEntity(bodyEntity);
			}

			HttpResponse response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() != expectStatus) {
				throw new RestRequestException(response.getStatusLine().getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");

		} catch (IOException e) {
			throw new RestRequestException(e.getMessage());
		}
	}

	//@ requires uri != null;
	//@ signals_only RestRequestException;
	//@ ensures \result != null;
	public String httpGetRequest(String uri, Map<String, String> headerParams) throws RestRequestException {

		try {
			HttpGet request = new HttpGet(uri);

			if (headerParams != null) {
				for (String header : headerParams.keySet()) {
					request.addHeader(header, headerParams.get(header));
				}
			}

			HttpResponse response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RestRequestException(response.getStatusLine().getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");

		} catch (IOException e) {
			throw new RestRequestException(e.getMessage());
		}
	}
}
