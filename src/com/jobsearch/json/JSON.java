package com.jobsearch.json;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public final class JSON {

	/**
	 * Decodes the supplied JSON string to an object of the specified type.
	 */
	public static <T> T parse(final String json, final Class<T> type) {
		try {
			return objectMapper.readValue(json, type);
		} catch (final Exception exception) {
			return null;
		}
	}

	/** Decodes the supplied JSON string to a list of the specified type. */
	public static <T> List<T> parseArray(final String json, final Class<T> type) {
		try {
			return objectMapper.readValue(json,
					objectMapper.getTypeFactory().constructCollectionType(List.class, type));
		} catch (final Exception exception) {
			return null;
		}
	}

	/**
	 * Decodes the supplied JSON string to a map of the specified value type.
	 */
	public static <T> Map<String, T> parseObject(final String json, final Class<T> type) {
		try {
			return objectMapper.readValue(json,
					objectMapper.getTypeFactory().constructMapType(Map.class, String.class, type));
		} catch (final Exception exception) {
			return null;
		}
	}

	/**
	 * Decodes the supplied JSON string to an object of custom type passed to
	 * {@link TypeReference}.
	 */
	public static <T> T parseType(final String json, final TypeReference<T> type) {
		try {
			return objectMapper.readValue(json, type);
		} catch (final Exception exception) {
			return null;
		}
	}

	/** Encodes the supplied object to a JSON string. */
	public static String stringify(final Object value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (final Exception exception) {
			return null;
		}
	}

	/** Decodes the supplied JSON string to a JsonNode tree. */
	public static JsonNode readTree(final String content) {
		try {
			return objectMapper.readTree(content);
		} catch (final Exception exception) {
			return null;
		}
	}

	// --------------------------------------------------------------------------

	private static ObjectMapper objectMapper = new ObjectMapper();

	private JSON() {
	}
}
