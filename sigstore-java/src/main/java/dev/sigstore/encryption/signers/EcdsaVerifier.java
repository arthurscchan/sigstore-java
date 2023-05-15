/*
 * Copyright 2022 The Sigstore Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.sigstore.encryption.signers;

import java.security.*;

/** ECDSA verifier, instantiated by {@link Verifiers#newVerifier(PublicKey)}. */
public class EcdsaVerifier implements Verifier {

  private final PublicKey publicKey;

  EcdsaVerifier(PublicKey publicKey) {
    this.publicKey = publicKey;
  }

  @Override
  public PublicKey getPublicKey() {
    return publicKey;
  }

  @Override
  public boolean verify(byte[] artifact, byte[] signature)
      throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    var verifier = Signature.getInstance("SHA256withECDSA");
    verifier.initVerify(publicKey);
    verifier.update(artifact);
    return verifier.verify(signature);
  }

  @Override
  public boolean verifyDigest(byte[] digest, byte[] signature)
      throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    if (digest.length > 64) {
      throw new SignatureException("Artifact digest cannot be longer than 64 bytes for this mode");
    }
    var verifier = Signature.getInstance("NONEwithECDSA");
    verifier.initVerify(publicKey);
    verifier.update(digest);
    return verifier.verify(signature);
  }
}
