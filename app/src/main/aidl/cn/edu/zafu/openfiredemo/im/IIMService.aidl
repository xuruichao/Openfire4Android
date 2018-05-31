// IIMService.aidl
package cn.edu.zafu.openfiredemo.im;
// Declare any non-default types here with import statements

interface IIMService {

    void createConnection();

    void login(String account, String password);

    void register(String account, String password);

    void startRecord();

    void stopRecord();

    void cancelRecord();

    void listenRecord(String filePath);

    void sendMessage(String targetAddress, String content);

    void addConnectionListener();

    void addChatListener();

    void disconnect();

    boolean isConnected();

}
