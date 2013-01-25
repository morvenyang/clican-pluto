import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String url = args[0];
		String file = args[1];
		String proxyHost = null;
		String proxyPort = null;
		if (args.length > 2) {
			proxyHost = args[2];
			proxyPort = args[3];
		}

		HttpURLConnection conn = null;
		if (args.length > 2) {
			conn = (HttpURLConnection) new URL(url).openConnection(new Proxy(
					Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer
							.parseInt(proxyPort))));
		} else {
			conn = (HttpURLConnection) new URL(url).openConnection();
		}
		conn.connect();
		InputStream is = conn.getInputStream();
		OutputStream os = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int read = -1;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
		is.close();
		os.close();
	}
}
