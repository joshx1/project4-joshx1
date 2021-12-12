package utilities;

import com.google.gson.Gson;
import ServerFramework.TicketServer;
import ServerFramework.TicketServerConstants;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.StringReader;
import java.util.Base64;
import java.util.Map;

/**
 * A utility class with several helper methods.
 */
public class LoginUtilities {

    // for parsing JSON
    private static final Gson gson = new Gson();

    /**
     * Hash the session ID to generate a nonce.
     * Uses Apache Commons Codec
     * See https://www.baeldung.com/sha-256-hashing-java
     * @param sessionId
     * @return
     */
    public static String generateNonce(String sessionId) {
        String sha256hex = DigestUtils.sha256Hex(sessionId);
        return sha256hex;
    }

    /**
     * Generates the URL to make the initial request to the authorize API.
     * @param clientId
     * @param state
     * @param nonce
     * @param redirectURI
     * @return
     */
    public static String generateSlackAuthorizeURL(String clientId, String state, String nonce, String redirectURI) {

        String url = String.format("https://%s/%s?%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s",
            TicketServerConstants.HOST,
            TicketServerConstants.AUTH_PATH,
            TicketServerConstants.RESPONSE_TYPE_KEY,
            TicketServerConstants.RESPONSE_TYPE_VALUE,
            TicketServerConstants.SCOPE_KEY,
            TicketServerConstants.SCOPE_VALUE,
            TicketServerConstants.CLIENT_ID_KEY,
            clientId,
            TicketServerConstants.STATE_KEY,
            state,
            TicketServerConstants.NONCE_KEY,
            nonce,
            TicketServerConstants.REDIRECT_URI_KEY,
            redirectURI
        );
        return url;
    }

    /**
     * Generates the URL to exchange the initial auth for a token.
     * @param clientId
     * @param clientSecret
     * @param code
     * @param redirectURI
     * @return
     */
    public static String generateSlackTokenURL(String clientId, String clientSecret, String code, String redirectURI) {

        String url = String.format("https://%s/%s?%s=%s&%s=%s&%s=%s&%s=%s",
            TicketServerConstants.HOST,
            TicketServerConstants.TOKEN_PATH,
            TicketServerConstants.CLIENT_ID_KEY,
            clientId,
            TicketServerConstants.CLIENT_SECRET_KEY,
            clientSecret,
            TicketServerConstants.CODE_KEY,
            code,
            TicketServerConstants.REDIRECT_URI_KEY,
            redirectURI
        );
        return url;
    }

    /**
     * Convert a JSON-formatted String to a Map.
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStrToMap(String jsonString) {
        Map<String, Object> map = gson.fromJson(new StringReader(jsonString), Map.class);
        return map;
    }

    /**
     * Verify the response from the token API.
     * If successful, returns a ClientInfo object with information about the authed client.
     * Returns null if not successful.
     * @param map
     * @param sessionId
     * @return
     */
    public static ClientInfo verifyTokenResponse(Map<String, Object> map, String sessionId) {

        // verify ok: true
        if(!map.containsKey(TicketServerConstants.OK_KEY) || !(boolean)map.get(TicketServerConstants.OK_KEY)) {
            return null;
        }

        // verify state is the users session cookie id
        if(!map.containsKey(TicketServerConstants.STATE_KEY) || !map.get(TicketServerConstants.STATE_KEY).equals(sessionId)) {
            return null;
        }

        String idToken = (String)map.get("id_token");
        Map<String, Object> payloadMap = decodeIdTokenPayload(idToken);

        //verify nonce
        String expectedNonce = generateNonce(sessionId);
        String actualNonce = (String) payloadMap.get(TicketServerConstants.NONCE_KEY);
        if(!expectedNonce.equals(actualNonce)) {
            return null;
        }

        // extract name from response
        String username = (String) payloadMap.get(TicketServerConstants.NAME_KEY);
        String access_token = (String) map.get(TicketServerConstants.ACCESS_TOKEN_KEY);
        String token_type = (String) map.get(TicketServerConstants.TOKEN_TYPE_KEY);
        String id_token = (String) map.get(TicketServerConstants.ID_TOKEN_KEY);
        String email = (String) payloadMap.get(TicketServerConstants.EMAIL_KEY);
        boolean email_verified = (boolean) payloadMap.get(TicketServerConstants.EMAIL_VERIFIED_KEY);
        return new ClientInfo(username, access_token, token_type, id_token, email, email_verified, null, null);
    }

    /**
     *
     * From the Slack documentation:
     * id_token is a standard JSON Web Token (JWT). You can decode it with off-the-shelf libraries in any programming
     * language, and most packages that handle OpenID will handle JWT decoding.
     *
     * Method decodes the String id_token and returns a Map with the contents of the payload.
     *
     * @param idToken
     * @return
     */
    private static Map<String, Object> decodeIdTokenPayload(String idToken) {
        // Decoding process taken from:
        // https://www.baeldung.com/java-jwt-token-decode
        String[] chunks = idToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        // convert the id_token payload to a map
        Map<String, Object> payloadMap = gson.fromJson(new StringReader(payload), Map.class);
        return payloadMap;
    }

    public static void main(String[] args) {
        System.out.println(generateSlackAuthorizeURL("abc", "123", "def", "url"));
    }
}
