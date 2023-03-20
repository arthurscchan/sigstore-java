/*
 * Copyright 2023 The Sigstore Authors.
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
import com.code_intelligence.jazzer.api.FuzzedDataProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import dev.sigstore.encryption.certificates.transparency.Serialization;
import dev.sigstore.encryption.certificates.transparency.SerializationException;

public class SerializationFuzzer {
  public static void fuzzerTestOneInput(FuzzedDataProvider data) {
    try {
      Integer num1 = data.consumeInt();
      Integer num2 = data.consumeInt();
      byte[] byteArray = data.consumeRemainingAsBytes();

      Serialization.readDEROctetString(byteArray);
      Serialization.readList(byteArray, num1, num2);
      Serialization.readVariableBytes(new ByteArrayInputStream(byteArray), num1);
      Serialization.readFixedBytes(new ByteArrayInputStream(byteArray), num1);
      Serialization.readNumber(new ByteArrayInputStream(byteArray), num1);
      Serialization.readLong(new ByteArrayInputStream(byteArray), num1);
      Serialization.readByte(new ByteArrayInputStream(byteArray));
      Serialization.writeVariableBytes(new ByteArrayOutputStream(), byteArray, num1);
      Serialization.writeFixedBytes(new ByteArrayOutputStream(), byteArray);
      Serialization.writeNumber(new ByteArrayOutputStream(), num1, num2);
    } catch (SerializationException e) {
    }
  }
}
