//_XXXXX_

//eagle/eagle-core/eagle-app/eagle-app-base/src/main/java/org/apache/eagle/app/service/impl/ApplicationHealthCheckEmailPublisher.java
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.eagle.app.service.impl;

import com.codahale.metrics.health.HealthCheck;
import com.typesafe.config.Config;
import org.apache.eagle.alert.engine.publisher.email.AlertEmailConstants;
import org.apache.eagle.alert.engine.publisher.email.EagleMailClient;
import org.apache.eagle.app.service.ApplicationHealthCheckPublisher;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationHealthCheckEmailPublisher implements ApplicationHealthCheckPublisher {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationHealthCheckEmailPublisher.class);
    private static final int MAX_RETRY_COUNT = 3;
    private static final String CONF_MAIL_RECIPIENTS = "mail.smtp.recipients";
    private static final String CONF_MAIL_SENDER = "mail.smtp.sender";
    private static final String CONF_MAIL_SUBJECT = "mail.smtp.subject";
    private static final String CONF_MAIL_CC = "mail.smtp.cc";
    private static final String CONF_MAIL_TEMPLATE = "mail.smtp.template";
    private static final String UNHEALTHY_CONTEXT = "unHealthyContext";
    private static final Integer HEALTH_CHECK_PORT = 9091;
    private static final String SERVICE_HOST = "host";
    private static final String SERVICE_PORT = "port";

    private Config config;

    public ApplicationHealthCheckEmailPublisher(Config config) {
        this.config = config;
    }

    @Override
    public void _XXXXX_(String type, Map<String, HealthCheck.Result> results) {
        if (results.size() == 0) {
            return;
        }
        Properties properties = parseMailClientConfig();
        if (properties == null) {
            return;
        }

        int count = 0;
        boolean success = false;
        while (count++ < MAX_RETRY_COUNT && !success) {
            LOG.info("Sending email, tried: " + count + ", max: " + MAX_RETRY_COUNT);
            try {
                String recipients = config.getString(CONF_MAIL_RECIPIENTS);
                if (recipients == null || recipients.equals("")) {
                    LOG.error("Recipients is null, skip sending emails ");
                    return;
                }

                final VelocityContext context = new VelocityContext();
                Map<String, String> appMsgs = new HashMap<>();
                int unhealthyCount = 0;
                int healthyCount = 0;
                for (String appId : results.keySet()) {
                    appMsgs.put(appId, results.get(appId).getMessage());
                    if (!results.get(appId).isHealthy()) {
                        unhealthyCount++;
                    } else {
                        healthyCount++;
                    }
                }
                Map<String, Object> unHealthyContext = new HashMap<>();
                unHealthyContext.put("appMsgs", appMsgs);
                unHealthyContext.put("appMgmtUrl", "http://" + config.getString(SERVICE_HOST) + ":" + config.getInt(SERVICE_PORT) + "/#/integration/site");
                unHealthyContext.put("healthCheckUrl", "http://" + config.getString(SERVICE_HOST) + ":" + HEALTH_CHECK_PORT + "/healthcheck");
                context.put(UNHEALTHY_CONTEXT, unHealthyContext);

                String subject = "";
                if (healthyCount > 0) {
                    subject += healthyCount + " healthy app(s)";
                }
                if (unhealthyCount > 0) {
                    if (!subject.isEmpty()) {
                        subject += ", ";
                    }
                    subject += unhealthyCount + " unhealthy app(s)";
                }
                subject = config.getString(CONF_MAIL_SUBJECT) + ": " + subject;
                EagleMailClient client = new EagleMailClient(properties);
                String hostname = InetAddress.getLocalHost().getHostName();
                if (!hostname.endsWith(".com")) {
                    //avoid invalid host exception
                    hostname += ".com";
                }
                success = client.send(
                        System.getProperty("user.name") + "@" + hostname,
                        recipients,
                        config.hasPath(CONF_MAIL_CC) ? config.getString(CONF_MAIL_CC) : null,
                        type + subject,
                        config.getString(CONF_MAIL_TEMPLATE),
                        context,
                        null);

                LOG.info("Success of sending email: " + success);
                if (!success && count < MAX_RETRY_COUNT) {
                    LOG.info("Sleep for a while before retrying");
                    Thread.sleep(10 * 1000);
                }
            } catch (Exception e) {
                LOG.warn("Sending mail exception", e);
            }
        }
        if (success) {
            LOG.info("Successfully send unhealthy email");
        } else {
            LOG.warn("Fail sending unhealthy email after tries {} times", MAX_RETRY_COUNT);
        }
    }

    private Properties parseMailClientConfig() {
        Properties props = new Properties();

        String mailHost = config.getString(AlertEmailConstants.CONF_MAIL_HOST);
        int mailPort = config.getInt(AlertEmailConstants.CONF_MAIL_PORT);
        if (mailHost == null || mailPort == 0 || mailHost.isEmpty()) {
            LOG.warn("SMTP server is unset, will exit");
            return null;
        }
        props.put(AlertEmailConstants.CONF_MAIL_HOST, mailHost);
        props.put(AlertEmailConstants.CONF_MAIL_PORT, mailPort);

        Boolean smtpAuth = config.hasPath(AlertEmailConstants.CONF_MAIL_AUTH) && config.getBoolean(AlertEmailConstants.CONF_MAIL_AUTH);
        props.put(AlertEmailConstants.CONF_MAIL_AUTH, smtpAuth);
        if (smtpAuth) {
            props.put(AlertEmailConstants.CONF_AUTH_USER, config.getString(AlertEmailConstants.CONF_AUTH_USER));
            props.put(AlertEmailConstants.CONF_AUTH_PASSWORD, config.getString(AlertEmailConstants.CONF_AUTH_PASSWORD));
        }

        String smtpConn = config.hasPath(AlertEmailConstants.CONF_MAIL_CONN) ? config.getString(AlertEmailConstants.CONF_MAIL_CONN) : AlertEmailConstants.CONN_PLAINTEXT;
        if (smtpConn.equalsIgnoreCase(AlertEmailConstants.CONN_TLS)) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        if (smtpConn.equalsIgnoreCase(AlertEmailConstants.CONN_SSL)) {
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
        }
        props.put(AlertEmailConstants.CONF_MAIL_DEBUG, config.hasPath(AlertEmailConstants.CONF_MAIL_DEBUG) && config.getBoolean(AlertEmailConstants.CONF_MAIL_DEBUG));
        return props;
    }
}
