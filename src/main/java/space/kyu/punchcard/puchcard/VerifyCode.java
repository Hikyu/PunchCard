package space.kyu.punchcard.puchcard;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import space.kyu.punchcard.util.Constants;
import space.kyu.punchcard.util.HttpClientUtil;

/**
 * 验证码的获取以及处理
 * 
 * @author kyu
 *
 */
public class VerifyCode {
	private static int BUFFER_SIZE = 1 * 1024 * 1024;
	private static String verifyCodePath = ".\\resource\\verifycode\\verifyCode.jpg";
	private static String trainPath = ".\\resource\\verifycode\\train";
	private static String oriPath = ".\\resource\\verifycode\\origin";

	public static String getCodeIdentityRes() {
		try {
			storeVerifyCode(getVerifyCode());
			checkTrainFileExist();
			return CodeIdentity.getIdentityResult(verifyCodePath, trainPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static void checkTrainFileExist() throws Exception {
		File train = new File(trainPath);
		boolean trainDirExist = train.exists() && train.isDirectory();
		boolean trainFileExist = train.listFiles() != null && train.listFiles().length > 0;
		if (!(trainDirExist && trainFileExist)) {
			CodeIdentity.trainData(oriPath, trainPath);
		}
	}

	private static void storeVerifyCode(byte[] bs) throws IOException {
		File file = new File(verifyCodePath);
		if (!(file.exists() && file.isFile())) {
			file.createNewFile();
		}
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
		outputStream.write(bs);
		outputStream.flush();
		outputStream.close();
	}

	public static byte[] getVerifyCode() throws Exception {
		URI uri = new URIBuilder().setScheme(Constants.SCHEME).setHost(Constants.HOST)
				.setPath(Constants.VEARIFY_CODE_PATH).build();

		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("User-Agent", Constants.User_Agent);
		httpGet.setHeader("Referer", Constants.VEARIFY_CODE_REFERER_PATH);
		httpGet.setHeader("Cookie", Constants.getCookie());
		return getCodeImg(HttpClientUtil.doGet(httpGet));
	}

	private static byte[] getCodeImg(HttpResponse response) throws Exception {
		InputStream content = HttpClientUtil.getStreamFomeResp(response);
		byte[] img = null;

		byte[] buffer = new byte[BUFFER_SIZE];
		int size = 0;
		int totalLength = 0;
		do {
			size = content.read(buffer, 0, BUFFER_SIZE);
			if (size == -1) {
				break;
			}
			if (size < BUFFER_SIZE) {
				img = new byte[totalLength + size];
				System.arraycopy(buffer, 0, img, totalLength, size);
				totalLength += size;
			} else if (size == BUFFER_SIZE) {
				img = new byte[totalLength + BUFFER_SIZE];
				System.arraycopy(buffer, 0, img, totalLength, BUFFER_SIZE);
				totalLength += BUFFER_SIZE;
			}
		} while (size != -1);

		return img;
	}

}
