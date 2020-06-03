/*
 * Copyright 2013-2017 the original author or authors.
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

package org.springframework.cloud.gateway.route;

import javax.validation.constraints.NotEmpty;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * @author Spencer Gibb
 */
@Validated
public class RouteDefinition {

	//路由声明的Id
	@NotEmpty
	private String id = UUID.randomUUID().toString();

	//断言集合
	@NotEmpty
	@Valid
	private List<PredicateDefinition> predicates = new ArrayList<>();


	//过滤器声明集合
	@Valid
	private List<FilterDefinition> filters = new ArrayList<>();

	//路由到的URI
	@NotNull
	private URI uri;

	//跳转的顺序
	private int order = 0; //排序

	public RouteDefinition() {}


	void test() {
		RouteDefinition def = new RouteDefinition("UserRoute=/user,Host=www.baidu.com");
	}

	/**
	 * 根据Text创建RouteDefinition
	 * 格式：Id=Uri,Predicates[0],Predicates[1]...Predicates[n]
	 * 例如：MyRoute=http://www.baidu.com,Host=*.searchxxx.com,addrequestparameter.org,Path=/get
	 * @see {@link RouteDefinition#test()}
	 * @param text
	 */
	public RouteDefinition(String text) {
		int eqIdx = text.indexOf('=');
		if (eqIdx <= 0) {
			throw new ValidationException("Unable to parse RouteDefinition text '" + text + "'" +
					", must be of the form name=value");
		}

		setId(text.substring(0, eqIdx));

		String[] args = tokenizeToStringArray(text.substring(eqIdx+1), ",");

		setUri(URI.create(args[0]));

		for (int i=1; i < args.length; i++) {
			this.predicates.add(new PredicateDefinition(args[i]));
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PredicateDefinition> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<PredicateDefinition> predicates) {
		this.predicates = predicates;
	}

	public List<FilterDefinition> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterDefinition> filters) {
		this.filters = filters;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RouteDefinition routeDefinition = (RouteDefinition) o;
		return Objects.equals(id, routeDefinition.id) &&
				Objects.equals(predicates, routeDefinition.predicates) &&
				Objects.equals(order, routeDefinition.order) &&
				Objects.equals(uri, routeDefinition.uri);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, predicates, uri);
	}

	@Override
	public String toString() {
		return "RouteDefinition{" +
				"id='" + id + '\'' +
				", predicates=" + predicates +
				", filters=" + filters +
				", uri=" + uri +
				", order=" + order +
				'}';
	}
}
