package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Builder
@Accessors(chain = true)
@Data
public class AlarmMessage implements Serializable {
    private String degree;
    private String title;
    private String occurrenceTime;
    private String occurrencePlace;
    private String describe;
    private String confirmButton;
    private String confirmButtonUrl;
    private String messageSource;
    private String url;

}
