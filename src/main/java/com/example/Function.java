package com.example;

import java.sql.Timestamp;
import java.util.Optional;

import com.azure.storage.file.share.ShareFileClient;
import com.azure.storage.file.share.ShareFileClientBuilder;
import com.azure.storage.file.share.models.ShareFileInfo;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
	/**
	 * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
	 * 1. curl -d "HTTP Body" {your host}/api/HttpExample
	 * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
	 */

	String connectionString = "DefaultEndpointsProtocol=https;AccountName=gaukstorage123;AccountKey=/xxx==;EndpointSuffix=core.windows.net";
	String sasToken = "https://gaukstorage123.file.core.windows.net/?sv=2019-12-12&ss=f&srt=sco&sp=rwdlc&se=2020-08-31T12:20:42Z&st=2020-08-10T04:20:42Z&spr=https,http&sig=xxx%3D";
	String endpoint = "https://gaukstorage123.file.core.windows.net/";


	@FunctionName("HttpExample")
	public HttpResponseMessage run(
			@HttpTrigger(
					name = "req",
					methods = {HttpMethod.GET, HttpMethod.POST},
					authLevel = AuthorizationLevel.ANONYMOUS)
			HttpRequestMessage<Optional<String>> request,
			final ExecutionContext context) {
		context.getLogger().info("Java HTTP trigger processed a request.");

		context.getLogger().info("creating fileclient");
		ShareFileClient fileClient = new ShareFileClientBuilder()
				.connectionString(connectionString)
				.endpoint(endpoint)
				.shareName("testfs")
				.resourcePath("myresource1234")
				.buildFileClient();

		context.getLogger().info("created client");

		ShareFileInfo response = null;

		context.getLogger().info("checking file client exists");
		if (fileClient != null) {
		//if (true) {
			context.getLogger().info("yes, file client exists");
			try {
				context.getLogger().info("trying to create a file");
				response = fileClient.create(1024);
				System.out.println("Complete creating the file.");

			} catch (Exception ioe) {
				System.out.println("There was an exception...");
				ioe.printStackTrace();
			}

		} else {
			//errorMsg = 'File Already Exists';
			System.out.println("File already exists...");
		}

		if (response == null) {
			return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("response is null").build();
		} else {
			return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + response.toString()).build();
		}


	}
}
