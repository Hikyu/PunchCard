package space.kyu.punchcard.code;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import space.kyu.punchcard.net.ServerOperation;
import space.kyu.punchcard.util.Config;

/**
 * 验证码的获取以及处理
 * 
 * @author kyu
 */
public class VerifyCode {
	public static String getCodeIdentityRes() throws Exception {
		try {
			storeCode(ServerOperation.getVerifyCode(), Config.VERIFY_CODE_PATH);
			checkTrainFileExist();
			String identityResult = CodeIdentity.getIdentityResult(Config.VERIFY_CODE_PATH, Config.TRAIN_PATH);
			return identityResult;
		} catch (Exception e) {
			return "";
		}
	}

	private static void checkTrainFileExist() throws Exception {
		File train = new File(Config.TRAIN_PATH);
		boolean trainDirExist = train.exists() && train.isDirectory();
		boolean trainFileExist = train.listFiles() != null && train.listFiles().length > 0;
		if (!(trainDirExist && trainFileExist)) {
			CodeIdentity.trainData(Config.ORIGIN_PATH, Config.TRAIN_PATH);
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
			File file = new File(Config.VERIFY_CODE_PATH);
			BufferedInputStream iStream = new BufferedInputStream(new FileInputStream(file));
			byte[] bs = new byte[(int) file.length()];
			iStream.read(bs);
			String path = Config.ORIGIN_PATH + File.separator + codeName + ".jpg";
			storeCode(bs, path);
			CodeIdentity.trainData(Config.ORIGIN_PATH, Config.TRAIN_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
