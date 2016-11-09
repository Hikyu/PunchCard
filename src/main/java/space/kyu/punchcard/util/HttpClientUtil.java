package space.kyu.punchcard.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
/**
 * 网络请求工具类
 * @author kyu
 *
 */
public class HttpClientUtil {
	private static HttpClient httpClient;
	static {
		httpClient = HttpClients.createDefault();
	}

	public static HttpResponse doPost(HttpPost post) throws Exception {
		post.setHeader("Accept-Encoding", "gzip");
		HttpResponse response = httpClient.execute(post);
		return response;
	}

	public static HttpResponse doGet(HttpGet get) throws Exception {
		get.setHeader("Accept-Encoding", "gzip");
		HttpResponse response = httpClient.execute(get);
		return response;
	}

	public static InputStream getStreamFomeResp(HttpResponse response) {
		HttpEntity responseContent = response.getEntity();
		InputStream content = null;
		if (responseContent != null) {
			try {
				content = getStreamFromGZIP(responseContent.getContent());
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}
	
	private static InputStream getStreamFromGZIP(InputStream is) {
		InputStream stream = null;
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			bis.mark(2);
			// 取前两个字节
			byte[] header = new byte[2];
			int result = bis.read(header);
			// reset 输入流到开始位置
			bis.reset();
			// 判断是否是 GZIP 格式
			int headerData = getShort(header);
			// Gzip 流 的前两个字节是 0x1f8b
			if (result != -1 && headerData == 0x1f8b) {
				stream = new GZIPInputStream(bis);
			} else {
				stream = bis;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stream;
	}
	
	private static int getShort(byte[] data) {
		return (data[0] << 8) | data[1] & 0xFF;
	}

	public static String printRespContent(HttpResponse response) throws Exception {
		InputStream content = getStreamFomeResp(response);
		BufferedReader reader = new BufferedReader(new InputStreamReader(content, "GBK"));
		StringBuilder sBuilder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sBuilder.append(line);
//			System.out.println(line);
		}
		return sBuilder.toString();
	}

}
