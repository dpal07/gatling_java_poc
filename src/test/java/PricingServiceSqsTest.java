import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazon.sqs.javamessaging.message.SQSMessage;
import io.gatling.javaapi.jms.*;
import software.amazon.awssdk.core.internal.http.AmazonAsyncHttpClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import javax.jms.ConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.jms.JmsDsl.*;


public class PricingServiceSqsTest extends Simulation {

    String queueUrl = "https://sqs.us-east-1.amazonaws.com/569806434804/rev-02-pricing-service-price-bill-performance-test.fifo";
    String messageBody ="'timestamp':'2021-08-26T07:18:45.410+0000','traceId':'99967be0-d574-40dc-9c19-799634a2e1b9','eventType':'CLIENT_BILL','payloadSchemaVersion':1,'eventAction':'CREATE','payload':{'clientBillId':'153521','professionalId':'8191','clientId':'3166','contractualManagementFee':349.8026700000001,'calculatedManagementFee':349.8026700000001,'status':'READY_TO_VALIDATE','billType':'REGULAR','documentType':'Invoice','invoiceNumber':'12345','billingCycle':{'accountingId':'02-2021','month':0,'year':2020},'invoiceReleaseDate':'2021-02-01T05:00:00.000+0000',invoiceUrl':'abc.pdf','workflow':{'dateApproved':'2021-02-01T05:00:00.000+0000','dateDrafted':'2021-02-01T05:00:00.000+0000','dateValidated':'2021-02-01T05:00:00.000+0000','datePaid':'2021-02-01T05:00:00.000+0000'},'managementFeeRateTableDetails':{'managementFeeProfessionalCount':1,'rateTableTier':1,'rateTableLowerProfessionalCount':1,'rateTableUpperProfessionalCount':null,'rateTableMinTotalValue':200,'rateTableMaxTotalValue':null,'rateTableDefaultManagementFeePct':0.2},'transactions':[{'clientBillTransactionId':'12345','amount':850,'paidAmount':350.25,'currency':'EUR','categoryType':'PROFESSIONAL','transactionTypeId':'2','targetCurrency':'CAD','targetFxRate':1.2201,'targetFxRateDate':'2021-02-01T05:00:00.000+0000','payPeriod':'1','reference':'2246759000002463209'}],'calculatedFeeCurrency':'USD','legacy':true,'fxRate':0.257,'fxRateDate':'2022-01-01T13:00:00.000+0000','legacyInvoiceFileMsaAcumaticaSiloId':'123'},'originService':'gp-go-global'}";

    AmazonSQS sqs = AmazonSQSAsyncClientBuilder.standard().build();
//    SqsClient sqsClient = (SqsClient) AmazonSQSAsyncClientBuilder.standard().build();


    Map<String, MessageAttributeValue> attributeMap = new HashMap<String, MessageAttributeValue>(){{

        put("profile",new MessageAttributeValue()
                .withDataType("String")
                .withStringValue("Developers_tf-569806434804")
        );
    }};

    SendMessageRequest sendMessageRequest = new SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody(messageBody)
            .withMessageGroupId("1")
            .withMessageAttributes(attributeMap);

    SqsClient sqsClient = (SqsClient) sqs.sendMessage(sendMessageRequest);
    JmsProtocolBuilder jmsProtocol = jms.connectionFactory(
            new SQSConnectionFactory(
                    new ProviderConfiguration(), sqsClient));
    ScenarioBuilder scenario = CoreDsl.scenario("Pricing SQS test")
            .exec(
                    jms("sqs testing").requestReply()
                            .queue("test jms queue")
                            .textMessage(messageBody)
                            .property("profile", "Developers_tf-569806434804")
                            .jmsType("test_type")
                            .check(xpath("//MD5OfMessageBody"))
            );



    @Override
    public void after() {
        super.after();
        sqsClient.close();
    }

    public PricingServiceSqsTest(){

        this.setUp(scenario.injectOpen(rampUsersPerSec(2).to(10).during(Duration.ofMinutes(15)))).protocols(jmsProtocol);
    }
}
