/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.springframework.cloud.gateway.handler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.test.BaseWebClientTests;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext
public class RoutePredicateHandlerMappingIntegrationTests extends BaseWebClientTests {

	private static int managementPort;

	@BeforeClass
	public static void beforeClass() {
		managementPort = SocketUtils.findAvailableTcpPort();
		System.setProperty("management.server.port", String.valueOf(managementPort));
	}

	@AfterClass
	public static void afterClass() {
		System.clearProperty("management.server.port");
	}

	@Test
	public void requestsToManagementPortReturn404() {
		testClient.mutate().baseUrl("http://localhost:"+managementPort).build()
				.get().uri("/get")
				.exchange()
				.expectStatus().isNotFound();
	}

	@EnableAutoConfiguration
	@SpringBootConfiguration
	@Import(DefaultTestConfig.class)
	public static class TestConfig { }

}
