package com.sometest.endpoint;

import com.google.gson.Gson;

public abstract class AbstractEndpoint {

	protected static final String API_VERSION = "/v1";
	protected static final Gson gson = new Gson();

	public abstract void initPaths();

}
