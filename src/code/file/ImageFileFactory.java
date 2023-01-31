package code.file;

import code.constant.ImageSize;
import code.constant.ProductType;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageFileFactory extends FileFactory {

	/**
	 * assign file by use path to group file
	 *
	 * @param path
	 * 		the group of picture file and images file
	 */
	public ImageFileFactory(String path) {
		super(path);
	}

	public void setSize(ImageSize size) {
		setFileBy(size.getName());
	}

	public void setName(ProductType name) {
		setFileBy(name.getName());
	}

	private void setFileBy(String filter) {
		File[] files = getFile().listFiles();
		if (files != null) {
			for (File folder : files) {
				if (folder.getName().equalsIgnoreCase(filter)) {
					setFile(folder);
				}
			}
		}
	}

	public String[] getAllImagePath() {
		File[] images = getFile().listFiles();
		if (images != null) {
			String[] paths = new String[images.length];
			for (int i = 0; i < paths.length; i++) {
				paths[i] = images[i].toPath().toString();
				System.out.println("URL: " + images[i].toURI().toString());
				System.out.println(paths[i]);
			}
			return paths;
		}
		return new String[0];
	}

	public URL[] getAllImageURL() {
		File[] images = getFile().listFiles();
		if (images != null) {
			URL[] urls = new URL[images.length];
			for (int i = 0; i < urls.length; i++) {
				try {
					urls[i] = images[i].toURI().toURL();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			return urls;
		}
		return new URL[0];
	}
}
