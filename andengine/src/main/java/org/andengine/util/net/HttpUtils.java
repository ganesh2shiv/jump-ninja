package org.andengine.util.net;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 19:20:32 - 03.05.2013
 */
public final class HttpUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private HttpUtils() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static List<NameValuePair> convertParametersToNameValuePairs(final Map<String, String> pParameters) {
		final List<NameValuePair> result = new ArrayList<NameValuePair>();

		for (final Entry<String, String> entry : pParameters.entrySet()) {
			result.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		return result;
	}

	public static boolean isHttpStatusCodeSuccess(final int pHttpStatusCode) {
		return pHttpStatusCode >= HttpStatus.SC_OK && pHttpStatusCode < HttpStatus.SC_MULTIPLE_CHOICES;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
