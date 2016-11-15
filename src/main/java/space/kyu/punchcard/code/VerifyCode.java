package space.kyu.punchcard.code;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import space.kyu.punchcard.net.ServerOperation;

/**
 * 验证码的获取以及处理
 * 
 * @author kyu
 * 2016-11-07
 */
public class VerifyCode {
	private static String verifyCodePath = ".\\resource\\verifycode\\verifyCode.jpg";
	private static String trainPath = ".\\resource\\verifycode\\train";
	private static String oriPath = ".\\resource\\verifycode\\origin";

	public static String getCodeIdentityRes() throws Exception {
		try {
			storeCode(ServerOperation.getVerifyCode(), verifyCodePath);
			checkTrainFileExist();
			String identityResult = CodeIdentity.getIdentityResult(verifyCodePath, trainPath);
			return identityResult;
		} catch (Exception e) {
			return "";
		}
	}

	private static void checkTrainFileExist() throws Exception {
		File train = new File(trainPath);
		boolean trainDirExist = train.exists() && train.isDirectory();
		boolean trainFileExist = train.listFiles() != null && train.listFiles().length > 0;
		if (!(trainDirExist && trainFileExist)) {
			CodeIdentity.trainData(oriPath, trainPath);
		}
	}

	private static void storeCode(byte[] bs, String path) throws IOException {
		File file = new File(path);
		if (!(file.exists() && file.isFile())) {
			file.createNewFile();
		}
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
		outputStream.write(bs);
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 将当前的验证码图片存入源验证码库
	 * 并重新生成训练库
	 * @param codeName
	 *            验证码名称
	 */
	public static void storeCode2TrainDir(String codeName) {
		try {
			File file = new File(verifyCodePath);
			BufferedInputStream iStream = new BufferedInputStream(new FileInputStream(file));
			byte[] bs = new byte[(int) file.length()];
			iStream.read(bs);
			String path = oriPath + File.separator + codeName + ".jpg";
			storeCode(bs, path);
			CodeIdentity.trainData(oriPath, trainPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
