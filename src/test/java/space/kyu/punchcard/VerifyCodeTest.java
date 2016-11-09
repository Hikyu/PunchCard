package space.kyu.punchcard;

import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

import space.kyu.punchcard.code.CodeIdentity;
import space.kyu.punchcard.code.VerifyCode;
import space.kyu.punchcard.net.ServerOperation;

public class VerifyCodeTest {
	private static String testCode = ".\\resource\\test\\code.jpg";
	private static String oriPath = ".\\resource\\verifycode\\origin";
	private static String trainPath = ".\\resource\\verifycode\\train";

	@Test
	public void testGetCode() {
		try {
			byte[] bs = ServerOperation.getVerifyCode();
			File file = new File(testCode);
			if (!(file.exists() && file.isFile())) {
				file.createNewFile();
			}
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
			outputStream.write(bs);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testTrainCode() {
		try {
			CodeIdentity.trainData(oriPath, trainPath);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testIdentityCode() {
		testGetCode();
		try {
			// BufferedInputStream stream = new BufferedInputStream(new
			// FileInputStream(file));
			// byte[] bytes = new byte[(int) file.length()];
			// stream.read(bytes);
			String result = CodeIdentity.getIdentityResult(testCode, trainPath);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
