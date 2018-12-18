/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.gateway.reactor.handler.reporter;

import io.gravitee.gateway.api.ExecutionContext;
import io.gravitee.gateway.api.handler.Handler;
import io.gravitee.gateway.report.ReporterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class ReporterHandler implements Handler<ExecutionContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReporterHandler.class);

    private final ReporterService reporterService;

    public ReporterHandler(final ReporterService reporterService) {
        this.reporterService = reporterService;
    }

    @Override
    public void handle(ExecutionContext context) {
        try {
            reporterService.report(context.request().metrics());

            if (context.request().metrics().getLog() != null) {
                reporterService.report(context.request().metrics().getLog());

                // Dispose the log reference since it must not be used anymore
                context.request().metrics().setLog(null);
            }
        } catch (Exception ex) {
            LOGGER.error("An error occurs while reporting metrics", ex);
        }
    }
}
