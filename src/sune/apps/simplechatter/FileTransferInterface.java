package sune.apps.simplechatter;

public interface FileTransferInterface
{
	public void beginTransfer(String UID);
	public void readBytes(FileTransfer.BytesInfo bytesInfo);
	public void canceled(String UID);
	public void paused(String UID);
	public void proceed(String UID);
	public void completed(String UID);
}