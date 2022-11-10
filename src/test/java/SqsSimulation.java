import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.jms.JmsProtocolBuilder;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.xpath;
import static io.gatling.javaapi.jms.JmsDsl.jms;


public class SqsSimulation extends Simulation {

    String queueUrl = "https://sqs.us-east-1.amazonaws.com/569806434804/rev-02-pricing-service-price-bill-performance-test.fifo";
    String messageBody ="'timestamp':'2021-08-26T07:18:45.410+0000','traceId':'99967be0-d574-40dc-9c19-799634a2e1b9','eventType':'CLIENT_BILL','payloadSchemaVersion':1,'eventAction':'CREATE','payload':{'clientBillId':'153521','professionalId':'8191','clientId':'3166','contractualManagementFee':349.8026700000001,'calculatedManagementFee':349.8026700000001,'status':'READY_TO_VALIDATE','billType':'REGULAR','documentType':'Invoice','invoiceNumber':'12345','billingCycle':{'accountingId':'02-2021','month':0,'year':2020},'invoiceReleaseDate':'2021-02-01T05:00:00.000+0000',invoiceUrl':'abc.pdf','workflow':{'dateApproved':'2021-02-01T05:00:00.000+0000','dateDrafted':'2021-02-01T05:00:00.000+0000','dateValidated':'2021-02-01T05:00:00.000+0000','datePaid':'2021-02-01T05:00:00.000+0000'},'managementFeeRateTableDetails':{'managementFeeProfessionalCount':1,'rateTableTier':1,'rateTableLowerProfessionalCount':1,'rateTableUpperProfessionalCount':null,'rateTableMinTotalValue':200,'rateTableMaxTotalValue':null,'rateTableDefaultManagementFeePct':0.2},'transactions':[{'clientBillTransactionId':'12345','amount':850,'paidAmount':350.25,'currency':'EUR','categoryType':'PROFESSIONAL','transactionTypeId':'2','targetCurrency':'CAD','targetFxRate':1.2201,'targetFxRateDate':'2021-02-01T05:00:00.000+0000','payPeriod':'1','reference':'2246759000002463209'}],'calculatedFeeCurrency':'USD','legacy':true,'fxRate':0.257,'fxRateDate':'2022-01-01T13:00:00.000+0000','legacyInvoiceFileMsaAcumaticaSiloId':'123'},'originService':'gp-go-global'}";

    String key_id = "ASIAYJKY4GH2KS7YWWWU";
    String key= "rEl7tqx0IzDH/kfPwQW/+IpfMELBrQKGBpF/A1ee";
    String token = "IQoJb3JpZ2luX2VjEOH//////////wEaCXVzLWVhc3QtMSJHMEUCIQCA2MYk6/8G8qo6Q/r/ick62eAZpwDO/qoKubb6XuoxTAIgL8gBRCNVMVDQ6hEl300NVBbXD+gd3R9las+05PnhXy0qqAMIShACGgw1Njk4MDY0MzQ4MDQiDFsMZz9lU3JPeI6xhCqFA7wWmOg0Jq1IzwaU/5yPE9Is8Sm24OyiDhs6kFR74RAYKwEaF6y69UcyAgmjl3sZQhw3O13u4f8bf6ysuBc6z12duqvNVGcyY/qE7fXA2xTDXU8CysmXrCTGnUl1JveAlyGKLmXmve9MkuX7hzhKm9Uf9tDuZtWJn1eOye3mYHcSFjunv9gYAYvKQWAzqmf9ZbSMnZr6tp8THOnut43hcTd02WzH4fpdRmOoY6JU0ypQmhHdZ1hisHQa/uze297wIVPWYRvCihIJApFFY3s5LMHiDLlSMUyZVQUsU/dHpAUSVSA6BJ4Lkcq95L6f2Xp5SV3t8o6160CIKpDGJq+OcQKaL5kx0b9gpzYlKPviEMuNRssiU9byRTwFlKRpJ4p5dJ9RNkBagwSVDVqY0pHP+eMd1Mu2MiS4g4XO0HlwHkDXPr4VovfAXfdT9pquacD3mZPSCDML9YlXuf1QouVQD8G8q05eUEH8IjMenQVFLj0NnTfOe5YAmnHpS7u4V0aLT83NsYXkMNW+z5cGOqYBZMfwAOcPZk+woM9x+Cv0izN9+XPM1eLwvrAEmIcIXRWS5K/V0HDWYByvvtWWgcz/+hkCjIBS43le8YbOoW+5NAf7BYVgskZvNilME9bJprVI/kn6BmPElSfiUVrs3/Kbm+nEaYMgqigpXbd0zac6JD1DmTcJfesipYbCF2Qq2jFrSeXrl420qKJeztFH8sBsS1Qcfhr2vb6NZEwk3VVM50fhoAKNCA==";

    BasicSessionCredentials credentialsProvider = new BasicSessionCredentials(key_id, key,token);
    BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(key, token);
//    AWSStaticCredentialsProvider configuration = new AWSStaticCredentialsProvider(credentialsProvider);
    AWSStaticCredentialsProvider configuration = new AWSStaticCredentialsProvider(basicAWSCredentials);


    AmazonSQSAsync builder = AmazonSQSAsyncClientBuilder.standard().withCredentials(configuration).withRegion(Regions.US_EAST_1).build();

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

    SqsClient sqsClient = (SqsClient) builder.sendMessage(sendMessageRequest);
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

    public SqsSimulation(){

        this.setUp(scenario.injectOpen(rampUsersPerSec(2).to(10).during(Duration.ofMinutes(15)))).protocols(jmsProtocol);
    }
}
