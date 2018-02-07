package space.kyu.punchcard.code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * 验证码识别 
 * 
 * @author kyu
 */
public class CodeIdentity {
	private static Map<BufferedImage, String> trainMap = null;
	public static int PIC_NO = 0;

	private static int isBlack(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 10) {
			return 1;
		}
		return 0;
	}

	private static int isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() > 500) {
			return 1;
		}
		return 0;
	}

	private static BufferedImage removeBackgroud(String picFile) throws Exception {
		BufferedImage img = ImageIO.read(new File(picFile));
		img = img.getSubimage(1, 1, img.getWidth() - 2, img.getHeight() - 2);
		int width = img.getWidth();
		int height = img.getHeight();
		double subWidth = (double) width / 5.0;
		for (int i = 0; i < 5; i++) {
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (int x = (int) (1 + i * subWidth); x < (i + 1) * subWidth && x < width - 1; ++x) {
				for (int y = 0; y < height; ++y) {
					if (isWhite(img.getRGB(x, y)) == 1)
						continue;
					if (map.containsKey(img.getRGB(x, y))) {
						map.put(img.getRGB(x, y), map.get(img.getRGB(x, y)) + 1);
					} else {
						map.put(img.getRGB(x, y), 1);
					}
				}
			}
			int max = 0;
			int colorMax = 0;
			for (Integer color : map.keySet()) {
				if (max < map.get(color)) {
					max = map.get(color);
					colorMax = color;
				}
			}
			for (int x = (int) (1 + i * subWidth); x < (i + 1) * subWidth && x < width - 1; ++x) {
				for (int y = 0; y < height; ++y) {
					if (img.getRGB(x, y) != colorMax) {
						img.setRGB(x, y, Color.WHITE.getRGB());
					} else {
						img.setRGB(x, y, Color.BLACK.getRGB());
					}
				}
			}
		}
		return img;
	}

	private static BufferedImage removeBlank(BufferedImage img) throws Exception {
		int width = img.getWidth();
		int height = img.getHeight();
		int start = 0;
		int end = 0;
		Label1: for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				if (isBlack(img.getRGB(x, y)) == 1) {
					start = y;
					break Label1;
				}
			}
		}
		Label2: for (int y = height - 1; y >= 0; --y) {
			for (int x = 0; x < width; ++x) {
				if (isBlack(img.getRGB(x, y)) == 1) {
					end = y;
					break Label2;
				}
			}
		}
		return img.getSubimage(0, start, width, end - start + 1);
	}

	private static List<BufferedImage> splitImage(BufferedImage img) throws Exception {
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
		int width = img.getWidth();
		int height = img.getHeight();
		List<Integer> weightlist = new ArrayList<Integer>();
		for (int x = 0; x < width; ++x) {
			int count = 0;
			for (int y = 0; y < height; ++y) {
				if (isBlack(img.getRGB(x, y)) == 1) {
					count++;
				}
			}
			weightlist.add(count);
		}
		for (int i = 0; i < weightlist.size(); i++) {
			int length = 0;
			while (i < weightlist.size() && weightlist.get(i) > 1) {
				i++;
				length++;
			}
			if (length > 2) {
				subImgs.add(removeBlank(img.getSubimage(i - length, 0, length, height)));
			}
		}
		return subImgs;
	}

	private static Map<BufferedImage, String> loadTrainData(String trainPath) throws Exception {
		if (trainMap == null) {
			Map<BufferedImage, String> map = new LinkedHashMap<BufferedImage, String>();
			File dir = new File(trainPath);
			File[] files = dir.listFiles();
			for (File file : files) {
				map.put(ImageIO.read(file), file.getName().charAt(0) + "");
			}
			trainMap = map;
		}
		return trainMap;
	}

	private static String getSingleCharOcr(BufferedImage img, Map<BufferedImage, String> map) {
		String result = "#";
		int width = img.getWidth();
		int height = img.getHeight();
		// 相同的像素点个数
		int minIdentical = 0;
		for (BufferedImage bi : map.keySet()) {
			int count = 0;
			if (Math.abs(bi.getWidth() - width) > 1)
				continue;
			int widthmin = Math.min(width, bi.getWidth());
			int heightmin = Math.min(height, bi.getHeight());
			for (int x = 0; x < widthmin; ++x) {
				for (int y = 0; y < heightmin; ++y) {
					if (isBlack(img.getRGB(x, y)) == isBlack(bi.getRGB(x, y))) {
						count++;
					}
				}
			}
			if (count > minIdentical) {
				// 选出相同色素个数最多的train image
				minIdentical = count;
				result = map.get(bi);
			}
		}
		return result;
	}

	/**
	 * 获取验证码解析结果
	 * 
	 * @param codePath
	 *            验证码存放路径
	 * @param trainPath
	 *            训练样本存放路径
	 * @return 解析结果
	 * @throws Exception
	 */
	public static String getIdentityResult(String codePath, String trainPath) throws Exception {
		String result = "";
		try {
			BufferedImage img = removeBackgroud(codePath);
			List<BufferedImage> listImg = splitImage(img);
			Map<BufferedImage, String> map = loadTrainData(trainPath);
			for (BufferedImage bi : listImg) {
				result += getSingleCharOcr(bi, map);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

	/**
	 * 生成训练样本
	 * 
	 * @param oriPath
	 *            源验证码样本存放路径
	 * @param trainPath
	 *            训练样本存放路径
	 * @throws Exception
	 */
	public static void trainData(String oriPath, String trainPath) throws Exception {
		clearTrainDir(trainPath);
		File dir = new File(oriPath);
		File[] files = dir.listFiles();
		for (File file : files) {
			BufferedImage img = removeBackgroud(oriPath + File.separatorChar + file.getName());
			List<BufferedImage> listImg = splitImage(img);
			if (listImg.size() == 4) {
				for (int j = 0; j < listImg.size(); ++j) {
					StringBuilder picName = new StringBuilder();
					picName.append(trainPath).append(File.separatorChar).append(file.getName().charAt(j)).append("-")
							.append(getPicNo()).append(".jpg");
					ImageIO.write(listImg.get(j), "JPG", new File(picName.toString()));
				}
			}
		}
	}

	private static boolean clearTrainDir(String trainPath) {
		File trainDir = new File(trainPath);
		if (trainDir.isDirectory()) {
			File[] file = trainDir.listFiles();
			for (int i = 0; i < file.length; i++) {
				boolean success = file[i].delete();
				if (!success) {
					return false;
				}
			}
		}
		return true;
	}

	public static int getPicNo() {
		return ++PIC_NO;
	}

}
