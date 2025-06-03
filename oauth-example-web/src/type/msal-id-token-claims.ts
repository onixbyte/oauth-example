export interface MsalIdTokenClaims {
  /**
   * The user's full name as provided by the identity provider.
   */
  name: string

  /**
   * A unique cryptographic nonce value used to associate a client session with an ID token,
   * and to mitigate replay attacks.
   */
  nonce: string

  /**
   * The immutable, unique identifier of the user object in the identity provider.
   * Often called the user's OpenID.
   */
  oid: string

  /**
   * A preferred username, usually an email address or user principal name.
   */
  preferred_username: string
}
