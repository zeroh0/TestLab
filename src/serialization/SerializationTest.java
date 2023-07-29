package serialization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import serialization.obj.Color;

import java.io.*;
import java.util.Base64;

import static org.assertj.core.api.Assertions.*;

public class SerializationTest {

	@DisplayName("객체의 직렬화 및 역직렬화")
	@Test
	void binarySerialization() throws IOException, ClassNotFoundException {
		// Serializable 인터페이스 상속받은 객체
		Color writeObject = new Color(255, 100, 50);

		// Serialization
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(writeObject);
		byte[] serializedObject = baos.toByteArray();
		String encodeBase64 = Base64.getEncoder().encodeToString(serializedObject);

		oos.close();
		baos.close();

		// Deserialization
		byte[] decodeBase64 = Base64.getDecoder().decode(encodeBase64);
		ByteArrayInputStream bais = new ByteArrayInputStream(decodeBase64);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Color readObject = (Color) ois.readObject();

		ois.close();
		bais.close();

		assertThat(writeObject.getRed()).isEqualTo(readObject.getRed());
		assertThat(writeObject.getGreen()).isEqualTo(readObject.getGreen());
		assertThat(writeObject.getBlue()).isEqualTo(readObject.getBlue());
	}

}
