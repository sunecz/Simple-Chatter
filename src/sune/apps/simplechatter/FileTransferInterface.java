package sune.apps.simplechatter;

public interface FileTransferInterface
{
	public void beginTransfer(FileTransfer transfer);
	public void readBytes(FileTransfer transfer, FileTransfer.BytesInfo bytesInfo);
	public void canceled(FileTransfer transfer);
	public void paused(FileTransfer transfer);
	public void proceed(FileTransfer transfer);
	public void completed(FileTransfer transfer);
}