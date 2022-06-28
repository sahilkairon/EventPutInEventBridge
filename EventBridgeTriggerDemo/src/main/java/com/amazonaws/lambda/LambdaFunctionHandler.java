package com.amazonaws.lambda;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.lambda.thirdparty.org.json.JSONObject;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.eventbridge.AmazonEventBridge;
import com.amazonaws.services.eventbridge.AmazonEventBridgeClient;
import com.amazonaws.services.eventbridge.model.PutEventsRequest;
import com.amazonaws.services.eventbridge.model.PutEventsRequestEntry;
import com.amazonaws.services.eventbridge.model.PutEventsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<APIGatewayProxyRequest, APIGatewayProxyResponse> {
	private String accessKey = "AKIAYUCMZEDMUPNLM73Q";
	private String secretKey = "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";
	APIGatewayProxyResponse response = new APIGatewayProxyResponse();

    @Override
    public APIGatewayProxyResponse handleRequest(APIGatewayProxyRequest input, Context context) {
        context.getLogger().log("Input: " + input);
        
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
		
	
		
		AmazonEventBridge client = AmazonEventBridgeClient.builder().withRegion(Regions.AP_SOUTH_1).withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
		context.getLogger().log("EventBridge Object created");
		
		PutEventsRequestEntry requestEntry = new PutEventsRequestEntry();
		context.getLogger().log("request Entry object created");
		
//		JSONObject passwordobj = new JSONObject();
//		passwordobj.put("password", "demoPassword");
//		passwordobj.put("userId", "1");
//		String details = passwordobj.toString();
		
		requestEntry.withSource("Lambda-Publish").withDetailType("custom event demo").withDetail("{ \"userId\": \"1\", \"password\": \"abcd\" }").withEventBusName("arn:aws:events:ap-south-1:592866517209:event-bus/EmailSenderBus");
		context.getLogger().log("details added to request entry object");
		
		PutEventsRequest request = new PutEventsRequest();
		request.withEntries(requestEntry);
		PutEventsResult result = client.putEvents(request);
		context.getLogger().log("Event pushed to event bus");
		
		context.getLogger().log(result.toString());


        return null;
    }

}
