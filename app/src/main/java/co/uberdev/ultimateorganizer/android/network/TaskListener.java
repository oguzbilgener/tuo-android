package co.uberdev.ultimateorganizer.android.network;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public interface TaskListener
{
	public void onPreExecute();
	public void onPostExecute(Integer result, Object data);
}
