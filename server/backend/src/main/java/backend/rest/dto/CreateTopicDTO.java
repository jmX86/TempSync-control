package backend.rest.dto;

public class CreateTopicDTO {
    // name, device, dir, data
    private String topicName;
    private String topicDevice;
    private Long topicDir;
    private Long topicType;

    public CreateTopicDTO(String topicName, String topicDevice, Long topicDir, Long topicType) {
        this.topicName = topicName;
        this.topicDevice = topicDevice;
        this.topicDir = topicDir;
        this.topicType = topicType;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDevice() {
        return topicDevice;
    }

    public void setTopicDevice(String topicDevice) {
        this.topicDevice = topicDevice;
    }

    public Long getTopicDir() {
        return topicDir;
    }

    public void setTopicDir(Long topicDir) {
        this.topicDir = topicDir;
    }

    public Long getTopicType() {
        return topicType;
    }

    public void setTopicType(Long topicType) {
        this.topicType = topicType;
    }
}
