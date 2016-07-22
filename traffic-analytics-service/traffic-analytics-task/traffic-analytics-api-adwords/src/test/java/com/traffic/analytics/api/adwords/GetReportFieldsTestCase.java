package com.traffic.analytics.api.adwords;

import org.junit.Test;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201509.cm.ReportDefinitionField;
import com.google.api.ads.adwords.axis.v201509.cm.ReportDefinitionReportType;
import com.google.api.ads.adwords.axis.v201509.cm.ReportDefinitionServiceInterface;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.client.auth.oauth2.Credential;

/**
 * @author yuhuibin.
 */
public class GetReportFieldsTestCase {

	@Test
    public void testGetReportFields() throws Exception {
        // Generate a refreshable OAuth2 credential.
        Credential oAuth2Credential = new OfflineCredentials.Builder()
                .forApi(Api.ADWORDS)
                .fromFile()
                .build()
                .generateCredential();

        // Construct an AdWordsSession.
        AdWordsSession session = new AdWordsSession.Builder()
                .fromFile()
                .withOAuth2Credential(oAuth2Credential)
                .build();

        AdWordsServices adWordsServices = new AdWordsServices();
        // Get the ReportDefinitionService.
        ReportDefinitionServiceInterface reportDefinitionService =
                adWordsServices.get(session, ReportDefinitionServiceInterface.class);

        // Get report fields.
        ReportDefinitionField[] reportDefinitionFields =
                reportDefinitionService
                        .getReportFields(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT);

        // Display report fields.
        System.out.println("Available fields for report:");

        for (ReportDefinitionField reportDefinitionField : reportDefinitionFields) {
            System.out.printf("\t %s(%s) := [", reportDefinitionField.getFieldName(),
                    reportDefinitionField.getFieldType());
            if (reportDefinitionField.getEnumValues() != null) {
                for (String enumValue : reportDefinitionField.getEnumValues()) {
                    System.out.printf("%s, ", enumValue);
                }
            }
            System.out.println("]");
        }
    }

}
